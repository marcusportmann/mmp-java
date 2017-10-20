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

import com.atomikos.jdbc.AtomikosDataSourceBean;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.common.persistence.DAOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
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
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Application</code> class provides the class that all application-specific application
 * classes should be derived from.
 *
 * @author Marcus Portmann
 */
@ComponentScan(basePackages = { "guru.mmp.application" }, lazyInit = true)
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SuppressWarnings("unused")
public abstract class Application extends ApplicationBase
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  static
  {
    System.setProperty("com.atomikos.icatch.registered", "true");
  }

  /**
   * The distributed in-memory caches.
   */
  Map<String, Map> caches = new ConcurrentHashMap<>();

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
   * The application data source.
   */
  private DataSource dataSource;

  /**
   * Constructs a new <code>Application</code>.
   */
  public Application() {}

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

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        switch (metaData.getDatabaseProductName())
        {
          case "H2":

            jpaVendorAdapter.setDatabase(Database.H2);
            jpaVendorAdapter.setShowSql(true);

            break;

          case "Microsoft SQL Server":

            jpaVendorAdapter.setDatabase(Database.SQL_SERVER);
            jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServer2012Dialect");
            jpaVendorAdapter.setShowSql(false);

            break;

          default:

            jpaVendorAdapter.setDatabase(Database.DEFAULT);
            jpaVendorAdapter.setShowSql(false);

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
   * Returns the local validator factory bean that provides support for JSR 303 Bean Validation.
   *
   * @return the local validator factory bean that provides support for JSR 303 Bean Validation
   */
  @Bean
  @DependsOn({ "messageSource" })
  public javax.validation.Validator localValidatorFactoryBean()
  {
    final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.setValidationMessageSource(messageSource());

    return localValidatorFactoryBean;
  }

  /**
   * Returns the application message source.
   *
   * @return the application message source
   */
  @Bean
  public MessageSource messageSource()
  {
    ApplicationMessageSource messageSource = new ApplicationMessageSource();
    messageSource.setBasename("classpath*:messages");

    return messageSource;
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

      dataSource = DataSourceBuilder.create().type(dataSourceClass).url(configuration.getDatabase()
          .getUrl()).build();

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
                  LoggerFactory.getLogger(Application.class).info("Executing SQL statement: "
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
              LoggerFactory.getLogger(Application.class).error(
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

  /**
   * Shutdown the in-memory application database if required.
   */
  @PreDestroy
  protected void shutdownInMemoryApplicationDatabase()
  {
    if (dataSource != null)
    {
      try
      {
        try (Connection connection = dataSource.getConnection();
          Statement statement = connection.createStatement())

        {
          DatabaseMetaData metaData = connection.getMetaData();

          switch (metaData.getDatabaseProductName())
          {
            case "H2":

              logger.info("Shutting down the in-memory " + metaData.getDatabaseProductName()
                  + " application database with version " + metaData.getDatabaseProductVersion());

              statement.executeUpdate("SHUTDOWN");

              break;

            default:

              break;
          }
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to shutdown the in-memory application database", e);
      }
    }
  }
}
