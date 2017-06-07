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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Application</code> class provides the base class that all application-specific
 * application classes should be derived from.
 *
 * @author Marcus Portmann
 */
@ComponentScan(basePackages = { "guru.mmp.application" }, lazyInit = true)
@EnableAsync
@EnableConfigurationProperties(ApplicationConfiguration.class)
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
@SuppressWarnings("unused")
public abstract class Application
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
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

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
   * Constructs a new <code>Application</code>.
   */
  public Application() {}

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
    synchronized (dataSourceLock)
    {
      if (applicationDataSource == null)
      {
        boolean logSQL = false;

        try
        {
          if ((applicationConfiguration.getDatabase() == null)
              || (applicationConfiguration.getDatabase().getDataSource() == null)
              || (applicationConfiguration.getDatabase().getUrl() == null))
          {
            throw new WebApplicationException(
                "Failed to retrieve the application database configuration");
          }

          Class<? extends DataSource> dataSourceClass = Thread.currentThread()
              .getContextClassLoader().loadClass(applicationConfiguration.getDatabase()
              .getDataSource()).asSubclass(DataSource.class);

          DataSource dataSource = DataSourceBuilder.create().type(dataSourceClass).url(
              applicationConfiguration.getDatabase().getUrl()).build();

          try (Connection connection = dataSource.getConnection())
          {
            DatabaseMetaData metaData = connection.getMetaData();

            logger.info("Connected to the " + metaData.getDatabaseProductName()
                + " application database with " + "version "
                + metaData.getDatabaseProductVersion());

            switch (metaData.getDatabaseProductName())
            {
              case "H2":

                applicationDatabaseVendor = Database.H2;

                break;

              case "Microsoft SQL Server":

                applicationDatabaseVendor = Database.SQL_SERVER;

              default:

                logger.info(
                    "The default database tables will not be populated for the database type ("
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

            if (applicationConfiguration.getDatabase().getMinPoolSize() > 0)
            {
              atomikosDataSourceBean.setMinPoolSize(applicationConfiguration.getDatabase()
                  .getMinPoolSize());

            }
            else
            {
              atomikosDataSourceBean.setMinPoolSize(5);
            }

            if (applicationConfiguration.getDatabase().getMaxPoolSize() > 0)
            {
              atomikosDataSourceBean.setMaxPoolSize(applicationConfiguration.getDatabase()
                  .getMaxPoolSize());
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
}
