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

import java.util.ArrayList;

/**
 * A {@link CollectionConfigurationConverter} that converts a string value into
 * an initialized {@link ArrayList}.
 *
 * @author George Aristy
 * @param <T>
 */
public class ListConfigurationConverter<T> extends BaseCollectionConfigurationConverter<T> {

  /**
   * 
   * @param elementConverter
   * @param delimiter 
   * @throws NullPointerException if either {@code elementConverter} or {@code delimiter} is {@code null}.
   */
  public ListConfigurationConverter(
          ConfigurationConverter<T> elementConverter, 
          String delimiter
  ) {
    super(elementConverter, delimiter, ArrayList.class);
  }
}