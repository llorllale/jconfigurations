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
package org.jconfigurations.configurators;

import org.jconfigurations.Configuration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.source.ConfigurationSource;

/**
 * <pre>
 * A {@link Configurator} assigns values to fields annotated with {@link Configuration} 
 * of any arbritrary object.
 * These values are (typically) picked up from a {@link ConfigurationSource} and 
 * {@link ConfigurationConverter converted} to an instance of the right type before 
 * assigning the result to the field.
 * Fields NOT annotated with {@link Configuration} are ignored.
 * </pre>
 * @author George Aristy
 */
public interface Configurator {
  /**
   * Assigns values to {@code object}'s fields annotated with the framework's 
   * annotations.
   * 
   * @param object any object
   * @throws ConfigurationException 
   * @throws NullPointerException if{@code object} is {@code null}.
   */
  public void configure(Object object) throws ConfigurationException;
}