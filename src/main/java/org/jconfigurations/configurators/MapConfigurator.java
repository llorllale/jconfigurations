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
package org.jconfigurations.configurators;

import java.lang.reflect.Field;
import org.jconfigurations.MapConfiguration;
import org.jconfigurations.Name;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.MapConfigurationConverter;
import org.jconfigurations.functions.DefaultFieldNameFunction;
import org.jconfigurations.functions.DefaultMapTypeConverterFunction;
import org.jconfigurations.functions.MapFieldConverterFunction;
import org.jconfigurations.source.ConfigurationSource;
import org.jconfigurations.util.ErrorFunction;

/**
 * The {@link MapConfigurator} will target all {@literal @}{@link MapConfiguration}
 * fields of an object. 
 * 
 * Unless specified otherwise, the {@link MapConfigurator} will use the 
 * {@link DefaultMapTypeConverterFunction} to {@link MapConfigurationConverter convert}
 * configuration values into instances of the appropriate types before assigning
 * them to fields.
 *
 * @author George Aristy
 */
public class MapConfigurator extends BaseConfigurator {
  /**
   * Fully customizable constructor that allows the user to chain link another 
   * {@link Configurator configurator} in order to compose a multi-featured chain.
   * 
   * @param source the {@link ConfigurationSource} that provides the configurations
   * @param configurator another {@link Configurator} to which this instance will hand off the 
   * target {@code object} to once this {@link CollectionConfigurator} has finished 
   * {@link #configure(java.lang.Object) configuring} the {@code object}.
   * @param fieldNameFunction the {@link ErrorFunction function} used to produce the field's name (see {@link Name}).
   * @param fieldConverterFunction the {@link ErrorFunction function} used to produce an instance of the 
   * appropriate {@link ConfigurationConverter converter} for a given field.
   * @throws NullPointerException if any of the parameters is {@code null}.
   */
  public MapConfigurator(
          ConfigurationSource source, 
          Configurator configurator, 
          ErrorFunction<Field, String> fieldNameFunction, 
          ErrorFunction<Field, MapConfigurationConverter> fieldConverterFunction
  ) {
    super(MapConfiguration.class, source, configurator, fieldNameFunction, fieldConverterFunction);
  }

  /**
   * Defaults to using the {@link MapFieldConverterFunction} to 
   * {@link ConfigurationConverter#convert(java.lang.String) convert} configuration
   * values before assignment.
   * 
   * @param source
   * @param configurator
   * @param fieldNameFunction 
   * @throws NullPointerException if any of the parameters is {@code null}.
   * @see #MapConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator, org.jconfigurations.util.ErrorFunction, org.jconfigurations.util.ErrorFunction) 
   */
  public MapConfigurator(
          ConfigurationSource source, 
          Configurator configurator, 
          ErrorFunction<Field, String> fieldNameFunction
  ){
    this(source, configurator, fieldNameFunction, new MapFieldConverterFunction());
  }

  /**
   * Defaults to using the {@link DefaultFieldNameFunction} to resolve the field's
   * {@link Name name}.
   * 
   * @param source
   * @param configurator 
   * @throws NullPointerException if any of the parameters is {@code null}.
   * @see #MapConfigurator(org.jconfigurations.source.ConfigurationSource, org.jconfigurations.configurators.Configurator, org.jconfigurations.util.ErrorFunction) 
   */
  public MapConfigurator(
          ConfigurationSource source, 
          Configurator configurator
  ){
    this(source, configurator, new DefaultFieldNameFunction());
  }

  /**
   * Defaults to using the {@link NoOpConfigurator} as the next link in the 
   * {@link Configurator} chain.
   * 
   * @param source 
   * @throws NullPointerException if {@code source} is {@code null}.
   */
  public MapConfigurator(ConfigurationSource source){
    this(source, new NoOpConfigurator());
  }
}