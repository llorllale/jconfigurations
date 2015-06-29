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

import org.jconfigurations.converters.BooleanConfigurationConverter;
import org.jconfigurations.converters.FileConfigurationConverter;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author George Aristy
 */
public class ConfiguratorTest {
  
  public ConfiguratorTest() {
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

  /**
   * Test of configure method, of class JConfigurator.
   */
  @Test
  public void testHappyPath() throws Exception {
    Map<String, String> config = new HashMap<String, String>();
    config.put("string", "Bob");
    config.put("integer", "11");
    config.put("_long", "1234");
    config.put("file", System.getProperty("java.home"));
    config.put("bigDecimal", "23456666666");
    config.put("bigInteger", "29895892345");
    config.put("bool", "true");
    config.put("_double", "2899234");
    config.put("_float", "2394892834");

    JConfigurator c = new JConfigurator();
    HappyPath myConfig = new HappyPath();
    c.configure(config, myConfig);

    assertEquals("Bob", myConfig.string);
    assertEquals(11, myConfig.integer);
    assertEquals(1234L, myConfig._long);
    assertEquals(new File(System.getProperty("java.home")), myConfig.file);
    assertEquals(new BigDecimal(23456666666L), myConfig.bigDecimal);
    assertEquals(BigInteger.valueOf(29895892345L), myConfig.bigInteger);
    assertEquals(true, myConfig.bool);
    assertEquals(2899234D, myConfig._double, 0L);
    assertEquals(2394892834F, myConfig._float, 0L);
  }

  @Test
  public void testHappyPathWithParameters() throws Exception {
    Map<String, String> config = new HashMap<String, String>();
    config.put("string", "Cat");
    config.put("integer", "12345");
    config.put("file", System.getProperty("java.home"));
    config.put("bool", "true");

    JConfigurator c = new JConfigurator();
    HappyPathWithParameters happy = new HappyPathWithParameters();
    c.configure(config, happy);
    
    assertEquals("Cat", happy.string);
    assertEquals(12345, happy.integer);
    assertEquals(new File(System.getProperty("java.home")), happy.file);
    assertEquals(true, happy.bool);
    assertFalse(happy.doThat);
  }

  @Test
  public void testMismatchedNames() throws Exception {
    Map<String, String> config = new HashMap<String, String>();
    config.put("person.name", "Bob");
    config.put("person.age", "20");
    config.put("doThis", "true");

    MismatchedNames names = new MismatchedNames();
    new JConfigurator().configure(config, names);

    assertEquals("Bob", names.string);
    assertEquals(20, names.integer);
    assertTrue(names.doThat);
  }

  @Test(expected = ConfigurationException.class)
  public void testRequiredConfiguration() throws Exception {
    Map<String, String> config = new HashMap<String, String>();
    config.put("person.phoneNumber", "8090000000");
    config.put("person.age", "20");

    RequiredConfigurations rc = new RequiredConfigurations();
    new JConfigurator().configure(config, rc); //throws exception
  }

  @Test
  public void testCollectionConfiguration() throws Exception {
    class Test {
      @CollectionConfiguration(delimiter = ",")
      List<String> names;
      @Configuration(converter = FileConfigurationConverter.class)
      @CollectionConfiguration(delimiter = ":")
      Set<File> files;
    }

    Map<String, String> config = new HashMap<String, String>();
    config.put("names", "George,Peter,Bill");
    config.put("files", "/tmp/test.txt:/usr/bin/java");

    Test test = new Test();
    new JConfigurator().configure(config, test);
    assertEquals(3, test.names.size());
    assertTrue(test.names.contains("George"));
    assertTrue(test.names.contains("Peter"));
    assertTrue(test.names.contains("Bill"));
    assertEquals(2, test.files.size());
    test.files.iterator().next().isFile();
  }
  
  private static class HappyPath {
    @Configuration
    private String string;
    @Configuration
    private int integer;
    @Configuration
    private long _long;
    @Configuration
    private File file;
    @Configuration
    private BigDecimal bigDecimal;
    @Configuration
    private BigInteger bigInteger;
    @Configuration
    private boolean bool;
    @Configuration
    private double _double;
    @Configuration
    private float _float;
  }

  private static class HappyPathWithParameters {
    @Configuration(name = "string")
    private String string;
    @Configuration(required = true)
    private int integer;
    @Configuration(converter = FileConfigurationConverter.class)
    private File file;
    @Configuration(name = "bool", required = true, converter = BooleanConfigurationConverter.class)
    private boolean bool;
    @Configuration
    @FlagConfiguration
    private boolean doThat;

  }

  private static class MismatchedNames {
    @Configuration(name = "person.name")
    private String string;
    @Configuration(name = "person.age")
    private int integer;
    @Configuration(name = "doThis")
    @FlagConfiguration
    private boolean doThat;

  }

  private static class RequiredConfigurations {
    @Configuration(name = "person.name", required = true)
    private String string;
    @Configuration(name = "person.age")
    private int integer;
    @Configuration(name = "doThis", required = true)
    @FlagConfiguration
    private boolean doThat;
  }
}