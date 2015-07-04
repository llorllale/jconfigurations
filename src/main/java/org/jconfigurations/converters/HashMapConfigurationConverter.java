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

import java.util.HashMap;
import org.jconfigurations.MapConfiguration;

/**
 * The default converter for {@link MapConfiguration map configurations}.<br><br>
 * 
 * This implementation will convert the configuration into a {@link HashMap}.
 * 
 * @author George Aristy
 * @param <K>
 * @param <V>
 */
public class HashMapConfigurationConverter<K, V> extends AbstractMapConfigurationConverter<K, V> {
  public HashMapConfigurationConverter(ConfigurationConverter<K> keyConverter, ConfigurationConverter<V> valueConverter) {
    super(keyConverter, valueConverter, HashMap.class);
  }

  public HashMapConfigurationConverter(ConfigurationConverter<K> keyConverter, ConfigurationConverter<V> valueConverter, String delimiter, String assignmentOp) {
    super(keyConverter, valueConverter, HashMap.class, delimiter, assignmentOp);
  }
}