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
import io.undertow.Undertow;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.SSLSessionInfo;
import io.undertow.servlet.api.DeploymentInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
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

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
  /**
   * The application data source lock.
   */
  private static final Object dataSourceLock = new Object();

  /**
   * The entity manager factory bean lock.
   */
  private static final Object entityManagerFactoryBeanLock = new Object();

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

  /**
   * The application data source.
   */
  private static DataSource applicationDataSource;

  /**
   * The Spring application context.
   */
  @Inject
  private ApplicationContext applicationContext;

  /**
   * The entity manager factory bean.
   */
  private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

  /**
   * The Spring application configuration retrieved from the <b>classpath:application.yml</b> file.
   */
  @Autowired
  private ApplicationConfiguration applicationConfiguration;

  /**
   * The database vendor for the application data source.
   */
  private Database applicationDatabaseVendor;

  /**
   * Constructs a new <code>WebApplication</code>.
   */
  public WebApplication() {}

  /**
   * Returns the application entity manager factory associated with the application data source.
   *
   * @return the application entity manager factory associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory()
  {
    synchronized (entityManagerFactoryBeanLock)
    {
      if (entityManagerFactoryBean == null)
      {
        DataSource dataSource = dataSource();

        entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);

        if (applicationDatabaseVendor == Database.H2)
        {
          jpaVendorAdapter.setDatabase(Database.H2);
        }
        else
        {
          jpaVendorAdapter.setDatabase(Database.DEFAULT);
        }

        entityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
        entityManagerFactoryBean.setJtaDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(StringUtils.toStringArray(
            getJpaPackagesToScan()));
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
      }
    }

    return entityManagerFactoryBean;
  }

  /**
   * Method description
   *
   * @return
   */
  @Bean
  public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory()
  {
    UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();

    factory.addDeploymentInfoCustomizers(new UndertowDeploymentInfoCustomizer()
        {
          @Override
          public void customize(DeploymentInfo deploymentInfo)
          {
            // try
            // {
            // Class<? extends Servlet> cxfServlet = Thread.currentThread()
            // .getContextClassLoader().loadClass("org.apache.cxf.transport.servlet.CXFServlet")
            // .asSubclass(Servlet.class);
            //
            // ServletInfo servletInfo = new ServletInfo("CXFServlet", cxfServlet);
            //
            // servletInfo.addMapping("/services/*");
            //
            // deploymentInfo.addServlet(servletInfo);
            //
            // logger.info("Initialising the Apache CXF framework");
            // }
            // catch (ClassNotFoundException ignored)
            // {}

            deploymentInfo.addInitialHandlerChainWrapper(new HandlerWrapper()
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
                    SSLSessionInfo sslSessionInfo = httpServerExchange.getConnection()
                        .getSslSessionInfo();

                    wrappedHttpHandler.handleRequest(httpServerExchange);
                  }
                };
              }
            });

          }
        });

    factory.addBuilderCustomizers(new UndertowBuilderCustomizer()
        {
          @Override
          public void customize(Undertow.Builder builder)
          {
            builder.addHttpListener(8081, "0.0.0.0");

            // .setHandler(Handlers.path().addPrefixPath("", new HttpHandler()
            // {
            // @Override
            // public void handleRequest(HttpServerExchange httpServerExchange)
            // throws Exception
            // {
            //
            //
            // }
            // }));

            // HttpHandler httpHandler = Handlers.

            // builder.addHttpListener(8081, "0.0.0.0").setHandler(Handlers.path().addPrefixPath("", new HttpHandler()
            // {
            // @Override
            // public void handleRequest(HttpServerExchange httpServerExchange)
            // throws Exception
            // {
            //
            //
            // }
            // }));

            // InternalWebInterfaceHandler xxx =  null;

            // builder.addHttpsListener();

          }

        });

    factory.addInitializers();

    return factory;
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
   * Returns the page that users will be redirected to once they have logged into the application.
   * <p/>
   *
   * @return the page that users will be redirected to once they have logged into the application
   */
  public abstract Class<? extends Page> getSecureHomePage();

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
    return createWebServiceEndpoint(name, implementation, pathToWsdl, null);
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



  /**
   * Returns the data source that can be used to interact with the application database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  protected DataSource dataSource()
  {
    synchronized (dataSourceLock)
    {
      if (applicationDataSource == null)
      {
        boolean logSQL = false;

        try
        {
          if ((applicationConfiguration.getDatabase() == null) || (applicationConfiguration.getDatabase().getDataSource() == null) || (applicationConfiguration.getDatabase().getUrl() == null))
          {
            throw new WebApplicationException("Failed to retrieve the application database configuration");
          }

          Class<? extends  DataSource> dataSourceClass = Thread.currentThread().getContextClassLoader().loadClass(applicationConfiguration.getDatabase().getDataSource()).asSubclass(DataSource.class);

          DataSource dataSource = DataSourceBuilder.create().type(dataSourceClass).url(applicationConfiguration.getDatabase().getUrl()).build();

          try (Connection connection = dataSource.getConnection())
          {
            DatabaseMetaData metaData = connection.getMetaData();

            logger.info("Connected to the " + metaData.getDatabaseProductName()
              + " application database with " + "version " + metaData.getDatabaseProductVersion());

            switch (metaData.getDatabaseProductName())
            {
              case "H2":

                applicationDatabaseVendor = Database.H2;

                break;

              case "Microsoft SQL Server":

                applicationDatabaseVendor = Database.SQL_SERVER;

              default:

                logger.info("The default database tables will not be populated for the database type ("
                  + metaData.getDatabaseProductName() + ")");

                break;
            }
          }

          if (applicationDatabaseVendor == Database.H2)
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
                throw new RuntimeException(
                  "Failed to shutdown the in-memory application database", e);
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
            atomikosDataSourceBean.setXaDataSource((XADataSource)dataSource);

            if (applicationConfiguration.getDatabase().getMinPoolSize() > 0)
              {
                atomikosDataSourceBean.setMinPoolSize(applicationConfiguration.getDatabase().getMinPoolSize());

          }
          else
            {
              atomikosDataSourceBean.setMinPoolSize(5);
            }

            if (applicationConfiguration.getDatabase().getMaxPoolSize() > 0)
            {
              atomikosDataSourceBean.setMaxPoolSize(applicationConfiguration.getDatabase().getMaxPoolSize());
            }
            else
            {
              atomikosDataSourceBean.setMinPoolSize(5);
            }

            applicationDataSource = atomikosDataSourceBean;
          }
          else if (dataSource instanceof DataSource)
          {
            applicationDataSource = dataSource;
          }
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the application database", e);
        }
      }

      return applicationDataSource;
    }
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
}
