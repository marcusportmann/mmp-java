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

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.application.registry.IRegistry;
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
 * The <code>SecurityService</code> class provides a Security Service implementation.
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
  private String getOrganisationIdSQL;
  private String getOrganisationSQL;
  private String getOrganisationsForUserDirectorySQL;
  private String getOrganisationsSQL;
  private String getUserDirectoriesForOrganisationSQL;
  private String getUserDirectoriesSQL;
  private String getUserDirectorySQL;
  private String getUserDirectoryTypesSQL;
  private String insertIDGeneratorSQL;

  /* Registry */
  @Inject
  private IRegistry registry;
  private String selectIDGeneratorSQL;
  private String updateFunctionSQL;
  private String updateIDGeneratorSQL;
  private String updateOrganisationSQL;
  private String updateUserDirectorySQL;
  private Map<Long, IUserDirectory> userDirectories = new ConcurrentHashMap<>();
  private Map<String, UserDirectoryType> userDirectoryTypes = new ConcurrentHashMap<>();

  /**
   * Constructs a new <code>SecurityService</code>.
   */
  public SecurityService() {}

  /**
   * Add the user to the group.
   *
   * @param userDirectoryId the unique ID for the user directory the user and group are associated
   *                        with
   * @param username        the username identifying the user
   * @param groupName       the name of the group uniquely identifying the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public void addUserToGroup(long userDirectoryId, String username, String groupName)
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
   * @param userDirectoryId      the unique ID for the user directory the user is associated with
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
  public void adminChangePassword(long userDirectoryId, String username, String newPassword,
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
   * @return the unique ID for the user directory the user is associated with
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredPasswordException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public long authenticate(String username, String password)
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
      long internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != -1)
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
        for (long userDirectoryId : userDirectories.keySet())
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
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param username        the username identifying the user
   * @param password        the password for the user that is used to authorise the operation
   * @param newPassword     the new password
   *
   * @throws UserDirectoryNotFoundException
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredPasswordException
   * @throws UserNotFoundException
   * @throws ExistingPasswordException
   * @throws SecurityException
   */
  public void changePassword(long userDirectoryId, String username, String password,
      String newPassword)
    throws UserDirectoryNotFoundException, AuthenticationFailedException, UserLockedException,
      ExpiredPasswordException, UserNotFoundException, ExistingPasswordException, SecurityException
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
      if (getFunctionId(connection, function.getCode()) != -1)
      {
        throw new DuplicateFunctionException("A function with the code (" + function.getCode()
            + ") already exists");
      }

      long functionId = nextId("Application.FunctionId");

      statement.setLong(1, functionId);
      statement.setString(2, function.getCode());
      statement.setString(3, function.getName());
      statement.setString(4, function.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + createFunctionSQL + ")");
      }

      function.setId(functionId);
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
   * @param userDirectoryId the unique ID for the user directory the group is associated with
   * @param group           the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws DuplicateGroupException
   * @throws SecurityException
   */
  public void createGroup(long userDirectoryId, Group group)
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
    if (isNullOrEmpty(organisation.getCode()))
    {
      throw new InvalidArgumentException("organisation.code");
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
        long organisationId;

        try (PreparedStatement statement = connection.prepareStatement(createOrganisationSQL))
        {
          if (getOrganisationId(connection, organisation.getCode()) != -1)
          {
            throw new DuplicateGroupException("The organisation (" + organisation.getCode()
                + ") already exists");
          }

          organisationId = nextId("Application.OrganisationId");

          statement.setLong(1, organisationId);
          statement.setString(2, organisation.getCode());
          statement.setString(3, organisation.getName());
          statement.setString(4, organisation.getDescription());

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

          long userDirectoryId;

          try (PreparedStatement statement = connection.prepareStatement(createUserDirectorySQL))
          {
            userDirectoryId = nextId("Application.UserDirectoryId");

            statement.setLong(1, userDirectoryId);
            statement.setString(2, userDirectory.getTypeId());
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

          userDirectory.setId(userDirectoryId);

          // Link the new user directory to the new organisation
          try (PreparedStatement statement =
              connection.prepareStatement(addUserDirectoryToOrganisationSQL))
          {
            statement.setLong(1, userDirectoryId);
            statement.setLong(2, organisationId);

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
          statement.setLong(1, 1);
          statement.setLong(2, organisationId);

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityException(
                "No rows were affected as a result of executing the SQL statement ("
                + addUserDirectoryToOrganisationSQL + ")");
          }
        }

        /*
         * Only update the organisation ID after the internal user directory has been successfully
         * if required.
         */
        organisation.setId(organisationId);
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
            + organisation.getCode() + ")", f);
      }

      throw new SecurityException("Failed to create the organisation (" + organisation.getCode()
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
            + organisation.getCode() + ")", e);
      }
    }
  }

  /**
   * Create a new user.
   *
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   *
   * @throws UserDirectoryNotFoundException
   * @throws DuplicateUserException
   * @throws SecurityException
   */
  public void createUser(long userDirectoryId, User user, boolean expiredPassword,
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
   * @throws DuplicateUserDirectoryException
   * @throws SecurityException
   */
  public void createUserDirectory(UserDirectory userDirectory)
    throws DuplicateUserDirectoryException, SecurityException
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
      long userDirectoryId = nextId("Application.UserDirectoryId");

      statement.setLong(1, userDirectoryId);
      statement.setString(2, userDirectory.getTypeId());
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
    catch (DuplicateUserDirectoryException e)
    {
      throw e;
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
      if (getFunctionId(connection, code) == -1)
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
   * @param userDirectoryId the unique ID for the user directory the group is associated with
   * @param groupName       the name of the group uniquely identifying the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  public void deleteGroup(long userDirectoryId, String groupName)
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
   * @param code the code uniquely identifying the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void deleteOrganisation(String code)
    throws OrganisationNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteOrganisationSQL))
    {
      if (getOrganisationId(connection, code) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + code
            + ") could not be found");
      }

      statement.setString(1, code);

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
      throw new SecurityException("Failed to delete the organisation (" + code + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param username        the username identifying the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void deleteUser(long userDirectoryId, String username)
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
   * @param userDirectoryId the unique ID for the user directory
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public void deleteUserDirectory(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteUserDirectorySQL))
    {
      statement.setLong(1, userDirectoryId);

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
      throw new SecurityException("Failed to delete the user directory (" + userDirectoryId + "): "
          + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   * @param attributes      the attribute criteria used to select the users
   *
   * @return the list of users whose attributes match the attribute criteria
   *
   * @throws UserDirectoryNotFoundException
   * @throws InvalidAttributeException
   * @throws SecurityException
   */
  public List<User> findUsers(long userDirectoryId, List<Attribute> attributes)
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

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   * @param attributes      the attribute criteria used to select the users
   * @param startPos        the position in the list of users to start from
   * @param maxResults      the maximum number of results to return or -1 for all
   *
   * @return the list of users whose attributes match the attribute criteria
   *
   * @throws UserDirectoryNotFoundException
   * @throws InvalidAttributeException
   * @throws SecurityException
   */
  public List<User> findUsersEx(long userDirectoryId, List<Attribute> attributes, int startPos,
      int maxResults)
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

    return userDirectory.findUsersEx(attributes, startPos, maxResults);
  }

  /**
   * Returns the <code>DataSource</code> for the <code>SecurityService</code>.
   *
   * @return the <code>DataSource</code> for the <code>SecurityService</code>
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Retrieve the filtered list of users.
   *
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   * @param filter          the filter to apply to the users
   *
   * @return the filtered list of users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<User> getFilteredUsers(long userDirectoryId, String filter)
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
   * @return the details for the authorised function with the specified code
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
            Function function = new Function(rs.getString(2));

            function.setId(rs.getInt(1));
            function.setName(rs.getString(3));
            function.setDescription(rs.getString(4));

            return function;
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
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param username        the username identifying the user
   *
   * @return the list of authorised function codes for the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public List<String> getFunctionCodesForUser(long userDirectoryId, String username)
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
          Function function = new Function(rs.getString(2));

          function.setId(rs.getInt(1));
          function.setName(rs.getString(3));
          function.setDescription(rs.getString(4));
          list.add(function);
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
   * @param userDirectoryId the unique ID for the user directory the group is associated with
   * @param groupName       the name of the group uniquely identifying the group
   *
   * @return the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public Group getGroup(long userDirectoryId, String groupName)
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
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param username        the username identifying the user
   *
   * @return the group names for the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public List<String> getGroupNamesForUser(long userDirectoryId, String username)
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
   * @param userDirectoryId the unique ID for the user directory the groups are associated with
   *
   * @return the list of groups
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<Group> getGroups(long userDirectoryId)
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
   * Retrieve the groups.
   *
   * @param userDirectoryId the unique ID for the user directory the groups are associated with
   * @param startPos        the position in the list of groups to start from
   * @param maxResults      the maximum number of results to return or -1 for all
   *
   * @return the list of groups
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<Group> getGroupsEx(long userDirectoryId, int startPos, int maxResults)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getGroupsEx(startPos, maxResults);
  }

  /**
   * Retrieve the groups for the user.
   *
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param username        the username identifying the user
   *
   * @return the groups for the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public List<Group> getGroupsForUser(long userDirectoryId, String username)
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
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   * @param filter          the filter to apply to the users
   *
   * @return the number of filtered users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfFilteredUsers(long userDirectoryId, String filter)
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
   * @param userDirectoryId the unique ID for the user directory the groups are associated with
   *
   * @return the number of groups
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfGroups(long userDirectoryId)
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
    catch (SecurityException e)
    {
      throw e;
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
    catch (SecurityException e)
    {
      throw e;
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
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   *
   * @return the number of users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfUsers(long userDirectoryId)
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
   * @param code the code uniquely identifying the organisation
   *
   * @return the details for the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public Organisation getOrganisation(String code)
    throws OrganisationNotFoundException, SecurityException
  {
    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getOrganisationSQL))
    {
      statement.setString(1, code);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          Organisation organisation = new Organisation(rs.getString(2));

          organisation.setId(rs.getLong(1));
          organisation.setName(rs.getString(3));
          organisation.setDescription(StringUtil.notNull(rs.getString(4)));

          return organisation;
        }
        else
        {
          throw new OrganisationNotFoundException("The organisation (" + code
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
      throw new SecurityException("Failed to retrieve the organisation (" + code + "): "
          + e.getMessage(), e);
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
          Organisation organisation = new Organisation(rs.getString(2));

          organisation.setId(rs.getLong(1));
          organisation.setName(rs.getString(3));
          organisation.setDescription(StringUtil.notNull(rs.getString(4)));

          list.add(organisation);
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
   * @param userDirectoryId the unique ID for the user directory the organisations are associated
   *                        with
   *
   * @return the organisations associated with the user directory
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<Organisation> getOrganisationsForUserDirectory(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getOrganisationsForUserDirectorySQL))
    {
      statement.setLong(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Organisation> list = new ArrayList<>();

        while (rs.next())
        {
          Organisation organisation = new Organisation(rs.getString(2));

          organisation.setId(rs.getLong(1));
          organisation.setName(rs.getString(3));
          organisation.setDescription(StringUtil.notNull(rs.getString(4)));

          list.add(organisation);
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

  /**
   * Returns the <code>Registry</code> for the <code>SecurityService</code>.
   *
   * @return the <code>Registry</code> for the <code>SecurityService</code>
   */
  public IRegistry getRegistry()
  {
    return registry;
  }

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param username        the username identifying the user
   *
   * @return the user
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public User getUser(long userDirectoryId, String username)
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
   * @param code the code uniquely identifying the organisation
   *
   * @return the user directories the organisation is associated with
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<UserDirectory> getUserDirectoriesForOrganisation(String code)
    throws OrganisationNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getUserDirectoriesForOrganisationSQL))
    {
      statement.setString(1, code);

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
          "Failed to retrieve the user directories associated with the organisation (" + code
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the unique ID for the user directory
   *
   * @return the user directory
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public UserDirectory getUserDirectory(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectorySQL))
    {
      statement.setLong(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildUserDirectoryFromResultSet(rs);
        }
        else
        {
          throw new UserDirectoryNotFoundException("The user directory (" + userDirectoryId
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
      throw new SecurityException("Failed to retrieve the user directory (" + userDirectoryId
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Retrieve the ID for the user directory that the user with the specified username is associated
   * with.
   *
   * @param username the username identifying the user
   *
   * @return the ID for the user directory that the user with the specified username is associated
   *         with or -1 if the user cannot be found
   *
   * @throws SecurityException
   */
  public long getUserDirectoryIdForUser(String username)
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
      long internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != -1)
      {
        return internalUserDirectoryId;
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if one of them can authenticate this
         * user.
         */
        for (long userDirectoryId : userDirectories.keySet())
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

        return -1;
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
   */
  public List<UserDirectoryType> getUserDirectoryTypes()
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectoryTypesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectoryType> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(new UserDirectoryType(rs.getString(1), rs.getString(2), rs.getString(3),
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
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   *
   * @return the list of users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<User> getUsers(long userDirectoryId)
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
   * Retrieve the users.
   *
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   * @param startPos        the position in the list of users to start from
   * @param maxResults      the maximum number of results to return or -1 for all
   *
   * @return the list of users
   *
   * @throws UserDirectoryNotFoundException
   * @throws SecurityException
   */
  public List<User> getUsersEx(long userDirectoryId, int startPos, int maxResults)
    throws UserDirectoryNotFoundException, SecurityException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException("The user directory ID (" + userDirectoryId
          + ") is invalid");
    }

    return userDirectory.getUsersEx(startPos, maxResults);
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
      throw new DAOException("Failed to retrieve the application data source"
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
      String schemaPrefix = DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA + schemaSeparator;

      // Build the SQL statements for the DAO
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
      throw new SecurityException("Failed to initialise the Security Service: " + e.getMessage(),
          e);
    }
  }

  /**
   * Is the user in the group for the specified organisation?
   *
   * @param userDirectoryId the unique ID for the user directory the group and user are associated
   *                        with
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
  public boolean isUserInGroup(long userDirectoryId, String username, String groupName)
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
   * Get the next unique <code>long</code> ID for the entity with the specified type.
   *
   * @param type the type of entity to retrieve the next ID for
   *
   * @return the next unique <code>long</code> ID for the entity with the specified type
   *
   * @throws SQLException
   */
  public long nextId(String type)
    throws SQLException
  {
    // Local variables
    long result;

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

      try (Connection connection = dataSource.getConnection())
      {
        try (PreparedStatement updateStatement = connection.prepareStatement(updateIDGeneratorSQL))
        {
          updateStatement.setString(1, type);

          // The following statment will block if another connection is currently
          // executing a transaction that is updating the IDGENERATOR table.
          if (updateStatement.executeUpdate() == 0)
          {
            // The row could not be found so INSERT one starting at id = 1
            try (PreparedStatement insertStatement =
                connection.prepareStatement(insertIDGeneratorSQL))
            {
              insertStatement.setLong(1, 1);
              insertStatement.setString(2, type);
              insertStatement.executeUpdate();
            }
          }
        }

        try (PreparedStatement selectStatement = connection.prepareStatement(selectIDGeneratorSQL))
        {
          selectStatement.setString(1, type);

          try (ResultSet rs = selectStatement.executeQuery())
          {
            if (rs.next())
            {
              result = rs.getLong(1);
            }
            else
            {
              throw new SQLException("No IDGenerator row found for type (" + type + ")");
            }
          }
        }
      }

      transactionManager.commit();

      return result;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving the new"
            + " ID for the entity of type (" + type + ") from the IDGENERATOR table", f);
      }

      throw new SQLException("Failed to retrieve the new ID for the entity of type (" + type
          + ") from the IDGENERATOR table", e);
    }
    finally
    {
      try
      {
        transactionManager.resume(existingTransaction);
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving the new"
            + " ID for the entity of type (" + type + ") from the IDGENERATOR table", e);
      }
    }
  }

  /**
   * Reload the user directories.
   */
  public void reloadUserDirectories()
  {
    try
    {
      Map<Long, IUserDirectory> reloadedUserDirectories = new ConcurrentHashMap<>();

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
            userDirectoryClass.getConstructor(Long.TYPE, Map.class);

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
   */
  public void reloadUserDirectoryTypes()
  {
    try
    {
      Map<String, UserDirectoryType> reloadedUserDirectoryTypes = new ConcurrentHashMap<>();

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

        try
        {
          userDirectoryType.getAdministrationClass();
        }
        catch (Throwable e)
        {
          logger.error("Failed to load the user directory type (" + userDirectoryType.getId()
              + "): Failed to retrieve the administration class for the user directory type", e);

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
   * @param userDirectoryId the unique ID for the user directory the user and group are associated
   *                        with
   * @param username        the username identifying the user
   * @param groupName       the group name
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public void removeUserFromGroup(long userDirectoryId, String username, String groupName)
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
   * @param userDirectoryId the unique ID for the user directory the group is associated with
   * @param groupName       the name of the group that will be renamed
   * @param newGroupName    the new name of the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  public void renameGroup(long userDirectoryId, String groupName, String newGroupName)
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

  /**
   * Set the <code>DataSource</code> for the <code>SecurityService</code>.
   *
   * @param dataSource the <code>DataSource</code> for the <code>SecurityService</code>
   */
  public void setDataSource(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Set the <code>Registry</code> for the <code>SecurityService</code>.
   *
   * @param registry the <code>Registry</code> for the <code>SecurityService</code>
   */
  public void setRegistry(IRegistry registry)
  {
    this.registry = registry;
  }

  /**
   * Does the user directory support administering groups.
   *
   * @param userDirectoryId the unique ID for the user directory
   *
   * @return <code>true</code> if the directory supports administering groups or <code>false</code>
   *         otherwise
   *
   * @throws UserDirectoryNotFoundException
   */
  public boolean supportsGroupAdministration(long userDirectoryId)
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
   * @param userDirectoryId the unique ID for the user directory
   *
   * @return <code>true</code> if the directory supports administering users or <code>false</code>
   *         otherwise
   *
   * @throws UserDirectoryNotFoundException
   */
  public boolean supportsUserAdministration(long userDirectoryId)
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
      if (getFunctionId(connection, function.getCode()) == -1)
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
   * @param userDirectoryId the unique ID for the user directory the group is associated with
   * @param group           the group
   *
   * @throws UserDirectoryNotFoundException
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public void updateGroup(long userDirectoryId, Group group)
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
    if (isNullOrEmpty(organisation.getCode()))
    {
      throw new InvalidArgumentException("organisation.code");
    }

    if (isNullOrEmpty(organisation.getName()))
    {
      throw new InvalidArgumentException("organisation.name");
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateOrganisationSQL))
    {
      if (getOrganisationId(connection, organisation.getCode()) == -1)
      {
        throw new FunctionNotFoundException("An organisation with the code ("
            + organisation.getCode() + ") could not be found");
      }

      statement.setString(1, organisation.getName());
      statement.setString(2, StringUtil.notNull(organisation.getDescription()));
      statement.setString(3, organisation.getCode());

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
      throw new SecurityException("Failed to update the organisation (" + organisation.getCode()
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Update the user.
   *
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param user            the user
   * @param expirePassword  expire the user's password as part of the update
   * @param lockUser        lock the user as part of the update
   *
   * @throws UserDirectoryNotFoundException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void updateUser(long userDirectoryId, User user, boolean expirePassword, boolean lockUser)
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

      statement.setLong(4, userDirectory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement ("
            + updateUserDirectorySQL + ")");
      }
    }
    catch (DuplicateUserDirectoryException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the user directory (" + userDirectory.getName()
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Generate the SQL statements for the <code>SecurityService</code>.
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
        + " (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // createUserDirectorySQL
    createUserDirectorySQL = "INSERT INTO " + schemaPrefix + "USER_DIRECTORIES"
        + " (ID, TYPE_ID, NAME, DESCRIPTION, CONFIGURATION) VALUES (?, ?, ?, ?, ?)";

    // deleteFunctionSQL
    deleteFunctionSQL = "DELETE FROM " + schemaPrefix + "FUNCTIONS F WHERE F.CODE=?";

    // deleteOrganisationSQL
    deleteOrganisationSQL = "DELETE FROM " + schemaPrefix + "ORGANISATIONS O"
        + " WHERE UPPER(O.CODE)=UPPER(CAST(? AS VARCHAR(100)))";

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

    // getOrganisationsForUserDirectorySQL
    getOrganisationsForUserDirectorySQL = "SELECT O.ID, O.CODE, O.NAME, O.DESCRIPTION FROM "
        + schemaPrefix + "ORGANISATIONS O INNER JOIN " + schemaPrefix
        + "USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM ON O.ID = UDTOM.ORGANISATION_ID"
        + " WHERE UDTOM.USER_DIRECTORY_ID=?";

    // getOrganisationIdSQL
    getOrganisationIdSQL = "SELECT O.ID FROM " + schemaPrefix + "ORGANISATIONS O"
        + " WHERE UPPER(O.CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // getOrganisationSQL
    getOrganisationSQL = "SELECT O.ID, O.CODE, O.NAME, O.DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS O WHERE UPPER(O.CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // getOrganisationsSQL
    getOrganisationsSQL = "SELECT O.ID, O.CODE, O.NAME, O.DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS O ORDER BY NAME";

    // getUserDirectoriesForOrganisationSQL
    getUserDirectoriesForOrganisationSQL = "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.DESCRIPTION,"
        + " UD.CONFIGURATION FROM " + schemaPrefix + "USER_DIRECTORIES UD INNER JOIN "
        + schemaPrefix + "USER_DIRECTORY_TO_ORGANISATION_MAP UDTOM"
        + " ON UD.ID = UDTOM.USER_DIRECTORY_ID INNER JOIN " + schemaPrefix + "ORGANISATIONS O"
        + " ON UDTOM.ORGANISATION_ID = O.ID WHERE O.CODE=?";

    // getUserDirectoriesSQL
    getUserDirectoriesSQL = "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.DESCRIPTION, UD.CONFIGURATION"
        + " FROM " + schemaPrefix + "USER_DIRECTORIES UD";

    // getUserDirectorySQL
    getUserDirectorySQL = "SELECT UD.ID, UD.TYPE_ID, UD.NAME, UD.DESCRIPTION, UD.CONFIGURATION"
        + " FROM " + schemaPrefix + "USER_DIRECTORIES UD WHERE UD.ID=?";

    // getUserDirectoryTypesSQL
    getUserDirectoryTypesSQL = "SELECT UDT.ID, UDT.NAME, UDT.USER_DIRECTORY_CLASS,"
        + " UDT.ADMINISTRATION_CLASS FROM " + schemaPrefix + "USER_DIRECTORY_TYPES UDT";

    // insertIDGeneratorSQL
    insertIDGeneratorSQL = "INSERT INTO " + schemaPrefix + "IDGENERATOR"
        + " (CURRENT, NAME) VALUES (?, ?)";

    // selectIDGeneratorSQL
    selectIDGeneratorSQL = "SELECT I.CURRENT FROM " + schemaPrefix + "IDGENERATOR I WHERE I.NAME=?";

    // updateFunctionSQL
    updateFunctionSQL = "UPDATE " + schemaPrefix + "FUNCTIONS F"
        + " SET F.NAME=?, F.DESCRIPTION=? WHERE F.CODE=?";

    // updateIDGeneratorSQL
    updateIDGeneratorSQL = "UPDATE " + schemaPrefix + "IDGENERATOR I"
        + " SET I.CURRENT = I.CURRENT + 1 WHERE I.NAME=?";

    // updateOrganisationSQL
    updateOrganisationSQL = "UPDATE " + schemaPrefix + "ORGANISATIONS O"
        + " SET O.NAME=?, O.DESCRIPTION=? WHERE O.CODE=?";

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
   */
  private UserDirectory buildUserDirectoryFromResultSet(ResultSet rs)
    throws SQLException
  {
    UserDirectory userDirectory = new UserDirectory();
    userDirectory.setId(rs.getLong(1));
    userDirectory.setTypeId(rs.getString(2));
    userDirectory.setType(userDirectoryTypes.get(rs.getString(2)));
    userDirectory.setName(rs.getString(3));
    userDirectory.setDescription(rs.getString(4));
    userDirectory.setConfiguration(rs.getString(5));

    return userDirectory;
  }

  /**
   * Returns the numeric ID for the function with the specified code.
   *
   * @param connection the existing database connection to use
   * @param code       the code uniquely identifying the function
   *
   * @return the numeric ID for the function or -1 if a function with the specified name could not
   *         be found
   *
   * @throws SQLException
   */
  private long getFunctionId(Connection connection, String code)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(getFunctionIdSQL))
    {
      statement.setString(1, code);

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
  }

  /**
   * Returns the numeric ID for the internal user directory the internal user with the specified
   * username is associated with.
   *
   * @param username the username uniquely identifying the internal user
   *
   * @return the numeric ID for the internal user directory the internal user with the specified
   *         username is associated with or -1 if an internal user with the specified username
   *         could not be found
   *
   * @throws SecurityException
   */
  private long getInternalUserDirectoryIdForUser(String username)
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
      throw new SecurityException("Failed to retrieve the numeric ID for the internal user ("
          + username + ")", e);
    }
  }

  /**
   * Returns the numeric ID for the organisation with the specified code.
   *
   * @param connection the existing database connection to use
   * @param code       the code uniquely identifying the organisation
   *
   * @return the numeric ID for the organisation or -1 if an organisation with the specified code
   *         could not be found
   *
   * @throws SecurityException
   */
  private long getOrganisationId(Connection connection, String code)
    throws SecurityException
  {
    try (PreparedStatement statement = connection.prepareStatement(getOrganisationIdSQL))
    {
      statement.setString(1, code);

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
      throw new SecurityException("Failed to retrieve the numeric ID for the organisation (" + code
          + ")", e);
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
  {
    UserDirectory userDirectory = new UserDirectory();

    userDirectory.setTypeId("b43fda33-d3b0-4f80-a39a-110b8e530f4f");
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
}
