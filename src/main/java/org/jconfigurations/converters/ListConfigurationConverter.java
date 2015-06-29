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

import java.util.ArrayList;

/**
 *
 * @author George Aristy
 * @param <T>
 */
public class ListConfigurationConverter<T> extends AbstractCollectionConfigurationConverter<T> {
  public ListConfigurationConverter(ConfigurationConverter<T> converter) {
    super(converter, ArrayList.class);
  }
  
  public ListConfigurationConverter(ConfigurationConverter<T> converter, String separator) {
    super(converter, ArrayList.class, separator);
  }
}