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
import guru.mmp.application.messaging.MessagingException;
import guru.mmp.application.messaging.messages.AuthenticateRequestData;
import guru.mmp.application.messaging.messages.AuthenticateResponseData;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagingServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>MessagingService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class MessagingServiceTest
{
  private static final UUID DEVICE_ID = UUID.randomUUID();
  private static final String USERNAME = "Administrator";
  private static final String PASSWORD = "Password1";
  @Inject
  private IMessagingService messagingService;

  /**
   * Test the authentication message.
   *
   * @throws Exception
   */
  @Test
  public void authenticationTest()
    throws Exception
  {
    Message authenticateRequestMessage = getAuthenticateRequestMessage();

    Message authenticateResponseMessage = messagingService.processMessage(
        authenticateRequestMessage);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    AuthenticateResponseData authenticateResponseData = messageTranslator.fromMessage(
        authenticateResponseMessage, new AuthenticateResponseData());

    assertEquals(
        "Failed to confirm that the Authenticate Request message was processed successfully", 0,
        authenticateResponseData.getErrorCode());

    assertEquals("Failed to confirm that the correlation IDs are the same for the "
        + "Authenticate Request and Authenticate Response messages",
        authenticateRequestMessage.getCorrelationId(),
        authenticateResponseMessage.getCorrelationId());
  }

  private static synchronized Message getAuthenticateRequestMessage()
    throws MessagingException
  {
    AuthenticateRequestData authenticateRequestData = new AuthenticateRequestData(USERNAME,
        PASSWORD, DEVICE_ID);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    return messageTranslator.toMessage(authenticateRequestData, UUID.randomUUID());
  }
}

//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('d21fb54e-5c5b-49e8-881f-ce00c6ced1a3', '');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('82223035-1726-407f-8703-3977708e792c', 'AuthenticateResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('cc005e6a-b01b-48eb-98a0-026297be69f3', 'CheckUserExistsRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a38bd55e-3470-46f1-a96a-a6b08a9adc63', 'CheckUserExistsResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('94d60eb6-a062-492d-b5e7-9fb1f05cf088', 'GetCodeCategoryRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('0336b544-91e5-4eb9-81db-3dd94e116c92', 'GetCodeCategoryResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('3500a28a-6a2c-482b-b81f-a849c9c3ef79', 'GetCodeCategoryWithParametersRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('12757310-9eee-4a3a-970c-9b4ee0e1108e', 'GetCodeCategoryWithParametersResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse');



///*
// * Copyright 2016 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package guru.mmp.application.test;
//
//
//
//import guru.mmp.application.messaging.*;
//import guru.mmp.common.crypto.EncryptionScheme;
//import guru.mmp.common.http.SecureHttpClientBuilder;
//import guru.mmp.common.wbxml.Document;
//import guru.mmp.common.wbxml.Parser;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ByteArrayEntity;
//import org.apache.http.impl.client.HttpClientBuilder;
//
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.security.MessageDigest;
//import java.util.Random;
//
//
//
///**
// * The <code>MessagingTests</code> class provides the base class for all classes that
// * implement JUnit tests that make use of the mobile messaging infrastructure.
// *
// * @author Marcus Portmann
// */
//@SuppressWarnings("unused")
//public abstract class MessagingTests extends InjectableTests
//{
//  /**
//   * The maximum size in bytes of an asynchronous message that can be sent directly. Messages
//   * larger than this size will be broken up into message parts which will be uploaded separately.
//   * Once all the parts for the asynchronous message have been uploaded the original message will
//   * be reassembled on the server and processed.
//   */
//  public static final int MESSAGING_MAX_ASYNC_MESSAGE_SIZE = 15360;
//
//  /**
//   * The maximum size of the data for a message part.
//   */
//  public static final int MESSAGING_MAX_MESSAGE_PART_SIZE = 15360;
//
//  /**
//   * The HTTP content-type used when receiving and sending WBXML.
//   */
//  public static final String WBXML_CONTENT_TYPE = "application/wbxml";
//  private String messagingEndpoint;
//
//  /**
//   * Constructs a new <code>MessagingTests</code>.
//   *
//   * @param messagingEndpoint the messaging endpoint e.g.
//   *                          http://localhost:8080/sample/servlet/MessagingServlet
//   */
//  public MessagingTests(String messagingEndpoint)
//  {
//    this.messagingEndpoint = messagingEndpoint;
//  }
//
//  /**
//   * Returns a random MAC in the following format xx-xx-xx-xx-xx where each 'xx' is an integer
//   * between 10 and 99.
//   *
//   * @return a random MAC
//   */
//  protected static String generateRandomMac()
//  {
//    StringBuilder sb = new StringBuilder();
//    Random g = new Random(System.currentTimeMillis());
//
//    for (int i = 0; i < 6; i++)
//    {
//      if (i > 0)
//      {
//        sb.append("-");
//      }
//
//      sb.append(g.nextInt(99 - 10 + 1) + 10);
//    }
//
//    return sb.toString();
//  }
//
//  protected byte[] invokeMessageServlet(byte[] data)
//    throws Exception
//  {
//    HttpClient httpClient;
//
//    if (messagingEndpoint.startsWith("https"))
//    {
//      // Configure the connection
//      SecureHttpClientBuilder secureHttpClientBuilder = new SecureHttpClientBuilder();
//
//      RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(
//          30000).setConnectTimeout(30000).setSocketTimeout(30000).build();
//
//      secureHttpClientBuilder.setDefaultRequestConfig(requestConfig);
//
//      httpClient = secureHttpClientBuilder.build();
//    }
//    else
//    {
//      RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(
//          30000).setConnectTimeout(30000).setSocketTimeout(30000).build();
//
//      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//
//      httpClientBuilder.setDefaultRequestConfig(requestConfig);
//
//      httpClient = httpClientBuilder.build();
//    }
//
//    HttpPost httpPost = new HttpPost(messagingEndpoint);
//
//    httpPost.setHeader("Content-type", WBXML_CONTENT_TYPE);
//
//    httpPost.setEntity(new ByteArrayEntity(data));
//
//    HttpResponse response = httpClient.execute(httpPost);
//
//    HttpEntity entity = response.getEntity();
//
//    if (entity != null)
//    {
//      InputStream in = entity.getContent();
//
//      ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//      int numberOfBytesRead;
//
//      byte[] readBuffer = new byte[2048];
//
//      while ((numberOfBytesRead = in.read(readBuffer)) != -1)
//      {
//        baos.write(readBuffer, 0, numberOfBytesRead);
//      }
//
//      return baos.toByteArray();
//    }
//    else
//    {
//      throw new RuntimeException("No HTTP response entity found");
//    }
//  }
//
//  protected MessageResult sendMessage(Message message)
//    throws Exception
//  {
//    byte[] data = invokeMessageServlet(message.toWBXML());
//
//    Parser parser = new Parser();
//
//    Document document = parser.parse(data);
//
//    // Check if we have received a valid message result
//    if (MessageResult.isValidWBXML(document))
//    {
//      return new MessageResult(document);
//    }
//    else
//    {
//      throw new RuntimeException("Failed to send the message (" + message.getId()
//          + "): The WBXML response data from the remote server is not a valid"
//          + " MessageResult document");
//    }
//  }
//
//  protected void sendMessageAsynchronously(Message message)
//    throws Exception
//  {
//    if (message.getData().length <= MESSAGING_MAX_ASYNC_MESSAGE_SIZE)
//    {
//      byte[] data = invokeMessageServlet(message.toWBXML());
//
//      Parser parser = new Parser();
//
//      Document document = parser.parse(data);
//
//      // Check if we have received a valid message result
//      if (MessageResult.isValidWBXML(document))
//      {
//        // MessageResult messageResult = new MessageResult(document);
//      }
//      else
//      {
//        throw new RuntimeException("Failed to send the message (" + message.getId()
//            + "): The WBXML response data from the remote server is not a valid"
//            + " MessageResult document");
//      }
//    }
//    else
//    {
//      // Calculate the hash for the message data to use as the message checksum
//      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
//
//      messageDigest.update(message.getData());
//
////    String messageChecksum = Base64.encodeBytes(messageDigest.digest(), false);
//
//      // Split the message up into a number of message parts and persist each message part
//      int numberOfParts = message.getData().length / MESSAGING_MAX_MESSAGE_PART_SIZE;
//
//      if ((message.getData().length % MESSAGING_MAX_MESSAGE_PART_SIZE) > 0)
//      {
//        numberOfParts++;
//      }
//
//      for (int i = 0; i < numberOfParts; i++)
//      {
//        byte[] messagePartData;
//
//        // If this is not the last message part
//        if (i < (numberOfParts - 1))
//        {
//          messagePartData = new byte[MESSAGING_MAX_MESSAGE_PART_SIZE];
//
//          System.arraycopy(message.getData(), (i * MESSAGING_MAX_MESSAGE_PART_SIZE),
//              messagePartData, 0, MESSAGING_MAX_MESSAGE_PART_SIZE);
//        }
//
//        // If this is the last message part
//        else
//        {
//          int sizeOfPart = message.getData().length - (i * MESSAGING_MAX_MESSAGE_PART_SIZE);
//
//          messagePartData = new byte[sizeOfPart];
//
//          System.arraycopy(message.getData(), (i * MESSAGING_MAX_MESSAGE_PART_SIZE),
//              messagePartData, 0, sizeOfPart);
//        }
//      }
//    }
//  }
//
//  protected MessageDownloadResponse sendMessageDownloadRequest(String device, String user,
//      EncryptionScheme messageEncryptionScheme)
//    throws Exception
//  {
//    MessageDownloadRequest messageDownloadRequest = new MessageDownloadRequest(device, user,
//      messageEncryptionScheme);
//
//    byte[] data = invokeMessageServlet(messageDownloadRequest.toWBXML());
//
//    Parser parser = new Parser();
//
//    Document document = parser.parse(data);
//
//    if (MessageDownloadResponse.isValidWBXML(document))
//    {
//      return new MessageDownloadResponse(document);
//    }
//    else
//    {
//      throw new RuntimeException("Failed to send the message download request:"
//          + "The WBXML response data from the remote server is not a valid"
//          + " MessageDownloadResponse document");
//    }
//  }
//
//  protected MessagePartDownloadResponse sendMessagePartDownloadRequest(String device)
//    throws Exception
//  {
//    MessagePartDownloadRequest messagePartDownloadRequest = new MessagePartDownloadRequest
// (device);
//
//    byte[] data = invokeMessageServlet(messagePartDownloadRequest.toWBXML());
//
//    Parser parser = new Parser();
//
//    Document document = parser.parse(data);
//
//    if (MessagePartDownloadResponse.isValidWBXML(document))
//    {
//      return new MessagePartDownloadResponse(document);
//    }
//    else
//    {
//      throw new RuntimeException("Failed to send the message part download request:"
//          + "The WBXML response data from the remote server is not a valid"
//          + " MessagePartDownloadResponse document");
//    }
//  }
//
//  protected MessagePartReceivedResponse sendMessagePartReceivedRequest(String device,
//      String messageId)
//    throws Exception
//  {
//    MessagePartReceivedRequest messagePartReceivedRequest = new MessagePartReceivedRequest(device,
//      messageId);
//
//    byte[] data = invokeMessageServlet(messagePartReceivedRequest.toWBXML());
//
//    Parser parser = new Parser();
//
//    Document document = parser.parse(data);
//
//    if (MessagePartReceivedResponse.isValidWBXML(document))
//    {
//      return new MessagePartReceivedResponse(document);
//    }
//    else
//    {
//      throw new RuntimeException("Failed to send the message part received request:"
//          + "The WBXML response data from the remote server is not a valid"
//          + " MessagePartReceivedResponse document");
//    }
//  }
//
//  protected MessageReceivedResponse sendMessageReceivedRequest(String device, String messageId)
//    throws Exception
//  {
//    MessageReceivedRequest messageReceivedRequest = new MessageReceivedRequest(device, messageId);
//
//    byte[] data = invokeMessageServlet(messageReceivedRequest.toWBXML());
//
//    Parser parser = new Parser();
//
//    Document document = parser.parse(data);
//
//    if (MessageReceivedResponse.isValidWBXML(document))
//    {
//      return new MessageReceivedResponse(document);
//    }
//    else
//    {
//      throw new RuntimeException("Failed to send the message received request:"
//          + "The WBXML response data from the remote server is not a valid"
//          + " MessageReceivedResponse document");
//    }
//  }
//}
