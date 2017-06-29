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

package guru.mmp.application.web;

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.jdbc.AtomikosDataSourceBean;
import guru.mmp.application.ApplicationConfiguration;
import guru.mmp.application.Debug;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.util.StringUtil;
import io.undertow.Undertow;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.SSLSessionInfo;
import io.undertow.util.Headers;
import org.apache.wicket.*;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;
import org.xnio.Options;
import org.xnio.SslClientAuthMode;

import javax.inject.Inject;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebApplication</code> class provides a base class for all "application specific"
 * Wicket web application classes.
 *
 * @author Marcus Portmann
 */
@ComponentScan(basePackages = { "guru.mmp.application" }, lazyInit = true)
@EnableAsync
@EnableConfigurationProperties(ApplicationConfiguration.class)
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public abstract class WebApplication extends org.apache.wicket.protocol.http.WebApplication
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

  /**
   * The mutual SSL HTTP listener port.
   */
  private static int MUTUAL_SSL_HTTP_LISTENER_PORT = 8443;

  /**
   * The distributed in-memory caches.
   */
  Map<String, Map> caches = new ConcurrentHashMap<>();

  /**
   * The paths for the unsecured web services.
   */
  private List<String> unsecuredWebServices = new ArrayList<>();

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
   * The private key for the application retrieved from the application's key store.
   */
  private Key privateKey;

  /**
   * The certificate for the application retrieved from the application's key store.
   */
  private X509Certificate certificate;

  /**
   * Constructs a new <code>WebApplication</code>.
   */
  public WebApplication() {}

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

                  // Check if this is an unsecured web service
                  for (String unsecuredWebService : unsecuredWebServices)
                  {
                    if (requestPath.startsWith(unsecuredWebService))
                    {
                      wrappedHttpHandler.handleRequest(httpServerExchange);

                      return;
                    }
                  }

                  if ((configuration.getMutualSSL().getSecureAPIs() && requestPath.startsWith(
                      "/api/"))
                      || (configuration.getMutualSSL().getSecureWebServices()
                          && requestPath.startsWith("/service/")))
                  {
                    SSLSessionInfo sslSessionInfo = httpServerExchange.getConnection()
                        .getSslSessionInfo();

                    if (sslSessionInfo == null)
                    {
                      if (requestPath.startsWith("/api/"))
                      {
                        logger.warn("The remote client (" + httpServerExchange.getSourceAddress()
                            + ") is attempting to access the secure API (" + requestPath
                            + ") insecurely");
                      }
                      else if (requestPath.startsWith("/service/"))
                      {
                        logger.warn("The remote client (" + httpServerExchange.getSourceAddress()
                            + ") is attempting to access the secure web service (" + requestPath
                            + ") insecurely");
                      }

                      httpServerExchange.setStatusCode(403);
                      httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
                          "text/plain");
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
                          if (requestPath.startsWith("/api/"))
                          {
                            logger.debug("The remote client ("
                                + httpServerExchange.getSourceAddress() + ") with certificate ("
                                + certificates[0].getSubjectDN()
                                + ") is attempting to access the secure API (" + requestPath + ")");
                          }
                          else if (requestPath.startsWith("/service/"))
                          {
                            logger.debug("The remote client ("
                                + httpServerExchange.getSourceAddress() + ") with certificate ("
                                + certificates[0].getSubjectDN()
                                + ") is attempting to access the secure web service ("
                                + requestPath + ")");
                          }
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
                    keyStore = loadKeyStore(keyStoreConfiguration.getPath(),
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
                      trustStore = loadTrustStore(trustStoreConfiguration.getPath(),
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
   * Returns the application entity manager factory bean associated with the application data
   * source.
   *
   * @return the application entity manager factory bean associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean()
  {
    try
    {
      DataSource dataSource = dataSource();

      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
          new LocalContainerEntityManagerFactoryBean();

      HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setGenerateDdl(false);
      jpaVendorAdapter.setShowSql(true);

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        switch (metaData.getDatabaseProductName())
        {
          case "H2":

            jpaVendorAdapter.setDatabase(Database.H2);

            break;

          case "Microsoft SQL Server":

            jpaVendorAdapter.setDatabase(Database.SQL_SERVER);

            break;

          default:

            jpaVendorAdapter.setDatabase(Database.DEFAULT);

            break;
        }
      }

      entityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
      entityManagerFactoryBean.setJtaDataSource(dataSource);
      entityManagerFactoryBean.setPackagesToScan(StringUtils.toStringArray(getJpaPackagesToScan()));
      entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

      PlatformTransactionManager platformTransactionManager = applicationContext.getBean(
          PlatformTransactionManager.class);

      if ((platformTransactionManager != null)
          && (platformTransactionManager instanceof JtaTransactionManager))
      {
        Map<String, Object> jpaPropertyMap = entityManagerFactoryBean.getJpaPropertyMap();

        jpaPropertyMap.put("hibernate.transaction.jta.platform", new SpringJtaPlatform(
            ((JtaTransactionManager) platformTransactionManager)));
      }

      return entityManagerFactoryBean;
    }
    catch (Throwable e)
    {
      throw new FatalBeanException(
          "Failed to initialise the application entity manager factory bean", e);
    }
  }

  /**
   * Returns the certificate for the application retrieved from the application's key store.
   *
   * @return the certificate for the application retrieved from the application's key store
   */
  public X509Certificate getCertificate()
  {
    return certificate;
  }

  /**
   * Returns the runtime configuration type for the Wicket web application.
   *
   * @return the runtime configuration type for the Wicket web application
   */
  @Override
  public RuntimeConfigurationType getConfigurationType()
  {
    if (Debug.inDebugMode())
    {
      return RuntimeConfigurationType.DEVELOPMENT;
    }
    else
    {
      return RuntimeConfigurationType.DEPLOYMENT;
    }
  }

  /**
   * Returns the page that users will be redirected to in order to login to the application.
   *
   * @return the page that users will be redirected to in order to login to the application
   */
  public abstract Class<? extends Page> getLoginPage();

  /**
   * Returns the page that will log a user out of the application.
   * <p/>
   * A user will be redirected to this page when they attempt to access a secure page which they
   * do not have access to.
   *
   * @return the page that will log a user out of the application
   */
  public abstract Class<? extends Page> getLogoutPage();

  /**
   * Returns the private key for the application retrieved from the application's key store.
   *
   * @return the private key for the application retrieved from the application's key store
   */
  public Key getPrivateKey()
  {
    return privateKey;
  }

  /**
   * Returns the page that users will be redirected to once they have logged into the application.
   * <p/>
   *
   * @return the page that users will be redirected to once they have logged into the application
   */
  public abstract Class<? extends Page> getSecureHomePage();

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

    return jackson2ObjectMapperBuilder;
  }

  /**
   * Creates a new session.
   *
   * @param request  the request that will create this session
   * @param response the response to initialise, for example with cookies
   *
   * @return the new session
   */
  @Override
  public Session newSession(Request request, Response response)
  {
    Session session = new WebSession(request);

    session.bind();

    return session;
  }

  /**
   * Returns the Spring task executor to use for @Async method invocations.
   *
   * @return the Spring task executor to use for @Async method invocations
   */
  @Bean
  public Executor taskExecutor()
  {
    return new SimpleAsyncTaskExecutor();
  }

  /**
   * Returns the Spring task scheduler.
   *
   * @return the Spring task scheduler
   */
  @Bean
  public TaskScheduler taskScheduler()
  {
    return new ConcurrentTaskScheduler();
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
   * @param pathToWsdl     the path to the web service WSDL
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation, String pathToWsdl)
  {
    return createWebServiceEndpoint(name, implementation, pathToWsdl, null, true);
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
   * @param pathToWsdl     the path to the web service WSDL
   * @param isSecured      <code>true</code> if the web service must be secured using mutual SSL or
   *                       <code>false</code> if the web service can be invoked insecurely
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation,
      String pathToWsdl, boolean isSecured)
  {
    return createWebServiceEndpoint(name, implementation, pathToWsdl, null, isSecured);
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
   * @param pathToWsdl     the path to the web service WSDL
   * @param handlers       the JAX-WS web service handlers for the web service
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation,
      String pathToWsdl, List<Handler> handlers)
  {
    return createWebServiceEndpoint(name, implementation, pathToWsdl, handlers, true);
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
   * @param pathToWsdl     the path to the web service WSDL
   * @param handlers       the JAX-WS web service handlers for the web service
   * @param isSecured      <code>true</code> if the web service must be secured using mutual SSL or
   *                       <code>false</code> if the web service can be invoked insecurely
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation,
      String pathToWsdl, List<Handler> handlers, boolean isSecured)
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

      Method setWsdlLocationMethod = endpointImplClass.getMethod("setWsdlLocation", String.class);

      setWsdlLocationMethod.invoke(endpoint, pathToWsdl);

      if (handlers != null)
      {
        Method setHandlersMethod = endpointImplClass.getMethod("setHandlers", List.class);

        setHandlersMethod.invoke(endpoint, handlers);
      }

      applicationContext.getAutowireCapableBeanFactory().autowireBean(implementation);

      if (!isSecured)
      {
        unsecuredWebServices.add("/service/" + name);
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

  /**
   * Returns the data source that can be used to interact with the application database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  protected DataSource dataSource()
  {
    boolean logSQL = false;

    try
    {
      if ((configuration.getDatabase() == null)
          || (configuration.getDatabase().getDataSource() == null)
          || (configuration.getDatabase().getUrl() == null))
      {
        throw new WebApplicationException(
            "Failed to retrieve the application database configuration");
      }

      Class<? extends DataSource> dataSourceClass = Thread.currentThread().getContextClassLoader()
          .loadClass(configuration.getDatabase().getDataSource()).asSubclass(DataSource.class);

      DataSource dataSource = DataSourceBuilder.create().type(dataSourceClass).url(
          configuration.getDatabase().getUrl()).build();

      Database databaseVendor = Database.DEFAULT;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        logger.info("Connected to the " + metaData.getDatabaseProductName()
            + " application database with version " + metaData.getDatabaseProductVersion());

        switch (metaData.getDatabaseProductName())
        {
          case "H2":

            databaseVendor = Database.H2;

            break;

          case "Microsoft SQL Server":

            databaseVendor = Database.SQL_SERVER;

          default:

            logger.info("The default database tables will not be populated for the database type ("
                + metaData.getDatabaseProductName() + ")");

            break;
        }
      }

      if (databaseVendor == Database.H2)
      {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            {
              try
              {
                try (Connection connection = dataSource.getConnection();
                  Statement statement = connection.createStatement())

                {
                  statement.executeUpdate("SHUTDOWN");
                }
              }
              catch (Throwable e)
              {
                throw new RuntimeException("Failed to shutdown the in-memory application database",
                    e);
              }
            }
            ));

        /*
         * Initialise the in-memory database using the SQL statements contained in the file with
         * the specified resource path.
         */
        for (String resourcePath : getInMemoryDatabaseInitResources())
        {
          try
          {
            // Load the SQL statements used to initialise the database tables
            List<String> sqlStatements = DAOUtil.loadSQL(resourcePath);

            // Get a connection to the in-memory database
            try (Connection connection = dataSource.getConnection())
            {
              for (String sqlStatement : sqlStatements)
              {
                if (logSQL)
                {
                  LoggerFactory.getLogger(WebApplication.class).info("Executing SQL statement: "
                      + sqlStatement);
                }

                try (Statement statement = connection.createStatement())
                {
                  statement.execute(sqlStatement);
                }
              }
            }
          }
          catch (SQLException e)
          {
            try (Connection connection = dataSource.getConnection();
              Statement shutdownStatement = connection.createStatement())
            {
              shutdownStatement.executeUpdate("SHUTDOWN");
            }
            catch (Throwable f)
            {
              LoggerFactory.getLogger(WebApplication.class).error(
                  "Failed to shutdown the in-memory application database: " + e.getMessage());
            }

            throw e;
          }
        }
      }

      if (dataSource instanceof XADataSource)
      {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        atomikosDataSourceBean.setUniqueResourceName("ApplicationDataSource");
        atomikosDataSourceBean.setXaDataSource((XADataSource) dataSource);

        if (configuration.getDatabase().getMinPoolSize() > 0)
        {
          atomikosDataSourceBean.setMinPoolSize(configuration.getDatabase().getMinPoolSize());

        }
        else
        {
          atomikosDataSourceBean.setMinPoolSize(5);
        }

        if (configuration.getDatabase().getMaxPoolSize() > 0)
        {
          atomikosDataSourceBean.setMaxPoolSize(configuration.getDatabase().getMaxPoolSize());
        }
        else
        {
          atomikosDataSourceBean.setMinPoolSize(5);
        }

        return atomikosDataSourceBean;
      }
      else
      {
        return dataSource;
      }
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialise the application data source", e);
    }
  }

  /**
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialise the in-memory application database.
   */
  protected List<String> getInMemoryDatabaseInitResources()
  {
    List<String> resources = new ArrayList<>();

    resources.add("guru/mmp/application/persistence/ApplicationH2.sql");

    return resources;
  }

  /**
   * Returns the names of the packages to scan for JPA classes.
   *
   * @return the names of the packages to scan for JPA classes
   */
  protected List<String> getJpaPackagesToScan()
  {
    List<String> packagesToScan = new ArrayList<>();

    packagesToScan.add("guru.mmp.application");

    return packagesToScan;
  }

  @Override
  protected void init()
  {
    super.init();

    getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));

    getSecuritySettings().setAuthorizationStrategy(new WebAuthorizationStrategy());

    if ((System.getProperty("was.install.root") != null)
        || (System.getProperty("wlp.user.dir") != null))
    {
      setRequestCycleProvider(new WebSphereAbsoluteUrlRequestCycleProvider());
    }
  }

  /**
   * Creates and returns a new instance of <code>IConverterLocator</code>.
   *
   * @return a new <code>IConverterLocator</code> instance
   */
  @Override
  protected IConverterLocator newConverterLocator()
  {
    ConverterLocator converterLocator = new ConverterLocator();

    converterLocator.set(Date.class,
        new DateConverter()
        {
          private static final long serialVersionUID = 1000000;

          @Override
          public DateFormat getDateFormat(Locale ignore)
          {
            return new SimpleDateFormat("yyyy-MM-dd");
          }
        });

    converterLocator.set(UUID.class,
        new IConverter<Object>()
        {
          private static final long serialVersionUID = 1000000;

          @Override
          public Object convertToObject(String value, Locale locale)
              throws ConversionException
          {
            return UUID.fromString(value);
          }

          @Override
          public String convertToString(Object value, Locale locale)
          {
            return value.toString();
          }
        });

    return converterLocator;
  }

  /**
   * Loads a key store.
   *
   * @param path     the path to the key store
   * @param alias    the alias for the key pair in the key store
   * @param password the key store password
   * @param type     the type of key store e.g. JKS, PKCS12, etc
   *
   * @return the key store that was loaded
   *
   * @throws GeneralSecurityException
   */
  private KeyStore loadKeyStore(String path, String alias, String password, String type)
    throws GeneralSecurityException
  {
    KeyStore ks;

    logger.info("Loading the application key (" + alias + ") from the key store (" + path + ")");

    InputStream input = null;

    try
    {
      PathMatchingResourcePatternResolver resourceLoader =
          new PathMatchingResourcePatternResolver();

      Resource keyStoreResource = resourceLoader.getResource(path);

      if (!keyStoreResource.exists())
      {
        throw new GeneralSecurityException("The application key store (" + path
            + ") could not be found");
      }

      ks = KeyStore.getInstance(type);

      input = keyStoreResource.getInputStream();

      ks.load(input,
          ((password == null) || (password.length() == 0))
          ? new char[0]
          : password.toCharArray());

      // Attempt to retrieve the private key for the application from the key store
      privateKey = ks.getKey(alias, password.toCharArray());

      if (privateKey == null)
      {
        throw new GeneralSecurityException("A private key for the application with alias (" + alias
            + ") could not be found in the key store (" + path + ")");
      }

      // Attempt to retrieve the certificate for the application from the key store
      Certificate tmpCertificate = ks.getCertificate(alias);

      if (tmpCertificate == null)
      {
        throw new GeneralSecurityException("A certificate for the application with alias (" + alias
            + ") could not be found in the key store (" + path + ")");
      }

      if (!(tmpCertificate instanceof X509Certificate))
      {
        throw new GeneralSecurityException("The certificate for the application with alias ("
            + alias + ") is not an X509 certificate");
      }

      certificate = (X509Certificate) tmpCertificate;

      return ks;
    }
    catch (Throwable e)
    {
      throw new GeneralSecurityException("Failed to load and query the application key store ("
          + path + ")", e);
    }
    finally
    {
      try
      {
        if (input != null)
        {
          input.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }

  /**
   * Loads the trust store.
   *
   * @param path     the path to the trust store
   * @param password the trust store password
   * @param type     the type of trust store e.g. JKS, PKCS12, etc
   *
   * @return the trust store that was loaded
   *
   * @throws GeneralSecurityException
   */
  private KeyStore loadTrustStore(String path, String password, String type)
    throws GeneralSecurityException
  {
    KeyStore ks;

    logger.info("Loading the trust store (" + path + ")");

    InputStream input = null;

    try
    {
      PathMatchingResourcePatternResolver resourceLoader =
          new PathMatchingResourcePatternResolver();

      Resource trustStoreResource = resourceLoader.getResource(path);

      if (!trustStoreResource.exists())
      {
        throw new GeneralSecurityException("The application trust store (" + path
            + ") could not be found");
      }

      ks = KeyStore.getInstance(type);

      input = trustStoreResource.getInputStream();

      ks.load(input,
          ((password == null) || (password.length() == 0))
          ? new char[0]
          : password.toCharArray());

      return ks;
    }
    catch (Throwable e)
    {
      throw new GeneralSecurityException("Failed to load and query the application trust store ("
          + path + ")", e);
    }
    finally
    {
      try
      {
        if (input != null)
        {
          input.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }
}
