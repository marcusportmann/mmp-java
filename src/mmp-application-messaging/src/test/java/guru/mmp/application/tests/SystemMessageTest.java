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

import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessageTranslator;
import guru.mmp.application.messaging.messages.AnotherTestRequestData;
import guru.mmp.application.messaging.messages.AnotherTestResponseData;
import guru.mmp.application.messaging.messages.AuthenticateRequestData;
import guru.mmp.application.messaging.messages.AuthenticateResponseData;
import guru.mmp.application.test.ApplicationClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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

    requestData.setTestValue(testValue);
    requestData.setTestData(testData);

    assertEquals(testValue, requestData.getTestValue());
    assertArrayEquals(testData, requestData.getTestData());

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    AnotherTestResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new AnotherTestResponseData());

    assertEquals(testValue, responseData.getTestValue());
    assertArrayEquals(testData, responseData.getTestData());

    responseData.setTestValue(testValue);
    responseData.setTestData(testData);

    assertEquals(testValue, responseData.getTestValue());
    assertArrayEquals(testData, responseData.getTestData());
  }

  /**
   * Test the authentication message.
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

    assertEquals(requestMessage.getCorrelationId(), responseMessage.getCorrelationId());
  }
}
