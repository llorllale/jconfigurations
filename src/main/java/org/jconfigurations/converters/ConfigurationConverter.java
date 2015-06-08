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

import org.jconfigurations.ConfigurationException;

/**
 * Converts a configuration value into the appropriate type {@code T} given it's string {@link #convert(java.lang.String) value}.<br>
 * User-specific implementations of this interface must provide an accessible no-arg constructor.
 * @author George Aristy
 * @param <T>
 */
public interface ConfigurationConverter<T> {
  public T convert(String value) throws ConfigurationException;
}
