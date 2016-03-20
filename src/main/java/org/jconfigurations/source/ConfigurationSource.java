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
package org.jconfigurations.source;

import java.util.Collections;
import java.util.Map;

/**
 * <pre>
 * Interface that decouples the source of configurations from this library's design.
 * </pre>
 * @author George Aristy
 */
@FunctionalInterface
public interface ConfigurationSource {
  /**
   * <pre>
   * A non-{@code null}, <b>non-modifiable</b> view of the configurations.
   * Attempts to modify the contents of this view should result in an 
   * {@link UnsupportedOperationException} to prevent inconsistencies.
   * </pre>
   * @return 
   * @see Collections#unmodifiableMap(java.util.Map) 
   */
  public Map<String, String> configurations();
}