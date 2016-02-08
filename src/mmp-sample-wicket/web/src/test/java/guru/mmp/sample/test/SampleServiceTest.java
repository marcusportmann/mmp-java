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

import javax.inject.Inject;
import javax.transaction.Transaction;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * The <code>SampleServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>SampleService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationClassRunner.class)
@ApplicationDataSourceResourceReference(name="java:jboss/datasources/SampleDS")
@ApplicationDataSourceSQLResource(path="guru/mmp/sample/persistence/SampleH2.sql")
public class SampleServiceTest
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleServiceTest.class);

  @Inject
  private ISampleService sampleService;

  @Test
  public void transactionalTest()
    throws Exception
  {
    Transaction transaction = null;

    try
    {
      TransactionManager.getTransactionManager().beginNew();

      transaction = TransactionManager.getTransactionManager().getTransaction();

      sampleService.addDataAndValidateTransaction(transaction);

      List<Data> retrievedData = sampleService.getDataAndValidateTransaction(transaction);

      assertEquals("The correct number of data objects was not retrieved", 10, retrievedData.size());

      TransactionManager.getTransactionManager().commit();
    }
    catch (Throwable e)
    {
      TransactionManager.getTransactionManager().rollback();

      throw e;
    }
  }


}

