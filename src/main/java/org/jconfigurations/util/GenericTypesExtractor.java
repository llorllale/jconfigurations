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
package org.jconfigurations.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * <pre>
 * Helper class that {@link #getGenericTypes() extracts} a given field's 
 * generic type parameters.
 * </pre>
 * 
 * @author George Aristy
 */
public class GenericTypesExtractor {
  private final List<Class<?>> genericTypes;

  /**
   * 
   * @param field 
   * @throws NullPointerException if {@code field} is {@code null}.
   */
  public GenericTypesExtractor(Field field){
    final List<Class<?>> tmp = new ArrayList<>();

    if(field.getGenericType() instanceof ParameterizedType){
      ParameterizedType generics = (ParameterizedType) field.getGenericType();
      Stream.of(generics.getActualTypeArguments())
              .map(t -> (Class<?>) t)
              .forEach(tmp::add);
    }

    this.genericTypes = Collections.unmodifiableList(tmp);
  }

  /**
   * 
   * @return an immutable list of the generic types declared for the given 
   * {@link #GenericTypesExtractor(java.lang.reflect.Field) field}. If the field
   * doesn't declare any generic types then the list will be empty.
   */
  public List<Class<?>> getGenericTypes(){
    return genericTypes;
  }
}