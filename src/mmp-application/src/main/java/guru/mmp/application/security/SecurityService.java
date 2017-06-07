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
import guru.mmp.common.exceptions.InvalidArgumentException;
import guru.mmp.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SecurityService</code> class provides the Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class SecurityService
  implements ISecurityService
{
  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the default user directory.
   */
  public static final UUID DEFAULT_USER_DIRECTORY_ID = UUID.fromString(
      "4ef18395-423a-4df6-b7d7-6bcdd85956e4");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the internal user directory
   * type.
   */
  public static final UUID INTERNAL_USER_DIRECTORY_TYPE_ID = UUID.fromString(
      "b43fda33-d3b0-4f80-a39a-110b8e530f4f");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the LDAP user directory
   * type.
   */
  public static final UUID LDAP_USER_DIRECTORY_TYPE_ID = UUID.fromString(
      "e5741a89-c87b-4406-8a60-2cc0b0a5fa3e");

  /**
   * The maximum number of filtered organisations.
   */
  private static final int MAX_FILTERED_ORGANISATIONS = 100;

  /**
   * The maximum number of filtered user directories.
   */
  private static final int MAX_FILTERED_USER_DIRECTORIES = 100;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
  private Map<UUID, IUserDirectory> userDirectories = new ConcurrentHashMap<>();
  private Map<UUID, UserDirectoryType> userDirectoryTypes = new ConcurrentHashMap<>();

  /**
   * The data source used to provide connections to the application database.
   */
  @Inject
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * The ID Generator.
   */
  @Inject
  private IDGenerator idGenerator;

  /**
   * The Spring application context.
   */
  @Inject
  private ApplicationContext applicationContext;

  /**
   * Constructs a new <code>SecurityService</code>.
   */
  public SecurityService() {}

  /**
   * Add the user to the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  public void addUserToGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.addUserToGroup(username, groupName);
  }

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                             the user directory
   * @param username             the username identifying the user
   * @param newPassword          the new password
   * @param expirePassword       expire the user's password
   * @param lockUser             lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason               the reason for changing the password
   */
  public void adminChangePassword(UUID userDirectoryId, String username, String newPassword,
      boolean expirePassword, boolean lockUser, boolean resetPasswordHistory,
      PasswordChangeReason reason)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (newPassword == null)
    {
      throw new InvalidArgumentException("newPassword");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.adminChangePassword(username, newPassword, expirePassword, lockUser,
        resetPasswordHistory, reason);
  }

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public UUID authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
        UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (password == null)
    {
      throw new InvalidArgumentException("password");
    }

    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        IUserDirectory internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null)
        {
          throw new SecurityException(String.format(
              "The user directory ID (%s) for the internal user (%s) is invalid",
              internalUserDirectoryId, username));
        }
        else
        {
          internalUserDirectory.authenticate(username, password);

          return internalUserDirectoryId;
        }
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if one of them can authenticate this
         * user.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                userDirectory.authenticate(username, password);

                return userDirectoryId;
              }
            }
          }
        }

        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExpiredPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to authenticate the user (%s): %s",
          username, e.getMessage()), e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public UUID changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
        ExistingPasswordException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (password == null)
    {
      throw new InvalidArgumentException("password");
    }

    if (newPassword == null)
    {
      throw new InvalidArgumentException("newPassword");
    }

    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        IUserDirectory internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null)
        {
          throw new SecurityException(String.format(
              "The user directory ID (%s) for the internal user (%s) is invalid",
              internalUserDirectoryId, username));
        }
        else
        {
          internalUserDirectory.changePassword(username, password, newPassword);

          return internalUserDirectoryId;
        }
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if one of them can change the
         * password for this user.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                userDirectory.changePassword(username, password, newPassword);

                return userDirectoryId;
              }
            }
          }
        }

        throw new UserNotFoundException(String.format("The user (%s) could not be found",
            username));
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExistingPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to change the password for the user (%s): %s", username, e.getMessage()), e);
    }
  }

  /**
   * Create a new authorised function.
   *
   * @param function the function
   */
  public void createFunction(Function function)
    throws DuplicateFunctionException, SecurityException

  {
    // Validate parameters
    if (isNullOrEmpty(function.getDescription()))
    {
      throw new InvalidArgumentException("function.id");
    }

    if (isNullOrEmpty(function.getCode()))
    {
      throw new InvalidArgumentException("function.code");
    }

    if (isNullOrEmpty(function.getName()))
    {
      throw new InvalidArgumentException("function.name");
    }

    String createFunctionSQL =
        "INSERT INTO SECURITY.FUNCTIONS (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createFunctionSQL))
    {
      if (getFunctionId(connection, function.getCode()) != null)
      {
        throw new DuplicateFunctionException(String.format(
            "A function with the code (%s) already exists", function.getCode()));
      }

      statement.setObject(1, function.getId());
      statement.setString(2, function.getCode());
      statement.setString(3, function.getName());
      statement.setString(4, function.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createFunctionSQL));
      }
    }
    catch (DuplicateFunctionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to create the function (%s): %s",
          function.getCode(), e.getMessage()), e);
    }
  }

  /**
   * Create a new security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  public void createGroup(UUID userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, DuplicateGroupException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(group.getGroupName()))
    {
      throw new InvalidArgumentException("group.groupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.createGroup(group);
  }

  /**
   * Create a new organisation.
   *
   * @param organisation        the organisation
   * @param createUserDirectory should a new internal user directory be created for the organisation
   *
   * @return the new internal user directory that was created for the organisation or
   *         <code>null</code> if no user directory was created
   */
  @Transactional(rollbackFor = Exception.class)
  public UserDirectory createOrganisation(Organisation organisation, boolean createUserDirectory)
    throws DuplicateOrganisationException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(organisation.getName()))
    {
      throw new InvalidArgumentException("organisation.name");
    }

    try
    {
      UUID organisationId = idGenerator.nextUUID();

      UserDirectory userDirectory = null;

      try (Connection connection = dataSource.getConnection())
      {
        String createOrganisationSQL =
            "INSERT INTO SECURITY.ORGANISATIONS (ID, NAME, STATUS) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(createOrganisationSQL))
        {
          if (organisationWithNameExists(connection, organisation.getName()))
          {
            throw new DuplicateOrganisationException(String.format(
                "An organisation with the name (%s) already exists", organisation.getName()));
          }

          statement.setObject(1, organisationId);
          statement.setString(2, organisation.getName());
          statement.setInt(3, organisation.getStatus().code());

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                createOrganisationSQL));
          }
        }

        String addUserDirectoryToOrganisationSQL =
            "INSERT INTO SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP "
            + "(USER_DIRECTORY_ID, ORGANISATION_ID) VALUES (?, ?)";

        if (createUserDirectory)
        {
          userDirectory = newInternalUserDirectoryForOrganisation(organisation);

          String createUserDirectorySQL = "INSERT INTO SECURITY.USER_DIRECTORIES "
              + " (ID, TYPE_ID, NAME, CONFIGURATION) VALUES (?, ?, ?, ?)";

          try (PreparedStatement statement = connection.prepareStatement(createUserDirectorySQL))
          {
            statement.setObject(1, userDirectory.getId());
            statement.setObject(2, userDirectory.getTypeId());
            statement.setString(3, userDirectory.getName());
            statement.setString(4, userDirectory.getConfiguration());

            if (statement.executeUpdate() != 1)
            {
              throw new SecurityException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  createUserDirectorySQL));
            }
          }

          // Link the new user directory to the new organisation
          try (PreparedStatement statement = connection.prepareStatement(
              addUserDirectoryToOrganisationSQL))
          {
            statement.setObject(1, userDirectory.getId());
            statement.setObject(2, organisationId);

            if (statement.executeUpdate() != 1)
            {
              throw new SecurityException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  addUserDirectoryToOrganisationSQL));
            }
          }
        }

        // Link the new organisation to the default user directory
        try (PreparedStatement statement = connection.prepareStatement(
            addUserDirectoryToOrganisationSQL))
        {
          statement.setObject(1, DEFAULT_USER_DIRECTORY_ID);
          statement.setObject(2, organisationId);

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                addUserDirectoryToOrganisationSQL));
          }
        }
      }

      organisation.setId(organisationId);

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }

      return userDirectory;
    }
    catch (DuplicateOrganisationException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to create the organisation (%s): %s",
          organisation.getId(), e.getMessage()), e);
    }
  }

  /**
   * Create a new user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  public void createUser(UUID userDirectoryId, User user, boolean expiredPassword,
      boolean userLocked)
    throws UserDirectoryNotFoundException, DuplicateUserException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(user.getUsername()))
    {
      throw new InvalidArgumentException("user.username");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.createUser(user, expiredPassword, userLocked);
  }

  /**
   * Create a new user directory.
   *
   * @param userDirectory the user directory
   */
  public void createUserDirectory(UserDirectory userDirectory)
    throws SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(userDirectory.getName()))
    {
      throw new InvalidArgumentException("userDirectory.name");
    }

    if (isNullOrEmpty(userDirectory.getTypeId()))
    {
      throw new InvalidArgumentException("userDirectory.typeId");
    }

    String createUserDirectorySQL = "INSERT INTO SECURITY.USER_DIRECTORIES "
        + "(ID, TYPE_ID, NAME, CONFIGURATION) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createUserDirectorySQL))
    {
      UUID userDirectoryId = idGenerator.nextUUID();

      statement.setObject(1, userDirectoryId);
      statement.setObject(2, userDirectory.getTypeId());
      statement.setString(3, userDirectory.getName());
      statement.setString(4, userDirectory.getConfiguration());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createUserDirectorySQL));
      }

      userDirectory.setId(userDirectoryId);

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to create the user directory (%s): %s",
          userDirectory.getName(), e.getMessage()), e);
    }
  }

  /**
   * Delete the authorised function.
   *
   * @param code the code identifying the authorised function
   */
  public void deleteFunction(String code)
    throws FunctionNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    String deleteFunctionSQL = "DELETE FROM SECURITY.FUNCTIONS WHERE CODE=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteFunctionSQL))
    {
      if (getFunctionId(connection, code) == null)
      {
        throw new FunctionNotFoundException(String.format(
            "A function with the code (%s) could not be found", code));
      }

      statement.setString(1, code);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteFunctionSQL));
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to delete the function (%s): %s", code,
          e.getMessage()), e);
    }
  }

  /**
   * Delete the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  public void deleteGroup(UUID userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, ExistingGroupMembersException,
        SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.deleteGroup(groupName);
  }

  /**
   * Delete the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public void deleteOrganisation(UUID id)
    throws OrganisationNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(id))
    {
      throw new InvalidArgumentException("id");
    }

    String deleteOrganisationSQL = "DELETE FROM SECURITY.ORGANISATIONS WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteOrganisationSQL))
    {
      if (!organisationExists(connection, id))
      {
        throw new OrganisationNotFoundException(String.format(
            "The organisation (%s) could not be found", id));
      }

      statement.setObject(1, id);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteOrganisationSQL));
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to delete the organisation (%s): %s", id,
          e.getMessage()), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   */
  public void deleteUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.deleteUser(username);
  }

  /**
   * Delete the user directory.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public void deleteUserDirectory(UUID id)
    throws UserDirectoryNotFoundException, SecurityException
  {
    String deleteUserDirectorySQL = "DELETE FROM SECURITY.USER_DIRECTORIES WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteUserDirectorySQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() <= 0)
      {
        throw new UserDirectoryNotFoundException(String.format(
            "The user directory ID (%s) is invalid", id));
      }

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to delete the user directory (%s): %s", id,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param attributes      the attribute criteria used to select the users
   *
   * @return the list of users whose attributes match the attribute criteria
   */
  public List<User> findUsers(UUID userDirectoryId, List<Attribute> attributes)
    throws UserDirectoryNotFoundException, InvalidAttributeException, SecurityException
  {
    // Validate parameters
    if (attributes == null)
    {
      throw new InvalidArgumentException("attributes");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.findUsers(attributes);
  }

  /**
   * Retrieve the filtered list of organisations.
   *
   * @param filter the filter to apply to the organisations
   *
   * @return the filtered list of organisations
   */
  public List<Organisation> getFilteredOrganisations(String filter)
    throws SecurityException
  {
    String getOrganisationsSQL =
        "SELECT O.ID, O.NAME, O.STATUS FROM SECURITY.ORGANISATIONS O ORDER BY O.NAME";

    String getFilteredOrganisationsSQL =
        "SELECT O.ID, O.NAME, O.STATUS FROM SECURITY.ORGANISATIONS O "
        + "WHERE (UPPER(O.NAME) LIKE ?) ORDER BY O.NAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getOrganisationsSQL
          : getFilteredOrganisationsSQL))
    {
      statement.setMaxRows(MAX_FILTERED_ORGANISATIONS);

      if (!StringUtil.isNullOrEmpty(filter))
      {
        String filterBuffer = String.format("%%%s%%", filter.toUpperCase());

        statement.setString(1, filterBuffer);
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<Organisation> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildOrganisationFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the filtered organisations: %s", e.getMessage()), e);
    }
  }

  /**
   * Retrieve the filtered list of user directories.
   *
   * @param filter the filter to apply to the user directories
   *
   * @return the filtered list of user directories
   */
  public List<UserDirectory> getFilteredUserDirectories(String filter)
    throws SecurityException
  {
    String getUserDirectoriesSQL =
        "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.CONFIGURATION FROM SECURITY.USER_DIRECTORIES UD";

    String getFilteredUserDirectoriesSQL =
        "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.CONFIGURATION FROM SECURITY.USER_DIRECTORIES UD "
        + "WHERE (UPPER(UD.NAME) LIKE ?) ORDER BY UD.NAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getUserDirectoriesSQL
          : getFilteredUserDirectoriesSQL))
    {
      statement.setMaxRows(MAX_FILTERED_USER_DIRECTORIES);

      if (!StringUtil.isNullOrEmpty(filter))
      {
        String filterBuffer = String.format("%%%s%%", filter.toUpperCase());

        statement.setString(1, filterBuffer);
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectory> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildUserDirectoryFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the filtered user directories: %s", e.getMessage()), e);
    }
  }

  /**
   * Retrieve the filtered list of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the filter to apply to the users
   *
   * @return the filtered list of users
   */
  public List<User> getFilteredUsers(UUID userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getFilteredUsers(filter);
  }

  /**
   * Retrieve the authorised function.
   *
   * @param code the code identifying the function
   *
   * @return the authorised function
   */
  public Function getFunction(String code)
    throws FunctionNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    String getFunctionSQL =
        "SELECT F.ID, F.CODE, F.NAME, F.DESCRIPTION FROM SECURITY.FUNCTIONS F WHERE F.CODE=?";

    try
    {
      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getFunctionSQL))
      {
        statement.setString(1, code);

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            return buildFunctionFromResultSet(rs);
          }
          else
          {
            throw new FunctionNotFoundException(String.format(
                "A function with the code (%s) could not be found", code));
          }
        }
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to retrieve the function (%s): %s", code,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the list of authorised function codes for the user
   */
  public List<String> getFunctionCodesForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getFunctionCodesForUser(username);
  }

  /**
   * Retrieve all the authorised functions.
   *
   * @return the list of authorised functions
   */
  public List<Function> getFunctions()
    throws SecurityException
  {
    String getFunctionsSQL = "SELECT F.ID, F.CODE, F.NAME, F.DESCRIPTION FROM SECURITY.FUNCTIONS F";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getFunctionsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<Function> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildFunctionFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to retrieve the functions: %s",
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   *
   * @return the security group
   */
  public Group getGroup(UUID userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getGroup(groupName);
  }

  /**
   * Retrieve the security group names for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security group names for the user
   */
  public List<String> getGroupNamesForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getGroupNamesForUser(username);
  }

  /**
   * Retrieve all the security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the list of security groups
   */
  public List<Group> getGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getGroups();
  }

  /**
   * Retrieve the security groups for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security groups for the user
   */
  public List<Group> getGroupsForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getGroupsForUser(username);
  }

  /**
   * Retrieve the number of filtered organisations.
   *
   * @param filter the filter to apply to the organisations
   *
   * @return the number of filtered organisations
   */
  public int getNumberOfFilteredOrganisations(String filter)
    throws SecurityException
  {
    String getNumberOfOrganisationsSQL = "SELECT COUNT(O.ID) FROM SECURITY.ORGANISATIONS O";

    String getNumberOfFilteredOrganisationsSQL =
        "SELECT COUNT(O.ID) FROM SECURITY.ORGANISATIONS O WHERE (UPPER(O.NAME) LIKE ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getNumberOfOrganisationsSQL
          : getNumberOfFilteredOrganisationsSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        String filterBuffer = String.format("%%%s%%", filter.toUpperCase());

        statement.setString(1, filterBuffer);
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          int numberOfFilteredOrganisations = rs.getInt(1);

          return ((numberOfFilteredOrganisations > MAX_FILTERED_ORGANISATIONS)
              ? MAX_FILTERED_ORGANISATIONS
              : numberOfFilteredOrganisations);
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
          "Failed to retrieve the number of filtered organisations: %s", e.getMessage()), e);
    }
  }

  /**
   * Retrieve the number of filtered user directories.
   *
   * @param filter the filter to apply to the user directories
   *
   * @return the number of filtered user directories
   */
  public int getNumberOfFilteredUserDirectories(String filter)
    throws SecurityException
  {
    String getNumberOfUserDirectoriesSQL = "SELECT COUNT(UD.ID) FROM SECURITY.USER_DIRECTORIES UD";

    String getNumberOfFilteredUserDirectoriesSQL =
        "SELECT COUNT(UD.ID) FROM SECURITY.USER_DIRECTORIES UD WHERE (UPPER(UD.NAME) LIKE ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getNumberOfUserDirectoriesSQL
          : getNumberOfFilteredUserDirectoriesSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        String filterBuffer = String.format("%%%s%%", filter.toUpperCase());

        statement.setString(1, filterBuffer);
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          int numberOfFilteredUserDirectories = rs.getInt(1);

          return ((numberOfFilteredUserDirectories > MAX_FILTERED_USER_DIRECTORIES)
              ? MAX_FILTERED_USER_DIRECTORIES
              : numberOfFilteredUserDirectories);
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
          "Failed to retrieve the number of filtered user directories: %s", e.getMessage()), e);
    }
  }

  /**
   * Retrieve the number of filtered users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the filter to apply to the users
   *
   * @return the number of filtered users
   */
  public int getNumberOfFilteredUsers(UUID userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getNumberOfFilteredUsers(filter);
  }

  /**
   * Retrieve the number of security groups
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of security groups
   */
  public int getNumberOfGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getNumberOfGroups();
  }

  /**
   * Retrieve the number of organisations
   *
   * @return the number of organisations
   */
  public int getNumberOfOrganisations()
    throws SecurityException
  {
    String getNumberOfOrganisationsSQL = "SELECT COUNT(O.ID) FROM SECURITY.ORGANISATIONS O";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfOrganisationsSQL))
    {
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
          "Failed to retrieve the number of organisations: %s", e.getMessage()), e);
    }
  }

  /**
   * Retrieve the number of user directories
   *
   * @return the number of user directories
   */
  public int getNumberOfUserDirectories()
    throws SecurityException
  {
    String getNumberOfUserDirectoriesSQL = "SELECT COUNT(UD.ID) FROM SECURITY.USER_DIRECTORIES UD";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfUserDirectoriesSQL))
    {
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
          "Failed to retrieve the number of user directories: %s", e.getMessage()), e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of users
   */
  public int getNumberOfUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getNumberOfUsers();
  }

  /**
   * Retrieve the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   *
   * @return the organisation
   */
  public Organisation getOrganisation(UUID id)
    throws OrganisationNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(id))
    {
      throw new InvalidArgumentException("id");
    }

    String getOrganisationSQL =
        "SELECT O.ID, O.NAME, O.STATUS FROM SECURITY.ORGANISATIONS O WHERE O.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getOrganisationSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildOrganisationFromResultSet(rs);
        }
        else
        {
          throw new OrganisationNotFoundException(String.format(
              "The organisation (%s) could not be found", id));
        }
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to retrieve the organisation (%s): %s", id,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the Universally Unique Identifiers (UUIDs) used to uniquely identify the organisations
   * associated with the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the Universally Unique Identifiers (UUIDs) used to uniquely identify the organisations
   *         associated with the user directory
   */
  public List<UUID> getOrganisationIdsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    String getOrganisationIdsForUserDirectorySQL =
        "SELECT UDTOM.ORGANISATION_ID FROM SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM "
        + "WHERE UDTOM.USER_DIRECTORY_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getOrganisationIdsForUserDirectorySQL))
    {
      statement.setObject(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<UUID> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(UUID.fromString(rs.getString(1)));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the IDs for the organisations for the user directory (%s): %s",
          userDirectoryId, e.getMessage()), e);
    }
  }

  /**
   * Retrieve the organisations.
   *
   * @return the list of organisations
   */
  public List<Organisation> getOrganisations()
    throws SecurityException
  {
    String getOrganisationsSQL =
        "SELECT O.ID, O.NAME, O.STATUS FROM SECURITY.ORGANISATIONS O ORDER BY O.NAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getOrganisationsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<Organisation> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildOrganisationFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to retrieve the organisations: %s",
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the organisations associated with the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the organisations associated with the user directory
   */
  public List<Organisation> getOrganisationsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    String getOrganisationsForUserDirectorySQL =
        "SELECT O.ID, O.NAME, O.STATUS FROM SECURITY.ORGANISATIONS O INNER JOIN "
        + "SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM ON O.ID = UDTOM.ORGANISATION_ID WHERE "
        + "UDTOM.USER_DIRECTORY_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getOrganisationsForUserDirectorySQL))
    {
      statement.setObject(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Organisation> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildOrganisationFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the organisations associated with the user directory (%s): %s",
          userDirectoryId, e.getMessage()), e);
    }
  }

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the user
   */
  public User getUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getUser(username);
  }

  /**
   * Retrieve the user directories.
   *
   * @return the list of user directories
   */
  public List<UserDirectory> getUserDirectories()
    throws SecurityException
  {
    String getUserDirectoriesSQL =
        "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.CONFIGURATION FROM SECURITY.USER_DIRECTORIES UD";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectoriesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectory> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildUserDirectoryFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to retrieve the user directories: %s",
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the user directories the organisation is associated with.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation
   *
   * @return the user directories the organisation is associated with
   */
  public List<UserDirectory> getUserDirectoriesForOrganisation(UUID organisationId)
    throws OrganisationNotFoundException, SecurityException
  {
    String getUserDirectoriesForOrganisationSQL =
        "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.CONFIGURATION FROM SECURITY.USER_DIRECTORIES UD "
        + "INNER JOIN SECURITY.USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM "
        + "ON UD.ID = UDTOM.USER_DIRECTORY_ID INNER JOIN SECURITY.ORGANISATIONS O "
        + "ON UDTOM.ORGANISATION_ID = O.ID WHERE O.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getUserDirectoriesForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectory> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildUserDirectoryFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the user directories associated with the organisation (%s): %s",
          organisationId, e.getMessage()), e);
    }
  }

  /**
   * Retrieve the user directory.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return the user directory
   */
  public UserDirectory getUserDirectory(UUID id)
    throws UserDirectoryNotFoundException, SecurityException
  {
    String getUserDirectorySQL = "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.CONFIGURATION "
        + "FROM SECURITY.USER_DIRECTORIES UD WHERE UD.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectorySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildUserDirectoryFromResultSet(rs);
        }
        else
        {
          throw new UserDirectoryNotFoundException(String.format(
              "The user directory (%s) could not be found", id));
        }
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to retrieve the user directory (%s): %s",
          id, e.getMessage()), e);
    }
  }

  /**
   * Retrieve the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * that the user with the specified username is associated with.
   *
   * @param username the username identifying the user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *         that the user with the specified username is associated with or <code>null</code> if
   *         the user cannot be found
   */
  public UUID getUserDirectoryIdForUser(String username)
    throws SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        return internalUserDirectoryId;
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if the user is associated with one
         * of them.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                return userDirectoryId;
              }
            }
          }
        }

        return null;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to retrieve the user directory ID for the user (%s): %s", username,
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   */
  public List<UserDirectoryType> getUserDirectoryTypes()
    throws SecurityException
  {
    String getUserDirectoryTypesSQL = "SELECT UDT.ID, UDT.NAME, UDT.USER_DIRECTORY_CLASS, "
        + "UDT.ADMINISTRATION_CLASS FROM SECURITY.USER_DIRECTORY_TYPES UDT";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectoryTypesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectoryType> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(new UserDirectoryType(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
              rs.getString(4)));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to retrieve the user directory types: %s",
          e.getMessage()), e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the list of users
   */
  public List<User> getUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.getUsers();
  }

  /**
   * Initialise the <code>SecurityService</code> instance.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      // Initialise the configuration
      initConfiguration();

      // Load the user directory types
      reloadUserDirectoryTypes();

      // Load the user directories
      reloadUserDirectories();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Security Service", e);
    }
  }

  /**
   * Is the user in the security group?
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the security group uniquely identifying the security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   *         otherwise
   */
  public boolean isUserInGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.isUserInGroup(username, groupName);
  }

  /**
   * Reload the user directories.
   */
  public void reloadUserDirectories()
    throws SecurityException
  {
    try
    {
      Map<UUID, IUserDirectory> reloadedUserDirectories = new ConcurrentHashMap<>();

      for (UserDirectory userDirectory : getUserDirectories())
      {
        if (userDirectory.getType() == null)
        {
          logger.error(String.format(
              "Failed to load the user directory (%s): The user directory type (%s) was not loaded",
              userDirectory.getId(), userDirectory.getTypeId()));

          continue;
        }

        try
        {
          Class<?> clazz = userDirectory.getType().getUserDirectoryClass();

          Class<? extends IUserDirectory> userDirectoryClass = clazz.asSubclass(
              IUserDirectory.class);

          if (userDirectoryClass == null)
          {
            throw new SecurityException(String.format(
                "The user directory class (%s) does not implement the IUserDirectory interface",
                userDirectory.getType().getUserDirectoryClassName()));
          }

          Constructor<? extends IUserDirectory> userDirectoryClassConstructor =
              userDirectoryClass.getConstructor(UUID.class, Map.class);

          if (userDirectoryClassConstructor == null)
          {
            throw new SecurityException(String.format(
                "The user directory class (%s) does not provide a valid constructor (long, "
                + "Map<String,String>)", userDirectory.getType().getUserDirectoryClassName()));
          }

          IUserDirectory userDirectoryInstance = userDirectoryClassConstructor.newInstance(
              userDirectory.getId(), userDirectory.getParameters());

          applicationContext.getAutowireCapableBeanFactory().autowireBean(userDirectoryInstance);

          reloadedUserDirectories.put(userDirectory.getId(), userDirectoryInstance);
        }
        catch (Throwable e)
        {
          throw new SecurityException(String.format(
              "Failed to initialise the user directory (%s)(%s)", userDirectory.getId(),
              userDirectory.getName()), e);
        }
      }

      this.userDirectories = reloadedUserDirectories;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to reload the user directories", e);
    }
  }

  /**
   * Remove the user from the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the security group name
   */
  public void removeUserFromGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.removeUserFromGroup(username, groupName);
  }

  /**
   * Does the user directory support administering security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user directory supports administering security groups or
   *         <code>false</code> otherwise
   */
  public boolean supportsGroupAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.supportsGroupAdministration();
  }

  /**
   * Does the user directory support administering users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user directory supports administering users or
   *         <code>false</code> otherwise
   */
  public boolean supportsUserAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    return userDirectory.supportsUserAdministration();
  }

  /**
   * Update the authorised function.
   *
   * @param function the function
   */
  public void updateFunction(Function function)
    throws FunctionNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(function.getId()))
    {
      throw new InvalidArgumentException("function.id");
    }

    if (isNullOrEmpty(function.getCode()))
    {
      throw new InvalidArgumentException("function.code");
    }

    if (isNullOrEmpty(function.getName()))
    {
      throw new InvalidArgumentException("function.name");
    }

    String updateFunctionSQL =
        "UPDATE SECURITY.FUNCTIONS SET NAME=?, DESCRIPTION=? WHERE CODE=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateFunctionSQL))
    {
      if (getFunctionId(connection, function.getCode()) == null)
      {
        throw new FunctionNotFoundException(String.format(
            "A function with the code (%s) could not be found", function.getCode()));
      }

      statement.setString(1, function.getName());
      statement.setString(2, StringUtil.notNull(function.getDescription()));
      statement.setString(3, function.getCode());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateFunctionSQL));
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to update the function (%s): %s",
          function.getCode(), e.getMessage()), e);
    }
  }

  /**
   * Update the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  public void updateGroup(UUID userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(group.getGroupName()))
    {
      throw new InvalidArgumentException("group.groupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.updateGroup(group);
  }

  /**
   * Update the organisation.
   *
   * @param organisation the organisation
   */
  public void updateOrganisation(Organisation organisation)
    throws OrganisationNotFoundException, SecurityException
  {
    if (isNullOrEmpty(organisation.getId()))
    {
      throw new InvalidArgumentException("organisation.id");
    }

    if (isNullOrEmpty(organisation.getName()))
    {
      throw new InvalidArgumentException("organisation.name");
    }

    String updateOrganisationSQL =
        "UPDATE SECURITY.ORGANISATIONS SET NAME=?, STATUS=? WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateOrganisationSQL))
    {
      if (!organisationExists(connection, organisation.getId()))
      {
        throw new OrganisationNotFoundException(String.format(
            "An organisation with the ID (%s) could not be found", organisation.getId()));
      }

      statement.setString(1, organisation.getName());
      statement.setInt(2, organisation.getStatus().code());
      statement.setObject(3, organisation.getId());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateOrganisationSQL));
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to update the organisation (%s): %s",
          organisation.getId(), e.getMessage()), e);
    }
  }

  /**
   * Update the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expirePassword  expire the user's password as part of the update
   * @param lockUser        lock the user as part of the update
   */
  public void updateUser(UUID userDirectoryId, User user, boolean expirePassword, boolean lockUser)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(user.getUsername()))
    {
      throw new InvalidArgumentException("user.username");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(String.format(
          "The user directory ID (%s) is invalid", userDirectoryId));
    }

    userDirectory.updateUser(user, expirePassword, lockUser);
  }

  /**
   * Update the user directory.
   *
   * @param userDirectory the user directory
   */
  public void updateUserDirectory(UserDirectory userDirectory)
    throws UserDirectoryNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(userDirectory.getName()))
    {
      throw new InvalidArgumentException("userDirectory.name");
    }

    if (isNullOrEmpty(userDirectory.getTypeId()))
    {
      throw new InvalidArgumentException("userDirectory.typeId");
    }

    String updateUserDirectorySQL = "UPDATE SECURITY.USER_DIRECTORIES "
        + "SET NAME=?, CONFIGURATION=? WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateUserDirectorySQL))
    {
      statement.setString(1, userDirectory.getName());
      statement.setString(2, userDirectory.getConfiguration());
      statement.setObject(3, userDirectory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateUserDirectorySQL));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format("Failed to update the user directory (%s): %s",
          userDirectory.getName(), e.getMessage()), e);
    }
  }

  /**
   * Create a new <code>Function</code> instance and populate it with the contents of the
   * current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>Function</code> instance
   *
   * @return the populated <code>Function</code> instance
   */
  private Function buildFunctionFromResultSet(ResultSet rs)
    throws SQLException
  {
    Function function = new Function();

    function.setId(UUID.fromString(rs.getString(1)));
    function.setCode(rs.getString(2));
    function.setName(rs.getString(3));
    function.setDescription(StringUtil.notNull(rs.getString(4)));

    return function;
  }

  /**
   * Create a new <code>Organisation</code> instance and populate it with the contents of the
   * current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>Organisation</code> instance
   *
   * @return the populated <code>Organisation</code> instance
   */
  private Organisation buildOrganisationFromResultSet(ResultSet rs)
    throws SQLException
  {
    Organisation organisation = new Organisation();
    organisation.setId(UUID.fromString(rs.getString(1)));
    organisation.setName(rs.getString(2));
    organisation.setStatus(OrganisationStatus.fromCode(rs.getInt(3)));

    return organisation;
  }

  /**
   * Create a new <code>UserDirectory</code> instance and populate it with the contents of the
   * current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>UserDirectory</code> instance
   *
   * @return the populated <code>UserDirectory</code> instance
   */
  private UserDirectory buildUserDirectoryFromResultSet(ResultSet rs)
    throws SQLException, SecurityException
  {
    UserDirectory userDirectory = new UserDirectory();
    userDirectory.setId(UUID.fromString(rs.getString(1)));
    userDirectory.setTypeId(UUID.fromString(rs.getString(2)));
    userDirectory.setType(userDirectoryTypes.get(UUID.fromString(rs.getString(2))));
    userDirectory.setName(rs.getString(3));
    userDirectory.setConfiguration(rs.getString(4));

    return userDirectory;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the function with
   * the specified code.
   *
   * @param connection the existing database connection to use
   * @param code       the code uniquely identifying the function
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the function or
   *         <code>null</code> if a function with the specified code cannot be found
   */
  private UUID getFunctionId(Connection connection, String code)
    throws SQLException
  {
    String getFunctionIdSQL = "SELECT F.ID FROM SECURITY.FUNCTIONS F WHERE F.CODE=?";

    try (PreparedStatement statement = connection.prepareStatement(getFunctionIdSQL))
    {
      statement.setString(1, code);

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
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   * directory the internal user with the specified username is associated with.
   *
   * @param username the username uniquely identifying the internal user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   *         directory the internal user with the specified username is associated with or
   *         <code>null</code> if an internal user with the specified username could not be found
   */
  private UUID getInternalUserDirectoryIdForUser(String username)
    throws SecurityException
  {
    String getInternalUserDirectoryIdForUserSQL = "SELECT IU.USER_DIRECTORY_ID FROM "
        + "SECURITY.INTERNAL_USERS IU WHERE UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getInternalUserDirectoryIdForUserSQL))
    {
      statement.setString(1, username);

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
          "Failed to retrieve the ID for the internal user directory for the internal user (%s)",
          username), e);
    }
  }

  /**
   * Initialise the configuration for the <code>SecurityService</code> instance.
   */
  private void initConfiguration()
    throws SecurityException
  {
//  try
//  {}
//  catch (Throwable e)
//  {
//    throw new SecurityException(
//        "Failed to initialise the configuration for the Security Service: " + e.getMessage(), e);
//  }
  }

  /**
   * Checks whether the specified value is <code>null</code> or blank.
   *
   * @param value the value to check
   *
   * @return true if the value is <code>null</code> or blank
   */
  private boolean isNullOrEmpty(Object value)
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

  private UserDirectory newInternalUserDirectoryForOrganisation(Organisation organisation)
    throws SecurityException
  {
    UserDirectory userDirectory = new UserDirectory();

    userDirectory.setId(idGenerator.nextUUID());
    userDirectory.setTypeId(UUID.fromString("b43fda33-d3b0-4f80-a39a-110b8e530f4f"));
    userDirectory.setName(organisation.getName() + " User Directory");

    String buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<!DOCTYPE userDirectory "
        + "SYSTEM \"UserDirectoryConfiguration.dtd\">" + "<userDirectory>"
        + "<parameter><name>MaxPasswordAttempts</name><value>5</value></parameter>"
        + "<parameter><name>PasswordExpiryMonths</name><value>12</value></parameter>"
        + "<parameter><name>PasswordHistoryMonths</name><value>24</value></parameter>"
        + "<parameter><name>MaxFilteredUsers</name><value>100</value></parameter>"
        + "</userDirectory>";

    userDirectory.setConfiguration(buffer);

    return userDirectory;
  }

  /**
   * Returns <code>true</code> if the organisation exists or <code>false</code> otherwise.
   *
   * @param connection the existing database connection to use
   * @param id         the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   organisation
   *
   * @return <code>true</code> if the organisation exists or <code>false</code> otherwise
   */
  private boolean organisationExists(Connection connection, UUID id)
    throws SecurityException
  {
    String organisationExistsSQL = "SELECT COUNT(O.ID) FROM SECURITY.ORGANISATIONS O WHERE O.ID=?";

    try (PreparedStatement statement = connection.prepareStatement(organisationExistsSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (rs.getInt(1) > 0);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to check whether the organisation (%s) exists", id), e);
    }
  }

  /**
   * Returns <code>true</code> if an organisation with the specified name exists or
   * <code>false</code> otherwise.
   *
   * @param connection the existing database connection to use
   * @param name       the organisation name
   *
   * @return <code>true</code> if an organisation with the specified name exists or
   *         <code>false</code> otherwise
   */
  private boolean organisationWithNameExists(Connection connection, String name)
    throws SecurityException
  {
    String organisationWithNameExistsSQL =
        "SELECT COUNT(O.ID) FROM SECURITY.ORGANISATIONS O WHERE (UPPER(O.NAME) LIKE ?)";

    try (PreparedStatement statement = connection.prepareStatement(organisationWithNameExistsSQL))
    {
      statement.setString(1, name.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (rs.getInt(1) > 0);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(String.format(
          "Failed to check whether an organisation with the name (%s) exists", name), e);
    }
  }

  /**
   * Reload the user directory types.
   */
  private void reloadUserDirectoryTypes()
    throws SecurityException
  {
    try
    {
      Map<UUID, UserDirectoryType> reloadedUserDirectoryTypes = new ConcurrentHashMap<>();

      for (UserDirectoryType userDirectoryType : getUserDirectoryTypes())
      {
        try
        {
          userDirectoryType.getUserDirectoryClass();
        }
        catch (Throwable e)
        {
          logger.error(String.format("Failed to load the user directory type (%s): "
              + "Failed to retrieve the user directory class for the user directory type",
              userDirectoryType.getId()), e);

          continue;
        }

        reloadedUserDirectoryTypes.put(userDirectoryType.getId(), userDirectoryType);
      }

      this.userDirectoryTypes = reloadedUserDirectoryTypes;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to reload the user directory types", e);
    }
  }
}
