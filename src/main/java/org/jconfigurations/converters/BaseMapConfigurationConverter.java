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

import java.util.Map;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.MapConfiguration;
import static java.util.Objects.requireNonNull;

/**
 * Handy abstract class that implementations of {@link MapConfigurationConverter} 
 * can extend.
 * 
 * The {@link BaseMapConfigurationConverter} will initialize an instance of 
 * the {@link Map}'s implementation class supplied by subclasses by calling its 
 * <em>no-arg constructor</em>. 
 *
 * @author George Aristy
 * @param <K>
 * @param <V>
 */
public abstract class BaseMapConfigurationConverter<K, V> implements MapConfigurationConverter<K, V>{
  protected final ConfigurationConverter<K> keyConverter;
  protected final ConfigurationConverter<V> valueConverter;
  protected final String entryDelimiter;
  protected final String keyValueSeparator;
  protected final Class<? extends Map> mapImplClass;

  /**
   * 
   * @param keyConverter see {@link MapConfiguration#keyConverter()}
   * @param valueConverter see {@link MapConfiguration#valueConverter()}
   * @param entryDelimiter see {@link MapConfiguration#entryDelimiter()}
   * @param keyValueSeparator  see {@link MapConfiguration#keyValueSeparator()}
   * @param mapImplClass the {@link Map}'s implementation class
   * @see MapConfiguration
   */
  public BaseMapConfigurationConverter(
          ConfigurationConverter<K> keyConverter, 
          ConfigurationConverter<V> valueConverter, 
          String entryDelimiter, 
          String keyValueSeparator,
          Class<? extends Map> mapImplClass
  ) {
    this.keyConverter = requireNonNull(keyConverter, "null keyConverter");
    this.valueConverter = requireNonNull(valueConverter, "null valueConverter");
    this.entryDelimiter = requireNonNull(entryDelimiter, "null entryDelimiter");
    this.keyValueSeparator = requireNonNull(keyValueSeparator, "null keyValueSeparator");
    this.mapImplClass = requireNonNull(mapImplClass, "null mapImplClass");
  }

  @Override
  public Map<K, V> convert(String value) throws ConfigurationException {
    requireNonNull(value, "null value");

    final Map<K, V> map = instantiate(mapImplClass);

    for(String entry : value.split(entryDelimiter)){
      final String[] keyValue = entry.split(keyValueSeparator);
      final String k = keyValue[0];
      final String v;

      if(keyValue.length > 1){
        v = keyValue[1];
      }else{
        v = null;
      }

      map.put(
              keyConverter.convert(k),
              v != null ? valueConverter.convert(v) : null
      );
    }

    return map;
  }

  private Map instantiate(Class<? extends Map> clazz) throws ConfigurationException {
    try{
      return clazz.newInstance();
    }catch(InstantiationException | IllegalAccessException e){
      throw new ConfigurationException(
              String.format(
                      "Error instantiating MapConfigurationConverter class '%s' using its no-arg constructor",
                      clazz.getName()
              ),
              e
      );
    }
  }
}