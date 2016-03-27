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
package org.jconfigurations.converters;

import java.util.HashMap;
import java.util.Map;
import org.jconfigurations.MapConfiguration;
import org.jconfigurations.configurators.MapConfigurator;
import org.jconfigurations.source.MapConfigurationSource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author George Aristy
 */
public class HashMapConfigurationConverterTest {
  
  public HashMapConfigurationConverterTest() {
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
  public void testSomeMethod() throws Exception {
    Map<String, String> testData = new HashMap<>();
    testData.put("nonAnnotatedIntStringMap", "1=one,2=two,3=three,4=four,5=five");
    testData.put("annotatedStringIntMap", "one=1,two=2,three=3,four=4,five=5");

    TestClass test = new TestClass();
    new MapConfigurator(new MapConfigurationSource(testData)).configure(test);

    assertNull(test.nonAnnotatedIntStringMap);
    assertNotNull(test.annotatedStringIntMap);
    assertEquals(new Integer(1), test.annotatedStringIntMap.get("one"));
    assertEquals(new Integer(2), test.annotatedStringIntMap.get("two"));
    assertEquals(new Integer(3), test.annotatedStringIntMap.get("three"));
    assertEquals(new Integer(4), test.annotatedStringIntMap.get("four"));
    assertEquals(new Integer(5), test.annotatedStringIntMap.get("five"));
  }

  private static class TestClass {
    private Map<Integer, String> nonAnnotatedIntStringMap;
    @MapConfiguration
    private Map<String, Integer> annotatedStringIntMap;
  }
}