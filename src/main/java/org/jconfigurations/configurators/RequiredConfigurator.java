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
import java.util.Objects;
import java.util.stream.Stream;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.Name;
import org.jconfigurations.source.ConfigurationSource;
import org.jconfigurations.Required;
import org.jconfigurations.functions.DefaultFieldNameFunction;
import org.jconfigurations.util.ErrorFunction;

/**
 * <pre>
 * A {@link Configurator} with the sole responsibility of making sure that 
 * a {@link Required required} field's configuration is present in the 
 * {@link ConfigurationSource}. 
 * If its configuration is not present then a {@link ConfigurationException} is thrown.
 * </pre>
 *
 * @author George Aristy
 * @see Required
 */
public class RequiredConfigurator implements Configurator {
  private final ConfigurationSource source;
  private final Configurator configurator;
  private final ErrorFunction<Field, String> fieldNameFunction;

  /**
   * 
   * @param source the {@link ConfigurationSource}
   * @param configurator a {@link Configurator} to form a chain of {@link Configurator}s.
   * @param fieldNameFunction a {@link ErrorFunction function} responsible for
   * resolving a field's {@link Name name}.
   * @throws NullPointerException if any of the parameters is {@code null}.
   */
  public RequiredConfigurator(
          ConfigurationSource source, 
          Configurator configurator, 
          ErrorFunction<Field, String> fieldNameFunction
  ) {
    this.source = Objects.requireNonNull(source, "null source.");
    this.configurator = Objects.requireNonNull(configurator, "null configurator.");
    this.fieldNameFunction = Objects.requireNonNull(fieldNameFunction, "null fieldNameFunction.");
  }

  /**
   * Defaults to the {@link DefaultFieldNameFunction}.
   * 
   * @param source the {@link ConfigurationSource}
   * @param configurator a {@link Configurator} to form a chain of {@link Configurator}s.
   * @throws NullPointerException if any of the parameters is {@code null}.
   * @see #RequiredConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator, org.jconfigurations.util.ErrorFunction) 
   */
  public RequiredConfigurator(ConfigurationSource source, Configurator configurator){
    this(source, configurator, new DefaultFieldNameFunction());
  }

  /**
   * Defaults to the {@link NoOpConfigurator} and the {@link DefaultFieldNameFunction}.
   * 
   * @param source the {@link ConfigurationSource}
   * @throws NullPointerException if {@code source} is {@code null}.
   * @see #RequiredConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator) 
   */
  public RequiredConfigurator(ConfigurationSource source){
    this(source, new NoOpConfigurator());
  }

  @Override
  public void configure(Object object) throws ConfigurationException {
    Objects.requireNonNull(object, "null object.");

    Iterator<Field> iter = Stream.of(object.getClass().getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(Required.class))
            .iterator();

    while(iter.hasNext()){
      Field f = iter.next();
      final String name = fieldNameFunction.apply(f);

      if(!source.configurations().containsKey(name)){
        throw new ConfigurationException(
                String.format(
                        "Required configuration not found for field '%s' in object of class '%s'",
                        f.getName(), 
                        object.getClass().getName()
                )
        );
      }
    }

    configurator.configure(object);
  }
}