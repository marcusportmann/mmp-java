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

package guru.mmp.application.tests;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.Code;
import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessageTranslator;
import guru.mmp.application.messaging.messages.*;
import guru.mmp.application.test.ApplicationClassRunner;
import guru.mmp.common.util.ISO8601;
import guru.mmp.service.codes.ws.GetCodeCategory;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.*;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SystemMessageTest</code> class contains the implementation of the JUnit
 * tests for the "system" messages supported by the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationClassRunner.class)
public class SystemMessageTest
{
  private static final UUID DEVICE_ID = UUID.randomUUID();
  private static final String USERNAME = "Administrator";
  private static final String PASSWORD = "Password1";
  @Inject
  private IMessagingService messagingService;
  @Inject
  private ICodesService codesService;

  /**
   * Test the "Another Test" request and response message functionality.
   *
   * @throws Exception
   */
  @Test
  public void anotherTestMessageTest()
    throws Exception
  {
    String testValue = "This is the test value";
    byte[] testData = "This is test data".getBytes();

    AnotherTestRequestData requestData = new AnotherTestRequestData(testValue, testData);

    assertEquals(testValue, requestData.getTestValue());
    assertArrayEquals(testData, requestData.getTestData());

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    AnotherTestResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new AnotherTestResponseData());

    assertEquals(testValue, responseData.getTestValue());
    assertArrayEquals(testData, responseData.getTestData());
  }

  /**
   * Test the "Authentication" message.
   *
   * @throws Exception
   */
  @Test
  public void authenticationTest()
    throws Exception
  {
    AuthenticateRequestData requestData = new AuthenticateRequestData(USERNAME, PASSWORD,
        DEVICE_ID);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    AuthenticateResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new AuthenticateResponseData());

    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getErrorMessage());

    List<OrganisationData> organisations = responseData.getOrganisations();

    assertEquals(1, organisations.size());

    OrganisationData organisation = organisations.get(0);

    assertEquals(UUID.fromString("c1685b92-9fe5-453a-995b-89d8c0f29cb5"), organisation.getId());
    assertEquals("MMP", organisation.getName());
    assertNotNull(responseData.getUserEncryptionKey());
    assertNotNull(responseData.getUserProperties());

    Map<String, Object> userProperties = responseData.getUserProperties();

    assertEquals(0, userProperties.size());

    assertEquals(requestMessage.getCorrelationId(), responseMessage.getCorrelationId());
  }

  /**
   * Test the "Check User Exists" message.
   *
   * @throws Exception
   */
  @Test
  public void checkUserExistsTest()
    throws Exception
  {
    CheckUserExistsRequestData requestData = new CheckUserExistsRequestData(USERNAME);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    CheckUserExistsResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new CheckUserExistsResponseData());

    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getErrorMessage());
    assertEquals(true, responseData.getUserExists());
  }

  /**
   * Test the "Test" request and response message functionality.
   *
   * @throws Exception
   */
  @Test
  public void testMessageTest()
    throws Exception
  {
    String testValue = "This is the test value";

    TestRequestData requestData = new TestRequestData(testValue);

    assertEquals(testValue, requestData.getTestValue());

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    TestResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new TestResponseData());

    assertEquals(testValue, responseData.getTestValue());
  }

  /**
   * Test the "Get Code Category" message.
   *
   * @throws Exception
   */
  @Test
  public void getGetCodeCategoryTest()
    throws Exception
  {
    CodeCategory testCodeCategory = new CodeCategory(UUID.randomUUID(), "Test Code Category",
      ISO8601.toDate("2016-05-03T19:14:00+02:00"));

    codesService.createCodeCategory(testCodeCategory);

    List<Code> testCodes = new ArrayList<>();

    for (int i = 1; i <= 10; i++)
    {
      Code testCode = new Code("Test Code ID " + i, testCodeCategory.getId(), "Test Code Name " + i, "Test Code Value " + i);

      codesService.createCode(testCode);

      testCodes.add(testCode);
    }


    GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData(testCodeCategory.getId(), new Date(0), true);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
      new GetCodeCategoryResponseData());

    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getErrorMessage());

    CodeCategoryData codeCategory = responseData.getCodeCategory();

    assertEquals(testCodeCategory.getId(), codeCategory.getId());
    assertEquals(testCodeCategory.getName(), codeCategory.getName());
    assertEquals(CodeCategoryData.CodeDataType.STANDARD, codeCategory.getCodeDataType());
    assertEquals(testCodeCategory.getUpdated(), codeCategory.getLastUpdated());
    assertEquals(testCodes.size(), codeCategory.getCodes().size());

    for (int i = 0; i < testCodes.size(); i++)
    {


    }






    int xxx = 0;
    xxx++;







    /*



    assertEquals(true, responseData.getUserExists());
    */
  }


}
