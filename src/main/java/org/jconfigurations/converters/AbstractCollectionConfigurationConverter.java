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
package org.jconfigurations.converters;

import java.util.Collection;
import org.jconfigurations.ConfigurationException;

/**
 *
 * @author George Aristy
 * @param <T>
 */
public abstract class AbstractCollectionConfigurationConverter<T> implements CollectionConfigurationConverter<T> {
  private final ConfigurationConverter<T> converter;
  private final Class<? extends Collection> collectionType;
  private final String separator;

  protected AbstractCollectionConfigurationConverter(ConfigurationConverter<T> converter, Class<? extends Collection> collectionType) {
    this(converter, collectionType, ",");
  }

  protected AbstractCollectionConfigurationConverter(ConfigurationConverter<T> converter, Class<? extends Collection> collectionType, String separator) {
    this.converter = converter;
    this.collectionType = collectionType;
    this.separator = separator;
  }

  public String getSeparator(){
    return separator;
  }

  @Override
  public Collection<T> convert(String value) throws ConfigurationException {
    Collection<T> collection = null;

    try{
      collection = collectionType.newInstance();

      for(String v : value.split(separator)){
        collection.add(converter.convert(v));
      }
    }catch(ConfigurationException cx){
      throw cx;
    }catch(Exception e){
      throw new ConfigurationException(e.getMessage(), e);
    }

    return collection;
  }
}