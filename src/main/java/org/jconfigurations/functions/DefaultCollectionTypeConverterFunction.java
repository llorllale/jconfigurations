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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.jconfigurations.converters.CollectionConfigurationConverter;
import org.jconfigurations.converters.ListConfigurationConverter;
import org.jconfigurations.converters.NoCollectionConfigurationConverter;
import org.jconfigurations.converters.SetConfigurationConverter;

/**
 * Special function used by the framework to produce an instance of a suitable 
 * {@link CollectionConfigurationConverter} for any given type.<br>
 * If none are found then the {@link NoCollectionConfigurationConverter} is returned.<br>
 * 
 * The following type-converter mappings are provided:
 * <ul>
 *    <li>{@link List} -{@literal>} {@link ListConfigurationConverter}</li>
 *    <li>{@link Set} -{@literal>} {@link SetConfigurationConverter}</li>
 * </ul>
 *
 * @author George Aristy
 */
public class DefaultCollectionTypeConverterFunction implements 
        Function<Class<?>, Class<? extends CollectionConfigurationConverter>> 
{
  private static final Map<Class<?>, Class<? extends CollectionConfigurationConverter>> DEFAULTS;

  static {
    DEFAULTS = new HashMap<>();
    DEFAULTS.put(List.class, ListConfigurationConverter.class);
    DEFAULTS.put(Set.class, SetConfigurationConverter.class);
  }

  @Override
  public Class<? extends CollectionConfigurationConverter> apply(Class<?> type) {
    return DEFAULTS.getOrDefault(type, NoCollectionConfigurationConverter.class);
  }
}