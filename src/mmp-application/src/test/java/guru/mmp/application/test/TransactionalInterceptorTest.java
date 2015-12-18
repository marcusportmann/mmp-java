/*
 * Copyright 2015 Marcus Portmann
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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.application.security.SecurityException;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;

import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * The <code>TransactionalInterceptorTest</code> class contains the implementation of the JUnit
 * tests for the <code>TransactionalInterceptor</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class TransactionalInterceptorTest
{
  /**
  * Method description
   *
   * @throws Exception
   */
  @Test
  public void testExecuteInExistingTransaction()
    throws Exception
  {
    UserTransaction userTransaction = getUserTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Found an existing transaction");
    }

    try
    {
      userTransaction.begin();





      userTransaction.commit();
    }
    catch (Throwable e)
    {
      if (userTransaction.getStatus() == Status.STATUS_ROLLING_BACK)
      {
        fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "The transaction is rolling back");
      }
      else if (userTransaction.getStatus() == Status.STATUS_ROLLEDBACK)
      {
        fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "The transaction was rolled back");
      }
      else if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
      {
        fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "No transaction exists");
      }

    }








  }




  private DataSource getApplicationDataSource()
  {
    try
    {
      return InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to retrieve the application data source"
        + " using the JNDI name (java:app/jdbc/ApplicationDataSource)");
    }
  }

  private UserTransaction getUserTransaction()
  {
    UserTransaction userTransaction = null;

    try
    {
      userTransaction = InitialContext.doLookup("java:comp/UserTransaction");
    }
    catch (Throwable ignored) {}

    if ((userTransaction == null) && (System.getProperty("jboss.home.dir") != null))
    {
      try
      {
        userTransaction = InitialContext.doLookup("java:jboss/UserTransaction");
      }
      catch (Throwable ignored)
      {
        throw new RuntimeException("Failed to lookup the bean managed transaction using the JNDI "
            + " lookups (java:comp/UserTransaction) and (java:jboss/UserTransaction)");
      }
    }

    return userTransaction;
  }
}
