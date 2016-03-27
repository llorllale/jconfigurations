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
import java.util.Map;
import org.jconfigurations.configurators.MapConfigurator;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.MapConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.converters.NoMapConfigurationConverter;

/**
 * The {@link MapConfigurator} acts upon fields marked with 
 * {@literal @}{@link MapConfiguration}. This annotation is designed
 * to be used with fields that implement or extend the {@link Map}
 * interface.
 *
 * @author George Aristy
 * @see MapConfigurator
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface MapConfiguration {
  /**
   * The delimiter string that separates each key-value pair ("entry") from others.
   * 
   * @return 
   */
  public String entryDelimiter() default ",";

  /**
   * The delimiter string that in turn separates the key and the value in each
   * entry.
   * 
   * @return 
   */
  public String keyValueSeparator() default "=";

  /**
   * Specifies the {@link MapConfigurator} to use to set the
   * {@link Map} field's value. 
   * 
   * If none is specified, then a default one is used from 
   * {@code org.jconfigurations.converters}.
   * 
   * If no default converter is found then a {@link ConfigurationException} will
   * be thrown.
   * 
   * @return 
   */
  public Class<? extends MapConfigurationConverter> converter() default NoMapConfigurationConverter.class;

  /**
   * Specifies the {@link ConfigurationConverter} to use to convert each key in 
   * this {@link Map}.
   * 
   * If {@link NoConfigurationConverter} is specified (by default), then an available
   * {@link ConfigurationConverter} will be used from {@code org.jconfigurations.converters}.
   * 
   * @return 
   * @see #keyValueSeparator() 
   * @see #entryDelimiter() 
   */
  public Class<? extends ConfigurationConverter> keyConverter() default NoConfigurationConverter.class;

  /**
   * Specifies the {@link ConfigurationConverter} to use to convert each value in 
   * this {@link Map}.
   * 
   * If {@link NoConfigurationConverter} is specified (by default), then an available
   * {@link ConfigurationConverter} will be used from {@code org.jconfigurations.converters}.
   * 
   * @return 
   */
  public Class<? extends ConfigurationConverter> valueConverter() default NoConfigurationConverter.class;
}