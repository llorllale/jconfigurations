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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Function;
import org.jconfigurations.Configuration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.util.ErrorFunction;

/**
 * <pre>
 * Function that produces a suitable {@link ConfigurationConverter} for a given field by
 * analyzing the field's {@link Configuration#converter()} value. If said value is equal
 * to {@link NoConfigurationConverter}, or if the field is NOT annotated with 
 * {@link Configuration}, then it delegates to a 'default' function.
 * </pre>
 *
 * @author George Aristy
 */
public class FieldConverterFunction implements ErrorFunction<Field, ConfigurationConverter, ConfigurationException> {
  private final Function<Class<?>, ConfigurationConverter> delegate;

  /**
   * 
   * 
   * @param defaultTypeFunction the default function to delegate to when a given field 
   * does not have an explicit {@link ConfigurationConverter converter} specified 
   * (other than the default {@link NoConfigurationConverter}).
   * @throws NullPointerException if {@code delegate} is {@code null}.
   */
  public FieldConverterFunction(Function<Class<?>, ConfigurationConverter> defaultTypeFunction) {
    this.delegate = Objects.requireNonNull(defaultTypeFunction, "null defaultTypeFunction");
  }

  /**
   * <pre>
   * Defaults to the {@link DefaultTypeConverterFunction} as delegate
   * </pre>
   * 
   * @see #FieldConverterFunction(java.util.function.Function) 
   */
  public FieldConverterFunction(){
    this(new DefaultTypeConverterFunction());
  }

  @Override
  public ConfigurationConverter apply(Field field) throws ConfigurationException {
    if(field.isAnnotationPresent(Configuration.class) && 
            !NoConfigurationConverter.class.equals(field.getAnnotation(Configuration.class).converter())
            ){
      try{
        Constructor<? extends ConfigurationConverter> c = field.getAnnotation(Configuration.class)
                .converter()
                .getConstructor();
        c.setAccessible(true);
        return c.newInstance();
      }catch(NoSuchMethodException e){
        throw new ConfigurationException(
                String.format(
                        "ConfigurationConverter of type %s for field %s of class %s does not have an accessible no-arg constructor.",
                        field.getAnnotation(Configuration.class).converter().getName(), 
                        field.getName(), 
                        field.getDeclaringClass().getName()
                ), 
                e
        );
      }catch(InvocationTargetException | IllegalAccessException | InstantiationException e){
        throw new ConfigurationException(
                String.format(
                        "Error instantiating ConfigurationConverter of type %s for field %s of class %s",
                        field.getAnnotation(Configuration.class).converter().getName(), 
                        field.getName(), 
                        field.getDeclaringClass().getName()
                ), 
                e
        );
      }
    }else{
      return delegate.apply(field.getType());
    }
  }
}