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

package guru.mmp.application.configuration;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>ConfigurationService</code> class provides the Config Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ConfigurationService
  implements IConfigurationService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
  private String createValueSQL;
  private DataSource dataSource;
  private String getValueSQL;
  private String keyExistsSQL;
  private String updateValueSQL;
  private String getFilteredConfigurationValuesSQL;
  private String getNumberOfFilteredConfigurationEntriesSQL;
  private String getConfigurationValuesSQL;
  private String getNumberOfConfigurationEntriesSQL;
  private String removeValueSQL;

  /**
   * Retrieve the binary configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the binary configuration value
   *
   * @throws ConfigurationNotFoundException
   * @throws ConfigurationException
   */
  public byte[] getBinary(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      return Base64.decode(getString(key));
    }
    catch (ConfigurationNotFoundException e)
    {
      throw new ConfigurationNotFoundException(String.format(
          "The binary configuration value with the key (%s) could not be found", key));
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the binary configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the binary configuration value.
   *
   * @param key          the key used to uniquely identify the configuration value
   * @param defaultValue the default value to return if the configuration value does not exist
   *
   * @return the binary configuration value or the default value if the configuration value does
   *         not exist
   *
   * @throws ConfigurationException
   */
  public byte[] getBinary(String key, byte[] defaultValue)
    throws ConfigurationException
  {
    try
    {
      String stringValue = getString(key, String.valueOf(Base64.encodeBytes(defaultValue)));

      return Base64.decode(stringValue);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the binary configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the <code>Double</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Double</code> configuration value
   *
   * @throws ConfigurationNotFoundException
   * @throws ConfigurationException
   */
  public Double getDouble(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      return Double.valueOf(getString(key));
    }
    catch (ConfigurationNotFoundException e)
    {
      throw new ConfigurationNotFoundException(String.format(
          "The Double configuration value with the key (%s) could not be found", key));
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the Double configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the <code>Double</code> configuration value.
   *
   * @param key          the key used to uniquely identify the configuration value
   * @param defaultValue the default value to return if the configuration value does not exist
   *
   * @return the <code>Double</code> configuration value or the default value if the configuration
   *         entry does not exist
   *
   * @throws ConfigurationException
   */
  public double getDouble(String key, double defaultValue)
    throws ConfigurationException
  {
    try
    {
      String stringValue = getString(key, String.valueOf(defaultValue));

      return Double.valueOf(stringValue);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the Double configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the filtered configuration values
   *
   * @throws ConfigurationException
   */
  public List<ConfigurationValue> getFilteredConfigurationValues(String filter)
    throws ConfigurationException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getConfigurationValuesSQL
          : getFilteredConfigurationValuesSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        statement.setString(1, "%" + filter.toUpperCase() + "%");
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<ConfigurationValue> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(new ConfigurationValue(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the configuration values matching the filter (%s): %s", filter,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the <code>Integer</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Integer</code> configuration value
   *
   * @throws ConfigurationNotFoundException
   * @throws ConfigurationException
   */
  public Integer getInteger(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      return Integer.valueOf(getString(key));
    }
    catch (ConfigurationNotFoundException e)
    {
      throw new ConfigurationNotFoundException(String.format(
          "The Integer configuration value with the key (%s) could not be found", key));
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the Integer configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the <code>Integer</code> configuration value.
   *
   * @param key          the key used to uniquely identify the configuration value
   * @param defaultValue the default value to return if the configuration value does not exist
   *
   * @return the <code>Integer</code> configuration value or the default value if the configuration
   *         entry does not exist
   *
   * @throws ConfigurationException
   */
  public int getInteger(String key, int defaultValue)
    throws ConfigurationException
  {
    try
    {
      String stringValue = getString(key, String.valueOf(defaultValue));

      return Integer.valueOf(stringValue);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the Integer configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the <code>Long</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Long</code> configuration value
   *
   * @throws ConfigurationNotFoundException
   * @throws ConfigurationException
   */
  public Long getLong(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      return Long.valueOf(getString(key));
    }
    catch (ConfigurationNotFoundException e)
    {
      throw new ConfigurationNotFoundException(String.format(
          "The Long configuration value with the key (%s) could not be found", key));
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the Long configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the <code>Long</code> configuration value.
   *
   * @param key          the key used to uniquely identify the configuration value
   * @param defaultValue the default value to return if the configuration value does not exist
   *
   * @return the <code>Long</code> configuration value or the default value if the configuration
   *         entry does not exist
   *
   * @throws ConfigurationException
   */
  public long getLong(String key, long defaultValue)
    throws ConfigurationException
  {
    try
    {
      String stringValue = getString(key, String.valueOf(defaultValue));

      return Long.valueOf(stringValue);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the Long configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the numbered of filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the number of filtered configuration values
   *
   * @throws ConfigurationException
   */
  public int getNumberOfFilteredConfigurationValues(String filter)
    throws ConfigurationException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getNumberOfConfigurationEntriesSQL
          : getNumberOfFilteredConfigurationEntriesSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        statement.setString(1, "%" + filter.toUpperCase() + "%");
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new ConfigurationException(String.format(
              "No rows were returned as a result of executing the SQL statement (%s)",
              getNumberOfFilteredConfigurationEntriesSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the number of configuration values matching the filter (%s): %s",
          filter, e.getMessage()), e);
    }
  }

  /**
   * Retrieve the value for the <code>String</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the value for the <code>String</code> configuration value
   *
   * @throws ConfigurationNotFoundException
   * @throws ConfigurationException
   */
  public String getString(String key)
    throws ConfigurationNotFoundException, ConfigurationException
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
          throw new ConfigurationNotFoundException(String.format(
              "The String configuration value with the key (%s) could not be found", key));
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the String configuration value with the key (%s): %s", key,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the value for the <code>String</code> configuration value.
   *
   * @param key          the key used to uniquely identify the configuration value
   * @param defaultValue the default value to return if the configuration value does not exist
   *
   * @return the value for the <code>String</code> configuration value or the default value if the
   *         configuration value does not exist
   *
   * @throws ConfigurationException
   */
  public String getString(String key, String defaultValue)
    throws ConfigurationException
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
          return defaultValue;
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
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
    logger.info("Initialising the Configuration Service");

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
      throw new RuntimeException("Failed to initialise the Configuration Service", e);
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
   * @throws ConfigurationException
   */
  public boolean keyExists(String key)
    throws ConfigurationException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return keyExists(connection, key);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException("Failed to checked whether the configuration key (" + key
          + ") exists", e);
    }
  }

  /**
   * Remove the configuration value with the specified key.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @throws ConfigurationException
   */
  public void removeValue(String key)
    throws ConfigurationException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(removeValueSQL))
    {
      statement.setString(3, key.toUpperCase());

      if (statement.executeUpdate() <= 0)
      {
        throw new ConfigurationException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            removeValueSQL));
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to remove the configuration value with the key (%s): %s", key, e.getMessage()),
          e);
    }
  }

  /**
   * Set the configuration key to the specified value.
   *
   * @param key         the key used to uniquely identify the configuration value
   * @param value       the value for the configuration value
   * @param description the description for the configuration value
   *
   * @throws ConfigurationException
   */
  public void setValue(String key, Object value, String description)
    throws ConfigurationException
  {
    try (Connection connection = dataSource.getConnection())
    {
      String stringValue;

      if (value instanceof String)
      {
        stringValue = (String) value;
      }
      else if (value instanceof byte[])
      {
        stringValue = Base64.encodeBytes((byte[]) value);
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
          statement.setString(2, description);
          statement.setString(3, key.toUpperCase());

          if (statement.executeUpdate() <= 0)
          {
            throw new ConfigurationException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                updateValueSQL));
          }
        }
      }
      else
      {
        createValue(connection, key, stringValue, description);
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
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
    // createValueSQL
    createValueSQL = "INSERT INTO " + schemaPrefix + "CONFIG (KEY, VALUE, DESCRIPTION) "
        + "VALUES (?, ?, ?)";

    // getConfigurationValuesSQL
    getConfigurationValuesSQL = "SELECT C.KEY, C.VALUE, C.DESCRIPTION FROM " + schemaPrefix
        + "CONFIG C ORDER BY C.KEY";

    // getFilteredConfigurationValuesSQL
    getFilteredConfigurationValuesSQL = "SELECT C.KEY, C.VALUE, C.DESCRIPTION FROM " + schemaPrefix
        + "CONFIG C WHERE (UPPER(C.KEY) LIKE ?) ORDER BY C.KEY";

    // getNumberOfConfigurationEntriesSQL
    getNumberOfConfigurationEntriesSQL = "SELECT COUNT(C.KEY) FROM " + schemaPrefix + "CONFIG C";

    // getNumberOfFilteredConfigurationEntriesSQL
    getNumberOfFilteredConfigurationEntriesSQL = "SELECT COUNT(C.KEY) FROM " + schemaPrefix
        + "CONFIG C WHERE (UPPER(C.KEY) LIKE ?)";

    // getValueSQL
    getValueSQL = "SELECT C.VALUE FROM " + schemaPrefix + "CONFIG C WHERE (UPPER(C.KEY) = ?)";

    // keyExistsSQL
    keyExistsSQL = "SELECT COUNT(C.KEY) FROM " + schemaPrefix
        + "CONFIG C WHERE (UPPER(C.KEY) LIKE ?)";

    // removeValueSQL
    removeValueSQL = "DELETE FROM " + schemaPrefix + "CONFIG C WHERE (UPPER(C.KEY) LIKE ?)";

    // updateValueSQL
    updateValueSQL = "UPDATE " + schemaPrefix + "CONFIG C SET C.VALUE = ?, C.DESCRIPTION = ? "
        + "WHERE (UPPER(C.KEY) = ?)";
  }

  private void createValue(Connection connection, String key, Object value, String description)
    throws SQLException, ConfigurationException
  {
    String stringValue;

    if (value instanceof String)
    {
      stringValue = (String) value;
    }
    else
    {
      stringValue = value.toString();
    }

    try (PreparedStatement statement = connection.prepareStatement(createValueSQL))
    {
      statement.setString(1, key);
      statement.setString(2, stringValue);
      statement.setString(3, description);

      if (statement.executeUpdate() <= 0)
      {
        throw new ConfigurationException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createValueSQL));
      }
    }
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
