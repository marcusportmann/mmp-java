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

import guru.mmp.application.persistence.HsqldbDataSource;
import guru.mmp.application.registry.Registry;
import guru.mmp.application.security.*;
import guru.mmp.application.security.ISecurityService.PasswordChangeReason;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The <code>SecurityServiceTests</code> class contains the implementation of the JUnit
 * tests for the <code>SecurityService</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SecurityServiceTests extends HsqldbDatabaseTests
{
  private HsqldbDataSource dataSource;
  private String origin = "127.0.0.1";
  private Registry registry;
  private SecurityService securityService;

  /**
   * Test the functionality to add a function to a function template.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void addFunctionToTemplateTest()
    throws guru.mmp.application.security.SecurityException
  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);

    FunctionTemplate template = getTestFunctionTemplateDetails();

    securityService.createFunctionTemplate(template, origin);
    securityService.addFunctionToTemplate(function.getCode(), template.getCode(), origin);

    FunctionTemplate retrievedTemplate = securityService.getFunctionTemplate(template.getCode(),
      origin);

    assertEquals(
        "The correct number of functions (1) was not retrieved for the function template ("
        + template.getCode() + ")", 1, retrievedTemplate.getFunctions().size());
    assertEquals("The function (" + function.getCode()
        + ") was not added to the function template (" + template.getCode()
        + ")", function.getCode(), retrievedTemplate.getFunctions().get(0).getCode());
  }

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

    securityService.createOrganisation(organisation, origin);

    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.addUserToGroup(user.getUsername(), group.getGroupName(),
        organisation.getCode(), origin);

    List<String> groupNames = securityService.getGroupNamesForUser(user.getUsername(),
      organisation.getCode(), origin);

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getGroupName() + ")", group.getGroupName(), groupNames.get(0));
  }

  /**
   * Test the functionality to add a user to an organisation.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void addUserToOrganisationTest()
    throws guru.mmp.application.security.SecurityException
  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    securityService.addUserToOrganisation(user.getUsername(), organisation.getCode(), origin);

    List<User> retrievedUsers = securityService.getUsersForOrganisation(organisation.getCode(),
      origin);

    assertEquals("The correct number of users (1) was not retrieved for the organisation ("
        + organisation.getCode() + ")", 1, retrievedUsers.size());

    compareUsers(user, retrievedUsers.get(0), false);
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
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.adminChangePassword(user.getUsername(), "Password2", false, false, true,
        PasswordChangeReason.ADMINISTRATIVE, origin);
    securityService.authenticate(user.getUsername(), "Password2", origin);
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
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.changePassword(user.getUsername(), user.getPassword(), "Password1", origin);
    securityService.authenticate(user.getUsername(), "Password1", origin);
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

    securityService.createFunction(function, origin);

    Function retrievedFunction = securityService.getFunction(function.getCode(), origin);

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
    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    Group retrievedGroup = securityService.getGroup(group.getGroupName(), origin);

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

    securityService.createOrganisation(organisation, origin);

    Organisation retrievedOrganisation = securityService.getOrganisation(organisation.getCode(),
      origin);

    compareOrganisations(organisation, retrievedOrganisation);
  }

  /**
   * Test the create function template functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void createTemplateTest()
    throws guru.mmp.application.security.SecurityException

  {
    FunctionTemplate template = getTestFunctionTemplateDetails();

    securityService.createFunctionTemplate(template, origin);

    FunctionTemplate retrievedTemplate = securityService.getFunctionTemplate(template.getCode(),
      origin);

    compareTemplates(template, retrievedTemplate);
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
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    User retrievedUser = securityService.getUser(user.getUsername(), origin);

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

    securityService.createFunction(function, origin);
    securityService.deleteFunction(function.getCode(), origin);

    try
    {
      securityService.getFunction(function.getCode(), origin);

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
    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);
    securityService.deleteGroup(group.getGroupName(), origin);

    try
    {
      securityService.getGroup(group.getGroupName(), origin);

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

    securityService.createOrganisation(organisation, origin);

    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.addUserToGroup(user.getUsername(), group.getGroupName(),
        organisation.getCode(), origin);

    List<String> groupNames = securityService.getGroupNamesForUser(user.getUsername(),
      organisation.getCode(), origin);

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getGroupName() + ")", group.getGroupName(), groupNames.get(0));
    securityService.deleteGroup(group.getGroupName(), origin);
  }

  /**
   * Test the delete invalid function functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.FunctionNotFoundException.class)
  public void deleteInvalidFunctionTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);
    securityService.deleteFunction("INVALID", origin);
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
    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);
    securityService.deleteGroup("INVALID", origin);
  }

  /**
   * Test the delete invalid function template functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.FunctionTemplateNotFoundException.class)
  public void deleteInvalidTemplateTest()
    throws guru.mmp.application.security.SecurityException

  {
    FunctionTemplate template = getTestFunctionTemplateDetails();

    securityService.createFunctionTemplate(template, origin);
    securityService.deleteFunctionTemplate("INVALID", origin);
  }

  /**
   * Test the delete invalid user functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.UserNotFoundException.class)
  public void deleteInvalidUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.deleteUser("INVALID", origin);
  }

  /**
   * Test the delete organisation by ID functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void deleteOrganisationByIdTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);
    securityService.deleteOrganisation(organisation.getCode(), origin);

    try
    {
      securityService.getOrganisation(organisation.getCode(), origin);

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

    securityService.createOrganisation(organisation, origin);

    securityService.deleteOrganisation(organisation.getCode(), origin);

    try
    {
      securityService.getOrganisation(organisation.getCode(), origin);

      fail("Retrieved the organisation (" + organisation.getId()
          + ") that should have been deleted");
    }
    catch (OrganisationNotFoundException ignored) {}
  }

  /**
   * Test the delete function template functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void deleteTemplateTest()
    throws guru.mmp.application.security.SecurityException

  {
    FunctionTemplate template = getTestFunctionTemplateDetails();

    securityService.createFunctionTemplate(template, origin);
    securityService.deleteFunctionTemplate(template.getCode(), origin);
  }

  /**
   * Test the delete user functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void deleteUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.deleteUser(user.getUsername(), origin);

    try
    {
      securityService.getUser(user.getUsername(), origin);

      fail("Retrieved the user (" + user.getUsername() + ") that should have been deleted");
    }
    catch (UserNotFoundException ignored) {}
  }

  /**
   * Test the expired user password functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.ExpiredPasswordException.class)
  public void expiredUserPasswordTest()
    throws guru.mmp.application.security.SecurityException

  {
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.adminChangePassword(user.getUsername(), "Password2", true, false, true,
        PasswordChangeReason.ADMINISTRATIVE, origin);
    securityService.authenticate(user.getUsername(), "Password2", origin);
  }

  /**
   * Test the find users functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void findUsersTest()
    throws guru.mmp.application.security.SecurityException

  {
    for (int i = 1; i < 20; i++)
    {
      User user = getNumberedTestUserDetails(i);

      securityService.createUser(user, false, false, origin);
    }

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

    List<User> retrievedUsers = securityService.findUsers(attributes, origin);

    assertEquals("The correct number of users (11) was not retrieved matching the search criteria",
        11, retrievedUsers.size());
  }

  /**
   * Test the functionality to revoke a function for a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getAllFunctionCodesForUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.addUserToGroup(user.getUsername(), group.getGroupName(),
        organisation.getCode(), origin);

    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);
    securityService.grantFunctionToUser(user.getUsername(), function.getCode(),
        organisation.getCode(), origin);

    Function anotherFunction = getAnotherTestFunctionDetails();

    securityService.createFunction(anotherFunction, origin);
    securityService.grantFunctionToGroup(group.getGroupName(), anotherFunction.getCode(), origin);

    List<String> functionCodes = securityService.getAllFunctionCodesForUser(user.getUsername(),
      organisation.getCode(), origin);

    assertEquals("The correct number of functions (2) was not retrieved for the user ("
        + user.getUsername() + ")", 2, functionCodes.size());
  }

  /**
   * Test the get functions functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getFunctionsTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);

    List<Function> retrievedFunctions = securityService.getFunctions(origin);

    assertEquals("The correct number of functions (1) was not retrieved", 1,
        retrievedFunctions.size());
    compareFunctions(function, retrievedFunctions.get(0));
  }

  /**
   * Test the get groups functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getGroupsTest()
    throws guru.mmp.application.security.SecurityException

  {
    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    List<Group> retrievedGroups = securityService.getGroups(origin);

    assertEquals("The correct number of groups (1) was not retrieved", 1, retrievedGroups.size());
    compareGroups(group, retrievedGroups.get(0));
  }

  /**
   * Test the functionality to retrieve an organisation.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getOrganisationTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    Organisation retrievedOrganisation = securityService.getOrganisation(organisation.getCode(),
      origin);

    compareOrganisations(organisation, retrievedOrganisation);

  }

  /**
   * Test the functionality to retrieve the organisations a user is associated with.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getOrganisationsForUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    securityService.addUserToOrganisation(user.getUsername(), organisation.getCode(), origin);

    List<Organisation> retrievedOrganisations =
      securityService.getOrganisationsForUser(user.getUsername(), origin);

    assertEquals("The correct number of organisations (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, retrievedOrganisations.size());

    compareOrganisations(organisation, retrievedOrganisations.get(0));
  }

  /**
   * Test the functionality to retrieve all the organisations.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getOrganisationsTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    List<Organisation> retrievedOrganisations = securityService.getOrganisations(origin);

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
   * Test the get function templates functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getTemplatesTest()
    throws guru.mmp.application.security.SecurityException

  {
    FunctionTemplate template = getTestFunctionTemplateDetails();

    securityService.createFunctionTemplate(template, origin);

    List<FunctionTemplate> retrievedTemplates = securityService.getFunctionTemplates(origin);

    assertEquals("The correct number of function templates (1) was not retrieved", 1,
        retrievedTemplates.size());
    compareTemplates(template, retrievedTemplates.get(0));
  }

  /**
   * Test the functionality to retrieve the users associated with an organisation.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getUsersForOrganisationTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    securityService.addUserToOrganisation(user.getUsername(), organisation.getCode(), origin);

    List<User> retrievedUsers = securityService.getUsersForOrganisation(organisation.getCode(),
      origin);

    assertEquals("The correct number of users (1) was not retrieved for the organisation ("
        + organisation.getCode() + ")", 1, retrievedUsers.size());

    compareUsers(user, retrievedUsers.get(0), false);
  }

  /**
   * Test the get users functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void getUsersTest()
    throws guru.mmp.application.security.SecurityException

  {
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    List<User> retrievedUsers = securityService.getUsers(origin);

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
   * Test the functionality to grant a function to a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void grantFunctionToGroupTest()
    throws guru.mmp.application.security.SecurityException

  {
    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);
    securityService.grantFunctionToGroup(group.getGroupName(), function.getCode(), origin);

    List<String> functionCodes = securityService.getFunctionCodesForGroup(group.getGroupName(),
      origin);

    assertEquals("The correct number of function codes (1) was not retrieved for the group ("
        + group.getGroupName() + ")", 1, functionCodes.size());
    assertEquals("The function (" + function.getCode() + ") was not assigned to the group ("
        + group.getGroupName() + ")", function.getCode(), functionCodes.get(0));

    List<Function> functions = securityService.getFunctionsForGroup(group.getGroupName(), origin);

    assertEquals("The correct number of functions (1) was not retrieved for the group ("
        + group.getGroupName() + ")", 1, functions.size());
    compareFunctions(function, functions.get(0));
  }

  /**
   * Test the functionality to grant a function to a user.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void grantFunctionToUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);
    securityService.grantFunctionToUser(user.getUsername(), function.getCode(),
        organisation.getCode(), origin);

    List<String> functionCodes = securityService.getFunctionCodesForUser(user.getUsername(),
      organisation.getCode(), origin);

    assertEquals("The correct number of function codes (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, functionCodes.size());
    assertEquals("The function (" + function.getCode() + ") was not assigned to the user ("
        + user.getUsername() + ")", function.getCode(), functionCodes.get(0));

    List<Function> functions = securityService.getFunctionsForUser(user.getUsername(),
      organisation.getCode(), origin);

    assertEquals("The correct number of functions (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, functions.size());
    compareFunctions(function, functions.get(0));
  }

  /**
   * Test the functionality to check whether a user is associated with an organisation.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void isUserAssociatedWithOrganisationTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    securityService.addUserToOrganisation(user.getUsername(), organisation.getCode(), origin);

    assertEquals("The user (" + user.getUsername() + ") is not associated with the organisation ("
        + organisation.getCode() + ")", true,
          securityService.isUserAssociatedWithOrganisation(user.getUsername(),
            organisation.getCode(), origin));
  }

  /**
   * Test the functionality to check whether a user is a member of a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void isUserInGroupTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.addUserToGroup(user.getUsername(), group.getGroupName(),
        organisation.getCode(), origin);
    assertEquals("Could not determine that the user (" + user.getUsername()
        + ") is a member of the group (" + group.getGroupName() + ")", true,
          securityService.isUserInGroup(user.getUsername(), group.getGroupName(),
            organisation.getCode(), origin));
  }

  /**
   * Test the locked user functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.UserLockedException.class)
  public void lockedUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.adminChangePassword(user.getUsername(), "Password2", false, true, true,
        PasswordChangeReason.ADMINISTRATIVE, origin);
    securityService.authenticate(user.getUsername(), "Password2", origin);
  }

  /**
   * Test the functionality to remove a function from a function template.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void removeFunctionFromFunctionTemplateTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);

    FunctionTemplate template = getTestFunctionTemplateDetails();

    securityService.createFunctionTemplate(template, origin);
    securityService.addFunctionToTemplate(function.getCode(), template.getCode(), origin);

    FunctionTemplate retrievedTemplate = securityService.getFunctionTemplate(template.getCode(),
      origin);

    assertEquals(
        "The correct number of functions (1) was not retrieved for the function template ("
        + template.getCode() + ")", 1, retrievedTemplate.getFunctions().size());
    assertEquals("The function (" + function.getCode()
        + ") was not added to the function template (" + template.getCode()
        + ")", function.getCode(), retrievedTemplate.getFunctions().get(0).getCode());
    securityService.removeFunctionFromTemplate(function.getCode(), template.getCode(), origin);
    retrievedTemplate = securityService.getFunctionTemplate(template.getCode(), origin);
    assertEquals(
        "The correct number of functions (0) was not retrieved for the function template ("
        + template.getCode() + ")", 0, retrievedTemplate.getFunctions().size());
  }

  /**
   * Test the functionality to remove a user from a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void removeUserFromGroupTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.addUserToGroup(user.getUsername(), group.getGroupName(),
        organisation.getCode(), origin);

    List<String> groupNames = securityService.getGroupNamesForUser(user.getUsername(),
      organisation.getCode(), origin);

    assertEquals("The correct number of group names (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, groupNames.size());
    assertEquals("The user (" + user.getUsername() + ") was not added to the group ("
        + group.getGroupName() + ")", group.getGroupName(), groupNames.get(0));
    securityService.removeUserFromGroup(user.getUsername(), group.getGroupName(),
        organisation.getCode(), origin);
    groupNames = securityService.getGroupNamesForUser(user.getUsername(), organisation.getCode(),
        origin);

    assertEquals("The correct number of group names (0) was not retrieved for the user ("
        + user.getUsername() + ")", 0, groupNames.size());
  }

  /**
   * Test the functionality to remove a user from an organisation.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void removeUserFromOrganisationTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    securityService.addUserToOrganisation(user.getUsername(), organisation.getCode(), origin);

    List<User> retrievedUsers = securityService.getUsersForOrganisation(organisation.getCode(),
      origin);

    assertEquals("The correct number of users (1) was not retrieved for the organisation ("
        + organisation.getCode() + ")", 1, retrievedUsers.size());

    compareUsers(user, retrievedUsers.get(0), false);

    securityService.removeUserFromOrganisation(user.getUsername(), organisation.getCode(), origin);

    retrievedUsers = securityService.getUsersForOrganisation(organisation.getCode(), origin);

    assertEquals("The correct number of users (0) was not retrieved for the organisation ("
        + organisation.getCode() + ")", 0, retrievedUsers.size());
  }

  /**
   * Test the functionality to revoke a function for a group.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void revokeFunctionForGroupTest()
    throws guru.mmp.application.security.SecurityException

  {
    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);

    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);
    securityService.grantFunctionToGroup(group.getGroupName(), function.getCode(), origin);

    List<String> functionCodes = securityService.getFunctionCodesForGroup(group.getGroupName(),
      origin);

    assertEquals("The correct number of function codes (1) was not retrieved for the group ("
        + group.getGroupName() + ")", 1, functionCodes.size());
    assertEquals("The function (" + function.getCode() + ") was not assigned to the group ("
        + group.getGroupName() + ")", function.getCode(), functionCodes.get(0));
    securityService.revokeFunctionForGroup(group.getGroupName(), function.getCode(), origin);
    functionCodes = securityService.getFunctionCodesForGroup(group.getGroupName(), origin);
    assertEquals("The correct number of function codes (0) was not retrieved for the group ("
        + group.getGroupName() + ")", 0, functionCodes.size());
  }

  /**
   * Test the functionality to revoke a function for a user.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void revokeFunctionForUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    Organisation organisation = getTestOrganisationDetails();

    securityService.createOrganisation(organisation, origin);

    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);
    securityService.grantFunctionToUser(user.getUsername(), function.getCode(),
        organisation.getCode(), origin);

    List<String> functionCodes = securityService.getFunctionCodesForUser(user.getUsername(),
      organisation.getCode(), origin);

    assertEquals("The correct number of function codes (1) was not retrieved for the user ("
        + user.getUsername() + ")", 1, functionCodes.size());
    assertEquals("The function (" + function.getCode() + ") was not assigned to the user ("
        + user.getUsername() + ")", function.getCode(), functionCodes.get(0));
    securityService.revokeFunctionForUser(user.getUsername(), function.getCode(),
        organisation.getCode(), origin);
    functionCodes = securityService.getFunctionCodesForUser(user.getUsername(),
        organisation.getCode(), origin);
    assertEquals("The correct number of function codes (0) was not retrieved for the user ("
        + user.getUsername() + ")", 0, functionCodes.size());
  }

  /**
   * This method is executed by the JUnit test infrastructure before a JUnit test is executed.
   * It is responsible for initialising the resources used by the tests e.g. an in-memory
   * HSQLDB database.
   *
   * @throws IOException
   * @throws SQLException
   */
  @Before
  public void setup()
    throws IOException, SQLException
  {
    // The SQL scripts that will be used to initialise the HSQLDB database
    List<String> resourcePaths = new ArrayList<>();

    resourcePaths.add("guru/mmp/application/registry/RegistryHsqldb.sql");
    resourcePaths.add("guru/mmp/application/security/SecurityServiceHsqldb.sql");

    // Initialise the in-memory HSQLDB database that will be used when executing a test
    dataSource = initDatabase("SecurityServiceTests", resourcePaths, false);

    // Create the database registry instance using the data source we have just setup
    registry = new Registry();

    registry.setDataSource(dataSource);

    registry.init();

    // Create the Security Service instance using the data source we have just setup
    securityService = new SecurityService();

    securityService.setDataSource(dataSource);

    securityService.setRegistry(registry);

    securityService.init();
  }

  /**
   * This method is executed by the JUnit test infrastructure after each JUnit test has been
   * executed. It is responsible for cleaning up the resources used by the tests e.g. an in-memory
   * HSQLDB database.
   *
   * @throws SQLException
   */
  @After
  public void tearDown()
    throws SQLException
  {
    if (dataSource != null)
    {
      try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement())
      {
        statement.execute("SHUTDOWN");
      }
      catch (Throwable ignored) {}

      dataSource = null;
    }

    registry = null;
  }

  /**
   * Test the update function functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void updateFunctionTest()
    throws guru.mmp.application.security.SecurityException

  {
    Function function = getTestFunctionDetails();

    securityService.createFunction(function, origin);
    function.setName("Test Updated Function Name");
    function.setDescription("Test Updated Function Description");
    securityService.updateFunction(function, origin);

    Function retrievedFunction = securityService.getFunction(function.getCode(), origin);

    compareFunctions(function, retrievedFunction);
  }

  /**
   * Test the update group functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void updateGroupTest()
    throws guru.mmp.application.security.SecurityException

  {
    Group group = getTestGroupDetails();

    securityService.createGroup(group, origin);
    group.setDescription("Test Updated Group Description");
    securityService.updateGroup(group, origin);

    Group retrievedGroup = securityService.getGroup(group.getGroupName(), origin);

    compareGroups(group, retrievedGroup);
  }

  /**
   * Test the update function template functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void updateTemplateTest()
    throws guru.mmp.application.security.SecurityException

  {
    FunctionTemplate template = getTestFunctionTemplateDetails();

    securityService.createFunctionTemplate(template, origin);
    template.setName("Test Updated Function Template Name");
    template.setDescription("Test Updated Function Template Description");
    securityService.updateFunctionTemplate(template, origin);

    FunctionTemplate retrievedTemplate = securityService.getFunctionTemplate(template.getCode(),
      origin);

    compareTemplates(template, retrievedTemplate);
  }

  /**
   * Test the user update functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test
  public void updateUserTest()
    throws guru.mmp.application.security.SecurityException

  {
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);

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
    securityService.updateUser(user, false, false, origin);

    User retrievedUser = securityService.getUser(user.getUsername(), origin);

    compareUsers(user, retrievedUser, true);
  }

  /**
   * Test the user password history functionality.
   *
   * @throws guru.mmp.application.security.SecurityException
   */
  @Test(expected = guru.mmp.application.security.ExistingPasswordException.class)
  public void userPasswordHistoryTest()
    throws guru.mmp.application.security.SecurityException

  {
    User user = getTestUserDetails();

    securityService.createUser(user, false, false, origin);
    securityService.changePassword(user.getUsername(), user.getPassword(), "Password1", origin);
    securityService.changePassword(user.getUsername(), "Password1", "Password2", origin);
    securityService.changePassword(user.getUsername(), "Password2", "Password1", origin);
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

  private void compareTemplates(FunctionTemplate template1, FunctionTemplate template2)
  {
    assertEquals("The code values for the two function templates do not match",
        template1.getCode(), template2.getCode());
    assertEquals("The description values for the two function templates do not match",
        template1.getDescription(), template2.getDescription());

    List<Function> template1Functions = template1.getFunctions();
    List<Function> template2Functions = template2.getFunctions();

    assertEquals("The two function templates do not have the same number of associated functions",
        template1Functions.size(), template2Functions.size());

    // TODO: Compare the functions on each function template
    assertEquals("The ID values for the two function templates do not match", template1.getId(),
        template2.getId());
    assertEquals("The name values for the two function templates do not match",
        template1.getName(), template2.getName());
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

  private User getNumberedTestUserDetails(int number)
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

  private Function getTestFunctionDetails()
  {
    Function function = new Function("Test Function Code");

    function.setName("Test Function Name");
    function.setDescription("Test Function Description");

    return function;
  }

  private FunctionTemplate getTestFunctionTemplateDetails()
  {
    return new FunctionTemplate("Test Function Template Code",
      "Test Function Template Name", "Test Function Template Description");
  }

  private Group getTestGroupDetails()
  {
    Group group = new Group("Test Group Name");

    group.setDescription("Test Group Description");

    return group;
  }

  private Organisation getTestOrganisationDetails()
  {
    Organisation organisation = new Organisation("Test Code");

    organisation.setName("Test Name");
    organisation.setDescription("Test Description");

    return organisation;
  }

  private User getTestUserDetails()
  {
    User user = new User("Test Username");

    user.setPassword("Test Password");
    user.setEmail("Test E-Mail");
    user.setDescription("Test User Description");
    user.setTitle("Test Title");
    user.setFirstNames("Test FirstName");
    user.setLastName("Test LastName");
    user.setPhoneNumber("Test Phone Number");
    user.setMobileNumber("Test Mobile Number");
    user.setFaxNumber("Test Fax Number");

    return user;
  }
}
