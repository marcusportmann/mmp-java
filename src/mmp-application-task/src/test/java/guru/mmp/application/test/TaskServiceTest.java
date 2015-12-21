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
import guru.mmp.application.security.Function;
import guru.mmp.application.security.User;
import guru.mmp.application.task.ITaskService;
import guru.mmp.application.task.ScheduledTask;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import java.math.BigDecimal;

import java.util.List;
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
  private static int scheduledTaskCount;
  @Inject
  private ITaskService taskService;

  /**
   * Test the scheduled task functionality.
   *
   * @throws Exception
   */
  @Test
  public void scheduledTaskTest()
    throws Exception
  {
    ScheduledTask scheduledTask = getTestScheduledTaskDetails();

    List<ScheduledTask> beforeRetrievedScheduledTasks = taskService.getScheduledTasks();

    taskService.createScheduledTask(scheduledTask);

    ScheduledTask retrievedScheduledTask = taskService.getScheduledTask(scheduledTask.getId());

    compareScheduledTasks(scheduledTask, retrievedScheduledTask);

    int numberOfScheduledTasks = taskService.getNumberOfScheduledTasks();

    List<ScheduledTask> afterRetrievedScheduledTasks = taskService.getScheduledTasks();

    assertEquals("The correct number of scheduled tasks (" + (beforeRetrievedScheduledTasks.size() + 1)
        + ") was not retrieved", beforeRetrievedScheduledTasks.size() + 1,
      afterRetrievedScheduledTasks.size());

    boolean foundScheduledTask = false;

    for (ScheduledTask afterRetrievedScheduledTask : afterRetrievedScheduledTasks)
    {
      if (afterRetrievedScheduledTask.getId().equals(scheduledTask.getId()))
      {
        compareScheduledTasks(scheduledTask, afterRetrievedScheduledTask);

        foundScheduledTask = true;

        break;
      }
    }

    if (!foundScheduledTask)
    {
      fail("Failed to find the scheduled task (" + scheduledTask.getId() + ") in the list of scheduled tasks");
    }
  }

  private static synchronized ScheduledTask getTestScheduledTaskDetails()
  {
    scheduledTaskCount++;

    ScheduledTask scheduledTask = new ScheduledTask();
    scheduledTask.setId(UUID.randomUUID().toString());
    scheduledTask.setName("Test Scheduled Task Name " + scheduledTaskCount);
    scheduledTask.setSchedulingPattern("5 * * * *");
    scheduledTask.setTaskClass("guru.mmp.application.test.TestTask");
    scheduledTask.setStatus(ScheduledTask.Status.SCHEDULED);
    scheduledTask.setExecutionAttempts(0);
    scheduledTask.setLockName(null);
    scheduledTask.setLastExecuted(null);
    scheduledTask.setUpdated(null);

    return scheduledTask;
  }

  private void compareScheduledTasks(ScheduledTask scheduledTask1, ScheduledTask scheduledTask2)
  {
    assertEquals("The ID values for the two scheduled tasks do not match", scheduledTask1.getId(),
        scheduledTask2.getId());
    assertEquals("The name values for the two scheduled tasks do not match",
        scheduledTask1.getName(), scheduledTask2.getName());
    assertEquals("The scheduling pattern values for the two scheduled tasks do not match",
        scheduledTask1.getSchedulingPattern(), scheduledTask2.getSchedulingPattern());
    assertEquals("The task class values for the two scheduled tasks do not match",
        scheduledTask1.getTaskClass(), scheduledTask2.getTaskClass());
    assertEquals("The status values for the two scheduled tasks do not match",
        scheduledTask1.getStatus(), scheduledTask2.getStatus());
    assertEquals("The execution attempts values for the two scheduled tasks do not match",
        scheduledTask1.getExecutionAttempts(), scheduledTask2.getExecutionAttempts());
    assertEquals("The lock name values for the two scheduled tasks do not match",
        scheduledTask1.getLockName(), scheduledTask2.getLockName());
    assertEquals("The last executed values for the two scheduled tasks do not match",
        scheduledTask1.getLastExecuted(), scheduledTask2.getLastExecuted());
    assertEquals("The updated values for the two scheduled tasks do not match",
        scheduledTask1.getUpdated(), scheduledTask2.getUpdated());
  }
}
