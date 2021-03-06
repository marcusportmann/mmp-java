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

import guru.mmp.common.util.JNDIUtil;
import guru.mmp.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

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

  /**
   * The data source used to provide connections to the application database.
   */
  @Inject
  @Qualifier("applicationDataSource")
  private DataSource dataSource;
  @SuppressWarnings("unused")
  private LdapName baseDN;
  private String bindDN;
  private String bindPassword;
  private LdapName groupBaseDN;
  private String groupDescriptionAttribute;
  private String groupMemberAttribute;
  private String[] groupMemberAttributeArray;
  private String groupNameAttribute;
  private String groupObjectClass;
  private String host;
  private int maxFilteredGroups;
  private int maxFilteredUsers;
  private int maxPasswordAttempts;
  private int passwordExpiryMonths;
  private int passwordHistoryMaxLength;
  private int passwordHistoryMonths;
  private int port;
  private LdapName sharedBaseDN;
  private boolean supportPasswordHistory;
  private boolean useSSL;
  private LdapName userBaseDN;
  private String userEmailAttribute;
  private String userFirstNameAttribute;
  private String userLastNameAttribute;
  private String userPhoneNumberAttribute;
  private String userMobileNumberAttribute;
  private String userObjectClass;
  private String userPasswordAttemptsAttribute;
  private String userPasswordExpiryAttribute;
  private String userPasswordHistoryAttribute;
  private String[] userPasswordHistoryAttributeArray;
  private String userUsernameAttribute;

  /**
   * Constructs a new <code>LDAPUserDirectory</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param parameters      the key-value configuration parameters for the user directory
   */
  public LDAPUserDirectory(UUID userDirectoryId, Map<String, String> parameters)
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
        throw new SecurityException(String.format(
            "No Host configuration parameter found for the user directory (%s)", userDirectoryId));
      }

      if (parameters.containsKey("Port"))
      {
        port = Integer.parseInt(parameters.get("Port"));
      }
      else
      {
        throw new SecurityException(String.format(
            "No Port configuration parameter found for the user directory (%s)", userDirectoryId));
      }

      useSSL = parameters.containsKey("UseSSL") && Boolean.parseBoolean(parameters.get("UseSSL"));

      if (parameters.containsKey("BindDN"))
      {
        bindDN = parameters.get("BindDN");
      }
      else
      {
        throw new SecurityException(String.format(
            "No BindDN configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("BindPassword"))
      {
        bindPassword = parameters.get("BindPassword");
      }
      else
      {
        throw new SecurityException(String.format(
            "No BindPassword configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("BaseDN"))
      {
        baseDN = new LdapName(parameters.get("BaseDN"));
      }
      else
      {
        throw new SecurityException(String.format(
            "No BindDN configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("UserBaseDN"))
      {
        userBaseDN = new LdapName(parameters.get("UserBaseDN"));
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserBaseDN configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("GroupBaseDN"))
      {
        groupBaseDN = new LdapName(parameters.get("GroupBaseDN"));
      }
      else
      {
        throw new SecurityException(String.format(
            "No GroupBaseDN configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if ((parameters.containsKey("SharedBaseDN"))
          && (!StringUtil.isNullOrEmpty(parameters.get("SharedBaseDN"))))
      {
        sharedBaseDN = new LdapName(parameters.get("SharedBaseDN"));
      }

      if (parameters.containsKey("UserObjectClass"))
      {
        userObjectClass = parameters.get("UserObjectClass");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserObjectClass configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("UserUsernameAttribute"))
      {
        userUsernameAttribute = parameters.get("UserUsernameAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserUsernameAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("UserPasswordExpiryAttribute"))
      {
        userPasswordExpiryAttribute = parameters.get("UserPasswordExpiryAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserPasswordExpiryAttribute configuration parameter found for the user directory "
            + "(%s)", userDirectoryId));
      }

      if (parameters.containsKey("UserPasswordAttemptsAttribute"))
      {
        userPasswordAttemptsAttribute = parameters.get("UserPasswordAttemptsAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserPasswordAttemptsAttribute configuration parameter found for the user directory "
            + "(%s)", userDirectoryId));
      }

      if (parameters.containsKey("UserPasswordHistoryAttribute"))
      {
        userPasswordHistoryAttribute = parameters.get("UserPasswordHistoryAttribute");
        userPasswordHistoryAttributeArray = new String[] { userPasswordHistoryAttribute };
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserPasswordHistoryAttribute configuration parameter found for the user directory "
            + "(%s)", userDirectoryId));
      }

      if (parameters.containsKey("UserFirstNameAttribute"))
      {
        userFirstNameAttribute = parameters.get("UserFirstNameAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserFirstNameAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("UserLastNameAttribute"))
      {
        userLastNameAttribute = parameters.get("UserLastNameAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserLastNameAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("UserPhoneNumberAttribute"))
      {
        userPhoneNumberAttribute = parameters.get("UserPhoneNumberAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserPhoneNumberAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("UserMobileNumberAttribute"))
      {
        userMobileNumberAttribute = parameters.get("UserMobileNumberAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserMobileNumberAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("UserEmailAttribute"))
      {
        userEmailAttribute = parameters.get("UserEmailAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No UserEmailAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("GroupObjectClass"))
      {
        groupObjectClass = parameters.get("GroupObjectClass");
      }
      else
      {
        throw new SecurityException(String.format(
            "No GroupObjectClass configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("GroupNameAttribute"))
      {
        groupNameAttribute = parameters.get("GroupNameAttribute");
      }
      else
      {
        throw new SecurityException(String.format(
            "No GroupNameAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (parameters.containsKey("GroupMemberAttribute"))
      {
        groupMemberAttribute = parameters.get("GroupMemberAttribute");

        groupMemberAttributeArray = new String[] { groupMemberAttribute };
      }
      else
      {
        throw new SecurityException(String.format(
            "No GroupMemberAttribute configuration parameter found for the user directory (%s)",
            userDirectoryId));
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", groupName));
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      BasicAttribute attribute = new BasicAttribute(groupMemberAttribute);

      if (attributes.get(groupMemberAttribute) != null)
      {
        @SuppressWarnings("unchecked")
        NamingEnumeration<String> groupMembers = (NamingEnumeration<String>) attributes.get(
            groupMemberAttribute).getAll();

        while (groupMembers.hasMore())
        {
          LdapName groupMemberDN = new LdapName(groupMembers.next());

          if (groupMemberDN.equals(userDN))
          {
            return;
          }
          else
          {
            attribute.add(groupMemberDN.toString());
          }
        }
      }

      attribute.add(userDN.toString());

      dirContext.modifyAttributes(groupDN, new ModificationItem[] { new ModificationItem(DirContext
          .REPLACE_ATTRIBUTE, attribute) });
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      List<ModificationItem> modificationItems = new ArrayList<>();

      BasicAttribute passwordAttribute = new BasicAttribute("userPassword");
      passwordAttribute.add(newPassword);

      modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, passwordAttribute));

      if (!StringUtil.isNullOrEmpty(userPasswordExpiryAttribute))
      {
        if (expirePassword)
        {
          BasicAttribute passwordExpiryAttribute = new BasicAttribute(userPasswordExpiryAttribute);
          passwordExpiryAttribute.add("0");

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordExpiryAttribute));
        }
        else
        {
          Calendar calendar = Calendar.getInstance();
          calendar.add(Calendar.MONTH, passwordExpiryMonths);

          BasicAttribute passwordExpiryAttribute = new BasicAttribute(userPasswordExpiryAttribute);
          passwordExpiryAttribute.add(String.valueOf(calendar.getTimeInMillis()));

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordExpiryAttribute));
        }
      }

      if (!StringUtil.isNullOrEmpty(userPasswordAttemptsAttribute))
      {
        if (lockUser)
        {
          BasicAttribute passwordAttemptsAttribute = new BasicAttribute(
              userPasswordAttemptsAttribute);
          passwordAttemptsAttribute.add(String.valueOf(maxPasswordAttempts));

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordAttemptsAttribute));
        }
        else
        {
          BasicAttribute passwordAttemptsAttribute = new BasicAttribute(
              userPasswordAttemptsAttribute);
          passwordAttemptsAttribute.add("0");

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordAttemptsAttribute));
        }
      }

      if (!StringUtil.isNullOrEmpty(userPasswordHistoryAttribute))
      {
        if (resetPasswordHistory)
        {
          BasicAttribute passwordHistoryAttribute = new BasicAttribute(
              userPasswordHistoryAttribute);

          modificationItems.add(new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
              passwordHistoryAttribute));
        }
      }

      dirContext.modifyAttributes(userDN, modificationItems.toArray(
          new ModificationItem[modificationItems.size()]));
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      User user = getUser(dirContext, username);

      if (user == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      LdapName userDN = new LdapName(user.getProperty("dn"));

      if (!userDN.startsWith(sharedBaseDN))
      {
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

          throw new AuthenticationFailedException(String.format(
              "Failed to authenticate the user (%s) for the user directory (%s)", username,
              getUserDirectoryId()));
        }
        else
        {
          logger.error(String.format(
              "Failed to authenticate the user (%s) for the user directory (%s)", username,
              getUserDirectoryId()), e);

          throw new AuthenticationFailedException(String.format(
              "Failed to authenticate the user (%s) for the user directory (%s)", username,
              getUserDirectoryId()), e);
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
      throw new SecurityException(String.format(
          "Failed to authenticate the user (%s) for the user directory (%s): %s", username,
          getUserDirectoryId(), e.getMessage()), e);
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
   */
  public void changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
        ExistingPasswordException, SecurityException
  {
    throw new SecurityException("TODO: NOT IMPLEMENTED");

/*

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
        throw new AuthenticationFailedException("Authentication failed while attempting to change "
            + "the password for the user (" + username + ")");
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
        | ExpiredPasswordException | UserLockedException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to change the password for the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }


 */

  }

  /**
   * Create a new security group.
   *
   * @param group the security group
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
        throw new DuplicateGroupException(String.format("The security group (%s) already exists",
            group.getGroupName()));
      }

      Attributes attributes = new BasicAttributes();

      attributes.put(new BasicAttribute("objectclass", "top"));
      attributes.put(new BasicAttribute("objectclass", groupObjectClass));

      attributes.put(new BasicAttribute(groupNameAttribute, group.getGroupName()));

      if (!StringUtil.isNullOrEmpty(groupDescriptionAttribute))
      {
        attributes.put(new BasicAttribute(groupDescriptionAttribute, StringUtil.notNull(
            group.getDescription())));
      }

      dirContext.bind(groupNameAttribute + "=" + group.getGroupName() + ","
          + groupBaseDN.toString(), dirContext, attributes);
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
   */
  public void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityException
  {
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, user.getUsername());

      if (userDN != null)
      {
        throw new DuplicateUserException("The user (" + user.getUsername() + ") already exists");
      }

      Attributes attributes = new BasicAttributes();

      attributes.put(new BasicAttribute("objectclass", "top"));
      attributes.put(new BasicAttribute("objectclass", userObjectClass));

      attributes.put(new BasicAttribute(userUsernameAttribute, user.getUsername()));

      if (!StringUtil.isNullOrEmpty(userFirstNameAttribute))
      {
        attributes.put(new BasicAttribute(userFirstNameAttribute, StringUtil.notNull(
            user.getFirstName())));
      }

      if (!StringUtil.isNullOrEmpty(userLastNameAttribute))
      {
        attributes.put(new BasicAttribute(userLastNameAttribute, StringUtil.notNull(
            user.getLastName())));
      }

      if (!StringUtil.isNullOrEmpty(userEmailAttribute))
      {
        attributes.put(new BasicAttribute(userEmailAttribute, StringUtil.notNull(user.getEmail())));
      }

      if (!StringUtil.isNullOrEmpty(userPhoneNumberAttribute))
      {
        attributes.put(new BasicAttribute(userPhoneNumberAttribute, StringUtil.notNull(
            user.getPhoneNumber())));
      }

      if (!StringUtil.isNullOrEmpty(userMobileNumberAttribute))
      {
        attributes.put(new BasicAttribute(userMobileNumberAttribute, StringUtil.notNull(
            user.getMobileNumber())));
      }

      String passwordHash;

      if (!isNullOrEmpty(user.getPassword()))
      {
        passwordHash = createPasswordHash(user.getPassword());
      }
      else
      {
        passwordHash = createPasswordHash("");
      }

      if (!StringUtil.isNullOrEmpty(userPasswordHistoryAttribute))
      {
        attributes.put(new BasicAttribute(userPasswordHistoryAttribute, passwordHash));
      }

      attributes.put(new BasicAttribute("userPassword", StringUtil.notNull(user.getPassword())));

      if (!StringUtil.isNullOrEmpty(userPasswordAttemptsAttribute))
      {
        if (userLocked)
        {
          attributes.put(new BasicAttribute(userPasswordAttemptsAttribute, String.valueOf(
              maxPasswordAttempts)));
        }
        else
        {
          attributes.put(new BasicAttribute(userPasswordAttemptsAttribute, "0"));
        }
      }

      if (!StringUtil.isNullOrEmpty(userPasswordExpiryAttribute))
      {
        if (expiredPassword)
        {
          attributes.put(new BasicAttribute(userPasswordExpiryAttribute, "0"));
        }
        else
        {
          Calendar calendar = Calendar.getInstance();
          calendar.add(Calendar.MONTH, passwordExpiryMonths);

          attributes.put(new BasicAttribute(userPasswordExpiryAttribute, String.valueOf(
              calendar.getTimeInMillis())));
        }
      }

      userDN = new LdapName(userUsernameAttribute + "=" + user.getUsername() + ","
          + userBaseDN.toString());

      dirContext.bind(userDN, dirContext, attributes);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", groupName));
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      if ((attributes.get(groupMemberAttribute) != null)
          && (attributes.get(groupMemberAttribute).size() > 0))
      {
        throw new ExistingGroupMembersException(String.format(
            "The security group (%s) could not be deleted since it is still associated with 1 or more user(s)",
            groupName));
      }

      dirContext.destroySubcontext(groupDN);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      dirContext.destroySubcontext(userDN);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + userObjectClass + ")";

      if (attributes.size() > 0)
      {
        StringBuilder buffer = new StringBuilder();

        buffer.append("(&(objectClass=");
        buffer.append(userObjectClass);
        buffer.append(")");

        for (Attribute attribute : attributes)
        {
          buffer.append("(");
          buffer.append(attribute.getName());
          buffer.append("=*");

          if (attribute.getType().equals(Attribute.ValueType.STRING_VALUE))
          {
            buffer.append(attribute.getStringValue());
          }
          else if (attribute.getType().equals(Attribute.ValueType.DECIMAL_VALUE))
          {
            buffer.append(attribute.getDecimalValue());
          }
          else if (attribute.getType().equals(Attribute.ValueType.DOUBLE_VALUE))
          {
            buffer.append(attribute.getDoubleValue());
          }
          else if (attribute.getType().equals(Attribute.ValueType.LONG_VALUE))
          {
            buffer.append(attribute.getLongValue());
          }
          else
          {
            throw new RuntimeException("Unsupported criteria attribute type ("
                + attribute.getType() + ")");
          }

          buffer.append("*)");
        }

        buffer.append(")");

        searchFilter = buffer.toString();
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

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to find the users for the user directory (%s): %s", getUserDirectoryId(),
          e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", userObjectClass);

      if (!StringUtil.isNullOrEmpty(filter))
      {
        searchFilter = String.format("(&(objectClass=%s)(|(%s=*%s*)(%s=*%s*)(%s=*%s*)))",
            userObjectClass, userUsernameAttribute, filter, userFirstNameAttribute, filter,
            userLastNameAttribute, filter);
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

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the filtered users for the user directory (%s): %s",
          getUserDirectoryId(), e.getMessage()), e);
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
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupMemberAttribute, userDN.toString());

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
          groupNames.add(String.valueOf(searchResult.getAttributes().get(groupNameAttribute)
              .get()));
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
      String getFunctionCodesForGroupsSQL = "SELECT DISTINCT F.CODE FROM SECURITY.FUNCTIONS F "
          + "INNER JOIN SECURITY.FUNCTION_TO_ROLE_MAP FTRM ON FTRM.FUNCTION_ID = F.ID "
          + "INNER JOIN SECURITY.ROLE_TO_GROUP_MAP RTGM ON RTGM.ROLE_ID = FTRM.ROLE_ID "
          + "INNER JOIN SECURITY.GROUPS G ON G.ID = RTGM.GROUP_ID";

      StringBuilder buffer = new StringBuilder(getFunctionCodesForGroupsSQL);
      buffer.append(" WHERE G.USER_DIRECTORY_ID='").append(getUserDirectoryId());
      buffer.append("' AND G.GROUPNAME IN (");

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

      try (Connection connection = dataSource.getConnection();
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
      throw new SecurityException(String.format(
          "Failed to retrieve the function codes for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
    }
  }

  /**
   * Retrieve the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return the security group
   */
  public Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityException
  {
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupNameAttribute, groupName);

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
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", groupName));
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupMemberAttribute, userDN.toString());

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
          groupNames.add(String.valueOf(searchResult.getAttributes().get(groupNameAttribute)
              .get()));
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
      throw new SecurityException(String.format(
          "Failed to retrieve the security group names for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", groupObjectClass);

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
      throw new SecurityException(String.format(
          "Failed to retrieve the security groups for the user directory (%s): %s",
          getUserDirectoryId(), e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupMemberAttribute, userDN.toString());

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
      throw new SecurityException(String.format(
          "Failed to retrieve the security groups for the user (%s) for the user directory (%s): %s",
          username, getUserDirectoryId(), e.getMessage()), e);
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

      String searchFilter = String.format("(objectClass=%s)", userObjectClass);

      if (!StringUtil.isNullOrEmpty(filter))
      {
        searchFilter = String.format("(&(objectClass=%s)(|(%s=*%s*)(%s=*%s*)(%s=*%s*)))",
            userObjectClass, userUsernameAttribute, filter, userFirstNameAttribute, filter,
            userLastNameAttribute, filter);
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

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
        {
          searchResultsSharedUsers.next();

          numberOfUsers++;
        }
      }

      return numberOfUsers;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the number of filtered users for the user directory (%s):%s",
          getUserDirectoryId(), e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", groupObjectClass);

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
      throw new SecurityException(String.format(
          "Failed to retrieve the number of security groups for the user directory (%s):%s",
          getUserDirectoryId(), e.getMessage()), e);
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

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
        {
          searchResultsSharedUsers.next();

          numberOfUsers++;
        }
      }

      return numberOfUsers;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the number of users for the user directory (%s):%s",
          getUserDirectoryId(), e.getMessage()), e);
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
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      return user;
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", userObjectClass);

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

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the users for the user directory (%s): %s", getUserDirectoryId(),
          e.getMessage()), e);
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

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", userObjectClass,
          userUsernameAttribute, username);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      // First search for a non-shared user
      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      if (searchResultsNonSharedUsers.hasMore())
      {
        return true;
      }

      // Next search for a shared user
      if (sharedBaseDN != null)
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
      throw new SecurityException(String.format(
          "Failed to check whether the user (%s) is an existing user for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", groupName));
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
      throw new SecurityException(String.format(
          "Failed to check if the user (%s) is in the security group (%s) for the user directory (%s): %s",
          username, groupName, getUserDirectoryId(), e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", groupName));
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      BasicAttribute attribute = new BasicAttribute(groupMemberAttribute);

      if (attributes.get(groupMemberAttribute) != null)
      {
        @SuppressWarnings("unchecked")
        NamingEnumeration<String> groupMembers = (NamingEnumeration<String>) attributes.get(
            groupMemberAttribute).getAll();

        while (groupMembers.hasMore())
        {
          LdapName groupMemberDN = new LdapName(groupMembers.next());

          if (!groupMemberDN.equals(userDN))
          {
            attribute.add(groupMemberDN.toString());
          }
        }
      }

      if (attribute.size() > 0)
      {
        dirContext.modifyAttributes(groupDN, new ModificationItem[] { new ModificationItem(
            DirContext.REPLACE_ATTRIBUTE, attribute) });
      }
      else
      {
        dirContext.modifyAttributes(groupDN, new ModificationItem[] { new ModificationItem(
            DirContext.REMOVE_ATTRIBUTE, attribute) });
      }
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName groupDN = getGroupDN(dirContext, group.getGroupName());

      if (groupDN == null)
      {
        throw new GroupNotFoundException(String.format(
            "The security group (%s) could not be found", group.getGroupName()));
      }

      List<ModificationItem> modificationItems = new ArrayList<>();

      if (!StringUtil.isNullOrEmpty(groupDescriptionAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            groupDescriptionAttribute, StringUtil.notNull(group.getDescription()))));
      }

      if (modificationItems.size() > 0)
      {
        dirContext.modifyAttributes(groupDN, modificationItems.toArray(
            new ModificationItem[modificationItems.size()]));
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, user.getUsername());

      if (userDN == null)
      {
        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            user.getUsername()));
      }

      List<ModificationItem> modificationItems = new ArrayList<>();

      if (!StringUtil.isNullOrEmpty(userFirstNameAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userFirstNameAttribute, StringUtil.notNull(user.getFirstName()))));
      }

      if (!StringUtil.isNullOrEmpty(userLastNameAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userLastNameAttribute, StringUtil.notNull(user.getLastName()))));
      }

      if (!StringUtil.isNullOrEmpty(userEmailAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userEmailAttribute, StringUtil.notNull(user.getEmail()))));
      }

      if (!StringUtil.isNullOrEmpty(userPhoneNumberAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userPhoneNumberAttribute, StringUtil.notNull(user.getPhoneNumber()))));
      }

      if (!StringUtil.isNullOrEmpty(userMobileNumberAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userMobileNumberAttribute, StringUtil.notNull(user.getMobileNumber()))));
      }

      if ((!StringUtil.isNullOrEmpty(userPasswordAttemptsAttribute))
          && (user.getPasswordAttempts() != null))
      {
        if (lockUser)
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordAttemptsAttribute, String.valueOf(
              maxPasswordAttempts))));
        }
        else
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordAttemptsAttribute, String.valueOf(
              user.getPasswordAttempts()))));
        }
      }

      if ((!StringUtil.isNullOrEmpty(userPasswordExpiryAttribute))
          && (user.getPasswordExpiry() != null))
      {
        if (expirePassword)
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordExpiryAttribute, String.valueOf(
              System.currentTimeMillis()))));
        }
        else
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordExpiryAttribute, String.valueOf(
              user.getPasswordExpiry().getTime()))));
        }
      }

      if (modificationItems.size() > 0)
      {
        dirContext.modifyAttributes(userDN, modificationItems.toArray(
            new ModificationItem[modificationItems.size()]));
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
    finally
    {
      JNDIUtil.close(dirContext);
    }
  }

  private Group buildGroupFromSearchResult(SearchResult searchResult)
    throws NamingException
  {
    Attributes attributes = searchResult.getAttributes();

    Group group = new Group(String.valueOf(attributes.get(groupNameAttribute).get()));

    group.setId(null);
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

    User user = new User();

    user.setId(null);
    user.setUsername(String.valueOf(attributes.get(userUsernameAttribute).get()));
    user.setUserDirectoryId(getUserDirectoryId());
    user.setReadOnly(isReadOnly);
    user.setPassword("");

    if ((!StringUtil.isNullOrEmpty(userFirstNameAttribute))
        && (attributes.get(userFirstNameAttribute) != null))
    {
      user.setFirstName(String.valueOf(attributes.get(userFirstNameAttribute).get()));
    }
    else
    {
      user.setFirstName("");
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
      String userPasswordAttemptsAttributeValue = String.valueOf(attributes.get(
          userPasswordAttemptsAttribute).get());

      if ((!StringUtil.isNullOrEmpty(userPasswordAttemptsAttributeValue))
          && (!userPasswordAttemptsAttributeValue.equals("-1")))
      {
        user.setPasswordAttempts(Integer.parseInt(String.valueOf(attributes.get(
            userPasswordAttemptsAttribute).get())));
      }
    }

    if ((!StringUtil.isNullOrEmpty(userPasswordExpiryAttribute))
        && (attributes.get(userPasswordExpiryAttribute) != null))
    {
      String userPasswordExpiryAttributeValue = String.valueOf(attributes.get(
          userPasswordExpiryAttribute).get());

      if ((!StringUtil.isNullOrEmpty(userPasswordExpiryAttributeValue))
          && (!userPasswordExpiryAttributeValue.equals("-1")))
      {
        user.setPasswordExpiry(new Date(Long.parseLong(String.valueOf(attributes.get(
            userPasswordExpiryAttribute).get()))));
      }
    }

    user.setProperty("dn", new LdapName(searchResult.getNameInNamespace()
        .toLowerCase()).toString());

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
      throw new SecurityException(String.format(
          "Failed to retrieve the JNDI directory context for the user directory (%s)",
          getUserDirectoryId()), e);
    }
  }

  private LdapName getGroupDN(DirContext dirContext, String groupName)
    throws SecurityException
  {
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      List<LdapName> groupDNs = new ArrayList<>();

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupNameAttribute, groupName);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      while (searchResults.hasMore())
      {
        groupDNs.add(new LdapName(searchResults.next().getNameInNamespace().toLowerCase()));
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

        throw new SecurityException(String.format(
            "Found multiple security groups (%d) with the security group name (%s) with DNs %s",
            groupDNs.size(), groupName, buffer.toString()));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the DN for the security group (%s) from the LDAP directory (%s:%d)",
          groupName, host, port), e);
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

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", userObjectClass,
          userUsernameAttribute, username);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      // First search for a non-shared user
      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore())
      {
        users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
      }

      // Next search for a shared user
      if (sharedBaseDN != null)
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

        throw new SecurityException(String.format(
            "Found multiple users (%d) with the username (%s) with DNs %s", users.size(), username,
            buffer.toString()));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the details for the user (%s) from the LDAP directory (%s:%d)",
          username, host, port), e);
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

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", userObjectClass,
          userUsernameAttribute, username);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);

      // First search for a non-shared user
      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore())
      {
        userDNs.add(new LdapName(searchResultsNonSharedUsers.next().getNameInNamespace()
            .toLowerCase()));
      }

      // Next search for a shared user
      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore())
        {
          userDNs.add(new LdapName(searchResultsSharedUsers.next().getNameInNamespace()
              .toLowerCase()));
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

        throw new SecurityException(String.format(
            "Found multiple users (%d) with the username (%s) with DNs %s", userDNs.size(),
            username, buffer.toString()));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the DN for the user (%s) from the LDAP directory (%s:%d)", username,
          host, port), e);
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
          && (user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() != -1))
      {
        dirContext.modifyAttributes(user.getProperty("dn"), new ModificationItem[] {
            new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userPasswordAttemptsAttribute, String.valueOf(user.getPasswordAttempts() + 1))) });
      }
    }
    catch (Throwable e)
    {
      logger.error(String.format(
          "Failed to increment the password attempts for the user (%s) for the user directory (%s)",
          user.getUsername(), getUserDirectoryId()), e);
    }
  }

  private boolean isPasswordInHistory(DirContext dirContext, String username, LdapName userDN,
      String passwordHash)
    throws SecurityException
  {
    try
    {
      if (!StringUtil.isNullOrEmpty(userPasswordHistoryAttribute))
      {
        Attributes attributes = dirContext.getAttributes(userDN, userPasswordHistoryAttributeArray);

        if (attributes.get(userPasswordHistoryAttribute) != null)
        {
          @SuppressWarnings("unchecked")
          NamingEnumeration<String> existingPasswordHashes =
              (NamingEnumeration<String>) attributes.get(userPasswordHistoryAttribute).getAll();

          while (existingPasswordHashes.hasMore())
          {
            if (existingPasswordHashes.next().equals(passwordHash))
            {
              return true;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to check whether the password hash (%s) is in the password history for the user "
          + "(%s) for the user directory (%s): %s", passwordHash, username, getUserDirectoryId(),
          e.getMessage()), e);
    }

    return false;
  }

  private void savePasswordHistory(DirContext dirContext, String username, LdapName userDN,
      String passwordHash)
    throws SecurityException
  {
    try
    {
      if (!StringUtil.isNullOrEmpty(userPasswordHistoryAttribute))
      {
        BasicAttribute attribute = new BasicAttribute(userPasswordHistoryAttribute);

        Attributes attributes = dirContext.getAttributes(userDN, userPasswordHistoryAttributeArray);

        if (attributes.get(userPasswordHistoryAttribute) != null)
        {
          @SuppressWarnings("unchecked")
          NamingEnumeration<String> existingPasswordHashes =
              (NamingEnumeration<String>) attributes.get(userPasswordHistoryAttribute).getAll();

          while (existingPasswordHashes.hasMore())
          {
            attribute.add(existingPasswordHashes.next());
          }
        }

        attribute.add(passwordHash);

        dirContext.modifyAttributes(userDN, new ModificationItem[] { new ModificationItem(DirContext
            .REPLACE_ATTRIBUTE, attribute) });
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to save the password hash (%s) for the user (%s) for the user directory (%s): %s",
          passwordHash, username, getUserDirectoryId(), e.getMessage()), e);
    }
  }
}
