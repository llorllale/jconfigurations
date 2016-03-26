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
package org.jconfigurations.configurators;

import org.jconfigurations.ConfigurationException;
import org.jconfigurations.source.ConfigurationSource;

/**
 * <pre>
 * A handy {@link Configurator} provided for the user's convenience to minimize refactoring on the
 * calling code by having to manually add another {@link Configurator} to their chain as new features
 * become available.
 * 
 * This {@link Configurator} delegates calls to its {@link #configure(java.lang.Object)} method to an internal
 * chain of {@link Configurator configurators} that is guaranteed to contain all of the framework's features.
 * </pre>
 * 
 * @author George Aristy
 */
public class JConfigurator implements Configurator {
  private final Configurator configurator;

  /**
   * 
   * @param source 
   */
  public JConfigurator(ConfigurationSource source) {
    this.configurator = 
            new RequiredConfigurator(
                    source, 
                    new BasicConfigurator(
                            source,
                            new CollectionConfigurator(
                                    source
                            )
                    )
            );
  }

  @Override
  public void configure(Object object) throws ConfigurationException {
    configurator.configure(object);
  }
}