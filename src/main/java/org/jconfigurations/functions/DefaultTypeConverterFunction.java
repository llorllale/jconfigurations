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
package org.jconfigurations.functions;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.jconfigurations.converters.BigDecimalConfigurationConverter;
import org.jconfigurations.converters.BigIntegerConfigurationConverter;
import org.jconfigurations.converters.BooleanConfigurationConverter;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.DoubleConfigurationConverter;
import org.jconfigurations.converters.FileConfigurationConverter;
import org.jconfigurations.converters.FloatConfigurationConverter;
import org.jconfigurations.converters.IntegerConfigurationConverter;
import org.jconfigurations.converters.LongConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.converters.StringConfigurationConverter;
import org.jconfigurations.converters.URLConfigurationConverter;

/**
 * Special function used by the framework to produce an instance of a suitable 
 * {@link ConfigurationConverter} for any given type.<br>
 * If none are found then the {@link NoConfigurationConverter} is returned.<br>
 * 
 * The following type-converter mappings are provided:
 * <ul>
 *    <li>{@code float ->} {@link FloatConfigurationConverter}</li>
 *    <li>{@code int ->} {@link IntegerConfigurationConverter}</li>
 *    <li>{@code String ->} {@link StringConfigurationConverter}</li>
 *    <li>{@code double ->} {@link DoubleConfigurationConverter}</li>
 *    <li>{@code long ->} {@link LongConfigurationConverter}</li>
 *    <li>{@code boolean ->} {@link BooleanConfigurationConverter}</li>
 *    <li>{@code BigDecimal ->} {@link BigDecimalConfigurationConverter}</li>
 *    <li>{@code BigInteger ->} {@link BigIntegerConfigurationConverter}</li>
 *    <li>{@code File ->} {@link FileConfigurationConverter}</li>
 *    <li>{@code URL ->} {@link URLConfigurationConverter}</li>
 * </ul>
 * 
 * @author George Aristy
 */
public final class DefaultTypeConverterFunction implements Function<Class<?>, ConfigurationConverter> {
  private static final Map<Class<?>, Class<? extends ConfigurationConverter>> DEFAULTS;

  static {
    DEFAULTS = new HashMap<>();
    DEFAULTS.put(Float.class, FloatConfigurationConverter.class);
    DEFAULTS.put(float.class, FloatConfigurationConverter.class);
    DEFAULTS.put(Integer.class, IntegerConfigurationConverter.class);
    DEFAULTS.put(int.class, IntegerConfigurationConverter.class);
    DEFAULTS.put(String.class, StringConfigurationConverter.class);
    DEFAULTS.put(Double.class, DoubleConfigurationConverter.class);
    DEFAULTS.put(double.class, DoubleConfigurationConverter.class);
    DEFAULTS.put(Long.class, LongConfigurationConverter.class);
    DEFAULTS.put(long.class, LongConfigurationConverter.class);
    DEFAULTS.put(BigDecimal.class, BigDecimalConfigurationConverter.class);
    DEFAULTS.put(BigInteger.class, BigIntegerConfigurationConverter.class);
    DEFAULTS.put(File.class, FileConfigurationConverter.class);
    DEFAULTS.put(Boolean.class, BooleanConfigurationConverter.class);
    DEFAULTS.put(boolean.class, BooleanConfigurationConverter.class);
    DEFAULTS.put(URL.class, URLConfigurationConverter.class);
  }

  @Override
  public ConfigurationConverter apply(Class<?> clazz) {
    try{
      return DEFAULTS.getOrDefault(clazz, NoConfigurationConverter.class).newInstance();
    }catch(InstantiationException | IllegalAccessException e){
      throw new RuntimeException(
              String.format(
                      "This wasn't supposed to happen, but the no-arg constructor of a default converter just exploded! (offending class: %s)",
                      DEFAULTS.getOrDefault(clazz, NoConfigurationConverter.class).getName()
              ),
              e
      );
    }
  }
}