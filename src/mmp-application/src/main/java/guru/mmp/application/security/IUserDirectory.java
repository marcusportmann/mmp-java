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
 * The <code>IUserDirectoryProvider</code> interface defines the functionality that must be
 * provided by a user directory, which manages users and groups.
 *
 * @author Marcus Portmann
 */
public interface IUserDirectory
{
  /**
   * The unique ID for the default internal user directory.
   */
  int DEFAULT_INTERNAL_USER_DIRECTORY_ID = 1;

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
  void addUserToGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException;

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
  void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason)
    throws UserNotFoundException, SecurityException;

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
  void authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
      UserNotFoundException, SecurityException;

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   *
   * @throws AuthenticationFailedException
   * @throws UserLockedException
   * @throws ExpiredPasswordException
   * @throws UserNotFoundException
   * @throws ExistingPasswordException
   * @throws SecurityException
   */
  void changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
      UserNotFoundException, ExistingPasswordException, SecurityException;

  /**
   * Create a new group.
   *
   * @param group the group
   *
   * @throws DuplicateGroupException
   * @throws SecurityException
   */
  void createGroup(Group group)
    throws DuplicateGroupException, SecurityException;

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
  void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityException;

  /**
   * Delete the group.
   *
   * @param groupName the name of the group uniquely identifying the group
   *
   * @throws GroupNotFoundException
   * @throws ExistingGroupMembersException
   * @throws SecurityException
   */
  void deleteGroup(String groupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException;

  /**
   * Delete the user.
   *
   * @param username the username identifying the user
   *
   * @throws UserNotFoundException
   * @throws SecurityException
   */
  void deleteUser(String username)
    throws UserNotFoundException, SecurityException;

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
  List<User> findUsers(List<Attribute> attributes)
    throws InvalidAttributeException, SecurityException;

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   * @param startPos   the position in the list of users to start from
   * @param maxResults the maximum number of results to return or -1 for all
   *
   * @return the list of users whose attributes match the attribute criteria
   *
   * @throws InvalidAttributeException
   * @throws SecurityException
   */
  List<User> findUsersEx(List<Attribute> attributes, int startPos, int maxResults)
    throws InvalidAttributeException, SecurityException;

  /**
   * Retrieve the filtered list of users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the filtered list of users
   *
   * @throws SecurityException
   */
  List<User> getFilteredUsers(String filter)
    throws SecurityException;

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
  List<String> getFunctionCodesForUser(String username)
    throws UserNotFoundException, SecurityException;

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
  Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityException;

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
  List<String> getGroupNamesForUser(String username)
    throws UserNotFoundException, SecurityException;

  /**
   * Retrieve all the groups.
   *
   * @return the list of groups
   *
   * @throws SecurityException
   */
  List<Group> getGroups()
    throws SecurityException;

  /**
   * Retrieve the groups.
   *
   * @param startPos   the position in the list of groups to start from
   * @param maxResults the maximum number of results to return or -1 for all
   *
   * @return the list of groups
   *
   * @throws SecurityException
   */
  List<Group> getGroupsEx(int startPos, int maxResults)
    throws SecurityException;

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
  List<Group> getGroupsForUser(String username)
    throws UserNotFoundException, SecurityException;

  /**
   * Retrieve the number of filtered users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the number of filtered users
   *
   * @throws SecurityException
   */
  int getNumberOfFilteredUsers(String filter)
    throws SecurityException;

  /**
   * Retrieve the number of groups
   *
   * @return the number of groups
   *
   * @throws SecurityException
   */
  int getNumberOfGroups()
    throws SecurityException;

  /**
   * Retrieve the number of users.
   *
   * @return the number of users
   *
   * @throws SecurityException
   */
  int getNumberOfUsers()
    throws SecurityException;

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
  User getUser(String username)
    throws UserNotFoundException, SecurityException;

  /**
   * Retrieve all the users.
   *
   * @return the list of users
   *
   * @throws SecurityException
   */
  List<User> getUsers()
    throws SecurityException;

  /**
   * Retrieve the users.
   *
   * @param startPos   the position in the list of users to start from
   * @param maxResults the maximum number of results to return or -1 for all
   *
   * @return the list of users
   *
   * @throws SecurityException
   */
  List<User> getUsersEx(int startPos, int maxResults)
    throws SecurityException;

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
  boolean isExistingUser(String username)
    throws SecurityException;

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
  boolean isUserInGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException;

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
  void removeUserFromGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityException;

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
  void renameGroup(String groupName, String newGroupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityException;

  /**
   * Does the user directory support administering groups.
   *
   * @return <code>true</code> if the directory supports administering groups or <code>false</code>
   *         otherwise
   */
  boolean supportsGroupAdministration();

  /**
   * Does the user directory support administering users.
   *
   * @return <code>true</code> if the directory supports administering users or <code>false</code>
   *         otherwise
   */
  boolean supportsUserAdministration();

  /**
   * Update the group.
   *
   * @param group the group
   *
   * @throws GroupNotFoundException
   * @throws SecurityException
   */
  void updateGroup(Group group)
    throws GroupNotFoundException, SecurityException;

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
  void updateUser(User user, boolean expirePassword, boolean lockUser)
    throws UserNotFoundException, SecurityException;
}
