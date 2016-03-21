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

package guru.mmp.vmware.service.ws;

//~--- non-JDK imports --------------------------------------------------------

import com.rsa.names._2009._12.product.riat.wsdl.STSService;
import com.rsa.names._2009._12.product.riat.wsdl.STSServicePortType;

import guru.mmp.common.service.ws.security.WebServiceClientHandlerResolver;
import guru.mmp.common.service.ws.security.WebServiceClientSecurityHelper;
import guru.mmp.common.util.NoTrustSSLSocketFactory;
import guru.mmp.vmware.service.ws.handler.*;

import org.oasis_open.docs.ws_sx.ws_trust._200512.LifetimeType;
import org.oasis_open.docs.ws_sx.ws_trust._200512.RenewingType;
import org.oasis_open.docs.ws_sx.ws_trust._200512.RequestSecurityTokenType;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.ObjectFactory;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.SecurityHeaderType;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0.AttributedDateTime;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vim25.ManagedObjectReference;
import vim25.ServiceContent;

import vim25service.VimPortType;
import vim25service.VimService;

//~--- JDK imports ------------------------------------------------------------

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLSocketFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>VMwareWebServiceUtil</code> class.
 *
 * @author Marcus Portmann
 */
public class VMwareWebServiceUtil
{
  static final ObjectFactory wsseObjFactory = new ObjectFactory();
  private static final String WSS_NS =
      "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
  private static final String SECURITY_ELEMENT_NAME = "Security";
  private static final String WS_1_3_TRUST_JAXB_PACKAGE =
      "org.oasis_open.docs.ws_sx.ws_trust._200512";
  private static final String WSSE_JAXB_PACKAGE =
      "org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0";
  private static final String WSSU_JAXB_PACKAGE =
      "org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0";
  private static final String URN_OASIS_NAMES_TC_SAML_2_0_ASSERTION =
      "urn:oasis:names:tc:SAML:2.0:assertion";

  /**
   * The path to the classpath resource containing the WSDL for the STS service.
   */
  public static final String STS_SERVICE_WSDL = "META-INF/wsdl/STSService.wsdl";
  private static ThreadLocal<Map<String, VimPortType>> sharedVimServicesThreadLocal =
      new ThreadLocal<Map<String, VimPortType>>()
  {
    @Override
    protected Map<String, VimPortType> initialValue()
    {
      return new ConcurrentHashMap<>();
    }
  };

  /**
   * The path to the classpath resource containing the WSDL for the VIM service.
   */
  public static final String VIM_SERVICE_WSDL = "META-INF/wsdl/vimService.wsdl";
  private static SSLSocketFactory sslSocketFactory;

  /**
   * Connects to the VMware Secure Token Service and authenticates the user, using the specified
   * username and password, in order to obtain a SAML token, which can be used to invoke the
   * VMware VIM service.
   *
   * @param stsServiceEndpoint the URL for the VMware Secure Token Service endpoint
   * @param username           the username
   * @param password           the password
   *
   * @return the SAML token
   *
   * @throws VMwareWebServiceException
   */
  public static Element getSAMLToken(String stsServiceEndpoint, String username, String password)
    throws VMwareWebServiceException
  {
    try
    {
      WebServiceClientHandlerResolver handlerResolver = new WebServiceClientHandlerResolver();

      handlerResolver.addHandler(new TimestampSecurityHandler());

      UserCredentialHandler userCredentialHandler = new UserCredentialHandler(username, password);
      handlerResolver.addHandler(userCredentialHandler);

      SAMLTokenExtractionHandler samlTokenExtractionHandler = new SAMLTokenExtractionHandler();
      handlerResolver.addHandler(samlTokenExtractionHandler);

      // NOTE: We need a connection without a session to retrieve the token
      STSServicePortType stsServicePort = WebServiceClientSecurityHelper.getServiceProxy(
          STSService.class, STSServicePortType.class, STS_SERVICE_WSDL, stsServiceEndpoint,
          handlerResolver);

      BindingProvider bindingProvider = ((BindingProvider) stsServicePort);

      Map<String, Object> requestContext = bindingProvider.getRequestContext();

      requestContext.put(WebServiceClientSecurityHelper.JAX_WS_PROPERTIES_SSL_SOCKET_FACTORY,
          getSSLSocketFactory());
      requestContext.put(WebServiceClientSecurityHelper
          .JAX_WS_INTERNAL_PROPERTIES_SSL_SOCKET_FACTORY, getSSLSocketFactory());
      requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, false);

      /*
       * Construct the SOAP body for the request. RequestSecurityTokenType is
       *  the parameter type that is passed to the "acquire" method. However,
       * based on what kind of token (bearer or holder-of-key type) and by
       * what means (aka username/password, certificate, or existing token) we
       * want to acquire the token, different elements need to be populated
       */
      RequestSecurityTokenType tokenType = new RequestSecurityTokenType();

      /*
       * For this request we need at least the following element in the
       * RequestSecurityTokenType set
       *
       * 1. Lifetime - represented by LifetimeType which specifies the
       * lifetime for the token to be issued
       *
       * 2. Tokentype - "urn:oasis:names:tc:SAML:2.0:assertion", which is the
       * class that models the requested token
       *
       * 3. RequestType -
       * "http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue", as we want
       * to get a token issued
       *
       * 4. KeyType -
       * "http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer",
       * representing the kind of key the token will have. There are two
       * options namely bearer and holder-of-key
       *
       * 5. SignatureAlgorithm -
       * "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", representing the
       * algorithm used for generating signature
       *
       * 6. Renewing - represented by the RenewingType which specifies whether
       * the token is renewable or not
       */
      LifetimeType lifetime = new LifetimeType();

      DatatypeFactory dtFactory = DatatypeFactory.newInstance();
      GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
      XMLGregorianCalendar xmlCalendar = dtFactory.newXMLGregorianCalendar(cal);
      AttributedDateTime created = new AttributedDateTime();
      created.setValue(xmlCalendar.toXMLFormat());

      AttributedDateTime expires = new AttributedDateTime();
      xmlCalendar.add(dtFactory.newDuration(30 * 60 * 1000));
      expires.setValue(xmlCalendar.toXMLFormat());

      lifetime.setCreated(created);
      lifetime.setExpires(expires);

      // String requestTypeNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";

      tokenType.setTokenType("urn:oasis:names:tc:SAML:2.0:assertion");

      // JAXBElement tokenTypeElement = new JAXBElement(new QName("", "TokenType"),
      // javax.xml.bind.JAXBElement.class, null, "urn:oasis:names:tc:SAML:2.0:assertion");

      tokenType.setRequestType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue");

      // JAXBElement requestTypeElement = new JAXBElement(new QName(requestTypeNamespace,
      // "RequestType"), javax.xml.bind.JAXBElement.class, null,
      // "http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue");

      tokenType.setKeyType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer");

      // JAXBElement keyTypeElement = new JAXBElement(new QName(requestTypeNamespace, "KeyType"),
      // javax.xml.bind.JAXBElement.class, null,
      // "http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer");

      tokenType.setSignatureAlgorithm("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");

      // JAXBElement signatureAlgorithmElement = new JAXBElement(new QName(requestTypeNamespace,
      // "SignatureAlgorithm"), javax.xml.bind.JAXBElement.class, null,
      // "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");

      tokenType.setLifetime(lifetime);

      tokenType.setDelegatable(true);

      RenewingType renewing = new RenewingType();
      renewing.setAllow(Boolean.FALSE);
      renewing.setOK(Boolean.FALSE);  // WS-Trust Profile: MUST be set to false

      tokenType.setRenewing(renewing);

      /*
       * Invoke the "issue" method on the VMware Secure Token Service to acquire the SAML token.
       */
      stsServicePort.issue(tokenType);

      return samlTokenExtractionHandler.getToken();
    }
    catch (Throwable e)
    {
      throw new VMwareWebServiceException("Failed to authenticate the user (" + username
          + ") and retrieve the SAML token from the VMware Secure Token Service", e);
    }
  }

  /**
   * Returns the SOAP header.
   * <p>
   * If a SOAP header is not found then one will be created and returned.
   *
   * @param messageContext the SOAP message context
   *
   * @return the SOAP header
   *
   * @throws SOAPException
   */
  public static SOAPHeader getSOAPHeader(SOAPMessageContext messageContext)
    throws SOAPException
  {
    return (messageContext.getMessage().getSOAPPart().getEnvelope().getHeader() == null)
        ? messageContext.getMessage().getSOAPPart().getEnvelope().addHeader()
        : messageContext.getMessage().getSOAPPart().getEnvelope().getHeader();
  }

  /**
   * Returns the Security element for the SOAP header.
   * <p>
   * If a Security element is not found then one will be created and returned.
   *
   * @param header the SOAP header
   *
   * @return the Security element for the SOAP header
   */
  public static Node getSecurityElement(SOAPHeader header)
  {
    NodeList targetElement = header.getElementsByTagNameNS(WSS_NS, SECURITY_ELEMENT_NAME);
    if ((targetElement == null) || (targetElement.getLength() == 0))
    {
      JAXBElement<SecurityHeaderType> value = wsseObjFactory.createSecurity(
          wsseObjFactory.createSecurityHeaderType());
      Node headerNode = marshalJaxbElement(value).getDocumentElement();

      return header.appendChild(header.getOwnerDocument().importNode(headerNode, true));
    }
    else if (targetElement.getLength() > 1)
    {
      throw new RuntimeException("Failed to retrieve the Security element from the SOAP header");
    }

    return targetElement.item(0);
  }

  /**
   * Retrieves the shared unauthenticated VMware VIM service.
   *
   * @param vimServiceEndpoint the URL for the VMware VIM service endpoint
   *
   * @return the VMware VIM service
   *
   * @throws VMwareWebServiceException
   */
  public static VimPortType getSharedVimPort(String vimServiceEndpoint)
    throws VMwareWebServiceException
  {
    if (sharedVimServicesThreadLocal.get().get(vimServiceEndpoint) == null)
    {
      try
      {
        VimPortType vimPort = WebServiceClientSecurityHelper.getServiceProxy(VimService.class,
            VimPortType.class, VIM_SERVICE_WSDL, vimServiceEndpoint);

        BindingProvider bindingProvider = ((BindingProvider) vimPort);

        Map<String, Object> requestContext = bindingProvider.getRequestContext();

        requestContext.put(WebServiceClientSecurityHelper.JAX_WS_PROPERTIES_SSL_SOCKET_FACTORY,
            getSSLSocketFactory());
        requestContext.put(WebServiceClientSecurityHelper
            .JAX_WS_INTERNAL_PROPERTIES_SSL_SOCKET_FACTORY, getSSLSocketFactory());
        requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, false);

        sharedVimServicesThreadLocal.get().put(vimServiceEndpoint, vimPort);
      }
      catch (Throwable e)
      {
        throw new VMwareWebServiceException("Failed to retrieve the VMware VIM Service", e);
      }
    }

    return sharedVimServicesThreadLocal.get().get(vimServiceEndpoint);
  }

  /**
   * Retrieves the authenticated VMware VIM service for the specified user.
   *
   * @param stsServiceEndpoint the URL for the VMware Secure Token Service endpoint
   * @param username           the username
   * @param password           the password
   * @param vimServiceEndpoint the URL for the VMware VIM service endpoint
   *
   * @return the VMware VIM service
   *
   * @throws VMwareWebServiceException
   */
  public static VimPortType getVimPort(String stsServiceEndpoint, String username, String password,
      String vimServiceEndpoint)
    throws VMwareWebServiceException
  {
    try
    {
      // Retrieve the Service Content using the shared unauthenticated VMware VIM service instance
      VimPortType sharedVimService = getSharedVimPort(vimServiceEndpoint);

      ManagedObjectReference serviceInstanceReference = new ManagedObjectReference();
      serviceInstanceReference.setType("ServiceInstance");
      serviceInstanceReference.setValue("ServiceInstance");

      ServiceContent serviceContent = sharedVimService.retrieveServiceContent(
          serviceInstanceReference);

      // Retrieve the SAML token for the user
      Element token = getSAMLToken(stsServiceEndpoint, username, password);

      // Retrieve the session-based VMware VIM service instance
      WebServiceClientHandlerResolver handlerResolver = new WebServiceClientHandlerResolver();

      handlerResolver.addHandler(new TimestampSecurityHandler());
      handlerResolver.addHandler(new SAMLTokenHandler(token));

      HeaderCookieExtractionHandler headerCookieExtractionHandler =
          new HeaderCookieExtractionHandler();

      handlerResolver.addHandler(headerCookieExtractionHandler);

      /**
       * NOTE: We do not use the web service client cache as we will be using JAX-WS handlers that
       *       are specific to the user connecting to the VMware VIM service.
       */
      VimPortType vimPort = WebServiceClientSecurityHelper.getServiceProxy(VimService.class,
          VimPortType.class, VIM_SERVICE_WSDL, vimServiceEndpoint, handlerResolver, false);

      BindingProvider bindingProvider = ((BindingProvider) vimPort);

      Map<String, Object> requestContext = bindingProvider.getRequestContext();

      requestContext.put(WebServiceClientSecurityHelper.JAX_WS_PROPERTIES_SSL_SOCKET_FACTORY,
          getSSLSocketFactory());
      requestContext.put(WebServiceClientSecurityHelper
          .JAX_WS_INTERNAL_PROPERTIES_SSL_SOCKET_FACTORY, getSSLSocketFactory());
      requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

      vimPort.loginByToken(serviceContent.getSessionManager(), null);

      handlerResolver.clearHandlers();
      handlerResolver.addHandler(new HeaderCookieHandler(
          headerCookieExtractionHandler.getCookies()));

      return vimPort;
    }
    catch (Throwable e)
    {
      throw new VMwareWebServiceException("Failed to retrieve the VMware VIM Service", e);
    }
  }

  /**
   * Returns <code>true</code> if the specified SOAP message context is associated with an outgoing
   * message or <code>false</code> otherwise.
   *
   * @param messageContext the SOAP message context
   *
   * @return <code>true</code> if the specified SOAP message context is associated with an outgoing
   *         message or <code>false</code> otherwise
   */
  public static boolean isOutgoingMessage(SOAPMessageContext messageContext)
  {
    return (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
  }

  /**
   * Check whether the specified token is a SAML token.
   *
   * @param token the token to check
   *
   * @return <code>true</code> if the token is a SAML token or <code>false</code> otherwise
   */
  public static boolean isSamlToken(Node token)
  {
    return (URN_OASIS_NAMES_TC_SAML_2_0_ASSERTION.equalsIgnoreCase(token.getNamespaceURI()))
        && ("assertion".equalsIgnoreCase(token.getLocalName()));
  }

  /**
   * Marshal a JAXB element into a Document
   *
   * @param jaxbElement the JAXB element to marshal
   * @param <T>         the type of the JAXB element to marshal
   *
   * @return the Document
   */
  public static <T> Document marshalJaxbElement(JAXBElement<T> jaxbElement)
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);

    Document result;
    try
    {
      JAXBContext jaxbContext = JAXBContext.newInstance(WS_1_3_TRUST_JAXB_PACKAGE + ":"
          + WSSE_JAXB_PACKAGE + ":" + WSSU_JAXB_PACKAGE);
      result = dbf.newDocumentBuilder().newDocument();
      jaxbContext.createMarshaller().marshal(jaxbElement, result);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to marshal the JAXB element", e);
    }

    return result;
  }

  private static synchronized SSLSocketFactory getSSLSocketFactory()
  {
    if (sslSocketFactory == null)
    {
      sslSocketFactory = new NoTrustSSLSocketFactory();
    }

    return sslSocketFactory;
  }
}
