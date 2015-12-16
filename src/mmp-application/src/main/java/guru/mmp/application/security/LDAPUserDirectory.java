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

import guru.mmp.common.util.JNDIUtil;
import guru.mmp.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.*;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;

/**
 * The <code>LDAPUserDirectory</code> class provides the LDAP user directory implementation.
 *
 * @author Marcus Portmann
 */
public class LDAPUserDirectory extends UserDirectoryBase
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
   * The default maximum number of filtered groups.
   */
  private static final int DEFAULT_MAX_FILTERED_GROUPS = 100;

  /**
   * The default maximum number of filtered users.
   */
  private static final int DEFAULT_MAX_FILTERED_USERS = 100;

  /**
   * The default maximum length of a users's password history.
   */
  private static final int DEFAULT_PASSWORD_HISTORY_MAX_LENGTH = 128;
  private static final String[] EMPTY_ATTRIBUTE_LIST = new String[0];

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(LDAPUserDirectory.class);
  private String baseDN;
  private String bindDN;
  private String bindPassword;
  private String getFunctionCodesForGroupsSQL;
  private String groupBaseDN;
  private String groupDescriptionAttribute;
  private String groupMemberAttribute;
  private String[] groupMemberAttributeArray;
  private String groupNameAttribute;
  private String groupObjectClass;
  private String host;
  private int maxFilteredGroups;

  /*
   * private String addInternalUserToInternalGroupSQL;
   * private String changeInternalUserPasswordSQL;
   * private String createInternalGroupSQL;
   * private String createInternalUserSQL;
   * private String deleteInternalGroupSQL;
   * private String deleteInternalUserSQL;
   * private String getFilteredInternalUsersSQL;
   * private String getFunctionCodesForUserIdSQL;
   * private String getInternalGroupIdSQL;
   * private String getInternalGroupNamesForInternalUserSQL;
   * private String getInternalGroupSQL;
   * private String getInternalGroupsForInternalUserSQL;
   * private String getInternalGroupsSQL;
   * private String getInternalUserIdSQL;
   * private String getInternalUserSQL;
   * private String getInternalUsersSQL;
   * private String getNumberOfFilteredInternalUsersSQL;
   * private String getNumberOfInternalGroupsSQL;
   * private String getNumberOfInternalUsersSQL;
   * private String getNumberOfUsersForGroupSQL;
   * private String isInternalUserInInternalGroupSQL;
   * private String isPasswordInInternalUserPasswordHistorySQL;
   */
  private int maxFilteredUsers;
  private int maxPasswordAttempts;
  private int passwordExpiryMonths;
  private int passwordHistoryMaxLength;
  private int passwordHistoryMonths;
  private int port;
  private String sharedBaseDN;
  private boolean supportPasswordHistory;
  private boolean useSSL;
  private String userBaseDN;
  private String userDescriptionAttribute;
  private String userEmailAttribute;
  private String userFaxNumberAttribute;
  private String userFirstNamesAttribute;
  private String userLastNameAttribute;
  private String userMobileNumberAttribute;
  private String userObjectClass;
  private String userPasswordAttemptsAttribute;
  private String userPasswordExpiryAttribute;
  private String userPasswordHistoryAttribute;
  private String userPhoneNumberAttribute;
  private String userTitleAttribute;
  private String userUsernameAttribute;

  /*
   * private String removeInternalUserFromInternalGroupSQL;
   * private String saveInternalUserPasswordHistorySQL;
   * private String updateInternalGroupSQL;
   */

  /**
   * Constructs a new <code>LDAPUserDirectory</code>.
   *
   * @param userDirectoryId the unique ID for the user directory
   * @param parameters      the key-value configuration parameters for the user directory
   *
   * @throws SecurityException
   */
  public LDAPUserDirectory(long userDirectoryId, Map<String, String> parameters)
    throws SecurityException
  {
    super(userDirectoryId, parameters);

    try
    {
      if (parameters.containsKey("Host"))
      {
        host = parameters.get("Host");
      }
      else
      {
        throw new SecurityException(
            "No Host configuration parameter found for the user directory (" + userDirectoryId
            + ")");
      }

      if (parameters.containsKey("Port"))
      {
        port = Integer.parseInt(parameters.get("Port"));
      }
      else
      {
        throw new SecurityException(
            "No Port configuration parameter found for the user directory (" + userDirectoryId
            + ")");
      }

      if (parameters.containsKey("UseSSL"))
      {
        useSSL = Boolean.parseBoolean(parameters.get("UseSSL"));
      }
      else
      {
        useSSL = false;
      }

      if (parameters.containsKey("BindDN"))
      {
        bindDN = parameters.get("BindDN");
      }
      else
      {
        throw new SecurityException(
            "No BindDN configuration parameter found for the user directory (" + userDirectoryId
            + ")");
      }

      if (parameters.containsKey("BindPassword"))
      {
        bindPassword = parameters.get("BindPassword");
      }
      else
      {
        throw new SecurityException(
            "No BindPassword configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("BaseDN"))
      {
        baseDN = parameters.get("BindDN");
      }
      else
      {
        throw new SecurityException(
            "No BindDN configuration parameter found for the user directory (" + userDirectoryId
            + ")");
      }

      if (parameters.containsKey("UserBaseDN"))
      {
        userBaseDN = parameters.get("UserBaseDN");
      }
      else
      {
        throw new SecurityException(
            "No UserBaseDN configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("GroupBaseDN"))
      {
        groupBaseDN = parameters.get("GroupBaseDN");
      }
      else
      {
        throw new SecurityException(
            "No GroupBaseDN configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("SharedBaseDN"))
      {
        sharedBaseDN = parameters.get("SharedBaseDN");
      }

      if (parameters.containsKey("UserObjectClass"))
      {
        userObjectClass = parameters.get("UserObjectClass");
      }
      else
      {
        throw new SecurityException(
            "No UserObjectClass configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserUsernameAttribute"))
      {
        userUsernameAttribute = parameters.get("UserUsernameAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserUsernameAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserPasswordExpiryAttribute"))
      {
        userPasswordExpiryAttribute = parameters.get("UserPasswordExpiryAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserPasswordExpiryAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserPasswordAttemptsAttribute"))
      {
        userPasswordAttemptsAttribute = parameters.get("UserPasswordAttemptsAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserPasswordAttemptsAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserPasswordHistoryAttribute"))
      {
        userPasswordHistoryAttribute = parameters.get("UserPasswordHistoryAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserPasswordHistoryAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserTitleAttribute"))
      {
        userTitleAttribute = parameters.get("UserTitleAttribute");
      }

      if (parameters.containsKey("UserFirstNamesAttribute"))
      {
        userFirstNamesAttribute = parameters.get("UserFirstNamesAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserFirstNamesAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserLastNameAttribute"))
      {
        userLastNameAttribute = parameters.get("UserLastNameAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserLastNameAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserPhoneNumberAttribute"))
      {
        userPhoneNumberAttribute = parameters.get("UserPhoneNumberAttribute");
      }

      if (parameters.containsKey("UserFaxNumberAttribute"))
      {
        userFaxNumberAttribute = parameters.get("UserFaxNumberAttribute");
      }

      if (parameters.containsKey("UserMobileNumberAttribute"))
      {
        userMobileNumberAttribute = parameters.get("UserMobileNumberAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserMobileNumberAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserEmailAttribute"))
      {
        userEmailAttribute = parameters.get("UserEmailAttribute");
      }
      else
      {
        throw new SecurityException(
            "No UserEmailAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("UserDescriptionAttribute"))
      {
        userDescriptionAttribute = parameters.get("UserDescriptionAttribute");
      }

      if (parameters.containsKey("GroupObjectClass"))
      {
        groupObjectClass = parameters.get("GroupObjectClass");
      }
      else
      {
        throw new SecurityException(
            "No GroupObjectClass configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("GroupNameAttribute"))
      {
        groupNameAttribute = parameters.get("GroupNameAttribute");
      }
      else
      {
        throw new SecurityException(
            "No GroupNameAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("GroupMemberAttribute"))
      {
        groupMemberAttribute = parameters.get("GroupMemberAttribute");

        groupMemberAttributeArray = new String[] { groupMemberAttribute };
      }
      else
      {
        throw new SecurityException(
            "No GroupMemberAttribute configuration parameter found for the user directory ("
            + userDirectoryId + ")");
      }

      if (parameters.containsKey("GroupDescriptionAttribute"))
      {
        groupDescriptionAttribute = parameters.get("GroupDescriptionAttribute");
      }

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

      supportPasswordHistory = parameters.containsKey("SupportPasswordHistory")
          && Boolean.parseBoolean(parameters.get("SupportPasswordHistory"));

      if (parameters.containsKey("PasswordHistoryMonths"))
      {
        passwordHistoryMonths = Integer.parseInt(parameters.get("PasswordHistoryMonths"));
      }
      else
      {
        passwordHistoryMonths = DEFAULT_PASSWORD_HISTORY_MONTHS;
      }

      if (parameters.containsKey("PasswordHistoryMaxLength"))
      {
        passwordHistoryMaxLength = Integer.parseInt(parameters.get("PasswordHistoryMaxLength"));
      }
      else
      {
        passwordHistoryMonths = DEFAULT_PASSWORD_HISTORY_MAX_LENGTH;
      }

      if (parameters.containsKey("MaxFilteredUsers"))
      {
        maxFilteredUsers = Integer.parseInt(parameters.get("MaxFilteredUsers"));
      }
      else
      {
        maxFilteredUsers = DEFAULT_MAX_FILTERED_USERS;
      }

      if (parameters.containsKey("MaxFilteredGroups"))
      {
        maxFilteredGroups = Integer.parseInt(parameters.get("MaxFilteredGroups"));
      }
      else
      {
        maxFilteredGroups = DEFAULT_MAX_FILTERED_GROUPS;
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /*
     * try (Connection connection = getDataSource().getConnection();
     * PreparedStatement statement = connection.prepareStatement(addInternalUserToInternalGroupSQL))
     * {
     * // Get the ID of the internal user with the specified username
     * long internalUserId;
     *
     * if ((internalUserId = getInternalUserId(connection, username)) == -1)
     * {
     *   throw new UserNotFoundException("The user (" + username + ") could not be found");
     * }
     *
     * // Get the ID of the internal group with the specified group name
     * long internalGroupId;
     *
     * if ((internalGroupId = getInternalGroupId(connection, groupName)) == -1)
     * {
     *   throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
     * }
     *
     * // Check if the user has already been added to the group for the user directory
     * if (isInternalUserInInternalGroup(connection, internalUserId, internalGroupId))
     * {
     *   // The user is already a member of the specified group do nothing
     *   return;
     * }
     *
     * // Add the user to the group
     * statement.setLong(1, getUserDirectoryId());
     * statement.setLong(2, internalUserId);
     * statement.setLong(3, internalGroupId);
     *
     * if (statement.executeUpdate() != 1)
     * {
     *   throw new SecurityException(
     *       "No rows were affected as a result of executing the SQL statement ("
     *       + addInternalUserToInternalGroupSQL + ")");
     * }
     * }
     * catch (UserNotFoundException | GroupNotFoundException | OrganisationNotFoundException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to add the user (" + username + ") to the group ("
     *     + groupName + ") for the user directory (" + getUserDirectoryId() + "): "
     *     + e.getMessage(), e);
     * }
     */
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /* NOTE: THE LOGIC BELOW FOR PASSWORD ATTEMPTS IS BROKEN!!!! */

    /*
     * try (Connection connection = getDataSource().getConnection();
     * PreparedStatement statement = connection.prepareStatement(changeInternalUserPasswordSQL))
     * {
     * User user = getUser(connection, username);
     *
     * if (user == null)
     * {
     *   throw new UserNotFoundException("The user (" + username + ") could not be found");
     * }
     *
     * String passwordHash = createPasswordHash(newPassword);
     *
     * statement.setString(1, passwordHash);
     *
     *
     *  THIS LOGIC IS WRONG SEE InternalUserDirectory
     *
     * if (lockUser)
     * {
     *   statement.setInt(2, -1);
     * }
     * else
     * {
     *   if (!isNullOrEmpty(user.getPasswordAttempts()))
     *   {
     *     statement.setInt(2, 0);
     *   }
     *   else
     *   {
     *     statement.setNull(2, java.sql.Types.INTEGER);
     *   }
     * }
     *
     * if (expirePassword)
     * {
     *   statement.setTimestamp(3, new Timestamp(System.currentTimeMillis() - 1000L));
     * }
     * else
     * {
     *   if (user.getPasswordExpiry() != null)
     *   {
     *     Calendar calendar = Calendar.getInstance();
     *
     *     calendar.setTime(new Date());
     *     calendar.add(Calendar.MONTH, passwordExpiryMonths);
     *     statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
     *   }
     *   else
     *   {
     *     statement.setTimestamp(3, null);
     *   }
     * }
     *
     * statement.setLong(4, getUserDirectoryId());
     * statement.setLong(5, user.getId());
     *
     * if (statement.executeUpdate() != 1)
     * {
     *   throw new SecurityException(
     *       "No rows were affected as a result of executing the SQL statement ("
     *       + changeInternalUserPasswordSQL + ")");
     * }
     *
     * savePasswordHistory(connection, user.getId(), passwordHash);
     * }
     * catch (UserNotFoundException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to administratively change the password for the user ("
     *     + username + ") for the user directory (" + getUserDirectoryId() + "): "
     *     + e.getMessage(), e);
     * }
     */
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      User user = getUser(dirContext, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      if ((user.getPasswordAttempts() == null)
          || (user.getPasswordAttempts() >= maxPasswordAttempts))
      {
        throw new UserLockedException("The user (" + username
            + ") has exceeded the number of failed password attempts and has been locked");
      }

      if (user.getPasswordExpiry() != null)
      {
        if (user.getPasswordExpiry().before(new Date()))
        {
          throw new ExpiredPasswordException("The password for the user (" + username
              + ") has expired");
        }
      }

      DirContext userDirContext = null;

      try
      {
        getDirContext(user.getProperty("dn"), password);

        user.setPassword(password);
      }
      catch (Throwable e)
      {
        if (e.getCause() instanceof javax.naming.AuthenticationException)
        {
          incrementPasswordAttempts(dirContext, user);

          throw new AuthenticationFailedException("Failed to authenticate the user (" + username
              + ") for the user directory (" + getUserDirectoryId() + ")");
        }
        else
        {
          logger.error("Failed to authenticate the user (" + username
              + ") for the user directory (" + getUserDirectoryId() + ")", e);

          throw new AuthenticationFailedException("Failed to authenticate the user (" + username
              + ") for the user directory (" + getUserDirectoryId() + ")", e);
        }
      }
      finally
      {
        JNDIUtil.close(userDirContext);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /* NOTE: THE LOGIC BELOW FOR PASSWORD ATTEMPTS IS BROKEN!!!! */

    /*
     * try (Connection connection = getDataSource().getConnection();
     * PreparedStatement statement = connection.prepareStatement(changeInternalUserPasswordSQL))
     * {
     * User user = getUser(connection, username);
     *
     * if (user == null)
     * {
     *   throw new UserNotFoundException("The user (" + username + ") could not be found");
     * }
     *
     *
     * if (user.getPasswordAttempts() != null)
     *  {
     *   THIS LOGIC IS WRONG SEE InternalUserDirectory
     *
     *   if ((user.getPasswordAttempts() == -1)
     *       || (user.getPasswordAttempts() > maxPasswordAttempts))
     *   {
     *     throw new UserLockedException("The user (" + username
     *         + ") has exceeded the number of failed password attempts and has been locked");
     *   }
     * }
     *
     * String passwordHash = createPasswordHash(password);
     * String newPasswordHash = createPasswordHash(newPassword);
     *
     * if (!user.getPassword().equals(passwordHash))
     * {
     *   throw new AuthenticationFailedException("Authentication failed while attempting to change"
     *       + " the password for the user (" + username + ")");
     * }
     *
     * if (isPasswordInHistory(connection, user.getId(), newPasswordHash))
     * {
     *   throw new ExistingPasswordException("The new password for the user (" + username
     *       + ") has been used recently and is not valid");
     * }
     *
     * statement.setString(1, newPasswordHash);
     *
     * if (!isNullOrEmpty(user.getPasswordAttempts()))
     * {
     *   statement.setInt(2, 0);
     * }
     * else
     * {
     *   statement.setNull(2, java.sql.Types.INTEGER);
     * }
     *
     * if (user.getPasswordExpiry() != null)
     * {
     *   Calendar calendar = Calendar.getInstance();
     *
     *   calendar.setTime(new Date());
     *   calendar.add(Calendar.MONTH, passwordExpiryMonths);
     *   statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
     * }
     * else
     * {
     *   statement.setTimestamp(3, null);
     * }
     *
     * statement.setLong(4, getUserDirectoryId());
     * statement.setLong(5, user.getId());
     *
     * if (statement.executeUpdate() != 1)
     * {
     *   throw new SecurityException(
     *       "No rows were affected as a result of executing the SQL statement ("
     *       + changeInternalUserPasswordSQL + ")");
     * }
     *
     * savePasswordHistory(connection, user.getId(), newPasswordHash);
     * }
     * catch (AuthenticationFailedException | ExistingPasswordException | UserNotFoundException
     *   | ExpiredPasswordException | UserLockedException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to change the password for the user (" + username
     *     + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
     * }
     */
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName groupDN = getGroupDN(dirContext, group.getGroupName());

      if (groupDN != null)
      {
        throw new DuplicateGroupException("The group (" + group.getGroupName()
            + ") already exists");
      }

      Attributes attributes = new BasicAttributes();

      attributes.put(new BasicAttribute("objectclass", "top"));
      attributes.put(new BasicAttribute("objectclass", "groupOfNames"));

      attributes.put(new BasicAttribute(groupNameAttribute, group.getGroupName()));

      if ((!StringUtil.isNullOrEmpty(groupDescriptionAttribute))
          && (!StringUtil.isNullOrEmpty(group.getDescription())))
      {
        attributes.put(new BasicAttribute(groupDescriptionAttribute, group.getDescription()));
      }

      dirContext.bind(groupNameAttribute + "=" + group.getGroupName() + "," + groupBaseDN,
          dirContext, attributes);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /*
     * try (Connection connection = getDataSource().getConnection();
     * PreparedStatement statement = connection.prepareStatement(createInternalUserSQL))
     * {
     * if (getInternalUserId(connection, user.getUsername()) != -1)
     * {
     *   throw new DuplicateUserException("The user (" + user.getUsername() + ") already exists");
     * }
     *
     * long internalUserId = nextId("Application.InternalUserId");
     *
     * statement.setLong(1, internalUserId);
     * statement.setLong(2, getUserDirectoryId());
     * statement.setString(3, user.getUsername());
     *
     * String passwordHash;
     *
     * if (!isNullOrEmpty(user.getPassword()))
     * {
     *   passwordHash = createPasswordHash(user.getPassword());
     *   statement.setString(4, passwordHash);
     * }
     * else
     * {
     *   passwordHash = createPasswordHash("");
     *   statement.setString(4, passwordHash);
     * }
     *
     * statement.setString(5, StringUtil.notNull(user.getTitle()));
     * statement.setString(6, StringUtil.notNull(user.getFirstNames()));
     * statement.setString(7, StringUtil.notNull(user.getLastName()));
     * statement.setString(8, StringUtil.notNull(user.getPhoneNumber()));
     * statement.setString(9, StringUtil.notNull(user.getFaxNumber()));
     * statement.setString(10, StringUtil.notNull(user.getMobileNumber()));
     * statement.setString(11, StringUtil.notNull(user.getEmail()));
     *
     * if (userLocked)
     * {
     *   statement.setInt(12, -1);
     * }
     * else
     * {
     *   if (!isNullOrEmpty(user.getPasswordAttempts()))
     *   {
     *     statement.setInt(12, user.getPasswordAttempts());
     *   }
     *   else
     *   {
     *     statement.setNull(12, java.sql.Types.INTEGER);
     *   }
     * }
     *
     * if (expiredPassword)
     * {
     *   statement.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
     * }
     * else
     * {
     *   if (user.getPasswordExpiry() != null)
     *   {
     *     statement.setTimestamp(13, new Timestamp(user.getPasswordExpiry().getTime()));
     *   }
     *   else
     *   {
     *     statement.setTimestamp(13, null);
     *   }
     * }
     *
     * statement.setString(14, StringUtil.notNull(user.getDescription()));
     *
     * if (statement.executeUpdate() != 1)
     * {
     *   throw new SecurityException(
     *       "No rows were affected as a result of executing the SQL statement ("
     *       + createInternalUserSQL + ")");
     * }
     *
     * user.setId(internalUserId);
     * user.setUserDirectoryId(getUserDirectoryId());
     *
     * // Save the password in the password history if one was specified
     * if (passwordHash != null)
     * {
     *   savePasswordHistory(connection, internalUserId, passwordHash);
     * }
     * }
     * catch (DuplicateUserException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to create the user (" + user.getUsername()
     *     + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
     * }
     */
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }


      if (true)
      {
        throw new ExistingGroupMembersException("The group (" + groupName
          + ") could not be deleted since it is still associated with 1 or more user(s)");
      }

      XXX


      dirContext.destroySubcontext(groupDN);
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /*
     * try (Connection connection = getDataSource().getConnection();
     * PreparedStatement statement = connection.prepareStatement(deleteInternalUserSQL))
     * {
     * long internalUserId = getInternalUserId(connection, username);
     *
     * if (internalUserId == -1)
     * {
     *   throw new UserNotFoundException("The user (" + username + ") could not be found");
     * }
     *
     * statement.setLong(1, getUserDirectoryId());
     * statement.setLong(2, internalUserId);
     *
     * if (statement.executeUpdate() <= 0)
     * {
     *   throw new SecurityException(
     *       "No rows were affected as a result of executing the SQL statement ("
     *       + deleteInternalUserSQL + ")");
     * }
     * }
     * catch (UserNotFoundException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to delete the user (" + username
     *     + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
     * }
     */
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /*
     * try (Connection connection = getDataSource().getConnection())
     * {
     * try (PreparedStatement statement = buildFindUsersStatement(connection, attributes))
     * {
     *   try (ResultSet rs = statement.executeQuery())
     *   {
     *     List<User> list = new ArrayList<>();
     *
     *     while (rs.next())
     *     {
     *       User user = buildUserFromResultSet(rs);
     *
     *       list.add(user);
     *     }
     *
     *     return list;
     *   }
     * }
     * }
     * catch (InvalidAttributeException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to find the users for the user directory ("
     *     + getUserDirectoryId() + "): " + e.getMessage(), e);
     * }
     */
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + userObjectClass + ")";

      if (!StringUtil.isNullOrEmpty(filter))
      {
        searchFilter = "(&(objectClass=" + userObjectClass + ")(|(" + userUsernameAttribute
          + "=*" + filter + "*)(" + userFirstNamesAttribute + "=*" + filter + "*)("
          + userLastNameAttribute + "=*" + filter + "*)))";
      }

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setCountLimit(maxFilteredUsers);

      List<User> users = new ArrayList<>();

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
      {
        users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
      }

      searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

      while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
      {
        users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the filtered users for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      String searchFilter = "(&(objectClass=" + groupObjectClass + ")(" + groupMemberAttribute
        + "=" + userDN.toString() + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<String> groupNames = new ArrayList<>();

      while (searchResults.hasMore())
      {
        SearchResult searchResult = searchResults.next();

        if (searchResult.getAttributes().get(groupNameAttribute) != null)
        {
          groupNames.add(
              String.valueOf(searchResult.getAttributes().get(groupNameAttribute).get()));
        }
      }

      /*
       * Build the SQL statement used to retrieve the function codes associated with the LDAP
       * groups the user is a member of.
       *
       * NOTE: This is not the ideal solution as a carefully crafted group name in the LDAP
       *       directory can be used to perpetrate a SQL injection attack. A better option
       *       would to be to use the ANY operator in the WHERE clause but this is not
       *       supported by H2.
       */

      StringBuilder buffer = new StringBuilder(getFunctionCodesForGroupsSQL);
      buffer.append(" WHERE G.USER_DIRECTORY_ID=").append(getUserDirectoryId());
      buffer.append(" AND G.GROUPNAME IN (");

      for (int i = 0; i < groupNames.size(); i++)
      {
        if (i > 0)
        {
          buffer.append(",");
        }

        buffer.append("'").append(groupNames.get(i).replaceAll("'", "''")).append("'");
      }

      buffer.append(")");

      List<String> functionCodes = new ArrayList<>();

      try (Connection connection = getDataSource().getConnection();
        Statement statement = connection.createStatement())
      {
        try (ResultSet rs = statement.executeQuery(buffer.toString()))
        {
          while (rs.next())
          {
            functionCodes.add(rs.getString(1));
          }
        }
      }

      return functionCodes;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(&(objectClass=" + groupObjectClass + ")(" + groupNameAttribute + "="
        + groupName + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      if (searchResults.hasMore())
      {
        return buildGroupFromSearchResult(searchResults.next());
      }
      else
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      String searchFilter = "(&(objectClass=" + groupObjectClass + ")(" + groupMemberAttribute
        + "=" + userDN.toString() + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<String> groupNames = new ArrayList<>();

      while (searchResults.hasMore())
      {
        SearchResult searchResult = searchResults.next();

        if (searchResult.getAttributes().get(groupNameAttribute) != null)
        {
          groupNames.add(
              String.valueOf(searchResult.getAttributes().get(groupNameAttribute).get()));
        }
      }

      return groupNames;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + groupObjectClass + ")";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setCountLimit(maxFilteredGroups);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<Group> groups = new ArrayList<>();

      while (searchResults.hasMore())
      {
        groups.add(buildGroupFromSearchResult(searchResults.next()));
      }

      return groups;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the groups for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      String searchFilter = "(&(objectClass=" + groupObjectClass + ")(" + groupMemberAttribute
        + "=" + userDN.toString() + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<Group> groups = new ArrayList<>();

      while (searchResults.hasMore())
      {
        groups.add(buildGroupFromSearchResult(searchResults.next()));
      }

      return groups;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + userObjectClass + ")";

      if (!StringUtil.isNullOrEmpty(filter))
      {
        searchFilter = "(&(objectClass=" + userObjectClass + ")(|(" + userUsernameAttribute
          + "=*" + filter + "*)(" + userFirstNamesAttribute + "=*" + filter + "*)("
          + userLastNameAttribute + "=*" + filter + "*)))";
      }

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);
      searchControls.setCountLimit(maxFilteredUsers);

      int numberOfUsers = 0;

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
      {
        searchResultsNonSharedUsers.next();

        numberOfUsers++;
      }

      searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

      while (searchResultsSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
      {
        searchResultsSharedUsers.next();

        numberOfUsers++;
      }

      return numberOfUsers;
    }
    catch (Throwable e)
    {
      throw new SecurityException(
          "Failed to retrieve the number of filtered users for the user directory ("
          + getUserDirectoryId() + "):" + e.getMessage(), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + groupObjectClass + ")";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);
      searchControls.setCountLimit(maxFilteredGroups);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      int numberOfGroups = 0;

      while (searchResults.hasMore())
      {
        searchResults.next();

        numberOfGroups++;
      }

      return numberOfGroups;
    }
    catch (Throwable e)
    {
      throw new SecurityException(
          "Failed to retrieve the number of groups for the user directory (" + getUserDirectoryId()
          + "):" + e.getMessage(), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + userObjectClass + ")";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);
      searchControls.setCountLimit(maxFilteredUsers);

      int numberOfUsers = 0;

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
      {
        searchResultsNonSharedUsers.next();

        numberOfUsers++;
      }

      searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

      while (searchResultsSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
      {
        searchResultsSharedUsers.next();

        numberOfUsers++;
      }

      return numberOfUsers;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the number of users for the user directory ("
          + getUserDirectoryId() + "):" + e.getMessage(), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      User user = getUser(dirContext, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      return user;
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + userObjectClass + ")";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setCountLimit(maxFilteredUsers);

      List<User> users = new ArrayList<>();

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
      {
        users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
      }

      searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

      while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
      {
        users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the users for the user directory ("
          + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(&(objectClass=" + userObjectClass + ")(" + userUsernameAttribute
        + "=" + username + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      // First search for a non-shared user
      if (!StringUtil.isNullOrEmpty(userBaseDN))
      {
        searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

        if (searchResultsNonSharedUsers.hasMore())
        {
          return true;
        }
      }

      // Next search for a shared user
      if (!StringUtil.isNullOrEmpty(sharedBaseDN))
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        if (searchResultsSharedUsers.hasMore())
        {
          return true;
        }
      }

      return false;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to check whether the user (" + username
          + ") is an existing user for the user directory (" + getUserDirectoryId() + ")", e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      if (attributes.get(groupMemberAttribute) != null)
      {
        NamingEnumeration<?> attributeValues = attributes.get(groupMemberAttribute).getAll();

        while (attributeValues.hasMore())
        {
          LdapName memberDN = new LdapName((String) attributeValues.next());

          if (memberDN.equals(userDN))
          {
            return true;
          }
        }
      }

      return false;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /*
     * try (Connection connection = getDataSource().getConnection();
     * PreparedStatement statement =
     *     connection.prepareStatement(removeInternalUserFromInternalGroupSQL))
     * {
     * // Get the ID of the internal user with the specified username
     * long internalUserId = getInternalUserId(connection, username);
     *
     * if (internalUserId == -1)
     * {
     *   throw new UserNotFoundException("The user (" + username + ") could not be found");
     * }
     *
     * // Get the ID of the internal group with the specified group name
     * long internalGroupId = getInternalGroupId(connection, groupName);
     *
     * if (internalGroupId == -1)
     * {
     *   throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
     * }
     *
     * // Remove the user from the group
     * statement.setLong(1, getUserDirectoryId());
     * statement.setLong(2, internalUserId);
     * statement.setLong(3, internalGroupId);
     * statement.executeUpdate();
     * }
     * catch (UserNotFoundException | GroupNotFoundException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to remove the user (" + username + ") from the group ("
     *     + groupName + ") for the user directory (" + getUserDirectoryId() + "): "
     *     + e.getMessage(), e);
     * }
     */
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /*
     * try (Connection connection = getDataSource().getConnection();
     * PreparedStatement statement = connection.prepareStatement(updateInternalGroupSQL))
     * {
     * long internalGroupId = getInternalGroupId(connection, group.getGroupName());
     *
     * if (internalGroupId == -1)
     * {
     *   throw new GroupNotFoundException("The group (" + group.getGroupName()
     *       + ") could not be found");
     * }
     *
     * statement.setString(1, StringUtil.notNull(group.getDescription()));
     * statement.setLong(2, getUserDirectoryId());
     * statement.setLong(3, internalGroupId);
     *
     * if (statement.executeUpdate() <= 0)
     * {
     *   throw new SecurityException(
     *       "No rows were affected as a result of executing the SQL statement ("
     *       + updateInternalGroupSQL + ")");
     * }
     * }
     * catch (GroupNotFoundException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to update the group (" + group.getGroupName()
     *     + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
     * }
     */
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
    throw new SecurityException("TODO: NOT IMPLEMENTED");

    /*
     * try (Connection connection = getDataSource().getConnection())
     * {
     * long internalUserId = getInternalUserId(connection, user.getUsername());
     *
     * if (internalUserId == -1)
     * {
     *   throw new UserNotFoundException("The user (" + user.getUsername() + ") could not be found");
     * }
     *
     * StringBuilder buffer = new StringBuilder();
     *
     * buffer.append("UPDATE ");
     * buffer.append(DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA).append(
     *     getDatabaseCatalogSeparator());
     *
     * buffer.append("INTERNAL_USERS ");
     *
     * StringBuilder fieldsBuffer = new StringBuilder();
     *
     * if (user.getTitle() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET TITLE=?"
     *       : ", TITLE=?");
     * }
     *
     * if (user.getFirstNames() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET FIRST_NAMES=?"
     *       : ", FIRST_NAMES=?");
     * }
     *
     * if (user.getLastName() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET LAST_NAME=?"
     *       : ", LAST_NAME=?");
     * }
     *
     * if (user.getEmail() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET EMAIL=?"
     *       : ", EMAIL=?");
     * }
     *
     * if (user.getPhoneNumber() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET PHONE=?"
     *       : ", PHONE=?");
     * }
     *
     * if (user.getFaxNumber() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET FAX=?"
     *       : ", FAX=?");
     * }
     *
     * if (user.getMobileNumber() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET MOBILE=?"
     *       : ", MOBILE=?");
     * }
     *
     * if (user.getDescription() != null)
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET DESCRIPTION=?"
     *       : ", DESCRIPTION=?");
     * }
     *
     * if (!StringUtil.isNullOrEmpty(user.getPassword()))
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET PASSWORD=?"
     *       : ", PASSWORD=?");
     * }
     *
     * if (lockUser || (user.getPasswordAttempts() != null))
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET PASSWORD_ATTEMPTS=?"
     *       : ", PASSWORD_ATTEMPTS=?");
     * }
     *
     * if (expirePassword || (user.getPasswordExpiry() != null))
     * {
     *   fieldsBuffer.append((fieldsBuffer.length() == 0)
     *       ? "SET PASSWORD_EXPIRY=?"
     *       : ", PASSWORD_EXPIRY=?");
     * }
     *
     * buffer.append(fieldsBuffer.toString());
     * buffer.append(" WHERE USER_DIRECTORY_ID=? AND ID=?");
     *
     * String updateUserSQL = buffer.toString();
     *
     * try (PreparedStatement statement = connection.prepareStatement(updateUserSQL))
     * {
     *   int parameterIndex = 1;
     *
     *   if (user.getTitle() != null)
     *   {
     *     statement.setString(parameterIndex, user.getTitle());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getFirstNames() != null)
     *   {
     *     statement.setString(parameterIndex, user.getFirstNames());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getLastName() != null)
     *   {
     *     statement.setString(parameterIndex, user.getLastName());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getEmail() != null)
     *   {
     *     statement.setString(parameterIndex, user.getEmail());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getPhoneNumber() != null)
     *   {
     *     statement.setString(parameterIndex, user.getPhoneNumber());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getFaxNumber() != null)
     *   {
     *     statement.setString(parameterIndex, user.getFaxNumber());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getMobileNumber() != null)
     *   {
     *     statement.setString(parameterIndex, user.getMobileNumber());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getDescription() != null)
     *   {
     *     statement.setString(parameterIndex, user.getDescription());
     *     parameterIndex++;
     *   }
     *
     *   if (user.getPassword() != null)
     *   {
     *     if (user.getPassword().length() > 0)
     *     {
     *       statement.setString(parameterIndex, createPasswordHash(user.getPassword()));
     *     }
     *     else
     *     {
     *       statement.setString(parameterIndex, "");
     *     }
     *
     *     parameterIndex++;
     *   }
     *
     *   if (lockUser || (user.getPasswordAttempts() != null))
     *   {
     *     if (lockUser)
     *     {
     *       statement.setInt(parameterIndex, -1);
     *     }
     *     else
     *     {
     *       statement.setInt(parameterIndex, user.getPasswordAttempts());
     *     }
     *
     *     parameterIndex++;
     *   }
     *
     *   if (expirePassword || (user.getPasswordExpiry() != null))
     *   {
     *     if (expirePassword)
     *     {
     *       statement.setTimestamp(parameterIndex, new Timestamp(System.currentTimeMillis()));
     *     }
     *     else
     *     {
     *       statement.setTimestamp(parameterIndex,
     *           new Timestamp(user.getPasswordExpiry().getTime()));
     *     }
     *
     *     parameterIndex++;
     *   }
     *
     *   statement.setLong(parameterIndex, getUserDirectoryId());
     *
     *   parameterIndex++;
     *
     *   statement.setLong(parameterIndex, internalUserId);
     *
     *   if (statement.executeUpdate() != 1)
     *   {
     *     throw new SecurityException(
     *         "No rows were affected as a result of executing the SQL statement (" + updateUserSQL
     *         + ")");
     *   }
     * }
     * }
     * catch (UserNotFoundException e)
     * {
     * throw e;
     * }
     * catch (Throwable e)
     * {
     * throw new SecurityException("Failed to update the user (" + user.getUsername()
     *     + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
     * }
     */
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

    // getFunctionCodesForGroupsSQL
    getFunctionCodesForGroupsSQL = "SELECT DISTINCT F.CODE FROM " + schemaPrefix + "FUNCTIONS F"
        + " INNER JOIN " + schemaPrefix
        + "FUNCTION_TO_ROLE_MAP FTRM ON FTRM.FUNCTION_ID = F.ID INNER JOIN " + schemaPrefix
        + "ROLE_TO_GROUP_MAP RTGM ON RTGM.ROLE_ID = FTRM.ROLE_ID INNER JOIN " + schemaPrefix
        + "GROUPS G ON G.ID = RTGM.GROUP_ID ";

    /*
     *
     * // addInternalUserToInternalGroupSQL
     * addInternalUserToInternalGroupSQL = "INSERT INTO " + schemaPrefix
     *   + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP"
     *   + " (USER_DIRECTORY_ID, INTERNAL_USER_ID, INTERNAL_GROUP_ID) VALUES (?, ?, ?)";
     *
     * // changeInternalUserPasswordSQL
     * changeInternalUserPasswordSQL = "UPDATE " + schemaPrefix + "INTERNAL_USERS IU"
     *   + " SET IU.PASSWORD=?, IU.PASSWORD_ATTEMPTS=?, IU.PASSWORD_EXPIRY=?"
     *   + " WHERE IU.USER_DIRECTORY_ID=? AND IU.ID=?";
     *
     * // createInternalGroupSQL
     * createInternalGroupSQL = "INSERT INTO " + schemaPrefix + "INTERNAL_GROUPS"
     *   + " (ID, USER_DIRECTORY_ID, GROUPNAME, DESCRIPTION) VALUES (?, ?, ?, ?)";
     *
     * // createInternalUserSQL
     * createInternalUserSQL = "INSERT INTO " + schemaPrefix + "INTERNAL_USERS"
     *   + " (ID, USER_DIRECTORY_ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES, LAST_NAME, PHONE,"
     *   + " FAX, MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY,"
     *   + " DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
     *
     * // deleteInternalGroupSQL
     * deleteInternalGroupSQL = "DELETE FROM " + schemaPrefix + "INTERNAL_GROUPS IG"
     *   + " WHERE IG.USER_DIRECTORY_ID=? AND IG.ID=?";
     *
     * // deleteInternalUserSQL
     * deleteInternalUserSQL = "DELETE FROM " + schemaPrefix + "INTERNAL_USERS IU"
     *   + " WHERE IU.USER_DIRECTORY_ID=? AND IU.ID=?";
     *
     * // getFilteredInternalUsersSQL
     * getFilteredInternalUsersSQL =
     * "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.TITLE, IU.FIRST_NAMES,"
     * + " IU.LAST_NAME, IU.PHONE, IU.FAX, IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS,"
     * + " IU.PASSWORD_EXPIRY, IU.DESCRIPTION FROM " + schemaPrefix
     * + "INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=? AND"
     * + " ((UPPER(IU.USERNAME) LIKE ?) OR (UPPER(IU.FIRST_NAMES) LIKE ?)"
     * + " OR (UPPER(IU.LAST_NAME) LIKE ?)) ORDER BY IU.USERNAME";
     *
     *
     *
     * // getInternalGroupIdSQL
     * getInternalGroupIdSQL = "SELECT IG.ID FROM " + schemaPrefix + "INTERNAL_GROUPS IG"
     *   + " WHERE IG.USER_DIRECTORY_ID=? AND UPPER(IG.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";
     *
     * // getInternalGroupNamesForInternalUserSQL
     * getInternalGroupNamesForInternalUserSQL = "SELECT IG.GROUPNAME FROM " + schemaPrefix
     *   + "INTERNAL_GROUPS IG, " + schemaPrefix + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM"
     *   + " WHERE IG.ID = IUTGM.INTERNAL_GROUP_ID AND IUTGM.USER_DIRECTORY_ID=?"
     *   + " AND IUTGM.INTERNAL_USER_ID=? ORDER BY IG.GROUPNAME";
     *
     * // getInternalGroupSQL
     * getInternalGroupSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM " + schemaPrefix
     *   + "INTERNAL_GROUPS IG WHERE IG.USER_DIRECTORY_ID=? AND"
     *   + " UPPER(IG.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";
     *
     * // getInternalGroupsForInternalUserSQL
     * getInternalGroupsForInternalUserSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM "
     *   + schemaPrefix + "INTERNAL_GROUPS IG, " + schemaPrefix
     *   + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM"
     *   + " WHERE IG.ID = IUTGM.INTERNAL_GROUP_ID AND IUTGM.USER_DIRECTORY_ID=?"
     *   + " AND IUTGM.INTERNAL_USER_ID=? ORDER BY IG.GROUPNAME";
     *
     * // getInternalGroupsSQL
     * getInternalGroupsSQL = "SELECT IG.ID, IG.GROUPNAME, IG.DESCRIPTION FROM " + schemaPrefix
     *   + "INTERNAL_GROUPS IG WHERE IG.USER_DIRECTORY_ID=? ORDER BY IG.GROUPNAME";
     *
     * // getNumberOfFilteredInternalUsersSQL
     * getNumberOfFilteredInternalUsersSQL = "SELECT COUNT(IU.ID) FROM " + schemaPrefix
     *   + "INTERNAL_USERS IU WHERE IU.USER_DIRECTORY_ID=? AND"
     *   + " ((UPPER(IU.USERNAME) LIKE ?) OR (UPPER(IU.FIRST_NAMES) LIKE ?)"
     *   + " OR (UPPER(IU.LAST_NAME) LIKE ?))";
     *
     * // getNumberOfInternalGroupsSQL
     * getNumberOfInternalGroupsSQL = "SELECT COUNT(IG.ID) FROM " + schemaPrefix
     *   + "INTERNAL_GROUPS IG" + " WHERE IG.USER_DIRECTORY_ID=?";
     *
     * // getNumberOfUsersForGroupSQL
     * getNumberOfUsersForGroupSQL = "SELECT COUNT (IUTGM.INTERNAL_USER_ID) FROM " + schemaPrefix
     *   + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM WHERE IUTGM.USER_DIRECTORY_ID=?"
     *   + " AND IUTGM.INTERNAL_GROUP_ID=?";
     *
     * // getNumberOfInternalUsersSQL
     * getNumberOfInternalUsersSQL = "SELECT COUNT(IU.ID) FROM " + schemaPrefix + "INTERNAL_USERS IU"
     *   + " WHERE IU.USER_DIRECTORY_ID=?";
     *
     * // getInternalUserIdSQL
     * getInternalUserIdSQL = "SELECT IU.ID FROM " + schemaPrefix + "INTERNAL_USERS IU"
     *   + " WHERE IU.USER_DIRECTORY_ID=? AND UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";
     *
     * // getInternalUserSQL
     * getInternalUserSQL = "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.TITLE, IU.FIRST_NAMES,"
     *   + " IU.LAST_NAME, IU.PHONE, IU.FAX,  IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS,"
     *   + " IU.PASSWORD_EXPIRY, IU.DESCRIPTION FROM " + schemaPrefix + "INTERNAL_USERS IU"
     *   + " WHERE IU.USER_DIRECTORY_ID=? AND UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";
     *
     * // getInternalUsersSQL
     * getInternalUsersSQL = "SELECT IU.ID, IU.USERNAME, IU.PASSWORD, IU.TITLE, IU.FIRST_NAMES,"
     *   + " IU.LAST_NAME, IU.PHONE, IU.FAX,  IU.MOBILE, IU.EMAIL, IU.PASSWORD_ATTEMPTS,"
     *   + " IU.PASSWORD_EXPIRY, IU.DESCRIPTION FROM " + schemaPrefix + "INTERNAL_USERS IU"
     *   + " WHERE IU.USER_DIRECTORY_ID=? ORDER BY IU.USERNAME";
     *
     * // isPasswordInInternalUserPasswordHistorySQL
     * isPasswordInInternalUserPasswordHistorySQL = "SELECT IUPH.ID FROM " + schemaPrefix
     *   + "INTERNAL_USERS_PASSWORD_HISTORY IUPH"
     *   + " WHERE IUPH.INTERNAL_USER_ID=? AND IUPH.CHANGED > ? AND IUPH.PASSWORD=?";
     *
     * // isInternalUserInInternalGroupSQL
     * isInternalUserInInternalGroupSQL = "SELECT IUTGM.INTERNAL_USER_ID FROM " + schemaPrefix
     *   + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM WHERE IUTGM.USER_DIRECTORY_ID=? AND"
     *   + " IUTGM.INTERNAL_USER_ID=? AND IUTGM.INTERNAL_GROUP_ID=?";
     *
     * // removeInternalUserFromInternalGroupSQL
     * removeInternalUserFromInternalGroupSQL = "DELETE FROM " + schemaPrefix
     *   + "INTERNAL_USER_TO_INTERNAL_GROUP_MAP IUTGM"
     *   + " WHERE IUTGM.USER_DIRECTORY_ID=? AND IUTGM.INTERNAL_USER_ID=?"
     *   + " AND IUTGM.INTERNAL_GROUP_ID=?";
     *
     * // saveInternalUserPasswordHistorySQL
     * saveInternalUserPasswordHistorySQL = "INSERT INTO " + schemaPrefix
     *   + "INTERNAL_USERS_PASSWORD_HISTORY"
     *   + " (ID, INTERNAL_USER_ID, CHANGED, PASSWORD) VALUES (?, ?, ?, ?)";
     *
     * // updateInternalGroupSQL
     * updateInternalGroupSQL = "UPDATE " + schemaPrefix + "INTERNAL_GROUPS IG"
     *   + " SET IG.DESCRIPTION=? WHERE IG.USER_DIRECTORY_ID=? AND IG.ID=?";
     */
  }

  private Group buildGroupFromSearchResult(SearchResult searchResult)
    throws NamingException
  {
    Attributes attributes = searchResult.getAttributes();

    Group group = new Group(String.valueOf(attributes.get(groupNameAttribute).get()));

    group.setId(-1);
    group.setUserDirectoryId(getUserDirectoryId());

    if ((!StringUtil.isNullOrEmpty(groupDescriptionAttribute))
        && (attributes.get(groupDescriptionAttribute) != null))
    {
      group.setDescription(String.valueOf(attributes.get(groupDescriptionAttribute).get()));
    }
    else
    {
      group.setDescription("");
    }

    return group;
  }

  private User buildUserFromSearchResult(SearchResult searchResult, boolean isReadOnly)
    throws NamingException
  {
    Attributes attributes = searchResult.getAttributes();

    User user = new User(String.valueOf(attributes.get(userUsernameAttribute).get()));

    user.setId(-1);
    user.setUserDirectoryId(getUserDirectoryId());
    user.setReadOnly(isReadOnly);
    user.setPassword("");

    if ((!StringUtil.isNullOrEmpty(userTitleAttribute))
        && (attributes.get(userTitleAttribute) != null))
    {
      user.setTitle(String.valueOf(attributes.get(userTitleAttribute).get()));
    }
    else
    {
      user.setTitle("");
    }

    if ((!StringUtil.isNullOrEmpty(userFirstNamesAttribute))
        && (attributes.get(userFirstNamesAttribute) != null))
    {
      user.setFirstNames(String.valueOf(attributes.get(userFirstNamesAttribute).get()));
    }
    else
    {
      user.setFirstNames("");
    }

    if ((!StringUtil.isNullOrEmpty(userLastNameAttribute))
        && (attributes.get(userLastNameAttribute) != null))
    {
      user.setLastName(String.valueOf(attributes.get(userLastNameAttribute).get()));
    }
    else
    {
      user.setLastName("");
    }

    if ((!StringUtil.isNullOrEmpty(userPhoneNumberAttribute))
        && (attributes.get(userPhoneNumberAttribute) != null))
    {
      user.setPhoneNumber(String.valueOf(attributes.get(userPhoneNumberAttribute).get()));
    }
    else
    {
      user.setPhoneNumber("");
    }

    if ((!StringUtil.isNullOrEmpty(userFaxNumberAttribute))
        && (attributes.get(userFaxNumberAttribute) != null))
    {
      user.setFaxNumber(String.valueOf(attributes.get(userFaxNumberAttribute).get()));
    }
    else
    {
      user.setFaxNumber("");
    }

    if ((!StringUtil.isNullOrEmpty(userMobileNumberAttribute))
        && (attributes.get(userMobileNumberAttribute) != null))
    {
      user.setMobileNumber(String.valueOf(attributes.get(userMobileNumberAttribute).get()));
    }
    else
    {
      user.setMobileNumber("");
    }

    if ((!StringUtil.isNullOrEmpty(userEmailAttribute))
        && (attributes.get(userEmailAttribute) != null))
    {
      user.setEmail(String.valueOf(attributes.get(userEmailAttribute).get()));
    }
    else
    {
      user.setEmail("");
    }

    if ((!StringUtil.isNullOrEmpty(userPasswordAttemptsAttribute))
        && (attributes.get(userPasswordAttemptsAttribute) != null))
    {
      user.setPasswordAttempts(
          Integer.parseInt(String.valueOf(attributes.get(userPasswordAttemptsAttribute).get())));
    }
    else
    {
      user.setPasswordAttempts(-1);
    }

    if ((!StringUtil.isNullOrEmpty(userPasswordExpiryAttribute))
        && (attributes.get(userPasswordExpiryAttribute) != null))
    {
      if (String.valueOf(attributes.get(userPasswordExpiryAttribute).get()).equals("-1"))
      {
        user.setPasswordExpiry(new Date(System.currentTimeMillis()
            + (1000L * 60L * 60L * 24L * 365L * 20L)));
      }
      else
      {
        user.setPasswordExpiry(
            new Date(
              Long.parseLong(String.valueOf(attributes.get(userPasswordExpiryAttribute).get()))));
      }
    }

    if ((!StringUtil.isNullOrEmpty(userDescriptionAttribute))
        && (attributes.get(userDescriptionAttribute) != null))
    {
      user.setDescription(String.valueOf(attributes.get(userDescriptionAttribute).get()));
    }
    else
    {
      user.setDescription("");
    }

    user.setProperty("dn",
        new LdapName(searchResult.getNameInNamespace().toLowerCase()).toString());

    return user;
  }

  private DirContext getDirContext(String userDN, String password)
    throws SecurityException
  {
    try
    {
      String url = useSSL
          ? "ldaps://"
          : "ldap://";
      url += host;
      url += ":";
      url += port;

      String connectionType = "simple";

      Hashtable<String, String> environment = new Hashtable<>();

      environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      environment.put(Context.PROVIDER_URL, url);
      environment.put(Context.SECURITY_AUTHENTICATION, connectionType);
      environment.put(Context.SECURITY_PRINCIPAL, userDN);
      environment.put(Context.SECURITY_CREDENTIALS, password);

      return new InitialDirContext(environment);
    }
    catch (Throwable e)
    {
      throw new SecurityException(
          "Failed to retrieve the JNDI directory context for the user directory ("
          + getUserDirectoryId() + ")", e);
    }
  }

  private LdapName getGroupDN(DirContext dirContext, String groupName)
    throws SecurityException
  {
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      List<LdapName> groupDNs = new ArrayList<>();

      String searchFilter = "(&(objectClass=" + groupObjectClass + ")(" + groupNameAttribute + "="
        + groupName + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);

      if (!StringUtil.isNullOrEmpty(groupBaseDN))
      {
        searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

        while (searchResults.hasMore())
        {
          groupDNs.add(new LdapName(searchResults.next().getNameInNamespace().toLowerCase()));
        }
      }

      if (groupDNs.size() == 0)
      {
        return null;
      }
      else if (groupDNs.size() == 1)
      {
        return groupDNs.get(0);
      }
      else
      {
        StringBuilder buffer = new StringBuilder();

        for (LdapName groupDN : groupDNs)
        {
          if (buffer.length() > 0)
          {
            buffer.append(" ");
          }

          buffer.append("(").append(groupDN).append(")");
        }

        throw new SecurityException("Found multiple groups (" + groupDNs.size()
            + ") with the group name (" + groupName + ") with DNs " + buffer.toString());
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the DN for the group (" + groupName
          + ") from the LDAP directory (" + host + ":" + port + ")", e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
    }
  }

  private User getUser(DirContext dirContext, String username)
    throws SecurityException
  {
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      List<User> users = new ArrayList<>();

      String searchFilter = "(&(objectClass=" + userObjectClass + ")(" + userUsernameAttribute
        + "=" + username + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      // First search for a non-shared user
      if (!StringUtil.isNullOrEmpty(userBaseDN))
      {
        searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

        while (searchResultsNonSharedUsers.hasMore())
        {
          users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
        }
      }

      // Next search for a shared user
      if (!StringUtil.isNullOrEmpty(sharedBaseDN))
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore())
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      if (users.size() == 0)
      {
        return null;
      }
      else if (users.size() == 1)
      {
        return users.get(0);
      }
      else
      {
        StringBuilder buffer = new StringBuilder();

        for (User user : users)
        {
          if (buffer.length() > 0)
          {
            buffer.append(" ");
          }

          buffer.append("(").append(user.getProperty("dn")).append(")");
        }

        throw new SecurityException("Found multiple users (" + users.size()
            + ") with the username (" + username + ") with DNs " + buffer.toString());
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the details for the user (" + username
          + ") from the LDAP directory (" + host + ":" + port + ")", e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
    }
  }

  private LdapName getUserDN(DirContext dirContext, String username)
    throws SecurityException
  {
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      List<LdapName> userDNs = new ArrayList<>();

      String searchFilter = "(&(objectClass=" + userObjectClass + ")(" + userUsernameAttribute
        + "=" + username + "))";

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);

      // First search for a non-shared user
      if (!StringUtil.isNullOrEmpty(userBaseDN))
      {
        searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

        while (searchResultsNonSharedUsers.hasMore())
        {
          userDNs.add(
              new LdapName(searchResultsNonSharedUsers.next().getNameInNamespace().toLowerCase()));
        }
      }

      // Next search for a shared user
      if (!StringUtil.isNullOrEmpty(sharedBaseDN))
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore())
        {
          userDNs.add(
              new LdapName(searchResultsSharedUsers.next().getNameInNamespace().toLowerCase()));
        }
      }

      if (userDNs.size() == 0)
      {
        return null;
      }
      else if (userDNs.size() == 1)
      {
        return userDNs.get(0);
      }
      else
      {
        StringBuilder buffer = new StringBuilder();

        for (LdapName userDN : userDNs)
        {
          if (buffer.length() > 0)
          {
            buffer.append(" ");
          }

          buffer.append("(").append(userDN).append(")");
        }

        throw new SecurityException("Found multiple users (" + userDNs.size()
            + ") with the username (" + username + ") with DNs " + buffer.toString());
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the DN for the user (" + username
          + ") from the LDAP directory (" + host + ":" + port + ")", e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
    }
  }

  private void incrementPasswordAttempts(DirContext dirContext, User user)
  {
    try
    {
      if ((!StringUtil.isNullOrEmpty(userPasswordAttemptsAttribute))
          && (user.getPasswordAttempts() != null) && (user.getPasswordAttempts() != -1))
      {
        ModificationItem[] modificationItems = new ModificationItem[1];

        modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
            new BasicAttribute(userPasswordAttemptsAttribute,
              String.valueOf(user.getPasswordAttempts() + 1)));

        dirContext.modifyAttributes(user.getProperty("dn"), modificationItems);
      }
    }
    catch (Throwable e)
    {
      logger.error("Failed to increment the password attempts for the user (" + user.getUsername()
          + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

///**
// * Is the password, given by the specified password hash, a historical password that cannot
// * be reused for a period of time i.e. was the password used previously in the last X months.
// * Where X is a configuration value retrieved from the registry.
// *
// * @param connection     the existing database connection
// * @param internalUserId the numeric ID uniquely identifying the internal user
// * @param passwordHash   the password hash
// *
// * @return <code>true</code> if the password was previously used and cannot be reused for a
// *         period of time or <code>false</code> otherwise
// *
// * @throws SQLException
// */
//private boolean isPasswordInHistory(Connection connection, long internalUserId,
//    String passwordHash)
//  throws SQLException
//{
//  try (PreparedStatement statement =
//      connection.prepareStatement(isPasswordInInternalUserPasswordHistorySQL))
//  {
//    Calendar calendar = Calendar.getInstance();
//
//    calendar.setTime(new Date());
//    calendar.add(Calendar.MONTH, -1 * passwordHistoryMonths);
//
//    statement.setLong(1, internalUserId);
//    statement.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
//    statement.setString(3, passwordHash);
//
//    try (ResultSet rs = statement.executeQuery())
//    {
//      return rs.next();
//    }
//  }
//}

//private void savePasswordHistory(Connection connection, long internalUserId, String passwordHash)
//  throws SQLException
//{
//  try (PreparedStatement statement =
//      connection.prepareStatement(saveInternalUserPasswordHistorySQL))
//  {
//    long id = nextId("Application.InternalUserPasswordHistoryId");
//
//    statement.setLong(1, id);
//    statement.setLong(2, internalUserId);
//    statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
//    statement.setString(4, passwordHash);
//    statement.execute();
//  }
//}
}
