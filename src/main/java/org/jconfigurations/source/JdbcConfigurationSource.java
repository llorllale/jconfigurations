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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ConfigurationSource} implementation that reads configurations from JDBC interfaces.
 * @author George Aristy (george.aristy@gmail.com)
 */
public class JdbcConfigurationSource implements ConfigurationSource {
  private final Map<String, String> configurations;

  /**
   * <p>Primary constructor.</p>
   * 
   * <p>The configurations are eagerly loaded and therefore {@link #configurations()} can be 
   * called safely.</p>
   * @param result a {@link ResultSet} containing <strong>at least</strong> two columns with values
   *     that can be retrieved like {@link ResultSet#getString(int) strings}
   * @throws SQLException if a JDBC error occurs
   */
  public JdbcConfigurationSource(ResultSet result) throws SQLException {
    this.configurations = new HashMap<>();

    while(result.next()){
      this.configurations.put(result.getString(1), result.getString(2));
    }
  }

  /**
   * Same as calling {@code new JdbcConfigurationSource(stmt.executeQuery())}.
   * @param stmt a {@link PreparedStatement} with any necessary parameters already binded
   * @throws SQLException if a JDBC error occurs
   * @see #JdbcConfigurationSource(java.sql.ResultSet) 
   */
  public JdbcConfigurationSource(PreparedStatement stmt) throws SQLException {
    this(stmt.executeQuery());
  }

  /**
   * Same as calling {@code new JdbcConfigurationSource(conn.prepareStatement(sql))}.
   * @param conn a {@link Connection jdbc connection}
   * @param sql an SQL statement that requires no parameters to be bound and can be executed as-is
   * @throws SQLException if a JDBC error occurs
   * @see #JdbcConfigurationSource(java.sql.PreparedStatement) 
   */
  public JdbcConfigurationSource(Connection conn, String sql) throws SQLException {
    this(conn.prepareStatement(sql));
  }

  @Override
  public Map<String, String> configurations() {
    return Collections.unmodifiableMap(this.configurations);
  }
}
