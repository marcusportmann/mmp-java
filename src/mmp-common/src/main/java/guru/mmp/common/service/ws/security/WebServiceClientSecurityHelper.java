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

import guru.mmp.common.security.context.ApplicationSecurityContext;
import guru.mmp.common.util.ClientSSLSocketFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The <code>WebServiceClientSecurityHelper</code> class is a utility class
 * that provides support for configuring secure web service clients.
 */
public class WebServiceClientSecurityHelper
{
  /**
   * The name of the internal JAX-WS property that allows the SSL socket factory to be configured.
   */
  public static final String JAX_WS_INTERNAL_PROPERTIES_SSL_SOCKET_FACTORY =
    "com.sun.xml" + ".internal.ws.transport.https.client.SSLSocketFactory";

  /**
   * The name of the JAX-WS property that allows the SSL socket factory to be configured.
   */
  public static final String JAX_WS_PROPERTIES_SSL_SOCKET_FACTORY =
    "com.sun.xml.ws.transport" + ".https.client.SSLSocketFactory";

  private static Method apacheCxfClientGetConduitMethod;

  private static Class apacheCxfClientProxyClass;

  private static Method apacheCxfClientProxyGetClientMethod;

  private static Method apacheCxfHttpConduitSetTlsClientParametersMethod;

  private static Class apacheCxfTlsClientParametersClass;

  private static Method apacheCxfTlsParametersBaseSetKeyManagersMethod;

  private static Method apacheCxfTlsParametersBaseSetTrustManagersMethod;

  /**
   * The socket factory used to connect to a web service securely using Client SSL
   * authentication that has been initialised using the <code>ApplicationSecurityContext</code>.
   */
  private static ClientSSLSocketFactory clientSSLSocketFactory;

  /* The web service client cache. */
  private static ConcurrentMap<String, WebServiceClient> webServiceClientCache = new
    ConcurrentHashMap<>();

  /**
   * Returns the secure web service proxy for the web service that has been secured with
   * transport level security using SSL client authentication.
   *
   * @param serviceClass     the Java web service client class
   * @param serviceInterface the Java interface for the web service
   * @param wsdlResourcePath the resource path to the WSDL for the web service on the classpath
   * @param serviceEndpoint  the URL giving the web service endpoint
   * @param <T>              the Java interface for the web service
   *
   * @return the secure web service proxy for the web service that has been secured with
   * transport level security using SSL client authentication
   *
   * @throws WebServiceClientSecurityException
   */
  @SuppressWarnings("unchecked")
  public static <T> T getClientSSLServiceProxy(
    Class<?> serviceClass, Class<T> serviceInterface, String wsdlResourcePath,
    String serviceEndpoint)
    throws WebServiceClientSecurityException
  {
    try
    {
      // First attempt to retrieve the web service client from the cache
      WebServiceClient webServiceClient = webServiceClientCache.get(
        serviceClass.getName() + "-" + WebServiceSecurityType.CLIENT_SSL.getCode());

      if (webServiceClient == null)
      {
        webServiceClient = getWebServiceClient(serviceClass, wsdlResourcePath);

        webServiceClientCache.put(
          serviceClass.getName() + "-" + WebServiceSecurityType.CLIENT_SSL.getCode(),
          webServiceClient);
      }

      T proxy = webServiceClient.getService().getPort(webServiceClient.getPortQName(),
        serviceInterface);

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) proxy);

      // Set the SSL socket factory for the Client SSL secured invocation of the Retailer service
      Map<String, Object> requestContext = bindingProvider.getRequestContext();

      // Retrieve the Client SSL socket factory

      /*
       * NOTE: We use two different properties here because different JDKs seem to use the
       *       different property values.
       */
      requestContext.put(JAX_WS_PROPERTIES_SSL_SOCKET_FACTORY, getClientSSLSocketFactory());
      requestContext.put(JAX_WS_INTERNAL_PROPERTIES_SSL_SOCKET_FACTORY,
        getClientSSLSocketFactory());

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        serviceEndpoint);

      // Check if we are running under JBoss
      if (System.getProperty("jboss.home.dir") != null)
      {
        try
        {
          /*
           * Initialise all of the classes and methods required to invoke the Apache CXF APIs
           * using reflection.
           */
          if (apacheCxfClientProxyClass == null)
          {
            apacheCxfClientProxyClass = Thread.currentThread().getContextClassLoader().loadClass(
              "org.apache.cxf.frontend.ClientProxy");

            Class apacheCxfClientClass = Thread.currentThread().getContextClassLoader().loadClass(
              "org.apache.cxf.endpoint.Client");

            Class apacheCxfTlsParametersBaseClass = Thread.currentThread().getContextClassLoader
              ().loadClass(
              "org.apache.cxf.configuration.jsse.TLSParameterBase");

            apacheCxfTlsClientParametersClass = Thread.currentThread().getContextClassLoader()
              .loadClass(
              "org.apache.cxf.configuration.jsse.TLSClientParameters");

            Class apacheCxfHttpConduitClass = Thread.currentThread().getContextClassLoader()
              .loadClass(
              "org.apache.cxf.transport.http.HTTPConduit");

            apacheCxfClientProxyGetClientMethod = apacheCxfClientProxyClass.getMethod("getClient",
              java.lang.Object.class);

            if (apacheCxfClientProxyGetClientMethod == null)
            {
              throw new RuntimeException(
                "Failed to retrieve the getClient method on the" + " org.apache.cxf.frontend" +
                  ".ClientProxy class");
            }

            apacheCxfClientGetConduitMethod = apacheCxfClientClass.getMethod("getConduit");

            if (apacheCxfClientGetConduitMethod == null)
            {
              throw new RuntimeException(
                "Failed to retrieve the getConduit method on the" + " org.apache.cxf.endpoint" +
                  ".Client class");
            }

            apacheCxfTlsParametersBaseSetKeyManagersMethod = apacheCxfTlsParametersBaseClass
              .getMethod(
              "setKeyManagers", javax.net.ssl.KeyManager[].class);

            if (apacheCxfTlsParametersBaseSetKeyManagersMethod == null)
            {
              throw new RuntimeException(
                "Failed to retrieve the setKeyManagers method on the" + " org.apache.cxf" +
                  ".configuration.jsse.TLSParameterBase class");
            }

            apacheCxfTlsParametersBaseSetTrustManagersMethod = apacheCxfTlsParametersBaseClass
              .getMethod(
              "setTrustManagers", TrustManager[].class);

            if (apacheCxfTlsParametersBaseSetTrustManagersMethod == null)
            {
              throw new RuntimeException(
                "Failed to retrieve the setTrustManagers method on the" + " org.apache.cxf" +
                  ".configuration.jsse.TLSParameterBase class");
            }

            apacheCxfHttpConduitSetTlsClientParametersMethod = apacheCxfHttpConduitClass.getMethod(
              "setTlsClientParameters", apacheCxfTlsClientParametersClass);

            if (apacheCxfHttpConduitSetTlsClientParametersMethod == null)
            {
              throw new RuntimeException(
                "Failed to retrieve the setTlsClientParameters method on the" + " org.apache" +
                  ".cxf.transport.http.HTTPConduit class");
            }
          }

          // Invoke the getClient method on the org.apache.cxf.frontend.ClientProxy class
          Object clientObject = apacheCxfClientProxyGetClientMethod.invoke(null, proxy);

          // Invoke the getClient method on the org.apache.cxf.endpoint.Client class
          Object httpConduitObject = apacheCxfClientGetConduitMethod.invoke(clientObject);

          // Create a new org.apache.cxf.configuration.jsse.TLSClientParameters instance
          Object tlsClientParametersObject = apacheCxfTlsClientParametersClass.newInstance();

          // Setup the key manager for the client SSL socket factory
          KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm());

          keyManagerFactory.init(ApplicationSecurityContext.getContext().getKeyStore(),
            ApplicationSecurityContext.getContext().getKeyStorePassword().toCharArray());

          /*
           * Invoke the setKeyManagers method on the
           * org.apache.cxf.configuration.jsse.TLSParameterBase class
           */
          apacheCxfTlsParametersBaseSetKeyManagersMethod.invoke(tlsClientParametersObject,
            new Object[]{keyManagerFactory.getKeyManagers()});

          // Create a trust manager that does not validate certificate chains
          TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
          {
            public void checkClientTrusted(X509Certificate[] chain, String authType)
              throws CertificateException
            {
              // Skip client verification step
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
              throws CertificateException
            {
              /*
               * TODO: Verify the server certificate using the keystore associated with the
               *       application security context.
               */
            }

            public X509Certificate[] getAcceptedIssuers()
            {
              return new X509Certificate[0];
            }
          }};

          /*
           * Invoke the setTrustManagers method on the
           * org.apache.cxf.configuration.jsse.TLSParameterBase class
           */
          apacheCxfTlsParametersBaseSetTrustManagersMethod.invoke(tlsClientParametersObject,
            new Object[]{trustAllCerts});

          // Invoke the setTlsClientParameters on the org.apache.cxf.transport.http.HTTPConduit
          apacheCxfHttpConduitSetTlsClientParametersMethod.invoke(httpConduitObject,
            tlsClientParametersObject);
        }
        catch (Throwable e)
        {
          throw new WebServiceClientSecurityException(
            "Failed to initialise the client SSL socket factory for the Apache CXF proxy", e);
        }
      }

      return proxy;
    }
    catch (Throwable e)
    {
      throw new WebServiceClientSecurityException(
        "Failed to retrieve the web service proxy for the web service (" +
          serviceClass.getName() + ") that" +
          " has been secured using SSL client authentication", e);
    }
  }

  /**
   * Returns the socket factory used to connect to a web service securely using Client SSL
   * authentication that has been initialised using the <code>ApplicationSecurityContext</code>.
   *
   * @return the the socket factory used to connect to a web service securely using Client SSL
   * authentication that has been initialised using the
   * <code>ApplicationSecurityContext</code>
   */
  private static SSLSocketFactory getClientSSLSocketFactory()
  {
    try
    {
      if (clientSSLSocketFactory == null)
      {
        if (!ApplicationSecurityContext.getContext().isInitialised())
        {
          throw new RuntimeException("The ApplicationSecurityContext is not initialised");
        }

        clientSSLSocketFactory = new ClientSSLSocketFactory(
          ApplicationSecurityContext.getContext().getKeyStore(),
          ApplicationSecurityContext.getContext().getKeyStorePassword(),
          ApplicationSecurityContext.getContext().getKeyStore(), false);
      }

      return clientSSLSocketFactory;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the ClientSSL socket factory", e);
    }
  }

  /**
   * Returns the secure web service proxy for the web service that has been secured with
   * digest authentication.
   *
   * @param serviceClass     the Java web service client class
   * @param serviceInterface the Java interface for the web service
   * @param wsdlResourcePath the resource path to the WSDL for the web service on the classpath
   * @param serviceEndpoint  the URL giving the web service endpoint
   * @param username         the username
   * @param password         the password
   * @param <T>              the Java interface for the web service
   *
   * @return the secure web service proxy for the web service that has been secured with
   * transport level security using SSL client authentication
   *
   * @throws WebServiceClientSecurityException
   */
  public static <T> T getDigestAuthenticationServiceProxy(
    Class<?> serviceClass, Class<T> serviceInterface, String wsdlResourcePath,
    String serviceEndpoint, String username, String password)
    throws WebServiceClientSecurityException
  {
    try
    {
      // First attempt to retrieve the web service client from the cache
      WebServiceClient webServiceClient = webServiceClientCache.get(
        serviceClass.getName() + "-" + WebServiceSecurityType.DIGEST_AUTHENTICATION.getCode());

      if (webServiceClient == null)
      {
        webServiceClient = getWebServiceClient(serviceClass, wsdlResourcePath);

        webServiceClientCache.put(
          serviceClass.getName() + "-" + WebServiceSecurityType.DIGEST_AUTHENTICATION.getCode(),
          webServiceClient);
      }

      T proxy = webServiceClient.getService().getPort(webServiceClient.getPortQName(),
        serviceInterface);

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) proxy);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        serviceEndpoint);

      CXFDigestSecurityProxyConfigurator.configureProxy(proxy, username, password);

      return proxy;
    }
    catch (Throwable e)
    {
      throw new WebServiceClientSecurityException(
        "Failed to retrieve the web service proxy for the web service that" + " has been " +
          "secured using digest authentication", e);
    }
  }

  /**
   * Returns the secure web service proxy for the web service that has been secured with
   * simple HTTP authentication.
   *
   * @param serviceClass     the Java web service client class
   * @param serviceInterface the Java interface for the web service
   * @param wsdlResourcePath the resource path to the WSDL for the web service on the classpath
   * @param serviceEndpoint  the URL giving the web service endpoint
   * @param username         the username
   * @param password         the password
   * @param <T>              the Java interface for the web service
   *
   * @return the secure web service proxy for the web service that has been secured with
   * simple HTTP authentication
   *
   * @throws WebServiceClientSecurityException
   */
  public static <T> T getHTTPAuthenticationServiceProxy(
    Class<?> serviceClass, Class<T> serviceInterface, String wsdlResourcePath,
    String serviceEndpoint, String username, String password)
    throws WebServiceClientSecurityException
  {
    try
    {
      // First attempt to retrieve the web service client from the cache
      WebServiceClient webServiceClient = webServiceClientCache.get(serviceClass.getName());

      if (webServiceClient == null)
      {
        webServiceClient = getWebServiceClient(serviceClass, wsdlResourcePath);

        webServiceClientCache.put(serviceClass.getName(), webServiceClient);
      }

      T proxy = webServiceClient.getService().getPort(webServiceClient.getPortQName(),
        serviceInterface);

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) proxy);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        serviceEndpoint);

      bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
      bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

      return proxy;
    }
    catch (Throwable e)
    {
      throw new WebServiceClientSecurityException(
        "Failed to retrieve the web service proxy for the web service (" +
          serviceClass.getName() + ") that" +
          " has been secured using simple HTTP authentication", e);
    }
  }

  /**
   * Returns the web service proxy for the unsecured web service.
   *
   * @param serviceClass     the Java web service client class
   * @param serviceInterface the Java interface for the web service
   * @param wsdlResourcePath the resource path to the WSDL for the web service on the classpath
   * @param serviceEndpoint  the URL giving the web service endpoint
   * @param <T>              the Java interface for the web service
   *
   * @return the web service proxy for the unsecured web service
   *
   * @throws WebServiceClientSecurityException
   */
  public static <T> T getServiceProxy(
    Class<?> serviceClass, Class<T> serviceInterface, String wsdlResourcePath,
    String serviceEndpoint)
    throws WebServiceClientSecurityException
  {
    try
    {
      // First attempt to retrieve the web service client from the cache
      WebServiceClient webServiceClient = webServiceClientCache.get(serviceClass.getName());

      if (webServiceClient == null)
      {
        webServiceClient = getWebServiceClient(serviceClass, wsdlResourcePath);

        webServiceClientCache.put(serviceClass.getName(), webServiceClient);
      }

      T proxy = webServiceClient.getService().getPort(webServiceClient.getPortQName(),
        serviceInterface);

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) proxy);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        serviceEndpoint);

      return proxy;
    }
    catch (Throwable e)
    {
      throw new WebServiceClientSecurityException(
        "Failed to retrieve the web service proxy for the unsecured web service (" +
          serviceClass.getName() + ") ", e);
    }
  }

  /**
   * Returns the secure web service proxy for the web service that has been secured with
   * message level security using WS-Security username token authentication.
   *
   * @param serviceClass     the Java web service client class
   * @param serviceInterface the Java interface for the web service
   * @param wsdlResourcePath the resource path to the WSDL for the web service on the classpath
   * @param serviceEndpoint  the URL giving the web service endpoint
   * @param username         the username
   * @param password         the password
   * @param <T>              the Java interface for the web service
   *
   * @return the secure web service proxy for the web service that has been secured with
   * message level security using WS-Security username token authentication
   *
   * @throws WebServiceClientSecurityException
   */
  public static <T> T getWSSecurityUsernameTokenServiceProxy(
    Class<?> serviceClass, Class<T> serviceInterface, String wsdlResourcePath,
    String serviceEndpoint, String username, String password)
    throws WebServiceClientSecurityException
  {
    try
    {
      // First attempt to retrieve the web service client from the cache
      WebServiceClient webServiceClient = webServiceClientCache.get(serviceClass.getName() + "-" +
        WebServiceSecurityType.WS_SECURITY_USERNAME_TOKEN.getCode());

      if (webServiceClient == null)
      {
        webServiceClient = getWebServiceClient(serviceClass, wsdlResourcePath);

        webServiceClient.getService().setHandlerResolver(
          new WSSUsernameTokenSecurityHandlerResolver(username, password));

        webServiceClientCache.put(serviceClass.getName() + "-" +
          WebServiceSecurityType.WS_SECURITY_USERNAME_TOKEN.getCode(), webServiceClient);
      }

      T proxy = webServiceClient.getService().getPort(webServiceClient.getPortQName(),
        serviceInterface);

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) proxy);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        serviceEndpoint);

      return proxy;
    }
    catch (Throwable e)
    {
      throw new WebServiceClientSecurityException(
        "Failed to retrieve the web service proxy for the web service (" +
          serviceClass.getName() + ") that" +
          " has been secured using WS-Security username token authentication", e);
    }
  }

  /**
   * Returns the secure web service proxy for the web service that has been secured with
   * message level security using WS-Security X509 certificate-based authentication.
   *
   * @param serviceClass     the Java web service client class
   * @param serviceInterface the Java interface for the web service
   * @param wsdlResourcePath the resource path to the WSDL for the web service on the classpath
   * @param serviceEndpoint  the URL giving the web service endpoint
   * @param <T>              the Java interface for the web service
   *
   * @return the secure web service proxy for the web service that has been secured with
   * message level security using WS-Security X509 certificate-based authentication
   *
   * @throws WebServiceClientSecurityException
   */
  public static <T> T getWSSecurityX509CertificateServiceProxy(
    Class<?> serviceClass, Class<T> serviceInterface, String wsdlResourcePath,
    String serviceEndpoint)
    throws WebServiceClientSecurityException
  {
    try
    {
      // First attempt to retrieve the web service client from the cache
      WebServiceClient webServiceClient = webServiceClientCache.get(serviceClass.getName() + "-" +
        WebServiceSecurityType.WS_SECURITY_X509_CERTIFICATE.getCode());

      if (webServiceClient == null)
      {
        webServiceClient = getWebServiceClient(serviceClass, wsdlResourcePath);

        webServiceClient.getService().setHandlerResolver(
          new WebServiceClientSecurityHandlerResolver());

        webServiceClientCache.put(serviceClass.getName() + "-" +
          WebServiceSecurityType.WS_SECURITY_X509_CERTIFICATE.getCode(), webServiceClient);
      }

      T proxy = webServiceClient.getService().getPort(webServiceClient.getPortQName(),
        serviceInterface);

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) proxy);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        serviceEndpoint);

      return proxy;
    }
    catch (Throwable e)
    {
      throw new WebServiceClientSecurityException(
        "Failed to retrieve the web service proxy for the web service (" +
          serviceClass.getName() + ") that" +
          " has been secured using WS-Security X509 certificate authentication", e);
    }
  }

  /**
   * Returns the web service client.
   *
   * @param serviceClass     the Java web service client class
   * @param wsdlResourcePath the resource path to the WSDL for the web service on the classpath
   *
   * @return the web service client
   *
   * @throws WebServiceClientSecurityException
   */
  private static WebServiceClient getWebServiceClient(
    Class<?> serviceClass, String wsdlResourcePath)
    throws WebServiceClientSecurityException
  {
    try
    {
      if (!Service.class.isAssignableFrom(serviceClass))
      {
        throw new WebServiceClientSecurityException(
          "The web service client class (" + serviceClass.getName() + ") does not extend the " +
            "javax.xml.ws.Service class");
      }

      /*
       * Retrieve the @WebServiceClient annotation from the service class to determine the qname
       * for the web service.
       */
      javax.xml.ws.WebServiceClient webServiceClientAnnotation = serviceClass.getAnnotation(
        javax.xml.ws.WebServiceClient.class);

      if (webServiceClientAnnotation == null)
      {
        throw new WebServiceClientSecurityException(
          "Failed to retrieve the @WebServiceClient annotation from the" + " web service client" +
            " class (" + serviceClass.getName() + ")");
      }

      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

      // Retrieve the WSDL for the web service as a resource from the classpath
      URL wsdlLocation = contextClassLoader.getResource(wsdlResourcePath);

      if (wsdlLocation == null)
      {
        throw new WebServiceClientSecurityException(
          "Failed to retrieve the WSDL for the web service (" + wsdlResourcePath + ") as a " +
            "resource from the classpath");
      }

      // Create a new instance of the web service client
      Constructor<?> constructor = serviceClass.getConstructor(URL.class, QName.class);

      QName serviceQName = new QName(webServiceClientAnnotation.targetNamespace(),
        webServiceClientAnnotation.name());

      Object serviceObject = constructor.newInstance(wsdlLocation, serviceQName);

      Service service = (Service) serviceObject;

      /*
       * Find the method with no parameters annotated with @WebEndpoint.
       *
       * Then use the value of the "name" parameter on this annotation to determine the port name.
       */
      String portName = null;

      for (Method method : serviceClass.getMethods())
      {
        WebEndpoint webEndpointAnnotation = method.getAnnotation(WebEndpoint.class);

        if (webEndpointAnnotation != null)
        {
          portName = webEndpointAnnotation.name();

          break;
        }
      }

      if (portName == null)
      {
        throw new WebServiceClientSecurityException(
          "Failed to determine the port name using the" + " @WebEndpoint annotation on one of " +
            "the methods on the web service client class (" + serviceClass.getName() + ")");
      }

      QName portQName = new QName(webServiceClientAnnotation.targetNamespace(), portName);

      return new WebServiceClient(portQName, service);
    }
    catch (Throwable e)
    {
      throw new WebServiceClientSecurityException(
        "Failed to retrieve the web service client for the web service (" +
          serviceClass.getName() + ")", e);
    }
  }

  /**
   * The <code>WebServiceClient</code> class stores the information for a web service
   * client.
   *
   * @author Marcus Portmann
   */
  public static class WebServiceClient
  {
    /**
     * The QName for the port.
     */
    private QName portQName;

    /**
     * The web service client.
     */
    private Service service;

    /**
     * Constructs a new <code>CachedWebServiceClient</code>.
     *
     * @param portQName the QName for the port
     * @param service   the web service client
     */
    public WebServiceClient(QName portQName, Service service)
    {
      this.portQName = portQName;
      this.service = service;
    }

    /**
     * Returns the QName for the port.
     *
     * @return the QName for the port
     */
    public QName getPortQName()
    {
      return portQName;
    }

    /**
     * Returns the web service client.
     *
     * @return the web service client
     */
    public Service getService()
    {
      return service;
    }
  }
}
