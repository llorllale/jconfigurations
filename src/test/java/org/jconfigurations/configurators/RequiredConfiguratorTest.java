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
package org.jconfigurations.configurators;

import java.util.HashMap;
import java.util.Map;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.Required;
import org.jconfigurations.source.MapConfigurationSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author George Aristy
 */
public class RequiredConfiguratorTest {
  
  public RequiredConfiguratorTest() {
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
  public void noErrorWhenRequiredIsPresent() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("nonRequiredField", "");
    map.put("requiredField", "");
    new RequiredConfigurator(new MapConfigurationSource(map)).configure(new TestClass());
  }

  @Test
  public void noErrorWhenNonRequiredIsNotPresent() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("requiredField", "");
    new RequiredConfigurator(new MapConfigurationSource(map)).configure(new TestClass());
  }

  @Test(expected = ConfigurationException.class)
  public void errorIfRequiredIsNotPresent() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("nonRequiredField", "");
    new RequiredConfigurator(new MapConfigurationSource(map)).configure(new TestClass());
  }
  
  private static class TestClass {
    private int nonRequiredField;

    @Required
    private double requiredField;
  }
}