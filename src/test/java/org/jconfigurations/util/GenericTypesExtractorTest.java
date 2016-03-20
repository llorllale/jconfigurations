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
package org.jconfigurations.util;

import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

/**
 *
 * @author George Aristy
 */
public class GenericTypesExtractorTest {
  
  public GenericTypesExtractorTest() {
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
  public void genericList() throws Exception {
    List<Class<?>> test = new GenericTypesExtractor(TestClass.class.getDeclaredField("strList"))
            .getGenericOrRawTypes();

    assertThat(test.size(), is(1));
    assertThat(test, hasItem(String.class));
  }

  @Test
  public void genericMap() throws Exception {
    List<Class<?>> test = new GenericTypesExtractor(TestClass.class.getDeclaredField("strObjMap"))
            .getGenericOrRawTypes();

    assertThat(test.size(), is(2));
    assertThat(test, hasItems(String.class, Object.class));
    assertThat(test.get(0), is(equalTo(String.class)));
    assertThat(test.get(1), is(equalTo(Object.class)));
  }

  @Test
  public void rawList() throws Exception {
    List<Class<?>> test = new GenericTypesExtractor(TestClass.class.getDeclaredField("rawList"))
            .getGenericOrRawTypes();

    assertThat(test.size(), is(1));
    assertThat(test, hasItem(List.class));
  }

  @Test
  public void rawMap() throws Exception {
    List<Class<?>> test = new GenericTypesExtractor(TestClass.class.getDeclaredField("rawMap"))
            .getGenericOrRawTypes();

    assertThat(test.size(), is(1));
    assertThat(test, hasItem(Map.class));
  }
  
  private static class TestClass {
    private List<String> strList;
    private List rawList;
    private List<Integer> intList;
    private Map<String, Object> strObjMap;
    private Map rawMap;
  }
}
