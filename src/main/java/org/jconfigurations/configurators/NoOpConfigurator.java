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
package org.jconfigurations.configurators;

import org.jconfigurations.ConfigurationException;

/**
 * <pre>
 * Special kind of {@link Configurator} that does absolutely nothing when its 
 * {@link #configure(java.lang.Object)} method is invoked.
 * It is used internally to end chains of {@link Configurator configurators}.
 * </pre>
 * 
 * @author George Aristy
 */
public class NoOpConfigurator implements Configurator {
  @Override
  public void configure(Object object) throws ConfigurationException {
    //do nothing
  }
}