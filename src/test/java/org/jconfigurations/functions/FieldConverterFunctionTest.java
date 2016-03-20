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

import java.time.Instant;
import java.time.format.DateTimeParseException;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import org.jconfigurations.Configuration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.IntegerConfigurationConverter;
import org.jconfigurations.converters.NoConfigurationConverter;
import org.jconfigurations.converters.StringConfigurationConverter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author George Aristy
 */
public class FieldConverterFunctionTest {
  
  public FieldConverterFunctionTest() {
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
  public void nonAnnotatedField() throws Exception {
    FieldConverterFunction f = new FieldConverterFunction();

    assertThat(
            f.apply(TestClass.class.getDeclaredField("nonAnnotatedField")), 
            is(instanceOf(IntegerConfigurationConverter.class))
    );
  }

  @Test
  public void annotatedField() throws Exception {
    FieldConverterFunction f = new FieldConverterFunction();

    assertThat(
            f.apply(TestClass.class.getDeclaredField("annotatedField")), 
            is(instanceOf(StringConfigurationConverter.class))
    );
  }

  @Test
  public void knownCustomConverterField() throws Exception {
    FieldConverterFunction f = new FieldConverterFunction();

    assertThat(
            f.apply(TestClass.class.getDeclaredField("customConverterField")), 
            is(instanceOf(TestConverter.class))
    );
  }

  @Test
  public void unknownCustomConverterField() throws Exception {
    FieldConverterFunction f = new FieldConverterFunction();

    assertThat(
            f.apply(TestClass.class.getDeclaredField("unknownCustomConverterField")), 
            is(instanceOf(NoConfigurationConverter.class))
    );
  }
  
  private static class TestClass {
    private int nonAnnotatedField;
    @Configuration
    private String annotatedField;
    @Configuration(converter = TestConverter.class)
    private Instant customConverterField;
    private Instant unknownCustomConverterField;
  }

  private static class TestConverter implements ConfigurationConverter<Instant> {
    public TestConverter(){}

    @Override
    public Instant convert(String value) throws ConfigurationException {
      try{
        return Instant.parse(value);
      }catch(DateTimeParseException e){
        throw new ConfigurationException(e.getMessage(), e);
      }
    }
  }
}