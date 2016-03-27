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

import org.jconfigurations.ConfigurationException;

/**
 * A function that can throw an exception.
 * 
 * @author George Aristy
 * @param <T> the function's input type
 * @param <R> the function's return type
 */
public interface ErrorFunction<T, R> {
  /**
   * Idempotent operation that can possibly throw an error {@code E}.
   * 
   * @param t the input parameter
   * @return the return value {@code R}.
   * @throws ConfigurationException if there is an error that impedes the function from producing {@code R}.
   */
  public R apply(T t) throws ConfigurationException;
}