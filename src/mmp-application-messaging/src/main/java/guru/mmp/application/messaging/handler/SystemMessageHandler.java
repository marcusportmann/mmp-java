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

package guru.mmp.application.messaging.handler;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.configuration.IConfigurationService;
import guru.mmp.application.messaging.ErrorReport;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessageTranslator;
import guru.mmp.application.messaging.messages.*;
import guru.mmp.application.security.AuthenticationFailedException;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.security.UserNotFoundException;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SystemMessageHandler</code> class implements the message handler that processes the
 * "system" messages for the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
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

  /* Configuration Service */
  @Inject
  private IConfigurationService configurationService;

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
   * Process the specified message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   */
  public Message processMessage(Message message)
    throws MessageHandlerException
  {
    // Process a "Authenticate Request" message
    if (message.getTypeId().equals(AuthenticateRequestData.MESSAGE_TYPE_ID))
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

    throw new MessageHandlerException(String.format(
        "Failed to process the unrecognised message (%s) with type (%s) from the user (%s) and "
        + "device (%s)", message.getId(), message.getTypeId(), message.getUsername(),
        message.getDeviceId()));
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

      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      AnotherTestRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new AnotherTestRequestData());

      AnotherTestResponseData responseData = new AnotherTestResponseData(
          requestData.getTestValue(), requestData.getTestData());

      Message responseMessage = messageTranslator.toMessage(responseData,
          requestMessage.getCorrelationId());

      logger.info(responseMessage.toString());

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processAuthenticateMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      AuthenticateRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new AuthenticateRequestData());

      // Authenticate the user
      AuthenticateResponseData responseData;

      try
      {
        UUID userDirectoryId = securityService.authenticate(requestData.getUsername(),
            requestData.getPassword());

        List<Organisation> organisations = securityService.getOrganisationsForUserDirectory(
            userDirectoryId);

        byte[] userEncryptionKey = messagingService.deriveUserDeviceEncryptionKey(
            requestData.getUsername(), requestData.getDeviceId());

        if (logger.isDebugEnabled())
        {
          logger.debug(String.format(
              "Generated the encryption key (%s) for the user (%s) and the device (%s)",
              Base64.encodeBytes(userEncryptionKey, false), requestData.getUsername(),
              requestData.getDeviceId()));
        }

        responseData = new AuthenticateResponseData(organisations, userEncryptionKey,
            new HashMap<>());
      }
      catch (AuthenticationFailedException | UserNotFoundException e)
      {
        responseData = new AuthenticateResponseData(AuthenticateResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format("Failed to authenticate the user (%s)",
            requestData.getUsername()));
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to authenticate the user (%s)",
            requestData.getUsername()), e);

        responseData = new AuthenticateResponseData(AuthenticateResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format("Failed to authenticate the user (%s): %s",
            requestData.getUsername(), e.getMessage()));
      }

      Message responseMessage = messageTranslator.toMessage(responseData,
          requestMessage.getCorrelationId());

      responseMessage.setIsEncryptionDisabled(true);

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processCheckUserExistsMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      CheckUserExistsRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new CheckUserExistsRequestData());

      CheckUserExistsResponseData responseData;

      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Checking if the user (%s) exists",
              requestData.getUsername()));
        }

        if (securityService.getUserDirectoryIdForUser(requestData.getUsername()) != null)
        {
          responseData = new CheckUserExistsResponseData(true);
        }
        else
        {
          responseData = new CheckUserExistsResponseData(false);
        }
      }
      catch (Throwable e)
      {
        responseData = new CheckUserExistsResponseData(CheckUserExistsResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format("Failed to check if the user (%s) exists: %s",
            requestData.getUsername(), e.getMessage()));

      }

      Message responseMessage = messageTranslator.toMessage(responseData);

      responseMessage.setIsEncryptionDisabled(true);

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processGetCodeCategoryRequestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      GetCodeCategoryRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new GetCodeCategoryRequestData());

      GetCodeCategoryResponseData responseData = null;

      try
      {
        CodeCategory codeCategory = codesService.getCodeCategory(requestData.getId(), true);

        if (codeCategory != null)
        {
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
            CodeCategory codeProviderCodeCategory = codesService.getCodeProviderCodeCategory(
                codeCategory, requestData.getLastRetrieved(),
                requestData.getReturnCodesIfCurrent());

            if (codeProviderCodeCategory == null)
            {
              responseData = new GetCodeCategoryResponseData(
                  GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR, String.format(
                  "Failed to retrieve the code provider code category (%s): "
                  + "The code provider code category could not be found", requestData.getId()));
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
          responseData = new GetCodeCategoryResponseData(GetCodeCategoryResponseData
              .ERROR_CODE_UNKNOWN_ERROR, String.format(
              "Failed to retrieve the code category (%s): The code category could not be found",
              requestData.getId()));
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to retrieve the code category (%s)",
            requestData.getId()), e);

        responseData = new GetCodeCategoryResponseData(GetCodeCategoryResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format(
            "Failed to retrieve the code category (%s): %s", requestData.getId(),
            ExceptionUtil.getNestedMessages(e)));
      }

      return messageTranslator.toMessage(responseData);
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processGetCodeCategoryWithParametersRequestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      GetCodeCategoryWithParametersRequestData requestData = messageTranslator.fromMessage(
          requestMessage, new GetCodeCategoryWithParametersRequestData());

      GetCodeCategoryWithParametersResponseData responseData = null;

      try
      {
        CodeCategory codeCategory = codesService.getCodeCategoryWithParameters(requestData.getId(),
            requestData.getParameters(), true);

        if (codeCategory != null)
        {
          if ((codeCategory.getCategoryType() == CodeCategoryType.LOCAL_STANDARD)
              || (codeCategory.getCategoryType() == CodeCategoryType.LOCAL_CUSTOM))
          {
            CodeCategoryData codeCategoryData = new CodeCategoryData(codeCategory);

            responseData = new GetCodeCategoryWithParametersResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_HTTP_SERVICE)
          {
            CodeCategory remoteCodeCategory = codesService.getRemoteCodeCategoryWithParameters(
                codeCategory, requestData.getParameters(), requestData.getLastRetrieved(),
                requestData.getReturnCodesIfCurrent());

            CodeCategoryData codeCategoryData = new CodeCategoryData(remoteCodeCategory);

            responseData = new GetCodeCategoryWithParametersResponseData(codeCategoryData);
          }
          else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_WEB_SERVICE)
          {
            CodeCategory remoteCodeCategory = codesService.getRemoteCodeCategoryWithParameters(
                codeCategory, requestData.getParameters(), requestData.getLastRetrieved(),
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
                  GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR, String.format(
                  "Failed to retrieve the code provider code category (%s) with parameters: The "
                  + "code provider code category could not be found", requestData.getId()));
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
              GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR, String.format(
              "Failed to retrieve the code category (%s) with parameters: The code category could "
              + "not be found", requestData.getId()));
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to retrieve the code category (%s)",
            requestData.getId()), e);

        responseData = new GetCodeCategoryWithParametersResponseData(
            GetCodeCategoryWithParametersResponseData.ERROR_CODE_UNKNOWN_ERROR, String.format(
            "Failed to retrieve the code category (%s) with parameters: %s", requestData.getId(),
            ExceptionUtil.getNestedMessages(e)));
      }

      return messageTranslator.toMessage(responseData);
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processSubmitErrorReportRequestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      SubmitErrorReportRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new SubmitErrorReportRequestData());

      ErrorReport errorReport = new ErrorReport(requestData.getId(),
          requestData.getApplicationId(), requestData.getApplicationVersion(),
          requestData.getDescription(), requestData.getDetail(), requestData.getFeedback(),
          requestData.getWhen(), requestData.getWho(), requestData.getDeviceId(),
          requestData.getData());

      messagingService.createErrorReport(errorReport);

      SubmitErrorReportResponseData responseData = new SubmitErrorReportResponseData(0,
          SubmitErrorReportResponseData.ERROR_MESSAGE_SUCCESS, requestData.getId());

      return messageTranslator.toMessage(responseData);
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processTestMessage(Message requestMessage)
    throws MessageHandlerException
  {
    try
    {
      logger.info(requestMessage.toString());

      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      TestRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new TestRequestData());

      TestResponseData responseData = new TestResponseData(requestData.getTestValue());

      Message responseMessage = messageTranslator.toMessage(responseData);

      logger.info(responseMessage.toString());

      return responseMessage;
    }
    catch (Throwable e)
    {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }
}
