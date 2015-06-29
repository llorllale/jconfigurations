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
package org.jconfigurations;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Marks a field as a <em>flag</em> configuration field.<br><br>
 * 
 * A 'flag' is a parameter whose presence, and <em>only</em> its presence, is required in order to determine its 'value'.<br><br>
 * 
 * It is illegal to annotate a type other than {@code boolean} with {@code @FlagConfiguration}.<br><br>
 * 
 * The configuration's presence or absence is used to set its value to {@code true} or {@code false}
 * respectively, regardless of the explicit value specified, effectively overriding any default value.<br><br>
 * 
 * {@link Configuration#required()} takes precedence over this annotation.
 * 
 * @author George Aristy
 */
@Retention(RUNTIME)
@Target({FIELD})
public @interface FlagConfiguration {

}
