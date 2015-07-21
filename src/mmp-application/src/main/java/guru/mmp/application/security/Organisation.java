/*
 * Copyright 2015 Marcus Portmann
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

package guru.mmp.application.security;

/**
 * The <code>Organisation</code> class stores the information for an organisation.
 *
 * @author Marcus Portmann
 */
public class Organisation
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String code;
  private String description;
  private long id;
  private String name;

  /**
   * Constructs a new <code>Organisation</code>.
   */
  public Organisation() {}

  /**
   * Constructs a new <code>Organisation</code>.
   *
   * @param code the code uniquely identifying the organisation
   */
  public Organisation(String code)
  {
    this.code = code;
  }

  /**
   * Returns the code uniquely identifying the organisation.
   *
   * @return the code uniquely identifying the organisation
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the description for the organisation.
   *
   * @return the description for the organisation
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the unique numeric ID for the organisation.
   *
   * @return the unique numeric ID for the organisation
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the name of the organisation.
   *
   * @return the name of the organisation
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the code uniquely identifying the organisation.
   *
   * @param code the code uniquely identifying the organisation
   */
  public void setCode(String code)
  {
    this.code = code;
  }

  /**
   * Set the description for the organisation.
   *
   * @param description the description for the organisation
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the unique numeric ID for the organisation.
   *
   * @param id the unique numeric ID for the organisation
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the name of the organisation.
   *
   * @param name the name of the organisation
   */
  public void setName(String name)
  {
    this.name = name;
  }
}
