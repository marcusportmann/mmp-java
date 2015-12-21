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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.TransactionManager;
import guru.mmp.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.*;
import java.util.Date;

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
  public static final int DEFAULT_MAX_PASSWORD_ATTEMPTS = 5;

  /**
   * The default number of months before a user's password expires.
   */
  public static final int DEFAULT_PASSWORD_EXPIRY_MONTHS = 3;

  /**
   * The default number of months to check password history against.
   */
  public static final int DEFAULT_PASSWORD_HISTORY_MONTHS = 12;

  /**
   * The default maximum number of filtered users.
   */
  private static final int DEFAULT_MAX_FILTERED_USERS = 100;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(InternalUserDirectory.class);
  private String addInternalUserToInternalGroupSQL;
  private String changeInternalUserPasswordSQL;
  private String createInternalGroupSQL;
  private String createInternalUserSQL;
  private String deleteInternalGroupSQL;
  private String deleteInternalUserSQL;
  private String getFilteredInternalUsersSQL;
  private String getFunctionCodesForUserIdSQL;
  private String getInternalGroupIdSQL;
  private String getInternalGroupNamesForInternalUserSQL;
  private String getInternalGroupSQL;
  private String getInternalGroupsForInternalUserSQL;
  private String getInternalGroupsSQL;
  private String getInternalUserIdSQL;
  private String getInternalUserSQL;
  private String getInternalUsersSQL;
  private String getNumberOfFilteredInternalUsersSQL;
  private String getNumberOfInternalGroupsSQL;
  private String getNumberOfInternalUsersSQL;
  private String getNumberOfUsersForGroupSQL;
  private String incrementPasswordAttemptsSQL;
  private String isInternalUserInInternalGroupSQL;
  private String isPasswordInInternalUserPasswordHistorySQL;
  private int maxFilteredUsers;
  private int maxPasswordAttempts;
  private int passwordExpiryMonths;
  private int passwordHistoryMonths;
  private String removeInternalUserFromInternalGroupSQL;
  private String saveInternalUserPasswordHistorySQL;
  private String updateInternalGroupSQL;

  /**
   * Constructs a new <code>InternalUserDirectory</code>.
   *
   * @param userDirectoryId the unique ID for the user directory
   * @param parameters      the key-value configuration parameters for the user directory
   *
   * @throws SecurityException
   */
  public InternalUserDirectory(long userDirectoryId, Map<String, String> parameters)
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
      throw new SecurityException("Failed to initialise the the user directory (" + userDirectoryId
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Add the user to the group.
   *
   * @param username  the username identifying the user
   * @param groupName the name of the group uniquely identifying the group
   *
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public void addUserToGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(addInternalUserToInternalGroupSQL))
    {
      // Get the ID of the internal user with the specified username
      long internalUserId;

      if ((internalUserId = getInternalUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the internal group with the specified group name
      long internalGroupId;

      if ((internalGroupId = getInternalGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Check if the user has already been added to the group for the user directory
      if (isInternalUserInInternalGroup(connection, internalUserId, internalGroupId))
      {
        // The user is already a member of the specified group do nothing
        return;
      }

      // Add the user to the group
      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalUserId);
      statement.setLong(3, internalGroupId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + addInternalUserToInternalGroupSQL + ")");
      }
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to add the user (" + username + ") to the group ("
          + groupName + ") for the user directory (" + getUserDirectoryId() + "): "
          + e.getMessage(), e);
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
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(changeInternalUserPasswordSQL))
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      String passwordHash = createPasswordHash(newPassword);

      statement.setString(1, passwordHash);

      if (user.getPasswordAttempts() == null)
      {
        statement.setNull(2, java.sql.Types.INTEGER);
      }
      else
      {
        if (lockUser)
        {
          statement.setInt(2, maxPasswordAttempts);
        }
        else
        {
          statement.setInt(2, 0);
        }
      }

      if (user.getPasswordExpiry() == null)
      {
        statement.setNull(3, Types.TIMESTAMP);
      }
      else
      {
        if (expirePassword)
        {
          statement.setTimestamp(3, new Timestamp(0));
        }
        else
        {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(new Date());
          calendar.add(Calendar.MONTH, passwordExpiryMonths);

          statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
        }
      }

      statement.setLong(4, getUserDirectoryId());
      statement.setLong(5, user.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + changeInternalUserPasswordSQL + ")");
      }

      savePasswordHistory(connection, user.getId(), passwordHash);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to administratively change the password for the user ("
          + username + ") for the user directory (" + getUserDirectoryId() + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredPasswordException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
      UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() >= maxPasswordAttempts))
      {
        throw new UserLockedException("The user (" + username
            + ") has exceeded the number of failed password attempts and has been locked");
      }

      if ((user.getPasswordExpiry() != null) && (user.getPasswordExpiry().before(new Date())))
      {
        throw new ExpiredPasswordException("The password for the user (" + username
            + ") has expired");
      }

      if (!user.getPassword().equals(createPasswordHash(password)))
      {
        if ((user.getPasswordAttempts() != null) && (user.getPasswordAttempts() != -1))
        {
          incrementPasswordAttempts(user.getId());
        }

        throw new AuthenticationFailedException("Authentication failed for the user (" + username
            + ")");
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExpiredPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to authenticate the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws UserNotFoundException
   * @throws ExistingPasswordException
   * @throws SecurityException
   */
  public void changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
      ExistingPasswordException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(changeInternalUserPasswordSQL))
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      if ((user.getPasswordAttempts() == null)
          || (user.getPasswordAttempts() > maxPasswordAttempts))
      {
        throw new UserLockedException("The user (" + username
            + ") has exceeded the number of failed password attempts and has been locked");
      }

      String passwordHash = createPasswordHash(password);
      String newPasswordHash = createPasswordHash(newPassword);

      if (!user.getPassword().equals(passwordHash))
      {
        throw new AuthenticationFailedException("Authentication failed while attempting to change"
            + " the password for the user (" + username + ")");
      }

      if (isPasswordInHistory(connection, user.getId(), newPasswordHash))
      {
        throw new ExistingPasswordException("The new password for the user (" + username
            + ") has been used recently and is not valid");
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

      statement.setLong(4, getUserDirectoryId());
      statement.setLong(5, user.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + changeInternalUserPasswordSQL + ")");
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
      throw new SecurityException("Failed to change the password for the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Create a new group.
   *
   * @param group the group
   *
   * @throws DuplicateGroupException
   * @throws SecurityException
   */
  public void createGroup(Group group)
    throws DuplicateGroupException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(createInternalGroupSQL))
    {
      if (getInternalGroupId(connection, group.getGroupName()) != -1)
      {
        throw new DuplicateGroupException("The group (" + group.getGroupName()
            + ") already exists");
      }

      long internalGroupId = nextId("Application.InternalGroupId");

      statement.setLong(1, internalGroupId);
      statement.setLong(2, getUserDirectoryId());
      statement.setString(3, group.getGroupName());
      statement.setString(4, group.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + createInternalGroupSQL + ")");
      }

      group.setId(internalGroupId);
      group.setUserDirectoryId(getUserDirectoryId());

      createGroup(connection, group.getGroupName());
    }
    catch (DuplicateGroupException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to create the group (" + group.getGroupName()
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Create a new user.
   *
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   *
   * @throws DuplicateUserException
   * @throws SecurityException
   */
  public void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(createInternalUserSQL))
    {
      if (getInternalUserId(connection, user.getUsername()) != -1)
      {
        throw new DuplicateUserException("The user (" + user.getUsername() + ") already exists");
      }

      long internalUserId = nextId("Application.InternalUserId");

      statement.setLong(1, internalUserId);
      statement.setLong(2, getUserDirectoryId());
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
      statement.setString(5, StringUtil.notNull(user.getTitle()));
      statement.setString(6, StringUtil.notNull(user.getFirstNames()));
      statement.setString(7, StringUtil.notNull(user.getLastName()));
      statement.setString(8, StringUtil.notNull(user.getPhoneNumber()));
      statement.setString(9, StringUtil.notNull(user.getFaxNumber()));
      statement.setString(10, StringUtil.notNull(user.getMobileNumber()));
      statement.setString(11, StringUtil.notNull(user.getEmail()));

      if (userLocked)
      {
        statement.setInt(12, maxPasswordAttempts);
        user.setPasswordAttempts(maxPasswordAttempts);
      }
      else
      {
        statement.setInt(12, 0);
        user.setPasswordAttempts(0);
      }

      if (expiredPassword)
      {
        statement.setTimestamp(13, new Timestamp(0));
        user.setPasswordExpiry(new Date(0));
      }
      else
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, passwordExpiryMonths);

        long expiryTime = calendar.getTimeInMillis();

        statement.setTimestamp(13, new Timestamp(expiryTime));
        user.setPasswordExpiry(new Date(expiryTime));
      }

      statement.setString(14, StringUtil.notNull(user.getDescription()));

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + createInternalUserSQL + ")");
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
      throw new SecurityException("Failed to create the user (" + user.getUsername()
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Delete the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   *
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  public void deleteGroup(String groupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteInternalGroupSQL))
    {
      long internalGroupId = getInternalGroupId(connection, groupName);

      if (internalGroupId == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      if (getNumberOfInternalUsersForInternalGroup(connection, internalGroupId) > 0)
      {
        throw new ExistingGroupMembersException("The group (" + groupName
            + ") could not be deleted since it is still associated with 1 or more user(s)");
      }

      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalGroupId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + deleteInternalGroupSQL + ")");
      }

      deleteGroup(connection, groupName);
    }
    catch (GroupNotFoundException | ExistingGroupMembersException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the group (" + groupName
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param username the username identifying the user
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void deleteUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteInternalUserSQL))
    {
      long internalUserId = getInternalUserId(connection, username);

      if (internalUserId == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalUserId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + deleteInternalUserSQL + ")");
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   *
   * @return the list of users whose attributes match the attribute criteria
   *
   * @throws InvalidAttributeException
   * @throws SecurityException
   */
  public List<User> findUsers(List<Attribute> attributes)
    throws InvalidAttributeException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
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
      throw new SecurityException("Failed to find the users for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the filtered list of users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the filtered list of users
   *
   * @throws SecurityException
   */
  public List<User> getFilteredUsers(String filter)
    throws SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getInternalUsersSQL
          : getFilteredInternalUsersSQL))
    {
      statement.setMaxRows(maxFilteredUsers);

      if (StringUtil.isNullOrEmpty(filter))
      {
        statement.setLong(1, getUserDirectoryId());
      }
      else
      {
        StringBuilder filterBuffer = new StringBuilder("%");

        filterBuffer.append(filter.toUpperCase());
        filterBuffer.append("%");

        statement.setLong(1, getUserDirectoryId());
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
      throw new SecurityException("Failed to retrieve the filtered users for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param username the username identifying the user
   *
   * @return the list of authorised function codes for the user
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public List<String> getFunctionCodesForUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      // Get the ID of the user with the specified username
      long internalUserId = getInternalUserId(connection, username);

      if (internalUserId == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      return getFunctionCodesForUserId(connection, internalUserId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the function codes for the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   *
   * @return the group
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(getInternalGroupSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setString(2, groupName);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          Group group = new Group(rs.getString(2));

          group.setId(rs.getLong(1));
          group.setUserDirectoryId(getUserDirectoryId());
          group.setDescription(StringUtil.notNull(rs.getString(3)));

          return group;
        }
        else
        {
          throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
        }
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the group (" + groupName
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the group names for the user.
   *
   * @param username the username identifying the user
   *
   * @return the group names for the user
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public List<String> getGroupNamesForUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      // Get the ID of the internal user with the specified username
      long internalUserId = getInternalUserId(connection, username);

      if (internalUserId == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      return getGroupNamesForUser(connection, internalUserId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the group names for the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve all the groups.
   *
   * @return the list of groups
   *
   * @throws SecurityException
   */
  public List<Group> getGroups()
    throws SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(getInternalGroupsSQL))
    {
      statement.setLong(1, getUserDirectoryId());

      try (ResultSet rs = statement.executeQuery())
      {
        List<Group> list = new ArrayList<>();

        while (rs.next())
        {
          Group group = new Group(rs.getString(2));

          group.setId(rs.getLong(1));
          group.setUserDirectoryId(getUserDirectoryId());
          group.setDescription(StringUtil.notNull(rs.getString(3)));
          list.add(group);
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the groups for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the groups for the user.
   *
   * @param username the username identifying the user
   *
   * @return the groups for the user
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public List<Group> getGroupsForUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      // Get the ID of the user with the specified username
      long internalUserId = getInternalUserId(connection, username);

      if (internalUserId == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
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
      throw new SecurityException("Failed to retrieve the groups for the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the number of filtered users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the number of filtered users
   *
   * @throws SecurityException
   */
  public int getNumberOfFilteredUsers(String filter)
    throws SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getNumberOfInternalUsersSQL
          : getNumberOfFilteredInternalUsersSQL))
    {
      if (StringUtil.isNullOrEmpty(filter))
      {
        statement.setLong(1, getUserDirectoryId());
      }
      else
      {
        StringBuilder filterBuffer = new StringBuilder("%");

        filterBuffer.append(filter.toUpperCase());
        filterBuffer.append("%");

        statement.setLong(1, getUserDirectoryId());
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
      throw new SecurityException(
          "Failed to retrieve the number of filtered users for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the number of groups
   *
   * @return the number of groups
   *
   * @throws SecurityException
   */
  public int getNumberOfGroups()
    throws SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfInternalGroupsSQL))
    {
      statement.setLong(1, getUserDirectoryId());

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
      throw new SecurityException(
          "Failed to retrieve the number of groups for the user directory (" + getUserDirectoryId()
          + "):" + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @return the number of users
   *
   * @throws SecurityException
   */
  public int getNumberOfUsers()
    throws SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfInternalUsersSQL))
    {
      statement.setLong(1, getUserDirectoryId());

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
      throw new SecurityException("Failed to retrieve the number of users for the user directory ("
          + getUserDirectoryId() + "):" + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the user.
   *
   * @param username the username identifying the user
   *
   * @return the user
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public User getUser(String username)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      User user = getUser(connection, username);

      if (user != null)
      {
        return user;
      }
      else
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @return the list of users
   *
   * @throws SecurityException
   */
  public List<User> getUsers()
    throws SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(getInternalUsersSQL))
    {
      statement.setLong(1, getUserDirectoryId());

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
      throw new SecurityException("Failed to retrieve the users for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Does the user with the specified username exist?
   *
   * @param username the username identifying the user
   *
   * @return <code>true</code> if a user with specified username exists or <code>false</code>
   *         otherwise
   *
   * @throws SecurityException
   */
  public boolean isExistingUser(String username)
    throws SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      return (getInternalUserId(connection, username) != -1);
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to check whether the user (" + username
          + ") is an existing user for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Is the user in the group for the specified organisation?
   *
   * @param username  the username identifying the user
   * @param groupName the name of the group uniquely identifying the group
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   *
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public boolean isUserInGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      // Get the ID of the internal user with the specified username
      long internalUserId = getInternalUserId(connection, username);

      if (internalUserId == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the internal group with the specified group name
      long internalGroupId = getInternalGroupId(connection, groupName);

      if (internalGroupId == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Get the current list of internal groups for the internal user
      return isInternalUserInInternalGroup(connection, internalUserId, internalGroupId);
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to check whether the user (" + username
          + ") is in the group (" + groupName + ") for the user directory (" + getUserDirectoryId()
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Remove the user from the group.
   *
   * @param username  the username identifying the user
   * @param groupName the group name
   *
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public void removeUserFromGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement =
          connection.prepareStatement(removeInternalUserFromInternalGroupSQL))
    {
      // Get the ID of the internal user with the specified username
      long internalUserId = getInternalUserId(connection, username);

      if (internalUserId == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the internal group with the specified group name
      long internalGroupId = getInternalGroupId(connection, groupName);

      if (internalGroupId == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Remove the user from the group
      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalUserId);
      statement.setLong(3, internalGroupId);
      statement.executeUpdate();
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to remove the user (" + username + ") from the group ("
          + groupName + ") for the user directory (" + getUserDirectoryId() + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Rename the existing group.
   *
   * @param groupName    the name of the group that will be renamed
   * @param newGroupName the new name of the group
   *
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  public void renameGroup(String groupName, String newGroupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException
  {
    throw new SecurityException("TODO: NOT IMPLEMENTED");
  }

  /**
   * Does the user directory support administering groups.
   *
   * @return <code>true</code> if the directory supports administering groups or <code>false</code>
   *         otherwise
   */
  public boolean supportsGroupAdministration()
  {
    return true;
  }

  /**
   * Does the user directory support administering users.
   *
   * @return <code>true</code> if the directory supports administering users or <code>false</code>
   *         otherwise
   */
  public boolean supportsUserAdministration()
  {
    return true;
  }

  /**
   * Update the group.
   *
   * @param group the group
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public void updateGroup(Group group)
    throws GroupNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(updateInternalGroupSQL))
    {
      long internalGroupId = getInternalGroupId(connection, group.getGroupName());

      if (internalGroupId == -1)
      {
        throw new GroupNotFoundException("The group (" + group.getGroupName()
            + ") could not be found");
      }

      statement.setString(1, StringUtil.notNull(group.getDescription()));
      statement.setLong(2, getUserDirectoryId());
      statement.setLong(3, internalGroupId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + updateInternalGroupSQL + ")");
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the group (" + group.getGroupName()
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Update the user.
   *
   * @param user           the user
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser       lock the user as part of the update
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void updateUser(User user, boolean expirePassword, boolean lockUser)
    throws UserNotFoundException, SecurityException
  {
    try (Connection connection = getDataSource().getConnection())
    {
      long internalUserId = getInternalUserId(connection, user.getUsername());

      if (internalUserId == -1)
      {
        throw new UserNotFoundException("The user (" + user.getUsername() + ") could not be found");
      }

      StringBuilder buffer = new StringBuilder();

      buffer.append("UPDATE ");
      buffer.append(DataAccessObject.DEFAULT_DATABASE_SCHEMA).append(getDatabaseCatalogSeparator());

      buffer.append("INTERNAL_USERS ");

      StringBuilder fieldsBuffer = new StringBuilder();

      if (user.getTitle() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET TITLE=?"
            : ", TITLE=?");
      }

      if (user.getFirstNames() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET FIRST_NAMES=?"
            : ", FIRST_NAMES=?");
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

      if (user.getFaxNumber() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET FAX=?"
            : ", FAX=?");
      }

      if (user.getMobileNumber() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET MOBILE=?"
            : ", MOBILE=?");
      }

      if (user.getDescription() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET DESCRIPTION=?"
            : ", DESCRIPTION=?");
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

        if (user.getTitle() != null)
        {
          statement.setString(parameterIndex, user.getTitle());
          parameterIndex++;
        }

        if (user.getFirstNames() != null)
        {
          statement.setString(parameterIndex, user.getFirstNames());
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

        if (user.getFaxNumber() != null)
        {
          statement.setString(parameterIndex, user.getFaxNumber());
          parameterIndex++;
        }

        if (user.getMobileNumber() != null)
        {
          statement.setString(parameterIndex, user.getMobileNumber());
          parameterIndex++;
        }

        if (user.getDescription() != null)
        {
          statement.setString(parameterIndex, user.getDescription());
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
            statement.setTimestamp(parameterIndex,
                new Timestamp(user.getPasswordExpiry().getTime()));
          }
        }

        parameterIndex++;

        statement.setLong(parameterIndex, getUserDirectoryId());

        parameterIndex++;

        statement.setLong(parameterIndex, internalUserId);

        if (statement.executeUpdate() != 1)
        {
          throw new SecurityException(
              "No rows were affected as a result of executing the SQL statement (" + updateUserSQL
              + ")");
        }
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the user (" + user.getUsername()
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Build the SQL statements for the user directory.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects for the user directory
   */
  @Override
  protected void buildStatements(String schemaPrefix)
  {
    super.buildStatements(schemaPrefix);

    // addInternalUserToInternalGroupSQL
    addInternalUserToInternalGroupSQL = "INSERT INTO " + schemaPrefix
        + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP"
        + " (USER_DIRECTORY_ID, INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES (?, ?, ?)";

    // changeInternalUserPasswordSQL
    changeInternalUserPasswordSQL = "UPDATE " + schemaPrefix + "INTERNAL_USERS IU"
        + " SET IU.PASSWORD=?, IU.PASSWORD_ATTEMPTS=?, IU.PASSWORD_EXPIRY=?"
        + " WHERE IU.USER_DIRECTORY_ID=? AND IU.ID=?";

    // createInternalGroupSQL
    createInternalGroupSQL = "INSERT INTO " + schemaPrefix + "INTERNAL_GROUPS"
        + " (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // createInternalUserSQL
    createInternalUserSQL = "INSERT INTO " + schemaPrefix + "INTERNAL_USERS"
        + " (ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES, LAST_NAME, PHONE,"
        + " FAX, MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY,"
        + " DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // deleteInternalGroupSQL
    deleteInternalGroupSQL = "DELETE FROM " + schemaPrefix + "INTERNAL_GROUPS IG"
        + " WHERE IG.USER_DIRECTORY_ID=? AND IG.ID=?";

    // deleteInternalUserSQL
    deleteInternalUserSQL = "DELETE FROM " + schemaPrefix + "INTERNAL_USERS IU"
        + " WHERE IU.USER_DIRECTORY_ID=? AND IU.ID=?";

    // getFilteredInternalUsersSQL
    getFilteredInternalUsersSQL =
      "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.TITLE, IU.FIRST_NAMES,"
      + " IU.LAST_NAME, IU.PHONE, IU.FAX, IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS,"
      + " IU.PASSWORD_EXPIRY, IU.DESCRIPTION FROM " + schemaPrefix
      + "INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=? AND"
      + " ((UPPER(IU.USERNAME) LIKE ?) OR (UPPER(IU.FIRST_NAMES) LIKE ?)"
      + " OR (UPPER(IU.LAST_NAME) LIKE ?)) ORDER BY IU.USERNAME";

    // getFunctionCodesForUserIdSQL
    getFunctionCodesForUserIdSQL =
      "SELECT DISTINCT F.CODE FROM " + schemaPrefix + "FUNCTIONS F" + " INNER JOIN " + schemaPrefix
      + "FUNCTION_TO_ROLE_MAP FTRM ON FTRM.FUNCTION_ID = F.ID INNER JOIN " + schemaPrefix
      + "ROLE_TO_GROUP_MAP RTGM ON RTGM.ROLE_ID = FTRM.ROLE_ID INNER JOIN " + schemaPrefix
      + "GROUPS G ON G.ID = RTGM.GROUP_ID INNER JOIN " + schemaPrefix
      + "INTERNAL_GROUPS IG ON IG.USER_DIRECTORY_ID = G.USER_DIRECTORY_ID"
      + "     AND UPPER(IG.GROUPNAME) = UPPER(G.GROUPNAME) INNER JOIN " + schemaPrefix
      + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTIGM"
      + "   ON IUTIGM.USER_DIRECTORY_ID = IG.USER_DIRECTORY_ID AND IUTIGM.INTERNAL_GROUP_ID = IG.ID"
      + " WHERE IUTIGM.USER_DIRECTORY_ID=? AND IUTIGM.INTERNAL_USER_ID=?";

    // getInternalGroupIdSQL
    getInternalGroupIdSQL = "SELECT IG.ID FROM " + schemaPrefix + "INTERNAL_GROUPS IG"
        + " WHERE IG.USER_DIRECTORY_ID=? AND UPPER(IG.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getInternalGroupNamesForInternalUserSQL
    getInternalGroupNamesForInternalUserSQL = "SELECT IG.GROUPNAME FROM " + schemaPrefix
        + "INTERNAL_GROUPS IG, " + schemaPrefix + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM"
        + " WHERE IG.ID = IUTGM.INTERNAL_GROUP_ID AND IUTGM.USER_DIRECTORY_ID=?"
        + " AND IUTGM.INTERNAL_USER_ID=? ORDER BY IG.GROUPNAME";

    // getInternalGroupSQL
    getInternalGroupSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM " + schemaPrefix
        + "INTERNAL_GROUPS IG WHERE IG.USER_DIRECTORY_ID=? AND"
        + " UPPER(IG.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getInternalGroupsForInternalUserSQL
    getInternalGroupsForInternalUserSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM "
        + schemaPrefix + "INTERNAL_GROUPS IG, " + schemaPrefix
        + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM"
        + " WHERE IG.ID = IUTGM.INTERNAL_GROUP_ID AND IUTGM.USER_DIRECTORY_ID=?"
        + " AND IUTGM.INTERNAL_USER_ID=? ORDER BY IG.GROUPNAME";

    // getInternalGroupsSQL
    getInternalGroupsSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM " + schemaPrefix
        + "INTERNAL_GROUPS IG WHERE IG.USER_DIRECTORY_ID=? ORDER BY IG.GROUPNAME";

    // getNumberOfFilteredInternalUsersSQL
    getNumberOfFilteredInternalUsersSQL = "SELECT COUNT(IU.ID) FROM " + schemaPrefix
        + "INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=? AND"
        + " ((UPPER(IU.USERNAME) LIKE ?) OR (UPPER(IU.FIRST_NAMES) LIKE ?)"
        + " OR (UPPER(IU.LAST_NAME) LIKE ?))";

    // getNumberOfInternalGroupsSQL
    getNumberOfInternalGroupsSQL = "SELECT COUNT(IG.ID) FROM " + schemaPrefix
        + "INTERNAL_GROUPS IG" + " WHERE IG.USER_DIRECTORY_ID=?";

    // getNumberOfUsersForGroupSQL
    getNumberOfUsersForGroupSQL = "SELECT COUNT (IUTGM.INTERNAL_USER_ID) FROM " + schemaPrefix
        + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM WHERE IUTGM.USER_DIRECTORY_ID=?"
        + " AND IUTGM.INTERNAL_GROUP_ID=?";

    // getNumberOfInternalUsersSQL
    getNumberOfInternalUsersSQL = "SELECT COUNT(IU.ID) FROM " + schemaPrefix + "INTERNAL_USERS IU"
        + " WHERE IU.USER_DIRECTORY_ID=?";

    // getInternalUserIdSQL
    getInternalUserIdSQL = "SELECT IU.ID FROM " + schemaPrefix + "INTERNAL_USERS IU"
        + " WHERE IU.USER_DIRECTORY_ID=? AND UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getInternalUserSQL
    getInternalUserSQL = "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.TITLE, IU.FIRST_NAMES,"
        + " IU.LAST_NAME, IU.PHONE, IU.FAX,  IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS,"
        + " IU.PASSWORD_EXPIRY, IU.DESCRIPTION FROM " + schemaPrefix + "INTERNAL_USERS IU"
        + " WHERE IU.USER_DIRECTORY_ID=? AND UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getInternalUsersSQL
    getInternalUsersSQL = "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.TITLE, IU.FIRST_NAMES,"
        + " IU.LAST_NAME, IU.PHONE, IU.FAX,  IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS,"
        + " IU.PASSWORD_EXPIRY, IU.DESCRIPTION FROM " + schemaPrefix + "INTERNAL_USERS IU"
        + " WHERE IU.USER_DIRECTORY_ID=? ORDER BY IU.USERNAME";

    // incrementPasswordAttemptsSQL
    incrementPasswordAttemptsSQL = "UPDATE " + schemaPrefix + "INTERNAL_USERS IU"
        + " SET IU.PASSWORD_ATTEMPTS = IU.PASSWORD_ATTEMPTS + 1"
        + " WHERE IU.USER_DIRECTORY_ID=? AND IU.ID=?";

    // isPasswordInInternalUserPasswordHistorySQL
    isPasswordInInternalUserPasswordHistorySQL = "SELECT IUPH.ID FROM " + schemaPrefix
        + "INTERNAL_USERS_PASSWORD_HISTORY IUPH"
        + " WHERE IUPH.INTERNAL_USER_ID=? AND IUPH.CHANGED > ? AND IUPH.PASSWORD=?";

    // isInternalUserInInternalGroupSQL
    isInternalUserInInternalGroupSQL = "SELECT IUTGM.INTERNAL_USER_ID FROM " + schemaPrefix
        + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM WHERE IUTGM.USER_DIRECTORY_ID=? AND"
        + " IUTGM.INTERNAL_USER_ID=? AND IUTGM.INTERNAL_GROUP_ID=?";

    // removeInternalUserFromInternalGroupSQL
    removeInternalUserFromInternalGroupSQL = "DELETE FROM " + schemaPrefix
        + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM"
        + " WHERE IUTGM.USER_DIRECTORY_ID=? AND IUTGM.INTERNAL_USER_ID=?"
        + " AND IUTGM.INTERNAL_GROUP_ID=?";

    // saveInternalUserPasswordHistorySQL
    saveInternalUserPasswordHistorySQL = "INSERT INTO " + schemaPrefix
        + "INTERNAL_USERS_PASSWORD_HISTORY"
        + " (ID, INTERNAL_USER_ID, CHANGED, PASSWORD) VALUES (?, ?, ?, ?)";

    // updateInternalGroupSQL
    updateInternalGroupSQL = "UPDATE " + schemaPrefix + "INTERNAL_GROUPS IG"
        + " SET IG.DESCRIPTION=? WHERE IG.USER_DIRECTORY_ID=? AND IG.ID=?";
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
   *
   * @throws InvalidAttributeException
   * @throws SQLException
   */
  private PreparedStatement buildFindUsersStatement(Connection connection,
      List<Attribute> attributes)
    throws InvalidAttributeException, AttributeException, SQLException
  {
    // Build the SQL statement to select the users
    StringBuilder buffer = new StringBuilder();

    buffer.append("SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.TITLE, IU.FIRST_NAMES, ");
    buffer.append("IU.LAST_NAME, IU.PHONE, IU.FAX, IU.MOBILE, IU.EMAIL, ");
    buffer.append("IU.PASSWORD_ATTEMPTS, IU.PASSWORD_EXPIRY, IU.DESCRIPTION FROM ");

    buffer.append(DataAccessObject.DEFAULT_DATABASE_SCHEMA).append(getDatabaseCatalogSeparator());

    buffer.append("INTERNAL_USERS IU");

    if (attributes.size() > 0)
    {
      // Build the parameters for the "WHERE" clause for the SQL statement
      StringBuilder whereParameters = new StringBuilder();

      for (Attribute attribute : attributes)
      {
        whereParameters.append(" AND ");

        if (attribute.getName().equalsIgnoreCase("description"))
        {
          whereParameters.append("LOWER(IU.DESCRIPTION) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("email"))
        {
          whereParameters.append("LOWER(IU.EMAIL) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("faxNumber"))
        {
          whereParameters.append("LOWER(IU.FAX) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("firstNames"))
        {
          whereParameters.append("LOWER(IU.FIRST_NAMES) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("lastName"))
        {
          whereParameters.append("LOWER(IU.LAST_NAME) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
        {
          whereParameters.append("LOWER(IU.MOBILE) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
        {
          whereParameters.append("LOWER(IU.PHONE) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("title"))
        {
          whereParameters.append("LOWER(IU.TITLE) LIKE LOWER(?)");
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

    statement.setLong(1, getUserDirectoryId());

    // Set the parameters for the prepared statement
    int parameterIndex = 2;

    for (Attribute attribute : attributes)
    {
      if (attribute.getName().equalsIgnoreCase("description"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("email"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("faxNumber"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("firstNames"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("lastName"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("title"))
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
   *
   * @throws SQLException
   */
  private User buildUserFromResultSet(ResultSet rs)
    throws SQLException
  {
    User user = new User(rs.getString(2));

    user.setId(rs.getLong(1));
    user.setUserDirectoryId(getUserDirectoryId());
    user.setPassword(StringUtil.notNull(rs.getString(3)));
    user.setTitle(StringUtil.notNull(rs.getString(4)));
    user.setFirstNames(StringUtil.notNull(rs.getString(5)));
    user.setLastName(StringUtil.notNull(rs.getString(6)));
    user.setPhoneNumber(StringUtil.notNull(rs.getString(7)));
    user.setFaxNumber(StringUtil.notNull(rs.getString(8)));
    user.setMobileNumber(StringUtil.notNull(rs.getString(9)));
    user.setEmail(StringUtil.notNull(rs.getString(10)));

    if (rs.getObject(11) != null)
    {
      user.setPasswordAttempts(rs.getInt(11));
    }

    if (rs.getObject(12) != null)
    {
      user.setPasswordExpiry(new Date(rs.getTimestamp(12).getTime()));
    }

    user.setDescription(StringUtil.notNull(rs.getString(13)));

    return user;
  }

  /**
   * Retrieve the list of authorised function codes for the internal user.
   *
   * @param connection     the existing database connection to use
   * @param internalUserId the numeric ID uniquely identifying the internal user
   *
   * @return the list of authorised function codes for the internal user with the specified ID
   *
   * @throws SQLException
   */
  private List<String> getFunctionCodesForUserId(Connection connection, long internalUserId)
    throws SQLException
  {
    List<String> functionCodes = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(getFunctionCodesForUserIdSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalUserId);

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
   * Retrieve the names for all the groups that the internal user with the specific numeric ID is
   * associated with.
   *
   * @param connection     the existing database connection
   * @param internalUserId the numeric ID for the internal user
   *
   * @return the list of groups
   *
   * @throws SQLException
   */
  private List<String> getGroupNamesForUser(Connection connection, long internalUserId)
    throws SQLException
  {
    try (PreparedStatement statement =
        connection.prepareStatement(getInternalGroupNamesForInternalUserSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalUserId);

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
   * Returns the numeric ID for the internal group with the specified group name.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the internal group
   *
   * @return the numeric ID for the internal group or -1 if an internal group with the specified
   *         group name could not be found
   *
   * @throws SecurityException
   */
  private long getInternalGroupId(Connection connection, String groupName)
    throws SecurityException
  {
    try (PreparedStatement statement = connection.prepareStatement(getInternalGroupIdSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setString(2, groupName);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getLong(1);
        }
        else
        {
          return -1;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the numeric ID for the group (" + groupName
          + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve all the internal groups that the internal user with the specific numeric ID is
   * associated with.
   *
   * @param connection     the existing database connection
   * @param internalUserId the numeric ID for the internal user
   *
   * @return the list of internal groups
   *
   * @throws SQLException
   */
  private List<Group> getInternalGroupsForInternalUser(Connection connection, long internalUserId)
    throws SQLException
  {
    try (PreparedStatement statement =
        connection.prepareStatement(getInternalGroupsForInternalUserSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalUserId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Group> list = new ArrayList<>();

        while (rs.next())
        {
          Group group = new Group(rs.getString(2));

          group.setId(rs.getLong(1));
          group.setUserDirectoryId(getUserDirectoryId());
          group.setDescription(rs.getString(3));
          list.add(group);
        }

        return list;
      }
    }
  }

  /**
   * Returns the numeric ID for the internal user with the specified username.
   *
   * @param connection the existing database connection to use
   * @param username   the username uniquely identifying the internal user
   *
   * @return the numeric ID for the internal user or -1 if an internal user with the specified
   *         username could not be found
   *
   * @throws SecurityException
   */
  private long getInternalUserId(Connection connection, String username)
    throws SecurityException
  {
    try (PreparedStatement statement = connection.prepareStatement(getInternalUserIdSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setString(2, username);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getLong(1);
        }
        else
        {
          return -1;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the numeric ID for the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve the IDs for all the internal users that are associated with the group with the
   * specific numeric ID.
   *
   * @param connection      the existing database connection
   * @param internalGroupId the numeric ID for the internal group
   *
   * @return the IDs for all the internal users that are associated with the group the specific
   *         numeric ID
   *
   * @throws SQLException
   */
  private long getNumberOfInternalUsersForInternalGroup(Connection connection, long internalGroupId)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(getNumberOfUsersForGroupSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalGroupId);

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
   *
   * @throws SQLException
   */
  private User getUser(Connection connection, String username)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(getInternalUserSQL))
    {
      statement.setLong(1, getUserDirectoryId());
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

  private void incrementPasswordAttempts(long internalUserId)
    throws SecurityException
  {
    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      try (Connection connection = getDataSource().getConnection())
      {
        try (PreparedStatement statement =
            connection.prepareStatement(incrementPasswordAttemptsSQL))
        {
          statement.setLong(1, getUserDirectoryId());
          statement.setLong(2, internalUserId);

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityException(
                "No rows were affected as a result of executing the SQL statement ("
                + incrementPasswordAttemptsSQL + ")");
          }
        }
      }

      transactionManager.commit();
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while incrementing the password attempts"
            + " for the user (" + internalUserId + ") for the user directory ("
            + getUserDirectoryId() + ")", f);
      }

      throw new SecurityException("Failed to increment the password attempts for the user ("
          + internalUserId + ") for the user directory (" + getUserDirectoryId() + "): "
          + e.getMessage(), e);
    }
    finally
    {
      try
      {
        transactionManager.resume(existingTransaction);
      }
      catch (Throwable e)
      {
        logger.error(
            "Failed to resume the original transaction while incrementing the password attempts"
            + " for the user (" + internalUserId + ") for the user directory ("
            + getUserDirectoryId() + ")", e);
      }
    }
  }

  /**
   * Is the user in the group?
   *
   * @param connection      the existing database connection
   * @param internalUserId  the numeric ID uniquely identifying the internal user
   * @param internalGroupId the numeric ID uniquely identifying the internal group
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   *
   * @throws SQLException
   */
  private boolean isInternalUserInInternalGroup(Connection connection, long internalUserId,
      long internalGroupId)
    throws SQLException
  {
    try (PreparedStatement statement =
        connection.prepareStatement(isInternalUserInInternalGroupSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setLong(2, internalUserId);
      statement.setLong(3, internalGroupId);

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
   * @param internalUserId the numeric ID uniquely identifying the internal user
   * @param passwordHash   the password hash
   *
   * @return <code>true</code> if the password was previously used and cannot be reused for a
   *         period of time or <code>false</code> otherwise
   *
   * @throws SQLException
   */
  private boolean isPasswordInHistory(Connection connection, long internalUserId,
      String passwordHash)
    throws SQLException
  {
    try (PreparedStatement statement =
        connection.prepareStatement(isPasswordInInternalUserPasswordHistorySQL))
    {
      Calendar calendar = Calendar.getInstance();

      calendar.setTime(new Date());
      calendar.add(Calendar.MONTH, -1 * passwordHistoryMonths);

      statement.setLong(1, internalUserId);
      statement.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
      statement.setString(3, passwordHash);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
  }

  private void savePasswordHistory(Connection connection, long internalUserId, String passwordHash)
    throws SQLException
  {
    try (PreparedStatement statement =
        connection.prepareStatement(saveInternalUserPasswordHistorySQL))
    {
      long id = nextId("Application.InternalUserPasswordHistoryId");

      statement.setLong(1, id);
      statement.setLong(2, internalUserId);
      statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      statement.setString(4, passwordHash);
      statement.execute();
    }
  }
}
