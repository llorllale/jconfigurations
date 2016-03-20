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
import java.util.Map;
import java.util.Objects;

/**
 * <pre>
 * Handy helper class that wraps a {@link Map} into a {@link ConfigurationSource},
 * making sure that {@link #configurations()} returns an unmodifiable map.
 * </pre>
 * 
 * @author George Aristy
 */
public class MapConfigurationSource implements ConfigurationSource {
  private final Map<String, String> configurations;

  /**
   * 
   * @param configurations 
   * @throws NullPointerException if {@code configurations} is {@code null}.
   */
  public MapConfigurationSource(Map<String, String> configurations) {
    this.configurations = Collections.unmodifiableMap(
            Objects.requireNonNull(configurations, "null configurations")
    );
  }

  @Override
  public Map<String, String> configurations() {
    return configurations;
  }
}