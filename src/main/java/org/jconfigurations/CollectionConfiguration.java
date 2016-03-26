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
package org.jconfigurations;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.util.Collection;
import org.jconfigurations.configurators.CollectionConfigurator;
import org.jconfigurations.converters.CollectionConfigurationConverter;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.NoCollectionConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.functions.CollectionFieldConverterFunction;
import org.jconfigurations.functions.DefaultCollectionTypeConverterFunction;
import org.jconfigurations.functions.DefaultTypeConverterFunction;
import org.jconfigurations.functions.FieldConverterFunction;

/**
 * The {@link CollectionConfigurator} acts upon fields marked with 
 * {@literal @}{@link CollectionConfiguration}. This annotation is designed
 * to be used with fields that implement or extend the {@link Collection}
 * interface.
 *
 * @author George Aristy
 * @see CollectionConfigurator
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface CollectionConfiguration {
  /**
   * The configuration value's string representation will be tokenized using 
   * this delimiter value. Each token will then be converted to the appropriate
   * type before being added to the field's collection-type implementation.
   * 
   * @return 
   * @see #elementConverter() 
   */
  public String delimiter() default ",";

  /**
   * Specifies the {@link CollectionConfigurator} to use to set the
   * {@link Collection} field's value. 
   * 
   * If none is specified, then a default one is used from 
   * {@code org.jconfigurations.converters}.
   * 
   * If no default converter is found then a {@link ConfigurationException} will
   * be thrown.
   * 
   * @return 
   * @see CollectionConfigurationConverter
   * @see CollectionFieldConverterFunction
   * @see DefaultCollectionTypeConverterFunction
   */
  public Class<? extends CollectionConfigurationConverter> converter() default NoCollectionConfigurationConverter.class;

  /**
   * After the configuration value's string representation is tokenized using the
   * specified {@link #delimiter() delimiter}, an instance of this 
   * {@link ConfigurationConverter} is used to convert each token to the appropriate
   * type before being added to the field's collection-type implementation.
   * 
   * If {@link NoConfigurationConverter} is specified (by default), then an available
   * {@link ConfigurationConverter} will be used from {@code org.jconfigurations.converters}.
   * 
   * @return 
   * @see #delimiter() 
   * @see DefaultTypeConverterFunction
   * @see FieldConverterFunction
   */
  public Class<? extends ConfigurationConverter> elementConverter() default NoConfigurationConverter.class;
}