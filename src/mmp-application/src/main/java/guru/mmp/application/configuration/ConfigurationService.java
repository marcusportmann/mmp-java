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

import guru.mmp.application.codes.ICodesDAO;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ConfigurationService</code> class provides the Configuration Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class ConfigurationService
  implements IConfigurationService
{
  /* Configuration DAO */
  @Autowired
  private IConfigurationDAO configurationDAO;

  /**
   * Retrieve the binary configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the binary configuration value
   */
  public byte[] getBinary(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      byte[] binaryValue = configurationDAO.getBinary(key);

      if (binaryValue == null)
      {
        throw new ConfigurationNotFoundException(String.format(
          "The binary configuration value with the key (%s) could not be found", key));
      }
      else
      {
        return binaryValue;
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the binary configuration value with the key (%s)", key
          ), e);
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
   */
  public byte[] getBinary(String key, byte[] defaultValue)
    throws ConfigurationException
  {
    try
    {
      byte[] binaryValue = configurationDAO.getBinary(key);

      if (binaryValue == null)
      {
        throw new ConfigurationNotFoundException(String.format(
          "The binary configuration value with the key (%s) could not be found", key));
      }
      else
      {
        return binaryValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the binary configuration value with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Boolean</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Boolean</code> configuration value
   */
  public boolean getBoolean(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      Boolean booleanValue = configurationDAO.getBoolean(key);

      if (booleanValue == null)
      {
        throw new ConfigurationNotFoundException(String.format(
          "The Boolean configuration value with the key (%s) could not be found", key));
      }
      else
      {
        return booleanValue;
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Boolean configuration value with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Boolean</code> configuration value.
   *
   * @param key          the key used to uniquely identify the configuration value
   * @param defaultValue the default value to return if the configuration value does not exist
   *
   * @return the <code>Boolean</code> configuration value or the default value if the configuration
   *         value does not exist
   */
  public boolean getBoolean(String key, boolean defaultValue)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      Boolean booleanValue = configurationDAO.getBoolean(key);

      if (booleanValue == null)
      {
        return defaultValue;
      }
      else
      {
        return booleanValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Boolean configuration value with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Double</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Double</code> configuration value
   */
  public Double getDouble(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      Double doubleValue = configurationDAO.getDouble(key);

      if (doubleValue == null)
      {
        throw new ConfigurationNotFoundException(String.format(
          "The Double configuration value with the key (%s) could not be found", key));
      }
      else
      {
        return doubleValue;
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Double configuration value with the key (%s)", key), e);
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
   */
  public double getDouble(String key, double defaultValue)
    throws ConfigurationException
  {
    try
    {
      Double doubleValue = configurationDAO.getDouble(key);

      if (doubleValue == null)
      {
        return defaultValue;
      }
      else
      {
        return doubleValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Double configuration value with the key (%s)", key), e);
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
    throws ConfigurationException
  {
    try
    {
      return configurationDAO.getFilteredConfigurationValues(filter);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the configuration values matching the filter (%s)", filter), e);
    }
  }

  /**
   * Retrieve the <code>Integer</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Integer</code> configuration value
   */
  public Integer getInteger(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      Integer integerValue = configurationDAO.getInteger(key);

      if (integerValue == null)
      {
        throw new ConfigurationNotFoundException(String.format(
          "The Integer configuration value with the key (%s) could not be found", key));
      }
      else
      {
        return integerValue;
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Integer configuration value with the key (%s)", key), e);
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
   */
  public int getInteger(String key, int defaultValue)
    throws ConfigurationException
  {
    try
    {
      Integer integerValue = configurationDAO.getInteger(key);

      if (integerValue == null)
      {
        return defaultValue;
      }
      else
      {
        return integerValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Integer configuration value with the key (%s)", key), e);
    }  }

  /**
   * Retrieve the <code>Long</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Long</code> configuration value
   */
  public Long getLong(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      Long longValue = configurationDAO.getLong(key);

      if (longValue == null)
      {
        throw new ConfigurationNotFoundException(String.format(
          "The Long configuration value with the key (%s) could not be found", key));
      }
      else
      {
        return longValue;
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Long configuration value with the key (%s)", key), e);
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
   */
  public long getLong(String key, long defaultValue)
    throws ConfigurationException
  {
    try
    {
      Long longValue = configurationDAO.getLong(key);

      if (longValue == null)
      {
        return defaultValue;
      }
      else
      {
        return longValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the Long configuration value with the key (%s)", key), e);
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
    throws ConfigurationException
  {
    try
    {
      return configurationDAO.getNumberOfFilteredConfigurationValues(filter);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to retrieve the number of configuration values matching the filter (%s)",
          filter), e);
    }
  }

  /**
   * Retrieve the value for the <code>String</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the value for the <code>String</code> configuration value
   */
  public String getString(String key)
    throws ConfigurationNotFoundException, ConfigurationException
  {
    try
    {
      String stringValue = configurationDAO.getString(key);

      if (stringValue == null)
      {
        throw new ConfigurationNotFoundException(String.format(
          "The String configuration value with the key (%s) could not be found", key));
      }
      else
      {
        return stringValue;
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the String configuration value with the key (%s)", key), e);
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
   */
  public String getString(String key, String defaultValue)
    throws ConfigurationException
  {
    try
    {
      String stringValue = configurationDAO.getString(key);

      if (stringValue == null)
      {
        return defaultValue;
      }
      else
      {
        return stringValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
        "Failed to retrieve the String configuration value with the key (%s)", key), e);
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
    throws ConfigurationException
  {
    try
    {
      return configurationDAO.keyExists(key);
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
   */
  public void removeValue(String key)
    throws ConfigurationException
  {
    try
    {
      configurationDAO.removeValue(key);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to remove the configuration value with the key (%s)", key),
          e);
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
    throws ConfigurationException
  {
    try
    {
      configurationDAO.setValue(key, value, description);
    }
    catch (Throwable e)
    {
      throw new ConfigurationException(String.format(
          "Failed to set the configuration value with the key (%s)]", key), e);
    }
  }
}
