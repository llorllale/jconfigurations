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
package org.jconfigurations.configurators;

import org.jconfigurations.converters.ConfigurationConverter;
import java.lang.reflect.Field;
import org.jconfigurations.Configuration;
import org.jconfigurations.Name;
import org.jconfigurations.functions.DefaultFieldNameFunction;
import org.jconfigurations.functions.DefaultTypeConverterFunction;
import org.jconfigurations.source.ConfigurationSource;
import org.jconfigurations.functions.FieldConverterFunction;
import org.jconfigurations.util.ErrorFunction;

/**
 * <pre>
 * The {@link BasicConfigurator} will target all {@link Configuration} fields of
 * an object. These are typically "atomic" fields such as primitives or other 
 * simple types.
 * 
 * Unless specified otherwise, the {@link BasicConfigurator} will use the {@link DefaultTypeConverterFunction}
 * to {@link ConfigurationConverter convert} configuration values into the appropriate types
 * before assigning them to fields.
 * </pre>
 * 
 * @author George Aristy
 * @see Configuration
 * @see ConfigurationConverter
 */
public class BasicConfigurator extends BaseConfigurator {
  /**
   * Fully customizable constructor that allows the user to chain link another 
   * {@link Configurator configurator} in order to compose a multi-featured chain.
   * 
   * @param source the {@link ConfigurationSource} that provides the configurations
   * @param configurator another {@link Configurator} to which this instance will hand off the 
   * target {@code object} to once this {@link BasicConfigurator} has finished 
   * {@link #configure(java.lang.Object) configuring} the {@code object}.
   * @param fieldNameFunction the {@link ErrorFunction function} used to produce the field's name (see {@link Name}).
   * @param fieldConverterFunction the {@link ErrorFunction function} used to produce an instance of the 
   * appropriate {@link ConfigurationConverter converter} for a given field.
   * @throws NullPointerException if any of the inputs is {@code null}.
   */
  public BasicConfigurator(
          ConfigurationSource source, 
          Configurator configurator,
          ErrorFunction<Field, String> fieldNameFunction, 
          ErrorFunction<Field, ConfigurationConverter> fieldConverterFunction
  ) {
    super(Configuration.class, source, configurator, fieldNameFunction, fieldConverterFunction);
  }

  /**
   * Defaults to using the {@link FieldConverterFunction} to 
   * {@link ConfigurationConverter#convert(java.lang.String) convert} configuration
   * values before assignment.
   * 
   * @param source
   * @param configurator
   * @param fieldNameFunction 
   * @throws NullPointerException if any of the inputs is {@code null}.
   */
  public BasicConfigurator(
          ConfigurationSource source,
          Configurator configurator,
          ErrorFunction<Field, String> fieldNameFunction
  ){
    this(source, configurator, fieldNameFunction, new FieldConverterFunction());
  }

  /**
   * Defaults to using the {@link DefaultFieldNameFunction} to calculate a field's 
   * {@link Name name}, and using the {@link FieldConverterFunction} to convert
   * configuration values before assignment.
   * 
   * @param source
   * @param configurator 
   * @throws NullPointerException if any of the inputs is {@code null}.
   */
  public BasicConfigurator(ConfigurationSource source, Configurator configurator){
    this(source, configurator, new DefaultFieldNameFunction());
  }

  /**
   * <pre>
   * Defaults to using the {@link NoOpConfigurator}, thereby ending this chain of 
   * {@link Configurator configurators}. In addition, the same defaults of 
   * {@link #BasicConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator)}
   * are used.
   * </pre>
   * 
   * @param source 
   * @throws NullPointerException if {@code source} is {@code null}.
   */
  public BasicConfigurator(ConfigurationSource source){
    this(source, new NoOpConfigurator());
  }
}