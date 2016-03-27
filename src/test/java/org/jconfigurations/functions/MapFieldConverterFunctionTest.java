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

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.MapConfiguration;
import org.jconfigurations.configurators.MapConfigurator;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.MapConfigurationConverter;
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
public class MapFieldConverterFunctionTest {
  
  public MapFieldConverterFunctionTest() {
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
    final Map<String, String> testData = new HashMap<>();
    testData.put("nonAnnotatedRawMap", "1=2,3=4,5=6");
    testData.put("annotatedIntStringMap", "1=one,2=two,3=three,4=four,5=five");
    testData.put("annotatedIntStringCustomMap", "1=one,2=two,3=three,4=four,5=five");
    Date date = Date.valueOf(LocalDate.now());
    testData.put("annotatedIntSQLDateCustomMap", "1=" + date.toString());

    TestClass test = new TestClass();
    new MapConfigurator(new MapConfigurationSource(testData)).configure(test);

    assertNull(test.nonAnnotatedRawMap);

    assertNotNull(test.annotatedIntStringMap);
    assertEquals("one", test.annotatedIntStringMap.get(1));
    assertEquals("two", test.annotatedIntStringMap.get(2));
    assertEquals("three", test.annotatedIntStringMap.get(3));
    assertEquals("four", test.annotatedIntStringMap.get(4));
    assertEquals("five", test.annotatedIntStringMap.get(5));

    assertNotNull(test.annotatedIntStringCustomMap);
    assertEquals(TreeMap.class, test.annotatedIntStringCustomMap.getClass());
    assertEquals("one", test.annotatedIntStringCustomMap.get(1));
    assertEquals("two", test.annotatedIntStringCustomMap.get(2));
    assertEquals("three", test.annotatedIntStringCustomMap.get(3));
    assertEquals("four", test.annotatedIntStringCustomMap.get(4));
    assertEquals("five", test.annotatedIntStringCustomMap.get(5));

    assertNotNull(test.annotatedIntSQLDateCustomMap);
    assertEquals(TreeMap.class, test.annotatedIntSQLDateCustomMap.getClass());
    assertEquals(date, test.annotatedIntSQLDateCustomMap.get(1));
  }
  
  private static class TestClass {
    private Map nonAnnotatedRawMap;
    @MapConfiguration
    private Map<Integer, String> annotatedIntStringMap;
    @MapConfiguration(converter = TreemapConfigurationConverter.class)
    private Map<Integer, String> annotatedIntStringCustomMap;
    @MapConfiguration(converter = TreemapConfigurationConverter.class, valueConverter = SQLDateConfigurationConverter.class)
    private Map<Integer, Date> annotatedIntSQLDateCustomMap;
  }

  private static class TreemapConfigurationConverter<K,V> implements MapConfigurationConverter<K,V> {
    private final ConfigurationConverter<K> keyConverter;
    private final ConfigurationConverter<V> valueConverter;
    private final String entryDelimiter;
    private final String keyValueSeparator;

    public TreemapConfigurationConverter(
            ConfigurationConverter<K> keyConverter, 
            ConfigurationConverter<V> valueConverter, 
            String entryDelimiter, 
            String keyValueSeparator
    ) {
      this.keyConverter = keyConverter;
      this.valueConverter = valueConverter;
      this.entryDelimiter = entryDelimiter;
      this.keyValueSeparator = keyValueSeparator;
    }

    @Override
    public Map<K, V> convert(String value) throws ConfigurationException {
      Map<K, V> result = new TreeMap<>();

      for(String entry : value.split(entryDelimiter)){
        String[] keyValue = entry.split(keyValueSeparator);
        String k = keyValue[0];
        String v = keyValue.length > 1 ? keyValue[1] : null;

        result.put(keyConverter.convert(k), valueConverter.convert(v));
      }

      return result;
    }
  }

  private static class SQLDateConfigurationConverter implements ConfigurationConverter<Date> {
    public SQLDateConfigurationConverter(){}

    @Override
    public Date convert(String value) throws ConfigurationException {
      return Date.valueOf(value);
    }
  }
}