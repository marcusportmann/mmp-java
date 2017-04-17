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

package guru.mmp.application.configuration;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.JtaPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.Executor;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationInitializer</code> class provides the base class that all
 * application-specific initializer classes should be derived from.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(basePackages = { "guru.mmp.application" })
@SuppressWarnings("unused")
public abstract class ApplicationInitializer
  implements TransactionManagementConfigurer
{
  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager()
  {
    return transactionManager();
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
   * Returns the transaction manager.
   *
   * @return the transaction manager
   */
  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager()
  {
    try
    {
      JtaPlatform jtaPlatform = new JtaPlatform();

      return new JtaTransactionManager(jtaPlatform.retrieveUserTransaction(),
          jtaPlatform.retrieveTransactionManager());
    }
    catch (Throwable e)
    {
      throw new RuntimeException(
          "Failed to initialise the JTA user transaction and transaction manager", e);
    }
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
   * Returns the application entity manager factory associated with the application data source.
   *
   * @return the application entity manager factory associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory()
  {
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
      new LocalContainerEntityManagerFactoryBean();

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(true);
    jpaVendorAdapter.setDatabase(Database.H2);

    localContainerEntityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
    localContainerEntityManagerFactoryBean.setJtaDataSource(dataSource());
    localContainerEntityManagerFactoryBean.setPackagesToScan("guru.mmp.application");
    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

//    Properties properties = new Properties();
//
//    properties.setProperty("hibernate.transaction.jta.platform",
//      AtomikosJtaPlatform.class.getName());
//
//    localContainerEntityManagerFactoryBean.setJpaProperties(properties);
//
//    localContainerEntityManagerFactoryBean.afterPropertiesSet();

    return localContainerEntityManagerFactoryBean;
  }

}
