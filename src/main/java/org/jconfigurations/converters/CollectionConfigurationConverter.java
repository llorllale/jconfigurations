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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.jconfigurations.CollectionConfiguration;
import org.jconfigurations.configurators.CollectionConfigurator;
import org.jconfigurations.functions.CollectionFieldConverterFunction;

/**
 * {@link CollectionConfigurationConverter}s are used by 
 * {@link CollectionConfigurator configurators} to convert a configuration
 * value into an instance of a {@link Collection} of type {@code T}.
 * Default implementations for {@link List} and {@link Set} are already provided.
 * Users may opt to implement their own converters and specify those via 
 * {@link CollectionConfiguration#converter()}.
 * <b>The {@link CollectionFieldConverterFunction} requires that implementations provide an 
 * accessible constructor that accepts two arguments: a 
 * {@link ConfigurationConverter} and the string 
 * {@link CollectionConfiguration#delimiter() delimiter}.</b>
 * 
 * {@link CollectionConfigurationConverter}s only act upon fields marked with 
 * {@link CollectionConfiguration}.
 *
 * @author George Aristy
 * @param <T>
 * @see ListConfigurationConverter
 * @see SetConfigurationConverter
 */
@FunctionalInterface
public interface CollectionConfigurationConverter<T> extends ConfigurationConverter<Collection<T>> {
  
}
