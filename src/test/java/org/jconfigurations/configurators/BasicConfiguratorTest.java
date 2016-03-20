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

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jconfigurations.Configuration;
import org.jconfigurations.source.MapConfigurationSource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author George Aristy
 */
public class BasicConfiguratorTest {
  
  public BasicConfiguratorTest() {
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
  public void checkCorrectConfiguration() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("nonAnnotatedField", "some string");
    map.put("intField", "4");
    map.put("stringField", "another string");
    map.put("fileField", ".");

    TestClass test = new TestClass();
    new BasicConfigurator(new MapConfigurationSource(map)).configure(test);

    assertNull(test.nonAnnotatedField);
    assertEquals(4, test.intField);
    assertEquals("another string", test.stringField);
    assertEquals(new File("."), test.fileField);
  }

  @Test(expected = NullPointerException.class)
  public void errorIfNullConfigurationSource(){
    new BasicConfigurator(null);
  }

  @Test(expected = NullPointerException.class)
  public void errorIfNullObject() throws Exception {
    new BasicConfigurator(
        () -> Collections.unmodifiableMap(new HashMap<>())
    ).configure(null);
  }

  private static class TestClass {
    private String nonAnnotatedField;
    @Configuration
    private int intField;
    @Configuration
    private String stringField;
    @Configuration
    private File fileField;
  }
}