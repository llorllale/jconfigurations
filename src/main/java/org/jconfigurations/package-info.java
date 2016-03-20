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
/**
 * <pre>
 * Contains the annotations that mark a field as a target for its respective {@link org.jconfigurations.configurators.Configurator}, and also contains 
 * the framework's {@link org.jconfigurations.ConfigurationException exception} class.
 * 
 * Each annotation is developed together with its respective {@link org.jconfigurations.configurators.Configurator} that "owns" it, meaning that no other 
 * {@link org.jconfigurations.configurators.Configurator} should be triggered by the presence of said annotation if it does not "own" it. Eg.: marking a 
 * field with {@link org.jconfigurations.Configuration} will force the {@link org.jconfigurations.configurators.BasicConfigurator} into action for the field, but 
 * such is not the case for the {@link org.jconfigurations.configurators.RequiredConfigurator} because field will be ignored by the latter.
 * </pre>
 * In order to use these features, you need to:
 * <ol>
 *    <li>Annotate a class' field accordingly with eg. {@link org.jconfigurations.Configuration}.</li>
 *    <li>Instantiate the annotation's respective {@link org.jconfigurations.configurators.Configurator configurator}, eg. {@link org.jconfigurations.configurators.BasicConfigurator} when using {@link org.jconfigurations.Configuration}, by providing an adequate {@link org.jconfigurations.source.ConfigurationSource}.</li>
 *    <li>Invoke the {@link org.jconfigurations.configurators.Configurator}'s {@link org.jconfigurations.configurators.Configurator#configure(java.lang.Object) configure(Object)} method and pass an instance of the class as input.</li>
 * </ol>
 * 
 * An alternative for step 2 would be to always use the {@link org.jconfigurations.configurators.JConfigurator} and never worry about having the right {@link org.jconfigurations.configurators.Configurator} for the annotations you're working with, because internally it delegates to a {@link org.jconfigurations.configurators.Configurator} chain containing all of the framework's {@link org.jconfigurations.configurators.Configurator configurators}.
 */
package org.jconfigurations;