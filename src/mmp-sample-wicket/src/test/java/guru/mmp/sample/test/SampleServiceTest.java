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

package guru.mmp.sample.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.test.TestClassRunner;
import guru.mmp.sample.model.Data;
import guru.mmp.sample.model.ISampleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>SampleService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { SampleTestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class SampleServiceTest
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleServiceTest.class);

  /* Sample Service */
  @Inject
  private ISampleService sampleService;

  /* Transaction Manager */
  @Inject
  private PlatformTransactionManager transactionManager;

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

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

    sampleService.addData();

    transactionManager.rollback(transactionStatus);

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

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

    sampleService.addData();

    List<Data> afterAddRetrievedData = sampleService.getData();

    assertEquals("The correct number of data objects was not retrieved",
        beforeAddRetrievedData.size() + 1, afterAddRetrievedData.size());

    transactionManager.commit(transactionStatus);

    afterAddRetrievedData = sampleService.getData();

    assertEquals("The correct number of data objects was not retrieved",
        beforeAddRetrievedData.size() + 1, afterAddRetrievedData.size());

  }
}
