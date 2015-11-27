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
 * The <code>UserDirectory</code> class stores the information for a user directory.
 *
 * @author Marcus Portmann
 */
public class UserDirectory
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String description;
  private long id;
  private String name;
  private String userDirectoryClass;

  /**
   * Constructs a new <code>UserDirectory</code>.
   */
  public UserDirectory() {}

  /**
   * Returns the description for the user directory.
   *
   * @return the description for the user directory
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the unique numeric ID for the user directory.
   *
   * @return the unique numeric ID for the user directory
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the name of the user directory.
   *
   * @return the name of the user directory
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the user directory.
   *
   * @return the fully qualified name of the Java class that implements the user directory
   */
  public String getUserDirectoryClass()
  {
    return userDirectoryClass;
  }

  /**
   * Set the description for the user directory.
   *
   * @param description the description for the user directory
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the unique numeric ID for the user directory.
   *
   * @param id the unique numeric ID for the user directory
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the name of the user directory.
   *
   * @param name the name of the user directory
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the fully qualified name of the Java class that implements the user directory.
   *
   * @param userDirectoryClass the fully qualified name of the Java class that implements the user
   *                           directory
   */
  public void setUserDirectoryClass(String userDirectoryClass)
  {
    this.userDirectoryClass = userDirectoryClass;
  }
}
