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

package guru.mmp.application.messaging.handler;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.messaging.ErrorReport;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessageTranslator;
import guru.mmp.application.messaging.message.*;
import guru.mmp.application.registry.IRegistry;
import guru.mmp.application.security.AuthenticationFailedException;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.UserNotFoundException;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.ExceptionUtil;
import guru.mmp.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>SystemMessageHandler</code> class implements the message handler that processes the
 * "system" messages for the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class SystemMessageHandler extends MessageHandler
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SystemMessageHandler.class);

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /* Messaging Service */
  @Inject
  private IMessagingService messagingService;

  /* Registry */
  @Inject
  private IRegistry registry;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>SystemMessageHandler</code>.
   *
   * @param messageHandlerConfig the configuration information for this message handler
   */
  @SuppressWarnings("unused")
  public SystemMessageHandler(MessageHandlerConfig messageHandlerConfig)
  {
    super("System Message Handler", messageHandlerConfig);
  }

  /**
   * Constructs a new <code>SystemMessageHandler</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected SystemMessageHandler() {}

  /**
   * Process the specified message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   *
   * @throws MessageHandlerException
   */
  public Message processMessage(Message message)
    throws MessageHandlerException
  {
    if (message.getTypeId().equals(RegisterRequestData.MESSAGE_TYPE_ID))
    {
      return processRegisterMessage(message);
    }
    // Process a "Authenticate Request" message
    else if (message.getTypeId().equals(AuthenticateRequestData.MESSAGE_TYPE_ID))
    {
      return processAuthenticateMessage(message);
    }
    // Process a "Check User Exists Request" message
    else if (message.getTypeId().equals(CheckUserExistsRequestData.MESSAGE_TYPE_ID))
    {
      return processCheckUserExistsMessage(message);
    }
    // Process a "Test Request" message
    else if (message.getTypeId().equals(TestRequestData.MESSAGE_TYPE_ID))
    {
      return processTestMessage(message);
    }
    // Process a "Another Test Request" message
    else if (message.getTypeId().equals(AnotherTestRequestData.MESSAGE_TYPE_ID))
    {
      return processAnotherTestMessage(message);
    }
    // Process a "Message Part Download Test Request" message
    else if (message.getTypeId().equals(MessagePartDownloadTestRequestData.MESSAGE_TYPE_ID))
    {
      return processMessagePartDownloadTestMessage(message);
    }
    // Process a "Submit Error Report Request" message
    else if (message.getTypeId().equals(SubmitErrorReportRequestData.MESSAGE_TYPE_ID))
    {
      return processSubmitErrorReportRequestMessage(message);
    }
    // Process a "Get Code Category Request" message
    else if (message.getTypeId().equals(GetCodeCategoryRequestData.MESSAGE_TYPE_ID))
    {
      return processGetCodeCategoryRequestMessage(message);
    }
    // Process a "Get Code Category With Parameters Request" message
    else if (message.getTypeId().equals(GetCodeCategoryWithParametersRequestData.MESSAGE_TYPE_ID))
    {
      return processGetCodeCategoryWithParametersRequestMessage(message);
    }

    throw new MessageHandlerException("Failed to process the unrecognised message ("
        + message.getId() + ") with type (" + message.getTypeId() + ") from user (" + message.getUser() + ") and device ("
        + message.getDeviceId() + ")");
  }

//private GetCodeCategoryResponseData getRemoteWebServiceCodeCategory(CodeCategory codeCategory,
//    Date lastRetrieved, boolean returnCodesIfCurrent)
//  throws MessageHandlerException
//{
//  try
//  {
//    URL wsdlLocation = Thread.currentThread().getContextClassLoader().getResource(
//        "META-INF/wsdl/CodesService.wsdl");
//
//    CodesService service = new CodesService(wsdlLocation,
//      new QName("http://ws.codes.services.mmp.guru", "CodesService"));
//
//    // Setup the JAX-WS handlers that implement the Web Service Security model
//    if (codeCategory.getIsEndPointSecure())
//    {
//      service.setHandlerResolver(new WebServiceClientSecurityHandlerResolver());
//    }
//
//    // Retrieve the web service proxy
//    guru.mmp.services.codes.ws.ICodesService codesService = service.getCodesService();
//
//    // Set the endpoint for the web service
//    BindingProvider bindingProvider = ((BindingProvider) codesService);
//
//    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
//        codeCategory.getEndPoint());
//
//    guru.mmp.services.codes.ws.CodeCategory remoteCodeCategory =
//      codesService.getCodeCategory(codeCategory.getId(),
//        XmlConversionUtil.asXMLGregorianCalendar(lastRetrieved), returnCodesIfCurrent);
//
//    List<CodeData> codes = new ArrayList<CodeData>();
//
//    for (guru.mmp.services.codes.ws.Code code : remoteCodeCategory.getCodes())
//    {
//      codes.add(new CodeData(codeCategory.getId(), code));
//    }
//
//    CodeCategoryData codeCategoryData = new CodeCategoryData(codeCategory, codes,
//      remoteCodeCategory.getCodeData());
//
//    codeCategoryData.setLastUpdated(
//        XmlConversionUtil.asDate(remoteCodeCategory.getLastUpdated()));
//
//    return new GetCodeCategoryResponseData(codeCategoryData);
//  }
//  catch (Throwable e)
//  {
//    logger.error("Failed to retrieve the remote web service code category with ID ("
//        + codeCategory.getId() + ") and name (" + codeCategory.getName() + ")", e);
//
//    return new GetCodeCategoryResponseData(GetCodeCategoryResponseData.ERROR_CODE_UNKNOWN_ERROR,
//        "Failed to retrieve the remote web service code category with ID ("
//        + codeCategory.getId() + ") and name (" + codeCategory.getName() + "): "
//        + e.buildMessageFromResultSet());
//  }
//}

  private Message processAnotherTestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      logger.info(requestMessage.toString());

      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      AnotherTestRequestData requestData = messageTranslator.fromMessage(requestMessage,
        new AnotherTestRequestData());

      AnotherTestResponseData responseData =
        new AnotherTestResponseData(requestData.getTestValue());

      Message responseMessage = messageTranslator.toMessage(responseData,
        requestMessage.getCorrelationId());

      logger.info(responseMessage.toString());

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processAuthenticateMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      AuthenticateRequestData requestData = messageTranslator.fromMessage(requestMessage,
        new AuthenticateRequestData());

      // Authenticate the user
      AuthenticateResponseData responseData;

      try
      {
        securityService.authenticate(requestData.getUser(), requestData.getPassword());

        byte[] userEncryptionKey = messagingService.deriveUserDeviceEncryptionKey(
            requestData.getPreferredEncryptionScheme(), requestData.getUser(),
            requestData.getOrganisation(), requestData.getDevice());

        if (logger.isDebugEnabled())
        {
          logger.debug("Generated the encryption key ("
              + Base64.encodeBytes(userEncryptionKey, false) + ") using the encryption scheme ("
              + requestData.getPreferredEncryptionScheme() + ") for the user ("
              + requestData.getUser() + ") from the organisation (" + requestData.getOrganisation()
              + ") using the device (" + requestData.getDevice() + ")");
        }
        else
        {
          logger.info("Generated the encryption key using the encryption scheme ("
              + requestData.getPreferredEncryptionScheme() + ") for the user ("
              + requestData.getUser() + ") from the organisation (" + requestData.getOrganisation()
              + ") using the device (" + requestData.getDevice() + ")");
        }

        responseData = new AuthenticateResponseData(requestData.getPreferredEncryptionScheme(),
            userEncryptionKey);
      }
      catch (AuthenticationFailedException | UserNotFoundException e)
      {
        responseData =
          new AuthenticateResponseData(AuthenticateResponseData.ERROR_CODE_UNKNOWN_ERROR,
            "Failed to authenticate the user (" + requestData.getUser() + ")");
      }
      catch (Throwable e)
      {
        logger.error("Failed to authenticate the user (" + requestData.getUser() + ")", e);

        responseData =
          new AuthenticateResponseData(AuthenticateResponseData.ERROR_CODE_UNKNOWN_ERROR,
            "Failed to authenticate the user (" + requestData.getUser() + "): " + e.getMessage());
      }

      Message responseMessage = messageTranslator.toMessage(responseData);

      responseMessage.setIsEncryptionDisabled(true);

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processCheckUserExistsMessage(Message requestMessage)
    throws MessageHandlerException
  {
    MessageTranslator messageTranslator;

    try
    {
      messageTranslator = new MessageTranslator(requestMessage.getUser(),
          requestMessage.getOrganisation(), requestMessage.getDevice());

      CheckUserExistsRequestData requestData = messageTranslator.fromMessage(requestMessage,
        new CheckUserExistsRequestData());

      if (logger.isDebugEnabled())
      {
        logger.debug("Checking if the user (" + requestData.getUser()
            + ") associated with the organisation (" + requestData.getOrganisation() + ") exists");
      }

      if (securityService.getUserDirectoryIdForUser(requestData.getUser()) != -1)
      {
        CheckUserExistsResponseData responseData = new CheckUserExistsResponseData(true);

        Message responseMessage = messageTranslator.toMessage(responseData);

        responseMessage.setIsEncryptionDisabled(true);

        return responseMessage;
      }
      else
      {
        CheckUserExistsResponseData responseData = new CheckUserExistsResponseData(false);

        Message responseMessage = messageTranslator.toMessage(responseData);

        responseMessage.setIsEncryptionDisabled(false);

        return responseMessage;
      }
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processGetCodeCategoryRequestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      GetCodeCategoryRequestData requestData = messageTranslator.fromMessage(requestMessage,
        new GetCodeCategoryRequestData());

      GetCodeCategoryResponseData responseData = null;

      try
      {
        CodeCategory codeCategory = codesService.getCodeCategory(requestData.getId(), true);

        if (codeCategory != null)
        {
          // TODO: IMPLEMENT ORGANISATION SECURITY CHECK HERE -- MARCUS
          if (!StringUtil.isNullOrEmpty(codeCategory.getOrganisation()))
          {
            if (!codeCategory.getOrganisation().equals(requestMessage.getOrganisation()))
            {
              logger.warn("The user (" + requestMessage.getUser()
                  + ") associated with organisation (" + requestMessage.getOrganisation()
                  + ") is attempting to retrieve the code category (" + codeCategory.getId()
                  + ") associated with organisation (" + codeCategory.getOrganisation() + ")");
            }
          }

          if ((codeCategory.getCategoryType() == CodeCategoryType.LOCAL_STANDARD)
              || (codeCategory.getCategoryType() == CodeCategoryType.LOCAL_CUSTOM))
          {
            CodeCategoryData codeCategoryData = new CodeCategoryData(codeCategory);

            responseData = new GetCodeCategoryResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_HTTP_SERVICE)
          {
            CodeCategory remoteCodeCategory = codesService.getRemoteCodeCategory(codeCategory,
              requestData.getLastRetrieved(), requestData.getReturnCodesIfCurrent());

            CodeCategoryData codeCategoryData = new CodeCategoryData(remoteCodeCategory);

            responseData = new GetCodeCategoryResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_WEB_SERVICE)
          {
            CodeCategory remoteCodeCategory = codesService.getRemoteCodeCategory(codeCategory,
              requestData.getLastRetrieved(), requestData.getReturnCodesIfCurrent());

            CodeCategoryData codeCategoryData = new CodeCategoryData(remoteCodeCategory);

            responseData = new GetCodeCategoryResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.CODE_PROVIDER)
          {
            CodeCategory codeProviderCodeCategory =
              codesService.getCodeProviderCodeCategory(codeCategory,
                requestData.getLastRetrieved(), requestData.getReturnCodesIfCurrent());

            if (codeProviderCodeCategory == null)
            {
              responseData = new GetCodeCategoryResponseData(
                  GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR,
                  "Failed to retrieve the code provider code category (" + requestData.getId()
                  + "): The code provider code category could not be found");
            }
            else
            {
              CodeCategoryData codeCategoryData = new CodeCategoryData(codeProviderCodeCategory);

              responseData = new GetCodeCategoryResponseData(codeCategoryData);
            }
          }
        }
        else
        {
          responseData =
            new GetCodeCategoryResponseData(GetCodeCategoryResponseData.ERROR_CODE_UNKNOWN_ERROR,
              "Failed to retrieve the code category (" + requestData.getId()
              + "): The code category could not be found");
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the code category (" + requestData.getId() + ")", e);

        responseData =
          new GetCodeCategoryResponseData(GetCodeCategoryResponseData.ERROR_CODE_UNKNOWN_ERROR,
            "Failed to retrieve the code category (" + requestData.getId() + "): "
            + ExceptionUtil.getNestedMessages(e));
      }

      return messageTranslator.toMessage(responseData);
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processGetCodeCategoryWithParametersRequestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      GetCodeCategoryWithParametersRequestData requestData =
        messageTranslator.fromMessage(requestMessage,
          new GetCodeCategoryWithParametersRequestData());

      GetCodeCategoryWithParametersResponseData responseData = null;

      try
      {
        CodeCategory codeCategory = codesService.getCodeCategoryWithParameters(requestData.getId(),
          requestData.getParameters(), true);

        if (codeCategory != null)
        {
          // TODO: IMPLEMENT ORGANISATION SECURITY CHECK HERE -- MARCUS
          if (!StringUtil.isNullOrEmpty(codeCategory.getOrganisation()))
          {
            if (!codeCategory.getOrganisation().equals(requestMessage.getOrganisation()))
            {
              logger.warn("The user (" + requestMessage.getUser()
                  + ") associated with organisation (" + requestMessage.getOrganisation()
                  + ") is attempting to retrieve the code category (" + codeCategory.getId()
                  + ") associated with organisation (" + codeCategory.getOrganisation() + ")");
            }
          }

          if ((codeCategory.getCategoryType() == CodeCategoryType.LOCAL_STANDARD)
              || (codeCategory.getCategoryType() == CodeCategoryType.LOCAL_CUSTOM))
          {
            CodeCategoryData codeCategoryData = new CodeCategoryData(codeCategory);

            responseData = new GetCodeCategoryWithParametersResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_HTTP_SERVICE)
          {
            CodeCategory remoteCodeCategory =
              codesService.getRemoteCodeCategoryWithParameters(codeCategory,
                requestData.getParameters(), requestData.getLastRetrieved(),
                requestData.getReturnCodesIfCurrent());

            CodeCategoryData codeCategoryData = new CodeCategoryData(remoteCodeCategory);

            responseData = new GetCodeCategoryWithParametersResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_WEB_SERVICE)
          {
            CodeCategory remoteCodeCategory =
              codesService.getRemoteCodeCategoryWithParameters(codeCategory,
                requestData.getParameters(), requestData.getLastRetrieved(),
                requestData.getReturnCodesIfCurrent());

            CodeCategoryData codeCategoryData = new CodeCategoryData(remoteCodeCategory);

            responseData = new GetCodeCategoryWithParametersResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.CODE_PROVIDER)
          {
            CodeCategory codeProviderCodeCategory =
              codesService.getCodeProviderCodeCategoryWithParameters(codeCategory,
                requestData.getParameters(), requestData.getLastRetrieved(),
                requestData.getReturnCodesIfCurrent());

            if (codeProviderCodeCategory == null)
            {
              responseData = new GetCodeCategoryWithParametersResponseData(
                  GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR,
                  "Failed to retrieve the code provider code category (" + requestData.getId()
                  + ") with parameters: The code provider code category could not be found");
            }
            else
            {
              CodeCategoryData codeCategoryData = new CodeCategoryData(codeProviderCodeCategory);

              responseData = new GetCodeCategoryWithParametersResponseData(codeCategoryData);
            }
          }
        }
        else
        {
          responseData = new GetCodeCategoryWithParametersResponseData(
              GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR,
              "Failed to retrieve the code category (" + requestData.getId()
              + ") with parameters: The code category could not be found");
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the code category (" + requestData.getId() + ")", e);

        responseData = new GetCodeCategoryWithParametersResponseData(
            GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR,
            "Failed to retrieve the code category (" + requestData.getId() + ") with parameters: "
            + ExceptionUtil.getNestedMessages(e));
      }

      return messageTranslator.toMessage(responseData);
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processMessagePartDownloadTestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      // MessagePartDownloadTestRequestData requestData =
      // messageTranslator.fromMessage(requestMessage, new MessagePartDownloadTestRequestData());

      MessagePartDownloadTestResponseData responseData =
        new MessagePartDownloadTestResponseData(new byte[128 * 1024]);

      Message responseMessage = messageTranslator.toMessage(responseData);

      logger.debug(responseMessage.toString());

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processRegisterMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      RegisterRequestData requestData = messageTranslator.fromMessage(requestMessage,
        new RegisterRequestData());

      // Authenticate the user
      RegisterResponseData responseData;

      try
      {
        securityService.authenticate(requestData.getUser(), requestData.getPassword());

        byte[] userEncryptionKey = messagingService.deriveUserDeviceEncryptionKey(
            requestData.getPreferredEncryptionScheme(), requestData.getUser(),
            requestData.getOrganisation(), requestData.getDevice());

        if (logger.isDebugEnabled())
        {
          logger.debug("Generated the encryption key ("
              + Base64.encodeBytes(userEncryptionKey, false) + ") using the encryption scheme ("
              + requestData.getPreferredEncryptionScheme() + ") for the user ("
              + requestData.getUser() + ") from the organisation (" + requestData.getOrganisation()
              + ") using the device (" + requestData.getDevice() + ")");
        }
        else
        {
          logger.info("Generated the encryption key using the encryption scheme ("
              + requestData.getPreferredEncryptionScheme() + ") for the user ("
              + requestData.getUser() + ") from the organisation (" + requestData.getOrganisation()
              + ") using the device (" + requestData.getDevice() + ")");
        }

        responseData = new RegisterResponseData(requestData.getPreferredEncryptionScheme(),
            userEncryptionKey);
      }
      catch (AuthenticationFailedException | UserNotFoundException e)
      {
        responseData =
          new RegisterResponseData(RegisterResponseData.ERROR_CODE_AUTHENTICATION_FAILED,
            "Failed to authenticate the user (" + requestData.getUser() + ")");
      }
      catch (Throwable e)
      {
        logger.error("Failed to authenticate the user (" + requestData.getUser() + ")", e);

        responseData = new RegisterResponseData(RegisterResponseData.ERROR_CODE_UNKNOWN_ERROR,
            "Failed to authenticate the user (" + requestData.getUser() + "): " + e.getMessage());
      }

      Message responseMessage = messageTranslator.toMessage(responseData);

      responseMessage.setIsEncryptionDisabled(true);

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processSubmitErrorReportRequestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      SubmitErrorReportRequestData requestData = messageTranslator.fromMessage(requestMessage,
        new SubmitErrorReportRequestData());

      ErrorReport errorReport = new ErrorReport(requestData.getId(),
        requestData.getApplicationId(), requestData.getApplicationVersion(),
        requestData.getDescription(), requestData.getDetail(), requestData.getFeedback(),
        requestData.getWhen(), requestData.getWho(), requestData.getDevice(),
        requestData.getData());

      messagingService.createErrorReport(errorReport);

      SubmitErrorReportResponseData responseData = new SubmitErrorReportResponseData(0,
        SubmitErrorReportResponseData.ERROR_MESSAGE_SUCCESS, requestData.getId());

      return messageTranslator.toMessage(responseData);
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }

  private Message processTestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      logger.info(requestMessage.toString());

      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUser(),
        requestMessage.getOrganisation(), requestMessage.getDevice());

      TestRequestData requestData = messageTranslator.fromMessage(requestMessage,
        new TestRequestData());

      TestResponseData responseData = new TestResponseData(requestData.getTestValue());

      Message responseMessage = messageTranslator.toMessage(responseData);

      logger.info(responseMessage.toString());

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException("Failed to process the message ("
          + requestMessage.getType() + ")", e);
    }
  }
}
