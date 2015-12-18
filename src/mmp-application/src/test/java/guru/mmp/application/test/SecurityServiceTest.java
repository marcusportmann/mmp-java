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
  @Inject
  private IRegistry registry;
  @Inject
  private ISecurityService securityService;

  /**
   * Test the functionality to add a user to a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void addUserToGroupTest()
    throws guru.mmp.application.security.SecurityException
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
  }

  /**
   * Test the administrative change user password functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void adminChangePasswordTest()
    throws guru.mmp.application.security.SecurityException
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
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void changePasswordTest()
    throws guru.mmp.application.security.SecurityException
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
   * Test the create function functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void createFunctionTest()
    throws guru.mmp.application.security.SecurityException
  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);

    Function retrievedFunction = securityService.getFunction(function.getCode());

    compareFunctions(function, retrievedFunction);
  }

  /**
   * Test the create group functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void createGroupTest()
    throws guru.mmp.application.security.SecurityException
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    Group retrievedGroup = securityService.getGroup(userDirectory.getId(), group.getGroupName());

    compareGroups(group, retrievedGroup);
  }

  /**
   * Test the create organisation functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void createOrganisationTest()
    throws guru.mmp.application.security.SecurityException
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Organisation retrievedOrganisation = securityService.getOrganisation(organisation.getCode());

    compareOrganisations(organisation, retrievedOrganisation);
  }

  /**
   * Test the create user functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void createUserTest()
    throws guru.mmp.application.security.SecurityException
  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);

    User retrievedUser = securityService.getUser(userDirectory.getId(), user.getUsername());

    compareUsers(user, retrievedUser, false);
  }

  /**
   * Test the delete function functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void deleteFunctionTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);
    securityService.deleteFunction(function.getCode());

    try
    {
      securityService.getFunction(function.getCode());

      fail("Retrieved the function (" + function.getCode() + ") that should have been deleted");
    }
    catch (FunctionNotFoundException ignored) {}

  }

  /**
   * Test the delete group functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void deleteGroupTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);
    securityService.deleteGroup(userDirectory.getId(), group.getGroupName());

    try
    {
      securityService.getGroup(userDirectory.getId(), group.getGroupName());

      fail("Retrieved the group (" + group.getGroupName() + ") that should have been deleted");
    }
    catch (GroupNotFoundException ignored) {}

  }

  /**
   * Test the functionality to delete a group with existing members.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.ExistingGroupMembersException.class)
  public void deleteGroupWithExistingMembers()
    throws guru.mmp.application.security.SecurityException

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
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test(expected = guru.mmp.application.security.FunctionNotFoundException.class)
  public void deleteInvalidFunctionTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);
    securityService.deleteFunction("INVALID");
  }

  /**
   * Test the delete invalid group functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.GroupNotFoundException.class)
  public void deleteInvalidGroupTest()
    throws guru.mmp.application.security.SecurityException
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
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test(expected = guru.mmp.application.security.UserNotFoundException.class)
  public void deleteInvalidUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.deleteUser(userDirectory.getId(), "INVALID");
  }

  /**
   * Test the delete organisation by ID functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void deleteOrganisationByIdTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, false);
    securityService.deleteOrganisation(organisation.getCode());

    try
    {
      securityService.getOrganisation(organisation.getCode());

      fail("Retrieved the organisation (" + organisation.getId()
          + ") that should have been deleted");
    }
    catch (OrganisationNotFoundException ignored) {}
  }

  /**
   * Test the delete organisation functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  public void deleteOrganisationTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, false);

    securityService.deleteOrganisation(organisation.getCode());

    try
    {
      securityService.getOrganisation(organisation.getCode());

      fail("Retrieved the organisation (" + organisation.getId()
          + ") that should have been deleted");
    }
    catch (OrganisationNotFoundException ignored) {}
  }

  /**
   * Test the delete user functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void deleteUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.deleteUser(userDirectory.getId(), user.getUsername());

    try
    {
      securityService.getUser(userDirectory.getId(), user.getUsername());

      fail("Retrieved the user (" + user.getUsername() + ") that should have been deleted");
    }
    catch (UserNotFoundException ignored) {}
  }

  /**
   * Test the expired user password functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test(expected = guru.mmp.application.security.ExpiredPasswordException.class)
  public void expiredUserPasswordTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);
    securityService.adminChangePassword(userDirectory.getId(), user.getUsername(), "Password2",
        true, false, true, PasswordChangeReason.ADMINISTRATIVE);
    securityService.authenticate(user.getUsername(), "Password2");
  }

///**
// * Test the find users functionality.
// *
// * @throws guru.mmp.application.security.SecurityException
// */
////@Test
//public void findUsersTest()
//  throws guru.mmp.application.security.SecurityException
//
//{
//  for (int i = 1; i < 20; i++)
//  {
//    User user = getNumberedTestUserDetails(i);
//
//    securityService.createUser(userDirectory.getId(), user, false,
//        false);
//  }
//
//  List<Attribute> attributes = new ArrayList<>();
//
//  attributes.add(new Attribute("description", "%Description 1%"));
//  attributes.add(new Attribute("email", "%E-Mail 1%"));
//  attributes.add(new Attribute("faxNumber", "%Fax Number 1%"));
//  attributes.add(new Attribute("firstNames", "%FirstName 1%"));
//  attributes.add(new Attribute("lastName", "%LastName 1%"));
//  attributes.add(new Attribute("mobileNumber", "%Mobile Number 1%"));
//  attributes.add(new Attribute("phoneNumber", "%Phone Number 1%"));
//  attributes.add(new Attribute("title", "%Title 1%"));
//  attributes.add(new Attribute("username", "%Username 1%"));
//
//  List<User> retrievedUsers =
//    securityService.findUsers(userDirectory.getId(), attributes);
//
//  assertEquals("The correct number of users (11) was not retrieved matching the search criteria",
//      11, retrievedUsers.size());
//}

///**
// * Test the functionality to retrieve all the function codes for a user.
// *
// * @throws guru.mmp.application.security.SecurityException
// */
//@Test
//public void getAllFunctionCodesForUserTest()
//throws guru.mmp.application.security.SecurityException
//
//{
//Organisation organisation = getTestOrganisationDetails();
//
//securityService.createOrganisation(organisation);
//
//Group group = getTestGroupDetails();
//
//securityService.createGroup(userDirectory.getId(), group);
//
//User user = getTestUserDetails();
//
//securityService.createUser(userDirectory.getId(), user, false, false);
//securityService.addUserToGroup(userDirectory.getId(), user.getUsername(), group.getGroupName());
//
//Function function = getTestFunctionDetails();
//
//securityService.createFunction(function);
//securityService.grantFunctionToUser(userDirectory.getId(), user.getUsername(), function.getCode());
//
//Function anotherFunction = getAnotherTestFunctionDetails();
//
//securityService.createFunction(anotherFunction);
//securityService.grantFunctionToGroup(group.getGroupName(), anotherFunction.getCode());
//
//List<String> functionCodes = securityService.getAllFunctionCodesForUser(user.getUsername(),
//  organisation.getCode());
//
//assertEquals("The correct number of functions (2) was not retrieved for the user ("
//    + user.getUsername() + ")", 2, functionCodes.size());
//}

  /**
   * Test the get functions functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  // @Test
  public void getFunctionsTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);

    List<Function> retrievedFunctions = securityService.getFunctions();

    assertEquals("The correct number of functions (1) was not retrieved", 1,
        retrievedFunctions.size());
    compareFunctions(function, retrievedFunctions.get(0));
  }

  /**
   * Test the get groups functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void getGroupsTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);

    List<Group> retrievedGroups = securityService.getGroups(userDirectory.getId());

    assertEquals("The correct number of groups (1) was not retrieved", 1, retrievedGroups.size());
    compareGroups(group, retrievedGroups.get(0));
  }

  /**
   * Test the functionality to retrieve an organisation.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void getOrganisationTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, false);

    Organisation retrievedOrganisation = securityService.getOrganisation(organisation.getCode());

    compareOrganisations(organisation, retrievedOrganisation);

  }

  /**
   * Test the functionality to retrieve all the organisations.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void getOrganisationsTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, false);

    List<Organisation> retrievedOrganisations = securityService.getOrganisations();

    assertEquals("The correct number of organisations (2) was not retrieved", 2,
        retrievedOrganisations.size());

    for (Organisation retrievedOrganisation : retrievedOrganisations)
    {
      if (retrievedOrganisation.getCode().equals(organisation.getCode()))
      {
        compareOrganisations(organisation, retrievedOrganisation);

        return;
      }
    }

    fail("Failed to find the organisation (" + organisation.getCode()
        + ") in the list of organisations");
  }

  /**
   * Test the get users functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void getUsersTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);

    List<User> retrievedUsers = securityService.getUsers(userDirectory.getId());

    assertEquals("The correct number of users (2) was not retrieved", 2, retrievedUsers.size());

    for (User retrievedUser : retrievedUsers)
    {
      if (retrievedUser.getUsername().equals(user.getUsername()))
      {
        compareUsers(user, retrievedUser, false);

        return;
      }
    }

    fail("The test user was not found in the list of users retrieved");
  }

  /**
   * Test the functionality to check whether a user is a member of a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void isUserInGroupTest()
    throws guru.mmp.application.security.SecurityException

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
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test(expected = guru.mmp.application.security.UserLockedException.class)
  public void lockedUserTest()
    throws guru.mmp.application.security.SecurityException

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
   * Test the functionality to remove a user from a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void removeUserFromGroupTest()
    throws guru.mmp.application.security.SecurityException

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

///**
// * Test the functionality to revoke a function for a group.
// *
// * @throws guru.mmp.application.security.SecurityException
// */
//@Test
//public void revokeFunctionForGroupTest()
//  throws guru.mmp.application.security.SecurityException
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
   * Test the update function functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  // @Test
  public void updateFunctionTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function);
    function.setName("Test Updated Function Name");
    function.setDescription("Test Updated Function Description");
    securityService.updateFunction(function);

    Function retrievedFunction = securityService.getFunction(function.getCode());

    compareFunctions(function, retrievedFunction);
  }

  /**
   * Test the update group functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void updateGroupTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    Group group = getTestGroupDetails();

    securityService.createGroup(userDirectory.getId(), group);
    group.setDescription("Test Updated Group Description");
    securityService.updateGroup(userDirectory.getId(), group);

    Group retrievedGroup = securityService.getGroup(userDirectory.getId(), group.getGroupName());

    compareGroups(group, retrievedGroup);
  }

  /**
   * Test the user update functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test
  public void updateUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    UserDirectory userDirectory = securityService.createOrganisation(organisation, true);

    User user = getTestUserDetails();

    securityService.createUser(userDirectory.getId(), user, false, false);

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

    User retrievedUser = securityService.getUser(userDirectory.getId(), user.getUsername());

    compareUsers(user, retrievedUser, true);
  }

  /**
   * Test the user password history functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */

  // @Test(expected = guru.mmp.application.security.ExistingPasswordException.class)
  public void userPasswordHistoryTest()
    throws guru.mmp.application.security.SecurityException

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

  private static synchronized User getNumberedTestUserDetails(int number)
  {
    User user = new User("Test Username " + number);

    user.setPassword("Test Password " + number);
    user.setEmail("Test E-Mail " + number);
    user.setDescription("Test User Description " + number);
    user.setTitle("Test Title " + number);
    user.setFirstNames("Test FirstName " + number);
    user.setLastName("Test LastName " + number);
    user.setPhoneNumber("Test Phone Number " + number);
    user.setMobileNumber("Test Mobile Number " + number);
    user.setFaxNumber("Test Fax Number " + number);

    return user;
  }

  private static synchronized Function getTestFunctionDetails()
  {
    functionCount++;

    Function function = new Function("Test Function " + functionCount);

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

    Organisation organisation = new Organisation("Test Organisation " + organisationCount);

    organisation.setName("Test Organisation Name " + organisationCount);
    organisation.setDescription("Test Organisation Description " + organisationCount);

    return organisation;
  }

  private static synchronized User getTestUserDetails()
  {
    userCount++;

    User user = new User("Test Username " + userCount);

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
    Function function = new Function("Another Test Function Code");

    function.setName("Another Test Function Name");
    function.setDescription("Another Test Function Description");

    return function;
  }
}