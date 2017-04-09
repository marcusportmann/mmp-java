///*
// * Copyright 2017 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package guru.mmp.application.configuration;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import guru.mmp.common.persistence.DAOUtil;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.TransactionManagementConfigurer;
//import org.springframework.transaction.jta.JtaTransactionManager;
//
//import javax.sql.DataSource;
//import javax.transaction.TransactionManager;
//import javax.transaction.UserTransaction;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//import java.util.logging.Logger;
//
////~--- JDK imports ------------------------------------------------------------
//
//
//
//
///**
// * The <code>ApplicationConfiguration</code> class.
// *
// * @author Marcus Portmann
// */
//@Configuration
//@EnableAsync
//@EnableScheduling
//@EnableTransactionManagement
//@ComponentScan(basePackages = { "guru.mmp.application" })
//public abstract class ApplicationConfiguration
//  implements TransactionManagementConfigurer
//{
//  private static Object dataSourceLock = new Object();
//  private static DataSource dataSource;
//
//  @Override
//  public PlatformTransactionManager annotationDrivenTransactionManager()
//  {
//    return transactionManager();
//  }
//
////  /**
////   * Returns the application entity manager factory associated with the application data source.
////   *
////   * @return the application entity manager factory associated with the application data source
////   */
////  @Bean(name = "applicationPersistenceUnit")
////  @DependsOn("applicationDataSource")
////  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory()
////  {
////    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
////      new LocalContainerEntityManagerFactoryBean();
////
////    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
////    jpaVendorAdapter.setGenerateDdl(false);
////    jpaVendorAdapter.setShowSql(true);
////    jpaVendorAdapter.setDatabase(Database.H2);
////
////    localContainerEntityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
////    localContainerEntityManagerFactoryBean.setJtaDataSource(dataSource());
////    localContainerEntityManagerFactoryBean.setPackagesToScan("guru.mmp.application");
////    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
////
////    Properties properties = new Properties();
////
////    properties.setProperty("hibernate.transaction.jta.platform",
////      AtomikosJtaPlatform.class.getName());
////
////    localContainerEntityManagerFactoryBean.setJpaProperties(properties);
////
////    localContainerEntityManagerFactoryBean.afterPropertiesSet();
////
////    return localContainerEntityManagerFactoryBean;
////  }
//
//  /**
//   * Returns the transaction manager.
//   *
//   * @return the transaction manager
//   */
//  @Bean(name = "transactionManager")
//  public PlatformTransactionManager transactionManager()
//  {
//    try
//    {
//
//
//
//      Enhancer transactionManagerEnhancer = new Enhancer();
//      transactionManagerEnhancer.setSuperclass(UserTransactionManager.class);
//      transactionManagerEnhancer.setCallback(new TransactionManagerTransactionTracker());
//
//      AtomikosJtaPlatform.atomikosTransactionManager =
//        (UserTransactionManager) transactionManagerEnhancer.create();
//
//      Enhancer userTransactionEnhancer = new Enhancer();
//      userTransactionEnhancer.setSuperclass(UserTransactionImp.class);
//      userTransactionEnhancer.setCallback(new UserTransactionTracker(AtomikosJtaPlatform
//        .atomikosTransactionManager));
//
//      AtomikosJtaPlatform.atomikosUserTransaction =
//        (UserTransactionImp) userTransactionEnhancer.create();
//
//      AtomikosJtaPlatform.atomikosUserTransaction.setTransactionTimeout(300);
//
//      return new JtaTransactionManager(AtomikosJtaPlatform.atomikosUserTransaction,
//        AtomikosJtaPlatform.atomikosTransactionManager);
//    }
//    catch (Throwable e)
//    {
//      throw new RuntimeException(
//        "Failed to initialise the Atomikos JTA user transaction and transaction manager", e);
//    }
//  }
//
//
//
//  /**
//   * Returns the application data source.
//   *
//   * @return the application data source
//   */
//  @Bean(name = "applicationDataSource")
//  @DependsOn({ "transactionManager" })
//  protected abstract DataSource getApplicationDataSource();
//
//
//}
//
//
//
//
//
//
//
//
////
////  /**
////   * Initialise the in-memory application database and return a data source that can be used to
////   * interact with the database.
////   * <p/>
////   * NOTE: This data source returned by this method must be closed after use with the
////   * <code>close()</code> method.
////   *
////   * @return the data source that can be used to interact with the in-memory database
////   */
////
////  protected DataSource dataSource()
////  {
////    synchronized (dataSourceLock)
////    {
////      if (dataSource == null)
////      {
////        boolean logSQL = false;
////
////        try
////        {
////          JdbcDataSource jdbcDataSource = new JdbcDataSource();
////
////          jdbcDataSource.setURL("jdbc:h2:mem:" + Thread.currentThread().getName()
////            + ";MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
////
////          Runtime.getRuntime().addShutdownHook(new Thread(() ->
////          {
////            try
////            {
////              try (Connection connection = jdbcDataSource.getConnection();
////                Statement statement = connection.createStatement())
////
////              {
////                statement.executeUpdate("SHUTDOWN");
////              }
////            }
////            catch (Throwable e)
////            {
////              throw new RuntimeException(
////                "Failed to shutdown the in-memory application database", e);
////            }
////          }
////          ));
////
////          /*
////           * Initialise the in-memory database using the SQL statements contained in the file with the
////           * specified resource path.
////           */
////          for (String resourcePath : getDatabaseInitResources())
////          {
////            try
////            {
////              // Load the SQL statements used to initialise the database tables
////              List<String> sqlStatements = DAOUtil.loadSQL(resourcePath);
////
////              // Get a connection to the in-memory database
////              try (Connection connection = jdbcDataSource.getConnection())
////              {
////                for (String sqlStatement : sqlStatements)
////                {
////                  if (logSQL)
////                  {
////                    Logger.getAnonymousLogger().info("Executing SQL statement: " + sqlStatement);
////                  }
////
////                  try (Statement statement = connection.createStatement())
////                  {
////                    statement.execute(sqlStatement);
////                  }
////                }
////              }
////            }
////            catch (SQLException e)
////            {
////              try (Connection connection = jdbcDataSource.getConnection();
////                Statement shutdownStatement = connection.createStatement())
////              {
////                shutdownStatement.executeUpdate("SHUTDOWN");
////              }
////              catch (Throwable f)
////              {
////                Logger.getAnonymousLogger().severe(
////                  "Failed to shutdown the in-memory application database: " + e.getMessage());
////              }
////
////              throw e;
////            }
////          }
////
////          AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
////
////          atomikosDataSourceBean.setUniqueResourceName(Thread.currentThread().getName()
////            + "-ApplicationDataSource");
////
////          atomikosDataSourceBean.setXaDataSource(jdbcDataSource);
////          atomikosDataSourceBean.setMinPoolSize(5);
////          atomikosDataSourceBean.setMaxPoolSize(10);
////
////          dataSource = atomikosDataSourceBean;
////        }
////        catch (Throwable e)
////        {
////          throw new RuntimeException("Failed to initialise the in-memory application database", e);
////        }
////      }
////
////      return dataSource;
////    }
////  }
////
//
//
//
//
//
//
////
////
////
/////**
//// * The <code>ApplicationConfiguration</code> class.
//// *
//// * @author Marcus Portmann
//// */
////@EnableTransactionManagement
////@Configuration
////@ComponentScan(basePackages = { "guru.mmp.application" })
////public abstract class ApplicationConfiguration
////  implements TransactionManagementConfigurer
////{
////  private PlatformTransactionManager transactionManager;
////
////  @Override
////  public PlatformTransactionManager annotationDrivenTransactionManager()
////  {
////    return transactionManager();
////  }
////
////  /**
////   * Returns the application data source.
////   *
////   * @return the application data source
////   */
////  @Bean
////  @Qualifier("applicationDataSource")
////  public DataSource getDataSource()
////  {
////    return getApplicationDataSource();
////  }
////
////  /**
////   * Returns a Spring transaction manager that leverages either the Atomikos JTA transaction manager
////   * and user transaction, if they are available, or the standard Spring JTA-based transaction
////   * manager.
////   *
////   * @return a Spring transaction manager that leverages either the Atomikos JTA transaction manager
////   *         and user transaction, if they are available, or the standard Spring JTA-based
////   *         transaction manager
////   */
////  @Bean
////  public PlatformTransactionManager transactionManager()
////  {
////    if (transactionManager == null)
////    {
////      try
////      {
////        try
////        {
////          Class<?> userTransactionManagerClass = Thread.currentThread().getContextClassLoader()
////              .loadClass("com.atomikos.icatch.jta.UserTransactionManager");
////          TransactionManager transactionManager =
////              (TransactionManager) userTransactionManagerClass.newInstance();
////
////          Class<?> userTransactionImpClass = Thread.currentThread().getContextClassLoader()
////              .loadClass("com.atomikos.icatch.jta.UserTransactionImp");
////          UserTransaction userTransaction = (UserTransaction) userTransactionImpClass.newInstance();
////
////          userTransaction.setTransactionTimeout(300);
////
////          this.transactionManager = new JtaTransactionManager(userTransaction, transactionManager);
////        }
////        catch (ClassNotFoundException ignore) {}
////
////        this.transactionManager = new JtaTransactionManager();
////      }
////      catch (Throwable e)
////      {
////        throw new ApplicationConfigurationException(
////            "Failed to initialise the JTA user transaction and transaction manager", e);
////      }
////    }
////
////    return this.transactionManager;
////  }
////
////}
