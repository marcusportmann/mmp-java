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

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IConfigurationDAO</code> interface defines the configuration-related persistence
 * operations.
 *
 * @author Marcus Portmann
 */
public interface IConfigurationDAO
{
  /**
   * Retrieve the binary configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the binary configuration value or <code>null</code> if the configuration value could
   *         not be found
   */
  byte[] getBinary(String key)
    throws DAOException;

  /**
   * Retrieve the <code>Boolean</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Boolean</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  Boolean getBoolean(String key)
    throws DAOException;

  /**
   * Retrieve the <code>Double</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Double</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  Double getDouble(String key)
    throws DAOException;

  /**
   * Retrieve the filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the filtered configuration values
   */
  List<ConfigurationValue> getFilteredConfigurationValues(String filter)
    throws DAOException;

  /**
   * Retrieve the <code>Integer</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Integer</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  Integer getInteger(String key)
    throws DAOException;

  /**
   * Retrieve the <code>Long</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Long</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   */
  Long getLong(String key)
    throws DAOException;

  /**
   * Retrieve the numbered of filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the number of filtered configuration values
   */
  int getNumberOfFilteredConfigurationValues(String filter)
    throws DAOException;

  /**
   * Retrieve the value for the <code>String</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the value for the <code>String</code> configuration value or <code>null</code> if the
   *         configuration value could not be found
   */
  String getString(String key)
    throws DAOException;

  /**
   * Check if a configuration value with the specified key exists.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return <code>true</code> if a configuration value with the specified key exists or
   *         <code>false</code> otherwise
   */
  boolean keyExists(String key)
    throws DAOException;

  /**
   * Remove the configuration value with the specified key.
   *
   * @param key the key used to uniquely identify the configuration value
   */
  void removeValue(String key)
    throws DAOException;

  /**
   * Set the configuration key to the specified value.
   *
   * @param key         the key used to uniquely identify the configuration value
   * @param value       the value for the configuration value
   * @param description the description for the configuration value
   */
  void setValue(String key, Object value, String description)
    throws DAOException;
}
