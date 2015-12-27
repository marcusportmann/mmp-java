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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.registry.IRegistry;
import guru.mmp.application.security.*;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;

import javax.inject.Inject;

/**
 * The <code>SecurityServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>SecurityService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class SecurityServiceTest
{
  private static int functionCount;
  private static int groupCount;
  private static int organisationCount;
  private static int userCount;
  private static int userDirectoryCount;
  @Inject
  private IRegistry registry;
  @Inject
  private ISecurityService securityService;

  /**
   * Test the functionality to add a user to a group.
   *
   * @throws Exception
   */
  @Test
  public void addUserToGroupTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getGroupName());

    List<String> groupNames = securityService.getGroupNamesForUser(userDirectory.getId(),
      user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getGroupName() + ")", group.getGroupName(), groupNames.get(0));

    List<Group> groups = securityService.getGroupsForUser(userDirectory.getId(),
      user.getUsername());

    assertEquals("The correct number of groups (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groups.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getGroupName() + ")", group.getGroupName(), groups.get(0).getGroupName());
  }

  /**
   * Test the administrative change user password functionality.
   *
   * @throws Exception
   */
  @Test
  public void adminChangePasswordTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.adminChangePassword(userDirectory.getId(), user.getUsername(), "Password2",
        false, false, true, PasswordChangeReason.ADMINISTRATIVE);
    securityService.authenticate(user.getUsername(), "Password2");
  }

  /**
   * Test the change user password functionality.
   *
   * @throws Exception
   */
  @Test
  public void changePasswordTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.changePassword(userDirectory.getId(), user.getUsername(), user.getPassword(),
        "Password2");
    securityService.authenticate(user.getUsername(), "Password2");
  }

  /**
   * Test the functionality to delete a group with existing members.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.ExistingGroupMembersException.class)
  public void deleteGroupWithExistingMembers()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getGroupName());

    List<String> groupNames = securityService.getGroupNamesForUser(userDirectory.getId(),
      user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getGroupName() + ")", group.getGroupName(), groupNames.get(0));
    securityService.deleteGroup(userDirectory.getId(), group.getGroupName());
  }

  /**
   * Test the delete invalid function functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.FunctionNotFoundException.class)
  public void deleteInvalidFunctionTest()
    throws Exception
  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);
    securityService.deleteFunction("INVALID");
  }

  /**
   * Test the delete invalid group functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.GroupNotFoundException.class)
  public void deleteInvalidGroupTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);
    securityService.deleteGroup(userDirectory.getId(), "INVALID");
  }

  /**
   * Test the delete invalid user functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.UserNotFoundException.class)
  public void deleteInvalidUserTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.deleteUser(userDirectory.getId(), "INVALID");
  }

  /**
   * Test the duplicate function functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.DuplicateFunctionException.class)
  public void duplicateFunctionTest()
    throws Exception
  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);
    securityService.createFunction(function);
  }

  /**
   * Test the duplicate group functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.DuplicateGroupException.class)
  public void duplicateGroupTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);
    securityService.createGroup(userDirectory.getId(), group);
  }

  /**
   * Test the duplicate organisation functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.DuplicateOrganisationException.class)
  public void duplicateOrganisationTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, false);
    securityService.createOrganisation(organisation, false);
  }

  /**
   * Test the duplicate user functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.DuplicateUserException.class)
  public void duplicateUserTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.createUser(userDirectory.getId(), user, false, false);
  }

  /**
   * Test the expired user password functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.ExpiredPasswordException.class)
  public void expiredUserPasswordTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.adminChangePassword(userDirectory.getId(), user.getUsername(), "Password2",
        true, false, true, PasswordChangeReason.ADMINISTRATIVE);
    securityService.authenticate(user.getUsername(), "Password2");
  }

  /**
   * Test the find users functionality.
   *
   * @throws Exception
   */
  @Test
  public void findUsersTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    for (int i = 1; i < 20; i++)
    {
      User user = getNumberedTestUserDetails(i);

      securityService.createUser(userDirectory.getId(), user, false, false);
    }

    List<User> retrievedUsersAll = securityService.getUsers(userDirectory.getId());

    assertEquals("The correct number of users (19) was not retrieved", 19,
        retrievedUsersAll.size());

    List<Attribute> attributes = new ArrayList<>();

    attributes.add(new Attribute("description", "%Description 1%"));
    attributes.add(new Attribute("email", "%E-Mail 1%"));
    attributes.add(new Attribute("faxNumber", "%Fax Number 1%"));
    attributes.add(new Attribute("firstNames", "%FirstName 1%"));
    attributes.add(new Attribute("lastName", "%LastName 1%"));
    attributes.add(new Attribute("mobileNumber", "%Mobile Number 1%"));
    attributes.add(new Attribute("phoneNumber", "%Phone Number 1%"));
    attributes.add(new Attribute("title", "%Title 1%"));
    attributes.add(new Attribute("username", "%Username 1%"));

    List<User> retrievedUsers = securityService.findUsers(userDirectory.getId(), attributes);

    assertEquals("The correct number of users (11) was not retrieved matching the search criteria",
        11, retrievedUsers.size());
  }

  /**
   * Test the create function functionality.
   *
   * @throws Exception
   */
  @Test
  public void functionTest()
    throws Exception
  {
    Function function = getTestFunctionDetails();

    List<Function> beforeRetrievedFunctions = securityService.getFunctions();

    securityService.createFunction(function);

    Function retrievedFunction = securityService.getFunction(function.getCode());

    compareFunctions(function, retrievedFunction);

    List<Function> afterRetrievedFunctions = securityService.getFunctions();

    assertEquals("The correct number of functions (" + (beforeRetrievedFunctions.size() + 1)
        + ") was not retrieved", beforeRetrievedFunctions.size() + 1,
          afterRetrievedFunctions.size());

    boolean foundFunction = false;

    for (Function afterRetrievedFunction : afterRetrievedFunctions)
    {
      if (afterRetrievedFunction.getCode().equals(function.getCode()))
      {
        compareFunctions(function, afterRetrievedFunction);

        foundFunction = true;

        break;
      }
    }

    if (!foundFunction)
    {
      fail("Failed to find the function (" + function.getCode() + ") in the list of functions");
    }

    function.setName("Test Updated Function Name");
    function.setDescription("Test Updated Function Description");
    securityService.updateFunction(function);

    Function retrievedUpdatedFunction = securityService.getFunction(function.getCode());

    compareFunctions(function, retrievedUpdatedFunction);

    securityService.deleteFunction(function.getCode());

    try
    {
      securityService.getFunction(function.getCode());

      fail("Retrieved the function (" + function.getCode() + ") that should have been deleted");
    }
    catch (FunctionNotFoundException ignore) {}
  }

  /**
   * Test the functionality to retrieve the authorised function codes for the user.
   *
   * @throws Exception
   */
  @Test
  public void getFunctionCodesForUserTest()
    throws Exception
  {
    User user = getTestUserDetails();

    securityService.createUser(SecurityService.DEFAULT_USER_DIRECTORY_ID, user, false, false);

    securityService.addUserToGroup(SecurityService.DEFAULT_USER_DIRECTORY_ID, user.getUsername(),
        "Administrators");

    List<String> groupNamesForUser =
      securityService.getGroupNamesForUser(SecurityService.DEFAULT_USER_DIRECTORY_ID,
        user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNamesForUser.size());

    List<String> functionCodesForUser =
      securityService.getFunctionCodesForUser(SecurityService.DEFAULT_USER_DIRECTORY_ID,
        user.getUsername());

    List<Function> functions = securityService.getFunctions();

    assertEquals("The correct number of function codes (" + functions.size()
        + ") was not retrieved for the user (" + user.getUsername() + ")", functions.size(),
          functionCodesForUser.size());
  }

  /**
   * Test the group functionality.
   *
   * @throws Exception
   */
  @Test
  public void groupTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    Group retrievedGroup = securityService.getGroup(userDirectory.getId(), group.getGroupName());

    compareGroups(group, retrievedGroup);

    int numberOfGroups = securityService.getNumberOfGroups(userDirectory.getId());

    assertEquals("The correct number of groups (1) was not retrieved", 1, numberOfGroups);

    List<Group> retrievedGroups = securityService.getGroups(userDirectory.getId());

    assertEquals("The correct number of groups (1) was not retrieved", 1, retrievedGroups.size());

    compareGroups(group, retrievedGroups.get(0));

    group.setDescription("Test Updated Group Description");
    securityService.updateGroup(userDirectory.getId(), group);

    Group retrievedUpdatedGroup = securityService.getGroup(userDirectory.getId(),
      group.getGroupName());

    compareGroups(group, retrievedUpdatedGroup);

    securityService.deleteGroup(userDirectory.getId(), group.getGroupName());

    try
    {
      securityService.getGroup(userDirectory.getId(), group.getGroupName());

      fail("Retrieved the group (" + group.getGroupName() + ") that should have been deleted");
    }
    catch (GroupNotFoundException ignored) {}
  }

  /**
   * Test the functionality to check whether a user is a member of a group.
   *
   * @throws Exception
   */
  @Test
  public void isUserInGroupTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getGroupName());
    assertEquals("Could not determine that the user (" + user.getUsername()
        + ") is a member of the group (" + group.getGroupName() + ")", true,
          securityService.isUserInGroup(userDirectory.getId(), user.getUsername(),
            group.getGroupName()));
  }

  /**
   * Test the locked user functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.UserLockedException.class)
  public void lockedUserTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.adminChangePassword(userDirectory.getId(), user.getUsername(), "Password2",
        false, true, true, PasswordChangeReason.ADMINISTRATIVE);
    securityService.authenticate(user.getUsername(), "Password2");
  }

  /**
   * Test the organisation functionality.
   *
   * @throws Exception
   */
  @Test
  public void organisationTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    List<Organisation> beforeRetrievedOrganisations = securityService.getOrganisations();

    securityService.createOrganisation(organisation, false);

    Organisation retrievedOrganisation = securityService.getOrganisation(organisation.getId());

    compareOrganisations(organisation, retrievedOrganisation);

    int numberOfOrganisations = securityService.getNumberOfOrganisations();

    assertEquals("The correct number of organisations ("
        + (beforeRetrievedOrganisations.size() + 1)
        + ") was not retrieved", beforeRetrievedOrganisations.size() + 1, numberOfOrganisations);

    List<Organisation> afterRetrievedOrganisations = securityService.getOrganisations();

    assertEquals("The correct number of organisations ("
        + (beforeRetrievedOrganisations.size() + 1)
        + ") was not retrieved", beforeRetrievedOrganisations.size()
          + 1, afterRetrievedOrganisations.size());

    boolean foundOrganisation = false;

    for (Organisation afterRetrievedOrganisation : afterRetrievedOrganisations)
    {
      if (afterRetrievedOrganisation.getId().equals(organisation.getId()))
      {
        compareOrganisations(organisation, afterRetrievedOrganisation);

        foundOrganisation = true;

        break;
      }
    }

    if (!foundOrganisation)
    {
      fail("Failed to find the organisation (" + organisation.getId()
          + ") in the list of organisations");
    }

    organisation.setName("Updated " + organisation.getName());
    organisation.setDescription("Updated " + organisation.getDescription());

    securityService.updateOrganisation(organisation);

    retrievedOrganisation = securityService.getOrganisation(organisation.getId());

    compareOrganisations(organisation, retrievedOrganisation);

    securityService.deleteOrganisation(organisation.getId());

    try
    {
      securityService.getOrganisation(organisation.getId());

      fail("Retrieved the organisation (" + organisation.getId()
          + ") that should have been deleted");
    }
    catch (OrganisationNotFoundException ignored) {}
  }

  /**
   * Test the reload user directories functionality.
   *
   * @throws Exception
   */
  @Test
  public void reloadUserDirectoriesTest()
    throws Exception
  {
    securityService.reloadUserDirectories();
  }

  /**
   * Test the functionality to remove a user from a group.
   *
   * @throws Exception
   */
  @Test
  public void removeUserFromGroupTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getGroupName());

    List<String> groupNames = securityService.getGroupNamesForUser(userDirectory.getId(),
      user.getUsername());

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getGroupName() + ")", group.getGroupName(), groupNames.get(0));
    securityService.removeUserFromGroup(userDirectory.getId(), user.getUsername(),
        group.getGroupName());
    groupNames = securityService.getGroupNamesForUser(userDirectory.getId(), user.getUsername());

    assertEquals("The correct number of group names (0) was not retrieved for the user ("
        + user.getUsername() + ")", 0, groupNames.size());
  }

  /**
   * Test the retrieve user directory types functionality.
   *
   * @throws Exception
   */
  @Test
  public void retrieveUserDirectoryTypesTest()
    throws Exception
  {
    List<UserDirectoryType> userDirectoryTypes = securityService.getUserDirectoryTypes();

    assertEquals("The correct number of user directory types () was not retrieved", 2,
        userDirectoryTypes.size());

    boolean foundInternalUserDirectoryType = false;

    for (UserDirectoryType userDirectoryType : userDirectoryTypes)
    {
      if (userDirectoryType.getId().equals(SecurityService.INTERNAL_USER_DIRECTORY_TYPE_ID))
      {
        foundInternalUserDirectoryType = true;

        break;
      }
    }

    if (!foundInternalUserDirectoryType)
    {
      fail("Failed to find the internal user directory type ("
          + SecurityService.INTERNAL_USER_DIRECTORY_TYPE_ID
          + ") in the list of user directory types");
    }

    boolean foundLdapUserDirectoryType = false;

    for (UserDirectoryType userDirectoryType : userDirectoryTypes)
    {
      if (userDirectoryType.getId().equals(SecurityService.LDAP_USER_DIRECTORY_TYPE_ID))
      {
        foundLdapUserDirectoryType = true;

        break;
      }
    }

    if (!foundLdapUserDirectoryType)
    {
      fail("Failed to find the internal user directory type ("
          + SecurityService.LDAP_USER_DIRECTORY_TYPE_ID + ") in the list of user directory types");
    }
  }

  /**
   * Test the user directory organisation mapping functionality.
   *
   * @throws Exception
   */
  @Test
  public void userDirectoryOrganisationMappingTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    List<Organisation> organisationsForUserDirectory =
      securityService.getOrganisationsForUserDirectory(userDirectory.getId());

    assertEquals(
        "The correct number of organisations (1) was not retrieved for the user directory", 1,
        organisationsForUserDirectory.size());

    List<UUID> organisationIdsForUserDirectory =
      securityService.getOrganisationIdsForUserDirectory(userDirectory.getId());

    assertEquals(
        "The correct number of organisation IDs (1) was not retrieved for the user directory", 1,
        organisationIdsForUserDirectory.size());

    List<UserDirectory> userDirectoriesForOrganisation =
      securityService.getUserDirectoriesForOrganisation(organisation.getId());

    assertEquals(
        "The correct number of user directories (1) was not retrieved for the organisation", 2,
        userDirectoriesForOrganisation.size());
  }

  /**
   * Test the user directory functionality.
   *
   * @throws Exception
   */
  @Test
  public void userDirectoryTest()
    throws Exception
  {
    List<UserDirectory> beforeRetrievedUserDirectories = securityService.getUserDirectories();

    UserDirectory userDirectory = getTestUserDirectoryDetails();

    securityService.createUserDirectory(userDirectory);

    UserDirectory retrievedUserDirectory = securityService.getUserDirectory(userDirectory.getId());

    compareUserDirectories(userDirectory, retrievedUserDirectory);

    int numberOfUserDirectories = securityService.getNumberOfUserDirectories();

    assertEquals("The correct number of user directories ("
        + (beforeRetrievedUserDirectories.size() + 1)
        + ") was not retrieved", beforeRetrievedUserDirectories.size()
          + 1, numberOfUserDirectories);

    List<UserDirectory> afterRetrievedUserDirectories = securityService.getUserDirectories();

    assertEquals("The correct number of user directories ("
        + (beforeRetrievedUserDirectories.size() + 1)
        + ") was not retrieved", beforeRetrievedUserDirectories.size()
          + 1, afterRetrievedUserDirectories.size());

    boolean foundUserDirectory = false;

    for (UserDirectory afterRetrievedUserDirectory : afterRetrievedUserDirectories)
    {
      if (afterRetrievedUserDirectory.getId().equals(userDirectory.getId()))
      {
        compareUserDirectories(userDirectory, afterRetrievedUserDirectory);

        foundUserDirectory = true;

        break;
      }
    }

    if (!foundUserDirectory)
    {
      fail("Failed to find the user directory (" + userDirectory.getId()
          + ") in the list of organisations");
    }

    userDirectory.setName("Updated " + userDirectory.getName());
    userDirectory.setDescription("Updated " + userDirectory.getDescription());

    securityService.updateUserDirectory(userDirectory);

    retrievedUserDirectory = securityService.getUserDirectory(userDirectory.getId());

    compareUserDirectories(userDirectory, retrievedUserDirectory);

    securityService.deleteUserDirectory(userDirectory.getId());

    try
    {
      securityService.getUserDirectory(userDirectory.getId());

      fail("Retrieved the user directory (" + userDirectory.getId()
          + ") that should have been deleted");
    }
    catch (UserDirectoryNotFoundException ignored) {}
  }

///**
// * Test the functionality to revoke a function for a group.
// *
// * @throws Exception
// */
//////@Test
//public void revokeFunctionForGroupTest()
//  throws Exception
//
//{
//  Group group = getTestGroupDetails();
//
//  securityService.createGroup(group);
//
//  Function function = getTestFunctionDetails();
//
//  securityService.createFunction(function);
//  securityService.grantFunctionToGroup(group.getGroupName(), function.getCode());
//
//  List<String> functionCodes = securityService.getFunctionCodesForGroup(group.getGroupName(),
//    origin);
//
//  assertEquals("The correct number of function codes (1) was not retrieved for the group ("
//      + group.getGroupName() + ")", 1, functionCodes.size());
//  assertEquals("The function (" + function.getCode() + ") was not assigned to the group ("
//      + group.getGroupName() + ")", function.getCode(), functionCodes.get(0));
//  securityService.revokeFunctionForGroup(group.getGroupName(), function.getCode());
//  functionCodes = securityService.getFunctionCodesForGroup(group.getGroupName());
//  assertEquals("The correct number of function codes (0) was not retrieved for the group ("
//      + group.getGroupName() + ")", 0, functionCodes.size());
//}

  /**
   * Test the user password history functionality.
   *
   * @throws Exception
   */
  @Test(expected = guru.mmp.application.security.ExistingPasswordException.class)
  public void userPasswordHistoryTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.changePassword(userDirectory.getId(), user.getUsername(), user.getPassword(),
        "Password1");
    securityService.changePassword(userDirectory.getId(), user.getUsername(), "Password1",
        "Password2");
    securityService.changePassword(userDirectory.getId(), user.getUsername(), "Password2",
        "Password1");
  }

  /**
   * Test the user functionality.
   *
   * @throws Exception
   */
  @Test
  public void userTest()
    throws Exception
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);

    UUID userDirectoryId = securityService.getUserDirectoryIdForUser(user.getUsername());

    assertEquals("The correct user directory ID was not retrieved for the user",
        userDirectory.getId(), userDirectoryId);

    User retrievedUser = securityService.getUser(userDirectory.getId(), user.getUsername());

    compareUsers(user, retrievedUser, false);

    int numberOfUsers = securityService.getNumberOfUsers(userDirectory.getId());

    assertEquals("The correct number of users (1) was not retrieved", 1, numberOfUsers);

    List<User> retrievedUsers = securityService.getUsers(userDirectory.getId());

    assertEquals("The correct number of users (1) was not retrieved", 1, retrievedUsers.size());

    compareUsers(user, retrievedUsers.get(0), true);

    int numberOfFilteredUsers = securityService.getNumberOfFilteredUsers(userDirectory.getId(),
      "Test");

    assertEquals("The correct number of filtered users (1) was not retrieved", 1,
        numberOfFilteredUsers);

    List<User> retrievedFilteredUsers = securityService.getFilteredUsers(userDirectory.getId(),
      "Test");

    assertEquals("The correct number of filtered users (1) was not retrieved", 1,
        retrievedFilteredUsers.size());

    compareUsers(user, retrievedFilteredUsers.get(0), true);

    Calendar calendar = Calendar.getInstance();

    calendar.setTime(new Date());
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    user.setPassword("Test Updated Password");
    user.setPasswordExpiry(calendar.getTime());
    user.setPasswordAttempts(2);
    user.setEmail("Test Updated E-Mail");
    user.setDescription("Test Updated User Description");
    user.setTitle("Test Updated Title");
    user.setFirstNames("Test Updated FirstName");
    user.setLastName("Test Updated LastName");
    user.setPhoneNumber("Test Updated Phone Number");
    user.setMobileNumber("Test Updated Mobile Number");
    user.setFaxNumber("Test Updated Fax Number");

    securityService.updateUser(userDirectory.getId(), user, false, false);

    User retrievedUpdatedUser = securityService.getUser(userDirectory.getId(), user.getUsername());

    compareUsers(user, retrievedUpdatedUser, true);

    securityService.deleteUser(userDirectory.getId(), user.getUsername());

    try
    {
      securityService.getUser(userDirectory.getId(), user.getUsername());

      fail("Retrieved the user (" + user.getUsername() + ") that should have been deleted");
    }
    catch (UserNotFoundException ignored) {}
  }

  private static synchronized User getNumberedTestUserDetails(int number)
  {
    User user = new User();

    user.setUsername("Numbered Test Username " + number);
    user.setPassword("Numbered Test Password " + number);
    user.setEmail("Numbered Test E-Mail " + number);
    user.setDescription("Numbered Test User Description " + number);
    user.setTitle("Numbered Test Title " + number);
    user.setFirstNames("Numbered Test FirstName " + number);
    user.setLastName("Numbered Test LastName " + number);
    user.setPhoneNumber("Numbered Test Phone Number " + number);
    user.setMobileNumber("Numbered Test Mobile Number " + number);
    user.setFaxNumber("Numbered Test Fax Number " + number);

    return user;
  }

  private static synchronized Function getTestFunctionDetails()
  {
    functionCount++;

    Function function = new Function();

    function.setId(UUID.randomUUID());
    function.setCode("Test Function Code " + functionCount);
    function.setName("Test Function Name " + functionCount);
    function.setDescription("Test Function Description " + functionCount);

    return function;
  }

  private static synchronized Group getTestGroupDetails()
  {
    groupCount++;

    Group group = new Group("Test Group " + groupCount);

    group.setDescription("Test Group Description " + groupCount);

    return group;
  }

  private static synchronized Organisation getTestOrganisationDetails()
  {
    organisationCount++;

    Organisation organisation = new Organisation();
    organisation.setId(UUID.randomUUID());
    organisation.setName("Test Organisation Name " + organisationCount);
    organisation.setDescription("Test Organisation Description " + organisationCount);

    return organisation;
  }

  private static synchronized User getTestUserDetails()
  {
    userCount++;

    User user = new User();

    user.setUsername("Test User Username " + userCount);
    user.setPassword("Test User Password " + userCount);
    user.setEmail("Test User E-Mail " + userCount);
    user.setDescription("Test User Description " + userCount);
    user.setTitle("Test User Title " + userCount);
    user.setFirstNames("Test User FirstName " + userCount);
    user.setLastName("Test User LastName " + userCount);
    user.setPhoneNumber("Test User Phone Number " + userCount);
    user.setMobileNumber("Test User Mobile Number " + userCount);
    user.setFaxNumber("Test User Fax Number " + userCount);

    return user;
  }

  private static synchronized UserDirectory getTestUserDirectoryDetails()
    throws Exception
  {
    userDirectoryCount++;

    UserDirectory userDirectory = new UserDirectory();

    userDirectory.setId(UUID.randomUUID());
    userDirectory.setTypeId(UUID.fromString("b43fda33-d3b0-4f80-a39a-110b8e530f4f"));
    userDirectory.setName("Test User Directory Name " + userDirectoryCount);
    userDirectory.setDescription("Test User Directory Description " + userDirectoryCount);

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

  private void compareFunctions(Function function1, Function function2)
  {
    assertEquals("The code values for the two functions do not match", function1.getCode(),
        function2.getCode());
    assertEquals("The description values for the two functions do not match",
        function1.getDescription(), function2.getDescription());
    assertEquals("The ID values for the two functions do not match", function1.getId(),
        function2.getId());
    assertEquals("The name values for the two functions do not match", function1.getName(),
        function2.getName());
  }

  private void compareGroups(Group group1, Group group2)
  {
    assertEquals("The description values for the two groups do not match", group1.getDescription(),
        group2.getDescription());
    assertEquals("The group name values for the two groups do not match", group1.getGroupName(),
        group2.getGroupName());
    assertEquals("The ID values for the two groups do not match", group1.getId(), group2.getId());
  }

  private void compareOrganisations(Organisation organisation1, Organisation organisation2)
  {
    assertEquals("The ID values for the two organisations do not match", organisation1.getId(),
        organisation2.getId());
    assertEquals("The name values for the two organisations do not match", organisation1.getName(),
        organisation2.getName());
    assertEquals("The description values for the two organisations do not match",
        organisation1.getDescription(), organisation2.getDescription());
  }

  private void compareUserDirectories(UserDirectory userDirectory1, UserDirectory userDirectory2)
  {
    assertEquals("The ID values for the two user directories do not match", userDirectory1.getId(),
        userDirectory2.getId());
    assertEquals("The name values for the two user directories do not match",
        userDirectory1.getName(), userDirectory2.getName());
    assertEquals("The type ID values for the two user directories do not match",
        userDirectory1.getTypeId(), userDirectory2.getTypeId());
    assertEquals("The description values for the two user directories do not match",
        userDirectory1.getDescription(), userDirectory2.getDescription());
    assertEquals("The configuration values for the two user directories do not match",
        userDirectory1.getConfiguration(), userDirectory2.getConfiguration());
  }

  private void compareUsers(User user1, User user2, boolean checkPasswordExpiry)
  {
    if (checkPasswordExpiry)
    {
      assertEquals("The password expiry values for the two users do not match",
          user1.getPasswordExpiry(), user2.getPasswordExpiry());
    }

    assertEquals("The description values for the two users do not match", user1.getDescription(),
        user2.getDescription());
    assertEquals("The e-mail values for the two users do not match", user1.getEmail(),
        user2.getEmail());
    assertEquals("The fax number values for the two users do not match", user1.getFaxNumber(),
        user2.getFaxNumber());
    assertEquals("The first names values for the two users do not match", user1.getFirstNames(),
        user2.getFirstNames());
    assertEquals("The ID values for the two users do not match", user1.getId(), user2.getId());
    assertEquals("The mobile number values for the two users do not match",
        user1.getMobileNumber(), user2.getMobileNumber());
    assertEquals("The password attempt values for the two users do not match",
        user1.getPasswordAttempts(), user2.getPasswordAttempts());
    assertEquals("The phone number values for the two users do not match", user1.getPhoneNumber(),
        user2.getPhoneNumber());
    assertEquals("The title values for the two users do not match", user1.getTitle(),
        user2.getTitle());
    assertEquals("The username values for the two users do not match", user1.getUsername(),
        user2.getUsername());
  }

  private Function getAnotherTestFunctionDetails()
  {
    Function function = new Function();
    function.setId(UUID.randomUUID());
    function.setCode("Another Test Function Code");
    function.setName("Another Test Function Name");
    function.setDescription("Another Test Function Description");

    return function;
  }
}
