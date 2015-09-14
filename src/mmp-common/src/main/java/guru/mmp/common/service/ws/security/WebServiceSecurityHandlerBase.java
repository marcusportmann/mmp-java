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

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.WSHandler;

import org.slf4j.Logger;

import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;

import java.security.KeyStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>WebServiceSecurityHandlerBase</code> class provides the base class for all web service
 * security handlers. It contains the functionality that is common to both the <i>web service</i>
 * and <i>web service client</i> handlers.
 *
 * @author Marcus Portmann
 */
public abstract class WebServiceSecurityHandlerBase extends WSHandler
  implements SOAPHandler<SOAPMessageContext>
{
  /**
   * The name of the parameter on the SOAP message context that will contain the crypto properties.
   */
  protected static final String MESSAGE_CONTEXT_CRYPTO_PROPERTIES = "CryptoProperties";

  /* The handler configuration options. */
  protected Map<String, Object> options;

  /**
   * The SOAP Protocol 1.1 factory.
   */
  private SOAPFactory soap11Factory = null;

  /**
   * The SOAP Protocol 1.2 factory.
   */
  private SOAPFactory soap12Factory = null;

  /**
   * The enumeration identifying the possible SOAP protocol versions.
   */
  enum SOAPVersion { SOAP_1_1, SOAP_1_2 }

  /**
   * Constructs a new <code>WebServiceSecurityHandlerBase</code>.
   *
   * @param isClient <code>true</code> if the derived class is a client-side JAX-WS handler
   *                 implementation or <code>false</code> if the derived class is a server-side
   *                 JAX-WS handler
   */
  public WebServiceSecurityHandlerBase(boolean isClient)
  {
    options = new HashMap<>();

    try
    {
      soap11Factory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
    }
    catch (SOAPException e)
    {
      if (isClient)
      {
        throw new WebServiceSecurityHandlerException(
            "Failed to initialise the web service client security handler: "
            + "The SOAP 1.1 factory could not be initialised", e);
      }
      else
      {
        throw new WebServiceSecurityHandlerException(
            "Failed to initialise the web service security handler: "
            + "The SOAP 1.1 factory could not be initialised", e);
      }
    }

    try
    {
      soap12Factory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
    }
    catch (SOAPException e)
    {
      if (isClient)
      {
        throw new WebServiceSecurityHandlerException(
            "Failed to initialise the web service client security handler: "
            + "The SOAP 1.2 factory could not be initialised", e);
      }
      else
      {
        throw new WebServiceSecurityHandlerException(
            "Failed to initialise the web service security handler: "
            + "The SOAP 1.2 factory could not be initialised", e);
      }
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
  @Override
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
    return options.get(key);
  }

  /**
   * Return the property identified by the specified key from the specified message context.
   *
   * @param messageContext the message context
   * @param key            the key identifying the property
   *
   * @return the property identified by the specified key
   *
   * @see org.apache.ws.security.handler.WSHandler#getProperty(Object, String)
   */
  @Override
  public Object getProperty(Object messageContext, String key)
  {
    return ((MessageContext) messageContext).get(key);
  }

  /**
   * Set the configuration option identified by the specified key.
   *
   * @param key    the key identifying the configuration option
   * @param option the configuration option
   */
  public void setOption(String key, Object option)
  {
    options.put(key, option);
  }

  /**
   * The  org.apache.ws.security.handler.WSHandler declares this as an abstract method but
   * it is not required as part of the custom implementation.
   *
   * @param msgContext the message context
   * @param password   the password
   *
   * @see org.apache.ws.security.handler.WSHandler#setPassword(Object, String)
   */
  @Override
  public void setPassword(Object msgContext, String password) {}

  /**
   * Abstract method required by org.apache.ws.security.handler.WSHandler.
   *
   * @param msgContext the message context
   * @param key        the property key
   * @param value      the property value
   *
   * @see org.apache.ws.security.handler.WSHandler#setProperty(Object, String, Object)
   */
  @Override
  public void setProperty(Object msgContext, String key, Object value)
  {
    ((MessageContext) msgContext).put(key, value);
  }

  /**
   * Utility method to convert a <code>SOAPMessage</code> to an <code>org.w3c.dom.Document</code>.
   *
   * @param message   the <code>SOAPMessage</code> instance to convert
   * @param isRequest <code>true</code> if the conversion is associated with a SOAP request or
   *                  <code>false</code> otherwise
   *
   * @return the <code>org.w3c.dom.Document</code> representation of the <code>SOAPMessage</code>
   *
   * @throws WSSecurityException
   */
  protected static Document messageToDocument(SOAPMessage message, boolean isRequest)
    throws WSSecurityException
  {
    try
    {
      Source content = message.getSOAPPart().getContent();

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

      documentBuilderFactory.setNamespaceAware(true);

      DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

      return builder.parse(org.apache.ws.security.util.XMLUtils.sourceToInputSource(content));
    }
    catch (Exception e)
    {
      if (isRequest)
      {
        throw new WSSecurityException("Failed to process the SOAP request:"
            + " Failed to convert the SOAPMessage into an org.w3c.dom.Document", e);
      }
      else
      {
        throw new WSSecurityException("Failed to process the SOAP response:"
            + " Failed to convert the SOAPMessage into an org.w3c.dom.Document", e);
      }
    }
  }

  /**
   * Create a SOAP fault from the specified WS-Security exception.
   *
   * @param messageContext the SOAP message context this SOAP fault is associated with
   * @param exception      the WS-Security exception information
   * @param isClient       <code>true</code> if this is a web service client handler or
   *                       <code>false</code> if this is a web service handler
   *
   * @return the SOAP fault
   */
  protected SOAPFault createSOAPFault(SOAPMessageContext messageContext, Throwable exception,
      boolean isClient)
  {
    return createSOAPFault(messageContext, exception, null, isClient);
  }

  /**
   * Create a SOAP fault from the specified WS-Security exception.
   *
   * @param messageContext the SOAP message context this SOAP fault is associated with
   * @param exception      the WS-Security exception information
   * @param isClient       <code>true</code> if this is a web service client handler or
   *                       <code>false</code> if this is a web service handler
   *
   * @return the SOAP fault
   */
  protected SOAPFault createSOAPFault(SOAPMessageContext messageContext,
      WSSecurityException exception, boolean isClient)
  {
    if (exception.getFaultCode() != null)
    {
      return createSOAPFault(messageContext, exception, exception.getFaultCode(), isClient);
    }
    else
    {
      return createSOAPFault(messageContext, exception, exception.getFaultCode(), isClient);
    }
  }

  /**
   * Dump the SOAP message to STDOUT.
   *
   * @param message the SOAP message to dump
   */
  @SuppressWarnings("unused")
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
   * Returns the key store containing the certificates for the web service or web service client.
   *
   * @return the key store containing the certificates for the web service or web service client
   */
  @SuppressWarnings("unused")
  protected abstract KeyStore getKeyStore();

  /**
   * Returns the logger associated with the handler.
   *
   * @return the logger associated with the handler
   */
  protected abstract Logger getLogger();

  /**
   * Returns the SOAP 1.1 factory associated with the web service security handler.
   *
   * @return the SOAP 1.1 factory associated with the web service security handler
   */
  protected SOAPFactory getSOAP11Factory()
  {
    return soap11Factory;
  }

  /**
   * Returns the SOAP 1.2 factory associated with the web service security handler.
   *
   * @return the SOAP 1.2 factory associated with the web service security handler
   */
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
   * Create a SOAP fault from the specified WS-Security exception.
   *
   * @param messageContext the SOAP message context this SOAP fault is associated with
   * @param exception      the WS-Security exception information
   * @param faultCode      the fault code to use for the fault or <code>null</code> if the generic
   *                       fault codes for the SOAP 1.1 or SOAP 1.2 protocols should be used
   * @param isClient       <code>true</code> if this is a web service client handler or
   *                       <code>false</code> if this is a web service handler
   *
   * @return the SOAP fault
   */
  private SOAPFault createSOAPFault(SOAPMessageContext messageContext, Throwable exception,
      QName faultCode, boolean isClient)
  {
    try
    {
      SOAPVersion soapVersion = getSOAPVersion(messageContext);
      String faultMessage = exception.getMessage();

      if (exception instanceof NullPointerException)
      {
        faultMessage = "NullPointerException";
      }

      if (soapVersion == SOAPVersion.SOAP_1_1)
      {
        if (faultCode != null)
        {
          return soap11Factory.createFault(faultMessage, faultCode);
        }
        else
        {
          if (isClient)
          {
            return soap11Factory.createFault(faultMessage,
                new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
          }
          else
          {
            return soap11Factory.createFault(faultMessage,
                new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
          }
        }
      }
      else if (soapVersion == SOAPVersion.SOAP_1_2)
      {
        if (faultCode != null)
        {
          return soap12Factory.createFault(faultMessage, faultCode);
        }
        else
        {
          if (isClient)
          {
            return soap12Factory.createFault(faultMessage,
                new QName("http://www.w3.org/2003/05/soap-envelope", "Sender"));
          }
          else
          {
            return soap12Factory.createFault(faultMessage,
                new QName("http://www.w3.org/2003/05/soap-envelope", "Receiver"));
          }
        }
      }
      else
      {
        throw new SOAPException("Unrecognised SOAP protocol version (" + soapVersion + ")");
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to create the SOAP fault", e);
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
