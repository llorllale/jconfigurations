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
package org.jconfigurations;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import org.jconfigurations.functions.DefaultFieldNameFunction;
import org.jconfigurations.source.ConfigurationSource;

  /**
   * <pre>
   * The {@link Configuration configuration's} name as expected in the {@link ConfigurationSource configuration source}.
   * By default, the name for a given configuration parameter is the field's own name as declared in 
   * the source code.
   * Eg.:
   * 
   *    private String name;</pre>
   * ... will be assigned the value of a configuration property named {@code name}, while:
   * <pre>
   * 
   *   {@literal @}Name("webServer.name")
   *    private String name;</pre>
   * 
   * ... will listen for a configuration property named {@code webServer.name}.<br><br>
   * 
   * It is illegal to specify an empty name and an error will be thrown in such a case.
   * 
   * @author George Aristy
   * @see DefaultFieldNameFunction
   */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Name {
  /**
   * The configuration's name.
   * @return 
   */
  public String value();
}
