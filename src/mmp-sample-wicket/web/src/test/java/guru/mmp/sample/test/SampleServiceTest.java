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

package guru.mmp.sample.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.test.ApplicationClassRunner;
import guru.mmp.application.test.ApplicationDataSourceResourceReference;
import guru.mmp.application.test.ApplicationDataSourceSQLResource;
import guru.mmp.common.persistence.TransactionManager;
import guru.mmp.sample.model.Data;
import guru.mmp.sample.model.ISampleService;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.inject.Inject;

import javax.transaction.Transaction;

/**
 * The <code>SampleServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>SampleService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationClassRunner.class)
@ApplicationDataSourceResourceReference(name = "java:jboss/datasources/SampleDS")
@ApplicationDataSourceSQLResource(path = "guru/mmp/sample/persistence/SampleH2.sql")
public class SampleServiceTest
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleServiceTest.class);
  @Inject
  private ISampleService sampleService;

  /**
   * The rollback transaction test.
   *
   * @throws Exception
   */
  @Test
  public void rollbackTransactionTest()
    throws Exception
  {
    List<Data> beforeAddRetrievedData = sampleService.getData();

    Transaction existingTransaction = null;

    try
    {
      existingTransaction = TransactionManager.getTransactionManager().beginNew();

      sampleService.addData();

      TransactionManager.getTransactionManager().rollback();
    }
    catch (Throwable e)
    {
      TransactionManager.getTransactionManager().rollback();

      throw e;
    }
    finally
    {
      if (existingTransaction != null)
      {
        TransactionManager.getTransactionManager().resume(existingTransaction);
      }
    }

    List<Data> afterAddRetrievedData = sampleService.getData();

    assertEquals("The correct number of data objects was not retrieved",
        beforeAddRetrievedData.size(), afterAddRetrievedData.size());
  }

  /**
   * The successful transaction test.
   *
   * @throws Exception
   */
  @Test
  public void successfulTransactionTest()
    throws Exception
  {
    List<Data> beforeAddRetrievedData = sampleService.getData();

    Transaction existingTransaction = null;

    try
    {
      existingTransaction = TransactionManager.getTransactionManager().beginNew();

      sampleService.addData();

      List<Data> afterAddRetrievedData = sampleService.getData();

      assertEquals("The correct number of data objects was not retrieved",
          beforeAddRetrievedData.size() + 1, afterAddRetrievedData.size());

      TransactionManager.getTransactionManager().commit();
    }
    catch (Throwable e)
    {
      TransactionManager.getTransactionManager().rollback();

      throw e;
    }
    finally
    {
      if (existingTransaction != null)
      {
        TransactionManager.getTransactionManager().resume(existingTransaction);
      }
    }
  }
}
