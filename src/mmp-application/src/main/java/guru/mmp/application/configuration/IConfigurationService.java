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

import java.util.Map;

/**
 * The <code>IConfigurationService</code> interface defines the functionality provided by a Config Service
 * implementation, which manages the configuration information for an application or service.
 *
 * @author Marcus Portmann
 */
public interface IConfigurationService
{
  /**
   * Retrieve the numbered of filtered <code>String</code> key-value pairs for the configuration
   * values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the number of filtered <code>String</code> key-value pairs for the configuration values
   *
   * @throws ConfigurationException
   */
  int getNumberOfFilteredStrings(String filter)
    throws ConfigurationException;

  /**
   * Retrieve the filtered <code>String</code> key-value pairs for the configuration values.
   *
   * @param filter the filter to apply to the keys for the configuration values
   *
   * @return the filtered <code>String</code> key-value pairs for the configuration values
   *
   * @throws ConfigurationException
   */
  Map<String, String> getFilteredStrings(String filter)
      throws ConfigurationException;

  /**
   * Retrieve the <code>String</code> configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   *
   * @return the <code>String</code> configuration value or <code>null</code> if the configuration
   *         value could not be found
   *
   * @throws ConfigurationException
   */
  String getString(String key)
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
