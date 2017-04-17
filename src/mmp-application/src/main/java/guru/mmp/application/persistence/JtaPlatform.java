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

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.lang.reflect.Method;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>JtaPlatform</code> class.
 *
 * @author Marcus Portmann
 */
public class JtaPlatform extends AbstractJtaPlatform
{
  private static TransactionManager transactionManager;
  private static UserTransaction userTransaction;
  private static Object lock = new Object();

  /**
   * Constructs a new <code>JtaPlatform</code>.
   */
  public JtaPlatform()
  {

    // TODO: Check for other sources for the Transaction Manager and User Transaction -- MARCUS

    synchronized(lock)
    {
      try
      {
        Class<?> atomikosUserTransactionManagerClass = Thread.currentThread().getContextClassLoader().loadClass(

          "com.atomikos.icatch.jta.UserTransactionManager");
        Class<?> atomikosUserTransactionClass = Thread.currentThread().getContextClassLoader().loadClass(
          "com.atomikos.icatch.jta.UserTransactionImp");

        transactionManager = (TransactionManager) atomikosUserTransactionManagerClass.newInstance();
        userTransaction = (UserTransaction) atomikosUserTransactionClass.newInstance();

        Method setTransactionTimeoutMethod = userTransaction.getClass().getMethod(
          "setTransactionTimeout", Integer.class);

        setTransactionTimeoutMethod.invoke(userTransaction, new Integer(300));
      }
      catch (Throwable e)
      {
        throw new RuntimeException(
          "Failed to initialise the JTA user transaction and transaction manager", e);
      }
    }
  }

  @Override
  protected TransactionManager locateTransactionManager()
  {
    return transactionManager;
  }

  @Override
  protected UserTransaction locateUserTransaction()
  {
    return userTransaction;
  }



}
