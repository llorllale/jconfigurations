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
import java.util.Map;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.HashMapConfigurationConverter;
import org.jconfigurations.converters.MapConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.converters.NoMapConfigurationConverter;
import org.jconfigurations.converters.StringConfigurationConverter;

/**
 * Marks a {@link Map} field as configurable.<br><br>
 * 
 * The {@link MapConfiguration} annotation parameters have precedence over any specified by {@link Configuration}.<br><br>
 * 
 * While {@code Map} configurations can be handled by providing an appropriate value for {@link Configuration#converter()},
 * the inclusion of this annotation provides the benefits of commonly used, default implementations - see {@link HashMapConfigurationConverter} - 
 * as well as the sensible default behavior: if no converters are specified for {@link #keyConverter()} or {@link #valueConverter()} then they're
 * treated as strings and the {@link StringConfigurationConverter} will be used.<br><br>
 * 
 * The Java API currently offers no well-defined way to get a type's generic parameter(s), which is the reason for the existence of 
 * {@link #keyConverter()} and {@link #valueConverter()} parameters.<br><br>
 * 
 * @author George Aristy
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface MapConfiguration {
  /**
   * Similar to {@link CollectionConfiguration#delimiter()}, it specifies the delimiter to use to separate the key-value pairs.
   * The default is {@code ","}.
   * @return 
   */
  public String delimiter() default ",";

  /**
   * Once the key-value pairs have been segregated (see {@link #delimiter()}), each pair in turn is split into two parts - the key and the value - 
   * using this string.
   * @return 
   */
  public String assignmentOp() default "=";

  /**
   * Specifies the {@link MapConfigurationConverter} to use to convert this {@link MapConfiguration}.
   * @return 
   * @see HashMapConfigurationConverter
   */
  public Class<? extends MapConfigurationConverter> mapConverter() default NoMapConfigurationConverter.class;

  /**
   * Specifies the {@link ConfigurationConverter} to use to convert the {@link Map}'s {@code key}.<br><br>
   * 
   * By default, the {@link StringConfigurationConverter} is used.
   * 
   * @return 
   */
  public Class<? extends ConfigurationConverter> keyConverter() default NoConfigurationConverter.class;

  /**
   * Specifies the {@link ConfigurationConverter} to use to convert the {@link Map}'s {@code value}.<br><br>
   * 
   * By default, the {@link StringConfigurationConverter} is used.
   * 
   * @return 
   */
  public Class<? extends ConfigurationConverter> valueConverter() default NoConfigurationConverter.class;
}
