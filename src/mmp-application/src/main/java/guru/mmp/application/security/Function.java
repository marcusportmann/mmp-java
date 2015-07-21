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
 * The <code>Function</code> class stores the information for an authorised function that can be
 * assigned to <code>Account</code>s and <code>Role</code>s.
 *
 * @author Marcus Portmann
 */
public class Function
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String code;
  private String description;
  private long id;
  private String name;

  /**
   * Constructs a new <code>Function</code>.
   *
   * @param code the code for the function
   */
  public Function(String code)
  {
    this.code = code;
  }

  /**
   * Constructs a new <code>Function</code>.
   *
   * @param code        the code for the function
   * @param name        the name of the function
   * @param description the description for the function
   */
  public Function(String code, String name, String description)
  {
    this.code = code;
    this.name = name;
    this.description = description;
  }

  /**
   * Returns the code for the function.
   *
   * @return the code for the function
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the description for the function.
   *
   * @return the description for the function
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the unique numeric ID for the function.
   *
   * @return the unique numeric ID for the function
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the name of the function.
   *
   * @return the name of the function
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the code for the function.
   *
   * @param code the code for the function
   */
  public void setCode(String code)
  {
    this.code = code;
  }

  /**
   * Set the description for the function.
   *
   * @param description the description for the function
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the unique numeric ID for the function.
   *
   * @param id the unique numeric ID for the function
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the name of the function.
   *
   * @param name the name of the function
   */
  public void setName(String name)
  {
    this.name = name;
  }
}
