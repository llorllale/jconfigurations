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

import java.util.HashMap;

/**
 * A {@link MapConfigurationConverter} that converts a string value into
 * an initialized {@link HashMap}.
 *
 * @author George Aristy
 * @param <K>
 * @param <V>
 */
public class HashMapConfigurationConverter<K, V> extends BaseMapConfigurationConverter<K, V> {
  
  /**
   * 
   * @param keyConverterClass
   * @param valueConverterClass
   * @param entryDelimiter
   * @param keyValueSeparator 
   * @throws NullPointerException if any of the parameters is {@code null}.
   * @see BaseMapConfigurationConverter#BaseMapConfigurationConverter(org.jconfigurations.converters.ConfigurationConverter, org.jconfigurations.converters.ConfigurationConverter, java.lang.String, java.lang.String, java.lang.Class) 
   */
  public HashMapConfigurationConverter(
          ConfigurationConverter<K> keyConverterClass, 
          ConfigurationConverter<V> valueConverterClass, 
          String entryDelimiter, 
          String keyValueSeparator
  ) {
    super(keyConverterClass, valueConverterClass, entryDelimiter, keyValueSeparator, HashMap.class);
  }
}