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

package guru.mmp.application.persistence;

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AtomikosJtaConfiguration</code> class implements a Spring configuration class that
 * configures Atomikos as the Spring JTA Transaction Manager.
 *
 * @author Marcus Portmann
 */
@Configuration
public class AtomikosJtaConfiguration
  implements TransactionManagementConfigurer
{
  private static final Object lock = new Object();
  private static UserTransactionManager userTransactionManager;
  private static UserTransactionImp userTransaction;

  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager()
  {
    return transactionManager();
  }

  /**
   * Returns the Spring Transaction Manager.
   *
   * @return the Spring Transaction Manager
   */
  @Bean
  @DependsOn({ "userTransactionManager", "userTransaction" })
  public PlatformTransactionManager transactionManager()
  {
    return new JtaTransactionManager(userTransaction(), userTransactionManager());
  }

  /**
   * Returns the Atomikos JTA Transaction Manager.
   *
   * @return the Atomikos JTA Transaction Manager
   */
  @Bean(initMethod = "init", destroyMethod = "close")
  public TransactionManager userTransactionManager()
  {
    synchronized (lock)
    {
      if (userTransactionManager == null)
      {
        try
        {
          userTransactionManager = new UserTransactionManager();
          userTransactionManager.setForceShutdown(false);
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the Atomikos Transaction Manager", e);
        }
      }

      return userTransactionManager;
    }
  }

  /**
   * Returns the Atomikos User Transaction.
   *
   * @return the Atomikos User Transaction
   */
  @Bean
  @DependsOn({ "userTransactionManager" })
  public UserTransaction userTransaction()
  {
    synchronized (lock)
    {
      if (userTransaction == null)
      {
        try
        {
          userTransaction = new UserTransactionImp();
          userTransaction.setTransactionTimeout(300);
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the Atomikos User Transaction", e);
        }
      }

      return userTransaction;
    }
  }
}
