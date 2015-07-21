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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;

/**
 * The <code>ISecurityService</code> interface defines the functionality provided by the
 * Security Service which manages security related information for users and groups.
 *
 * @author Marcus Portmann
 */
public interface ISecurityService
{
  /**
   * The possible reasons for why a user's password was changed.
   */
  public enum PasswordChangeReason
  {
    USER(0, "User"), ADMINISTRATIVE(1, "Administrative"), FORGOTTEN(2, "Forgotten");

    private String description;
    private int id;

    PasswordChangeReason(int id, String description)
    {
      this.id = id;
      this.description = description;
    }

    /**
     * Returns the description for the password change reason.
     *
     * @return the description for the password change reason
     */
    public String description()
    {
      return description;
    }

    /**
     * Returns the numeric identifier for the password change reason.
     *
     * @return the numeric identifier for the password change reason
     */
    public int id()
    {
      return id;
    }
  }

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
      FunctionTemplateNotFoundException, SecurityException;

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
      SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws UserNotFoundException, SecurityException;

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
      UserNotFoundException, SecurityException;

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
      UserNotFoundException, SecurityException;

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
      UserNotFoundException, ExistingPasswordException, SecurityException;

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
    throws DuplicateFunctionException, SecurityException;

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
    throws DuplicateFunctionTemplateException, SecurityException;

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
    throws DuplicateGroupException, SecurityException;

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
    throws DuplicateOrganisationException, SecurityException;

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
    throws DuplicateUserException, SecurityException;

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
    throws FunctionNotFoundException, SecurityException;

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
    throws FunctionTemplateNotFoundException, SecurityException;

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
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException;

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
    throws OrganisationNotFoundException, SecurityException;

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
    throws UserNotFoundException, SecurityException;

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
    throws InvalidAttributeException, SecurityException;

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   * @param startPos   the position in the list of users to start from
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
    throws InvalidAttributeException, SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws OrganisationNotFoundException, SecurityException;

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
    throws FunctionNotFoundException, SecurityException;

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
    throws GroupNotFoundException, SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws FunctionTemplateNotFoundException, SecurityException;

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
    throws SecurityException;

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
    throws SecurityException;

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
    throws GroupNotFoundException, SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws GroupNotFoundException, SecurityException;

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
    throws GroupNotFoundException, SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

  /**
   * Retrieve all the groups.
   *
   * @param origin the origin of the request e.g. the IP address, subnetwork or workstation name
   *               for the remote client
   *
   * @return a list of <code>Group</code> containing the group information
   *
   * @throws SecurityException
   */
  public List<Group> getGroups(String origin)
    throws SecurityException;

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
    throws SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws OrganisationNotFoundException, SecurityException;

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
    throws SecurityException;

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
    throws OrganisationNotFoundException, SecurityException;

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
    throws OrganisationNotFoundException, SecurityException;

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
    throws SecurityException;

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
    throws UserNotFoundException, SecurityException;

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
    throws UserNotFoundException, SecurityException;

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
    throws UserNotFoundException, SecurityException;

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
    throws SecurityException;

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
    throws SecurityException;

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
    throws GroupNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws OrganisationNotFoundException, SecurityException;

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
    throws GroupNotFoundException, FunctionNotFoundException, SecurityException;

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
      SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
      SecurityException;

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
    throws FunctionNotFoundException, FunctionTemplateNotFoundException, SecurityException;

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
      SecurityException;

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
    throws UserNotFoundException, OrganisationNotFoundException, SecurityException;

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
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException;

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
    throws GroupNotFoundException, FunctionNotFoundException, SecurityException;

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
      SecurityException;

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
    throws FunctionNotFoundException, SecurityException;

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
    throws FunctionTemplateNotFoundException, SecurityException;

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
    throws GroupNotFoundException, SecurityException;

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
    throws OrganisationNotFoundException, SecurityException;

  /**
   * Update the existing user.
   *
   * @param user           the <code>User</code> instance containing the updated user information
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser       lock the user as part of the update
   * @param origin         the origin of the request e.g. the IP address, subnetwork or workstation
   *                       name for the remote client
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  public void updateUser(User user, boolean expirePassword, boolean lockUser, String origin)
    throws UserNotFoundException, SecurityException;
}
