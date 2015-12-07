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
@SuppressWarnings("unused")
public interface ISecurityService
{
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
  void addUserToGroup(long userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
      SecurityException;

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
  void adminChangePassword(long userDirectoryId, String username, String newPassword,
      boolean expirePassword, boolean lockUser, boolean resetPasswordHistory,
      PasswordChangeReason reason)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException;

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
  long authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
      UserNotFoundException, SecurityException;

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
  void changePassword(long userDirectoryId, String username, String password, String newPassword)
    throws UserDirectoryNotFoundException, AuthenticationFailedException, UserLockedException,
      ExpiredPasswordException, UserNotFoundException, ExistingPasswordException, SecurityException;

  /**
   * Create a new authorised function.
   *
   * @param function the function
   *
   * @throws DuplicateFunctionException
   * @throws SecurityException
   */
  void createFunction(Function function)
    throws DuplicateFunctionException, SecurityException;

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
  void createGroup(long userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, DuplicateGroupException, SecurityException;

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
  UserDirectory createOrganisation(Organisation organisation, boolean createUserDirectory)
    throws DuplicateOrganisationException, SecurityException;

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
  void createUser(long userDirectoryId, User user, boolean expiredPassword, boolean userLocked)
    throws UserDirectoryNotFoundException, DuplicateUserException, SecurityException;

  /**
   * Delete the authorised function.
   *
   * @param code the code identifying the authorised function
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  void deleteFunction(String code)
    throws FunctionNotFoundException, SecurityException;

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
  void deleteGroup(long userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, ExistingGroupMembersException,
      SecurityException;

  /**
   * Delete the organisation.
   *
   * @param code the code uniquely identifying the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  void deleteOrganisation(String code)
    throws OrganisationNotFoundException, SecurityException;

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
  void deleteUser(long userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException;

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
  List<User> findUsers(long userDirectoryId, List<Attribute> attributes)
    throws UserDirectoryNotFoundException, InvalidAttributeException, SecurityException;

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
  List<User> findUsersEx(long userDirectoryId, List<Attribute> attributes, int startPos,
      int maxResults)
    throws UserDirectoryNotFoundException, InvalidAttributeException, SecurityException;

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
  List<User> getFilteredUsers(long userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityException;

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
  Function getFunction(String code)
    throws FunctionNotFoundException, SecurityException;

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
  List<String> getFunctionCodesForUser(long userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException;

  /**
   * Retrieve all the authorised functions.
   *
   * @return the list of authorised functions
   *
   * @throws SecurityException
   */
  List<Function> getFunctions()
    throws SecurityException;

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
  Group getGroup(long userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityException;

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
  List<String> getGroupNamesForUser(long userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException;

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
  List<Group> getGroups(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException;

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
  List<Group> getGroupsEx(long userDirectoryId, int startPos, int maxResults)
    throws UserDirectoryNotFoundException, SecurityException;

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
  List<Group> getGroupsForUser(long userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException;

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
  int getNumberOfFilteredUsers(long userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityException;

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
  int getNumberOfGroups(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException;

  /**
   * Retrieve the number of organisations
   *
   * @return the number of organisations
   *
   * @throws SecurityException
   */
  int getNumberOfOrganisations()
    throws SecurityException;

  /**
   * Retrieve the number of user directories
   *
   * @return the number of user directories
   *
   * @throws SecurityException
   */
  int getNumberOfUserDirectories()
    throws SecurityException;

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
  int getNumberOfUsers(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException;

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
  Organisation getOrganisation(String code)
    throws OrganisationNotFoundException, SecurityException;

  /**
   * Retrieve the organisations.
   *
   * @return the list of organisations
   *
   * @throws SecurityException
   */
  List<Organisation> getOrganisations()
    throws SecurityException;

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
  List<Organisation> getOrganisationsForUserDirectory(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException;

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
  User getUser(long userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException;

  /**
   * Retrieve the user directories.
   *
   * @return the list of user directories
   *
   * @throws SecurityException
   */
  List<UserDirectory> getUserDirectories()
    throws SecurityException;

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
  List<UserDirectory> getUserDirectoriesForOrganisation(String code)
    throws OrganisationNotFoundException, SecurityException;

  /**
   * Retrieve the user directories with parameters.
   *
   * @return the list of user directories with parameters
   *
   * @throws SecurityException
   */
  List<UserDirectory> getUserDirectoriesWithParameters()
    throws SecurityException;

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the unique ID for the user directory
   *
   * @return the user directory
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  UserDirectory getUserDirectory(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException;

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
  long getUserDirectoryIdForUser(String username)
    throws SecurityException;

  /**
   * Retrieve the supported user directory types.
   *
   * @return the supported user directory types
   */
  List<UserDirectoryType> getUserDirectoryTypes();

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
  List<User> getUsers(long userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityException;

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
  List<User> getUsersEx(long userDirectoryId, int startPos, int maxResults)
    throws UserDirectoryNotFoundException, SecurityException;

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
  boolean isUserInGroup(long userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
      SecurityException;

  /**
   * Reload the user directories.
   */
  void reloadUserDirectories();

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
  void removeUserFromGroup(long userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
      SecurityException;

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
  void renameGroup(long userDirectoryId, String groupName, String newGroupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, ExistingGroupMembersException,
      SecurityException;

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
  boolean supportsGroupAdministration(long userDirectoryId)
    throws UserDirectoryNotFoundException;

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
  boolean supportsUserAdministration(long userDirectoryId)
    throws UserDirectoryNotFoundException;

  /**
   * Update the authorised function.
   *
   * @param function the function
   *
   * @throws FunctionNotFoundException
   * @throws SecurityException
   */
  void updateFunction(Function function)
    throws FunctionNotFoundException, SecurityException;

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
  void updateGroup(long userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityException;

  /**
   * Update the organisation.
   *
   * @param organisation the organisation
   *
   * @throws OrganisationNotFoundException
   * @throws SecurityException
   */
  void updateOrganisation(Organisation organisation)
    throws OrganisationNotFoundException, SecurityException;

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
  void updateUser(long userDirectoryId, User user, boolean expirePassword, boolean lockUser)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException;
}
