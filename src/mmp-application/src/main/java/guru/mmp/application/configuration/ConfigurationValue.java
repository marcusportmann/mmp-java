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

import java.io.Serializable;

/**
 * The <code>ConfigurationValue</code> class stores the key, value and description for a
 * configuration value.
 *
 * @author Marcus Portmann
 */
public class ConfigurationValue
  implements Serializable
{
  /**
   * The key used to uniquely identify the configuration value.
   */
  private String key;

  /**
   * The value for the configuration value.
   */
  private String value;

  /**
   * The description for the configuration value.
   */
  private String description;

  /**
   * Constructs a new <code>ConfigurationValue</code>.
   *
   * @param key         the key used to uniquely identify the configuration value
   * @param value       the value for the configuration value
   * @param description the description for the configuration value
   */
  public ConfigurationValue(String key, String value, String description)
  {
    this.key = key;
    this.value = value;
    this.description = description;
  }

  /**
   * Returns the description for the configuration value.
   *
   * @return the description for the configuration value
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the key used to uniquely identify the configuration value.
   *
   * @return the key used to uniquely identify the configuration value
   */
  public String getKey()
  {
    return key;
  }

  /**
   * Returns the value for the configuration value.
   *
   * @return the value for the configuration value
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the description for the configuration value.
   *
   * @param description the description for the configuration value
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the key used to uniquely identify the configuration value.
   *
   * @param key the key used to uniquely identify the configuration value
   */
  public void setKey(String key)
  {
    this.key = key;
  }

  /**
   * Set the value for the configuration value.
   *
   * @param value the value for the configuration value
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}
