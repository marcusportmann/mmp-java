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

package guru.mmp.common.service.ws.security;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.security.context.ApplicationSecurityContext;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerResult;
import org.apache.ws.security.message.token.Timestamp;
import org.apache.ws.security.util.WSSecurityUtil;
import org.apache.xml.security.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.security.auth.callback.CallbackHandler;
import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebServiceClientSecurityHandler</code> class is a JAX-WS handler that implements the
 * custom Web Service Security Model for JAX-WS web service clients.
 *
 * @author Marcus Portmann
 */
public class WebServiceClientSecurityHandler extends WebServiceSecurityHandlerBase
{
  /* Logger */
  private static final Logger logger =
    LoggerFactory.getLogger(WebServiceClientSecurityHandler.class);

  /**
   * The application security context holding the security information for the application
   * acting as a secure web service client.
   */
  private ApplicationSecurityContext applicationSecurityContext = null;

  /* The crypto properties that will be set on the message context for the web service client. */
  private Properties cryptoProperties = null;

  /**
   * Constructs a new <code>WebServiceSecurityHandler</code>.
   */
  public WebServiceClientSecurityHandler()
  {
    super(true);

    // Retrieve the application security context and confirm that it has been initialised.
    applicationSecurityContext = ApplicationSecurityContext.getContext();

    if (!applicationSecurityContext.isInitialised())
    {
      throw new WebServiceSecurityHandlerException(
          "Failed to initialise the web service client security handler: "
          + "The application security context has not been initialised");
    }

    try
    {
      setOption(WSHandlerConstants.USER, applicationSecurityContext.getKeyStoreAlias());
      setOption(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
      setOption(WSHandlerConstants.SIG_ALGO, "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
      setOption(WSHandlerConstants.SIGNATURE_PARTS, "Body");
      setOption(WSHandlerConstants.SIG_KEY_ID, "DirectReference");
      setOption(WSHandlerConstants.MUST_UNDERSTAND, "true");
      setOption(WSHandlerConstants.ENABLE_SIGNATURE_CONFIRMATION, "false");
      setOption(WSHandlerConstants.SIG_SUBJECT_CERT_CONSTRAINTS, ".*");

      cryptoProperties = new Properties();
      cryptoProperties.put("org.apache.ws.security.crypto.provider",
          "guru.mmp.common.service.ws.security.WebServiceClientCrypto");
      setOption(WSHandlerConstants.SIG_PROP_REF_ID, MESSAGE_CONTEXT_CRYPTO_PROPERTIES);

      secEngine.getWssConfig().setValidator(WSSecurityEngine.SIGNATURE, new SignatureTrustValidator());
    }
    catch (WebServiceSecurityHandlerException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new WebServiceSecurityHandlerException(
          "Failed to initialise the web service client security handler: " + e.getMessage(), e);
    }

    if (logger.isDebugEnabled())
    {
      logger.debug("Successfully initialised the web service client security handler for"
          + " the application (" + applicationSecurityContext.getApplicationDN() + ")");
    }
  }

  /**
   * Returns the configuration option identified by the specified key.
   *
   * @param key the key identifying the configuration option
   *
   * @return the configuration option identified by the specified key
   *
   * @see org.apache.ws.security.handler.WSHandler#getOption(String)
   */
  @Override
  public Object getOption(String key)
  {
    if ("pswd".equalsIgnoreCase(key))
    {
      return getApplicationSecurityContext().getKeyStorePassword();
    }
    else
    {
      return super.getOption(key);
    }
  }

  /**
   * Returns the password used to access the key store that contains the private key for the
   * secure web service client. This private key is used to sign the SOAP messages sent by the
   * client.
   *
   * @param messageContext the message context
   *
   * @return The password used to access the key store that contains the private key for the secure
   *         web service client. This private key is used to sign the SOAP messages sent by the
   *         client.
   *
   * @see org.apache.ws.security.handler.WSHandler#getPassword(Object)
   */
  @Override
  public String getPassword(Object messageContext)
  {
    return applicationSecurityContext.getKeyStorePassword();
  }

  /**
   * The <code>handleFault</code> method is invoked for fault message processing. Refer to the
   * description of the handler framework in the JAX-WS specification for full details.
   *
   * @param messageContext the message context
   *
   * @return <code>true</code> if handler fault processing should continue for the current message
   *         or <code>false</code> if fault processing should stop
   *
   * @see javax.xml.ws.handler.Handler#handleFault(MessageContext)
   */
  @Override
  public boolean handleFault(SOAPMessageContext messageContext)
  {
    WebServiceClientSecurityContext securityContext = WebServiceClientSecurityContext.getContext();

    securityContext.reset();

    return true;
  }

  /**
   * The <code>handleMessage</code> method is invoked for normal processing of inbound and outbound
   * messages. Refer to the description of the handler framework in the JAX-WS specification for
   * full details.
   *
   * @param messageContext the message context
   *
   * @return <code>true</code> if handler processing should continue for the current message
   *         or <code>false</code> if processing should stop
   */
  @Override
  public boolean handleMessage(SOAPMessageContext messageContext)
  {
    // Retrieve the SOAP protocol version
    SOAPVersion soapVersion = getSOAPVersion(messageContext);

    Boolean outboundProperty =
      (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

    messageContext.put(MESSAGE_CONTEXT_CRYPTO_PROPERTIES, cryptoProperties);

    RequestData requestData = new RequestData();

    requestData.setMsgContext(messageContext);

    try
    {
      // OUT
      if (outboundProperty)
      {
        return doRequest(soapVersion, messageContext, requestData);
      }

      // IN
      else
      {
        return doResponse(soapVersion, messageContext, requestData);
      }
    }
    catch (Throwable e)
    {
      throw new WebServiceSecurityHandlerException("The web service client security handler"
          + " failed to handle the SOAP message", e);
    }
    finally
    {
      requestData.clear();
    }
  }

  /**
   * Returns the key store containing the certificates for the web service or web service client.
   *
   * @return the key store containing the certificates for the web service or web service client
   */
  @Override
  protected KeyStore getKeyStore()
  {
    return applicationSecurityContext.getKeyStore();
  }

  /**
   * Returns the logger associated with the handler.
   *
   * @return the logger associated with the handler
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }

  /**
   * Process the request sent to the web service.
   *
   * @param soapVersion    the version of the SOAP protocol
   * @param messageContext the message context
   * @param requestData    the request data
   *
   * @return <code>true</code> if handler processing should continue for the current message
   *         or <code>false</code> if processing should stop
   *
   * @throws WSSecurityException
   */
  private boolean doRequest(SOAPVersion soapVersion, SOAPMessageContext messageContext,
      RequestData requestData)
    throws WSSecurityException
  {
    // Initialise the request
    requestData.getSignatureParts().clear();
    requestData.getEncryptParts().clear();
    requestData.setNoSerialization(false);

    // Decode the action(s) being performed e.g. Sign, Encrypt, etc
    List<Integer> actions = new ArrayList<>();

    String action = (String) getOption(WSHandlerConstants.ACTION);

    if (action == null)
    {
      throw new WSSecurityException("Failed to process the SOAP request: No action defined");
    }

    int doAction = WSSecurityUtil.decodeAction(action, actions);

    if (doAction == WSConstants.NO_SECURITY)
    {
      logger.warn("No security-related actions specified."
          + " The SOAP request will not be modified");

      return true;
    }

    /*
     * For every action we need a username, so get this now.
     */
    requestData.setUsername((String) getOption(WSHandlerConstants.USER));

    /*
     * Now we perform some set-up for UsernameToken and Signature functions. No need to do it for
     * encryption only. Check if username is available and then get a password.
     */
    if ((doAction & (WSConstants.SIGN | WSConstants.UT | WSConstants.UT_SIGN)) != 0)
    {
      /*
       * We need a username - if none throw an WebServiceException. For encryption
       * there is a specific parameter to get a username.
       */
      if ((requestData.getUsername() == null) || requestData.getUsername().equals(""))
      {
        throw new WSSecurityException("Failed to process the SOAP request:"
            + " Empty username for the specified action (" + action + ")");
      }
    }

    // Retrieve the SOAP message
    SOAPMessage message = messageContext.getMessage();

    // Retrieve the SOAP part of the SOAP message
    SOAPPart sPart = message.getSOAPPart();

    // Convert the SOAP message to an org.w3c.dom.Document
    Document doc = messageToDocument(message, true);

    doSenderAction(doAction, doc, requestData, actions, true);

    // Convert the org.w3c.dom.Document to a String and set it as the content on the SOAP part
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    XMLUtils.outputDOM(doc, os, true);

    try
    {
      sPart.setContent(new StreamSource(new ByteArrayInputStream(os.toByteArray())));

      /*
       * This step is required to force the JAX-WS runtime to acknowledge that the message has
       * been updated via the setContent call above. If we do not do this then certain JAX-WS
       * runtimes (i.e. WebSphere) will assume that the message has not been modified and send
       * the original message content without the WS-Security additions.
       */
      messageContext.setMessage(messageContext.getMessage());
    }
    catch (SOAPException e)
    {
      throw new WSSecurityException("Failed to process the SOAP response:"
          + " Couldn't set content on the SOAPPart", e);
    }

    return true;
  }

  /**
   * Process the response received from the web service.
   *
   * @param soapVersion    the version of the SOAP protocol
   * @param messageContext the message context
   * @param requestData    the request data
   *
   * @return <code>true</code> if handler processing should continue for the current message
   *         or <code>false</code> if processing should stop
   *
   * @throws WSSecurityException
   */
  @SuppressWarnings({ "unchecked" })
  private boolean doResponse(SOAPVersion soapVersion, SOAPMessageContext messageContext,
      RequestData requestData)
    throws WSSecurityException, SOAPException
  {
    /*
     * Retrieve the web service client security context associated with the current thread of
     * execution and reset the service certificate. The context is stored in thread-local storage.
     */
    WebServiceClientSecurityContext securityContext = WebServiceClientSecurityContext.getContext();

    securityContext.setServiceCertificate(null);

    // Decode the action(s) being performed e.g. Sign, Encrypt, etc
    List<Integer> actions = new ArrayList<>();

    String action = (String) getOption(WSHandlerConstants.ACTION);

    if (action == null)
    {
      throw new WSSecurityException("Failed to process the SOAP response: No action defined");
    }

    int doAction = WSSecurityUtil.decodeAction(action, actions);

    if (doAction == WSConstants.NO_SECURITY)
    {
      logger.warn("No security-related actions specified."
          + " The SOAP response will not be processed");

      return true;
    }

    // Retrieve the actor
    String actor = (String) getOption(WSHandlerConstants.ACTOR);

    // Retrieve the SOAP message
    SOAPMessage message = messageContext.getMessage();

    // Retrieve the SOAP part of the SOAP message
    SOAPPart sPart = message.getSOAPPart();

    // Convert the SOAP message to an org.w3c.dom.Document
    Document doc = messageToDocument(message, false);

    // Check if it's a fault. Don't process faults.
    if (soapVersion == SOAPVersion.SOAP_1_1)
    {
      if (WSSecurityUtil.findElement(doc.getDocumentElement(), "Fault",
          SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE) != null)
      {
        return false;
      }
    }
    else if (soapVersion == SOAPVersion.SOAP_1_2)
    {
      if (WSSecurityUtil.findElement(doc.getDocumentElement(), "Fault",
          SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE) != null)
      {
        return false;
      }
    }

    // To check a UsernameToken or to decrypt an encrypted message we need a password.
    CallbackHandler passwordCallbackHandler = null;

    if ((doAction & (WSConstants.ENCR | WSConstants.UT)) != 0)
    {
      passwordCallbackHandler = getPasswordCallbackHandler(requestData);
    }

    /*
     * Get and check the Signature specific parameters first because they may be used for
     * encryption too.
     */
    doReceiverAction(doAction, requestData);

    List<WSSecurityEngineResult> wsResults = secEngine.processSecurityHeader(doc, actor,
      passwordCallbackHandler, requestData.getSigCrypto(), requestData.getDecCrypto());

    if (wsResults == null)
    {
      throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE,
          "Failed to process the SOAP response:"
          + " The response does not contain the required security header");
    }

    if (requestData.getWssConfig().isEnableSignatureConfirmation())
    {
      checkSignatureConfirmation(requestData, wsResults);
    }

    /*
     * If we had some security processing, get the original SOAP part of message and replace it
     * with the new SOAP part. This new part may contain decrypted elements.
     */
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    XMLUtils.outputDOM(doc, os, true);

    try
    {
      sPart.setContent(new StreamSource(new ByteArrayInputStream(os.toByteArray())));
    }
    catch (SOAPException e)
    {
      throw new WSSecurityException("Failed to process the SOAP response:"
          + " Couldn't set content on the SOAPPart", e);
    }

    /*
     * After setting the new current message, probably modified because of decryption, we need to
     * locate the security header. That is, we force the runtime (with getEnvelope()) to parse the
     * string, build the new header. Then we examine, look up the security header and set the
     * header as processed.
     *
     * Please note: find all header elements that contain the same actor that was given to
     * processSecurityHeader(). Then check if there is a security header with this actor.
     */
    SOAPHeader soapHeader;

    try
    {
      soapHeader = message.getSOAPPart().getEnvelope().getHeader();
    }
    catch (Exception e)
    {
      throw new WSSecurityException("Failed to process the SOAP response:"
          + " Cannot get SOAP header after security processing", e);
    }

    Iterator<?> headers;

    if (actor != null)
    {
      headers = soapHeader.examineHeaderElements(actor);
    }
    else
    {
      headers = soapHeader.examineAllHeaderElements();
    }

    SOAPHeaderElement securityHeaderElement = null;

    while (headers.hasNext())
    {
      SOAPHeaderElement hE = (SOAPHeaderElement) headers.next();

      if (hE.getElementName().getLocalName().equals(WSConstants.WSSE_LN)
          && hE.getNamespaceURI().equals(WSConstants.WSSE_NS))
      {
        securityHeaderElement = hE;

        break;
      }
    }

    // Disable "must understand" header -- David Goosen for details
    securityHeaderElement.setMustUnderstand(false);

    /*
     * Now we can check the certificate used to sign the message. In the following implementation
     * the certificate is only trusted if either it itself or the certificate of the issuer is
     * installed in the keystore.
     *
     * Note: the method verifyTrust(X509Certificate) allows custom implementations with other
     * validation algorithms for subclasses.
     */

    // Extract the signature action result from the action vector
    WSSecurityEngineResult signActionResult = WSSecurityUtil.fetchActionResult(wsResults,
      WSConstants.SIGN);

    // Verify the certificate associated with the signature action
    X509Certificate returnCert = null;

    if (signActionResult != null)
    {
      returnCert =
        (X509Certificate) signActionResult.get(WSSecurityEngineResult.TAG_X509_CERTIFICATE);

      if (returnCert != null)
      {
        if (!verifyTrust(returnCert, requestData))
        {
          throw new WSSecurityException("Failed to process the SOAP response:"
              + " The certificate used to sign the response is not trusted");
        }
      }
      else
      {
        throw new WSSecurityException("Failed to process the SOAP response:"
            + " Unable to retrieve the certificate used to sign the response");
      }
    }

    /*
     * Perform further checks on the timestamp that was transmitted in the header. In the following
     * implementation the timestamp is valid if it was created after (now-ttl), where ttl is set on
     * server side, not by the client.
     *
     * Note: the method verifyTimestamp(Timestamp) allows custom implementations with other
     * validation algorithms for subclasses.
     */

    // Extract the timestamp action result from the action vector
    WSSecurityEngineResult timestampActionResult = WSSecurityUtil.fetchActionResult(wsResults,
      WSConstants.TS);

    if (timestampActionResult != null)
    {
      Timestamp timestamp =
        (Timestamp) timestampActionResult.get(WSSecurityEngineResult.TAG_TIMESTAMP);

      if ((timestamp != null) && requestData.getWssConfig().isTimeStampStrict())
      {
//      if (!verifyTimestamp(timestamp, decodeTimeToLive(requestData)))
//      {
//        throw new WSSecurityException("Failed to process the SOAP request:"
//            + " The timestamp could not be validated");
//      }
      }
    }

    // Now check the security actions: do they match, in right order?
    if (!checkReceiverResults(wsResults, actions))
    {
      throw new WSSecurityException("Failed to process the SOAP response:"
          + " Security processing failed (security actions mismatch)");
    }

    /*
     * All ok up to this point. Now construct and setup the security result structure. The service
     * may fetch this and check it.
     */
    List<Object> results;

    if ((results = (List<Object>) messageContext.get(WSHandlerConstants.RECV_RESULTS)) == null)
    {
      results = new ArrayList<>();
      messageContext.put(WSHandlerConstants.RECV_RESULTS, results);
    }

    WSHandlerResult rResult = new WSHandlerResult(actor, wsResults);

    results.add(0, rResult);

    // Save the service's certificate on the web service client security context
    securityContext.setServiceCertificate(returnCert);

    return true;
  }

  /**
   * Returns the application security context.
   *
   * @return the application security context
   */
  private ApplicationSecurityContext getApplicationSecurityContext()
  {
    return ApplicationSecurityContext.getContext();
  }
}
