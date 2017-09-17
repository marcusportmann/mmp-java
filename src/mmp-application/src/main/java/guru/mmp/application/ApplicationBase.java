/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.WebApplicationException;
import guru.mmp.common.crypto.CryptoUtils;
import guru.mmp.common.json.*;
import guru.mmp.common.util.StringUtil;
import io.undertow.Undertow;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.SSLSessionInfo;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.xnio.Options;
import org.xnio.SslClientAuthMode;

import javax.inject.Inject;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationBase</code> class provides the base class that application classes can be
 * derived from.
 *
 * @author Marcus Portmann
 */
@ComponentScan(basePackages = { "guru.mmp.application" }, lazyInit = true)
@EnableConfigurationProperties(ApplicationConfiguration.class)
@SpringBootApplication
@SuppressWarnings("unused")
public abstract class ApplicationBase
  implements ServletContextInitializer

{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  /**
   * The mutual SSL HTTP listener port.
   */
  private static int MUTUAL_SSL_HTTP_LISTENER_PORT = 8443;

  /**
   * The paths for the unsecured resources.
   */
  private List<String> unsecuredResources = new ArrayList<>();

  /**
   * The Spring application context.
   */
  @Inject
  private ApplicationContext applicationContext;

  /**
   * The Spring application configuration retrieved from the <b>classpath:application.yml</b> file.
   */
  @Inject
  private ApplicationConfiguration configuration;

  /**
   * Returns the embedded servlet container factory used to configure the embedded Undertow servlet
   * container.
   *
   * @return the embedded servlet container factory used to configure the embedded Undertow servlet
   *         container
   */
  @Bean
  public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory()
  {
    UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();

    factory.addDeploymentInfoCustomizers(
        (UndertowDeploymentInfoCustomizer) deploymentInfo -> deploymentInfo.addInitialHandlerChainWrapper(
        new HandlerWrapper()
        {
          @Override
          public HttpHandler wrap(HttpHandler wrappedHttpHandler)
          {
            return new HttpHandler()
            {
              @Override
              public void handleRequest(HttpServerExchange httpServerExchange)
                  throws Exception
              {
                if (configuration.isMutualSSLEnabled())
                {
                  String requestPath = httpServerExchange.getRequestPath();

                  // Check if this is an unsecured resource
                  for (String unsecuredResource : unsecuredResources)
                  {
                    if (requestPath.startsWith(unsecuredResource))
                    {
                      wrappedHttpHandler.handleRequest(httpServerExchange);

                      return;
                    }
                  }

                  SSLSessionInfo sslSessionInfo = httpServerExchange.getConnection()
                      .getSslSessionInfo();

                  if (sslSessionInfo == null)
                  {
                    logger.warn("The remote client (" + httpServerExchange.getSourceAddress()
                        + ") is attempting to access the secure resource (" + requestPath
                        + ") insecurely");

                    httpServerExchange.setStatusCode(403);
                    httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    httpServerExchange.getResponseSender().send("Access Denied");
                  }
                  else
                  {
                    if (logger.isDebugEnabled())
                    {
                      javax.security.cert.X509Certificate[] certificates =
                          sslSessionInfo.getPeerCertificateChain();

                      if ((certificates != null) && (certificates.length > 0))
                      {
                        if (logger.isDebugEnabled())
                        {
                          logger.debug("The remote client ("
                              + httpServerExchange.getSourceAddress() + ") with certificate ("
                              + certificates[0].getSubjectDN()
                              + ") is attempting to access the secure resource (" + requestPath
                              + ")");
                        }
                      }
                    }
                  }

                }

                wrappedHttpHandler.handleRequest(httpServerExchange);
              }
            };
          }
        }));

    if ((configuration.getMutualSSL() != null) && (configuration.getMutualSSL().getEnabled()))
    {
      ApplicationConfiguration.KeyStoreConfiguration keyStoreConfiguration =
          configuration.getMutualSSL().getKeyStore();

      ApplicationConfiguration.TrustStoreConfiguration trustStoreConfiguration =
          configuration.getMutualSSL().getTrustStore();

      if (keyStoreConfiguration == null)
      {
        logger.error("Failed to initialise the mutual SSL HTTP listener on port "
            + MUTUAL_SSL_HTTP_LISTENER_PORT + ": No key store configuration specified");
      }
      else if (trustStoreConfiguration == null)
      {
        logger.error("Failed to initialise the mutual SSL HTTP listener on port "
            + MUTUAL_SSL_HTTP_LISTENER_PORT + ": No trust store configuration specified");
      }
      else
      {
        factory.addBuilderCustomizers(new UndertowBuilderCustomizer()
            {
              @Override
              public void customize(Undertow.Builder builder)
              {
                try
                {
                  if (StringUtil.isNullOrEmpty(keyStoreConfiguration.getType()))
                  {
                    throw new FatalBeanException(
                        "The type was not specified for the mutual SSL key store");
                  }

                  if (StringUtil.isNullOrEmpty(keyStoreConfiguration.getPath()))
                  {
                    throw new FatalBeanException(
                        "The path was not specified for the mutual SSL key store");
                  }

                  if (StringUtil.isNullOrEmpty(keyStoreConfiguration.getAlias()))
                  {
                    throw new FatalBeanException(
                        "The alias was not specified for the mutual SSL key store");
                  }

                  if (StringUtil.isNullOrEmpty(keyStoreConfiguration.getPassword()))
                  {
                    throw new FatalBeanException(
                        "The password was not specified for the mutual SSL key store");
                  }

                  KeyStore keyStore;

                  try
                  {
                    keyStore = CryptoUtils.loadKeyStore(keyStoreConfiguration.getPath(),
                        keyStoreConfiguration.getAlias(), keyStoreConfiguration.getPassword(),
                        keyStoreConfiguration.getType());
                  }
                  catch (Throwable e)
                  {
                    throw new GeneralSecurityException(
                        "Failed to initialise the mutual SSL key store", e);
                  }

                  KeyStore trustStore = keyStore;

                  if (trustStoreConfiguration != null)
                  {
                    if (StringUtil.isNullOrEmpty(trustStoreConfiguration.getType()))
                    {
                      throw new FatalBeanException(
                          "The type was not specified for the mutual SSL trust store");
                    }

                    if (StringUtil.isNullOrEmpty(trustStoreConfiguration.getPath()))
                    {
                      throw new FatalBeanException(
                          "The path was not specified for the mutual SSL trust store");
                    }

                    if (StringUtil.isNullOrEmpty(trustStoreConfiguration.getPassword()))
                    {
                      throw new FatalBeanException(
                          "The password was not specified for the mutual SSL trust store");
                    }

                    try
                    {
                      trustStore = CryptoUtils.loadTrustStore(trustStoreConfiguration.getPath(),
                          trustStoreConfiguration.getPassword(), trustStoreConfiguration.getType());
                    }
                    catch (Throwable e)
                    {
                      throw new GeneralSecurityException(
                          "Failed to initialise the mutual SSL key store", e);
                    }
                  }

                  // Setup the key manager factory
                  KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                      KeyManagerFactory.getDefaultAlgorithm());

                  keyManagerFactory.init(keyStore, keyStoreConfiguration.getPassword()
                      .toCharArray());

                  // Setup the trust manager factory
                  TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                      TrustManagerFactory.getDefaultAlgorithm());

                  trustManagerFactory.init(trustStore);

                  SSLContext sslContext = SSLContext.getInstance("TLS");
                  sslContext.init(keyManagerFactory.getKeyManagers(),
                      trustManagerFactory.getTrustManagers(), new SecureRandom());

                  builder.addHttpsListener(8443, "0.0.0.0", sslContext);
                  builder.setSocketOption(Options.SSL_CLIENT_AUTH_MODE, SslClientAuthMode.REQUIRED);
                }
                catch (Throwable e)
                {
                  logger.error("Failed to initialise the mutual SSL HTTP listener on port "
                      + MUTUAL_SSL_HTTP_LISTENER_PORT, e);
                }
              }
            });
      }
    }

    factory.addInitializers();

    return factory;
  }

  /**
   * Returns the <code>Jackson2ObjectMapperBuilder</code> bean, which configures the Jackson JSON
   * processor package.
   *
   * @return the <code>Jackson2ObjectMapperBuilder</code> bean, which configures the Jackson JSON
   *         processor package
   */
  @Bean
  public Jackson2ObjectMapperBuilder jacksonBuilder()
  {
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
    jackson2ObjectMapperBuilder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    jackson2ObjectMapperBuilder.serializerByType(LocalDate.class, new LocalDateSerializer());
    jackson2ObjectMapperBuilder.deserializerByType(LocalDate.class, new LocalDateDeserializer());
    jackson2ObjectMapperBuilder.serializerByType(LocalTime.class, new LocalTimeSerializer());
    jackson2ObjectMapperBuilder.deserializerByType(LocalTime.class, new LocalTimeDeserializer());
    jackson2ObjectMapperBuilder.serializerByType(LocalDateTime.class,
        new LocalDateTimeSerializer());
    jackson2ObjectMapperBuilder.deserializerByType(LocalDateTime.class,
        new LocalDateTimeDeserializer());
    jackson2ObjectMapperBuilder.serializerByType(ZonedDateTime.class,
        new ZonedDateTimeSerializer());
    jackson2ObjectMapperBuilder.deserializerByType(ZonedDateTime.class,
        new ZonedDateTimeDeserializer());

    return jackson2ObjectMapperBuilder;
  }

  /**
   * Configure the given {@link ServletContext} with any servlets, filters, listeners,
   * context-params and attributes necessary for initialization.
   *
   * @param servletContext the {@code ServletContext} to initialize
   */
  @Override
  public void onStartup(ServletContext servletContext)
    throws ServletException
  {
    try
    {
      Class<? extends Servlet> dispatcherServletClass = Thread.currentThread()
          .getContextClassLoader().loadClass("org.springframework.web.servlet.DispatcherServlet")
          .asSubclass(Servlet.class);

      ServletRegistration dispatcherServlet = servletContext.addServlet("DispatcherServlet",
          (dispatcherServletClass));
      dispatcherServlet.addMapping("/*");

      dispatcherServlet.setInitParameter("contextClass",
          "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");

      logger.info("Initialising the Spring Dispatcher servlet");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class<? extends Servlet> cxfServletClass = Thread.currentThread().getContextClassLoader()
          .loadClass("org.apache.cxf.transport.servlet.CXFServlet").asSubclass(Servlet.class);

      ServletRegistration cxfServlet = servletContext.addServlet("CXFServlet", (cxfServletClass));
      cxfServlet.addMapping("/service/*");

      logger.info("Initialising the Apache CXF framework");
    }
    catch (ClassNotFoundException ignored) {}
  }

  /**
   * Returns the web service bean factory post processor.
   *
   * @return web service bean factory post processor
   */
  @Bean
  protected static BeanFactoryPostProcessor webServiceBeanFactoryPostProcessor()
  {
    return new BeanFactoryPostProcessor()
    {
      @Override
      public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException
      {
        try
        {
          Class<?> springBusClass = Thread.currentThread().getContextClassLoader().loadClass(
              "org.apache.cxf.bus.spring.SpringBus");

          Object springBus = springBusClass.newInstance();

          beanFactory.registerSingleton("cxf", springBus);
        }
        catch (ClassNotFoundException ignored) {}
        catch (Throwable e)
        {
          throw new FatalBeanException(
              "Failed to initialise the org.apache.cxf.bus.spring.SpringBus bean", e);
        }

      }
    };
  }

  /**
   * Create the web service endpoint.
   * <p/>
   * Requires the Apache CXF framework to have been initialised by adding the
   * <b>org.apache.cxf:cxf-rt-frontend-jaxws</b> and <b>org.apache.cxf:cxf-rt-transports-http</b>
   * Maven dependencies to the project.
   *
   * @param name           the web service name
   * @param implementation the web service implementation
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation)
  {
    return createWebServiceEndpoint(name, implementation, null, true);
  }

  /**
   * Create the web service endpoint.
   * <p/>
   * Requires the Apache CXF framework to have been initialised by adding the
   * <b>org.apache.cxf:cxf-rt-frontend-jaxws</b> and <b>org.apache.cxf:cxf-rt-transports-http</b>
   * Maven dependencies to the project.
   *
   * @param name           the web service name
   * @param implementation the web service implementation
   * @param isSecured      <code>true</code> if the web service must be secured using mutual SSL or
   *                       <code>false</code> if the web service can be invoked insecurely
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation, boolean isSecured)
  {
    return createWebServiceEndpoint(name, implementation, null, isSecured);
  }

  /**
   * Create the web service endpoint.
   * <p/>
   * Requires the Apache CXF framework to have been initialised by adding the
   * <b>org.apache.cxf:cxf-rt-frontend-jaxws</b> and <b>org.apache.cxf:cxf-rt-transports-http</b>
   * Maven dependencies to the project.
   *
   * @param name           the web service name
   * @param implementation the web service implementation
   * @param handlers       the JAX-WS web service handlers for the web service
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation,
      List<Handler> handlers)
  {
    return createWebServiceEndpoint(name, implementation, handlers, true);
  }

  /**
   * Create the web service endpoint.
   * <p/>
   * Requires the Apache CXF framework to have been initialised by adding the
   * <b>org.apache.cxf:cxf-rt-frontend-jaxws</b> and <b>org.apache.cxf:cxf-rt-transports-http</b>
   * Maven dependencies to the project.
   *
   * @param name           the web service name
   * @param implementation the web service implementation
   * @param handlers       the JAX-WS web service handlers for the web service
   * @param isSecured      <code>true</code> if the web service must be secured using mutual SSL or
   *                       <code>false</code> if the web service can be invoked insecurely
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation,
      List<Handler> handlers, boolean isSecured)
  {
    try
    {
      Class<? extends Endpoint> endpointImplClass = Thread.currentThread().getContextClassLoader()
          .loadClass("org.apache.cxf.jaxws.EndpointImpl").asSubclass(Endpoint.class);

      Class<?> busClass = Thread.currentThread().getContextClassLoader().loadClass(
          "org.apache.cxf.Bus");

      Class<?> springBusClass = Thread.currentThread().getContextClassLoader().loadClass(
          "org.apache.cxf.bus.spring.SpringBus");

      Object springBus = applicationContext.getBean(springBusClass);

      Constructor<? extends Endpoint> constructor = endpointImplClass.getConstructor(busClass,
          Object.class);

      Endpoint endpoint = constructor.newInstance(springBus, implementation);

      Method publishMethod = endpointImplClass.getMethod("publish", String.class);

      publishMethod.invoke(endpoint, "/" + name);

      if (handlers != null)
      {
        Method setHandlersMethod = endpointImplClass.getMethod("setHandlers", List.class);

        setHandlersMethod.invoke(endpoint, handlers);
      }

      applicationContext.getAutowireCapableBeanFactory().autowireBean(implementation);

      if (!isSecured)
      {
        unsecuredResources.add("/service/" + name);
      }

      return endpoint;
    }
    catch (ClassNotFoundException e)
    {
      throw new WebApplicationException("Failed to create the endpoint for the service (" + name
          + "): The Apache CXF framework has not been initialised", e);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to create the endpoint for the service (" + name
          + ")", e);
    }
  }
}
