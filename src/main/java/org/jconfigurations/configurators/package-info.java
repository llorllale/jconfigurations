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
 * Contains the {@link org.jconfigurations.configurators.Configurator} interface and all its implementations.
 * 
 * Each {@link org.jconfigurations.configurators.Configurator} is developed together with its respective annotation that can be found in the 
 * {@link org.jconfigurations} package. Eg. {@link org.jconfigurations.Configuration} was introduced together with {@link org.jconfigurations.configurators.BasicConfigurator}, meaning 
 * that {@link org.jconfigurations.configurators.BasicConfigurator} is the only {@link org.jconfigurations.configurators.Configurator} <em>provided by the framework</em> that will be triggered on fields marked 
 * with {@link org.jconfigurations.Configuration}.
 * 
 * {@link org.jconfigurations.configurators.Configurator Configurators} provided by the framework can be chained together to combine features that act on 
 * combinations of annotations. The special {@link org.jconfigurations.configurators.NoOpConfigurator} is used to end these chains.
 * </pre>
 */
package org.jconfigurations.configurators;