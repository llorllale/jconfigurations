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

import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import org.jconfigurations.configurators.BasicConfigurator;
import org.jconfigurations.configurators.Configurator;

/**
 * <pre>
 * The {@link BasicConfigurator} acts upon fields marked with {@link Configuration}.
 * Fields marked with {@link Configuration} should be of "atomic" types such as 
 * primitives (or their boxed types) and other "non-collection" types, 
 * eg. {@link java.io.File} or {@link java.math.BigDecimal}.
 * </pre>
 * @author George Aristy
 * @see Configurator
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Configuration {
  /**
   * <pre>
   * Specifies the {@link ConfigurationConverter converter} to use to set this {@link Configuration configuration's} value.
   * If none is specified, then a default converter is used from {@code org.jconfigurations.converters}.
   * If no default converter then a {@link ConfigurationException} will be thrown.
   * </pre>
   * @return 
   * @see ConfigurationConverter
   */
  public Class<? extends ConfigurationConverter> converter() default NoConfigurationConverter.class;
}