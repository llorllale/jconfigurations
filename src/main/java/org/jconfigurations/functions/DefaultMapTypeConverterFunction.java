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
package org.jconfigurations.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.jconfigurations.converters.HashMapConfigurationConverter;
import org.jconfigurations.converters.MapConfigurationConverter;
import org.jconfigurations.converters.NoMapConfigurationConverter;

/**
 * Special function used by the framework to produce an instance of a suitable 
 * {@link MapConfigurationConverter} for any given type.<br>
 * If none are found then the {@link NoMapConfigurationConverter} is returned.<br>
 * 
 * The following type-converter mappings are provided:
 * <ul>
 *    <li>{@link Map} -{@literal>} {@link HashMapConfigurationConverter}</li>
 *    <li>{@link HashMap} -{@literal>} {@link HashMapConfigurationConverter}</li>
 * </ul>
 *
 * @author George Aristy
 */
public class DefaultMapTypeConverterFunction implements Function<Class<?>, Class<? extends MapConfigurationConverter>> {
  private static final Map<Class<?>, Class<? extends MapConfigurationConverter>> DEFAULTS;

  static {
    DEFAULTS = new HashMap<>();
    DEFAULTS.put(Map.class, HashMapConfigurationConverter.class);
    DEFAULTS.put(HashMap.class, HashMapConfigurationConverter.class);
  }

  @Override
  public Class<? extends MapConfigurationConverter> apply(Class<?> type) {
    return DEFAULTS.getOrDefault(type, NoMapConfigurationConverter.class);
  }
}
