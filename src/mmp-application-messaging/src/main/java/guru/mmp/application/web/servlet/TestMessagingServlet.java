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

package guru.mmp.application.web.servlet;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.*;
import guru.mmp.application.messaging.message.*;
import guru.mmp.application.registry.IRegistry;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.common.crypto.EncryptionScheme;
import guru.mmp.common.http.SecureHttpClientBuilder;
import guru.mmp.common.util.Base64;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Parser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Random;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestMessagingServlet</code> class implements the servlet used to test the
 * messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class TestMessagingServlet extends HttpServlet
{
  /**
   * The HTTP content-type used when receiving and sending WBXML.
   */
  public static final String WBXML_CONTENT_TYPE = "application/wbxml";
  private static final String TEST_ORGANISATION = "MMP";
  private static final String TEST_PASSWORD = "Password1";
  private static final String TEST_USER = "Administrator";
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestMessagingServlet.class);

  /* Registry */
  @SuppressWarnings("unused")
  @Inject
  private IRegistry registry;

  /* Security Service */
  @SuppressWarnings("unused")
  @Inject
  private ISecurityService securityService;

  /**
   * Initialise the <code>TestMessagingServlet</code>.
   *
   * @param config the servlet configuration
   *
   * @throws ServletException
   */
  @Override
  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String device = generateRandomMac();
    byte[] userEncryptionKey;

    PrintWriter pw = response.getWriter();

    printHtmlHeader(pw);

    pw.println("<h1>Test Messaging Servlet</h1>");

    pw.println("<ul>");

    String messagingEndpoint = getMessagingEndpoint(request);

    try
    {
      /*
       * Send registration request to obtain user encryption key.
       */
      {
        logger.info("Registering as the user (" + TEST_USER + ") with device ID (" + device + ")");

        pw.println("<li>Registering as the user (" + TEST_USER + ") with device ID (" + device
            + ")</li>");

        MessageTranslator messageTranslator = new MessageTranslator(TEST_USER, TEST_ORGANISATION,
          device);

        RegisterRequestData requestData = new RegisterRequestData(TEST_USER, TEST_ORGANISATION,
          TEST_PASSWORD, device, EncryptionScheme.AES_CFB);

        Message requestMessage = messageTranslator.toMessage(requestData);

        MessageResult messageResult = sendMessage(messagingEndpoint, requestMessage);

        if (messageResult.getCode() != 0)
        {
          throw new RuntimeException("Failed to send the RegisterRequest message: ["
              + messageResult.getCode() + "] " + messageResult.getDetail());
        }
        else
        {
          RegisterResponseData responseData =
            messageTranslator.fromMessage(messageResult.getMessage(), new RegisterResponseData());

          if (responseData.getErrorCode() != 0)
          {
            throw new RuntimeException("Failed to send the RegisterRequest message: ["
                + responseData.getErrorCode() + "] " + responseData.getErrorMessage());
          }

          userEncryptionKey = responseData.getUserEncryptionKey();

          logger.info("Successfully registered as the user (" + TEST_USER
              + ") with encryption key (" + Base64.encodeBytes(userEncryptionKey) + ")");

          pw.println("<li>Successfully registered as the user (" + TEST_USER
              + ") with encryption key (" + Base64.encodeBytes(userEncryptionKey) + ")</li>");
        }
      }

      /*
       * Send a test request
       */
      boolean testMessageEnabled = false;
      if (testMessageEnabled)
      {
        logger.info("Sending the TestRequest message");

        pw.println("<li>Sending the <i>TestRequest</i> message</li>");

        MessageTranslator messageTranslator = new MessageTranslator(TEST_USER, TEST_ORGANISATION,
          device, EncryptionScheme.AES_CFB, userEncryptionKey);

        TestRequestData requestData = new TestRequestData("Test Value");

        Message requestMessage = messageTranslator.toMessage(requestData);

        MessageResult messageResult = sendMessage(messagingEndpoint, requestMessage);

        if (messageResult.getCode() != 0)
        {
          throw new RuntimeException("Failed to send the TestRequest message: ["
              + messageResult.getCode() + "] " + messageResult.getDetail());
        }
        else
        {
          Message responseMessage = messageResult.getMessage();

          if (responseMessage.isEncrypted())
          {
            logger.info("Retrieved an encrypted response message to the TestRequest message");

            pw.println("<li>Retrieved an encrypted response message to the"
                + " <i>TestRequest</i> message</li>");
          }

          TestResponseData responseData = messageTranslator.fromMessage(responseMessage,
            new TestResponseData());

          logger.info("Successfully received the TestResponse message value ("
              + responseData.getTestValue() + ")");

          pw.println("<li>Successfully received the <i>TestResponse</i> message with value ("
              + responseData.getTestValue() + ")</li>");

        }
      }

      boolean anotherTestMessageEnabled = true;
      if (anotherTestMessageEnabled)
      {
        /*
         * Send an another test request
         */
        {
          logger.info("Queueing the AnotherTestRequest message for processing");

          pw.println("<li>Queueing the <i>AnotherTestRequest</i> message for processing</li>");

          MessageTranslator messageTranslator = new MessageTranslator(TEST_USER, TEST_ORGANISATION,
            device, EncryptionScheme.AES_CFB, userEncryptionKey);

          AnotherTestRequestData requestData = new AnotherTestRequestData("Test Value");

          Message requestMessage = messageTranslator.toMessage(requestData);

          MessageResult messageResult = sendMessage(messagingEndpoint, requestMessage);

          if (messageResult.getCode() != 0)
          {
            throw new RuntimeException("Failed to send the AnotherTestRequest message: ["
                + messageResult.getCode() + "] " + messageResult.getDetail());
          }
          else
          {
            logger.info("Successfully queued the AnotherTestRequest message for processing");

            pw.println("<li>Successfully queued the <i>AnotherTestRequest</i> message for"
                + " processing</li>");
          }
        }

        /*
         * Sleep to give the back-end a chance to process the message.
         */
        try
        {
          Thread.sleep(2500L);
        }
        catch (Throwable ignored) {}

        /*
         * Retrieve messages queued for download.
         */
        logger.info("Attempting to retrieve the messages queued for download");

        pw.println("<li>Attempting to retrieve the messages queued for download</li>");

        {
          boolean hasFinishedDownloadingMessages = false;

          while (!hasFinishedDownloadingMessages)
          {
            MessageDownloadResponse messageDownloadResponse =
              sendMessageDownloadRequest(messagingEndpoint, device, TEST_USER,
                EncryptionScheme.AES_CFB);

            if (messageDownloadResponse.getCode() != 0)
            {
              throw new RuntimeException("Failed to send the MessageDownloadRequest: ["
                  + messageDownloadResponse.getCode() + "] " + messageDownloadResponse.getDetail());
            }
            else
            {
              List<Message> messages = messageDownloadResponse.getMessages();

              hasFinishedDownloadingMessages = messages.size() == 0;

              logger.info("Downloaded " + messages.size() + " messages");

              pw.println("<li>Downloaded " + messages.size() + " messages</li>");

              for (Message message : messages)
              {
                logger.info("Downloaded message (" + message.getId() + ") with type ("
                    + message.getType() + ")");

                pw.println("<li>Downloaded message (" + message.getId() + ") with type ("
                    + message.getType() + ")</li>");

                sendMessageReceivedRequest(messagingEndpoint, device, message.getId());
              }
            }
          }
        }
      }

      boolean messagePartUploadTestEnabled = false;
      if (messagePartUploadTestEnabled)
      {
        /*
         * Send an another test request
         */
        MessageTranslator messageTranslator;

        {
          logger.info("Queueing the AnotherTestRequest message for processing");

          pw.println("<li>Queueing the <i>AnotherTestRequest</i> message for processing</li>");

          messageTranslator = new MessageTranslator(TEST_USER, TEST_ORGANISATION, device,
              EncryptionScheme.AES_CFB, userEncryptionKey);

          AnotherTestRequestData requestData = new AnotherTestRequestData("Test Value");

          byte[] testData =
            getClasspathResource("guru/mmp/application/web/template/resource/image/test.jpg");

          logger.info("Loaded " + testData.length + " bytes of test data");

          requestData.setTestData(testData);

          Message requestMessage = messageTranslator.toMessage(requestData);

          MessageResult messageResult = sendMessage(messagingEndpoint, requestMessage);

          if (messageResult.getCode() != 0)
          {
            throw new RuntimeException("Failed to send the AnotherTestRequest message: ["
                + messageResult.getCode() + "] " + messageResult.getDetail());
          }
          else
          {
            logger.info("Successfully queued the AnotherTestRequest message for processing");

            pw.println("<li>Successfully queued the <i>AnotherTestRequest</i> message for"
                + " processing</li>");
          }
        }

        /*
         * Sleep to give the back-end a chance to process the message.
         */
        try
        {
          Thread.sleep(5000L);
        }
        catch (Throwable ignored) {}

        /*
         * Retrieve messages queued for download.
         */
        logger.info("Attempting to retrieve the messages queued for download");

        pw.println("<li>Attempting to retrieve the messages queued for download</li>");

        {
          boolean hasFinishedDownloadingMessages = false;

          while (!hasFinishedDownloadingMessages)
          {
            MessageDownloadResponse messageDownloadResponse =
              sendMessageDownloadRequest(messagingEndpoint, device, TEST_USER,
                EncryptionScheme.AES_CFB);

            if (messageDownloadResponse.getCode() != 0)
            {
              throw new RuntimeException("Failed to send the MessageDownloadRequest: ["
                  + messageDownloadResponse.getCode() + "] " + messageDownloadResponse.getDetail());
            }
            else
            {
              List<Message> messages = messageDownloadResponse.getMessages();

              hasFinishedDownloadingMessages = messages.size() == 0;

              logger.info("Downloaded " + messages.size() + " messages");

              pw.println("<li>Downloaded " + messages.size() + " messages</li>");

              for (Message message : messages)
              {
                logger.info("Downloaded message (" + message.getId() + ") with type ("
                    + message.getType() + ")");

                pw.println("<li>Downloaded message (" + message.getId() + ") with type ("
                    + message.getType() + ")</li>");

                if (message.getType().equals(AnotherTestResponseData.MESSAGE_TYPE))
                {
                  AnotherTestResponseData responseData = messageTranslator.fromMessage(message,
                    new AnotherTestResponseData());

                  logger.info("Successfully received the AnotherTestResponse message value ("
                      + responseData.getTestValue() + ")");

                  logger.info("Successfully received " + responseData.getTestData().length
                      + " bytes of data for the AnotherTestResponse message");

                  pw.println("<li>Successfully received the <i>AnotherTestResponse</i> message"
                      + " with value (" + responseData.getTestValue() + ")</li>");

                  pw.println("<li>Successfully received " + responseData.getTestData().length
                      + " bytes of data for the AnotherTestResponse message</li>");
                }

                sendMessageReceivedRequest(messagingEndpoint, device, message.getId());
              }
            }
          }
        }
      }

      boolean messagePartDownloadTestEnabled = false;
      if (messagePartDownloadTestEnabled)
      {
        /*
         * Send a message part download test request
         */
        {
          logger.info("Sending the MessagePartDownloadTestRequest message");

          pw.println("<li>Sending the <i>MessagePartDownloadTestRequest</i> message</li>");

          MessageTranslator messageTranslator = new MessageTranslator(TEST_USER, TEST_ORGANISATION,
            device, EncryptionScheme.AES_CFB, userEncryptionKey);

          MessagePartDownloadTestRequestData requestData =
            new MessagePartDownloadTestRequestData("Test Value");

          Message requestMessage = messageTranslator.toMessage(requestData);

          MessageResult messageResult = sendMessage(messagingEndpoint, requestMessage);

          if (messageResult.getCode() != 0)
          {
            throw new RuntimeException(
                "Failed to send the MessagePartDownloadTestRequest message: ["
                + messageResult.getCode() + "] " + messageResult.getDetail());
          }
          else
          {
            /*
             * Message responseMessage = messageResult.getMessage();
             *
             * if (responseMessage.isEncrypted())
             * {
             * logger.info("Retrieved an encrypted response message to the TestRequest message");
             *
             * pw.println("<li>Retrieved an encrypted response message to the"
             *     + " <i>TestRequest</i> message</li>");
             * }
             *
             * TestResponseData responseData = messageTranslator.fromMessage(responseMessage,
             * new TestResponseData());
             *
             * logger.info("Successfully received the TestResponse message value ("
             *   + responseData.getTestValue() + ")");
             *
             * pw.println("<li>Successfully received the <i>TestResponse</i> message with value ("
             *   + responseData.getTestValue() + ")</li>");
             */
          }
        }

        /*
         * Sleep to give the back-end a chance to process the message.
         */
        try
        {
          Thread.sleep(2500L);
        }
        catch (Throwable ignored) {}

        /*
         * Retrieve message parts queued for download.
         */
        logger.info("Attempting to retrieve the message parts queued for download");

        pw.println("<li>Attempting to retrieve the message parts queued for download</li>");

        {
          boolean hasFinishedDownloadingMessageParts = false;

          while (!hasFinishedDownloadingMessageParts)
          {
            MessagePartDownloadResponse messagePartDownloadResponse =
              sendMessagePartDownloadRequest(messagingEndpoint, device);

            if (messagePartDownloadResponse.getCode() != 0)
            {
              throw new RuntimeException("Failed to send the MessagePartDownloadRequest: ["
                  + messagePartDownloadResponse.getCode() + "] "
                  + messagePartDownloadResponse.getDetail());
            }
            else
            {
              List<MessagePart> messageParts = messagePartDownloadResponse.getMessageParts();

              hasFinishedDownloadingMessageParts = messageParts.size() == 0;

              logger.info("Downloaded " + messageParts.size() + " message parts");

              pw.println("<li>Downloaded " + messageParts.size() + " message parts</li>");

              for (MessagePart messagePart : messageParts)
              {
                logger.info("Downloaded message part " + messagePart.getPartNo() + " of "
                    + messagePart.getTotalParts() + " with ID (" + messagePart.getId()
                    + ") and type (" + messagePart.getMessageType() + ")");

                pw.println("<li>Downloaded message part " + messagePart.getPartNo() + " of "
                    + messagePart.getTotalParts() + " with ID (" + messagePart.getId()
                    + ") and type (" + messagePart.getMessageType() + ")</li>");

                sendMessagePartReceivedRequest(messagingEndpoint, device, messagePart.getId());
              }
            }
          }
        }
      }

      boolean getCodeCategoryTestEnabled = true;
      if (getCodeCategoryTestEnabled)
      {
        logger.info("Sending the GetCodeCategoryRequest message");

        pw.println("<li>Sending the <i>GetCodeCategoryRequest</i> message</li>");

        MessageTranslator messageTranslator = new MessageTranslator(TEST_USER, TEST_ORGANISATION,
          device, EncryptionScheme.AES_CFB, userEncryptionKey);

        GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData();

        requestData.setId("f8f53502-cc27-4691-aec3-2c20a35c3f69");
        requestData.setLastRetrieved(new Date());
        requestData.setReturnCodesIfCurrent(true);

        Message requestMessage = messageTranslator.toMessage(requestData);

        MessageResult messageResult = sendMessage(messagingEndpoint, requestMessage);

        if (messageResult.getCode() != 0)
        {
          throw new RuntimeException("Failed to send the GetCodeCategoryRequest message: ["
              + messageResult.getCode() + "] " + messageResult.getDetail());
        }
        else
        {
          Message responseMessage = messageResult.getMessage();

          GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
            new GetCodeCategoryResponseData());

          logger.info("Successfully received the GetCodeCategoryResponse message with code ("
              + responseData.getErrorCode() + ") and message (" + responseData.getErrorMessage()
              + ")");

          pw.println("<li>Successfully received the GetCodeCategoryResponse message with code ("
              + responseData.getErrorCode() + ") and message (" + responseData.getErrorMessage()
              + ")</li>");

          if (responseData.getErrorCode() == 0)
          {
            logger.info("Successfully retrieved the code category: "
                + responseData.getCodeCategory().toString());

            pw.println("<li>Successfully retrieved the code category: "
                + responseData.getCodeCategory().toString() + "</li>");
          }
        }
      }

      pw.println("</ul>");
    }
    catch (Throwable e)
    {
      logger.error("Failed to test the messaging infrastructure", e);
    }

    printHtmlFooter(pw);
  }

  protected byte[] getClasspathResource(String path)
  {
    InputStream is = null;

    try
    {
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[4096];
      int numberOfBytesRead;

      while ((numberOfBytesRead = is.read(buffer)) != -1)
      {
        baos.write(buffer, 0, numberOfBytesRead);
      }

      return baos.toByteArray();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to read the classpath resource (" + path + ")", e);
    }
    finally
    {
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to close the input stream for the classpath resource (" + path + ")",
            e);
      }
    }
  }

  /**
   * Returns a random MAC in the following format xx-xx-xx-xx-xx where each 'xx' is an integer
   * between 10 and 99.
   *
   * @return a random MAC
   */
  private static String generateRandomMac()
  {
    StringBuilder sb = new StringBuilder();
    Random g = new Random(System.currentTimeMillis());

    for (int i = 0; i < 6; i++)
    {
      if (i > 0)
      {
        sb.append("-");
      }

      sb.append(g.nextInt(99 - 10 + 1) + 10);
    }

    return sb.toString();
  }

  private String getMessagingEndpoint(HttpServletRequest request)
  {
    return "http://" + request.getServerName() + ":" + request.getServerPort() + "/"
        + request.getContextPath() + "/servlet/MessagingServlet";
  }

  private byte[] invokeMessageServlet(String messagingEndpoint, byte[] data)
    throws Exception
  {
    HttpClient httpClient;

    if (messagingEndpoint.startsWith("https"))
    {
      // Configure the connection
      SecureHttpClientBuilder secureHttpClientBuilder = new SecureHttpClientBuilder();

      RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(
          30000).setConnectTimeout(30000).setSocketTimeout(30000).build();

      secureHttpClientBuilder.setDefaultRequestConfig(requestConfig);

      httpClient = secureHttpClientBuilder.build();
    }
    else
    {
      RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(
          30000).setConnectTimeout(30000).setSocketTimeout(30000).build();

      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

      httpClientBuilder.setDefaultRequestConfig(requestConfig);

      httpClient = httpClientBuilder.build();
    }

    HttpPost httpPost = new HttpPost(messagingEndpoint);

    httpPost.setHeader("Content-type", WBXML_CONTENT_TYPE);

    httpPost.setEntity(new ByteArrayEntity(data));

    HttpResponse response = httpClient.execute(httpPost);

    HttpEntity entity = response.getEntity();

    if (entity != null)
    {
      InputStream in = entity.getContent();

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int numberOfBytesRead;

      byte[] readBuffer = new byte[2048];

      while ((numberOfBytesRead = in.read(readBuffer)) != -1)
      {
        baos.write(readBuffer, 0, numberOfBytesRead);
      }

      return baos.toByteArray();
    }
    else
    {
      throw new RuntimeException("No HTTP response entity found");
    }
  }

  private void printHtmlFooter(PrintWriter pw)
    throws IOException
  {
    pw.println("  </body>");
    pw.println("</html>");
  }

  private void printHtmlHeader(PrintWriter pw)
    throws IOException
  {
    pw.println("<html>");
    pw.println("  <head>");
    pw.println("    <style>");
    pw.println(
        "      body {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 8pt;}");
    pw.println("      .section {padding-top: 10px; padding-bottom: 2px; color: green;"
        + " thirdparty-weight: bold; thirdparty-size: 9pt;}");
    pw.println("      .className {color: 808080;}");
    pw.println("    </style>");
    pw.println("  </head>");
    pw.println("  <body>");
  }

  private MessageResult sendMessage(String messagingEndpoint, Message message)
    throws Exception
  {
    try
    {
      if (message.getData().length > Message.MAX_ASYNC_MESSAGE_SIZE)
      {
        // Calculate the hash for the message data to use as the message checksum
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        messageDigest.update(message.getData());

        String messageChecksum = Base64.encodeBytes(messageDigest.digest());

        // Split the message up into a number of message parts and persist each message part
        int numberOfParts = message.getData().length / MessagePart.MAX_MESSAGE_PART_SIZE;

        if ((message.getData().length % MessagePart.MAX_MESSAGE_PART_SIZE) > 0)
        {
          numberOfParts++;
        }

        for (int i = 0; i < numberOfParts; i++)
        {
          byte[] messagePartData;

          // If this is not the last message part
          if (i < (numberOfParts - 1))
          {
            messagePartData = new byte[MessagePart.MAX_MESSAGE_PART_SIZE];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, MessagePart.MAX_MESSAGE_PART_SIZE);
          }

          // If this is the last message part
          else
          {
            int sizeOfPart = message.getData().length - (i * MessagePart.MAX_MESSAGE_PART_SIZE);

            messagePartData = new byte[sizeOfPart];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, sizeOfPart);
          }

          MessagePart messagePart = new MessagePart(i + 1, numberOfParts, message.getId(),
            message.getUser(), message.getOrganisation(), message.getDevice(), message.getType(),
            message.getTypeVersion(), message.getCorrelationId(), message.getPriority(),
            message.getCreated(), message.getDataHash(), message.getEncryptionScheme(),
            message.getEncryptionIV(), messageChecksum, messagePartData);

          messagePart.setStatus(MessagePart.Status.QUEUED_FOR_SENDING);

          // Send the message part
          byte[] data = invokeMessageServlet(messagingEndpoint, messagePart.toWBXML());

          Parser parser = new Parser();

          Document document = parser.parse(data);

          // Check if we have received a valid message result
          if (MessagePartResult.isValidWBXML(document))
          {
            logger.info("Uploaded the message part (" + messagePart.getPartNo() + "/"
                + messagePart.getTotalParts() + ") for the message (" + messagePart.getMessageId()
                + ") from the user (" + messagePart.getMessageUser() + ") and the device ("
                + messagePart.getMessageDevice() + ") with message type ("
                + messagePart.getMessageType() + ") and version ("
                + messagePart.getMessageTypeVersion() + ")");
          }
          else
          {
            throw new RuntimeException("The WBXML response data from the remote server is not a"
                + " valid MessageResult document");
          }
        }

        return new MessageResult(0, "");
      }
      else
      {
        byte[] data = invokeMessageServlet(messagingEndpoint, message.toWBXML());

        Parser parser = new Parser();

        Document document = parser.parse(data);

        // Check if we have received a valid message result
        if (MessageResult.isValidWBXML(document))
        {
          return new MessageResult(document);
        }
        else
        {
          throw new RuntimeException("The WBXML response data from the remote server is not a"
              + " valid MessageResult document");
        }
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to send the message (" + message.getId() + "): "
          + e.getMessage(), e);
    }
  }

  private MessageDownloadResponse sendMessageDownloadRequest(String messagingEndpoint,
      String device, String user, EncryptionScheme encryptionScheme)
    throws Exception
  {
    MessageDownloadRequest messageDownloadRequest = new MessageDownloadRequest(device, user,
      encryptionScheme);

    byte[] data = invokeMessageServlet(messagingEndpoint, messageDownloadRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessageDownloadResponse.isValidWBXML(document))
    {
      return new MessageDownloadResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message download request:"
          + "The WBXML response data from the remote server is not a valid"
          + " MessageDownloadResponse document");
    }
  }

  private MessagePartDownloadResponse sendMessagePartDownloadRequest(String messagingEndpoint,
      String device)
    throws Exception
  {
    MessagePartDownloadRequest messagePartDownloadRequest = new MessagePartDownloadRequest(device);

    byte[] data = invokeMessageServlet(messagingEndpoint, messagePartDownloadRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessagePartDownloadResponse.isValidWBXML(document))
    {
      return new MessagePartDownloadResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message part download request:"
          + "The WBXML response data from the remote server is not a valid"
          + " MessagePartDownloadResponse document");
    }
  }

  private MessagePartReceivedResponse sendMessagePartReceivedRequest(String messagingEndpoint,
      String device, String messageId)
    throws Exception
  {
    MessagePartReceivedRequest messagePartReceivedRequest = new MessagePartReceivedRequest(device,
      messageId);

    byte[] data = invokeMessageServlet(messagingEndpoint, messagePartReceivedRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessagePartReceivedResponse.isValidWBXML(document))
    {
      return new MessagePartReceivedResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message part received request:"
          + "The WBXML response data from the remote server is not a valid"
          + " MessagePartReceivedResponse document");
    }
  }

  private MessageReceivedResponse sendMessageReceivedRequest(String messagingEndpoint,
      String device, String messageId)
    throws Exception
  {
    MessageReceivedRequest messageReceivedRequest = new MessageReceivedRequest(device, messageId);

    byte[] data = invokeMessageServlet(messagingEndpoint, messageReceivedRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessageReceivedResponse.isValidWBXML(document))
    {
      return new MessageReceivedResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message received request:"
          + "The WBXML response data from the remote server is not a valid"
          + " MessageReceivedResponse document");
    }
  }
}
