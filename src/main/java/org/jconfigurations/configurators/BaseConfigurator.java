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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;
import static java.util.Objects.requireNonNull;
import java.util.stream.Stream;
import org.jconfigurations.CollectionConfiguration;
import org.jconfigurations.Configuration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.MapConfiguration;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.source.ConfigurationSource;
import org.jconfigurations.util.ErrorFunction;

/**
 * Handy abstract class that does the heavy-lifting for {@link #configure(java.lang.Object)}.
 * <br><br>
 * Note that this class does not check the annotations provided in its 
 * {@link #BaseConfigurator(java.lang.Class, org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator, org.jconfigurations.util.ErrorFunction, org.jconfigurations.util.ErrorFunction) constructor}.
 * The annotation provided is merely used to filter fields pertaining to the input
 * {@link #configure(java.lang.Object) object}.
 *
 * @author George Aristy
 */
public abstract class BaseConfigurator implements Configurator {
  protected final Class<? extends Annotation> annotation;
  protected final ConfigurationSource source;
  protected final Configurator configurator;
  protected final ErrorFunction<Field, String> fieldNameFunction;
  protected final ErrorFunction<Field, ? extends ConfigurationConverter> fieldConverterFunction;

  /**
   * 
   * @param annotation one of the framework's provided annotations 
   * (see {@link Configuration}, {@link CollectionConfiguration}, {@link MapConfiguration}).
   * @param source
   * @param configurator
   * @param fieldNameFunction
   * @param fieldConverterFunction 
   * @throws NullPointerException if any of the parameters are {@code null}.
   */
  protected BaseConfigurator(
          Class<? extends Annotation> annotation, 
          ConfigurationSource source, 
          Configurator configurator,
          ErrorFunction<Field, String> fieldNameFunction, 
          ErrorFunction<Field, ? extends ConfigurationConverter> fieldConverterFunction
  ) {
    this.annotation = requireNonNull(annotation, "null annotation");
    this.source = requireNonNull(source, "null source");
    this.configurator = requireNonNull(configurator, "null configurator");
    this.fieldNameFunction = requireNonNull(fieldNameFunction, "null fieldNameFunction");
    this.fieldConverterFunction = requireNonNull(fieldConverterFunction, "null fieldConverterFunction");
  }

  @Override
  public void configure(Object object) throws ConfigurationException {
    requireNonNull(object, "null object.");

    Iterator<Field> iter = Stream.of(object.getClass().getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(annotation))
            .map(f -> {f.setAccessible(true); return f;})
            .iterator();

    while(iter.hasNext()){
      Field field = iter.next();
      final String propertyName = fieldNameFunction.apply(field);

      if(source.configurations().containsKey(propertyName)){
        final String propertyValue = source.configurations().get(propertyName);
        final ConfigurationConverter converter = fieldConverterFunction.apply(field);
    
        try{
          field.set(object, converter.convert(propertyValue));
        }catch(IllegalArgumentException | IllegalAccessException | ConfigurationException e){
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