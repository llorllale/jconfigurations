/* 
 * Copyright 2015 George Aristy.
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
package org.jconfigurations.converters;

import org.jconfigurations.Configuration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.configurators.BasicConfigurator;
import org.jconfigurations.configurators.Configurator;

/**
 * <pre>
 * {@code ConfigurationConverter}s are used by {@link Configurator configurators} to convert a configuration
 * value into an instance of type {@code T}.
 * Several default implementations for primitives and other common types are already provided.
 * Users may opt to implement their own converters and specify those via {@link Configuration#converter()}.
 * <b>The {@link BasicConfigurator} requires that implementations must provide 
 * an accessible no-arg constructor.</b>
 * </pre>
 * @author George Aristy
 * @param <T>
 * @see Configuration#converter() 
 * @see Configurator
 * @see Configuration
 */
@FunctionalInterface
public interface ConfigurationConverter<T> {
  
  /**
   * Converts {@code value} into an instance of type {@code T}.
   * @param value
   * @return
   * @throws ConfigurationException if {@code value} cannot be converted to an instance of type {@code T}.
   */
  public T convert(String value) throws ConfigurationException;
}