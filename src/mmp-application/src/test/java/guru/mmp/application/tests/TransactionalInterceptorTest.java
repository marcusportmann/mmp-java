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

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.transaction.Status;
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
   * testFailedExecutionWithCheckedExceptionInExistingTransactionWithRollback
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
   */
  @Test
  public void testFailedExecutionWithCheckedExceptionInNewTransaction()
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

    try
    {
      testTransactionalService.createTestDataInNewTransactionWithCheckedException(testData);
    }
    catch (Throwable ignored) {}

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    userTransaction.rollback();

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to retrieve the test data after a checked exception was caught");
    }
  }

  /**
   * testFailedExecutionWithRuntimeExceptionInExistingTransactionWithRollback
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
   */
  @Test
  public void testFailedExecutionWithRuntimeExceptionInNewTransaction()
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

    try
    {
      testTransactionalService.createTestDataInNewTransactionWithRuntimeException(testData);
    }
    catch (Throwable ignored) {}

    if (userTransaction.getStatus() != Status.STATUS_ACTIVE)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    userTransaction.rollback();

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test Transactional Service in a new transaction: "
          + "Retrieved the test data after a runtime exception was caught");
    }
  }

  /**
   * testFailedExecutionWithoutTransaction
   */
  @Test
  public void testFailedExecutionWithoutTransaction()
    throws Exception
  {
    UserTransaction userTransaction = getUserTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service without an existing transaction: "
          + "Found an existing transaction");
    }

    TestData testData = getTestData();

    try
    {
      testTransactionalService.createTestDataWithCheckedException(testData);
    }
    catch (TestTransactionalServiceException ignored) {}

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service without an existing transaction: "
          + "Failed to retrieve the test data after a checked exception was caught");
    }
  }

  /**
   * testSuccessfulExecutionInExistingTransaction
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

  /**
   * testSuccessfulExecutionWithoutTransaction
   */
  @Test
  public void testSuccessfulExecutionWithoutTransaction()
    throws Exception
  {
    UserTransaction userTransaction = getUserTransaction();

    if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
    {
      fail("Failed to invoked the Test Transactional Service without an existing transaction: "
          + "Found an existing transaction");
    }

    TestData testData = getTestData();

    testTransactionalService.createTestData(testData);

    TestData retrievedTestData = testTransactionalService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test Transactional Service without an existing transaction: "
          + "Failed to retrieve the test data");
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
