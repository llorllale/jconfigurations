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

import org.jconfigurations.ConfigurationException;
import org.jconfigurations.Name;
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
public class DefaultFieldNameFunctionTest {
  
  public DefaultFieldNameFunctionTest() {
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
  public void defaultFieldName() throws Exception {
    final String expected = "name";
    final String result = new DefaultFieldNameFunction().apply(TestClass.class.getDeclaredField("name"));
    assertEquals(expected, result);
  }

  @Test
  public void namedField() throws Exception {
    final String expected = "alice";
    final String result = new DefaultFieldNameFunction().apply(TestClass.class.getDeclaredField("otherName"));
    assertEquals(expected, result);
  }

  @Test(expected = ConfigurationException.class)
  public void errorIfEmptyName() throws Exception {
    new DefaultFieldNameFunction().apply(TestClass.class.getDeclaredField("emptyName"));
  }
  
  private static class TestClass {
    private String name;

    @Name("alice")
    private String otherName;

    @Name("")
    private String emptyName;
  }
}