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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.inject.Inject;

import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestJPAServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>TestJPAService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class TestJPAServiceTest
{
  private static int testDataCount = 1000;
  @Inject
  private ITestJPAService testJPAService;
  @Inject
  private PlatformTransactionManager transactionManager;

  /**
   * testFailedExecutionWithCheckedExceptionInExistingTransactionWithRollback
   */
  @Test
  public void testFailedExecutionWithCheckedExceptionInExistingTransactionWithRollback()
    throws Exception
  {
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    try
    {
      testJPAService.createTestDataWithCheckedException(testData);
    }
    catch (TestJPAServiceException ignored) {}

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    transactionManager.rollback(transactionStatus);

    if (!transactionStatus.isCompleted())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "The transaction was not rolled back successfully");
    }

    retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
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
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    try
    {
      testJPAService.createTestDataInNewTransactionWithCheckedException(testData);
    }
    catch (Throwable ignored) {}

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    transactionManager.rollback(transactionStatus);

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
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
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    try
    {
      testJPAService.createTestDataWithRuntimeException(testData);
    }
    catch (Throwable ignored) {}

    if (!transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to find a transaction marked for rollback after creating the test data");
    }

    transactionManager.rollback(transactionStatus);

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
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
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    try
    {
      testJPAService.createTestDataInNewTransactionWithRuntimeException(testData);
    }
    catch (Throwable ignored) {}

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    transactionManager.rollback(transactionStatus);

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
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
    TestData testData = getTestData();

    try
    {
      testJPAService.createTestDataWithCheckedException(testData);
    }
    catch (TestJPAServiceException ignored) {}

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service without an existing transaction: "
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
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    testJPAService.createTestData(testData);

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    transactionManager.commit(transactionStatus);

    if (!transactionStatus.isCompleted())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "The transaction was not committed successfully");
    }

    retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
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
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    testJPAService.createTestData(testData);

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    transactionManager.rollback(transactionStatus);

    if (!transactionStatus.isCompleted())
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "The transaction was not rolled back successfully");
    }

    retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData != null)
    {
      fail("Failed to invoked the Test JPA Service in an existing transaction: "
          + "Retrieved the test data after the transaction was rolled back");
    }
  }

  /**
   * testSuccessfulExecutionInNewTransaction
   */
  @Test
  public void testSuccessfulExecutionInNewTransaction()
    throws Exception
  {
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    testJPAService.createTestDataInNewTransaction(testData);

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    transactionManager.commit(transactionStatus);

    if (!transactionStatus.isCompleted())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "The transaction was not committed successfully");
    }

    retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
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
    TestData testData = getTestData();

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    testJPAService.createTestDataInNewTransaction(testData);

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to find an active transaction after creating the test data");
    }

    TestData retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to retrieve the test data within the transaction");
    }

    if (transactionStatus.isCompleted() || transactionStatus.isRollbackOnly())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "Failed to find an active transaction after retrieving the test data");
    }

    transactionManager.rollback(transactionStatus);

    if (!transactionStatus.isCompleted())
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
          + "The transaction was not rolled back successfully");
    }

    retrievedTestData = testJPAService.getTestData(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service in a new transaction: "
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
    TestData testData = getTestData();

    // NOTE: The transactional createTestData method is called as merging requires a transaction
    testJPAService.createTestData(testData);

    TestData retrievedTestData = testJPAService.getTestDataWithoutTransaction(testData.getId());

    if (retrievedTestData == null)
    {
      fail("Failed to invoked the Test JPA Service without an existing transaction: "
          + "Failed to retrieve the test data");
    }
  }

  private static synchronized TestData getTestData()
  {
    testDataCount++;

    return new TestData("Test Data ID " + testDataCount, "Test Name " + testDataCount,
        "Test Description " + testDataCount);
  }
}
