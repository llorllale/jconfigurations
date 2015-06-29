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
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Marks a field as configurable.
 * @author George Aristy
 * @see JConfigurator#configure(java.util.Map, java.lang.Object) 
 */
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE})
public @interface Configuration {
  /**
   * The {@link Configuration configuration's} name as expected in the configuration source.<br>
   * By default, the name for a given configuration parameter is the field's own name as declared in 
   * the source code.<br>
   * Eg.:<br>
   * <pre>
   * {@literal @}Configuration
   * private String name;
   * </pre>
   * ... will be assigned the value of a configuration property named {@code name}, while:<br>
   * <pre>
   * {@literal @}Configuration(name = "webServer.name")
   * private String name;
   * </pre>
   * ... will listen for a configuration property named {@code webServer.name}.
   * @return 
   */
  public String name() default "";
  
  /**
   * {@code false} by default, a {@link Configuration configuration} marked as required will trigger an
   * {@link ConfigurationException error} if it's not found in the input source of configuratios according 
   * to its {@link #name() name}.
   * @return 
   */
  public boolean required() default false;

  /**
   * Specifies the {@link ConfigurationConverter converter} to use to set this {@link Configuration configuration's} value.<br>
   * If none is specified, then a default converter is used from {@code org.jconfigurations.converters}.<br>
   * If no default converter then a {@link ConfigurationException} will be thrown.
   * @return 
   * @see JConfigurator#configure(java.util.Map, java.lang.Object) 
   */
  public Class<? extends ConfigurationConverter> converter() default NoConfigurationConverter.class;
}