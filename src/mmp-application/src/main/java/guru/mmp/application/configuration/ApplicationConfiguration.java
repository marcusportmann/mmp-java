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
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.TransactionManagementConfigurer;
//import org.springframework.transaction.jta.JtaTransactionManager;
//
//import javax.sql.DataSource;
//import javax.transaction.TransactionManager;
//import javax.transaction.UserTransaction;
//
////~--- JDK imports ------------------------------------------------------------
//
///**
// * The <code>ApplicationConfiguration</code> class.
// *
// * @author Marcus Portmann
// */
//@EnableTransactionManagement
//@Configuration
//@ComponentScan(basePackages = { "guru.mmp.application" })
//public abstract class ApplicationConfiguration
//  implements TransactionManagementConfigurer
//{
//  private PlatformTransactionManager transactionManager;
//
//  @Override
//  public PlatformTransactionManager annotationDrivenTransactionManager()
//  {
//    return transactionManager();
//  }
//
//  /**
//   * Returns the application data source.
//   *
//   * @return the application data source
//   */
//  @Bean
//  @Qualifier("applicationDataSource")
//  public DataSource getDataSource()
//  {
//    return getApplicationDataSource();
//  }
//
//  /**
//   * Returns a Spring transaction manager that leverages either the Atomikos JTA transaction manager
//   * and user transaction, if they are available, or the standard Spring JTA-based transaction
//   * manager.
//   *
//   * @return a Spring transaction manager that leverages either the Atomikos JTA transaction manager
//   *         and user transaction, if they are available, or the standard Spring JTA-based
//   *         transaction manager
//   */
//  @Bean
//  public PlatformTransactionManager transactionManager()
//  {
//    if (transactionManager == null)
//    {
//      try
//      {
//        try
//        {
//          Class<?> userTransactionManagerClass = Thread.currentThread().getContextClassLoader()
//              .loadClass("com.atomikos.icatch.jta.UserTransactionManager");
//          TransactionManager transactionManager =
//              (TransactionManager) userTransactionManagerClass.newInstance();
//
//          Class<?> userTransactionImpClass = Thread.currentThread().getContextClassLoader()
//              .loadClass("com.atomikos.icatch.jta.UserTransactionImp");
//          UserTransaction userTransaction = (UserTransaction) userTransactionImpClass.newInstance();
//
//          userTransaction.setTransactionTimeout(300);
//
//          this.transactionManager = new JtaTransactionManager(userTransaction, transactionManager);
//        }
//        catch (ClassNotFoundException ignore) {}
//
//        this.transactionManager = new JtaTransactionManager();
//      }
//      catch (Throwable e)
//      {
//        throw new ApplicationConfigurationException(
//            "Failed to initialise the JTA user transaction and transaction manager", e);
//      }
//    }
//
//    return this.transactionManager;
//  }
//
//  /**
//   * Returns the application data source.
//   *
//   * @return the application data source
//   */
//  protected abstract DataSource getApplicationDataSource();
//}
