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
package org.jconfigurations.source;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.Properties;

/**
 * <pre>
 * Handy helper class that wraps up a {@link Properties} into a {@link ConfigurationSource}.
 * </pre>
 * @author George Aristy
 */
public class PropertiesConfigurationSource implements ConfigurationSource {
  private final Map<String, String> configurations;

  /**
   * 
   * @param properties 
   * @throws NullPointerException if {@code properties} is {@code null}.
   */
  public PropertiesConfigurationSource(Properties properties){
    Map<String, String> tmp = new HashMap<>();

    requireNonNull(properties, "null properties").stringPropertyNames()
            .forEach(
                    k -> tmp.put(k, properties.getProperty(k))
            );

    configurations = Collections.unmodifiableMap(tmp);
  }

  @Override
  public Map<String, String> configurations() {
    return configurations;
  }
}