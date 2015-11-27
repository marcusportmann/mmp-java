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
  private String createFunctionSQL;
  private String createOrganisationSQL;
  private DataSource dataSource;
  private String deleteFunctionSQL;
  private String deleteOrganisationSQL;
  private String getFunctionIdSQL;
  private String getFunctionSQL;
  private String getFunctionsSQL;
  private String getInternalUserDirectoryIdForUserSQL;
  private String getNumberOfOrganisationsSQL;
  private String getOrganisationIdSQL;
  private String getOrganisationSQL;
  private String getOrganisationsSQL;
  private String insertIDGeneratorSQL;

  /* Registry */
  @Inject
  private IRegistry registry;
  private String selectIDGeneratorSQL;
  private String updateFunctionSQL;
  private String updateIDGeneratorSQL;
  private String updateOrganisationSQL;
  private Map<Long, IUserDirectory> userDirectories = new ConcurrentHashMap<>();

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

      }
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

    userDirectory.createGroup(group);
  }

  /**
   * Create a new organisation.
   *
   * @param organisation the organisation
   *
   * @throws DuplicateOrganisationException
   * @throws SecurityException
   */
  public void createOrganisation(Organisation organisation)
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

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createOrganisationSQL))
    {
      if (getOrganisationId(connection, organisation.getCode()) != -1)
      {
        throw new DuplicateGroupException("The organisation (" + organisation.getCode()
            + ") already exists");
      }

      long organisationId = nextId("Application.OrganisationId");

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

      organisation.setId(organisationId);
    }
    catch (DuplicateOrganisationException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to create the organisation (" + organisation.getCode()
          + "): " + e.getMessage(), e);
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

    userDirectory.createUser(user, expiredPassword, userLocked);
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

    userDirectory.deleteUser(username);
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

    return userDirectory.findUsersEx(attributes, startPos, maxResults);
  }

///**
// * Retrieve the authorised function codes for the user for the specified organisation.
// * <p/>
// * This list will include all of the authorised functions that the user is associated with as a
// * result of being a member of one or more groups that have also been linked to authorised
// * functions.
// *
// * @param username     the username identifying the user
// * @param organisation the code uniquely identifying the organisation
// * @param origin       the origin of the request e.g. the IP address, subnetwork or
// *                     workstation name for the remote client
// *
// * @return the list of authorised function codes for the user for the specified organisation
// *
// * @throws UserNotFoundException
// * @throws OrganisationNotFoundException
// * @throws SecurityException
// */
//public List<String> getAllFunctionCodesForUser(String username, String organisation,
//    String origin)
//  throws UserNotFoundException, OrganisationNotFoundException, SecurityException
//{
//  // Validate parameters
//  if (isNullOrEmpty(username))
//  {
//    throw new InvalidArgumentException("username");
//  }
//
//  if (isNullOrEmpty(organisation))
//  {
//    throw new InvalidArgumentException("organisation");
//  }
//
//  if (isNullOrEmpty(origin))
//  {
//    throw new InvalidArgumentException("origin");
//  }
//
//  try (Connection connection = dataSource.getConnection();
//    PreparedStatement statement = connection.prepareStatement(getAllFunctionCodesForUserSQL))
//  {
//    // Get the ID of the user with the specified username
//    long userId;
//
//    if ((userId = getUserId(connection, username)) == -1)
//    {
//      throw new UserNotFoundException("The user (" + username + ") could not be found");
//    }
//
//    // Get the ID of the organisation with the specified code
//    long organisationId;
//
//    if ((organisationId = getOrganisationId(connection, organisation)) == -1)
//    {
//      throw new OrganisationNotFoundException("The organisation (" + organisation
//          + ") could not be found");
//    }
//
//    statement.setLong(1, userId);
//    statement.setLong(2, organisationId);
//
//    List<String> list = new ArrayList<>();
//
//    try (ResultSet rs = statement.executeQuery())
//    {
//      while (rs.next())
//      {
//        list.add(rs.getString(1));
//      }
//    }
//
//    // Add the function codes assigned to the user directly
//    getFunctionCodesForUserId(connection, userId, organisationId, list);
//
//    return list;
//  }
//  catch (UserNotFoundException | OrganisationNotFoundException e)
//  {
//    throw e;
//  }
//  catch (Throwable e)
//  {
//    throw new SecurityException("Failed to retrieve all the function codes for the user ("
//        + username + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
//  }
//}

///**
// * Retrieve the list of authorised functions for the user for the specified organisation.
// * <p/>
// * This list will include all of the authorised functions that the user is associated with as a
// * result of being a member of one or more groups that have also been linked to authorised
// * functions.
// *
// * @param username     the username identifying the user
// * @param organisation the code uniquely identifying the organisation
// * @param origin       the origin of the request e.g. the IP address, subnetwork or
// *                     workstation name for the remote client
// *
// * @return the list of authorised functions for the user for the specified organisation
// *
// * @throws UserNotFoundException
// * @throws OrganisationNotFoundException
// * @throws SecurityException
// */
//public List<Function> getAllFunctionsForUser(String username, String organisation, String origin)
//  throws UserNotFoundException, OrganisationNotFoundException, SecurityException
//{
//  if (isNullOrEmpty(username))
//  {
//    throw new InvalidArgumentException("username");
//  }
//
//  if (isNullOrEmpty(organisation))
//  {
//    throw new InvalidArgumentException("organisation");
//  }
//
//  if (isNullOrEmpty(origin))
//  {
//    throw new InvalidArgumentException("origin");
//  }
//
//  throw new SecurityException("TODO: IMPLEMENT ME");
//}

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
    // Validate parameters
    if (isNullOrEmpty(filter))
    {
      throw new InvalidArgumentException("filter");
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

///**
// * Retrieve the list of authorised function codes for the group.
// *
// * @param groupName the name of the group uniquely identifying the group
// * @param origin    the origin of the request e.g. the IP address, subnetwork or workstation name
// *                  for the remote client
// *
// * @return the list of authorised function codes for the group
// *
// * @throws GroupNotFoundException
// * @throws SecurityException
// */
//public List<String> getFunctionCodesForGroup(String groupName, String origin)
//  throws GroupNotFoundException, SecurityException
//{
//  // Validate parameters
//  if (isNullOrEmpty(groupName))
//  {
//    throw new InvalidArgumentException("groupName");
//  }
//
//  if (isNullOrEmpty(origin))
//  {
//    throw new InvalidArgumentException("origin");
//  }
//
//  try
//  {
//    try (Connection connection = dataSource.getConnection();
//      PreparedStatement statement = connection.prepareStatement(getFunctionCodesForGroupSQL))
//    {
//      // Get the ID of the group with the specified group name
//      long groupId;
//
//      if ((groupId = getGroupId(connection, groupName)) == -1)
//      {
//        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
//      }
//
//      statement.setLong(1, groupId);
//
//      try (ResultSet rs = statement.executeQuery())
//      {
//        List<String> list = new ArrayList<>();
//
//        while (rs.next())
//        {
//          list.add(rs.getString(1));
//        }
//
//        return list;
//      }
//    }
//  }
//  catch (GroupNotFoundException e)
//  {
//    throw e;
//  }
//  catch (Throwable e)
//  {
//    throw new SecurityException("Failed to retrieve the function codes for the group ("
//        + groupName + "): " + e.getMessage(), e);
//  }
//}

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
    // Validate parameters
    if (isNullOrEmpty(filter))
    {
      throw new InvalidArgumentException("filter");
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

    return userDirectory.getUser(username);
  }

///**
// * Retrieve the unique numeric ID for the user.
// *
// * @param userDirectoryId the unique ID for the user directory the user is associated with
// * @param username        the username identifying the user
// *
// * @return the unique numeric ID for the user
// *
// * @throws UserDirectoryNotFoundException
// * @throws UserNotFoundException
// * @throws SecurityException
// */
//public long getUserId(long userDirectoryId, String username)
//  throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
//{
//  // Validate parameters
//  if (isNullOrEmpty(username))
//  {
//    throw new InvalidArgumentException("username");
//  }
//
//  return userDirectory.getUserId(username);
//}

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
      userDirectory = new InternalUserDirectory(1, new HashMap<>());

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

    userDirectory.updateUser(user, expirePassword, lockUser);
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
    // createFunctionSQL
    createFunctionSQL = "INSERT INTO " + schemaPrefix + "FUNCTIONS"
        + " (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // createOrganisationSQL
    createOrganisationSQL = "INSERT INTO " + schemaPrefix + "ORGANISATIONS"
        + " (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // deleteFunctionSQL
    deleteFunctionSQL = "DELETE FROM " + schemaPrefix + "FUNCTIONS WHERE CODE=?";

    // deleteOrganisationSQL
    deleteOrganisationSQL = "DELETE FROM " + schemaPrefix + "ORGANISATIONS"
        + " WHERE UPPER(CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // getFunctionIdSQL
    getFunctionIdSQL = "SELECT ID FROM " + schemaPrefix + "FUNCTIONS WHERE CODE=?";

    // getFunctionSQL
    getFunctionSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "FUNCTIONS WHERE CODE=?";

    // getFunctionsSQL
    getFunctionsSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix + "FUNCTIONS";

    // getInternalUserDirectoryIdForUserSQL
    getInternalUserDirectoryIdForUserSQL = "SELECT IU.USER_DIRECTORY_ID FROM " + schemaPrefix
        + "INTERNAL_USERS IU WHERE UPPER(IU.USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getNumberOfOrganisationsSQL
    getNumberOfOrganisationsSQL = "SELECT COUNT(A.ID) FROM " + schemaPrefix + "ORGANISATIONS A";

    // getOrganisationIdSQL
    getOrganisationIdSQL = "SELECT ID FROM " + schemaPrefix + "ORGANISATIONS"
        + " WHERE UPPER(CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // getOrganisationSQL
    getOrganisationSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS WHERE UPPER(CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // getOrganisationsSQL
    getOrganisationsSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS ORDER BY NAME";

    // insertIDGeneratorSQL
    insertIDGeneratorSQL = "INSERT INTO " + schemaPrefix + "IDGENERATOR"
        + " (CURRENT, NAME) VALUES (?, ?)";

    // selectIDGeneratorSQL
    selectIDGeneratorSQL = "SELECT CURRENT FROM " + schemaPrefix + "IDGENERATOR" + " WHERE NAME=?";

    // updateFunctionSQL
    updateFunctionSQL = "UPDATE " + schemaPrefix + "FUNCTIONS"
        + " SET NAME=?, DESCRIPTION=? WHERE CODE=?";

    // updateIDGeneratorSQL
    updateIDGeneratorSQL = "UPDATE " + schemaPrefix + "IDGENERATOR"
        + " SET CURRENT = CURRENT + 1 WHERE NAME=?";

    // updateOrganisationSQL
    updateOrganisationSQL = "UPDATE " + schemaPrefix + "ORGANISATIONS"
        + " SET NAME=?, DESCRIPTION=? WHERE CODE=?";
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
}
