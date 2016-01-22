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
 * The <code>ConfigurationEntry</code> class stores the key and value for a configuration entry.
 */
public class ConfigurationEntry
  implements Serializable
{
  /**
   * The key for the configuration entry.
   */
  private String key;

  /**
   * The value for the configuration entry.
   */
  private String value;

  /**
   * Constructs a new <code>ConfigurationEntry</code>.
   *
   * @param key   the key for the configuration entry
   * @param value the value for the configuration entry
   */
  public ConfigurationEntry(String key, String value)
  {
    this.key = key;
    this.value = value;
  }

  /**
   * Returns the key for the configuration entry.
   *
   * @return the key for the configuration entry
   */
  public String getKey()
  {
    return key;
  }

  /**
   * Returns the value for the configuration entry.
   *
   * @return the value for the configuration entry
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the key for the configuration entry.
   *
   * @param key the key for the configuration entry
   */
  public void setKey(String key)
  {
    this.key = key;
  }

  /**
   * Set the value for the configuration entry.
   *
   * @param value the value for the configuration entry
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}
