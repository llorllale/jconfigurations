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
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import org.jconfigurations.CollectionConfiguration;
import org.jconfigurations.ConfigurationException;
import org.jconfigurations.converters.CollectionConfigurationConverter;
import org.jconfigurations.converters.ConfigurationConverter;
import org.jconfigurations.converters.ListConfigurationConverter;
import org.jconfigurations.converters.NoCollectionConfigurationConverter;
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
public class CollectionFieldConverterFunctionTest {
  
  public CollectionFieldConverterFunctionTest() {
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
  public void noConverterIfNoAnnotation() throws Exception {
    CollectionFieldConverterFunction f = new CollectionFieldConverterFunction();
    assertEquals(
            NoCollectionConfigurationConverter.class, 
            f.apply(TestClass.class.getDeclaredField("nonAnnotatedRawList")).getClass()
    );
  }

  @Test
  public void createCorrectConverterFromGenericAnnotatedField() throws Exception {
    CollectionFieldConverterFunction f = new CollectionFieldConverterFunction();
    assertEquals(
            ListConfigurationConverter.class, 
            f.apply(TestClass.class.getDeclaredField("annotatedIntList")).getClass()
    );
  }

  @Test
  public void convertGenericAnnotatedField() throws Exception {
    final List<Integer> testData = Arrays.asList(1, 2, 3, 4, 5);
    final String testDataString = testData.stream()
            .map(String::valueOf)
            .reduce((a, b) -> a + "," + b)
            .get();

    CollectionConfigurationConverter converter = new CollectionFieldConverterFunction()
            .apply(TestClass.class.getDeclaredField("annotatedIntList"));

    assertEquals(testData, converter.convert(testDataString));
  }

  @Test
  public void convertRawAnnotatedField() throws Exception {
    final Set testData = new HashSet();
    testData.add("1");
    testData.add("2");
    testData.add("3");
    testData.add("4");
    testData.add("5");

    final String testDataString = (String) testData.stream()
            .map(String::valueOf)
            .reduce((a, b) -> a + "," + b)
            .get();

    CollectionConfigurationConverter converter = new CollectionFieldConverterFunction()
            .apply(TestClass.class.getDeclaredField("annotatedRawSet"));

    assertEquals(testData, converter.convert(testDataString));
  }

  @Test
  public void convertAnnotatedCustomCollectionField() throws Exception {
    final List<String> testData = Arrays.asList("A:\\", "B:\\", "C:\\", "D:\\", "E:\\");
    final String testDataString = testData.stream().reduce((a,b) -> a + "," + b).get();
    final Queue<File> refQueue = new ArrayBlockingQueue<>(testData.size());
    testData.stream()
            .map(File::new)
            .forEach(refQueue::add);

    CollectionFieldConverterFunction f = new CollectionFieldConverterFunction();

    final Queue<File> testQueue = (Queue<File>) f.apply(TestClass.class.getDeclaredField("annotatedCustomCollectionField"))
            .convert(testDataString);

    testQueue.stream().forEach((o) -> {
      assertEquals(refQueue.poll(), o);
    });
  }

  @Test
  public void convertAnnotatedCustomCollectionAndGenericTypeField() throws Exception {
    final List<Instant> testData = Arrays.asList(
            Instant.now(), 
            Instant.ofEpochMilli(System.currentTimeMillis()),
            Instant.EPOCH,
            Instant.MAX,
            Instant.MIN
    );
    final String testDataString = testData.stream()
            .map(Instant::toString)
            .reduce((a,b) -> a + "," + b)
            .get();
    final Queue<Instant> refQueue = new ArrayBlockingQueue<>(testData.size());
    testData.stream().forEach(refQueue::add);

    CollectionFieldConverterFunction f = new CollectionFieldConverterFunction();

    final Queue<Instant> testQueue = (Queue<Instant>) f.apply(TestClass.class.getDeclaredField("annotatedCustomCollectionAndGenericTypeField"))
            .convert(testDataString);

    testQueue.stream().forEach((o) -> {
      assertEquals(refQueue.poll(), o);
    });
  }
  
  private static class TestClass {
    private List nonAnnotatedRawList;
    @CollectionConfiguration
    private List<Integer> annotatedIntList;
    @CollectionConfiguration
    private Set annotatedRawSet;
    @CollectionConfiguration(converter = QueueCollectionConverter.class)
    private Queue<File> annotatedCustomCollectionField;
    @CollectionConfiguration(converter = QueueCollectionConverter.class, elementConverter = InstantConfigurationConverter.class)
    private Queue<Instant> annotatedCustomCollectionAndGenericTypeField;
  }

  private static class QueueCollectionConverter implements CollectionConfigurationConverter<Queue> {
    private final ConfigurationConverter elementConverter;
    private final String delimiter;

    public QueueCollectionConverter(ConfigurationConverter elementConverter, String delimiter){
      this.elementConverter = elementConverter;
      this.delimiter = delimiter;
    }

    @Override
    public Collection<Queue> convert(String value) throws ConfigurationException {
      final Queue queue = new ArrayBlockingQueue(5);

      for(String v : value.split(delimiter)){
        queue.add(elementConverter.convert(v));
      }

      return queue;
    }
  }

  private static class InstantConfigurationConverter implements ConfigurationConverter<Instant> {
    public InstantConfigurationConverter(){

    }

    @Override
    public Instant convert(String value) throws ConfigurationException {
      return Instant.parse(value);
    }
  }
}