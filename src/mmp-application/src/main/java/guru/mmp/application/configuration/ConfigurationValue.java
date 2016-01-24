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
 * The <code>ConfigurationValue</code> class stores the <code>String</code> key and
 * <code>String</code> value for a configuration value.
 *
 * @author Marcus Portmann
 */
public class ConfigurationValue
  implements Serializable
{
  /**
   * The <code>String</code> key for the configuration value.
   */
  private String key;

  /**
   * The <code>String</code> value for the configuration value.
   */
  private String value;

  /**
   * Constructs a new <code>ConfigurationValue</code>.
   *
   * @param key   the <code>String</code> key for the configuration value
   * @param value the <code>String</code> value for the configuration value
   */
  public ConfigurationValue(String key, String value)
  {
    this.key = key;
    this.value = value;
  }

  /**
   * Returns the <code>String</code> key for the configuration value.
   *
   * @return the <code>String</code> key for the configuration value
   */
  public String getKey()
  {
    return key;
  }

  /**
   * Returns the <code>String</code> value for the configuration value.
   *
   * @return the <code>String</code> value for the configuration value
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the <code>String</code> key for the configuration value.
   *
   * @param key the <code>String</code> key for the configuration value
   */
  public void setKey(String key)
  {
    this.key = key;
  }

  /**
   * Set the <code>String</code> value for the configuration value.
   *
   * @param value the <code>String</code> value for the configuration value
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}
