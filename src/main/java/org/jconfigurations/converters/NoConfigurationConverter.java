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
import org.jconfigurations.functions.DefaultTypeConverterFunction;

/**
 * <pre>
 * Special {@link ConfigurationConverter} that always throws a {@link ConfigurationException}
 * when its {@link #convert(java.lang.String)} method is called.
 * Although it is assigned by default to {@link Configuration#converter()}, the framework
 * attempts to locate a suitable converter before falling back to this one.
 * Suitable converters are found either by the user explicitely specifying a different 
 * one via {@link Configuration#converter()}, or by using the {@link DefaultTypeConverterFunction}.
 * </pre>
 * 
 * @author George Aristy
 */
public class NoConfigurationConverter implements ConfigurationConverter<Void> {

  @Override
  public Void convert(String value) throws ConfigurationException {
    throw new ConfigurationException(
            String.format("No configuration converter found to convert value '%s'", value)
    );
  }
}