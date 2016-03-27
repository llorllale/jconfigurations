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

import java.lang.reflect.Field;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.Name;
import org.jconfigurations.util.ErrorFunction;

/**
 * Function that extracts a field's name from either:
 * 
 * <ol>
 *  <li>its {@literal @}{@code Name} value, or</li>
 *  <li>its {@link Field#getName() name} as declared in the source code</li>
 * </ol>
 * 
 * Using this {@link ErrorFunction function} with a field whose {@link Name name} is
 * {@code empty} will result in a {@link ConfigurationException}.
 * 
 * @author George Aristy
 * @see Name
 */
public class DefaultFieldNameFunction implements ErrorFunction<Field, String> {
  @Override
  public String apply(Field field) throws ConfigurationException {
    if(field.isAnnotationPresent(Name.class)){
      final String name = field.getAnnotation(Name.class).value();

      if(name.isEmpty()){
        throw new ConfigurationException(
                String.format(
                        "A @Name value cannot be empty (culprit is field '%s' in class '%s').",
                        field.getName(),
                        field.getDeclaringClass().getName()
                )
        );
      }else{
        return name;
      }
    }else{
      return field.getName();
    }
  }
}