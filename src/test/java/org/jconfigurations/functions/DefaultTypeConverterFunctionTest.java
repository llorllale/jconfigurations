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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import org.jconfigurations.converters.BigDecimalConfigurationConverter;
import org.jconfigurations.converters.BigIntegerConfigurationConverter;
import org.jconfigurations.converters.BooleanConfigurationConverter;
import org.jconfigurations.converters.DoubleConfigurationConverter;
import org.jconfigurations.converters.FileConfigurationConverter;
import org.jconfigurations.converters.FloatConfigurationConverter;
import org.jconfigurations.converters.IntegerConfigurationConverter;
import org.jconfigurations.converters.LongConfigurationConverter;
import org.jconfigurations.converters.StringConfigurationConverter;
import org.jconfigurations.converters.URLConfigurationConverter;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author George Aristy
 */
public class DefaultTypeConverterFunctionTest {
  
  public DefaultTypeConverterFunctionTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  @Test
  public void produceCorrectDefaultConfigurationConverters() {
    DefaultTypeConverterFunction f = new DefaultTypeConverterFunction();

    assertEquals(IntegerConfigurationConverter.class, f.apply(Integer.class));
    assertEquals(DoubleConfigurationConverter.class, f.apply(Double.class));
    assertEquals(FloatConfigurationConverter.class, f.apply(Float.class));
    assertEquals(LongConfigurationConverter.class, f.apply(Long.class));
    assertEquals(StringConfigurationConverter.class, f.apply(String.class));
    assertEquals(BooleanConfigurationConverter.class, f.apply(Boolean.class));
    assertEquals(BigDecimalConfigurationConverter.class, f.apply(BigDecimal.class));
    assertEquals(BigIntegerConfigurationConverter.class, f.apply(BigInteger.class));
    assertEquals(FileConfigurationConverter.class, f.apply(File.class));
    assertEquals(URLConfigurationConverter.class, f.apply(URL.class));
  }
}