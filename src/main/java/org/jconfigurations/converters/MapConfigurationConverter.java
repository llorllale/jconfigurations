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
import java.util.Map;
import org.jconfigurations.MapConfiguration;
import org.jconfigurations.configurators.MapConfigurator;
import org.jconfigurations.functions.MapFieldConverterFunction;

/**
 * {@link MapConfigurationConverter}s are used by 
 * {@link MapConfigurator configurators} to convert a configuration
 * value into an instance of a {@link Map} of types {@code K} and {@code V}.
 * Default implementations for {@link Map} and {@link HashMap} are already provided.
 * Users may opt to implement their own converters and specify those via 
 * {@link MapConfiguration#converter()}.
 * <b>The {@link MapFieldConverterFunction} requires that implementations provide an 
 * accessible constructor that accepts four arguments</b>: 
 * <ol>
 *    <li>the {@link MapConfiguration#keyConverter() keyConverter}</li>
 *    <li>the {@link MapConfiguration#valueConverter() valueConverter}</li>
 *    <li>the {@link MapConfiguration#entryDelimiter() entryDelimiter}</li>
 *    <li>the {@link MapConfiguration#keyValueSeparator() keyValueSeparator}</li>
 * </ol>
 * 
 * {@link MapConfigurationConverter}s only act upon fields marked with 
 * {@literal @}{@link MapConfiguration}.
 *
 * @author George Aristy
 * @param <K>
 * @param <V>
 * @see HashMapConfigurationConverter
 */
public interface MapConfigurationConverter<K, V> extends ConfigurationConverter<Map<K, V>> {
  
}
