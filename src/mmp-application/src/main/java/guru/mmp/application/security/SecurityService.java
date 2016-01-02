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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.registry.IRegistry;
import guru.mmp.common.exception.InvalidArgumentException;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.IDGenerator;
import guru.mmp.common.persistence.TransactionManager;
import guru.mmp.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;

import java.sql.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>SecurityService</code> class provides the Security Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class SecurityService
  implements ISecurityService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the LDAP user directory
   * type.
   */
  public static final UUID LDAP_USER_DIRECTORY_TYPE_ID =
    UUID.fromString("e5741a89-c87b-4406-8a60-2cc0b0a5fa3e");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the internal user directory
   * type.
   */
  public static final UUID INTERNAL_USER_DIRECTORY_TYPE_ID =
    UUID.fromString("b43fda33-d3b0-4f80-a39a-110b8e530f4f");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the default user directory.
   */
  public static final UUID DEFAULT_USER_DIRECTORY_ID =
    UUID.fromString("4ef18395-423a-4df6-b7d7-6bcdd85956e4");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the default organisation.
   */
  public static final UUID DEFAULT_ORGANISATION_ID =
    UUID.fromString("c1685b92-9fe5-453a-995b-89d8c0f29cb5");
  private String addUserDirectoryToOrganisationSQL;
  private String createFunctionSQL;
  private String createOrganisationSQL;
  private String createUserDirectorySQL;
  private DataSource dataSource;
  private String deleteFunctionSQL;
  private String deleteOrganisationSQL;
  private String deleteUserDirectorySQL;
  private String getFunctionIdSQL;
  private String getFunctionSQL;
  private String getFunctionsSQL;
  private String getInternalUserDirectoryIdForUserSQL;
  private String getNumberOfOrganisationsSQL;
  private String getNumberOfUserDirectoriesSQL;
  private String getOrganisationIdsForUserDirectorySQL;
  private String getOrganisationSQL;
  private String getOrganisationsForUserDirectorySQL;
  private String getOrganisationsSQL;
  private String getUserDirectoriesForOrganisationSQL;
  private String getUserDirectoriesSQL;
  private String getUserDirectorySQL;
  private String getUserDirectoryTypesSQL;
  private String organisationExistsSQL;

  /* Registry */
  @Inject
  private IRegistry registry;
  private String updateFunctionSQL;
  private String updateOrganisationSQL;
  private String updateUserDirectorySQL;
  private Map<UUID, IUserDirectory> userDirectories = new ConcurrentHashMap<>();
  private Map<UUID, UserDirectoryType> userDirectoryTypes = new ConcurrentHashMap<>();

  /**
   * Constructs a new <code>SecurityService</code>.
   */
  public SecurityService() {}

  /**
   * Add the user to the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the group uniquely identifying the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
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
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
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
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredPasswordException
   * @throws UserNotFoundException
   * @throws SecurityException
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
          throw new SecurityException("The user directory ID (" + internalUserDirectoryId
              + ") for the internal user (" + username + ") is invalid");
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

        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExpiredPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to authenticate the user (" + username + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param password        the password for the user that is used to authorise the operation
   * @param newPassword     the new password
   *
   * @throws UserDirectoryNotFoundException
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws UserNotFoundException
   * @throws ExistingPasswordException
   * @throws SecurityException
   */
  public void changePassword(UUID userDirectoryId, String username, String password,
      String newPassword)
    throws UserDirectoryNotFoundException, AuthenticationFailedException, UserLockedException,
      UserNotFoundException, ExistingPasswordException, SecurityException
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

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.changePassword(username, password, newPassword);
  }

  /**
   * Create a new authorised function.
   *
   * @param function the function
   *
   * @throws DuplicateFunctionException
   * @throws SecurityException
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

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createFunctionSQL))
    {
      if (getFunctionId(connection, function.getCode()) != null)
      {
        throw new DuplicateFunctionException("A function with the code (" + function.getCode()
            + ") already exists");
      }

      statement.setObject(1, function.getId());
      statement.setString(2, function.getCode());
      statement.setString(3, function.getName());
      statement.setString(4, function.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + createFunctionSQL + ")");
      }
    }
    catch (DuplicateFunctionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to create the function (" + function.getCode() + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Create a new group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws DuplicateGroupException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
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
   *
   * @throws DuplicateOrganisationException
   * @throws SecurityException
   */
  public UserDirectory createOrganisation(Organisation organisation, boolean createUserDirectory)
    throws DuplicateOrganisationException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(organisation.getId()))
    {
      throw new InvalidArgumentException("organisation.id");
    }

    if (isNullOrEmpty(organisation.getName()))
    {
      throw new InvalidArgumentException("organisation.name");
    }

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

      UserDirectory userDirectory = null;

      try (Connection connection = dataSource.getConnection())
      {
        try (PreparedStatement statement = connection.prepareStatement(createOrganisationSQL))
        {
          if (organisationExists(connection, organisation.getId()))
          {
            throw new DuplicateOrganisationException("The organisation (" + organisation.getId()
                + ") already exists");
          }

          statement.setObject(1, organisation.getId());
          statement.setString(2, organisation.getName());
          statement.setString(3, organisation.getDescription());

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityException(
                "No rows were affected as a result of executing the SQL statement ("
                + createOrganisationSQL + ")");
          }
        }

        if (createUserDirectory)
        {
          userDirectory = newInternalUserDirectoryForOrganisation(organisation);

          try (PreparedStatement statement = connection.prepareStatement(createUserDirectorySQL))
          {
            statement.setObject(1, userDirectory.getId());
            statement.setObject(2, userDirectory.getTypeId());
            statement.setString(3, userDirectory.getName());
            statement.setString(4, userDirectory.getDescription());
            statement.setString(5, userDirectory.getConfiguration());

            if (statement.executeUpdate() != 1)
            {
              throw new SecurityException(
                  "No rows were affected as a result of executing the SQL statement ("
                  + createUserDirectorySQL + ")");
            }
          }

          // Link the new user directory to the new organisation
          try (PreparedStatement statement =
              connection.prepareStatement(addUserDirectoryToOrganisationSQL))
          {
            statement.setObject(1, userDirectory.getId());
            statement.setObject(2, organisation.getId());

            if (statement.executeUpdate() != 1)
            {
              throw new SecurityException(
                  "No rows were affected as a result of executing the SQL statement ("
                  + addUserDirectoryToOrganisationSQL + ")");
            }
          }
        }

        // Link the new organisation to the default user directory
        try (PreparedStatement statement =
            connection.prepareStatement(addUserDirectoryToOrganisationSQL))
        {
          statement.setObject(1, DEFAULT_USER_DIRECTORY_ID);
          statement.setObject(2, organisation.getId());

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityException(
                "No rows were affected as a result of executing the SQL statement ("
                + addUserDirectoryToOrganisationSQL + ")");
          }
        }
      }

      transactionManager.commit();

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
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while creating the organisation ("
            + organisation.getId() + ")", f);
      }

      throw e;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while creating the organisation ("
            + organisation.getId() + ")", f);
      }

      throw new SecurityException("Failed to create the organisation (" + organisation.getId()
          + "): " + e.getMessage(), e);
    }
    finally
    {
      try
      {
        transactionManager.resume(existingTransaction);
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while creating the organisation ("
            + organisation.getId() + ")", e);
      }
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
   *
   * @throws UserDirectoryNotFoundException
   * @throws DuplicateUserException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.createUser(user, expiredPassword, userLocked);
  }

  /**
   * Create a new user directory.
   *
   * @param userDirectory the user directory
   *
   * @throws SecurityException
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

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createUserDirectorySQL))
    {
      UUID userDirectoryId = IDGenerator.nextUUID(dataSource);

      statement.setObject(1, userDirectoryId);
      statement.setObject(2, userDirectory.getTypeId());
      statement.setString(3, userDirectory.getName());
      statement.setString(4, userDirectory.getDescription());
      statement.setString(5, userDirectory.getConfiguration());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + createUserDirectorySQL + ")");
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
      throw new SecurityException("Failed to create the user directory (" + userDirectory.getName()
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Delete the authorised function.
   *
   * @param code the code identifying the authorised function
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  public void deleteFunction(String code)
    throws FunctionNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteFunctionSQL))
    {
      if (getFunctionId(connection, code) == null)
      {
        throw new FunctionNotFoundException("A function with the code (" + code
            + ") could not be found");
      }

      statement.setString(1, code);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + deleteFunctionSQL + ")");
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the function (" + code + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Delete the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the group uniquely identifying the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.deleteGroup(groupName);
  }

  /**
   * Delete the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void deleteOrganisation(UUID id)
    throws OrganisationNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(id))
    {
      throw new InvalidArgumentException("id");
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteOrganisationSQL))
    {
      if (!organisationExists(connection, id))
      {
        throw new OrganisationNotFoundException("The organisation (" + id + ") could not be found");
      }

      statement.setObject(1, id);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + deleteOrganisationSQL + ")");
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the organisation (" + id + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.deleteUser(username);
  }

  /**
   * Delete the user directory.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public void deleteUserDirectory(UUID id)
    throws UserDirectoryNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteUserDirectorySQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() <= 0)
      {
        throw new UserDirectoryNotFoundException();
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
      throw new SecurityException("Failed to delete the user directory (" + id + "): "
          + e.getMessage(), e);
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
   *
   * @throws UserDirectoryNotFoundException
   * @throws InvalidAttributeException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.findUsers(attributes);
  }

///**
// * Returns the <code>DataSource</code> for the <code>SecurityService</code>.
// *
// * @return the <code>DataSource</code> for the <code>SecurityService</code>
// */
//public DataSource getDataSource()
//{
//  return dataSource;
//}

  /**
   * Retrieve the filtered list of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the filter to apply to the users
   *
   * @return the filtered list of users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<User> getFilteredUsers(UUID userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getFilteredUsers(filter);
  }

  /**
   * Retrieve the authorised function.
   *
   * @param code the code identifying the function
   *
   * @return the authorised function
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  public Function getFunction(String code)
    throws FunctionNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

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
            throw new FunctionNotFoundException("A function with the code (" + code
                + ") could not be found");
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
      throw new SecurityException("Failed to retrieve the function (" + code + "): "
          + e.getMessage(), e);
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
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getFunctionCodesForUser(username);
  }

  /**
   * Retrieve all the authorised functions.
   *
   * @return the list of authorised functions
   *
   * @throws SecurityException
   */
  public List<Function> getFunctions()
    throws SecurityException
  {
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
      throw new SecurityException("Failed to retrieve the functions: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the group uniquely identifying the group
   *
   * @return the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getGroup(groupName);
  }

  /**
   * Retrieve the group names for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the group names for the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getGroupNamesForUser(username);
  }

  /**
   * Retrieve all the groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the list of groups
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<Group> getGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getGroups();
  }

  /**
   * Retrieve the groups for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the groups for the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getGroupsForUser(username);
  }

  /**
   * Retrieve the number of filtered users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the filter to apply to the users
   *
   * @return the number of filtered users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfFilteredUsers(UUID userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getNumberOfFilteredUsers(filter);
  }

  /**
   * Retrieve the number of groups
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of groups
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getNumberOfGroups();
  }

  /**
   * Retrieve the number of organisations
   *
   * @return the number of organisations
   *
   * @throws SecurityException
   */
  public int getNumberOfOrganisations()
    throws SecurityException
  {
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
      throw new SecurityException("Failed to retrieve the number of organisations"
          + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the number of user directories
   *
   * @return the number of user directories
   *
   * @throws SecurityException
   */
  public int getNumberOfUserDirectories()
    throws SecurityException
  {
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
      throw new SecurityException("Failed to retrieve the number of user directories"
          + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getNumberOfUsers();
  }

  /**
   * Retrieve the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   *
   * @return the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public Organisation getOrganisation(UUID id)
    throws OrganisationNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(id))
    {
      throw new InvalidArgumentException("id");
    }

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
          throw new OrganisationNotFoundException("The organisation (" + id
              + ") could not be found");
        }
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the organisation (" + id + "): "
          + e.getMessage(), e);
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
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<UUID> getOrganisationIdsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getOrganisationIdsForUserDirectorySQL))
    {
      statement.setObject(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<UUID> list = new ArrayList<>();

        while (rs.next())
        {
          list.add((UUID) rs.getObject(1));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException(
          "Failed to retrieve the IDs for the organisations associated with the user directory ("
          + userDirectoryId + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the organisations.
   *
   * @return the list of organisations
   *
   * @throws SecurityException
   */
  public List<Organisation> getOrganisations()
    throws SecurityException
  {
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
      throw new SecurityException("Failed to retrieve the organisations: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the organisations associated with the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the organisations associated with the user directory
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<Organisation> getOrganisationsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getOrganisationsForUserDirectorySQL))
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
      throw new SecurityException(
          "Failed to retrieve the organisations associated with the user directory ("
          + userDirectoryId + "): " + e.getMessage(), e);
    }
  }

///**
// * Returns the <code>Registry</code> for the <code>SecurityService</code>.
// *
// * @return the <code>Registry</code> for the <code>SecurityService</code>
// */
//public IRegistry getRegistry()
//{
//  return registry;
//}

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getUser(username);
  }

  /**
   * Retrieve the user directories.
   *
   * @return the list of user directories
   *
   * @throws SecurityException
   */
  public List<UserDirectory> getUserDirectories()
    throws SecurityException
  {
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
      throw new SecurityException("Failed to retrieve the user directories: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the user directories the organisation is associated with.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation
   *
   * @return the user directories the organisation is associated with
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<UserDirectory> getUserDirectoriesForOrganisation(UUID organisationId)
    throws OrganisationNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getUserDirectoriesForOrganisationSQL))
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
      throw new SecurityException(
          "Failed to retrieve the user directories associated with the organisation ("
          + organisationId + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the user directory.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return the user directory
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public UserDirectory getUserDirectory(UUID id)
    throws UserDirectoryNotFoundException, SecurityException
  {
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
          throw new UserDirectoryNotFoundException("The user directory (" + id
              + ") could not be found");
        }
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the user directory (" + id + "): "
          + e.getMessage(), e);
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
   *
   * @throws SecurityException
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
      throw new SecurityException("Failed to retrieve the user directory ID for the user ("
          + username + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   *
   * @throws SecurityException
   */
  public List<UserDirectoryType> getUserDirectoryTypes()
    throws SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectoryTypesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectoryType> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(new UserDirectoryType((UUID) rs.getObject(1), rs.getString(2), rs.getString(3),
              rs.getString(4)));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the user directory types: " + e.getMessage(),
          e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the list of users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<User> getUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
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
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored) {}
    }

    if (dataSource == null)
    {
      throw new RuntimeException("Failed to retrieve the application data source"
          + " using the JNDI names (java:app/jdbc/ApplicationDataSource) and"
          + " (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Retrieve the database meta data
      String schemaSeparator;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.DEFAULT_DATABASE_SCHEMA + schemaSeparator;

      // Build the SQL statements
      buildStatements(schemaPrefix);

      // Initialise the configuration
      initConfiguration();

      // Load the user directory types
      reloadUserDirectoryTypes();

      // Load the user directories
      reloadUserDirectories();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Security Service: " + e.getMessage(), e);
    }
  }

  /**
   * Is the user in the group for the specified organisation?
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the group uniquely identifying the group
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.isUserInGroup(username, groupName);
  }

  /**
   * Reload the user directories.
   *
   * @throws SecurityException
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
          logger.error("Failed to load the user directory (" + userDirectory.getId()
              + "): The user directory type (" + userDirectory.getTypeId() + ") was not loaded");

          continue;
        }

        try
        {
          Class<?> clazz = userDirectory.getType().getUserDirectoryClass();

          Class<? extends IUserDirectory> userDirectoryClass =
            clazz.asSubclass(IUserDirectory.class);

          if (userDirectoryClass == null)
          {
            throw new SecurityException("The user directory class ("
                + userDirectory.getType().getUserDirectoryClassName()
                + ") does not implement the IUserDirectory interface");
          }

          Constructor<? extends IUserDirectory> userDirectoryClassConstructor =
            userDirectoryClass.getConstructor(UUID.class, Map.class);

          if (userDirectoryClassConstructor == null)
          {
            throw new SecurityException("The user directory class ("
                + userDirectory.getType().getUserDirectoryClassName()
                + ") does not provide a valid constructor (long, Map<String,String>)");
          }

          IUserDirectory userDirectoryInstance =
            userDirectoryClassConstructor.newInstance(userDirectory.getId(),
              userDirectory.getParameters());

          reloadedUserDirectories.put(userDirectory.getId(), userDirectoryInstance);
        }
        catch (Throwable e)
        {
          throw new SecurityException("Failed to initialise the user directory ("
              + userDirectory.getId() + ")(" + userDirectory.getName() + ")", e);
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
   * Reload the user directory types.
   *
   * @throws SecurityException
   */
  public void reloadUserDirectoryTypes()
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
          logger.error("Failed to load the user directory type (" + userDirectoryType.getId()
              + "): Failed to retrieve the user directory class for the user directory type", e);

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

  /**
   * Remove the user from the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the group name
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.removeUserFromGroup(username, groupName);
  }

  /**
   * Rename the existing group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the group that will be renamed
   * @param newGroupName    the new name of the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  public void renameGroup(UUID userDirectoryId, String groupName, String newGroupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, ExistingGroupMembersException,
      SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(newGroupName))
    {
      throw new InvalidArgumentException("newGroupName");
    }

    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.renameGroup(groupName, newGroupName);
  }

///**
// * Set the <code>DataSource</code> for the <code>SecurityService</code>.
// *
// * @param dataSource the <code>DataSource</code> for the <code>SecurityService</code>
// */
//public void setDataSource(DataSource dataSource)
//{
//  this.dataSource = dataSource;
//}

///**
// * Set the <code>Registry</code> for the <code>SecurityService</code>.
// *
// * @param registry the <code>Registry</code> for the <code>SecurityService</code>
// */
//public void setRegistry(IRegistry registry)
//{
//  this.registry = registry;
//}

  /**
   * Does the user directory support administering groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the directory supports administering groups or <code>false</code>
   *         otherwise
   *
   * @throws UserDirectoryNotFoundException
   */
  public boolean supportsGroupAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.supportsGroupAdministration();
  }

  /**
   * Does the user directory support administering users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the directory supports administering users or <code>false</code>
   *         otherwise
   *
   * @throws UserDirectoryNotFoundException
   */
  public boolean supportsUserAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.supportsUserAdministration();
  }

  /**
   * Update the authorised function.
   *
   * @param function the function
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
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

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateFunctionSQL))
    {
      if (getFunctionId(connection, function.getCode()) == null)
      {
        throw new FunctionNotFoundException("A function with the code (" + function.getCode()
            + ") could not be found");
      }

      statement.setString(1, function.getName());
      statement.setString(2, StringUtil.notNull(function.getDescription()));
      statement.setString(3, function.getCode());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + updateFunctionSQL + ")");
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the function (" + function.getCode() + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Update the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.updateGroup(group);
  }

  /**
   * Update the organisation.
   *
   * @param organisation the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
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

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateOrganisationSQL))
    {
      if (!organisationExists(connection, organisation.getId()))
      {
        throw new OrganisationNotFoundException("An organisation with the ID ("
            + organisation.getId() + ") could not be found");
      }

      statement.setString(1, organisation.getName());
      statement.setString(2, StringUtil.notNull(organisation.getDescription()));
      statement.setObject(3, organisation.getId());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + updateOrganisationSQL + ")");
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the organisation (" + organisation.getId()
          + "): " + e.getMessage(), e);
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
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
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
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    userDirectory.updateUser(user, expirePassword, lockUser);
  }

  /**
   * Update the user directory.
   *
   * @param userDirectory the user directory
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
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

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateUserDirectorySQL))
    {
      statement.setString(1, userDirectory.getName());
      statement.setString(2, userDirectory.getDescription());
      statement.setString(3, userDirectory.getConfiguration());
      statement.setObject(4, userDirectory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + updateUserDirectorySQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the user directory (" + userDirectory.getName()
          + "): " + e.getMessage(), e);
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
   *
   * @throws SQLException
   */
  private Function buildFunctionFromResultSet(ResultSet rs)
    throws SQLException
  {
    Function function = new Function();

    function.setId((UUID) rs.getObject(1));
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
   *
   * @throws SQLException
   */
  private Organisation buildOrganisationFromResultSet(ResultSet rs)
    throws SQLException
  {
    Organisation organisation = new Organisation();
    organisation.setId((UUID) rs.getObject(1));
    organisation.setName(rs.getString(2));
    organisation.setDescription(StringUtil.notNull(rs.getString(3)));

    return organisation;
  }

  /**
   * Generate the SQL statements.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects
   *
   * @throws SQLException
   */
  private void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // addUserDirectoryToOrganisationSQL
    addUserDirectoryToOrganisationSQL = "INSERT INTO " + schemaPrefix
        + "USER_DIRECTORY_TO_ORGANISATION_MAP (USER_DIRECTORY_ID, ORGANISATION_ID) VALUES (?, ?)";

    // createFunctionSQL
    createFunctionSQL = "INSERT INTO " + schemaPrefix + "FUNCTIONS"
        + " (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // createOrganisationSQL
    createOrganisationSQL = "INSERT INTO " + schemaPrefix + "ORGANISATIONS"
        + " (ID, NAME, DESCRIPTION) VALUES (?, ?, ?)";

    // createUserDirectorySQL
    createUserDirectorySQL = "INSERT INTO " + schemaPrefix + "USER_DIRECTORIES"
        + " (ID, TYPE_ID, NAME, DESCRIPTION, CONFIGURATION) VALUES (?, ?, ?, ?, ?)";

    // deleteFunctionSQL
    deleteFunctionSQL = "DELETE FROM " + schemaPrefix + "FUNCTIONS F WHERE F.CODE=?";

    // deleteOrganisationSQL
    deleteOrganisationSQL = "DELETE FROM " + schemaPrefix + "ORGANISATIONS O" + " WHERE O.ID=?";

    // deleteUserDirectorySQL
    deleteUserDirectorySQL = "DELETE FROM " + schemaPrefix + "USER_DIRECTORIES UD WHERE UD.ID=?";

    // getFunctionIdSQL
    getFunctionIdSQL = "SELECT F.ID FROM " + schemaPrefix + "FUNCTIONS F WHERE F.CODE=?";

    // getFunctionSQL
    getFunctionSQL = "SELECT F.ID, F.CODE, F.NAME, F.DESCRIPTION FROM " + schemaPrefix
        + "FUNCTIONS F WHERE F.CODE=?";

    // getFunctionsSQL
    getFunctionsSQL = "SELECT F.ID, F.CODE, F.NAME, F.DESCRIPTION FROM " + schemaPrefix
        + "FUNCTIONS F";

    // getInternalUserDirectoryIdForUserSQL
    getInternalUserDirectoryIdForUserSQL = "SELECT IU.USER_DIRECTORY_ID FROM " + schemaPrefix
        + "INTERNAL_USERS IU WHERE UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getNumberOfOrganisationsSQL
    getNumberOfOrganisationsSQL = "SELECT COUNT(O.ID) FROM " + schemaPrefix + "ORGANISATIONS O";

    // getNumberOfUserDirectoriesSQL
    getNumberOfUserDirectoriesSQL = "SELECT COUNT(UD.ID) FROM " + schemaPrefix
        + "USER_DIRECTORIES UD";

    // getOrganisationIdsForUserDirectorySQL
    getOrganisationIdsForUserDirectorySQL = "SELECT UDTOM.ORGANISATION_ID FROM " + schemaPrefix
        + "USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM WHERE UDTOM.USER_DIRECTORY_ID=?";

    // getOrganisationsForUserDirectorySQL
    getOrganisationsForUserDirectorySQL = "SELECT O.ID, O.NAME, O.DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS O INNER JOIN " + schemaPrefix
        + "USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM ON O.ID = UDTOM.ORGANISATION_ID"
        + " WHERE UDTOM.USER_DIRECTORY_ID=?";

    // getOrganisationSQL
    getOrganisationSQL = "SELECT O.ID, O.NAME, O.DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS O WHERE O.ID=?";

    // getOrganisationsSQL
    getOrganisationsSQL = "SELECT O.ID, O.NAME, O.DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS O ORDER BY O.NAME";

    // getUserDirectoriesForOrganisationSQL
    getUserDirectoriesForOrganisationSQL = "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.DESCRIPTION,"
        + " UD.CONFIGURATION FROM " + schemaPrefix + "USER_DIRECTORIES UD INNER JOIN "
        + schemaPrefix + "USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM"
        + " ON UD.ID = UDTOM.USER_DIRECTORY_ID INNER JOIN " + schemaPrefix + "ORGANISATIONS O"
        + " ON UDTOM.ORGANISATION_ID = O.ID WHERE O.ID=?";

    // getUserDirectoriesSQL
    getUserDirectoriesSQL = "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.DESCRIPTION, UD.CONFIGURATION"
        + " FROM " + schemaPrefix + "USER_DIRECTORIES UD";

    // getUserDirectorySQL
    getUserDirectorySQL = "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.DESCRIPTION, UD.CONFIGURATION"
        + " FROM " + schemaPrefix + "USER_DIRECTORIES UD WHERE UD.ID=?";

    // getUserDirectoryTypesSQL
    getUserDirectoryTypesSQL = "SELECT UDT.ID, UDT.NAME, UDT.USER_DIRECTORY_CLASS,"
        + " UDT.ADMINISTRATION_CLASS FROM " + schemaPrefix + "USER_DIRECTORY_TYPES UDT";

    // organisationExistsSQL
    organisationExistsSQL = "SELECT COUNT(O.ID) FROM " + schemaPrefix + "ORGANISATIONS O"
        + " WHERE O.ID=?";

    // updateFunctionSQL
    updateFunctionSQL = "UPDATE " + schemaPrefix + "FUNCTIONS F"
        + " SET F.NAME=?, F.DESCRIPTION=? WHERE F.CODE=?";

    // updateOrganisationSQL
    updateOrganisationSQL = "UPDATE " + schemaPrefix + "ORGANISATIONS O"
        + " SET O.NAME=?, O.DESCRIPTION=? WHERE O.ID=?";

    // updateUserDirectorySQL
    updateUserDirectorySQL = "UPDATE " + schemaPrefix + "USER_DIRECTORIES UD"
        + " SET UD.NAME=?, UD.DESCRIPTION=?, UD.CONFIGURATION=? WHERE UD.ID=?";
  }

  /**
   * Create a new <code>UserDirectory</code> instance and populate it with the contents of the
   * current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>UserDirectory</code> instance
   *
   * @return the populated <code>UserDirectory</code> instance
   *
   * @throws SQLException
   * @throws SecurityException
   */
  private UserDirectory buildUserDirectoryFromResultSet(ResultSet rs)
    throws SQLException, SecurityException
  {
    UserDirectory userDirectory = new UserDirectory();
    userDirectory.setId((UUID) rs.getObject(1));
    userDirectory.setTypeId((UUID) rs.getObject(2));
    userDirectory.setType(userDirectoryTypes.get(rs.getObject(2)));
    userDirectory.setName(rs.getString(3));
    userDirectory.setDescription(rs.getString(4));
    userDirectory.setConfiguration(rs.getString(5));

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
   *
   * @throws SQLException
   */
  private UUID getFunctionId(Connection connection, String code)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(getFunctionIdSQL))
    {
      statement.setString(1, code);

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
   *
   * @throws SecurityException
   */
  private UUID getInternalUserDirectoryIdForUser(String username)
    throws SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getInternalUserDirectoryIdForUserSQL))
    {
      statement.setString(1, username);

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
      throw new SecurityException(
          "Failed to retrieve the ID for the internal user directory for the internal user ("
          + username + ")", e);
    }
  }

  /**
   * Initialise the configuration for the <code>SecurityService</code> instance.
   *
   * @throws SecurityException
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

    userDirectory.setId(IDGenerator.nextUUID(dataSource));
    userDirectory.setTypeId(UUID.fromString("b43fda33-d3b0-4f80-a39a-110b8e530f4f"));
    userDirectory.setName(organisation.getName() + " User Directory");
    userDirectory.setDescription(organisation.getDescription() + " User Directory");

    String buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<!DOCTYPE user-directory SYSTEM \"UserDirectoryConfiguration.dtd\">" + "<user-directory>"
      + "<parameter><name>MaxPasswordAttempts</name><value>5</value></parameter>"
      + "<parameter><name>PasswordExpiryMonths</name><value>12</value></parameter>"
      + "<parameter><name>PasswordHistoryMonths</name><value>24</value></parameter>"
      + "<parameter><name>MaxFilteredUsers</name><value>100</value></parameter>"
      + "</user-directory>";

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
   *
   * @throws SecurityException
   */
  private boolean organisationExists(Connection connection, UUID id)
    throws SecurityException
  {
    try (PreparedStatement statement = connection.prepareStatement(organisationExistsSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getInt(1) > 0);
        }
        else
        {
          return false;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to check whether the organisation (" + id + ") exists",
          e);
    }
  }
}
