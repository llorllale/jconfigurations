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
package org.jconfigurations;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import org.jconfigurations.converters.CollectionConfigurationConverter;
import org.jconfigurations.converters.ListConfigurationConverter;
import org.jconfigurations.converters.NoCollectionConfigurationConverter;
import org.jconfigurations.converters.SetConfigurationConverter;

/**
 * Marks a <em>Collection</em> field as configurable.<br><br>
 * Default values are assumed for the respective {@link Configuration configurtion} parameters for 
 * fields marked with this annotation but missing the {@link Configuration coniguration} annotation.
 * 
 * @author George Aristy
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface CollectionConfiguration {
  /**
   * Defines the delimiter character used to separate each value in the configuration string.
   * @return 
   */
  public String delimiter();

  /**
   * Indicates the {@link CollectionConfigurationConverter converter} to use to build the collection.<br><br>
   * Default implementations are provided (see {@link ListConfigurationConverter} and {@link SetConfigurationConverter} so that
   * this parameteres is not strictly required.
   * 
   * @return 
   */
  public Class<? extends CollectionConfigurationConverter> collectionConverter() default NoCollectionConfigurationConverter.class;
}
