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

import org.jconfigurations.converters.BigDecimalConfigurationConverter;
import org.jconfigurations.converters.BigIntegerConfigurationConverter;
import org.jconfigurations.converters.BooleanConfigurationConverter;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.DoubleConfigurationConverter;
import org.jconfigurations.converters.FileConfigurationConverter;
import org.jconfigurations.converters.FloatConfigurationConverter;
import org.jconfigurations.converters.IntegerConfigurationConverter;
import org.jconfigurations.converters.LongConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.converters.StringConfigurationConverter;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Main class.
 * @author George Aristy
 */
public class JConfigurator {
  private static final Map<Class<?>, Class<? extends ConfigurationConverter<?>>> defaultConverters = new HashMap<Class<?>, Class<? extends ConfigurationConverter<?>>>();

  static {
    defaultConverters.put(Float.class, FloatConfigurationConverter.class);
    defaultConverters.put(float.class, FloatConfigurationConverter.class);
    defaultConverters.put(Integer.class, IntegerConfigurationConverter.class);
    defaultConverters.put(int.class, IntegerConfigurationConverter.class);
    defaultConverters.put(String.class, StringConfigurationConverter.class);
    defaultConverters.put(Double.class, DoubleConfigurationConverter.class);
    defaultConverters.put(double.class, DoubleConfigurationConverter.class);
    defaultConverters.put(Long.class, LongConfigurationConverter.class);
    defaultConverters.put(long.class, LongConfigurationConverter.class);
    defaultConverters.put(BigDecimal.class, BigDecimalConfigurationConverter.class);
    defaultConverters.put(BigInteger.class, BigIntegerConfigurationConverter.class);
    defaultConverters.put(File.class, FileConfigurationConverter.class);
    defaultConverters.put(Boolean.class, BooleanConfigurationConverter.class);
    defaultConverters.put(boolean.class, BooleanConfigurationConverter.class);
  }

  /**
   * 
   * @param properties
   * @param object
   * @throws ConfigurationException 
   */
  public void configure(Properties properties, Object object) throws ConfigurationException {
      Map<String, String> map = new HashMap<String, String>();

      for(String property : properties.stringPropertyNames()){
          map.put(property, properties.getProperty(property));
      }

      configure(map, object);
  }

  /**
   * 
   * @param properties
   * @param object
   * @throws ConfigurationException 
   */
  public void configure(Map<String, String> properties, Object object) throws ConfigurationException {
    try{
      for(Field field : object.getClass().getDeclaredFields()){
        if(field.isAnnotationPresent(Configuration.class)){
          Configuration configuration = field.getAnnotation(Configuration.class);
          final String propertyName = configuration.name().isEmpty() ? field.getName() : configuration.name();
          final String propertyValue = properties.get(propertyName);
          final Class<?> propertyType = field.getType();
          boolean isFlag = configuration.flag();
  
          if(propertyValue == null && configuration.required()){
            throw new ConfigurationException("Missing configuration value for required configuration property: " + propertyName + " in class: " + object.getClass().getName());
          }

          field.setAccessible(true);

          if(isFlag){
            if(propertyType == Boolean.class || propertyType == boolean.class){
              field.set(object, properties.containsKey(propertyName));
            }else{
              throw new ConfigurationException("Cannot set Configuration.flag = 'true' on non-boolean field " + field.getName() + " in class " + object.getClass());
            }
          }else{
            Object coercedPropertyValue = null;
          
            if(propertyValue != null){
              coercedPropertyValue = convert(propertyName, propertyValue, field.getType(), configuration.converter());
              field.set(object, coercedPropertyValue);
            }
          }
        }
      }
    }catch(ConfigurationException ce){
      throw ce;
    }catch(Exception e){
      throw new ConfigurationException(e.getMessage(), e);
    }
  }

  private Object convert(String propertyName, String propertyValue, Class<?> propertyValueType, Class<? extends ConfigurationConverter<?>> converterClass) throws ConfigurationException {
    Object result = null;

    try{
      if(NoConfigurationConverter.class.isAssignableFrom(converterClass)){
        if(defaultConverters.containsKey(propertyValueType)){
          result = defaultConverters.get(propertyValueType).getConstructor(String.class).newInstance(propertyName).convert(propertyValue);
        }else{
          throw new ConfigurationException("No ConfigurationConverter found for configuration name: " + propertyName + " type: " + propertyValueType.getName());
        }
      }else{
        ConfigurationConverter<?> converter;
        
        if(defaultConverters.containsValue(converterClass)){
          converter = converterClass.getConstructor(String.class).newInstance(propertyName);
        }else{
          converter = converterClass.newInstance();
        }

        result = converter.convert(propertyValue);
      }
    }catch(ConfigurationException ce){
      throw ce;
    }catch(Exception e){
      throw new ConfigurationException(e.getMessage(), e);
    }

    return result;
  }
}