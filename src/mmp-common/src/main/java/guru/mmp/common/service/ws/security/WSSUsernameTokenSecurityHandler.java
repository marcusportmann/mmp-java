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

package guru.mmp.common.service.ws.security;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>WSSUsernameTokenSecurityHandler</code> class is a JAX-WS handler that implements the
 * WS-Security Username Token security model for JAX-WS web service clients.
 *
 * @author Marcus Portmann
 */
public class WSSUsernameTokenSecurityHandler
  implements SOAPHandler<SOAPMessageContext>
{
  /**
   * The name of the "flow" configuration option.
   */
  @SuppressWarnings("unused")
  protected static final String FLOW_CONFIG_OPTION = "flow";
  private static final String WSS_USERNAME_TOKEN_PROFILE_PASSWORD_TEXT_TYPE = "http://docs"
      + ".oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
  private static final String WSS_WS_SECURITY_UTILITY_NAMESPACE = "http://docs.oasis-open"
      + ".org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(
      WSSUsernameTokenSecurityHandler.class);

  /**
   * The SOAP Protocol 1.1 factory.
   */
  private SOAPFactory soap11Factory = null;

  /**
   * The SOAP Protocol 1.2 factory.
   */
  private SOAPFactory soap12Factory = null;

  /**
   * The password to use when accessing a web service with WS-Security Username Token security
   * enabled.
   */
  private String password;

  /**
   * The username to use when accessing a web service with WS-Security Username Token security
   * enabled.
   */
  private String username;

  /**
   * The enumeration identifying the possible SOAP protocol versions.
   */
  enum SOAPVersion { SOAP_1_1, SOAP_1_2 }

  /**
   * Constructs a new <code>WSSUsernameTokenSecurityHandler</code>.
   *
   * @param username the username to use when accessing a web service with WS-Security Username
   *                 Token security enabled
   * @param password the password to use when accessing a web service with WS-Security Username
   *                 Token security enabled
   */
  public WSSUsernameTokenSecurityHandler(String username, String password)
  {
    this.username = username;
    this.password = password;

    try
    {
      soap11Factory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
    }
    catch (SOAPException e)
    {
      throw new WebServiceSecurityHandlerException(
          "Failed to initialise the WS-Security Username Token security handler: " + "The SOAP 1"
          + ".1 factory could not be initialised", e);
    }

    try
    {
      soap12Factory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
    }
    catch (SOAPException e)
    {
      throw new WebServiceSecurityHandlerException(
          "Failed to initialise the WS-Security Username Token security handler: " + "The SOAP 1"
          + ".2 factory could not be initialised", e);
    }
  }

  /**
   * Called at the conclusion of a message exchange pattern just prior to the JAX-WS runtime
   * dispatching a message, fault or exception. Refer to the description of the handler framework
   * in the JAX-WS specification for full details.
   *
   * @param messageContext the message context
   *
   * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public void close(MessageContext messageContext) {}

  /**
   * Gets the header blocks that can be processed by this Handler instance.
   *
   * @return Set of <code>QNames</code> of header blocks processed by this handler instance.
   *         <code>QName</code> is the qualified name of the outermost element of the Header block.
   *
   * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
   */
  public Set<QName> getHeaders()
  {
    QName securityHeader = new QName(
        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
        "Security");

    HashSet<QName> headers = new HashSet<>();

    headers.add(securityHeader);

    return headers;
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
    // throw new UnsupportedOperationException("Not supported yet.");
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
    Boolean outboundProperty = (Boolean) messageContext.get(MessageContext
        .MESSAGE_OUTBOUND_PROPERTY);

    RequestData requestData = new RequestData();

    requestData.setMsgContext(messageContext);

    try
    {
      // OUT
      if (outboundProperty)
      {
        if (doRequest(messageContext))
        {
          if (logger.isDebugEnabled())
          {
            dumpSOAPMessage(messageContext.getMessage(), "Request Message");
          }

          return true;
        }
        else
        {
          return false;
        }
      }

      // IN
      else
      {
        return true;
      }
    }
    catch (Throwable e)
    {
      throw new WebServiceSecurityHandlerException(
          "The WS-Security Username Token security handler" + " failed to handle the SOAP message",
          e);
    }
    finally
    {
      requestData.clear();
    }
  }

  /**
   * Dump the SOAP message to STDOUT.
   *
   * @param message the SOAP message to dump
   */
  protected void dumpSOAPMessage(SOAPMessage message, String messageDescription)
  {
    if (message == null)
    {
      System.out.println("SOAP Message is null");

      return;
    }

    System.out.println("");
    System.out.println("-------------------------------------------------------------------------");
    System.out.println(" SOAP MESSAGE (" + messageDescription + ")");
    System.out.println("-------------------------------------------------------------------------");

    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      message.writeTo(baos);
      System.out.println(baos.toString(getMessageEncoding(message)));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Returns the SOAP 1.1 factory associated with the web service security handler.
   *
   * @return the SOAP 1.1 factory associated with the web service security handler
   */
  @SuppressWarnings("unused")
  protected SOAPFactory getSOAP11Factory()
  {
    return soap11Factory;
  }

  /**
   * Returns the SOAP 1.2 factory associated with the web service security handler.
   *
   * @return the SOAP 1.2 factory associated with the web service security handler
   */
  @SuppressWarnings("unused")
  protected SOAPFactory getSOAP12Factory()
  {
    return soap12Factory;
  }

  /**
   * Returns the version of the SOAP protocol for the specified SOAP message context.
   *
   * @param messageContext the SOAP message context to return the SOAP protocol version for
   *
   * @return the version of the SOAP protocol for the specified SOAP message context
   */
  @SuppressWarnings("unused")
  protected SOAPVersion getSOAPVersion(SOAPMessageContext messageContext)
  {
    String soapNS;

    try
    {
      SOAPEnvelope envelope = messageContext.getMessage().getSOAPPart().getEnvelope();

      soapNS = envelope.getElementQName().getNamespaceURI();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to determine the SOAP protocol version: "
          + e.getMessage(), e);
    }

    if (soapNS.equalsIgnoreCase(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE))
    {
      return SOAPVersion.SOAP_1_1;
    }
    else if (soapNS.equalsIgnoreCase(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE))
    {
      return SOAPVersion.SOAP_1_2;
    }
    else
    {
      throw new RuntimeException("Unable to determine the SOAP protocol version from the envelope"
          + " namespace (" + soapNS + ")");
    }
  }

  /**
   * Process the request sent to the web service.
   *
   * @param messageContext the message context
   *
   * @return <code>true</code> if handler processing should continue for the current message
   *         or <code>false</code> if processing should stop
   *
   * @throws WSSecurityException
   */
  private boolean doRequest(SOAPMessageContext messageContext)
    throws WSSecurityException
  {
    try
    {
      SOAPEnvelope envelope = messageContext.getMessage().getSOAPPart().getEnvelope();
      SOAPHeader header = envelope.addHeader();

      SOAPElement securityElement = header.addChildElement("Security", "wsse",
          "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

      SOAPElement usernameTokenElement = securityElement.addChildElement("UsernameToken", "wsse");

      usernameTokenElement.addAttribute(new QName("xmlns:wsu"), WSS_WS_SECURITY_UTILITY_NAMESPACE);

      SOAPElement usernameElement = usernameTokenElement.addChildElement("Username", "wsse");

      usernameElement.addTextNode(username);

      SOAPElement passwordElement = usernameTokenElement.addChildElement("Password", "wsse");

      passwordElement.setAttribute("Type", WSS_USERNAME_TOKEN_PROFILE_PASSWORD_TEXT_TYPE);
      passwordElement.addTextNode(password);

      return true;
    }
    catch (Throwable e)
    {
      throw new WebServiceSecurityHandlerException("Failed to add the WS-Security Username Token "
          + "authentication credentials to the " + "request being sent to the web service", e);
    }
  }

  /**
   * Returns the encoding for the SOAP message.
   *
   * @param message the message whose encoding should be returned
   *
   * @return the encoding for the SOAP message
   *
   * @throws SOAPException
   */
  private String getMessageEncoding(SOAPMessage message)
    throws SOAPException
  {
    String encoding = "utf-8";

    if (message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null)
    {
      encoding = message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();
    }

    return encoding;
  }
}
