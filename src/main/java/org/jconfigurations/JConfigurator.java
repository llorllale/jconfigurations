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
import org.jconfigurations.converters.StringConfigurationConverter;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.jconfigurations.converters.CollectionConfigurationConverter;
import org.jconfigurations.converters.ListConfigurationConverter;
import org.jconfigurations.converters.NoCollectionConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.converters.SetConfigurationConverter;

/**
 * Main class.
 * @author George Aristy
 */
public class JConfigurator {
  private static final Map<Class<?>, Class<? extends ConfigurationConverter>> defaultConverters = new HashMap<Class<?>, Class<? extends ConfigurationConverter>>();
  private static final Map<Class<?>, Class<? extends CollectionConfigurationConverter>> defaultCollectionConverters = new HashMap<Class<?>, Class<? extends CollectionConfigurationConverter>>();

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

  static {
    defaultCollectionConverters.put(List.class, ListConfigurationConverter.class);
    defaultCollectionConverters.put(Set.class, SetConfigurationConverter.class);
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
    for(Field field : object.getClass().getDeclaredFields()){
      if(field.isAnnotationPresent(Configuration.class) || field.isAnnotationPresent(CollectionConfiguration.class)){
        validate(properties, field);
        field.setAccessible(true);

        if(field.isAnnotationPresent(CollectionConfiguration.class)){
          configureCollectionField(properties, field, object);
        }else if(field.isAnnotationPresent(Configuration.class)){
          configureField(properties, field, object);
        }
      }
    }
  }

  private void configureField(Map<String, String> properties, Field field, Object object) throws ConfigurationException {
    Configuration configuration = field.getAnnotation(Configuration.class);
    ConfigurationConverter converter = getConfigurationConverter(field);
    final String propertyName = getConfigurationName(field);
    final String propertyValue = properties.get(propertyName);
    boolean isFlag = field.isAnnotationPresent(FlagConfiguration.class);

    try{
      if(isFlag){
        field.set(object, properties.containsKey(propertyName));
      }else if(propertyValue != null){
        field.set(object, converter.convert(propertyValue));
      }
    }catch(Exception e){
      throw new ConfigurationException(e.getMessage(), e);
    }
  }

  private void configureCollectionField(Map<String, String> properties, Field field, Object object) throws ConfigurationException {
    CollectionConfigurationConverter converter = getCollectionConfigurationConverter(field);

    try{
      field.set(object, converter.convert(properties.get(getConfigurationName(field))));
    }catch(Exception e){
      throw new ConfigurationException(e.getMessage(), e);
    }
  }

  private void validate(Map<String, String> properties, Field field) throws ConfigurationException {
    if(!field.isAnnotationPresent(Configuration.class) && !field.isAnnotationPresent(CollectionConfiguration.class)){
      throw new ConfigurationException("Field '" + field.getName() + "' of class '" + field.getDeclaringClass().getName() + "' has no valid annotations.");
    }

    if(field.isAnnotationPresent(CollectionConfiguration.class) && !Collection.class.isAssignableFrom(field.getType())){
      throw new ConfigurationException("Cannot annotate a field that is not a subtype of java.util.Collection with @CollectionConfiguration.");
    }

    //Configuration value must be specified if field is marked as required
    final boolean isRequired;
    if(field.isAnnotationPresent(Configuration.class)){
      isRequired = field.getAnnotation(Configuration.class).required();
    }else{
      isRequired = false;
    }

    final String configurationName = getConfigurationName(field);

    if(isRequired){
      if(!properties.containsKey(configurationName) || properties.get(configurationName) == null || properties.get(configurationName).isEmpty()){
        throw new ConfigurationException("Missing configuration value for required configuration field '" + configurationName + "' in class '" + field.getDeclaringClass().getName() + "'.");
      }
    }

    //Check if ConfigurationConverter is defined
    if(null == getConfigurationConverterClass(field)){
      throw new ConfigurationException("No ConfigurationConverter found for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "'.");
    }

    //Check if CollectionConfigurationConverter is defined
    if(field.isAnnotationPresent(CollectionConfiguration.class) && null == getCollectionConfigurationConverterClass(field)){
      throw new ConfigurationException("No CollectionConfigurationConverter found for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "'.");
    }

    //Check @FlagConfiguration compatibility
    final boolean isFlag = field.isAnnotationPresent(FlagConfiguration.class);

    if(isFlag && (!Boolean.class.isAssignableFrom(field.getType()) && !boolean.class.isAssignableFrom(field.getType()))){
        throw new ConfigurationException("Cannot use @FlagConfiguration on non-boolean field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "'.");
    }
  }

  private ConfigurationConverter getConfigurationConverter(Field field) throws ConfigurationException {
    Class<? extends ConfigurationConverter> converterClass = getConfigurationConverterClass(field);
    ConfigurationConverter converter = null;

      try{
        if(defaultConverters.containsValue(converterClass)){
          converter = converterClass.newInstance();
        }
      }catch(Exception e){
        throw new ConfigurationException("Unable to instantiate converter '" + converterClass.getName() + "' declared for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "'.", e);
      }
    
    return converter;
  }

  private CollectionConfigurationConverter getCollectionConfigurationConverter(Field field) throws ConfigurationException {
    CollectionConfigurationConverter collectionConverter = null;  //return variable
    final String delimiter = field.getAnnotation(CollectionConfiguration.class).delimiter();
    ConfigurationConverter configurationConverter = getConfigurationConverter(field);
    Class<? extends CollectionConfigurationConverter> collectionConverterClass = getCollectionConfigurationConverterClass(field);

      try{
        if(defaultCollectionConverters.containsValue(collectionConverterClass)){
          if(!delimiter.isEmpty()){
            collectionConverter = collectionConverterClass.getConstructor(ConfigurationConverter.class, String.class).newInstance(configurationConverter, delimiter);
          }else{
            collectionConverter = collectionConverterClass.getConstructor(ConfigurationConverter.class).newInstance(configurationConverter);
          }
        }
      }catch(Exception e){
        throw new ConfigurationException("Unable to instantiate converter '" + collectionConverterClass.getName() + "' declared for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "'.", e);
      }
    
    return collectionConverter;  
  }

  private Class<? extends CollectionConfigurationConverter> getCollectionConfigurationConverterClass(Field field) throws ConfigurationException {
    CollectionConfiguration config = field.getAnnotation(CollectionConfiguration.class);
    Class<? extends CollectionConfigurationConverter> converterClass = config.collectionConverter() != NoCollectionConfigurationConverter.class ? config.collectionConverter() : defaultCollectionConverters.get(field.getType());
    return converterClass;
  }

  private String getConfigurationName(Field field){
    String name = null;

    if(field.isAnnotationPresent(Configuration.class)){
      Configuration config = field.getAnnotation(Configuration.class);
      name = !"".equals(config.name()) ? config.name() : field.getName();
    }else{
      name = field.getName();
    }

    return name;
  }

  private Class<? extends ConfigurationConverter> getConfigurationConverterClass(Field field) throws ConfigurationException {
    Class<? extends ConfigurationConverter> converterClass = null; //return variable

    if(field.isAnnotationPresent(Configuration.class)){
      Configuration configuration = field.getAnnotation(Configuration.class);
      converterClass = configuration.converter() == NoConfigurationConverter.class ? defaultConverters.get(field.getType()) : configuration.converter();
    }else if(field.isAnnotationPresent(CollectionConfiguration.class)){
      converterClass = defaultConverters.get(String.class);
    }

    return converterClass;
  }
}