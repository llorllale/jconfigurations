/*
 * Copyright 2017 George Aristy (george.aristy@gmail.com).
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

package org.jconfigurations.source;

import com.mockrunner.mock.jdbc.MockResultSet;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author George Aristy (george.aristy@gmail.com)
 */
public class JdbcConfigurationSourceTest {
  private static final String[] KEYS = new String[]{"a", "b", "c", "d"};
  private static final Integer[] VALUES = new Integer[]{1, 2, 3, 4};
  private MockResultSet mock;

  @Before
  public void before() {
    mock = new MockResultSet("mock");
    mock.addColumn("key", KEYS);
    mock.addColumn("value", VALUES);
  }

  @After
  public void after() throws Exception {
    mock.close();
  }

  @Test
  public void testConfigurationsSize() throws Exception {
    assertThat(
        new JdbcConfigurationSource(mock)
            .configurations()
            .size(),
        is(KEYS.length)
    );
  }

  @Test
  public void testConfigurationsValue() throws Exception {
    assertThat(
        new JdbcConfigurationSource(mock)
            .configurations()
            .get(KEYS[0]),
        is(String.valueOf(VALUES[0]))
    );
  }
}