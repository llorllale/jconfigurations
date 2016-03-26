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

import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.regex.PatternSyntaxException;
import org.jconfigurations.ConfigurationException;

/**
 * Handy abstract class that implementations of {@link CollectionConfigurationConverter} 
 * can extend.
 * 
 * The {@link BaseCollectionConfigurationConverter} will initialize an instance of 
 * the {@link Collection}'s implementation class supplied by subclasses by calling its 
 * <em>no-arg constructor</em>. 
 *
 * @author George Aristy
 * @param <T>
 * @see #BaseCollectionConfigurationConverter(org.jconfigurations.converters.ConfigurationConverter, java.lang.String, java.lang.Class) 
 */
public abstract class BaseCollectionConfigurationConverter<T> implements CollectionConfigurationConverter<T> {
  protected final ConfigurationConverter<T> elementConverter;
  protected final String delimiter;
  private final Class<? extends Collection> collectionType;

  /**
   * 
   * 
   * @param elementConverter the {@link ConfigurationConverter converter} to use 
   * to convert each substring to the appropriate type.
   * @param delimiter the delimiter to use to tokenize the configuration's 
   * {@link #convert(java.lang.String) value}. The configuration string will be 
   * tokenized using the standard {@link String#split(java.lang.String)} method.
   * @param collectionType the implementation class - must have an accessible no-arg 
   * constructor.
   * @throws NullPointerException if any of the parameters is {@code null}.
   */
  protected BaseCollectionConfigurationConverter(
          ConfigurationConverter<T> elementConverter, 
          String delimiter, 
          Class<? extends Collection> collectionType
  ) {
    this.elementConverter = requireNonNull(elementConverter, "null elementConverter");
    this.delimiter = requireNonNull(delimiter, "null delimiter");
    this.collectionType = requireNonNull(collectionType, "null collectionType");
  }

  @Override
  public Collection<T> convert(String value) throws ConfigurationException {
    value = requireNonNull(value, "null value");

    try{
      final Collection<T> collection = collectionType.newInstance();
  
      for(String v : value.split(delimiter)){
        collection.add(elementConverter.convert(v));
      }

      return collection;
    }catch(InstantiationException | IllegalAccessException e){
      throw new ConfigurationException(
              String.format(
                      "Error calling no-arg constructor for collection configuration converter class '%s'",
                      collectionType.getName()
              ), 
              e
      );
    }catch(PatternSyntaxException e){
      throw new ConfigurationException(
              String.format(
                      "Malformed delimiter string '%s'.",
                      delimiter
              ), 
              e
      );
    }
  }
}