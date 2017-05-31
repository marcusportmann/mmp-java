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

import guru.mmp.application.persistence.AtomikosJtaConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * The <code>TransactionManagerTest</code> class.
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { AtomikosJtaConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })

public class TransactionManagerTest
{
  @Inject
  private PlatformTransactionManager transactionManager;

  /**
   * Test the JTA Transaction Manager.
   */
  @Test
  public void transactionManagerTest()
  {
    transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition
        .PROPAGATION_NEVER));

    TransactionStatus transactionStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));

    transactionManager.rollback(transactionStatus);
  }
}
