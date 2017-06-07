/*
 * Copyright 2017 Marcus Portmann
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

import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ConfigurationDAO</code> class implements the configuration-related persistence
 * operations.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Repository
public class ConfigurationDAO
  implements IConfigurationDAO
{
  /**
   * The data source used to provide connections to the application database.
   */
  @Inject
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Retrieve the binary configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the binary configuration value or <code>null</code> if the configuration value could
   *         not be found
   */
  public byte[] getBinary(String key)
    throws DAOException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Base64.decode(stringValue);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the binary configuration value with the key (%s) from the database",
          key), e);
    }
  }

  /**
   * Retrieve the <code>Boolean</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Boolean</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  public Boolean getBoolean(String key)
    throws DAOException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Boolean.parseBoolean(stringValue);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the Boolean configuration value with the key (%s) from the database",
          key), e);
    }
  }

  /**
   * Retrieve the <code>Double</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Double</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  public Double getDouble(String key)
    throws DAOException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Double.parseDouble(stringValue);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the Double configuration value with the key (%s) from the database",
          key), e);
    }
  }

  /**
   * Retrieve the filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the filtered configuration values
   */
  public List<ConfigurationValue> getFilteredConfigurationValues(String filter)
    throws DAOException
  {
    String getConfigurationValuesSQL = "SELECT C.\"KEY\", C.VALUE, C.DESCRIPTION FROM "
        + "CONFIGURATION.CONFIGURATION C ORDER BY C.\"KEY\"";

    String getFilteredConfigurationValuesSQL = "SELECT C.\"KEY\", C.VALUE, C.DESCRIPTION FROM "
        + "CONFIGURATION.CONFIGURATION C WHERE (UPPER(C.\"KEY\") LIKE ?) ORDER BY C.\"KEY\"";

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
      throw new DAOException(String.format(
          "Failed to retrieve the configuration values matching the filter (%s) from the database",
          filter), e);
    }
  }

  /**
   * Retrieve the <code>Integer</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Integer</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  public Integer getInteger(String key)
    throws DAOException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Integer.parseInt(stringValue);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the Integer configuration value with the key (%s) from the database",
          key), e);
    }
  }

  /**
   * Retrieve the <code>Long</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Long</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  public Long getLong(String key)
    throws DAOException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Long.parseLong(stringValue);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the Long configuration value with the key (%s) from the database",
          key), e);
    }
  }

  /**
   * Retrieve the numbered of filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the number of filtered configuration values
   */
  public int getNumberOfFilteredConfigurationValues(String filter)
    throws DAOException
  {
    String getNumberOfConfigurationEntriesSQL = "SELECT COUNT(C.\"KEY\") FROM "
        + "CONFIGURATION.CONFIGURATION C";

    String getNumberOfFilteredConfigurationEntriesSQL = "SELECT COUNT(C.\"KEY\") FROM "
        + "CONFIGURATION.CONFIGURATION C WHERE (UPPER(C.\"KEY\") LIKE ?)";

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
          throw new DAOException(String.format(
              "No rows were returned as a result of executing the SQL statement (%s)",
              getNumberOfFilteredConfigurationEntriesSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the number of configuration values matching the filter (%s) from the database",
          filter), e);
    }
  }

  /**
   * Retrieve the value for the <code>String</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the value for the <code>String</code> configuration value or <code>null</code> if the
   *         configuration value could not be found
   */
  public String getString(String key)
    throws DAOException
  {
    String getValueSQL =
        "SELECT C.VALUE FROM CONFIGURATION.CONFIGURATION C WHERE (UPPER(C.\"KEY\") = ?)";

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
      throw new DAOException(String.format(
          "Failed to retrieve the String configuration value with the key (%s) from the database",
          key), e);
    }
  }

  /**
   * Check if a configuration value with the specified key exists.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return <code>true</code> if a configuration value with the specified key exists or
   *         <code>false</code> otherwise
   */
  public boolean keyExists(String key)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return keyExists(connection, key);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to checked whether the configuration key (" + key
          + ") exists in the database", e);
    }
  }

  /**
   * Remove the configuration value with the specified key.
   *
   * @param key the key used to uniquely identify the configuration value
   */
  public void removeValue(String key)
    throws DAOException
  {
    String removeValueSQL = "DELETE FROM CONFIGURATION.CONFIGURATION WHERE (UPPER(\"KEY\") LIKE ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(removeValueSQL))
    {
      statement.setString(1, key.toUpperCase());

      if (statement.executeUpdate() <= 0)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            removeValueSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to remove the configuration value with the key (%s) from the database", key), e);
    }
  }

  /**
   * Set the configuration key to the specified value.
   *
   * @param key         the key used to uniquely identify the configuration value
   * @param value       the value for the configuration value
   * @param description the description for the configuration value
   */
  public void setValue(String key, Object value, String description)
    throws DAOException
  {
    String updateValueSQL = "UPDATE CONFIGURATION.CONFIGURATION SET VALUE = ?, DESCRIPTION = ? "
        + "WHERE (UPPER(\"KEY\") = ?)";

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
          statement.setString(2, StringUtil.notNull(description));
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
      throw new DAOException(String.format(
          "Failed to set the configuration value with the key (%s) in the database", key), e);
    }
  }

  private void createValue(Connection connection, String key, Object value, String description)
    throws SQLException, DAOException
  {
    String createValueSQL = "INSERT INTO CONFIGURATION.CONFIGURATION (\"KEY\", VALUE, DESCRIPTION) "
        + "VALUES (?, ?, ?)";

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
      statement.setString(3, StringUtil.notNull(description));

      if (statement.executeUpdate() <= 0)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createValueSQL));
      }
    }
  }

  private boolean keyExists(Connection connection, String key)
    throws SQLException
  {
    String keyExistsSQL =
        "SELECT COUNT(C.\"KEY\") FROM CONFIGURATION.CONFIGURATION C WHERE (UPPER(C.\"KEY\") LIKE ?)";

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
