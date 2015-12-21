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

import guru.mmp.application.registry.IRegistry;
import guru.mmp.application.registry.RegistryException;

import guru.mmp.application.security.User;
import guru.mmp.application.task.ITaskService;
import guru.mmp.application.task.ScheduledTask;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

import java.math.BigDecimal;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>TaskServiceTest</code> class contains the implementation of the JUnit tests for
 * the Task Service.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class TaskServiceTest
{
  @Inject
  private ITaskService taskService;

  /**
   * Test the user functionality.
   *
   * @throws Exception
   */
  @Test
  public void scheduledTaskTest()
    throws Exception
  {

  }

  private static int scheduledTaskCount;

  private static synchronized ScheduledTask getTestScheduledTaskDetails()
  {
    scheduledTaskCount++;

    ScheduledTask scheduledTask = new ScheduledTask();
    scheduledTask.setId(UUID.randomUUID().toString());
    scheduledTask.setName("Test Scheduled Task Name " + scheduledTaskCount);
    scheduledTask.setSchedulingPattern();
    scheduledTask.setTaskClass();
    scheduledTask.setStatus(ScheduledTask.Status.UNKNOWN);
    scheduledTask.setExecutionAttempts(0);
    scheduledTask.setLockName(null);
    scheduledTask.setLastExecuted(null);
    scheduledTask.setName(null);
    scheduledTask.setUpdated(null);

    return scheduledTask;
  }
}


