/*
 * Copyright 2016 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package guru.mmp.application.config;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.DataAccessObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>ConfigService</code> class provides the Config Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ConfigService
  implements IConfigService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
  private String createValueSQL;
  private DataSource dataSource;
  private String getValueSQL;
  private String keyExistsSQL;
  private String updateValueSQL;

  /**
   * Retrieve the <code>String</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>String</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   *
   * @throws ConfigException
   */
  public String getString(String key)
    throws ConfigException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getValueSQL))
    {
      statement.setString(1, key.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getString(1);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigException(String.format(
          "Failed to retrieve the String configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Initialise the Config Service instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Config Service");

    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored) {}
    }

    if (dataSource == null)
    {
      throw new RuntimeException(
          "Failed to retrieve the application data source using the JNDI names "
          + "(java:app/jdbc/ApplicationDataSource) and (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.MMP_DATABASE_SCHEMA + DAOUtil.getSchemaSeparator(
          dataSource);

      // Build the SQL statements
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Config Service", e);
    }
  }

  /**
   * Check if a configuration value with the specified key exists.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return <code>true</code> if a configuration value with the specified key exists or
   *         <code>false</code> otherwise
   *
   * @throws ConfigException
   */
  public boolean keyExists(String key)
    throws ConfigException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return keyExists(connection, key);
    }
    catch (Throwable e)
    {
      throw new ConfigException("Failed to checked whether the configuration key (" + key
          + ") exists", e);
    }
  }

  /**
   * Set the configuration key to the specified value.
   *
   * @param key   the key used to uniquely identify the configuration value
   * @param value the value
   *
   * @throws ConfigException
   */
  public void setValue(String key, Object value)
    throws ConfigException
  {
    try (Connection connection = dataSource.getConnection())
    {
      String stringValue = null;

      if (value instanceof String)
      {
        stringValue = (String) value;
      }
      else
      {
        stringValue = value.toString();
      }

      if (keyExists(connection, key))
      {
        try (PreparedStatement statement = connection.prepareStatement(updateValueSQL))
        {
          statement.setString(1, stringValue);
          statement.setString(2, key);

          if (statement.executeUpdate() <= 0)
          {
            throw new ConfigException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                updateValueSQL));
          }
        }
      }
      else
      {
        try (PreparedStatement statement = connection.prepareStatement(createValueSQL))
        {
          statement.setString(1, key);
          statement.setString(2, stringValue);

          if (statement.executeUpdate() <= 0)
          {
            throw new ConfigException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                createValueSQL));
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigException(String.format(
          "Failed to set the configuration value with the key (%s): %s", key, e.getMessage()), e);
    }
  }

  /**
   * Generate the SQL statements.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects
   *
   * @throws SQLException
   */
  private void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // keyExistsSQL
    keyExistsSQL = "SELECT COUNT(C.KEY) FROM " + schemaPrefix
        + "CONFIG C WHERE (UPPER(C.KEY) LIKE ?)";

    // createValueSQL
    createValueSQL = "INSERT INTO " + schemaPrefix + "CONFIG (KEY, VALUE) VALUES (?, ?)";

    // getValueSQL
    getValueSQL = "SELECT C.VALUE FROM " + schemaPrefix + "CONFIG C WHERE (UPPER(C.KEY) LIKE ?)";

    // updateValueSQL
    updateValueSQL = "UPDATE " + schemaPrefix + "CONFIG C SET C.VALUE = ? WHERE C.KEY = ?";
  }

  private boolean keyExists(Connection connection, String key)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(keyExistsSQL))
    {
      statement.setString(1, key.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getInt(1) > 0);
        }
        else
        {
          throw new SQLException(String.format(
              "Failed to check whether the configuration value with the key (%s) exists: "
              + "No results were returned as a result of executing the SQL statement (%s)", key,
              keyExistsSQL));
        }
      }
    }
  }
}
