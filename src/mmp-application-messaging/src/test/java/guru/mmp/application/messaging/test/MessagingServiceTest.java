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

package guru.mmp.application.messaging.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.test.TestClassRunner;
import guru.mmp.application.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;

/**
 * The <code>MessagingServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>MessagingService</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class MessagingServiceTest
{
  /* Messaging Service */
  @Inject
  private IMessagingService messagingService;

  /**
   * Test.
   */
  @Test
  public void test()
    throws Exception
  {
    messagingService.getMaximumProcessingAttempts();
  }
}



//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('d21fb54e-5c5b-49e8-881f-ce00c6ced1a3', '');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('82223035-1726-407f-8703-3977708e792c', 'AuthenticateResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('cc005e6a-b01b-48eb-98a0-026297be69f3', 'CheckUserExistsRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a38bd55e-3470-46f1-a96a-a6b08a9adc63', 'CheckUserExistsResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('94d60eb6-a062-492d-b5e7-9fb1f05cf088', 'GetCodeCategoryRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('0336b544-91e5-4eb9-81db-3dd94e116c92', 'GetCodeCategoryResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('3500a28a-6a2c-482b-b81f-a849c9c3ef79', 'GetCodeCategoryWithParametersRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('12757310-9eee-4a3a-970c-9b4ee0e1108e', 'GetCodeCategoryWithParametersResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest');
//INSERT INTO MMP.MESSAGE_TYPES (ID, NAME) VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse');
