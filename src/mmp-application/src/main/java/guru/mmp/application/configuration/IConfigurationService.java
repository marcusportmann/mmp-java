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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>IConfigurationService</code> interface defines the functionality provided by a
 * Configuration Service implementation, which manages the configuration information for an
 * application or service.
 *
 * @author Marcus Portmann
 */
public interface IConfigurationService
{
  /**
   * Retrieve the binary configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the binary configuration value or <code>null</code> if the configuration value could
   *         not be found
   *
   * @throws ConfigurationException
   */
  byte[] getBinary(String key)
    throws ConfigurationException;

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
  byte[] getBinary(String key, byte[] defaultValue)
    throws ConfigurationException;

  /**
   * Retrieve the <code>Double</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Double</code> configuration value or <code>null</code> if the configuration
   *         entry could not be found
   *
   * @throws ConfigurationException
   */
  Double getDouble(String key)
    throws ConfigurationException;

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
  double getDouble(String key, double defaultValue)
    throws ConfigurationException;

  /**
   * Retrieve the filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the filtered configuration values
   *
   * @throws ConfigurationException
   */
  List<ConfigurationValue> getFilteredConfigurationValues(String filter)
    throws ConfigurationException;

  /**
   * Retrieve the <code>Integer</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Integer</code> configuration value or <code>null</code> if the configuration
   *         entry could not be found
   *
   * @throws ConfigurationException
   */
  Integer getInteger(String key)
    throws ConfigurationException;

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
  int getInteger(String key, int defaultValue)
    throws ConfigurationException;

  /**
   * Retrieve the <code>Long</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>Long</code> configuration value or <code>null</code> if the configuration
   *         entry could not be found
   *
   * @throws ConfigurationException
   */
  Long getLong(String key)
    throws ConfigurationException;

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
  long getLong(String key, long defaultValue)
    throws ConfigurationException;

  /**
   * Retrieve the numbered of filtered configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the number of filtered configuration values
   *
   * @throws ConfigurationException
   */
  int getNumberOfFilteredConfigurationValues(String filter)
    throws ConfigurationException;

  /**
   * Retrieve the value for the <code>String</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the value for the <code>String</code> configuration value or <code>null</code> if the
   *         configuration value could not be found
   *
   * @throws ConfigurationException
   */
  String getString(String key)
    throws ConfigurationException;

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
  String getString(String key, String defaultValue)
    throws ConfigurationException;

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
  boolean keyExists(String key)
    throws ConfigurationException;

  /**
   * Set the configuration key to the specified value.
   *
   * @param key   the key used to uniquely identify the configuration value
   * @param value the value
   *
   * @throws ConfigurationException
   */
  void setValue(String key, Object value)
    throws ConfigurationException;
}
