/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application.messaging.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.Code;
import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessageTranslator;
import guru.mmp.application.messaging.messages.*;
import guru.mmp.application.test.ApplicationClassRunner;
import guru.mmp.application.test.ApplicationConfiguration;
import guru.mmp.common.util.ISO8601;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.*;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SystemMessageTest</code> class contains the implementation of the JUnit
 * tests for the "system" messages supported by the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@RunWith(ApplicationClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class SystemMessageTest
{
  private static final UUID DEVICE_ID = UUID.randomUUID();
  private static final String USERNAME = "Administrator";
  private static final String PASSWORD = "Password1";

  /* Messaging Service */
  @Autowired
  private IMessagingService messagingService;

  /* Codes Service */
  @Autowired
  private ICodesService codesService;

  /**
   * Test the "Another Test" request and response message functionality.
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
   * Test the "Get Code Category" message.
   */
  @Test
  public void getGetCodeCategoryTest()
    throws Exception
  {
    CodeCategory testStandardCodeCategory = new CodeCategory(UUID.randomUUID(),
        "Test Standard Code Category", ISO8601.toDate("2016-05-03T19:14:00+02:00"));

    if (testStandardCodeCategory != null)
    {
      codesService.createCodeCategory(testStandardCodeCategory);

      List<Code> testCodes = new ArrayList<>();

      for (int i = 1; i <= 10; i++)
      {
        Code testStandardCode = new Code("Test Standard Code ID " + i,
            testStandardCodeCategory.getId(), "Test Standard Code Name " + i,
            "Test Standard Code Value " + i);

        codesService.createCode(testStandardCode);

        testCodes.add(testStandardCode);
      }

      GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData(
          testStandardCodeCategory.getId(), new Date(0), true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
          new GetCodeCategoryResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testStandardCodeCategory.getId(), codeCategory.getId());
      assertEquals(testStandardCodeCategory.getName(), codeCategory.getName());
      assertEquals(CodeCategoryData.CodeDataType.STANDARD, codeCategory.getCodeDataType());
      assertEquals(testStandardCodeCategory.getUpdated(), codeCategory.getLastUpdated());
      assertEquals(testCodes.size(), codeCategory.getCodes().size());

      boolean foundMatchingCode = false;

      for (Code testCode : testCodes)
      {
        for (CodeData code : codeCategory.getCodes())
        {
          if (testCode.getId().equals(code.getId()))
          {
            assertEquals(testCode.getCategoryId(), code.getCategoryId());
            assertEquals(testCode.getName(), code.getName());
            assertEquals(testCode.getValue(), code.getValue());

            foundMatchingCode = true;

            break;
          }
        }

        if (!foundMatchingCode)
        {
          fail(String.format("Failed to find the matching code (%s)", testCode));
        }

        foundMatchingCode = false;
      }
    }

    CodeCategory testCustomCodeCategory = new CodeCategory(UUID.randomUUID(),
        "Test Custom Code Category", "Test Custom Code Data", ISO8601.toDate(
        "2016-05-03T19:14:00+02:00"));

    if (testCustomCodeCategory != null)
    {
      codesService.createCodeCategory(testCustomCodeCategory);

      GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData(
          testCustomCodeCategory.getId(), new Date(0), true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
          new GetCodeCategoryResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testCustomCodeCategory.getId(), codeCategory.getId());
      assertEquals(testCustomCodeCategory.getName(), codeCategory.getName());
      assertEquals(CodeCategoryData.CodeDataType.CUSTOM, codeCategory.getCodeDataType());
      assertEquals(testCustomCodeCategory.getUpdated(), codeCategory.getLastUpdated());
      assertEquals(testCustomCodeCategory.getCodeData(), new String(codeCategory.getCodeData()));
    }
  }

  /**
   * Test the "Get Code Category With Parameters" message.
   */
  @Test
  public void getGetCodeCategoryWithParametersTest()
    throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    parameters.put("Parameter Name 1", "Parameter Value 1");
    parameters.put("Parameter Name 2", "Parameter Value 2");

    CodeCategory testStandardCodeCategory = new CodeCategory(UUID.randomUUID(),
        "Test Standard Code Category", ISO8601.toDate("2016-05-03T19:14:00+02:00"));

    if (testStandardCodeCategory != null)
    {
      codesService.createCodeCategory(testStandardCodeCategory);

      List<Code> testCodes = new ArrayList<>();

      for (int i = 1; i <= 10; i++)
      {
        Code testStandardCode = new Code("Test Standard Code ID " + i,
            testStandardCodeCategory.getId(), "Test Standard Code Name " + i,
            "Test Standard Code Value " + i);

        codesService.createCode(testStandardCode);

        testCodes.add(testStandardCode);
      }

      GetCodeCategoryWithParametersRequestData requestData =
          new GetCodeCategoryWithParametersRequestData(testStandardCodeCategory.getId(), new Date(
          0), parameters, true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryWithParametersResponseData responseData = messageTranslator.fromMessage(
          responseMessage, new GetCodeCategoryWithParametersResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testStandardCodeCategory.getId(), codeCategory.getId());
      assertEquals(testStandardCodeCategory.getName(), codeCategory.getName());
      assertEquals(CodeCategoryData.CodeDataType.STANDARD, codeCategory.getCodeDataType());
      assertEquals(testStandardCodeCategory.getUpdated(), codeCategory.getLastUpdated());
      assertEquals(testCodes.size(), codeCategory.getCodes().size());

      boolean foundMatchingCode = false;

      for (Code testCode : testCodes)
      {
        for (CodeData code : codeCategory.getCodes())
        {
          if (testCode.getId().equals(code.getId()))
          {
            assertEquals(testCode.getCategoryId(), code.getCategoryId());
            assertEquals(testCode.getName(), code.getName());
            assertEquals(testCode.getValue(), code.getValue());

            foundMatchingCode = true;

            break;
          }
        }

        if (!foundMatchingCode)
        {
          fail(String.format("Failed to find the matching code (%s)", testCode));
        }

        foundMatchingCode = false;
      }
    }

    CodeCategory testCustomCodeCategory = new CodeCategory(UUID.randomUUID(),
        "Test Custom Code Category", "Test Custom Code Data", ISO8601.toDate(
        "2016-05-03T19:14:00+02:00"));

    if (testCustomCodeCategory != null)
    {
      codesService.createCodeCategory(testCustomCodeCategory);

      GetCodeCategoryWithParametersRequestData requestData =
          new GetCodeCategoryWithParametersRequestData(testCustomCodeCategory.getId(), new Date(0),
          parameters, true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryWithParametersResponseData responseData = messageTranslator.fromMessage(
          responseMessage, new GetCodeCategoryWithParametersResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testCustomCodeCategory.getId(), codeCategory.getId());
      assertEquals(testCustomCodeCategory.getName(), codeCategory.getName());
      assertEquals(CodeCategoryData.CodeDataType.CUSTOM, codeCategory.getCodeDataType());
      assertEquals(testCustomCodeCategory.getUpdated(), codeCategory.getLastUpdated());
      assertEquals(testCustomCodeCategory.getCodeData(), new String(codeCategory.getCodeData()));
    }
  }

  /**
   * Test the "Submit Error Report" message.
   */
  @Test
  public void submitErrorReportTest()
    throws Exception
  {
    SubmitErrorReportRequestData requestData = new SubmitErrorReportRequestData(UUID.randomUUID(),
        UUID.randomUUID(), 1, "Test Description", "Test Detail", "Test Feedback", new Date(),
        "Administrator", UUID.randomUUID(), "Test Data".getBytes());

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    SubmitErrorReportResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new SubmitErrorReportResponseData());

    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getErrorMessage());
    assertEquals(requestData.getId(), responseData.getErrorReportId());
  }

  /**
   * Test the "Test" request and response message functionality.
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
}
