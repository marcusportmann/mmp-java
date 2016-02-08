/*
 * Copyright 2016 Marcus Portmann
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

package guru.mmp.application.tests;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.test.ApplicationClassRunner;
import guru.mmp.common.persistence.TransactionManagerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TransactionalInterceptorTest</code> class contains the implementation of the JUnit
 * tests for the <code>TransactionalInterceptor</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationClassRunner.class)
public class TransactionalInterceptorTest
{
  private static int testDataCount;
  @Inject
  private ITestTransactionalService testTransactionalService;

  /**
   * testFailedExecutionInExistingTransaction
   *
   * @throws Exception
   */
  @Test
  public void testFailedExecutionInExistingTransaction()
    throws Exception
  {
    TransactionManager transactionManager = TransactionManagerFactory.getTransactionManager();

    UserTransaction userTransaction = getUserTransaction();

    Transaction beforeUserTransactionBeginTransaction = transactionManager.getTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Found an existing transaction");
    }

    userTransaction.begin();

    Transaction afterUserTransactionBeginTransaction = transactionManager.getTransaction();

    TestData testData = getTestData();

    testTransactionalService.createTestData(testData);

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    Transaction beforeUserTransactionRollbackTransaction = transactionManager.getTransaction();

    userTransaction.rollback();

    Transaction afterUserTransactionRollbackTransaction = transactionManager.getTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: " + "The "
          + "transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Retrieved the test data after the transaction was rolled back");
    }
  }

  /**
   * testFailedExecutionInNewTransaction
   *
   * @throws Exception
   */
  @Test
  public void testFailedExecutionInNewTransaction()
    throws Exception
  {
    UserTransaction userTransaction = getUserTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Found an "
          + "existing transaction");
    }

    userTransaction.begin();

    TestData testData = getTestData();

    testTransactionalService.createTestDataInNewTransaction(testData);

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "retrieve the test data within the transaction");
    }

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "find an active transaction after retrieving the test data");
    }

    userTransaction.rollback();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "The "
          + "transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "retrieve the test data after the transaction was rolled back");
    }
  }

  /**
   * testSuccessfulExecutionInExistingTransaction
   *
   * @throws Exception
   */
  @Test
  public void testSuccessfulExecutionInExistingTransaction()
    throws Exception
  {
    UserTransaction userTransaction = getUserTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Found an existing transaction");
    }

    userTransaction.begin();

    TestData testData = getTestData();

    testTransactionalService.createTestData(testData);

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    userTransaction.commit();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: " + "The "
          + "transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to retrieve the test data after the transaction was committed");
    }
  }

  /**
   * testSuccessfulExecutionInExistingTransaction
   *
   * @throws Exception
   */
  @Test
  public void testSuccessfulExecutionInNewTransaction()
    throws Exception
  {
    UserTransaction userTransaction = getUserTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Found an "
          + "existing transaction");
    }

    userTransaction.begin();

    TestData testData = getTestData();

    testTransactionalService.createTestDataInNewTransaction(testData);

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "retrieve the test data within the transaction");
    }

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "find an active transaction after retrieving the test data");
    }

    userTransaction.commit();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "The "
          + "transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: " + "Failed to "
          + "retrieve the test data after the transaction was committed");
    }
  }

  private static synchronized TestData getTestData()
  {
    testDataCount++;

    return new TestData("Test Data ID " + testDataCount, "Test Name " + testDataCount,
        "Test Description " + testDataCount);
  }

  private DataSource getApplicationDataSource()
  {
    try
    {
      DataSource dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");

      Thread currentThread = Thread.currentThread();

      return dataSource;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to retrieve the application data source"
          + " using the JNDI name " + "(java:app/jdbc/ApplicationDataSource)");
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
            + " lookups " + "(java:comp/UserTransaction) and (java:jboss/UserTransaction)");
      }
    }

    return userTransaction;
  }
}
