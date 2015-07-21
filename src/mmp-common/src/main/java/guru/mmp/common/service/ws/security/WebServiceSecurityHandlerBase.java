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
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.handler.WSHandler;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

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
  private static final char[] HEX_DIGITS =
  {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

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
   * The map of certificate DNs to certificate thumbprints for the certificates that have been
   * verified successfully as trusted certificates.
   */
  private Map<String, String> verifiedCertificates;

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
    verifiedCertificates = new HashMap<>();

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
  protected final SOAPFactory getSOAP11Factory()
  {
    return soap11Factory;
  }

  /**
   * Returns the SOAP 1.2 factory associated with the web service security handler.
   *
   * @return the SOAP 1.2 factory associated with the web service security handler
   */
  protected final SOAPFactory getSOAP12Factory()
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
   * Returns the thumbprint for the X509 certificate which is the base64 encoded SHA-1 hash of
   * the DER encoded certificate data.
   *
   * @param certificate the certificate to return the thumbprint for
   *
   * @return the thumbprint for the X509 certificate
   *
   * @throws NoSuchAlgorithmException
   * @throws CertificateEncodingException
   */
  protected String getThumbPrint(X509Certificate certificate)
    throws NoSuchAlgorithmException, CertificateEncodingException
  {
    MessageDigest md = MessageDigest.getInstance("SHA-1");

    md.update(certificate.getEncoded());

    return hexify(md.digest());
  }

  /**
   * Evaluate whether a given certificate should be trusted.
   * <p/>
   * Policy used in this implementation:
   * <ol>
   * <li>Search the key store for the transmitted certificate.</li>
   * <li>Search the keystore for a connection to the transmitted certificate i.e. search for
   * certificate(s) of the issuer of the transmitted certificate.</li>
   * <li>Verify the trust path for those certificates found because the search for the issuer
   * might be fooled by a phony DN.</li>
   * </ol>
   *
   * @param certificate the certificate that should be validated
   * @param requestData the request data for the SOAP message
   *
   * @return <code>true</code> if the certificate is trusted or <code>false</code> otherwise
   *
   * @throws WSSecurityException
   */
  protected boolean verifyTrust(X509Certificate certificate, RequestData requestData)
    throws WSSecurityException
  {
    // If no certificate was transmitted, do not trust the signature
    if (certificate == null)
    {
      return false;
    }

    if (getLogger().isDebugEnabled())
    {
      getLogger().debug("Verifying certificate with subject (" + certificate.getSubjectDN()
          + ") and serial number (" + certificate.getSerialNumber().toString() + ") issued by ("
          + certificate.getIssuerDN() + ")");
    }

    try
    {
      String verifiedThumbprint = verifiedCertificates.get(certificate.getSubjectDN().toString());

      if (verifiedThumbprint != null)
      {
        if (getThumbPrint(certificate).equals(verifiedThumbprint))
        {
          if (getLogger().isDebugEnabled())
          {
            getLogger().debug("Successfully verified the trust for the certificate with subject ("
                + certificate.getSubjectDN() + ") and thumbprint (" + verifiedThumbprint + ")");
          }

          return true;
        }
      }
    }
    catch (Throwable e)
    {
      getLogger().error("Failed to check previously verified certificate with subject ("
          + certificate.getSubjectDN() + ")", e);
    }

    /*
     * Search the key store for a certificate whose subject X500 principal matches the one
     * for the specified certificate or a certificate whose subject X500 principal matches
     * the one for the issuer of the specified certificate.
     */
    try
    {
      KeyStore keyStore = getKeyStore();

      Certificate keyStoreCertificate;

      for (Enumeration<String> e = keyStore.aliases(); e.hasMoreElements(); )
      {
        String alias = e.nextElement();
        Certificate[] certs = keyStore.getCertificateChain(alias);

        if ((certs == null) || (certs.length == 0))
        {
          // No certificate chain, so lets check if getCertificate gives us a result
          keyStoreCertificate = keyStore.getCertificate(alias);
        }
        else
        {
          keyStoreCertificate = certs[0];
        }

        if ((keyStoreCertificate != null) && (keyStoreCertificate instanceof X509Certificate))
        {
          X509Certificate x509Certificate = (X509Certificate) keyStoreCertificate;

          // Check whether the certificate being validated has been found i.e. direct trust
          if (x509Certificate.getSubjectX500Principal().equals(
              certificate.getSubjectX500Principal()))
          {
            /*
             * The certificates must be compared to protect against phony DNs i.e. Compare
             * encoded form including signature.
             */
            if (x509Certificate.equals(certificate))
            {
              return true;
            }
          }

          // X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
          // X500Principal issuerX500Principal = certificate.getIssuerX500Principal();

          // Check whether the issuer of the certificate being validated has been found
          if (x509Certificate.getSubjectX500Principal().equals(
              certificate.getIssuerX500Principal()))
          {
            if (certs == null)
            {
              certs = new Certificate[1];
              certs[0] = x509Certificate;
            }

            /*
             *  Form a certificate chain from the transmitted certificate and the certificate(s)
             *  of the issuer from the key store.
             */
            X509Certificate[] x509certs = new X509Certificate[certs.length + 1];

            // Add the certificate being verified
            x509certs[0] = certificate;

            // Add the rest of certificates in the chain
            for (int i = 0; i < certs.length; i++)
            {
              x509certs[i + 1] = (X509Certificate) certs[i];
            }

            /*
             * Use the validation method from the crypto implementation to check whether the
             * subjects' certificate was really signed by the issuer stated in the certificate.
             */
            if (requestData.getSigCrypto().verifyTrust(x509certs, false))
            {
              if (getLogger().isDebugEnabled())
              {
                getLogger().debug("The certificate path has been verified for certificate with"
                    + " subject (" + certificate.getSubjectDN() + ")");
              }

              verifiedCertificates.put(certificate.getSubjectDN().toString(),
                  getThumbPrint(certificate));

              return true;
            }
          }
        }
      }
    }
    catch (WSSecurityException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new WSSecurityException("Failed to verify the trust for the certificate ("
          + certificate.getSubjectX500Principal().toString() + "): " + e.getMessage(), e);
    }

    getLogger().warn("The trust for the certificate (" + certificate.getSubjectDN()
        + ") was not verified successfully");

    return false;
  }

  /**
   * Returns the hex value of the binary data.
   *
   * @param bytes the binary data to convert to hex
   *
   * @return the hex value of the binary data
   */
  private static String hexify(byte bytes[])
  {
    StringBuilder buf = new StringBuilder(bytes.length * 2);

    for (byte aByte : bytes)
    {
      buf.append(HEX_DIGITS[(aByte & 0xf0) >> 4]);
      buf.append(HEX_DIGITS[aByte & 0x0f]);
    }

    return buf.toString();
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
