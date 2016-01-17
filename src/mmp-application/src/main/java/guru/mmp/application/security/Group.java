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

package guru.mmp.application.security;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>Group</code> class stores the information for a group.
 *
 * @author Marcus Portmann
 */
public class Group
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String description;
  private String groupName;
  private UUID id;
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>Group</code>.
   */
  public Group() {}

  /**
   * Constructs a new <code>Group</code>.
   *
   * @param groupName the name of the group uniquely identifying the group
   */
  public Group(String groupName)
  {
    this.groupName = groupName;
  }

  /**
   * Returns the description for the group.
   *
   * @return the description for the group
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the name of the group uniquely identifying the group.
   *
   * @return the name of the group uniquely identifying the group
   */
  public String getGroupName()
  {
    return groupName;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the group.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the group
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * the group is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * the group is associated with
   */
  public UUID getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Set the description for the group.
   *
   * @param description the description for the group
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the name of the group uniquely identifying the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   */
  public void setGroupName(String groupName)
  {
    this.groupName = groupName;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the group.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the group
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the user directory the
   * group is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory the group is associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }
}
