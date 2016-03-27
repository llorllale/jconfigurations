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
package org.jconfigurations.converters;

import java.util.Map;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.MapConfiguration;
import org.jconfigurations.functions.DefaultMapTypeConverterFunction;

/**
 * Special {@link MapConfigurationConverter} that always throws a 
 * {@link ConfigurationException} when its {@link #convert(java.lang.String)} 
 * method is called.
 * 
 * Although it is assigned by default to {@link MapConfiguration#converter()}, 
 * the framework attempts to locate a suitable converter before falling back 
 * to this one.
 * 
 * Suitable converters are found either by the user explicitely specifying a 
 * different one via {@link MapConfiguration#converter()}, or by using 
 * the {@link DefaultMapTypeConverterFunction}.
 *
 * @author George Aristy
 */
public class NoMapConfigurationConverter implements MapConfigurationConverter<Void, Void>{
  /**
   * 
   * @param keyConverter
   * @param valueConverter
   * @param entryDelimiter
   * @param keyValueSeparator 
   */
  public NoMapConfigurationConverter(
          ConfigurationConverter keyConverter, 
          ConfigurationConverter valueConverter,
          String entryDelimiter,
          String keyValueSeparator
  ){

  }

  /**
   * 
   */
  public NoMapConfigurationConverter(){
    this(null, null, null, null);
  }

  @Override
  public Map<Void, Void> convert(String value) throws ConfigurationException {
    throw new ConfigurationException(
            String.format(
                    "No map configuration converter found to convert value '%s'", 
                    value
            )
    );
  }
}