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

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

import javax.naming.InitialContext;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

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
   * testFailedExecutionWithCheckedExceptionInExistingTransactionWithRollback
   *
   * @throws Exception
   */
  @Test
  public void testFailedExecutionWithCheckedExceptionInExistingTransactionWithRollback()
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

    try
    {
      testTransactionalService.createTestDataWithCheckedException(testData);
    }
    catch (TestTransactionalServiceException ignored) {}

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

    userTransaction.rollback();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "The transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Retrieved the test data after the transaction was rolled back");
    }
  }

  /**
   * testFailedExecutionWithCheckedExceptionInNewTransaction
   *
   * @throws Exception
   */
  @Test
  public void testFailedExecutionWithCheckedExceptionInNewTransaction()
    throws Exception {}

  /**
   * testFailedExecutionWithRuntimeExceptionInExistingTransactionWithRollback
   *
   * @throws Exception
   */
  @Test
  public void testFailedExecutionWithRuntimeExceptionInExistingTransactionWithRollback()
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

    try
    {
      testTransactionalService.createTestDataWithRuntimeException(testData);
    }
    catch (Throwable ignored) {}

    if (userTransaction.getStatus() != Status.STATUS_MARKED_ROLLBACK)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to find a transaction marked for rollback after creating the test data");
    }

    userTransaction.rollback();

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Retrieved the test data after the transaction was rolled back");
    }
  }

  /**
   * testFailedExecutionWithRuntimeExceptionInNewTransaction
   *
   * @throws Exception
   */
  @Test
  public void testFailedExecutionWithRuntimeExceptionInNewTransaction()
    throws Exception {}

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
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "The transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Failed to retrieve the test data after the transaction was committed");
    }
  }

  /**
   * testSuccessfulExecutionInExistingTransactionWithRollback
   *
   * @throws Exception
   */
  @Test
  public void testSuccessfulExecutionInExistingTransactionWithRollback()
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

    userTransaction.rollback();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "The transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test Transactional Service in an existing transaction: "
          + "Retrieved the test data after the transaction was rolled back");
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
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Found an existing transaction");
    }

    userTransaction.begin();

    TestData testData = getTestData();

    testTransactionalService.createTestDataInNewTransaction(testData);

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    userTransaction.commit();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "The transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to retrieve the test data after the transaction was committed");
    }
  }

  /**
   * testSuccessfulExecutionInNewTransactionWithRollback
   *
   * @throws Exception
   */
  @Test
  public void testSuccessfulExecutionInNewTransactionWithRollback()
    throws Exception
  {
    UserTransaction userTransaction = getUserTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Found an existing transaction");
    }

    userTransaction.begin();

    TestData testData = getTestData();

    testTransactionalService.createTestDataInNewTransaction(testData);

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    userTransaction.rollback();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "The transaction was not committed successfully");
    }

    retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to retrieve the test data after the transaction was rolled back");
    }
  }

  private static synchronized TestData getTestData()
  {
    testDataCount++;

    return new TestData("Test Data ID " + testDataCount, "Test Name " + testDataCount,
        "Test Description " + testDataCount);
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
            + "lookups (java:comp/UserTransaction) and (java:jboss/UserTransaction)");
      }
    }

    return userTransaction;
  }
}
