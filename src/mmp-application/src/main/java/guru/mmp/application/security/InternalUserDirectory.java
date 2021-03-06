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

import guru.mmp.application.persistence.IDGenerator;
import guru.mmp.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>InternalUserDirectory</code> class provides the internal user directory implementation.
 *
 * @author Marcus Portmann
 */
public class InternalUserDirectory extends UserDirectoryBase
{
  /**
   * The default number of failed password attempts before the user is locked.
   */
  private static final int DEFAULT_MAX_PASSWORD_ATTEMPTS = 5;

  /**
   * The default number of months before a user's password expires.
   */
  private static final int DEFAULT_PASSWORD_EXPIRY_MONTHS = 3;

  /**
   * The default number of months to check password history against.
   */
  private static final int DEFAULT_PASSWORD_HISTORY_MONTHS = 12;

  /**
   * The default maximum number of filtered users.
   */
  private static final int DEFAULT_MAX_FILTERED_USERS = 100;

  /**
   * The data source used to provide connections to the application database.
   */
  @Inject
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * The Spring application context.
   */
  @Inject
  private ApplicationContext applicationContext;

  /**
   * The ID Generator.
   */
  @Inject
  private IDGenerator idGenerator;

  /**
   * The maximum number of filtered users to return.
   */
  private int maxFilteredUsers;

  /**
   * The maximum number of password attempts.
   */
  private int maxPasswordAttempts;

  /**
   * The password expiry period in months.
   */
  private int passwordExpiryMonths;

  /**
   * The password history period in months.
   */
  private int passwordHistoryMonths;

  /**
   * Constructs a new <code>InternalUserDirectory</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param parameters      the key-value configuration parameters for the user directory
   */
  public InternalUserDirectory(UUID userDirectoryId, Map<String, String> parameters)
    throws SecurityException
  {
    super(userDirectoryId, parameters);

    try
    {
      if (parameters.containsKey("MaxPasswordAttempts"))
      {
        maxPasswordAttempts = Integer.parseInt(parameters.get("MaxPasswordAttempts"));
      }
      else
      {
        maxPasswordAttempts = DEFAULT_MAX_PASSWORD_ATTEMPTS;
      }

      if (parameters.containsKey("PasswordExpiryMonths"))
      {
        passwordExpiryMonths = Integer.parseInt(parameters.get("PasswordExpiryMonths"));
      }
      else
      {
        passwordExpiryMonths = DEFAULT_PASSWORD_EXPIRY_MONTHS;
      }

      if (parameters.containsKey("PasswordHistoryMonths"))
      {
        passwordHistoryMonths = Integer.parseInt(parameters.get("PasswordHistoryMonths"));
      }
      else
      {
        passwordHistoryMonths = DEFAULT_PASSWORD_HISTORY_MONTHS;
      }

      if (parameters.containsKey("MaxFilteredUsers"))
      {
        maxFilteredUsers = Integer.parseInt(parameters.get("MaxFilteredUsers"));
      }
      else
      {
        maxFilteredUsers = DEFAULT_MAX_FILTERED_USERS;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to initialise the user directory (%s): %s", userDirectoryId, e.getMessage()),
          e);
    }
  }

  /**
   * Add the user to the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the name of the security group uniquely identifying the security group
   */
  public void addUserToGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException
  {
    String addInternalUserToInternalGroupSQL =
        "INSERT INTO SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP "
        + "(INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES (?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(addInternalUserToInternalGroupSQL))
    {
      // Get the ID of the internal user with the specified username
      UUID internalUserId;

      if ((internalUserId = getInternalUserId(connection, username)) == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      // Get the ID of the internal security group with the specified group name
      UUID internalGroupId;

      if ((internalGroupId = getInternalGroupId(connection, groupName)) == null)
      {
        throw new GroupNotFoundException(String.format("The group (%s) could not be found",
            groupName));
      }

      // Check if the user has already been added to the security group for the user directory
      if (isInternalUserInInternalGroup(connection, internalUserId, internalGroupId))
      {
        // The user is already a member of the specified security group do nothing
        return;
      }

      // Add the user to the security group
      statement.setObject(1, internalUserId);
      statement.setObject(2, internalGroupId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            addInternalUserToInternalGroupSQL));
      }
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to add the user (%s) to the security group (%s) for the user directory (%s): %s",
          username, groupName, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Administratively change the password for the user.
   *
   * @param username             the username identifying the user
   * @param newPassword          the new password
   * @param expirePassword       expire the user's password
   * @param lockUser             lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason               the reason for changing the password
   */
  public void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason)
    throws UserNotFoundException, SecurityException
  {
    String changeInternalUserPasswordSQL = "UPDATE SECURITY.INTERNAL_USERS "
        + "SET PASSWORD=?, PASSWORD_ATTEMPTS=?, PASSWORD_EXPIRY=? "
        + "WHERE USER_DIRECTORY_ID=? AND ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(changeInternalUserPasswordSQL))
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      String passwordHash = createPasswordHash(newPassword);

      statement.setString(1, passwordHash);

      if (lockUser)
      {
        statement.setInt(2, maxPasswordAttempts);
      }
      else
      {
        if (user.getPasswordAttempts() == null)
        {
          statement.setNull(2, java.sql.Types.INTEGER);
        }
        else
        {
          statement.setInt(2, 0);
        }
      }

      if (expirePassword)
      {
        statement.setTimestamp(3, new Timestamp(0));
      }
      else
      {
        if (user.getPasswordExpiry() == null)
        {
          statement.setNull(3, Types.TIMESTAMP);
        }
        else
        {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(new Date());
          calendar.add(Calendar.MONTH, passwordExpiryMonths);

          statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
        }
      }

      statement.setObject(4, getUserDirectoryId());
      statement.setObject(5, user.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            changeInternalUserPasswordSQL));
      }

      savePasswordHistory(connection, user.getId(), passwordHash);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to change the password for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   */
  public void authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
        UserNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() >= maxPasswordAttempts))
      {
        throw new UserLockedException(String.format(
            "The user (%s) has exceeded the number of failed password attempts and has been locked",
            username));
      }

      if ((user.getPasswordExpiry() != null) && (user.getPasswordExpiry().before(new Date())))
      {
        throw new ExpiredPasswordException(String.format(
            "The password for the user (%s) has expired", username));
      }

      if (!user.getPassword().equals(createPasswordHash(password)))
      {
        if ((user.getPasswordAttempts() != null) && (user.getPasswordAttempts() != -1))
        {
          incrementPasswordAttempts(user.getId());
        }

        throw new AuthenticationFailedException(String.format(
            "Authentication failed for the user (%s)", username));
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExpiredPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to authenticate the user (%s) for the user directory (%s): %s", username,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   */
  public void changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
        ExistingPasswordException, SecurityException
  {
    String changeInternalUserPasswordSQL = "UPDATE SECURITY.INTERNAL_USERS "
        + "SET PASSWORD=?, PASSWORD_ATTEMPTS=?, PASSWORD_EXPIRY=? "
        + "WHERE USER_DIRECTORY_ID=? AND ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(changeInternalUserPasswordSQL))
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() > maxPasswordAttempts))
      {
        throw new UserLockedException(String.format(
            "The user (%s) has exceeded the number of failed password attempts and has been locked",
            username));
      }

      String passwordHash = createPasswordHash(password);
      String newPasswordHash = createPasswordHash(newPassword);

      if (!user.getPassword().equals(passwordHash))
      {
        throw new AuthenticationFailedException(String.format(
            "Authentication failed while attempting to change the password for the user (%s)",
            username));
      }

      if (isPasswordInHistory(connection, user.getId(), newPasswordHash))
      {
        throw new ExistingPasswordException(String.format(
            "The new password for the user (%s) has been used recently and is not valid",
            username));
      }

      statement.setString(1, newPasswordHash);

      if (user.getPasswordAttempts() == null)
      {
        statement.setNull(2, java.sql.Types.INTEGER);
      }
      else
      {
        statement.setInt(2, 0);
      }

      if (user.getPasswordExpiry() == null)
      {
        statement.setNull(3, Types.TIMESTAMP);
      }
      else
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, passwordExpiryMonths);

        statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
      }

      statement.setObject(4, getUserDirectoryId());
      statement.setObject(5, user.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            changeInternalUserPasswordSQL));
      }

      savePasswordHistory(connection, user.getId(), newPasswordHash);
    }
    catch (AuthenticationFailedException | ExistingPasswordException | UserNotFoundException
        | UserLockedException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to change the password for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Create a new security group.
   *
   * @param group the security group
   */
  public void createGroup(Group group)
    throws DuplicateGroupException, SecurityException
  {
    String createInternalGroupSQL = "INSERT INTO SECURITY.INTERNAL_GROUPS "
        + "(ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createInternalGroupSQL))
    {
      if (getInternalGroupId(connection, group.getGroupName()) != null)
      {
        throw new DuplicateGroupException(String.format("The security group (%s) already exists",
            group.getGroupName()));
      }

      UUID internalGroupId = idGenerator.nextUUID();

      statement.setObject(1, internalGroupId);
      statement.setObject(2, getUserDirectoryId());
      statement.setString(3, group.getGroupName());
      statement.setString(4, group.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createInternalGroupSQL));
      }

      group.setId(internalGroupId);
      group.setUserDirectoryId(getUserDirectoryId());

      createGroup(connection, group.getId(), group.getGroupName());
    }
    catch (DuplicateGroupException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to create the security group (%s) for the user directory (%s): %s",
          group.getGroupName(), getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Create a new user.
   *
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  public void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityException
  {
    String createInternalUserSQL = "INSERT INTO SECURITY.INTERNAL_USERS "
        + "(ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, MOBILE, "
        + "EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createInternalUserSQL))
    {
      if (getInternalUserId(connection, user.getUsername()) != null)
      {
        throw new DuplicateUserException(String.format("The user (%s) already exists",
            user.getUsername()));
      }

      UUID internalUserId = idGenerator.nextUUID();

      statement.setObject(1, internalUserId);
      statement.setObject(2, getUserDirectoryId());
      statement.setString(3, user.getUsername());

      String passwordHash;

      if (!isNullOrEmpty(user.getPassword()))
      {
        passwordHash = createPasswordHash(user.getPassword());
      }
      else
      {
        passwordHash = createPasswordHash("");
      }

      statement.setString(4, passwordHash);
      statement.setString(5, StringUtil.notNull(user.getFirstName()));
      statement.setString(6, StringUtil.notNull(user.getLastName()));
      statement.setString(7, StringUtil.notNull(user.getPhoneNumber()));
      statement.setString(8, StringUtil.notNull(user.getMobileNumber()));
      statement.setString(9, StringUtil.notNull(user.getEmail()));

      if (userLocked)
      {
        statement.setInt(10, maxPasswordAttempts);
        user.setPasswordAttempts(maxPasswordAttempts);
      }
      else
      {
        statement.setInt(10, 0);
        user.setPasswordAttempts(0);
      }

      if (expiredPassword)
      {
        statement.setTimestamp(11, new Timestamp(0));
        user.setPasswordExpiry(new Date(0));
      }
      else
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, passwordExpiryMonths);

        long expiryTime = calendar.getTimeInMillis();

        statement.setTimestamp(11, new Timestamp(expiryTime));
        user.setPasswordExpiry(new Date(expiryTime));
      }

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createInternalUserSQL));
      }

      user.setId(internalUserId);
      user.setUserDirectoryId(getUserDirectoryId());

      // Save the password in the password history if one was specified
      if (passwordHash != null)
      {
        savePasswordHistory(connection, internalUserId, passwordHash);
      }
    }
    catch (DuplicateUserException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to create the user (%s) for the user directory (%s): %s", user.getUsername(),
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Delete the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   */
  public void deleteGroup(String groupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException
  {
    String deleteInternalGroupSQL = "DELETE FROM SECURITY.INTERNAL_GROUPS "
        + "WHERE USER_DIRECTORY_ID=? AND ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteInternalGroupSQL))
    {
      UUID internalGroupId = getInternalGroupId(connection, groupName);

      if (internalGroupId == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", groupName));
      }

      if (getNumberOfInternalUsersForInternalGroup(connection, internalGroupId) > 0)
      {
        throw new ExistingGroupMembersException(String.format(
            "The security group (%s) could not be deleted since it is still associated with 1 or more user(s)",
            groupName));
      }

      statement.setObject(1, getUserDirectoryId());
      statement.setObject(2, internalGroupId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteInternalGroupSQL));
      }

      deleteGroup(connection, groupName);
    }
    catch (GroupNotFoundException | ExistingGroupMembersException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to delete the security group (%s) for the user directory (%s): %s", groupName,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param username the username identifying the user
   */
  public void deleteUser(String username)
    throws UserNotFoundException, SecurityException
  {
    String deleteInternalUserSQL =
        "DELETE FROM SECURITY.INTERNAL_USERS WHERE USER_DIRECTORY_ID=? AND ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteInternalUserSQL))
    {
      UUID internalUserId = getInternalUserId(connection, username);

      if (internalUserId == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      statement.setObject(1, getUserDirectoryId());
      statement.setObject(2, internalUserId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteInternalUserSQL));
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to delete the user (%s) for the user directory (%s): %s", username,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   *
   * @return the list of users whose attributes match the attribute criteria
   */
  public List<User> findUsers(List<Attribute> attributes)
    throws InvalidAttributeException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      try (PreparedStatement statement = buildFindUsersStatement(connection, attributes))
      {
        try (ResultSet rs = statement.executeQuery())
        {
          List<User> list = new ArrayList<>();

          while (rs.next())
          {
            User user = buildUserFromResultSet(rs);

            list.add(user);
          }

          return list;
        }
      }
    }
    catch (InvalidAttributeException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to find the users for the user directory (%s): %s", getUserDirectoryId(),
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the filtered list of users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the filtered list of users
   */
  public List<User> getFilteredUsers(String filter)
    throws SecurityException
  {
    String getInternalUsersSQL = "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.FIRST_NAME, "
        + "IU.LAST_NAME, IU.PHONE, IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS, IU.PASSWORD_EXPIRY "
        + "FROM SECURITY.INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=? ORDER BY IU.USERNAME";

    String getFilteredInternalUsersSQL =
        "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.FIRST_NAME, IU.LAST_NAME, IU.PHONE, IU.MOBILE, "
        + "IU.EMAIL, IU.PASSWORD_ATTEMPTS, IU.PASSWORD_EXPIRY FROM SECURITY.INTERNAL_USERS IU "
        + "WHERE IU.USER_DIRECTORY_ID=? AND " + "((UPPER(IU.USERNAME) LIKE ?) "
        + "OR (UPPER(IU.FIRST_NAME) LIKE ?) OR (UPPER(IU.LAST_NAME) LIKE ?)) ORDER BY IU.USERNAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getInternalUsersSQL
          : getFilteredInternalUsersSQL))
    {
      statement.setMaxRows(maxFilteredUsers);

      if (StringUtil.isNullOrEmpty(filter))
      {
        statement.setObject(1, getUserDirectoryId());
      }
      else
      {
        StringBuilder filterBuffer = new StringBuilder("%");

        filterBuffer.append(filter.toUpperCase());
        filterBuffer.append("%");

        statement.setObject(1, getUserDirectoryId());
        statement.setString(2, filterBuffer.toString());
        statement.setString(3, filterBuffer.toString());
        statement.setString(4, filterBuffer.toString());
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<User> list = new ArrayList<>();

        while (rs.next())
        {
          User user = buildUserFromResultSet(rs);

          list.add(user);
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the filtered users for the user directory (%s): %s",
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param username the username identifying the user
   *
   * @return the list of authorised function codes for the user
   */
  public List<String> getFunctionCodesForUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the user with the specified username
      UUID internalUserId = getInternalUserId(connection, username);

      if (internalUserId == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      return getFunctionCodesForUserId(connection, internalUserId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the function codes for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return the group
   */
  public Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityException
  {
    String getInternalGroupSQL =
        "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM SECURITY.INTERNAL_GROUPS IG "
        + "WHERE IG.USER_DIRECTORY_ID=? AND UPPER(IG.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getInternalGroupSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, groupName);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          Group group = new Group(rs.getString(2));

          group.setId(UUID.fromString(rs.getString(1)));
          group.setUserDirectoryId(getUserDirectoryId());
          group.setDescription(StringUtil.notNull(rs.getString(3)));

          return group;
        }
        else
        {
          throw new GroupNotFoundException(String.format(
              "The security group (%s) could not be found", groupName));
        }
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the security group (%s) for the user directory (%s): %s", groupName,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the security group names for the user.
   *
   * @param username the username identifying the user
   *
   * @return the security group names for the user
   */
  public List<String> getGroupNamesForUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the internal user with the specified username
      UUID internalUserId = getInternalUserId(connection, username);

      if (internalUserId == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      return getGroupNamesForUser(connection, internalUserId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the security group names for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve all the security groups.
   *
   * @return the list of security groups
   */
  public List<Group> getGroups()
    throws SecurityException
  {
    String getInternalGroupsSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM "
        + "SECURITY.INTERNAL_GROUPS IG WHERE IG.USER_DIRECTORY_ID=? ORDER BY IG.GROUPNAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getInternalGroupsSQL))
    {
      statement.setObject(1, getUserDirectoryId());

      try (ResultSet rs = statement.executeQuery())
      {
        List<Group> list = new ArrayList<>();

        while (rs.next())
        {
          Group group = new Group(rs.getString(2));

          group.setId(UUID.fromString(rs.getString(1)));
          group.setUserDirectoryId(getUserDirectoryId());
          group.setDescription(StringUtil.notNull(rs.getString(3)));
          list.add(group);
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the security groups for the user directory (%s): %s",
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the security groups for the user.
   *
   * @param username the username identifying the user
   *
   * @return the security groups for the user
   */
  public List<Group> getGroupsForUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the user with the specified username
      UUID internalUserId = getInternalUserId(connection, username);

      if (internalUserId == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      // Get the list of groups the user is associated with
      return getInternalGroupsForInternalUser(connection, internalUserId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the security groups for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the number of filtered users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the number of filtered users
   */
  public int getNumberOfFilteredUsers(String filter)
    throws SecurityException
  {
    String getNumberOfInternalUsersSQL =
        "SELECT COUNT(IU.ID) FROM SECURITY.INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=?";

    String getNumberOfFilteredInternalUsersSQL =
        "SELECT COUNT(IU.ID) FROM SECURITY.INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=? "
        + "AND ((UPPER(IU.USERNAME) LIKE ?) OR (UPPER(IU.FIRST_NAME) LIKE ?) OR "
        + "(UPPER(IU.LAST_NAME) LIKE ?))";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getNumberOfInternalUsersSQL
          : getNumberOfFilteredInternalUsersSQL))
    {
      if (StringUtil.isNullOrEmpty(filter))
      {
        statement.setObject(1, getUserDirectoryId());
      }
      else
      {
        StringBuilder filterBuffer = new StringBuilder("%");

        filterBuffer.append(filter.toUpperCase());
        filterBuffer.append("%");

        statement.setObject(1, getUserDirectoryId());
        statement.setString(2, filterBuffer.toString());
        statement.setString(3, filterBuffer.toString());
        statement.setString(4, filterBuffer.toString());
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          int numberOfFilteredUsers = rs.getInt(1);

          return ((numberOfFilteredUsers > maxFilteredUsers)
              ? maxFilteredUsers
              : numberOfFilteredUsers);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the number of filtered users for the user directory (%s): %s",
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the number of security groups
   *
   * @return the number of security groups
   */
  public int getNumberOfGroups()
    throws SecurityException
  {
    String getNumberOfInternalGroupsSQL = "SELECT COUNT(IG.ID) FROM "
        + "SECURITY.INTERNAL_GROUPS IG WHERE IG.USER_DIRECTORY_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfInternalGroupsSQL))
    {
      statement.setObject(1, getUserDirectoryId());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the number of security groups for the user directory (%s): %s",
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @return the number of users
   */
  public int getNumberOfUsers()
    throws SecurityException
  {
    String getNumberOfInternalUsersSQL =
        "SELECT COUNT(IU.ID) FROM SECURITY.INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfInternalUsersSQL))
    {
      statement.setObject(1, getUserDirectoryId());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the number of users for the user directory (%s): %s",
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve the user.
   *
   * @param username the username identifying the user
   *
   * @return the user
   */
  public User getUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      User user = getUser(connection, username);

      if (user != null)
      {
        return user;
      }
      else
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the user (%s) for the user directory (%s): %s", username,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @return the list of users
   */
  public List<User> getUsers()
    throws SecurityException
  {
    String getInternalUsersSQL = "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.FIRST_NAME, "
        + "IU.LAST_NAME, IU.PHONE, IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS, IU.PASSWORD_EXPIRY "
        + "FROM SECURITY.INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=? ORDER BY IU.USERNAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getInternalUsersSQL))
    {
      statement.setObject(1, getUserDirectoryId());

      try (ResultSet rs = statement.executeQuery())
      {
        List<User> list = new ArrayList<>();

        while (rs.next())
        {
          User user = buildUserFromResultSet(rs);

          list.add(user);
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the users for the user directory (%s): %s", getUserDirectoryId(),
          e.getMessage()), e);
    }
  }

  /**
   * Does the user with the specified username exist?
   *
   * @param username the username identifying the user
   *
   * @return <code>true</code> if a user with specified username exists or <code>false</code>
   *         otherwise
   */
  public boolean isExistingUser(String username)
    throws SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return (getInternalUserId(connection, username) != null);
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to check whether the user (%s) is an existing user for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
  }

  /**
   * Is the user in the security group?
   *
   * @param username  the username identifying the user
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   *         otherwise
   */
  public boolean isUserInGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the internal user with the specified username
      UUID internalUserId = getInternalUserId(connection, username);

      if (internalUserId == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      // Get the ID of the internal security group with the specified group name
      UUID internalGroupId = getInternalGroupId(connection, groupName);

      if (internalGroupId == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", groupName));
      }

      // Get the current list of internal security groups for the internal user
      return isInternalUserInInternalGroup(connection, internalUserId, internalGroupId);
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to check if the user (%s) is in the security group (%s) for the user directory (%s): %s",
          username, groupName, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Remove the user from the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the security group name
   */
  public void removeUserFromGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException
  {
    String removeInternalUserFromInternalGroupSQL =
        "DELETE FROM SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP "
        + "WHERE INTERNAL_USER_ID=? AND INTERNAL_GROUP_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          removeInternalUserFromInternalGroupSQL))
    {
      // Get the ID of the internal user with the specified username
      UUID internalUserId = getInternalUserId(connection, username);

      if (internalUserId == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the internal group with the specified group name
      UUID internalGroupId = getInternalGroupId(connection, groupName);

      if (internalGroupId == null)
      {
        throw new GroupNotFoundException("The security group (" + groupName
            + ") could not be found");
      }

      // Remove the user from the group
      statement.setObject(1, internalUserId);
      statement.setObject(2, internalGroupId);
      statement.executeUpdate();
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to remove the user (%s) from the security group (%s) for the user directory (%s): %s",
          username, groupName, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Does the user directory support administering security groups.
   *
   * @return <code>true</code> if the user directory supports administering security groups or
   *         <code>false</code> otherwise
   */
  public boolean supportsGroupAdministration()
  {
    return true;
  }

  /**
   * Does the user directory support administering users.
   *
   * @return <code>true</code> if the user directory supports administering users or
   *         <code>false</code> otherwise
   */
  public boolean supportsUserAdministration()
  {
    return true;
  }

  /**
   * Update the security group.
   *
   * @param group the security group
   */
  public void updateGroup(Group group)
    throws GroupNotFoundException, SecurityException
  {
    String updateInternalGroupSQL = "UPDATE SECURITY.INTERNAL_GROUPS "
        + "SET DESCRIPTION=? WHERE USER_DIRECTORY_ID=? AND ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateInternalGroupSQL))
    {
      UUID internalGroupId = getInternalGroupId(connection, group.getGroupName());

      if (internalGroupId == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", group.getGroupName()));
      }

      statement.setString(1, StringUtil.notNull(group.getDescription()));
      statement.setObject(2, getUserDirectoryId());
      statement.setObject(3, internalGroupId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateInternalGroupSQL));
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to update the security group (%s) for the user directory (%s): %s",
          group.getGroupName(), getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Update the user.
   *
   * @param user           the user
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser       lock the user as part of the update
   */
  public void updateUser(User user, boolean expirePassword, boolean lockUser)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection())
    {
      UUID internalUserId = getInternalUserId(connection, user.getUsername());

      if (internalUserId == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            user.getUsername()));
      }

      StringBuilder buffer = new StringBuilder();

      buffer.append("UPDATE SECURITY.INTERNAL_USERS ");

      StringBuilder fieldsBuffer = new StringBuilder();

      if (user.getFirstName() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET FIRST_NAME=?"
            : ", FIRST_NAME=?");
      }

      if (user.getLastName() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET LAST_NAME=?"
            : ", LAST_NAME=?");
      }

      if (user.getEmail() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET EMAIL=?"
            : ", EMAIL=?");
      }

      if (user.getPhoneNumber() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET PHONE=?"
            : ", PHONE=?");
      }

      if (user.getMobileNumber() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET MOBILE=?"
            : ", MOBILE=?");
      }

      if (!StringUtil.isNullOrEmpty(user.getPassword()))
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET PASSWORD=?"
            : ", PASSWORD=?");
      }

      fieldsBuffer.append((fieldsBuffer.length() == 0)
          ? "SET PASSWORD_ATTEMPTS=?"
          : ", PASSWORD_ATTEMPTS=?");

      fieldsBuffer.append((fieldsBuffer.length() == 0)
          ? "SET PASSWORD_EXPIRY=?"
          : ", PASSWORD_EXPIRY=?");

      buffer.append(fieldsBuffer.toString());
      buffer.append(" WHERE USER_DIRECTORY_ID=? AND ID=?");

      String updateUserSQL = buffer.toString();

      try (PreparedStatement statement = connection.prepareStatement(updateUserSQL))
      {
        int parameterIndex = 1;

        if (user.getFirstName() != null)
        {
          statement.setString(parameterIndex, user.getFirstName());
          parameterIndex++;
        }

        if (user.getLastName() != null)
        {
          statement.setString(parameterIndex, user.getLastName());
          parameterIndex++;
        }

        if (user.getEmail() != null)
        {
          statement.setString(parameterIndex, user.getEmail());
          parameterIndex++;
        }

        if (user.getPhoneNumber() != null)
        {
          statement.setString(parameterIndex, user.getPhoneNumber());
          parameterIndex++;
        }

        if (user.getMobileNumber() != null)
        {
          statement.setString(parameterIndex, user.getMobileNumber());
          parameterIndex++;
        }

        if (user.getPassword() != null)
        {
          if (user.getPassword().length() > 0)
          {
            statement.setString(parameterIndex, createPasswordHash(user.getPassword()));
          }
          else
          {
            statement.setString(parameterIndex, "");
          }

          parameterIndex++;
        }

        if (user.getPasswordAttempts() == null)
        {
          statement.setNull(parameterIndex, Types.INTEGER);
        }
        else
        {
          if (lockUser)
          {
            statement.setInt(parameterIndex, maxPasswordAttempts);
          }
          else
          {
            statement.setInt(parameterIndex, user.getPasswordAttempts());
          }
        }

        parameterIndex++;

        if (user.getPasswordExpiry() == null)
        {
          statement.setNull(parameterIndex, Types.TIMESTAMP);
        }
        else
        {
          if (expirePassword)
          {
            statement.setTimestamp(parameterIndex, new Timestamp(System.currentTimeMillis()));
          }
          else
          {
            statement.setTimestamp(parameterIndex, new Timestamp(user.getPasswordExpiry()
                .getTime()));
          }
        }

        parameterIndex++;

        statement.setObject(parameterIndex, getUserDirectoryId());

        parameterIndex++;

        statement.setObject(parameterIndex, internalUserId);

        if (statement.executeUpdate() != 1)
        {
          throw new SecurityException(String.format(
              "No rows were affected as a result of executing the SQL statement (%s)",
              updateUserSQL));
        }
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to update the user (%s) for the user directory (%s): %s", user.getUsername(),
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Build the JDBC <code>PreparedStatement</code> for the SQL query that will select the users
   * in the INTERNAL_USERS table using the values of the specified attributes as the selection
   * criteria.
   *
   * @param connection the existing database connection to use
   * @param attributes the attributes to be used as the selection criteria
   *
   * @return the <code>PreparedStatement</code> for the SQL query that will select the users in the
   *         INTERNAL_USERS table using the values of the specified attributes as the selection
   *         criteria
   */
  private PreparedStatement buildFindUsersStatement(Connection connection,
      List<Attribute> attributes)
    throws InvalidAttributeException, AttributeException, SQLException
  {
    // Build the SQL statement to select the users
    StringBuilder buffer = new StringBuilder();

    buffer.append("SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.FIRST_NAME, ");
    buffer.append("IU.LAST_NAME, IU.PHONE, IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS, ");
    buffer.append("IU.PASSWORD_EXPIRY FROM SECURITY.INTERNAL_USERS IU");

    if (attributes.size() > 0)
    {
      // Build the parameters for the "WHERE" clause for the SQL statement
      StringBuilder whereParameters = new StringBuilder();

      for (Attribute attribute : attributes)
      {
        whereParameters.append(" AND ");

        if (attribute.getName().equalsIgnoreCase("email"))
        {
          whereParameters.append("LOWER(IU.EMAIL) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("firstName"))
        {
          whereParameters.append("LOWER(IU.FIRST_NAME) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("lastName"))
        {
          whereParameters.append("LOWER(IU.LAST_NAME) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
        {
          whereParameters.append("LOWER(IU.PHONE) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
        {
          whereParameters.append("LOWER(IU.MOBILE) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("username"))
        {
          whereParameters.append("LOWER(IU.USERNAME) LIKE LOWER(?)");
        }
        else
        {
          throw new InvalidAttributeException("The attribute (" + attribute.getName()
              + ") is invalid");
        }
      }

      buffer.append(" WHERE IU.USER_DIRECTORY_ID=?");
      buffer.append(whereParameters.toString());
    }
    else
    {
      buffer.append(" WHERE IU.USER_DIRECTORY_ID=?");
    }

    PreparedStatement statement = connection.prepareStatement(buffer.toString());

    statement.setObject(1, getUserDirectoryId());

    // Set the parameters for the prepared statement
    int parameterIndex = 2;

    for (Attribute attribute : attributes)
    {
      if (attribute.getName().equalsIgnoreCase("email"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("firstName"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("lastName"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("username"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
    }

    return statement;
  }

  /**
   * Create a new <code>User</code> instance and populate it with the contents of the current
   * row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>User</code> instance
   *
   * @return the populated <code>User</code> instance
   */
  private User buildUserFromResultSet(ResultSet rs)
    throws SQLException
  {
    User user = new User();

    user.setId(UUID.fromString(rs.getString(1)));
    user.setUsername(rs.getString(2));
    user.setUserDirectoryId(getUserDirectoryId());
    user.setPassword(StringUtil.notNull(rs.getString(3)));
    user.setFirstName(StringUtil.notNull(rs.getString(4)));
    user.setLastName(StringUtil.notNull(rs.getString(5)));
    user.setPhoneNumber(StringUtil.notNull(rs.getString(6)));
    user.setMobileNumber(StringUtil.notNull(rs.getString(7)));
    user.setEmail(StringUtil.notNull(rs.getString(8)));

    if (rs.getObject(9) != null)
    {
      user.setPasswordAttempts(rs.getInt(9));
    }

    if (rs.getObject(10) != null)
    {
      user.setPasswordExpiry(new Date(rs.getTimestamp(10).getTime()));
    }

    return user;
  }

  /**
   * Retrieve the list of authorised function codes for the internal user.
   *
   * @param connection     the existing database connection to use
   * @param internalUserId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       internal user
   *
   * @return the list of authorised function codes for the internal user
   */
  private List<String> getFunctionCodesForUserId(Connection connection, UUID internalUserId)
    throws SQLException
  {
    String getFunctionCodesForUserIdSQL = "SELECT DISTINCT F.CODE FROM SECURITY.FUNCTIONS F "
        + "INNER JOIN SECURITY.FUNCTION_TO_ROLE_MAP FTRM ON FTRM.FUNCTION_ID = F.ID "
        + "INNER JOIN SECURITY.ROLE_TO_GROUP_MAP RTGM ON RTGM.ROLE_ID = FTRM.ROLE_ID "
        + "INNER JOIN SECURITY.GROUPS G ON G.ID = RTGM.GROUP_ID "
        + "INNER JOIN SECURITY.INTERNAL_GROUPS IG "
        + "ON IG.USER_DIRECTORY_ID = G.USER_DIRECTORY_ID AND IG.ID = G.ID "
        + "INNER JOIN SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTIGM "
        + "ON IUTIGM.INTERNAL_GROUP_ID = IG.ID WHERE IUTIGM.INTERNAL_USER_ID=?";

    List<String> functionCodes = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(getFunctionCodesForUserIdSQL))
    {
      statement.setObject(1, internalUserId);

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          functionCodes.add(rs.getString(1));
        }

        return functionCodes;
      }
    }
  }

  /**
   * Retrieve the names for all the security groups that the internal user with the specific numeric
   * ID is associated with.
   *
   * @param connection     the existing database connection
   * @param internalUserId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       internal user
   *
   * @return the list of security groups
   */
  private List<String> getGroupNamesForUser(Connection connection, UUID internalUserId)
    throws SQLException
  {
    String getInternalGroupNamesForInternalUserSQL = "SELECT IG.GROUPNAME FROM "
        + "SECURITY.INTERNAL_GROUPS IG, SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM "
        + "WHERE IG.ID = IUTGM.INTERNAL_GROUP_ID AND IUTGM.INTERNAL_USER_ID=? ORDER BY IG.GROUPNAME";

    try (PreparedStatement statement = connection.prepareStatement(
        getInternalGroupNamesForInternalUserSQL))
    {
      statement.setObject(1, internalUserId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<String> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(rs.getString(1));
        }

        return list;
      }
    }
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the internal
   * security group with the specified group name.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the internal security group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the internal
   *         security group with the specified group name
   */
  private UUID getInternalGroupId(Connection connection, String groupName)
    throws SecurityException
  {
    String getInternalGroupIdSQL = "SELECT IG.ID FROM SECURITY.INTERNAL_GROUPS IG "
        + "WHERE IG.USER_DIRECTORY_ID=? AND UPPER(IG.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getInternalGroupIdSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, groupName);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return UUID.fromString(rs.getString(1));
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

  /**
   * Retrieve all the internal security groups that the internal user with the specific numeric ID
   * is associated with.
   *
   * @param connection     the existing database connection
   * @param internalUserId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       internal user
   *
   * @return the list of internal security groups
   */
  private List<Group> getInternalGroupsForInternalUser(Connection connection, UUID internalUserId)
    throws SQLException
  {
    String getInternalGroupsForInternalUserSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM "
        + "SECURITY.INTERNAL_GROUPS IG, SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM "
        + "WHERE IG.ID = IUTGM.INTERNAL_GROUP_ID AND IUTGM.INTERNAL_USER_ID=? "
        + "ORDER BY IG.GROUPNAME";

    try (PreparedStatement statement = connection.prepareStatement(
        getInternalGroupsForInternalUserSQL))
    {
      statement.setObject(1, internalUserId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Group> list = new ArrayList<>();

        while (rs.next())
        {
          Group group = new Group(rs.getString(2));

          group.setId(UUID.fromString(rs.getString(1)));
          group.setUserDirectoryId(getUserDirectoryId());
          group.setDescription(rs.getString(3));
          list.add(group);
        }

        return list;
      }
    }
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   * with the specified username.
   *
   * @param connection the existing database connection to use
   * @param username   the username uniquely identifying the internal user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   *         with the specified username
   */
  private UUID getInternalUserId(Connection connection, String username)
    throws SecurityException
  {
    String getInternalUserIdSQL = "SELECT IU.ID FROM SECURITY.INTERNAL_USERS IU "
        + "WHERE IU.USER_DIRECTORY_ID=? AND UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getInternalUserIdSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, username);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return UUID.fromString(rs.getString(1));
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
          "Failed to retrieve the ID for the user (%s) for the user directory (%s)", username,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the number of internal users for the internal security group.
   *
   * @param connection      the existing database connection
   * @param internalGroupId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        internal security group
   *
   * @return the IDs for all the internal users that are associated with the internal security group
   *         with the specified ID
   */
  private long getNumberOfInternalUsersForInternalGroup(Connection connection, UUID internalGroupId)
    throws SQLException
  {
    String getNumberOfInternalUsersForInternalGroupSQL =
        "SELECT COUNT (IUTGM.INTERNAL_USER_ID) FROM "
        + "SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM WHERE IUTGM.INTERNAL_GROUP_ID=?";

    try (PreparedStatement statement = connection.prepareStatement(
        getNumberOfInternalUsersForInternalGroupSQL))
    {
      statement.setObject(1, internalGroupId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getLong(1);
        }
        else
        {
          return 0;
        }
      }
    }
  }

  /**
   * Retrieve the information for the internal user with the specified username.
   *
   * @param connection the existing database connection to use
   * @param username   the username identifying the internal user
   *
   * @return the <code>User</code> or <code>null</code> if the internal user could not be found
   */
  private User getUser(Connection connection, String username)
    throws SQLException
  {
    String getInternalUserSQL = "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.FIRST_NAME, "
        + "IU.LAST_NAME, IU.PHONE, IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS, IU.PASSWORD_EXPIRY "
        + "FROM SECURITY.INTERNAL_USERS IU "
        + "WHERE IU.USER_DIRECTORY_ID=? AND UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getInternalUserSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, username);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildUserFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
  }

  /**
   * Increment the password attempts for the user.
   *
   * @param internalUserId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       internal user
   */
  private void incrementPasswordAttempts(UUID internalUserId)
    throws SecurityException
  {
    String incrementPasswordAttemptsSQL = "UPDATE SECURITY.INTERNAL_USERS "
        + "SET PASSWORD_ATTEMPTS = PASSWORD_ATTEMPTS + 1 WHERE USER_DIRECTORY_ID=? AND ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(incrementPasswordAttemptsSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setObject(2, internalUserId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            incrementPasswordAttemptsSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to increment the password attempts "
          + "for the user (%s) for the user directory (%s): %s", internalUserId,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Is the user in the security group?
   *
   * @param connection      the existing database connection
   * @param internalUserId  the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        internal user
   * @param internalGroupId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        internal security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   *         otherwise
   */
  private boolean isInternalUserInInternalGroup(Connection connection, UUID internalUserId,
      UUID internalGroupId)
    throws SQLException
  {
    String isInternalUserInInternalGroupSQL = "SELECT IUTGM.INTERNAL_USER_ID FROM "
        + "SECURITY.INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM WHERE IUTGM.INTERNAL_USER_ID=? AND "
        + "IUTGM.INTERNAL_GROUP_ID=?";

    try (PreparedStatement statement = connection.prepareStatement(
        isInternalUserInInternalGroupSQL))
    {
      statement.setObject(1, internalUserId);
      statement.setObject(2, internalGroupId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
  }

  /**
   * Is the password, given by the specified password hash, a historical password that cannot
   * be reused for a period of time i.e. was the password used previously in the last X months.
   *
   * @param connection     the existing database connection
   * @param internalUserId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       internal user
   * @param passwordHash   the password hash
   *
   * @return <code>true</code> if the password was previously used and cannot be reused for a
   *         period of time or <code>false</code> otherwise
   */
  private boolean isPasswordInHistory(Connection connection, UUID internalUserId,
      String passwordHash)
    throws SQLException
  {
    String isPasswordInInternalUserPasswordHistorySQL =
        "SELECT IUPH.ID FROM  SECURITY.INTERNAL_USERS_PASSWORD_HISTORY IUPH "
        + "WHERE IUPH.INTERNAL_USER_ID=? AND IUPH.CHANGED > ? AND IUPH.PASSWORD=?";

    try (PreparedStatement statement = connection.prepareStatement(
        isPasswordInInternalUserPasswordHistorySQL))
    {
      Calendar calendar = Calendar.getInstance();

      calendar.setTime(new Date());
      calendar.add(Calendar.MONTH, -1 * passwordHistoryMonths);

      statement.setObject(1, internalUserId);
      statement.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
      statement.setString(3, passwordHash);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
  }

  private void savePasswordHistory(Connection connection, UUID internalUserId, String passwordHash)
    throws SQLException
  {
    String saveInternalUserPasswordHistorySQL =
        "INSERT INTO SECURITY.INTERNAL_USERS_PASSWORD_HISTORY "
        + "(ID, INTERNAL_USER_ID, CHANGED, PASSWORD) VALUES (?, ?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(
        saveInternalUserPasswordHistorySQL))
    {
      UUID id = idGenerator.nextUUID();

      statement.setObject(1, id);
      statement.setObject(2, internalUserId);
      statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      statement.setString(4, passwordHash);
      statement.execute();
    }
  }
}
