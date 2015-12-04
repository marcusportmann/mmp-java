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
 * The <code>UserDirectoryParameter</code> class stores the information for a user directory
 * parameter.
 *
 * @author Marcus Portmann
 */
public class UserDirectoryParameter
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private transient boolean deleted;
  private long id = -1;
  private String name;
  private transient boolean updated;
  private long userDirectoryId;
  private String value;

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   */
  public UserDirectoryParameter() {}

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name  the name of the user directory parameter
   * @param value the value for the user directory parameter
   */
  public UserDirectoryParameter(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param id              the unique ID for the user directory parameter
   * @param userDirectoryId the unique ID for the user directory the user directory parameter is
   *                        associated with
   * @param name            the name of the user directory parameter
   * @param value           the value for the user directory parameter
   */
  public UserDirectoryParameter(long id, long userDirectoryId, String name, String value)
  {
    this.id = id;
    this.userDirectoryId = userDirectoryId;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the unique ID for the user directory parameter.
   *
   * @return the unique ID for the user directory parameter
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the name of the user directory parameter.
   *
   * @return the name of the user directory parameter
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the unique ID for the user directory the user directory parameter is associated with.
   *
   * @return the unique ID for the user directory the user directory parameter is associated with
   */
  public long getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Returns the value for the user directory parameter.
   *
   * @return the value for the user directory parameter
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Returns whether the user directory parameter has been deleted.
   *
   * @return <code>true</code> if the user directory parameter has been deleted or
   *         <code>false</code> otherwise
   */
  public boolean isDeleted()
  {
    return deleted;
  }

  /**
   * Returns whether the user directory parameter has been updated.
   *
   * @return <code>true</code> if the user directory parameter has been updated or
   *         <code>false</code> otherwise
   */
  public boolean isUpdated()
  {
    return updated;
  }

  /**
   * Set whether the user directory parameter has been deleted.
   *
   * @param deleted <code>true</code> if the user directory parameter has been deleted or
   *                <code>false</code> otherwise
   */
  public void setDeleted(boolean deleted)
  {
    this.deleted = deleted;
  }

  /**
   * Set the unique ID for the user directory parameter.
   *
   * @param id the unique ID for the user directory parameter
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the name of the user directory parameter.
   *
   * @param name the name of the user directory parameter
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set whether the user directory parameter has been updated.
   *
   * @param updated <code>true</code> if the user directory parameter has been updated or
   *                <code>false</code> otherwise
   */
  public void setUpdated(boolean updated)
  {
    this.updated = updated;
  }

  /**
   * Set the unique ID for the user directory the user directory parameter is associated with.
   *
   * @param userDirectoryId the unique ID for the user directory the user directory parameter is
   *                        associated with
   */
  public void setUserDirectoryId(long userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Set the value for the user directory parameter.
   *
   * @param value the value for the user directory parameter
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}
