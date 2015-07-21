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
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.TransactionManager;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.security.MessageDigest;

import java.sql.*;

import java.util.*;
import java.util.Date;

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
  /**
   * The default number of failed password attempts before the user is locked.
   */
  public static final int DEFAULT_MAX_PASSWORD_ATTEMPTS = 5;

  /**
   * The default number of months before a user's password expires.
   */
  public static final int DEFAULT_PASSWORD_EXPIRY_MONTHS = 12;

  /**
   * The default number of months to check password history against.
   */
  public static final int DEFAULT_PASSWORD_HISTORY_MONTHS = 12;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
  private String addFunctionToTemplateSQL;
  private String addUserToGroupSQL;
  private String addUserToOrganisationSQL;
  private String changePasswordSQL;
  private String createFunctionSQL;
  private String createFunctionTemplateSQL;
  private String createGroupSQL;
  private String createOrganisationSQL;
  private String createUserSQL;
  private DataSource dataSource;
  private String databaseCatalogSeparator = ".";
  private String deleteFunctionSQL;
  private String deleteFunctionTemplateSQL;
  private String deleteGroupSQL;
  private String deleteOrganisationSQL;
  private String deleteUserSQL;
  private String getAllFunctionCodesForUserSQL;
  private String getFilteredUsersForOrganisationSQL;
  private String getFunctionCodesForGroupSQL;
  private String getFunctionCodesForUserSQL;
  private String getFunctionIdSQL;
  private String getFunctionSQL;
  private String getFunctionTemplateIdSQL;
  private String getFunctionTemplateSQL;
  private String getFunctionTemplatesSQL;
  private String getFunctionsForGroupSQL;
  private String getFunctionsForTemplateSQL;
  private String getFunctionsForUserSQL;
  private String getFunctionsSQL;
  private String getGroupIdSQL;
  private String getGroupNamesForUserSQL;
  private String getGroupSQL;
  private String getGroupsForUserSQL;
  private String getGroupsSQL;
  private String getNumberOfFilteredUsersForOrganisationSQL;
  private String getNumberOfGroupsSQL;
  private String getNumberOfUsersForOrganisationSQL;
  private String getOrganisationIdSQL;
  private String getOrganisationSQL;
  private String getOrganisationsForUserSQL;
  private String getOrganisationsSQL;
  private String getUserIdSQL;
  private String getUserIdsForGroupSQL;
  private String getUserSQL;
  private String getUsersForOrganisationSQL;
  private String getUsersSQL;
  private String grantFunctionToGroupSQL;
  private String grantFunctionToUserSQL;
  private String insertIDGeneratorSQL;
  private String isGroupGrantedFunctionSQL;
  private String isPasswordInHistorySQL;
  private String isUserAssociatedWithOrganisationSQL;
  private String isUserGrantedFunctionSQL;
  private String isUserInGroupSQL;
  private int maxPasswordAttempts;
  private int passwordExpiryMonths;
  private int passwordHistoryMonths;

  /* Registry */
  @Inject
  private IRegistry registry;
  private String removeFunctionFromTemplateSQL;
  private String removeUserFromGroupSQL;
  private String removeUserFromOrganisationSQL;
  private String revokeFunctionForGroupSQL;
  private String revokeFunctionForUserSQL;
  private String savePasswordHistorySQL;
  private String selectIDGeneratorSQL;
  private String updateFunctionSQL;
  private String updateFunctionTemplateSQL;
  private String updateGroupSQL;
  private String updateIDGeneratorSQL;
  private String updateOrganisationSQL;

  /**
   * Constructs a new <code>SecurityService</code>.
   */
  public SecurityService() {}

  /**
   * Add the authorised function with the specified code to the authorised function template given
   * by the specified code.
   *
   * @param functionCode the code identifying the authorised function
   * @param templateCode the code identifying the authorised function template
   * @param origin       the origin of the request e.g. the IP address, subnetwork or workstation
   *                     name for the remote client
   *
   * @throws DuplicateFunctionException
   * @throws FunctionNotFoundException
   * @throws FunctionTemplateNotFoundException
   * @throws SecurityException
   */
  public void addFunctionToTemplate(String functionCode, String templateCode, String origin)
    throws DuplicateFunctionException, FunctionNotFoundException,
      FunctionTemplateNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(functionCode))
    {
      throw new InvalidArgumentException("functionCode");
    }

    if (isNullOrEmpty(templateCode))
    {
      throw new InvalidArgumentException("templateCode");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the function with the specified code
      long functionId;

      if ((functionId = getFunctionId(connection, functionCode)) == -1)
      {
        throw new FunctionNotFoundException("A function with the code (" + functionCode
            + ") could not be found");
      }

      // Get the ID of the function template with the specified code
      long templateId;

      if ((templateId = getFunctionTemplateId(connection, templateCode)) == -1)
      {
        throw new FunctionTemplateNotFoundException("A function template with the code ("
            + templateCode + ") could not be found");
      }

      // Get the current list of functions for the function template
      List<Function> functions = getFunctionsForTemplate(templateId);

      for (Function function : functions)
      {
        if (function.getCode().equals(functionCode))
        {
          // The function has already been added to the function template so do nothing
          return;
        }
      }

      // Add the function to the template
      statement = connection.prepareStatement(addFunctionToTemplateSQL);
      statement.setLong(1, functionId);
      statement.setLong(2, templateId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to add the function (" + functionCode
            + ") to the function template (" + templateCode
            + "): No rows were affected as a result of executing the SQL statement ("
            + addFunctionToTemplateSQL + ")");
      }
    }
    catch (DuplicateFunctionException | FunctionNotFoundException
        | FunctionTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to add the function (" + functionCode
          + ") to the function template (" + templateCode + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Add the user to the group for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param groupName    the name of the group uniquely identifying the group
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void addUserToGroup(String username, String groupName, String organisation, String origin)
    throws UserNotFoundException, GroupNotFoundException, OrganisationNotFoundException,
      SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the group with the specified group name
      long groupId;

      if ((groupId = getGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Check if the user has already been added to the group for the specified organisation
      if (isUserInGroup(connection, userId, groupId, organisationId))
      {
        // The user is already a member of the specified group do nothing
        return;
      }

      // Add the user to the group
      statement = connection.prepareStatement(addUserToGroupSQL);
      statement.setLong(1, userId);
      statement.setLong(2, groupId);
      statement.setLong(3, organisationId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to add the user (" + username + ") to the group ("
            + groupName + ") for the organisation (" + organisation
            + "): No rows were affected as a result of executing the SQL statement ("
            + addUserToGroupSQL + ")");
      }
    }
    catch (UserNotFoundException | GroupNotFoundException | OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to add the user (" + username + ") to the group ("
          + groupName + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Add the user to the specified organisation.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void addUserToOrganisation(String username, String organisation, String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Check if the user has already been associated with the specified organisation
      if (isUserAssociatedWithOrganisation(connection, userId, organisationId))
      {
        // The user is already associated with the specified organisation
        return;
      }

      // Add the user to the organisation
      statement = connection.prepareStatement(addUserToOrganisationSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to add the user (" + username
            + ") to the organisation (" + organisation
            + "): No rows were affected as a result of executing the SQL statement ("
            + addUserToOrganisationSQL + ")");
      }
    }
    catch (UserNotFoundException | OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to add the user (" + username + ") to the organisation ("
          + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
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
   * @param origin               the origin of the request e.g. the IP address, subnetwork or
   *                             workstation name for the remote client
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason, String origin)
    throws UserNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (newPassword == null)
    {
      throw new InvalidArgumentException("newPassword");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      String passwordHash = createPasswordHash(newPassword);

      statement = connection.prepareStatement(changePasswordSQL);
      statement.setString(1, passwordHash);

      if (lockUser)
      {
        statement.setInt(2, -1);
      }
      else
      {
        if (!isNullOrEmpty(user.getPasswordAttempts()))
        {
          statement.setInt(2, 0);
        }
        else
        {
          statement.setNull(2, java.sql.Types.INTEGER);
        }
      }

      if (expirePassword)
      {
        statement.setTimestamp(3, new Timestamp(System.currentTimeMillis() - 1000L));
      }
      else
      {
        if (user.getPasswordExpiry() != null)
        {
          Calendar calendar = Calendar.getInstance();

          calendar.setTime(new Date());
          calendar.add(Calendar.MONTH, passwordExpiryMonths);
          statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
        }
        else
        {
          statement.setTimestamp(3, null);
        }
      }

      statement.setString(4, username);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to administratively change the password for the user ("
            + username + "): No rows were affected as a result of executing the SQL statement ("
            + changePasswordSQL + ")");
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
          + username + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Authenticate a user using credentials other than a simple password.
   *
   * @param username    the username identifying the user
   * @param credentials the credentials used to authenticate
   * @param origin      the origin of the request e.g. the IP address, subnetwork or workstation
   *                    name for the remote client
   *
   * @return the results of authenticating the user successfully
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredCredentialsException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public Map<String, Object> authenticate(String username, List<Credential> credentials,
      String origin)
    throws AuthenticationFailedException, UserLockedException, ExpiredCredentialsException,
      UserNotFoundException, SecurityException
  {
    throw new AuthenticationFailedException("Not implemented");
  }

  /**
   * Authenticate.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @return the results of authenticating the user successfully
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredPasswordException
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public Map<String, Object> authenticate(String username, String password, String origin)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
      UserNotFoundException, SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (password == null)
    {
      throw new InvalidArgumentException("password");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      if (user.getPasswordAttempts() != null)
      {
        if ((user.getPasswordAttempts() == -1)
            || (user.getPasswordAttempts() > maxPasswordAttempts))
        {
          throw new UserLockedException("The user (" + username
              + ") has exceeded the number of failed password attempts and has been locked");
        }
      }

      if (user.getPasswordExpiry() != null)
      {
        if (user.getPasswordExpiry().before(new Date()))
        {
          throw new ExpiredPasswordException("The password for the user (" + username
              + ") has expired");
        }
      }

      if (!user.getPassword().equals(createPasswordHash(password)))
      {
        throw new AuthenticationFailedException("Authentication failed for the user (" + username
            + ")");
      }

      return new HashMap<>();
    }
    catch (AuthenticationFailedException e)
    {
      throw e;
    }
    catch (UserLockedException e)
    {
      throw e;
    }
    catch (ExpiredPasswordException e)
    {
      throw e;
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to authenticate the user (" + username + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   * @param origin      the origin of the request e.g. the IP address, subnetwork or workstation
   *                    name for the remote client
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredPasswordException
   * @throws UserNotFoundException
   * @throws ExistingPasswordException
   * @throws SecurityException
   */
  public void changePassword(String username, String password, String newPassword, String origin)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
      UserNotFoundException, ExistingPasswordException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

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

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      if (user.getPasswordAttempts() != null)
      {
        if ((user.getPasswordAttempts() == -1)
            || (user.getPasswordAttempts() > maxPasswordAttempts))
        {
          throw new UserLockedException("The user (" + username
              + ") has exceeded the number of failed password attempts and has been locked");
        }
      }

      if (user.getPasswordExpiry() != null)
      {
        if (user.getPasswordExpiry().before(new Date()))
        {
          throw new ExpiredPasswordException("The password for the user (" + username
              + ") has expired");
        }
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

      statement = connection.prepareStatement(changePasswordSQL);
      statement.setString(1, newPasswordHash);

      if (!isNullOrEmpty(user.getPasswordAttempts()))
      {
        statement.setInt(2, 0);
      }
      else
      {
        statement.setNull(2, java.sql.Types.INTEGER);
      }

      if (user.getPasswordExpiry() != null)
      {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, passwordExpiryMonths);
        statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
      }
      else
      {
        statement.setTimestamp(3, null);
      }

      statement.setString(4, username);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to change the password for the user (" + username
            + "): No rows were affected as a result of executing the SQL statement ("
            + changePasswordSQL + ")");
      }

      savePasswordHistory(connection, user.getId(), newPasswordHash);
    }
    catch (AuthenticationFailedException e)
    {
      throw e;
    }
    catch (UserLockedException e)
    {
      throw e;
    }
    catch (ExpiredPasswordException e)
    {
      throw e;
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (ExistingPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to change the password for the user (" + username + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create a new authorised function.
   *
   * @param function the <code>Function</code> instance containing the information for the new
   *                 authorised function
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @throws DuplicateFunctionException
   * @throws SecurityException
   */
  public void createFunction(Function function, String origin)
    throws DuplicateFunctionException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(function.getCode()))
    {
      throw new InvalidArgumentException("function.code");
    }

    if (isNullOrEmpty(function.getName()))
    {
      throw new InvalidArgumentException("function.name");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getFunctionId(connection, function.getCode()) != -1)
      {
        throw new DuplicateFunctionException("A function with the code (" + function.getCode()
            + ") already exists");
      }

      long functionId = nextId("Application.FunctionId");

      statement = connection.prepareStatement(createFunctionSQL);
      statement.setLong(1, functionId);
      statement.setString(2, function.getCode());
      statement.setString(3, function.getName());
      statement.setString(4, function.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to create the function (" + function.getCode()
            + "): No rows were affected as a result of executing the SQL statement ("
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
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create a new authorised function template.
   *
   * @param template the <code>FunctionTemplate</code> instance containing the information for the
   *                 new authorised function template
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @throws DuplicateFunctionTemplateException
   * @throws SecurityException
   */
  public void createFunctionTemplate(FunctionTemplate template, String origin)
    throws DuplicateFunctionTemplateException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(template.getCode()))
    {
      throw new InvalidArgumentException("template.code");
    }

    if (isNullOrEmpty(template.getName()))
    {
      throw new InvalidArgumentException("template.name");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getFunctionTemplateId(connection, template.getCode()) != -1)
      {
        throw new DuplicateFunctionTemplateException("A function template with the code ("
            + template.getCode() + ") already exists");
      }

      long templateId = nextId("Application.FunctionTemplateId");

      statement = connection.prepareStatement(createFunctionTemplateSQL);
      statement.setLong(1, templateId);
      statement.setString(2, template.getCode());
      statement.setString(3, template.getName());
      statement.setString(4, template.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to create the function template (" + template.getCode()
            + "): No rows were affected as a result of executing the SQL statement ("
            + createFunctionTemplateSQL + ")");
      }

      template.setId(templateId);
    }
    catch (DuplicateFunctionTemplateException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to create the function template (" + template.getCode()
          + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create a new group.
   *
   * @param group  the <code>Group</code> instance containing the information for the new group
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @throws DuplicateGroupException
   * @throws SecurityException
   */
  public void createGroup(Group group, String origin)
    throws DuplicateGroupException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(group.getGroupName()))
    {
      throw new InvalidArgumentException("group.groupName");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getGroupId(connection, group.getGroupName()) != -1)
      {
        throw new DuplicateGroupException("The group (" + group.getGroupName()
            + ") already exists");
      }

      long groupId = nextId("Application.GroupId");

      statement = connection.prepareStatement(createGroupSQL);
      statement.setLong(1, groupId);
      statement.setString(2, group.getGroupName());
      statement.setString(3, group.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to create the group (" + group.getGroupName()
            + "): No rows were affected as a result of executing the SQL statement ("
            + createGroupSQL + ")");
      }

      group.setId(groupId);
    }
    catch (DuplicateGroupException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to create the group (" + group.getGroupName() + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create a new organisation.
   *
   * @param organisation the <code>Organisation</code> instance containing the information for the
   *                     new organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or workstation
   *                     name for the remote client
   *
   * @throws DuplicateOrganisationException
   * @throws SecurityException
   */
  public void createOrganisation(Organisation organisation, String origin)
    throws DuplicateOrganisationException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(organisation.getCode()))
    {
      throw new InvalidArgumentException("organisation.code");
    }

    if (isNullOrEmpty(organisation.getName()))
    {
      throw new InvalidArgumentException("organisation.name");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getOrganisationId(connection, organisation.getCode()) != -1)
      {
        throw new DuplicateGroupException("The organisation (" + organisation.getCode()
            + ") already exists");
      }

      long organisationId = nextId("Application.OrganisationId");

      statement = connection.prepareStatement(createOrganisationSQL);
      statement.setLong(1, organisationId);
      statement.setString(2, organisation.getCode());
      statement.setString(3, organisation.getName());
      statement.setString(4, organisation.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to create the organisation (" + organisation.getCode()
            + "): No rows were affected as a result of executing the SQL statement ("
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
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create a new user.
   *
   * @param user            the <code>User</code> instance containing the information for the new
   *                        user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   * @param origin          the origin of the request e.g. the IP address, subnetwork or
   *                        workstation name for the remote client
   *
   * @throws DuplicateUserException
   * @throws SecurityException
   */
  public void createUser(User user, boolean expiredPassword, boolean userLocked, String origin)
    throws DuplicateUserException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(user.getUsername()))
    {
      throw new InvalidArgumentException("user.username");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getUserId(connection, user.getUsername()) != -1)
      {
        throw new DuplicateUserException("The user (" + user.getUsername() + ") already exists");
      }

      long userId = nextId("Application.UserId");

      statement = connection.prepareStatement(createUserSQL);
      statement.setLong(1, userId);
      statement.setString(2, user.getUsername());

      String passwordHash;

      if (!isNullOrEmpty(user.getPassword()))
      {
        passwordHash = createPasswordHash(user.getPassword());
        statement.setString(3, passwordHash);
      }
      else
      {
        passwordHash = createPasswordHash("");
        statement.setString(3, passwordHash);
      }

      statement.setString(4, StringUtil.notNull(user.getTitle()));
      statement.setString(5, StringUtil.notNull(user.getFirstNames()));
      statement.setString(6, StringUtil.notNull(user.getLastName()));
      statement.setString(7, StringUtil.notNull(user.getPhoneNumber()));
      statement.setString(8, StringUtil.notNull(user.getFaxNumber()));
      statement.setString(9, StringUtil.notNull(user.getMobileNumber()));
      statement.setString(10, StringUtil.notNull(user.getEmail()));

      if (userLocked)
      {
        statement.setInt(11, -1);
      }
      else
      {
        if (!isNullOrEmpty(user.getPasswordAttempts()))
        {
          statement.setInt(11, user.getPasswordAttempts());
        }
        else
        {
          statement.setNull(11, java.sql.Types.INTEGER);
        }
      }

      if (expiredPassword)
      {
        statement.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
      }
      else
      {
        if (user.getPasswordExpiry() != null)
        {
          statement.setTimestamp(12, new Timestamp(user.getPasswordExpiry().getTime()));
        }
        else
        {
          statement.setTimestamp(12, null);
        }
      }

      statement.setString(13, StringUtil.notNull(user.getDescription()));

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to create the user (" + user.getUsername()
            + "): No rows were affected as a result of executing the SQL statement ("
            + createUserSQL + ")");
      }

      user.setId(userId);

      // Save the password in the password history if one was specified
      if (passwordHash != null)
      {
        savePasswordHistory(connection, userId, passwordHash);
      }
    }
    catch (DuplicateUserException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to create the user (" + user.getUsername() + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the existing authorised function.
   *
   * @param code   the code identifying the function
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  public void deleteFunction(String code, String origin)
    throws FunctionNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getFunctionId(connection, code) == -1)
      {
        throw new FunctionNotFoundException("A function with the code (" + code
            + ") could not be found");
      }

      statement = connection.prepareStatement(deleteFunctionSQL);
      statement.setString(1, code);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to delete the function (" + code
            + "): No rows were affected as a result of executing the SQL statement ("
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
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the existing authorised function template.
   *
   * @param code   the code identifying the function template
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @throws FunctionTemplateNotFoundException
   * @throws SecurityException
   */
  public void deleteFunctionTemplate(String code, String origin)
    throws FunctionTemplateNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getFunctionTemplateId(connection, code) == -1)
      {
        throw new FunctionTemplateNotFoundException("A function template with the code (" + code
            + ") could not be found");
      }

      statement = connection.prepareStatement(deleteFunctionTemplateSQL);
      statement.setString(1, code);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to delete the function template (" + code
            + "): No rows were affected as a result of executing the SQL statement ("
            + deleteFunctionTemplateSQL + ")");
      }
    }
    catch (FunctionTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the function template (" + code + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the existing group.
   *
   * @param groupName the name of the group uniquely identifying the group
   * @param origin    the origin of the request e.g. the IP address, subnetwork or workstation name
   *                  for the remote client
   *
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  public void deleteGroup(String groupName, String origin)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      long groupId = getGroupId(connection, groupName);

      if (groupId == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      List<Long> userIds = getUserIdsForGroup(connection, groupId);

      if (userIds.size() > 0)
      {
        throw new ExistingGroupMembersException("The group (" + groupName
            + ") could not be deleted since it is still associated with " + userIds.size()
            + " user(s)");
      }

      statement = connection.prepareStatement(deleteGroupSQL);
      statement.setString(1, groupName);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to delete the group (" + groupName
            + "): No rows were affected as a result of executing the SQL statement ("
            + deleteGroupSQL + ")");
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (ExistingGroupMembersException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the group (" + groupName + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the existing organisation.
   *
   * @param code   the code uniquely identifying the organisation
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void deleteOrganisation(String code, String origin)
    throws OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getOrganisationId(connection, code) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + code
            + ") could not be found");
      }

      statement = connection.prepareStatement(deleteOrganisationSQL);
      statement.setString(1, code);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to delete the organisation (" + code
            + "): No rows were affected as a result of executing the SQL statement ("
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
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the existing user.
   *
   * @param username the username identifying the user
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void deleteUser(String username, String origin)
    throws UserNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getUserId(connection, username) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      statement = connection.prepareStatement(deleteUserSQL);
      statement.setString(1, username);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to delete the user (" + username
            + "): No rows were affected as a result of executing the SQL statement ("
            + deleteUserSQL + ")");
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the user (" + username + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   * @param origin     the origin of the request e.g. the IP address, subnetwork or workstation
   *                   name for the remote client
   *
   * @return a list of <code>User</code>s whose attributes match the attribute criteria
   *
   * @throws InvalidAttributeException
   * @throws SecurityException
   */
  public List<User> findUsers(List<Attribute> attributes, String origin)
    throws InvalidAttributeException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (attributes == null)
    {
      throw new InvalidArgumentException("attributes");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = buildFindUsersStatement(connection, attributes);
      rs = statement.executeQuery();

      List<User> list = new ArrayList<>();

      while (rs.next())
      {
        User user = buildUserFromResultSet(rs);

        list.add(user);
      }

      return list;
    }
    catch (InvalidAttributeException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to find the users: " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   * @param startPos   the position in the list of users to start from '0-based'
   * @param maxResults the maximum number of results to return or -1 for all
   * @param origin     the origin of the request e.g. the IP address, subnetwork or workstation
   *                   name for the remote client
   *
   * @return a list of <code>User</code>s whose attributes match the attribute criteria
   *
   * @throws InvalidAttributeException
   * @throws SecurityException
   */
  public List<User> findUsersEx(List<Attribute> attributes, int startPos, int maxResults,
      String origin)
    throws InvalidAttributeException, SecurityException
  {
    List<User> allUsers = findUsers(attributes, origin);
    List<User> selectedUsers = new ArrayList<>();

    for (int i = startPos; i < (startPos + maxResults); i++)
    {
      selectedUsers.add(allUsers.get(i));
    }

    return selectedUsers;
  }

  /**
   * Retrieve the authorised function codes for the user for the specified organisation.
   * <p/>
   * This list will include all of the authorised functions that the user is associated with as a
   * result of being a member of one or more groups that have also been linked to authorised
   * functions.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the list of authorised function codes for the user for the specified organisation
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<String> getAllFunctionCodesForUser(String username, String organisation,
      String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      statement = connection.prepareStatement(getAllFunctionCodesForUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);
      rs = statement.executeQuery();

      List<String> list = new ArrayList<>();

      while (rs.next())
      {
        list.add(rs.getString(1));
      }

      // Add the function codes assigned to the user directly
      getFunctionCodesForUserId(connection, userId, organisationId, list);

      return list;
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve all the function codes for the user ("
          + username + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the list of authorised functions for the user for the specified organisation.
   * <p/>
   * This list will include all of the authorised functions that the user is associated with as a
   * result of being a member of one or more groups that have also been linked to authorised
   * functions.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the list of authorised functions for the user for the specified organisation
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<Function> getAllFunctionsForUser(String username, String organisation, String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    throw new SecurityException("TODO: IMPLEMENT ME");
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
   * Returns the filtered list of users associated with the specified organisation.
   *
   * @param organisation the code uniquely identifying the organisation
   * @param filter       the filter to apply to the users
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the filtered list of the users associated with the specified organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<User> getFilteredUsersForOrganisation(String organisation, String filter,
      String origin)
    throws OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(filter))
    {
      throw new InvalidArgumentException("filter");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      StringBuilder filterBuffer = new StringBuilder("%");

      filterBuffer.append(filter.toUpperCase());
      filterBuffer.append("%");

      statement = connection.prepareStatement(getFilteredUsersForOrganisationSQL);

      statement.setLong(1, organisationId);
      statement.setString(2, filterBuffer.toString());
      statement.setString(3, filterBuffer.toString());
      statement.setString(4, filterBuffer.toString());

      rs = statement.executeQuery();

      List<User> list = new ArrayList<>();

      while (rs.next())
      {
        User user = buildUserFromResultSet(rs);

        list.add(user);
      }

      return list;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the users for the organisation ("
          + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the details for the authorised function with the specified code.
   *
   * @param code   the code identifying the function
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return the details for the authorised function with the specified code
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  public Function getFunction(String code, String origin)
    throws FunctionNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getFunctionSQL);
      statement.setString(1, code);
      rs = statement.executeQuery();

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
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the function (" + code + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the list of authorised function codes for the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   * @param origin    the origin of the request e.g. the IP address, subnetwork or workstation name
   *                  for the remote client
   *
   * @return the list of authorised function codes for the group
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public List<String> getFunctionCodesForGroup(String groupName, String origin)
    throws GroupNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the group with the specified group name
      long groupId;

      if ((groupId = getGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      statement = connection.prepareStatement(getFunctionCodesForGroupSQL);
      statement.setLong(1, groupId);
      rs = statement.executeQuery();

      List<String> list = new ArrayList<>();

      while (rs.next())
      {
        list.add(rs.getString(1));
      }

      return list;
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the function codes for the group ("
          + groupName + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the list of authorised function codes for the user for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the list of authorised function codes for the user for the specified organisation
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<String> getFunctionCodesForUser(String username, String organisation, String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      statement = connection.prepareStatement(getFunctionCodesForUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);
      rs = statement.executeQuery();

      List<String> list = new ArrayList<>();

      while (rs.next())
      {
        list.add(rs.getString(1));
      }

      return list;
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the function codes for the user (" + username
          + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the list of authorised function codes for the user.
   *
   * @param connection     the existing database connection to use
   * @param userId         the numeric ID uniquely identifying the user
   * @param organisationId the numeric ID uniquely identifying the organisation
   * @param functionCodes  the list of function codes to populate
   *
   * @return the list of authorised function codes for the user with the specified ID
   *
   * @throws SQLException
   */
  public List<String> getFunctionCodesForUserId(Connection connection, long userId,
      long organisationId, List<String> functionCodes)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getFunctionCodesForUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);
      rs = statement.executeQuery();

      while (rs.next())
      {
        String functionCode = rs.getString(1);

        if (!functionCodes.contains(functionCode))
        {
          functionCodes.add(functionCode);
        }
      }

      return functionCodes;
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Retrieve the details for the authorised function template with the specified code.
   *
   * @param code   the code identifying the function template
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return the details for the authorised function template with the specified code
   *
   * @throws FunctionTemplateNotFoundException
   * @throws SecurityException
   */
  public FunctionTemplate getFunctionTemplate(String code, String origin)
    throws FunctionTemplateNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getFunctionTemplateSQL);
      statement.setString(1, code);
      rs = statement.executeQuery();

      if (rs.next())
      {
        FunctionTemplate template = new FunctionTemplate(rs.getString(2));

        template.setId(rs.getInt(1));
        template.setName(rs.getString(3));
        template.setDescription(rs.getString(4));

        // Retrieve the functions for the template
        List<Function> functions = getFunctionsForTemplate(template.getId());

        template.setFunctions(functions);

        return template;
      }
      else
      {
        throw new FunctionTemplateNotFoundException("A function template with the code (" + code
            + ") could not be found");
      }
    }
    catch (FunctionTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the function template (" + code + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve all the authorised function templates.
   *
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return the list of authorised function templates
   *
   * @throws SecurityException
   */
  public List<FunctionTemplate> getFunctionTemplates(String origin)
    throws SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getFunctionTemplatesSQL);
      rs = statement.executeQuery();

      List<FunctionTemplate> list = new ArrayList<>();

      while (rs.next())
      {
        FunctionTemplate template = new FunctionTemplate(rs.getString(2));

        template.setId(rs.getInt(1));
        template.setName(rs.getString(3));
        template.setDescription(rs.getString(4));

        List<Function> functions = getFunctionsForTemplate(template.getId());

        template.setFunctions(functions);
        list.add(template);
      }

      return list;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the function templates: " + e.getMessage(),
          e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve all the authorised functions.
   *
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return the list of authorised functions
   *
   * @throws SecurityException
   */
  public List<Function> getFunctions(String origin)
    throws SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getFunctionsSQL);
      rs = statement.executeQuery();

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
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the functions: " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the list of authorised functions for the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   * @param origin    the origin of the request e.g. the IP address, subnetwork or workstation name
   *                  for the remote client
   *
   * @return the list of authorised functions for the group
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public List<Function> getFunctionsForGroup(String groupName, String origin)
    throws GroupNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the group with the specified group name
      long groupId;

      if ((groupId = getGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      statement = connection.prepareStatement(getFunctionsForGroupSQL);
      statement.setLong(1, groupId);
      rs = statement.executeQuery();

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
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the functions for the group (" + groupName
          + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the list of authorised functions for the user for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the list of authorised functions for the user for the specified organisation
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<Function> getFunctionsForUser(String username, String organisation, String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      statement = connection.prepareStatement(getFunctionsForUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);
      rs = statement.executeQuery();

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
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the functions for the user (" + username
          + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   * @param origin    the origin of the request e.g. the IP address, subnetwork or workstation name
   *                  for the remote client
   *
   * @return the <code>Group</code>
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public Group getGroup(String groupName, String origin)
    throws GroupNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getGroupSQL);
      statement.setString(1, groupName);
      rs = statement.executeQuery();

      if (rs.next())
      {
        Group group = new Group(rs.getString(2));

        group.setId(rs.getLong(1));
        group.setDescription(StringUtil.notNull(rs.getString(3)));

        return group;
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
      throw new SecurityException("Failed to retrieve the group (" + groupName + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the unique numeric ID for the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   *
   * @return the unique numeric ID for the group
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public long getGroupId(String groupName)
    throws GroupNotFoundException, SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    try
    {
      connection = dataSource.getConnection();

      long groupId = getGroupId(connection, groupName);

      if (groupId == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      return groupId;
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the ID for the group (" + groupName + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the group names for the user for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the group names for the user for the specified organisation
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<String> getGroupNamesForUser(String username, String organisation, String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      return getGroupNamesForUser(connection, userId, organisationId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the group names for the user (" + username
          + ") and organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve all the groups.
   *
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return a list of <code>Group</code> objects containing the group information
   *
   * @throws SecurityException
   */
  public List<Group> getGroups(String origin)
    throws SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getGroupsSQL);
      rs = statement.executeQuery();

      List<Group> list = new ArrayList<>();

      while (rs.next())
      {
        Group group = new Group(rs.getString(2));

        group.setId(rs.getLong(1));
        group.setDescription(StringUtil.notNull(rs.getString(3)));
        list.add(group);
      }

      return list;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the groups: " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve a list of groups.
   *
   * @param startPos   the position in the list of groups to start from
   * @param maxResults the maximum number of results to return or -1 for all
   * @param origin     the origin of the request e.g. the IP address, subnetwork or workstation
   *                   name for the remote client
   *
   * @return a list of <code>Group</code> objects containing the group information
   *
   * @throws SecurityException
   */
  public List<Group> getGroupsEx(int startPos, int maxResults, String origin)
    throws SecurityException
  {
    throw new SecurityException("TODO: IMPLEMENT ME");
  }

  /**
   * Retrieve the groups for the user for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the groups for the user for the specified organisation
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<Group> getGroupsForUser(String username, String organisation, String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Get the list of groups the user is associated with
      return getGroupsForUser(connection, userId, organisationId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the groups for the user (" + username
          + ") and organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  /**
   * Returns the number of filtered users associated with the specified organisation.
   *
   * @param organisation the code uniquely identifying the organisation
   * @param filter       the filter to apply to the users
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the number of filtered users associated with the specified organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfFilteredUsersForOrganisation(String organisation, String filter,
      String origin)
    throws OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(filter))
    {
      throw new InvalidArgumentException("filter");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      StringBuilder filterBuffer = new StringBuilder("%");

      filterBuffer.append(filter.toUpperCase());
      filterBuffer.append("%");

      statement = connection.prepareStatement(getNumberOfFilteredUsersForOrganisationSQL);

      statement.setLong(1, organisationId);
      statement.setString(2, filterBuffer.toString());
      statement.setString(3, filterBuffer.toString());
      statement.setString(4, filterBuffer.toString());

      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getInt(1);
      }
      else
      {
        return 0;
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the number of users for the organisation ("
          + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Returns the number of groups
   *
   * @param origin the origin of the request e.g. the IP address, subnetwork or
   *               workstation name for the remote client
   *
   * @return the number of groups
   *
   * @throws SecurityException
   */
  public int getNumberOfGroups(String origin)
    throws SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getNumberOfGroupsSQL);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getInt(1);
      }
      else
      {
        return 0;
      }
    }
    catch (SecurityException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the number of groups" + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Returns the number of users associated with the specified organisation.
   *
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the number of users associated with the specified organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public int getNumberOfUsersForOrganisation(String organisation, String origin)
    throws OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      statement = connection.prepareStatement(getNumberOfUsersForOrganisationSQL);

      statement.setLong(1, organisationId);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getInt(1);
      }
      else
      {
        return 0;
      }
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the number of users for the organisation ("
          + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the organisation.
   *
   * @param code   the code uniquely identifying the organisation
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return the details for the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public Organisation getOrganisation(String code, String origin)
    throws OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getOrganisationSQL);
      statement.setString(1, code);
      rs = statement.executeQuery();

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
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the organisation (" + code + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the information for all existing organisations.
   *
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return a list of <code>Organisation</code> objects containing the organisation information
   *
   * @throws SecurityException
   */
  public List<Organisation> getOrganisations(String origin)
    throws SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getOrganisationsSQL);
      rs = statement.executeQuery();

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
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the organisations: " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Returns the organisations the user is associated with.
   *
   * @param username the username identifying the user
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @return the organisations the user is associated with
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public List<Organisation> getOrganisationsForUser(String username, String origin)
    throws UserNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      statement = connection.prepareStatement(getOrganisationsForUserSQL);

      statement.setLong(1, userId);

      rs = statement.executeQuery();

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
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the organisations for the user (" + username
          + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
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
   * Retrieve the information for an existing user.
   *
   * @param username the username identifying the user
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @return the <code>User</code>
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public User getUser(String username, String origin)
    throws UserNotFoundException, SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

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
      throw new SecurityException("Failed to retrieve the user (" + username + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the unique numeric ID for the user.
   *
   * @param username the username identifying the user
   *
   * @return the unique numeric ID for the user
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public long getUserId(String username)
    throws UserNotFoundException, SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    try
    {
      connection = dataSource.getConnection();

      long userId = getUserId(connection, username);

      if (userId == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      return userId;
    }
    catch (SecurityException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the ID for the user (" + username + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the information for all existing users.
   *
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return a list of <code>User</code> objects containing the user information
   *
   * @throws SecurityException
   */
  public List<User> getUsers(String origin)
    throws SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getUsersSQL);
      rs = statement.executeQuery();

      List<User> list = new ArrayList<>();

      while (rs.next())
      {
        User user = buildUserFromResultSet(rs);

        list.add(user);
      }

      return list;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the users: " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the information for a subset of all the existing users.
   *
   * @param startPos   the position in the list of users to start from
   * @param maxResults the maximum number of results to return or -1 for all
   * @param origin     the origin of the request e.g. the IP address, subnetwork or workstation
   *                   name for the remote client
   *
   * @return a list of <code>User</code> objects containing the user information
   *
   * @throws SecurityException
   */
  public List<User> getUsersEx(int startPos, int maxResults, String origin)
    throws SecurityException
  {
    throw new SecurityException("TODO: IMPLEMENT ME");
  }

  /**
   * Retrieve the users for the group for the specified organisation.
   *
   * @param groupName    the name of the group uniquely identifying the group
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return a list of <code>User</code> objects containing the user information
   *
   * @throws GroupNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<User> getUsersForGroup(String groupName, String organisation, String origin)
    throws GroupNotFoundException, OrganisationNotFoundException, SecurityException
  {
    throw new SecurityException("TODO: IMPLEMENT ME");
  }

  /**
   * Returns the users associated with the specified organisation.
   *
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return the users associated with the specified organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public List<User> getUsersForOrganisation(String organisation, String origin)
    throws OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    // Validate parameters
    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      statement = connection.prepareStatement(getUsersForOrganisationSQL);

      statement.setLong(1, organisationId);

      rs = statement.executeQuery();

      List<User> list = new ArrayList<>();

      while (rs.next())
      {
        User user = buildUserFromResultSet(rs);

        list.add(user);
      }

      return list;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the users for the organisation ("
          + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Add the authorised function to the group's access profile.
   *
   * @param groupName the name of the group uniquely identifying the group
   * @param code      the code identifying the authorised function
   * @param origin    the origin of the request e.g. the IP address, subnetwork or workstation name
   *                  for the remote client
   *
   * @throws GroupNotFoundException
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  public void grantFunctionToGroup(String groupName, String code, String origin)
    throws GroupNotFoundException, FunctionNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the group with the specified group name
      long groupId;

      if ((groupId = getGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Get the ID of the function with the specified code
      long functionId;

      if ((functionId = getFunctionId(connection, code)) == -1)
      {
        throw new FunctionNotFoundException("The function (" + code + ") could not be found");
      }

      // Check if the group has already been granted the function
      if (isGroupGrantedFunction(connection, groupId, functionId))
      {
        // The user has already been granted the function
        return;
      }

      // Grant the function to the user
      statement = connection.prepareStatement(grantFunctionToGroupSQL);
      statement.setLong(1, groupId);
      statement.setLong(2, functionId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to grant the function (" + code + ") to the group ("
            + groupName + "): No rows were affected as a result of executing the SQL statement ("
            + grantFunctionToGroupSQL + ")");
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to grant the function (" + code + ") to the group ("
          + groupName + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Add the authorised function to the user's access profile for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param code         the code identifying the authorised function
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @throws UserNotFoundException
   * @throws FunctionNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void grantFunctionToUser(String username, String code, String organisation, String origin)
    throws UserNotFoundException, FunctionNotFoundException, OrganisationNotFoundException,
      SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the function with the specified code
      long functionId;

      if ((functionId = getFunctionId(connection, code)) == -1)
      {
        throw new FunctionNotFoundException("The function (" + code + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Check if the user has already been granted the function
      if (isUserGrantedFunction(connection, userId, functionId, organisationId))
      {
        // The user has already been granted the function
        return;
      }

      // Grant the function to the user
      statement = connection.prepareStatement(grantFunctionToUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, functionId);
      statement.setLong(3, organisationId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to grant the function (" + code + ") to the user ("
            + username + ") for the organisation (" + organisation
            + "): No rows were affected as a result of executing the SQL statement ("
            + grantFunctionToUserSQL + ")");
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to grant the function (" + code + ") to the user ("
          + username + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
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
      Connection connection = null;
      String schemaSeparator = ".";

      try
      {
        connection = dataSource.getConnection();

        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }
      }
      finally
      {
        DAOUtil.close(connection);
      }

      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);

      // Initialise the configuration
      initConfiguration();
    }
    catch (Exception e)
    {
      throw new SecurityException("Failed to initialise the SecurityService instance: "
          + e.getMessage(), e);
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to initialise the SecurityService instance: "
          + e.getMessage());
    }
  }

  /**
   * Returns <code>true</code> if the user is associated with the specified organisation or
   * <code>false</code> otherwise.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return <code>true</code> if the user is associated with the specified organisation or
   *         <code>false</code> otherwise
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public boolean isUserAssociatedWithOrganisation(String username, String organisation,
      String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Get the current list of groups for the user
      return isUserAssociatedWithOrganisation(connection, userId, organisationId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to check whether the user (" + username
          + ") is associated with the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  /**
   * Is the user in the group for the specified organisation?
   *
   * @param username     the username identifying the user
   * @param groupName    the name of the group uniquely identifying the group
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   *
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public boolean isUserInGroup(String username, String groupName, String organisation,
      String origin)
    throws UserNotFoundException, GroupNotFoundException, OrganisationNotFoundException,
      SecurityException
  {
    Connection connection = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the group with the specified group name
      long groupId;

      if ((groupId = getGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Get the current list of groups for the user
      return isUserInGroup(connection, userId, groupId, organisationId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to check whether the user (" + username
          + ") is in the group (" + groupName + ") for the organisation (" + organisation + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
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
    Connection connection = null;
    PreparedStatement updateStatement = null;
    PreparedStatement selectStatement = null;
    ResultSet rs = null;

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

      connection = dataSource.getConnection();
      updateStatement = connection.prepareStatement(updateIDGeneratorSQL);
      updateStatement.setString(1, type);

      // The following statment will block if another connection is currently
      // executing a transaction that is updating the IDGENERATOR table.
      if (updateStatement.executeUpdate() == 0)
      {
        // The row could not be found so INSERT one starting at id = 1
        PreparedStatement insertStatement = null;

        try
        {
          insertStatement = connection.prepareStatement(insertIDGeneratorSQL);
          insertStatement.setLong(1, 1);
          insertStatement.setString(2, type);
          insertStatement.executeUpdate();
        }
        finally
        {
          DAOUtil.close(insertStatement);
        }
      }

      selectStatement = connection.prepareStatement(selectIDGeneratorSQL);
      selectStatement.setString(1, type);
      rs = selectStatement.executeQuery();

      if (rs.next())
      {
        result = rs.getLong(1);

        transactionManager.commit();

        return result;
      }
      else
      {
        throw new SQLException("No IDGenerator row found for type (" + type + ")");
      }
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
      DAOUtil.close(rs);
      DAOUtil.close(selectStatement);
      DAOUtil.close(updateStatement);
      DAOUtil.close(connection);

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
   * Remove the authorised function with specified code from the authorised function template with
   * the specified code.
   *
   * @param functionCode the code identifying the authorised function
   * @param templateCode the code identifying the authorised function template
   * @param origin       the origin of the request e.g. the IP address, subnetwork or workstation
   *                     name for the remote client
   *
   * @throws FunctionNotFoundException
   * @throws FunctionTemplateNotFoundException
   * @throws SecurityException
   */
  public void removeFunctionFromTemplate(String functionCode, String templateCode, String origin)
    throws FunctionNotFoundException, FunctionTemplateNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(functionCode))
    {
      throw new InvalidArgumentException("functionCode");
    }

    if (isNullOrEmpty(templateCode))
    {
      throw new InvalidArgumentException("templateCode");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the function with the specified code
      long functionId;

      if ((functionId = getFunctionId(connection, functionCode)) == -1)
      {
        throw new FunctionNotFoundException("A function with the code (" + functionCode
            + ") could not be found");
      }

      // Get the ID of the function template with the specified code
      long templateId;

      if ((templateId = getFunctionTemplateId(connection, templateCode)) == -1)
      {
        throw new FunctionTemplateNotFoundException("A function template with the code ("
            + templateCode + ") could not be found");
      }

      // Remove the function from the template
      statement = connection.prepareStatement(removeFunctionFromTemplateSQL);
      statement.setLong(1, functionId);
      statement.setLong(2, templateId);
      statement.executeUpdate();
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (FunctionTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to remove the function (" + functionCode
          + ") from the function template (" + templateCode + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Remove the user from the group for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param groupName    the group name
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @throws UserNotFoundException
   * @throws GroupNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void removeUserFromGroup(String username, String groupName, String organisation,
      String origin)
    throws UserNotFoundException, GroupNotFoundException, OrganisationNotFoundException,
      SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the group with the specified group name
      long groupId;

      if ((groupId = getGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Remove the user from the group
      statement = connection.prepareStatement(removeUserFromGroupSQL);
      statement.setLong(1, userId);
      statement.setLong(2, groupId);
      statement.setLong(3, organisationId);
      statement.executeUpdate();
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to remove the user (" + username + ") from the group ("
          + groupName + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Remove the user from the specified organisation.
   *
   * @param username     the username identifying the user
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @throws UserNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void removeUserFromOrganisation(String username, String organisation, String origin)
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Remove the user from the organisation
      statement = connection.prepareStatement(removeUserFromOrganisationSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to remove the user (" + username
            + ") from the organisation (" + organisation
            + "): No rows were affected as a result of executing the SQL statement ("
            + removeUserFromOrganisationSQL + ")");
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to remove the user (" + username
          + ") from the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Rename the existing group.
   *
   * @param groupName    the name of the group that will be renamed
   * @param newGroupName the new name of the group
   * @param origin       the origin of the request e.g. the IP address, subnetwork or workstation
   *                     name for the remote client
   *
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  public void renameGroup(String groupName, String newGroupName, String origin)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException
  {
    throw new SecurityException("TODO: IMPLEMENT ME");
  }

  /**
   * Remove the authorised function from the group's access profile.
   *
   * @param groupName the name of the group uniquely identifying the group
   * @param code      the code identifying the authorised function
   * @param origin    the origin of the request e.g. the IP address, subnetwork or workstation name
   *                  for the remote client
   *
   * @throws GroupNotFoundException
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  public void revokeFunctionForGroup(String groupName, String code, String origin)
    throws GroupNotFoundException, FunctionNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(groupName))
    {
      throw new InvalidArgumentException("groupName");
    }

    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the group with the specified group name
      long groupId;

      if ((groupId = getGroupId(connection, groupName)) == -1)
      {
        throw new GroupNotFoundException("The group (" + groupName + ") could not be found");
      }

      // Get the ID of the function with the specified code
      long functionId;

      if ((functionId = getFunctionId(connection, code)) == -1)
      {
        throw new FunctionNotFoundException("The function (" + code + ") could not be found");
      }

      // Revoke the function for the group
      statement = connection.prepareStatement(revokeFunctionForGroupSQL);
      statement.setLong(1, groupId);
      statement.setLong(2, functionId);
      statement.executeUpdate();
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to revoke the function (" + code + ") for the group ("
          + groupName + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Remove the authorised function from the user's access profile for the specified organisation.
   *
   * @param username     the username identifying the user
   * @param code         the code identifying the authorised function
   * @param organisation the code uniquely identifying the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or
   *                     workstation name for the remote client
   *
   * @throws UserNotFoundException
   * @throws FunctionNotFoundException
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void revokeFunctionForUser(String username, String code, String organisation,
      String origin)
    throws UserNotFoundException, FunctionNotFoundException, OrganisationNotFoundException,
      SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(username))
    {
      throw new InvalidArgumentException("username");
    }

    if (isNullOrEmpty(code))
    {
      throw new InvalidArgumentException("code");
    }

    if (isNullOrEmpty(organisation))
    {
      throw new InvalidArgumentException("organisation");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      // Get the ID of the user with the specified username
      long userId;

      if ((userId = getUserId(connection, username)) == -1)
      {
        throw new UserNotFoundException("The user (" + username + ") could not be found");
      }

      // Get the ID of the function with the specified code
      long functionId;

      if ((functionId = getFunctionId(connection, code)) == -1)
      {
        throw new FunctionNotFoundException("The function (" + code + ") could not be found");
      }

      // Get the ID of the organisation with the specified code
      long organisationId;

      if ((organisationId = getOrganisationId(connection, organisation)) == -1)
      {
        throw new OrganisationNotFoundException("The organisation (" + organisation
            + ") could not be found");
      }

      // Revoke the function for the user
      statement = connection.prepareStatement(revokeFunctionForUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, functionId);
      statement.setLong(3, organisationId);
      statement.executeUpdate();
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (OrganisationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to revoke the function (" + code + ") for the user ("
          + username + ") for the organisation (" + organisation + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
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
   * @param function the <code>Function</code> instance containing the updated authorised function
   *                 information
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  public void updateFunction(Function function, String origin)
    throws FunctionNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(function.getCode()))
    {
      throw new InvalidArgumentException("function.code");
    }

    if (isNullOrEmpty(function.getName()))
    {
      throw new InvalidArgumentException("function.name");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getFunctionId(connection, function.getCode()) == -1)
      {
        throw new FunctionNotFoundException("A function with the code (" + function.getCode()
            + ") could not be found");
      }

      statement = connection.prepareStatement(updateFunctionSQL);
      statement.setString(1, function.getName());
      statement.setString(2, StringUtil.notNull(function.getDescription()));
      statement.setString(3, function.getCode());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to update the function (" + function.getCode()
            + "): No rows were affected as a result of executing the SQL statement ("
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
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Update the authorised function template.
   *
   * @param template the <code>FunctionTemplate</code> instance containing the updated authorised
   *                 function template information
   * @param origin   the origin of the request e.g. the IP address, subnetwork or workstation name
   *                 for the remote client
   *
   * @throws FunctionTemplateNotFoundException
   * @throws SecurityException
   */
  public void updateFunctionTemplate(FunctionTemplate template, String origin)
    throws FunctionTemplateNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(template.getCode()))
    {
      throw new InvalidArgumentException("template.code");
    }

    if (isNullOrEmpty(template.getName()))
    {
      throw new InvalidArgumentException("template.name");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getFunctionTemplateId(connection, template.getCode()) == -1)
      {
        throw new FunctionTemplateNotFoundException("A function template with the code ("
            + template.getCode() + ") could not be found");
      }

      statement = connection.prepareStatement(updateFunctionTemplateSQL);
      statement.setString(1, template.getName());
      statement.setString(2, StringUtil.notNull(template.getDescription()));
      statement.setString(3, template.getCode());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to update the function template (" + template.getCode()
            + "): No rows were affected as a result of executing the SQL statement ("
            + updateFunctionTemplateSQL + ")");
      }
    }
    catch (FunctionTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the function template (" + template.getCode()
          + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Update the existing group.
   *
   * @param group  the <code>Group</code> instance containing the updated group information
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  public void updateGroup(Group group, String origin)
    throws GroupNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(group.getGroupName()))
    {
      throw new InvalidArgumentException("group.groupName");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getGroupId(connection, group.getGroupName()) == -1)
      {
        throw new GroupNotFoundException("The group (" + group.getGroupName()
            + ") could not be found");
      }

      statement = connection.prepareStatement(updateGroupSQL);
      statement.setString(1, StringUtil.notNull(group.getDescription()));
      statement.setString(2, group.getGroupName());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to update the group (" + group.getGroupName()
            + "): No rows were affected as a result of executing the SQL statement ("
            + updateGroupSQL + ")");
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the group (" + group.getGroupName() + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Update the existing organisation.
   *
   * @param organisation the <code>Organisation</code> instance containing the updated information
   *                     for the organisation
   * @param origin       the origin of the request e.g. the IP address, subnetwork or workstation
   *                     name for the remote client
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  public void updateOrganisation(Organisation organisation, String origin)
    throws OrganisationNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    if (isNullOrEmpty(organisation.getCode()))
    {
      throw new InvalidArgumentException("organisation.code");
    }

    if (isNullOrEmpty(organisation.getName()))
    {
      throw new InvalidArgumentException("organisation.name");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getOrganisationId(connection, organisation.getCode()) == -1)
      {
        throw new FunctionNotFoundException("An organisation with the code ("
            + organisation.getCode() + ") could not be found");
      }

      statement = connection.prepareStatement(updateOrganisationSQL);
      statement.setString(1, organisation.getName());
      statement.setString(2, StringUtil.notNull(organisation.getDescription()));
      statement.setString(3, organisation.getCode());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException("Failed to update the organisation (" + organisation.getCode()
            + "): No rows were affected as a result of executing the SQL statement ("
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
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Update the existing user.
   *
   * @param user           the <code>User</code> instance containing the update information for the
   *                       user
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser       lock the user as part of the update
   * @param origin         the origin of the request e.g. the IP address, subnetwork or workstation
   *                       name for the remote client
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void updateUser(User user, boolean expirePassword, boolean lockUser, String origin)
    throws UserNotFoundException, SecurityException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    // Validate parameters
    if (isNullOrEmpty(user.getUsername()))
    {
      throw new InvalidArgumentException("user.username");
    }

    if (isNullOrEmpty(origin))
    {
      throw new InvalidArgumentException("origin");
    }

    try
    {
      connection = dataSource.getConnection();

      if (getUserId(connection, user.getUsername()) == -1)
      {
        throw new UserNotFoundException("The user (" + user.getUsername() + ") could not be found");
      }

      StringBuilder buffer = new StringBuilder();

      buffer.append("UPDATE ");
      buffer.append(DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA).append(
          databaseCatalogSeparator);

      buffer.append("USERS ");

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

      if (lockUser || (user.getPasswordAttempts() != null))
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET PASSWORD_ATTEMPTS=?"
            : ", PASSWORD_ATTEMPTS=?");
      }

      if (expirePassword || (user.getPasswordExpiry() != null))
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET PASSWORD_EXPIRY=?"
            : ", PASSWORD_EXPIRY=?");
      }

      buffer.append(fieldsBuffer.toString());
      buffer.append(" WHERE UPPER(USERNAME)=UPPER(CAST(? AS VARCHAR(100)))");

      String updateUserSQL = buffer.toString();

      statement = connection.prepareStatement(updateUserSQL);

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

      if (lockUser || (user.getPasswordAttempts() != null))
      {
        if (lockUser)
        {
          statement.setInt(parameterIndex, -1);
        }
        else
        {
          statement.setInt(parameterIndex, user.getPasswordAttempts());
        }

        parameterIndex++;
      }

      if (expirePassword || (user.getPasswordExpiry() != null))
      {
        if (expirePassword)
        {
          statement.setTimestamp(parameterIndex, new Timestamp(System.currentTimeMillis()));
        }
        else
        {
          statement.setTimestamp(parameterIndex, new Timestamp(user.getPasswordExpiry().getTime()));
        }

        parameterIndex++;
      }

      statement.setString(parameterIndex, user.getUsername());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException("Failed to update the user (" + user.getUsername()
            + "): No rows were affected as a result of executing the SQL statement ("
            + updateUserSQL + ")");
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to update the user (" + user.getUsername() + "): "
          + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Generate the SQL statements for the <code>SecurityService</code>.
   *
   * @param schemaPrefix the schema prefix to append to database objects reference by the
   *
   * @throws SQLException if a database error occurs
   */
  protected void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // addFunctionToTemplateSQL
    addFunctionToTemplateSQL = "INSERT INTO " + schemaPrefix + "FUNCTION_TEMPLATE_MAP"
        + " (FUNCTION_ID, TEMPLATE_ID) VALUES (?, ?)";

    // addUserToGroupSQL
    addUserToGroupSQL = "INSERT INTO " + schemaPrefix + "USER_GROUP_MAP"
        + " (USER_ID, GROUP_ID, ORGANISATION_ID) VALUES (?, ?, ?)";

    // addUserToOrganisationSQL
    addUserToOrganisationSQL = "INSERT INTO " + schemaPrefix + "USER_ORGANISATION_MAP"
        + " (USER_ID, ORGANISATION_ID) VALUES (?, ?)";

    // changePasswordSQL
    changePasswordSQL = "UPDATE " + schemaPrefix + "USERS"
        + " SET PASSWORD=?, PASSWORD_ATTEMPTS=?, PASSWORD_EXPIRY=?"
        + " WHERE UPPER(USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // createFunctionSQL
    createFunctionSQL = "INSERT INTO " + schemaPrefix + "FUNCTIONS"
        + " (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // createFunctionTemplateSQL
    createFunctionTemplateSQL = "INSERT INTO " + schemaPrefix + "FUNCTION_TEMPLATES"
        + " (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // createGroupSQL
    createGroupSQL = "INSERT INTO " + schemaPrefix + "GROUPS"
        + " (ID, GROUPNAME, DESCRIPTION) VALUES (?, ?, ?)";

    // createOrganisationSQL
    createOrganisationSQL = "INSERT INTO " + schemaPrefix + "ORGANISATIONS"
        + " (ID, CODE, NAME, DESCRIPTION) VALUES (?, ?, ?, ?)";

    // createUserSQL
    createUserSQL = "INSERT INTO " + schemaPrefix + "USERS"
        + " (ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES, LAST_NAME, PHONE,"
        + " FAX, MOBILE, EMAIL, PASSWORD_ATTEMPTS, PASSWORD_EXPIRY,"
        + " DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // deleteFunctionSQL
    deleteFunctionSQL = "DELETE FROM " + schemaPrefix + "FUNCTIONS WHERE CODE=?";

    // deleteFunctionTemplateSQL
    deleteFunctionTemplateSQL = "DELETE FROM " + schemaPrefix + "FUNCTION_TEMPLATES"
        + " WHERE CODE=?";

    // deleteGroupSQL
    deleteGroupSQL = "DELETE FROM " + schemaPrefix + "GROUPS"
        + " WHERE UPPER(GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // deleteOrganisationSQL
    deleteOrganisationSQL = "DELETE FROM " + schemaPrefix + "ORGANISATIONS"
        + " WHERE UPPER(CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // deleteUserSQL
    deleteUserSQL = "DELETE FROM " + schemaPrefix + "USERS"
        + " WHERE UPPER(USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getAllFunctionCodesForUserSQL
    getAllFunctionCodesForUserSQL = "SELECT DISTINCT A.CODE FROM " + schemaPrefix + "FUNCTIONS A, "
        + schemaPrefix + "FUNCTION_GROUP_MAP B WHERE A.ID = B.FUNCTION_ID AND"
        + " B.GROUP_ID IN (SELECT GROUP_ID FROM " + schemaPrefix + "USER_GROUP_MAP WHERE"
        + " USER_ID=? AND ORGANISATION_ID=?)";

    // getFilteredUsersForOrganisationSQL
    getFilteredUsersForOrganisationSQL = "SELECT A.ID, A.USERNAME, A.PASSWORD,"
        + " A.TITLE, A.FIRST_NAMES, A.LAST_NAME, A.PHONE, A.FAX, A.MOBILE,"
        + " A.EMAIL, A.PASSWORD_ATTEMPTS, A.PASSWORD_EXPIRY, A.DESCRIPTION FROM " + schemaPrefix
        + "USERS A, " + schemaPrefix + "USER_ORGANISATION_MAP B"
        + " WHERE A.ID = B.USER_ID AND B.ORGANISATION_ID=?"
        + " AND ((UPPER(A.USERNAME) LIKE ?) OR (UPPER(A.FIRST_NAMES) LIKE ?)"
        + " OR (UPPER(A.LAST_NAME) LIKE ?)) ORDER BY A.USERNAME";

    // getFunctionCodesForGroupSQL
    getFunctionCodesForGroupSQL = "SELECT DISTINCT A.CODE FROM " + schemaPrefix + "FUNCTIONS A, "
        + schemaPrefix + "FUNCTION_GROUP_MAP B WHERE A.ID = B.FUNCTION_ID AND" + " B.GROUP_ID=?";

    // getFunctionCodesForUserSQL
    getFunctionCodesForUserSQL = "SELECT DISTINCT A.CODE FROM " + schemaPrefix + "FUNCTIONS A, "
        + schemaPrefix + "FUNCTION_USER_MAP B WHERE A.ID = B.FUNCTION_ID AND"
        + " B.USER_ID=? AND B.ORGANISATION_ID=?";

    // getFunctionIdSQL
    getFunctionIdSQL = "SELECT ID FROM " + schemaPrefix + "FUNCTIONS WHERE CODE=?";

    // getFunctionSQL
    getFunctionSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "FUNCTIONS WHERE CODE=?";

    // getFunctionTemplateIdSQL
    getFunctionTemplateIdSQL = "SELECT ID FROM " + schemaPrefix + "FUNCTION_TEMPLATES"
        + " WHERE CODE=?";

    // getFunctionTemplateSQL
    getFunctionTemplateSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "FUNCTION_TEMPLATES WHERE CODE=?";

    // getFunctionTemplatesSQL
    getFunctionTemplatesSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "FUNCTION_TEMPLATES";

    // getFunctionsForGroupSQL
    getFunctionsForGroupSQL = "SELECT A.ID, A.CODE, A.NAME, A.DESCRIPTION FROM " + schemaPrefix
        + "FUNCTIONS A, " + schemaPrefix + "FUNCTION_GROUP_MAP B"
        + " WHERE A.ID = B.FUNCTION_ID AND B.GROUP_ID=?";

    // getFunctionsForTemplateSQL
    getFunctionsForTemplateSQL = "SELECT A.ID, A.CODE, A.NAME, A.DESCRIPTION FROM " + schemaPrefix
        + "FUNCTIONS A, " + schemaPrefix + "FUNCTION_TEMPLATE_MAP B"
        + " WHERE A.ID = B.FUNCTION_ID AND B.TEMPLATE_ID=?";

    // getFunctionsForUserSQL
    getFunctionsForUserSQL = "SELECT A.ID, A.CODE, A.NAME, A.DESCRIPTION FROM " + schemaPrefix
        + "FUNCTIONS A, " + schemaPrefix + "FUNCTION_USER_MAP B"
        + " WHERE A.ID = B.FUNCTION_ID AND B.USER_ID=? AND B.ORGANISATION_ID=?";

    // getFunctionsSQL
    getFunctionsSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix + "FUNCTIONS";

    // getGroupIdSQL
    getGroupIdSQL = "SELECT ID FROM " + schemaPrefix + "GROUPS"
        + " WHERE UPPER(GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getGroupNamesForUserSQL
    getGroupNamesForUserSQL = "SELECT A.GROUPNAME FROM " + schemaPrefix + "GROUPS A, "
        + schemaPrefix + "USER_GROUP_MAP B"
        + " WHERE A.ID = B.GROUP_ID AND B.USER_ID=? AND B.ORGANISATION_ID=?"
        + " ORDER BY A.GROUPNAME";

    // getGroupSQL
    getGroupSQL = "SELECT ID, GROUPNAME, DESCRIPTION FROM " + schemaPrefix
        + "GROUPS WHERE UPPER(GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getGroupsForUserSQL
    getGroupsForUserSQL = "SELECT A.ID, A.GROUPNAME, A.DESCRIPTION FROM " + schemaPrefix
        + "GROUPS A, " + schemaPrefix + "USER_GROUP_MAP B"
        + " WHERE A.ID = B.GROUP_ID AND B.USER_ID=? AND B.ORGANISATION_ID=?"
        + " ORDER BY A.GROUPNAME";

    // getGroupsSQL
    getGroupsSQL = "SELECT ID, GROUPNAME, DESCRIPTION FROM " + schemaPrefix
        + "GROUPS ORDER BY GROUPNAME";

    // getNumberOfFilteredUsersForOrganisationSQL
    getNumberOfFilteredUsersForOrganisationSQL = "SELECT COUNT(A.ID) FROM " + schemaPrefix
        + "USERS A, " + schemaPrefix + " USER_ORGANISATION_MAP B"
        + " WHERE A.ID = B.USER_ID AND B.ORGANISATION_ID=?"
        + " AND ((UPPER(A.USERNAME) LIKE ?) OR (UPPER(A.FIRST_NAMES) LIKE ?)"
        + " OR (UPPER(A.LAST_NAME) LIKE ?))";

    // getNumberOfGroupsSQL
    getNumberOfGroupsSQL = "SELECT COUNT(A.ID) FROM " + schemaPrefix + "GROUPS A";

    // getNumberOfUsersForOrganisationSQL
    getNumberOfUsersForOrganisationSQL = "SELECT COUNT(A.ID) FROM " + schemaPrefix + "USERS A, "
        + schemaPrefix + "USER_ORGANISATION_MAP B"
        + " WHERE A.ID = B.USER_ID AND B.ORGANISATION_ID=?";

    // getOrganisationIdSQL
    getOrganisationIdSQL = "SELECT ID FROM " + schemaPrefix + "ORGANISATIONS"
        + " WHERE UPPER(CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // getOrganisationSQL
    getOrganisationSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS WHERE UPPER(CODE)=UPPER(CAST(? AS VARCHAR(100)))";

    // getOrganisationsForUserSQL
    getOrganisationsForUserSQL = "SELECT A.ID, A.CODE, A.NAME, A.DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS A, " + schemaPrefix + "USER_ORGANISATION_MAP B"
        + " WHERE A.ID = B.ORGANISATION_ID AND B.USER_ID=? ORDER BY A.NAME";

    // getOrganisationsSQL
    getOrganisationsSQL = "SELECT ID, CODE, NAME, DESCRIPTION FROM " + schemaPrefix
        + "ORGANISATIONS ORDER BY NAME";

    // getUserIdSQL
    getUserIdSQL = "SELECT ID FROM " + schemaPrefix + "USERS"
        + " WHERE UPPER(USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getUserIdsForGroupSQL
    getUserIdsForGroupSQL = "SELECT A.ID FROM " + schemaPrefix + "USERS A, " + schemaPrefix
        + "USER_GROUP_MAP B WHERE A.ID = B.USER_ID AND B.GROUP_ID=?";

    // getUserSQL
    getUserSQL = "SELECT ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES,"
        + " LAST_NAME, PHONE, FAX,  MOBILE, EMAIL, PASSWORD_ATTEMPTS,"
        + " PASSWORD_EXPIRY, DESCRIPTION FROM " + schemaPrefix + "USERS"
        + " WHERE UPPER(USERNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getUsersForOrganisationSQL
    getUsersForOrganisationSQL = "SELECT A.ID, A.USERNAME, A.PASSWORD,"
        + " A.TITLE, A.FIRST_NAMES, A.LAST_NAME, A.PHONE, A.FAX, A.MOBILE,"
        + " A.EMAIL, A.PASSWORD_ATTEMPTS, A.PASSWORD_EXPIRY, A.DESCRIPTION FROM " + schemaPrefix
        + "USERS A, " + schemaPrefix + "USER_ORGANISATION_MAP B"
        + " WHERE A.ID = B.USER_ID AND B.ORGANISATION_ID=?" + " ORDER BY A.USERNAME";

    // getUsersSQL
    getUsersSQL = "SELECT ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES,"
        + " LAST_NAME, PHONE, FAX, MOBILE, EMAIL, PASSWORD_ATTEMPTS,"
        + " PASSWORD_EXPIRY, DESCRIPTION FROM " + schemaPrefix + "USERS";

    // grantFunctionToGroupSQL
    grantFunctionToGroupSQL = "INSERT INTO " + schemaPrefix + "FUNCTION_GROUP_MAP"
        + " (GROUP_ID, FUNCTION_ID) VALUES (?, ?)";

    // grantFunctionToUserSQL
    grantFunctionToUserSQL = "INSERT INTO " + schemaPrefix + "FUNCTION_USER_MAP"
        + " (USER_ID, FUNCTION_ID, ORGANISATION_ID) VALUES (?, ?, ?)";

    // insertIDGeneratorSQL
    insertIDGeneratorSQL = "INSERT INTO " + schemaPrefix + "IDGENERATOR"
        + " (CURRENT, NAME) VALUES (?, ?)";

    // isGroupGrantedFunctionSQL
    isGroupGrantedFunctionSQL = "SELECT GROUP_ID FROM " + schemaPrefix
        + "FUNCTION_GROUP_MAP WHERE GROUP_ID=? AND FUNCTION_ID=?";

    // isPasswordInHistorySQL
    isPasswordInHistorySQL = "SELECT ID FROM " + schemaPrefix + "PASSWORD_HISTORY"
        + " WHERE USER_ID=? AND CHANGED > ? AND PASSWORD=?";

    // isUserAssociatedWithOrganisationSQL
    isUserAssociatedWithOrganisationSQL = "SELECT USER_ID FROM " + schemaPrefix
        + "USER_ORGANISATION_MAP" + " WHERE USER_ID=? AND ORGANISATION_ID=?";

    // isUserGrantedFunctionSQL
    isUserGrantedFunctionSQL = "SELECT USER_ID FROM " + schemaPrefix + "FUNCTION_USER_MAP"
        + " WHERE USER_ID=? AND FUNCTION_ID=? AND ORGANISATION_ID=?";

    // isUserInGroupSQL
    isUserInGroupSQL = "SELECT USER_ID FROM " + schemaPrefix + "USER_GROUP_MAP"
        + " WHERE USER_ID=? AND GROUP_ID=? AND ORGANISATION_ID=?";

    // removeFunctionFromTemplateSQL
    removeFunctionFromTemplateSQL = "DELETE FROM " + schemaPrefix + "FUNCTION_TEMPLATE_MAP"
        + " WHERE FUNCTION_ID=? AND TEMPLATE_ID=?";

    // removeUserFromGroupSQL
    removeUserFromGroupSQL = "DELETE FROM " + schemaPrefix + "USER_GROUP_MAP"
        + " WHERE USER_ID=? AND GROUP_ID=? AND ORGANISATION_ID=?";

    // removeUserFromOrganisationSQL
    removeUserFromOrganisationSQL = "DELETE FROM " + schemaPrefix + "USER_ORGANISATION_MAP"
        + " WHERE USER_ID=? AND ORGANISATION_ID=?";

    // revokeFunctionForGroupSQL
    revokeFunctionForGroupSQL = "DELETE FROM " + schemaPrefix + "FUNCTION_GROUP_MAP"
        + " WHERE GROUP_ID=? AND FUNCTION_ID=?";

    // revokeFunctionForUserSQL
    revokeFunctionForUserSQL = "DELETE FROM " + schemaPrefix + "FUNCTION_USER_MAP"
        + " WHERE USER_ID=? AND FUNCTION_ID=? AND ORGANISATION_ID=?";

    // savePasswordHistorySQL
    savePasswordHistorySQL = "INSERT INTO " + schemaPrefix + "PASSWORD_HISTORY"
        + " (ID, USER_ID, CHANGED, PASSWORD) VALUES (?, ?, ?, ?)";

    // selectIDGeneratorSQL
    selectIDGeneratorSQL = "SELECT CURRENT FROM " + schemaPrefix + "IDGENERATOR" + " WHERE NAME=?";

    // updateFunctionSQL
    updateFunctionSQL = "UPDATE " + schemaPrefix + "FUNCTIONS"
        + " SET NAME=?, DESCRIPTION=? WHERE CODE=?";

    // updateFunctionTemplateSQL
    updateFunctionTemplateSQL = "UPDATE " + schemaPrefix + "FUNCTION_TEMPLATES"
        + " SET NAME=?, DESCRIPTION=? WHERE CODE=?";

    // updateGroupSQL
    updateGroupSQL = "UPDATE " + schemaPrefix + "GROUPS"
        + " SET DESCRIPTION=? WHERE UPPER(GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // updateIDGeneratorSQL
    updateIDGeneratorSQL = "UPDATE " + schemaPrefix + "IDGENERATOR"
        + " SET CURRENT = CURRENT + 1 WHERE NAME=?";

    // updateOrganisationSQL
    updateOrganisationSQL = "UPDATE " + schemaPrefix + "ORGANISATIONS"
        + " SET NAME=?, DESCRIPTION=? WHERE CODE=?";
  }

  /**
   * Returns the database catalog separator used when accessing the database.
   *
   * @return the database catalog separator used when accessing the database
   */
  protected String getDatabaseCatalogSeparator()
  {
    return databaseCatalogSeparator;
  }

  /**
   * Returns the numeric ID for the group with the specified group name.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the group
   *
   * @return the numeric ID for the group or -1 if a group with the specified group name could not
   *         be found
   *
   * @throws SecurityException
   */
  protected synchronized long getGroupId(Connection connection, String groupName)
    throws SecurityException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getGroupIdSQL);
      statement.setString(1, groupName);
      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getLong(1);
      }
      else
      {
        return -1;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the numeric ID for the group (" + groupName
          + ")", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
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
  protected synchronized long getOrganisationId(Connection connection, String code)
    throws SecurityException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getOrganisationIdSQL);
      statement.setString(1, code);
      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getLong(1);
      }
      else
      {
        return -1;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the numeric ID for the organisation (" + code
          + ")", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Returns the numeric ID for the user with the specified username.
   *
   * @param connection the existing database connection to use
   * @param username   the username uniquely identifying the user
   *
   * @return the numeric ID for the user or -1 if a user with the specified username could not be
   *         found
   *
   * @throws SecurityException
   */
  protected synchronized long getUserId(Connection connection, String username)
    throws SecurityException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getUserIdSQL);
      statement.setString(1, username);
      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getLong(1);
      }
      else
      {
        return -1;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the numeric ID for the user (" + username
          + ")", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Build the JDBC <code>PreparedStatement</code> for the SQL query that will select the users
   * in the USER table using the values of the specified attributes as the selection criteria.
   *
   * @param connection the existing database connection to use
   * @param attributes the attributes to be used as the selection criteria
   *
   * @return the <code>PreparedStatement</code> for the SQL query that will select the users in the
   *         USER table using the values of the specified attributes as the selection criteria
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

    buffer.append("SELECT ID, USERNAME, PASSWORD, TITLE, FIRST_NAMES, ");
    buffer.append("LAST_NAME, PHONE, FAX, MOBILE, EMAIL, ");
    buffer.append("PASSWORD_ATTEMPTS, PASSWORD_EXPIRY, DESCRIPTION FROM ");

    buffer.append(DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA).append(
        databaseCatalogSeparator);

    buffer.append("USERS");

    if (attributes.size() > 0)
    {
      // Build the parameters for the "WHERE" clause for the SQL statement
      StringBuilder whereParameters = new StringBuilder();

      for (Attribute attribute : attributes)
      {
        if (whereParameters.length() > 0)
        {
          whereParameters.append(" AND ");
        }

        if (attribute.getName().equalsIgnoreCase("description"))
        {
          whereParameters.append("DESCRIPTION LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("email"))
        {
          whereParameters.append("EMAIL LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("faxNumber"))
        {
          whereParameters.append("FAX LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("firstNames"))
        {
          whereParameters.append("FIRST_NAMES LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("lastName"))
        {
          whereParameters.append("LAST_NAME LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
        {
          whereParameters.append("MOBILE LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
        {
          whereParameters.append("PHONE LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("title"))
        {
          whereParameters.append("TITLE LIKE ?");
        }
        else if (attribute.getName().equalsIgnoreCase("username"))
        {
          whereParameters.append("USERNAME LIKE ?");
        }
        else
        {
          throw new InvalidAttributeException("The attribute (" + attribute.getName()
              + ") is invalid");
        }
      }

      buffer.append(" WHERE ");
      buffer.append(whereParameters.toString());
    }

    PreparedStatement statement = connection.prepareStatement(buffer.toString());

    // Set the parameters for the prepared statement
    int parameterIndex = 1;

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
   * Create a SHA-1 has of the specified password.
   *
   * @param password the password to hash
   *
   * @return the SHA-1 hash of the password
   *
   * @throws SecurityException
   */
  private String createPasswordHash(String password)
    throws SecurityException
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("SHA-1");

      md.update(password.getBytes("iso-8859-1"), 0, password.length());

      return Base64.encodeBytes(md.digest());
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to generate a SHA-1 hash of the password (" + password
          + "): " + e.getMessage(), e);
    }
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
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getFunctionIdSQL);
      statement.setString(1, code);
      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getLong(1);
      }
      else
      {
        return -1;
      }
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Returns the numeric ID for the function template with the specified code.
   *
   * @param connection the existing database connection to use
   * @param code       the code uniquely identifying the function template
   *
   * @return the numeric ID for the function template or -1 if a function template with the
   *         specified name could not be found
   *
   * @throws SQLException
   */
  private long getFunctionTemplateId(Connection connection, String code)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getFunctionTemplateIdSQL);
      statement.setString(1, code);
      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getLong(1);
      }
      else
      {
        return -1;
      }
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Retrieve all the authorised functions associated with the function template with the specified
   * numeric ID.
   *
   * @param templateId the numeric ID for the function template
   *
   * @return the list of authorised functions
   *
   * @throws SQLException
   */
  private List<Function> getFunctionsForTemplate(long templateId)
    throws SQLException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(getFunctionsForTemplateSQL);
      statement.setLong(1, templateId);
      rs = statement.executeQuery();

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
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the names for all the groups that the user with the specific numeric ID is associated
   * with for the specified organisation.
   *
   * @param connection     the existing database connection
   * @param userId         the numeric ID for the user
   * @param organisationId the numeric ID for the organisation
   *
   * @return the list of groups
   *
   * @throws SQLException
   */
  private List<String> getGroupNamesForUser(Connection connection, long userId, long organisationId)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getGroupNamesForUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);
      rs = statement.executeQuery();

      List<String> list = new ArrayList<>();

      while (rs.next())
      {
        list.add(rs.getString(1));
      }

      return list;
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Retrieve all the groups that the user with the specific numeric ID is associated with for the
   * specified organisation.
   *
   * @param connection     the existing database connection
   * @param userId         the numeric ID for the user
   * @param organisationId the numeric ID for the organisation
   *
   * @return the list of groups
   *
   * @throws SQLException
   */
  private List<Group> getGroupsForUser(Connection connection, long userId, long organisationId)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getGroupsForUserSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);
      rs = statement.executeQuery();

      List<Group> list = new ArrayList<>();

      while (rs.next())
      {
        Group group = new Group(rs.getString(2));

        group.setId(rs.getLong(1));
        group.setDescription(rs.getString(3));
        list.add(group);
      }

      return list;
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Retrieve the information for the user with the specified username.
   *
   * @param connection the existing database connection to use
   * @param username   the username identifying the user
   *
   * @return the <code>User</code> or <code>null</code> if the user could not be found
   *
   * @throws SQLException
   */
  private User getUser(Connection connection, String username)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getUserSQL);
      statement.setString(1, username);
      rs = statement.executeQuery();

      if (rs.next())
      {
        return buildUserFromResultSet(rs);
      }
      else
      {
        return null;
      }
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Retrieve the IDs for all the users that are associated with the group with the specific
   * numeric ID.
   *
   * @param connection the existing database connection
   * @param groupId    the numeric ID for the group
   *
   * @return the IDs for all the users that are associated with the group the specific numeric ID
   *
   * @throws SQLException
   */
  private List<Long> getUserIdsForGroup(Connection connection, long groupId)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(getUserIdsForGroupSQL);
      statement.setLong(1, groupId);
      rs = statement.executeQuery();

      List<Long> list = new ArrayList<>();

      while (rs.next())
      {
        list.add(rs.getLong(1));
      }

      return list;
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
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
    try
    {
      if (!registry.integerValueExists("/SecurityService", "MaxPasswordAttempts"))
      {
        registry.setIntegerValue("/SecurityService", "MaxPasswordAttempts",
            DEFAULT_MAX_PASSWORD_ATTEMPTS);
      }

      if (!registry.integerValueExists("/SecurityService", "PasswordExpiryMonths"))
      {
        registry.setIntegerValue("/SecurityService", "PasswordExpiryMonths",
            DEFAULT_PASSWORD_EXPIRY_MONTHS);
      }

      if (!registry.integerValueExists("/SecurityService", "PasswordHistoryMonths"))
      {
        registry.setIntegerValue("/SecurityService", "PasswordHistoryMonths",
            DEFAULT_PASSWORD_HISTORY_MONTHS);
      }

      maxPasswordAttempts = registry.getIntegerValue("/SecurityService", "MaxPasswordAttempts",
          DEFAULT_MAX_PASSWORD_ATTEMPTS);
      passwordExpiryMonths = registry.getIntegerValue("/SecurityService", "PasswordExpiryMonths",
          DEFAULT_PASSWORD_EXPIRY_MONTHS);
      passwordHistoryMonths = registry.getIntegerValue("/SecurityService", "PasswordHistoryMonths",
          DEFAULT_PASSWORD_HISTORY_MONTHS);
    }
    catch (Throwable e)
    {
      throw new SecurityException(
          "Failed to initialise the configuration for the SecurityService instance: "
          + e.getMessage(), e);
    }
  }

  /**
   * Has the group been granted the function?
   *
   * @param connection the existing database connection
   * @param groupId    the numeric ID uniquely identifying the group
   * @param functionId the numeric ID uniquely identifying the function
   *
   * @return true if the user is a member of the group or false otherwise
   *
   * @throws SQLException
   */
  private boolean isGroupGrantedFunction(Connection connection, long groupId, long functionId)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(isGroupGrantedFunctionSQL);
      statement.setLong(1, groupId);
      statement.setLong(2, functionId);
      rs = statement.executeQuery();

      return rs.next();
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
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

  /**
   * Is the password, given by the specified password hash, a historical password that cannot
   * be reused for a period of time i.e. was the password used previously in the last X months.
   * Where X is a configuration value retrieved from the registry.
   *
   * @param connection   the existing database connection
   * @param userId       the numeric ID uniquely identifying the user
   * @param passwordHash the password hash
   *
   * @return <code>true</code> if the password was previously used and cannot be reused for a
   *         period of time or <code>false</code> otherwise
   *
   * @throws SQLException
   */
  private boolean isPasswordInHistory(Connection connection, long userId, String passwordHash)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      Calendar calendar = Calendar.getInstance();

      calendar.setTime(new Date());
      calendar.add(Calendar.MONTH, -1 * passwordHistoryMonths);
      statement = connection.prepareStatement(isPasswordInHistorySQL);
      statement.setLong(1, userId);
      statement.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
      statement.setString(3, passwordHash);
      rs = statement.executeQuery();

      return rs.next();
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Is the user associated with the organisation?
   *
   * @param connection     the existing database connection
   * @param userId         the numeric ID uniquely identifying the user
   * @param organisationId the numeric ID uniquely identifying the organisation
   *
   * @return <code>true</code> if the user is associated with the organisation or <code>false</code>
   *         otherwise
   *
   * @throws SQLException
   */
  private boolean isUserAssociatedWithOrganisation(Connection connection, long userId,
      long organisationId)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(isUserAssociatedWithOrganisationSQL);
      statement.setLong(1, userId);
      statement.setLong(2, organisationId);
      rs = statement.executeQuery();

      return rs.next();
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Has the user been granted the function?
   *
   * @param connection     the existing database connection
   * @param userId         the numeric ID uniquely identifying the user
   * @param functionId     the numeric ID uniquely identifying the function
   * @param organisationId the numeric ID uniquely identifying the organisation
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   *
   * @throws SQLException
   */
  private boolean isUserGrantedFunction(Connection connection, long userId, long functionId,
      long organisationId)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(isUserGrantedFunctionSQL);
      statement.setLong(1, userId);
      statement.setLong(2, functionId);
      statement.setLong(3, organisationId);
      rs = statement.executeQuery();

      return rs.next();
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  /**
   * Is the user in the group?
   *
   * @param connection     the existing database connection
   * @param userId         the numeric ID uniquely identifying the user
   * @param groupId        the numeric ID uniquely identifying the group
   * @param organisationId the numeric ID uniquely identifying the organisation
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   *
   * @throws SQLException
   */
  private boolean isUserInGroup(Connection connection, long userId, long groupId,
      long organisationId)
    throws SQLException
  {
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      statement = connection.prepareStatement(isUserInGroupSQL);
      statement.setLong(1, userId);
      statement.setLong(2, groupId);
      statement.setLong(3, organisationId);
      rs = statement.executeQuery();

      return rs.next();
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
    }
  }

  private void savePasswordHistory(Connection connection, long userId, String passwordHash)
    throws SQLException
  {
    PreparedStatement statement = null;

    try
    {
      long id = nextId("Application.UserPasswordHistoryId");

      statement = connection.prepareStatement(savePasswordHistorySQL);
      statement.setLong(1, id);
      statement.setLong(2, userId);
      statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      statement.setString(4, passwordHash);
      statement.execute();
    }
    finally
    {
      DAOUtil.close(statement);
    }
  }
}
