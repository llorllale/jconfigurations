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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.function.Function;
import org.jconfigurations.CollectionConfiguration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.converters.CollectionConfigurationConverter;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.NoCollectionConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.util.ErrorFunction;
import org.jconfigurations.util.GenericTypesExtractor;

/**
 * The {@link CollectionFieldConverterFunction} is analogous to the {@link FieldConverterFunction}:
 * it analyzes the field's {@link CollectionConfiguration#converter()} value to
 * produce an instance of a suitable {@link CollectionConfigurationConverter}.
 * If {@link NoCollectionConfigurationConverter} is specified then it delegates to
 * a 'fallback' function.
 * 
 * {@link CollectionFieldConverterFunction} requires that implementations of 
 * {@link CollectionConfigurationConverter} provide an accessible 2-arg 
 * constructor for a {@link ConfigurationConverter} and the 
 * {@link CollectionConfiguration#delimiter() delimiter}.
 *
 * @author George Aristy
 * @see DefaultCollectionTypeConverterFunction
 */
public class CollectionFieldConverterFunction 
        implements ErrorFunction<Field, CollectionConfigurationConverter> 
{
  private final Function<Class<?>, Class<? extends ConfigurationConverter>> typeConverterFunction;
  private final Function<Class<?>, Class<? extends CollectionConfigurationConverter>> collectionTypeConverterFunction;

  /**
   * 
   * @param typeConverterFunction
   * @param defaultCollectionTypeConverterFunction 
   * @throws NullPointerException if any of the parameters is {@code null}.
   */
  public CollectionFieldConverterFunction(
          Function<Class<?>, Class<? extends ConfigurationConverter>> typeConverterFunction,
          Function<Class<?>, Class<? extends CollectionConfigurationConverter>> defaultCollectionTypeConverterFunction
  ) {
    this.typeConverterFunction = requireNonNull(typeConverterFunction, "null typeConverterFunction");
    this.collectionTypeConverterFunction = requireNonNull(defaultCollectionTypeConverterFunction, "null defaultCollectionTypeConverterFunction");
  }

  /**
   * Defaults to the {@link DefaultCollectionTypeConverterFunction} as the 
   * {@link ErrorFunction function} to use to produce instances of the appropriate
   * {@link CollectionConfigurationConverter}.
   * 
   * @param typeConverterFunction 
   * @throws NullPointerException if {@code typeConverterFunction} is {@code null}.
   */
  public CollectionFieldConverterFunction(Function<Class<?>, Class<? extends ConfigurationConverter>> typeConverterFunction){
    this(typeConverterFunction, new DefaultCollectionTypeConverterFunction());
  }

  /**
   * Defaults to the {@link DefaultTypeConverterFunction} as the {@link ErrorFunction 
   * function} to use to produce instances of the appropriate {@link ConfigurationConverter}
   * to convert the collection's individual element values, as well as the 
   * {@link DefaultCollectionTypeConverterFunction} in order to produce 
   * instances of the appropriate {@link CollectionConfigurationConverter} to
   * instantiate the appropriate implementation of the field's {@link Collection}
   * type.
   * 
   * @see CollectionConfiguration#elementConverter() 
   * @see CollectionConfiguration#converter() 
   */
  public CollectionFieldConverterFunction(){
    this(new DefaultTypeConverterFunction());
  }

  @Override
  public CollectionConfigurationConverter apply(Field field) throws ConfigurationException {
    if(!field.isAnnotationPresent(CollectionConfiguration.class)){
      return new NoCollectionConfigurationConverter();
    }else{
      CollectionConfiguration fieldConfig = field.getAnnotation(CollectionConfiguration.class);

      try{
        final Class<? extends CollectionConfigurationConverter> collectionConverterClass;
  
        if(!NoCollectionConfigurationConverter.class.equals(fieldConfig.converter())){
          collectionConverterClass = fieldConfig.converter();
        }else{
          collectionConverterClass = collectionTypeConverterFunction.apply(field.getType());
        }

        final Constructor<? extends CollectionConfigurationConverter> constructor 
                = collectionConverterClass.getConstructor(ConfigurationConverter.class, String.class);
        constructor.setAccessible(true);
  
        Class<?> genericType = new GenericTypesExtractor(field).getGenericTypes()
                .stream()
                .findFirst()
                .orElse(String.class);  //default to String.class in case of raw type

        Class<? extends ConfigurationConverter> elementConverterClass = null;
        final ConfigurationConverter elementConverter;

        try{
          if(NoConfigurationConverter.class.equals(fieldConfig.elementConverter())){
            elementConverterClass = typeConverterFunction.apply(genericType);
            elementConverter = elementConverterClass.newInstance();
          }else{
            elementConverterClass = fieldConfig.elementConverter();
            elementConverter = elementConverterClass.newInstance();
          }
        }catch(InstantiationException | IllegalAccessException e){
          throw new ConfigurationException(
                  String.format(
                          "Error creating an instance of elementConverter of type '%s' for use with collectionConverter of type '%s' for field '%s' in class '%s'",
                          elementConverterClass.getName(),
                          constructor.getDeclaringClass().getName(),
                          field.getName(),
                          field.getDeclaringClass().getName()
                  ),
                  e
          );
        }

        return constructor.newInstance(
                elementConverter,
                fieldConfig.delimiter()
        );
      }catch(NoSuchMethodException e){
        throw new ConfigurationException(
                String.format(
                        "CollectionConfigurationConverter of type %s for field %s of class %s does not have an accessible 2-arg constructor.",
                        field.getAnnotation(CollectionConfiguration.class).converter().getName(), 
                        field.getName(), 
                        field.getDeclaringClass().getName()
                ), 
                e
        );
      }catch(InvocationTargetException | IllegalAccessException | InstantiationException e){
        throw new ConfigurationException(
                String.format(
                        "Error instantiating CollectionConfigurationConverter of type %s for field %s of class %s",
                        field.getAnnotation(CollectionConfiguration.class).converter().getName(), 
                        field.getName(), 
                        field.getDeclaringClass().getName()
                ), 
                e
        );
      }
    }
  }
}