/*
 * Copyright 2016 George Aristy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jconfigurations.functions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.MapConfiguration;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.MapConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.converters.NoMapConfigurationConverter;
import org.jconfigurations.util.ErrorFunction;
import org.jconfigurations.util.GenericTypesExtractor;
import static java.util.Objects.requireNonNull;

/**
 * The {@link MapFieldConverterFunction} is analogous to the {@link FieldConverterFunction}:
 * it analyzes the field's {@link MapConfiguration#converter()} value to
 * produce an instance of a suitable {@link MapConfigurationConverter}.
 * If {@link NoMapConfigurationConverter} is specified then it delegates to
 * a 'fallback' function.
 * 
 * {@link MapFieldConverterFunction} requires that implementations of 
 * {@link MapConfigurationConverter} provide an accessible 4-arg 
 * constructor (see {@link MapConfigurationConverter}).
 *
 * @author George Aristy
 * @see DefaultMapTypeConverterFunction
 */
public class MapFieldConverterFunction 
        implements ErrorFunction<Field, MapConfigurationConverter, ConfigurationException> 
{
  private final Function<Class<?>, Class<? extends ConfigurationConverter>> typeConverterFunction;
  private final Function<Class<?>, Class<? extends MapConfigurationConverter>> mapTypeConverterFunction;

  /**
   * 
   * @param typeConverterFunction
   * @param mapTypeConverterFunction 
   * @throws NullPointerException if any of the parameters is {@code null}.
   */
  public MapFieldConverterFunction(
          Function<Class<?>, Class<? extends ConfigurationConverter>> typeConverterFunction, 
          Function<Class<?>, Class<? extends MapConfigurationConverter>> mapTypeConverterFunction
  ) {
    this.typeConverterFunction = requireNonNull(typeConverterFunction, "null typeConverterFunction");
    this.mapTypeConverterFunction = requireNonNull(mapTypeConverterFunction, "null mapTypeConverterFunction");
  }

  /**
   * Defaults to the {@link DefaultMapTypeConverterFunction} function as the 
   * {@link ErrorFunction function} to use to produce instances of the appropriate
   * {@link MapConfigurationConverter}.
   * 
   * @param typeConverterFunction 
   * @throws NullPointerException if {@code typeConverterFunction} is {@code null}.
   * @see #MapFieldConverterFunction(java.util.function.Function, java.util.function.Function) 
   */
  public MapFieldConverterFunction(
          Function<Class<?>, Class<? extends ConfigurationConverter>> typeConverterFunction
  ){
    this(typeConverterFunction, new DefaultMapTypeConverterFunction());
  }

  /**
   * Defaults to the {@link DefaultTypeConverterFunction} as the {@link ErrorFunction 
   * function} to use to produce instances of the appropriate {@link ConfigurationConverter}
   * to convert the map's individual key/value element values, as well as the 
   * {@link DefaultMapTypeConverterFunction} in order to produce 
   * instances of the appropriate {@link MapConfigurationConverter} to
   * instantiate the appropriate implementation of the field's {@link Map}
   * type.
   * 
   * @see #MapFieldConverterFunction(java.util.function.Function) 
   */
  public MapFieldConverterFunction(){
    this(new DefaultTypeConverterFunction());
  }

  @Override
  public MapConfigurationConverter apply(Field field) throws ConfigurationException {
    if(!field.isAnnotationPresent(MapConfiguration.class)){
      return new NoMapConfigurationConverter();
    }else{
      final MapConfiguration fieldConfig = field.getAnnotation(MapConfiguration.class);
      final Class<? extends MapConfigurationConverter> mapConverterClass;

      if(!fieldConfig.converter().equals(NoMapConfigurationConverter.class)){
        mapConverterClass = fieldConfig.converter();
      }else{
        mapConverterClass = mapTypeConverterFunction.apply(field.getType());
      }

      final Class<? extends ConfigurationConverter> keyConverterClass;

      if(!fieldConfig.keyConverter().equals(NoConfigurationConverter.class)){
        keyConverterClass = fieldConfig.keyConverter();
      }else{
        keyConverterClass = typeConverterFunction.apply(
                new GenericTypesExtractor(field)
                        .getGenericTypes()
                        .stream()
                        .findFirst()
                        .orElse(String.class)
        );
      }

      final Class<? extends ConfigurationConverter> valueConverterClass;

      if(!fieldConfig.valueConverter().equals(NoConfigurationConverter.class)){
        valueConverterClass = fieldConfig.valueConverter();
      }else{
        final List<Class<?>> genericTypes = new GenericTypesExtractor(field)
                .getGenericTypes();

        if(genericTypes.size() > 1){
          valueConverterClass = typeConverterFunction.apply(genericTypes.get(1));
        }else{
          valueConverterClass = typeConverterFunction.apply(String.class);
        }
      }

      final ConfigurationConverter keyConverter = instantiate(keyConverterClass);
      final ConfigurationConverter valueConverter = instantiate(valueConverterClass);

      return instantiate(
              mapConverterClass, 
              keyConverter, 
              valueConverter, 
              fieldConfig.entryDelimiter(), 
              fieldConfig.keyValueSeparator()
      );
    }
  }

  private ConfigurationConverter instantiate(Class<? extends ConfigurationConverter> clazz) throws ConfigurationException {
    try{
      return clazz.newInstance();
    }catch(InstantiationException | IllegalAccessException e){
      throw new ConfigurationException(
              String.format(
                      "Error instantiating ConfigurationConverter class '%s'",
                      clazz.getName()
              ), 
              e
      );
    }
  }

  private MapConfigurationConverter instantiate(
          Class<? extends MapConfigurationConverter> mapConverterClass,
          ConfigurationConverter keyConverter,
          ConfigurationConverter valueConverter,
          String entryDelimiter,
          String keyValueSeparator
  ) throws ConfigurationException 
  {
    try{
      return mapConverterClass.getConstructor(
              ConfigurationConverter.class, 
              ConfigurationConverter.class, 
              String.class, 
              String.class
      ).newInstance(
              keyConverter,
              valueConverter,
              entryDelimiter,
              keyValueSeparator
      );
    }catch(NoSuchMethodException e){
      throw new ConfigurationException(
              String.format(
                      "MapConfigurationConverter class '%s' does not have an accessible 4-arg constructor with parameter types: '%s', '%s', '%s', '%s'",
                      mapConverterClass.getName(),
                      ConfigurationConverter.class.getName(),
                      ConfigurationConverter.class.getName(),
                      entryDelimiter.getClass().getName(),
                      keyValueSeparator.getClass().getName()
              ), 
              e
      );
    }catch(InstantiationException | IllegalAccessException | InvocationTargetException e){
      throw new ConfigurationException(
              String.format(
                      "Error instantiating MapConfigurationConverter class '%s'",
                      mapConverterClass.getName()
              ), 
              e
      );
    }
  }
}