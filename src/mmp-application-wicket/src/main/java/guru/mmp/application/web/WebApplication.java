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
import guru.mmp.application.Debug;
import guru.mmp.common.persistence.DAOUtil;
import org.apache.wicket.*;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
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
import java.sql.Connection;
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
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(basePackages = { "guru.mmp.application" }, lazyInit = true)
public abstract class WebApplication extends org.apache.wicket.protocol.http.WebApplication
{
  private static final Object inMemoryDataSourceLock = new Object();
  private static final Object entityManagerFactoryBeanLock = new Object();
  private static DataSource inMemoryDataSource;
  @Inject
  private ApplicationContext applicationContext;
  private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

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
        entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabase(Database.H2);

        entityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
        entityManagerFactoryBean.setJtaDataSource(dataSource());
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
   * Returns the data source that can be used to interact with the application database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  protected abstract DataSource dataSource();

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
   * Initialise the in-memory application database and return a data source that can be used to
   * interact with the database.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  protected DataSource inMemoryDataSource()
  {
    synchronized (inMemoryDataSourceLock)
    {
      if (inMemoryDataSource == null)
      {
        boolean logSQL = false;

        try
        {
          JdbcDataSource jdbcDataSource = new JdbcDataSource();

          jdbcDataSource.setURL("jdbc:h2:mem:" + Thread.currentThread().getName()
              + ";MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

          Runtime.getRuntime().addShutdownHook(new Thread(() ->
              {
                try
                {
                  try (Connection connection = jdbcDataSource.getConnection();
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
              try (Connection connection = jdbcDataSource.getConnection())
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
              try (Connection connection = jdbcDataSource.getConnection();
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

          AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

          atomikosDataSourceBean.setUniqueResourceName(Thread.currentThread().getName()
              + "-ApplicationDataSource");

          atomikosDataSourceBean.setXaDataSource(jdbcDataSource);
          atomikosDataSourceBean.setMinPoolSize(5);
          atomikosDataSourceBean.setMaxPoolSize(10);

          inMemoryDataSource = atomikosDataSourceBean;
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the in-memory application database", e);
        }
      }

      return inMemoryDataSource;
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
