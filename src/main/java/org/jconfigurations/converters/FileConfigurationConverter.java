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
package org.jconfigurations.converters;

import org.jconfigurations.ConfigurationException;
import java.io.File;

/**
 *
 * @author George Aristy
 */
public class FileConfigurationConverter extends BaseConfigurationConverter<File> {
  public FileConfigurationConverter(String optionName) {
    super(optionName);
  }

  @Override
  public File convert(String value) throws ConfigurationException {
    try{
      return new File(value);
    }catch(Exception e){
      throw new ConfigurationException(getErrorMessage(value, Float.class), e);
    }
  }
}