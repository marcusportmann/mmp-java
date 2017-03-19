/*
 * Copyright 2017 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.Base64;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserDirectoryBase</code> class provides the base class from which all user directory
 * classes should be derived.
 *
 * @author Marcus Portmann
 */
abstract class UserDirectoryBase
  implements IUserDirectory
{
  /**
   * The key-value configuration parameters for the user directory.
   */
  private Map<String, String> parameters;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>UserDirectoryBase</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param parameters      the key-value configuration parameters for the user directory
   */
  UserDirectoryBase(UUID userDirectoryId, Map<String, String> parameters)
    throws SecurityException
  {
    this.userDirectoryId = userDirectoryId;
    this.parameters = parameters;
  }

  /**
   * Returns the key-value configuration parameters for the user directory.
   *
   * @return the key-value configuration parameters for the user directory
   */
  public Map<String, String> getParameters()
  {
    return parameters;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public UUID getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Create a SHA-256 has of the specified password.
   *
   * @param password the password to hash
   *
   * @return the SHA-256 hash of the password
   */
  String createPasswordHash(String password)
    throws SecurityException
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      md.update(password.getBytes("iso-8859-1"), 0, password.length());

      return Base64.encodeBytes(md.digest());
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to generate a SHA-256 hash of the password (%s): %s", password, e.getMessage()),
          e);
    }
  }

  /**
   * Delete the security group.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the security group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   *         or <code>null</code> if a security group with the specified group name could not be
   *         found
   */
  UUID deleteGroup(Connection connection, String groupName)
    throws SecurityException
  {
    String deleteGroupSQL = "DELETE FROM SECURITY.GROUPS G WHERE G.USER_DIRECTORY_ID=? "
        + "AND UPPER(G.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(deleteGroupSQL))
    {
      UUID groupId = getGroupId(connection, groupName);

      if (groupId == null)
      {
        return groupId;
      }

      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, groupName);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteGroupSQL));
      }

      return groupId;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to delete the security group (%s) for the user directory (%s): %s", groupName,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Create a new security group.
   * <p/>
   * If a security group with the specified group name already exists the ID for this existing
   * security group will be returned.
   *
   * @param connection the existing database connection
   * @param groupId    the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   security group
   * @param groupName  the group name uniquely identifying the security group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   */
  protected UUID createGroup(Connection connection, UUID groupId, String groupName)
    throws SecurityException
  {
    String createGroupSQL =
        "INSERT INTO SECURITY.GROUPS (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES (?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(createGroupSQL))
    {
      UUID existingGroupId = getGroupId(connection, groupName);

      if (existingGroupId != null)
      {
        return existingGroupId;
      }

      statement.setObject(1, groupId);
      statement.setObject(2, getUserDirectoryId());
      statement.setString(3, groupName);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createGroupSQL));
      }

      return groupId;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to create the security group (%s) with the ID (%s) for the user directory (%s): %s",
          groupName, groupId, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Checks whether the specified value is <code>null</code> or blank.
   *
   * @param value the value to check
   *
   * @return true if the value is <code>null</code> or blank
   */
  protected boolean isNullOrEmpty(Object value)
  {
    if (value == null)
    {
      return true;
    }

    if (value instanceof String)
    {
      if (String.class.cast(value).length() == 0)
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the ID for the security group with the specified group name.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the security group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   *         or <code>null</code> if a security group with the specified group name could not be
   *         found
   */
  private UUID getGroupId(Connection connection, String groupName)
    throws SecurityException
  {
    String getGroupIdSQL = "SELECT G.ID FROM SECURITY.GROUPS G WHERE G.USER_DIRECTORY_ID=? AND "
        + "UPPER(G.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getGroupIdSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, groupName);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (UUID) rs.getObject(1);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the ID for the security group (%s) for the user directory (%s)",
          groupName, getUserDirectoryId()), e);
    }
  }
}
