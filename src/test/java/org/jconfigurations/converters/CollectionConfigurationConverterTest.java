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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jconfigurations.CollectionConfiguration;
import org.jconfigurations.configurators.CollectionConfigurator;
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
public class CollectionConfigurationConverterTest {
  
  public CollectionConfigurationConverterTest() {
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
    final Map<String, String> map = new HashMap<>();
    map.put("nonAnnotatedIntList", "1,2,3,4,5");
    map.put("annotatedIntList", "1,2,3,4,5");
    map.put("annotatedStringSet", "one,two,three,four,five");

    TestClass test = new TestClass();
    new CollectionConfigurator(new MapConfigurationSource(map)).configure(test);

    assertNull(test.nonAnnotatedIntList);
    assertEquals(5, test.annotatedIntList.size());
    assertEquals(test.annotatedIntList, Arrays.asList(1, 2, 3, 4, 5));
    assertEquals(test.annotatedStringSet, new HashSet<>(Arrays.asList("one", "two", "three", "four", "five")));
  }

  @Test(expected = NullPointerException.class)
  public void errorIfNullConfigurationSource(){
    new CollectionConfigurator(null);
  }

  @Test(expected = NullPointerException.class)
  public void errorIfNullInputObject() throws Exception {
    new CollectionConfigurator(new MapConfigurationSource(new HashMap<>()))
            .configure(null);
  }
  
  private static class TestClass {
    private List<Integer> nonAnnotatedIntList;
    @CollectionConfiguration
    private List<Integer> annotatedIntList;
    @CollectionConfiguration
    private Set<String> annotatedStringSet;
  }
}