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
package org.jconfigurations.configurators;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.stream.Stream;
import org.jconfigurations.CollectionConfiguration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.converters.CollectionConfigurationConverter;
import org.jconfigurations.source.ConfigurationSource;
import org.jconfigurations.util.ErrorFunction;
import static java.util.Objects.requireNonNull;
import org.jconfigurations.Name;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.functions.CollectionFieldConverterFunction;
import org.jconfigurations.functions.DefaultCollectionTypeConverterFunction;
import org.jconfigurations.functions.DefaultFieldNameFunction;

/**
 * The {@link CollectionConfigurator} will target all {@link CollectionConfiguration}
 * fields of an object. 
 * 
 * Unless specified otherwise, the {@link CollectionConfigurator} will use the 
 * {@link DefaultCollectionTypeConverterFunction} to {@link CollectionConfigurationConverter convert}
 * configuration values into instances of the appropriate types before assigning
 * them to fields.
 *
 * @author George Aristy
 * @see CollectionFieldConverterFunction
 * @see DefaultCollectionTypeConverterFunction
 */
public class CollectionConfigurator implements Configurator {
  private final ConfigurationSource source;
  private final Configurator configurator;
  private final ErrorFunction<Field, String> fieldNameFunction;
  private final ErrorFunction<Field, CollectionConfigurationConverter> fieldConverterFunction;

  /**
   * Fully customizable constructor that allows the user to chain link another 
   * {@link Configurator configurator} in order to compose a multi-featured chain.
   * 
   * @param source the {@link ConfigurationSource} that provides the configurations
   * @param configurator another {@link Configurator} to which this instance will hand off the 
   * target {@code object} to once this {@link CollectionConfigurator} has finished 
   * {@link #configure(java.lang.Object) configuring} the {@code object}.
   * @param fieldNameFunction the {@link ErrorFunction function} used to produce the field's name (see {@link Name}).
   * @param fieldConverterFunction the {@link ErrorFunction function} used to produce an instance of the 
   * appropriate {@link ConfigurationConverter converter} for a given field.
   * @throws NullPointerException if any of the inputs is {@code null}.
   */
  public CollectionConfigurator(
          ConfigurationSource source, 
          Configurator configurator,
          ErrorFunction<Field, String> fieldNameFunction, 
          ErrorFunction<Field, CollectionConfigurationConverter> fieldConverterFunction
  ) {
    this.source = requireNonNull(source, "null source");
    this.configurator = requireNonNull(configurator, "null configurator");
    this.fieldNameFunction = requireNonNull(fieldNameFunction, "null fieldNameFunction");
    this.fieldConverterFunction = requireNonNull(fieldConverterFunction, "null fieldConverterFunction");
  }

  /**
   * Defaults to using the {@link CollectionFieldConverterFunction} to 
   * {@link ConfigurationConverter#convert(java.lang.String) convert} configuration
   * values before assignment.
   * 
   * @param source
   * @param configurator
   * @param fieldNameFunction 
   * @throws NullPointerException if any of the inputs is {@code null}.
   * @see #CollectionConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator, org.jconfigurations.util.ErrorFunction, org.jconfigurations.util.ErrorFunction) 
   */
  public CollectionConfigurator(
        ConfigurationSource source,
        Configurator configurator,
        ErrorFunction<Field, String> fieldNameFunction
  ) {
    this(source, configurator, fieldNameFunction, new CollectionFieldConverterFunction());
  }

  /**
   * Defaults to using the {@link DefaultFieldNameFunction} to resolve the field's
   * {@link Name name}.
   * 
   * @param source
   * @param configurator 
   * @throws NullPointerException if any of the inputs is {@code null}.
   * @see #CollectionConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator, org.jconfigurations.util.ErrorFunction) 
   */
  public CollectionConfigurator(ConfigurationSource source, Configurator configurator){
    this(source, configurator, new DefaultFieldNameFunction());
  }

  /**
   * Defaults to using the {@link NoOpConfigurator} as the next link in the 
   * {@link Configurator} chain.
   * 
   * @param source 
   * @throws NullPointerException if {@code source} is {@code null}.
   * @see #CollectionConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator) 
   */
  public CollectionConfigurator(ConfigurationSource source){
    this(source, new NoOpConfigurator());
  }

  @Override
  public void configure(Object object) throws ConfigurationException {
    requireNonNull(object, "null object");

    Iterator<Field> iter = Stream.of(object.getClass().getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(CollectionConfiguration.class))
            .map(f -> {f.setAccessible(true); return f;})
            .iterator();

    while(iter.hasNext()){
      Field field = iter.next();
      final String propertyName = fieldNameFunction.apply(field);

      if(source.configurations().containsKey(propertyName)){
        CollectionConfigurationConverter converter = fieldConverterFunction.apply(field);

        try{
          field.set(object, converter.convert(source.configurations().get(propertyName)));
        }catch(IllegalArgumentException | IllegalAccessException e){
          throw new ConfigurationException(
                  String.format(
                          "Unable to configure field '%s' of type '%s' in object of class '%s'",
                          field.getName(), 
                          field.getType().getName(), 
                          field.getDeclaringClass().getName()
                  ), 
                  e
          );
        }
      }
    }

    configurator.configure(object);
  }
}