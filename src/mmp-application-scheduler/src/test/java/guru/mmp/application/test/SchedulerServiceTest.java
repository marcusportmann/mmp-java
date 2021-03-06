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

import guru.mmp.application.scheduler.ISchedulerService;
import guru.mmp.application.scheduler.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SchedulerServiceTest</code> class contains the implementation of the JUnit tests for
 * the Scheduler Service.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { SchedulerTestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class SchedulerServiceTest
{
  private static int jobCount;

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

  /**
   * Test the execute job functionality.
   * @Test
   * public void executeJobTest()
   * throws Exception
   * {
   * Job job = getTestJobDetails();
   *
   * schedulerService.executeJob(job);
   * }
   *
   *
   * Test the job parameters functionality.
   */
  @Test
  public void jobParametersTest()
    throws Exception
  {
    Job job = getTestJobDetails();
    job.setIsEnabled(false);

    schedulerService.createJob(job);

    for (int i = 0; i < 10; i++)
    {
      // schedulerService.createJobParameter(job.getId(), job.getName() + " Parameter " + i, job
      // .getName() + " Value " + i);
    }
  }

  /**
   * Test the job functionality.
   */
  @Test
  public void jobTest()
    throws Exception
  {
    Job disabledJob = getTestJobDetails();
    disabledJob.setIsEnabled(false);

    schedulerService.createJob(disabledJob);

    List<Job> unscheduledJobs = schedulerService.getUnscheduledJobs();

    for (Job unscheduledJob : unscheduledJobs)
    {
      if (unscheduledJob.getId().equals(disabledJob.getId()))
      {
        fail(String.format("The disabled job (%s) was retrieved incorrectly as an unscheduled job",
            disabledJob.getId()));
      }
    }

    Job job = getTestJobDetails();

    List<Job> beforeRetrievedJobs = schedulerService.getJobs();

    schedulerService.createJob(job);

    Job retrievedJob = schedulerService.getJob(job.getId());

    compareJobs(job, retrievedJob);

    int numberOfJobs = schedulerService.getNumberOfJobs();

    assertEquals(String.format("The correct number of jobs (%d) was not retrieved",
        beforeRetrievedJobs.size() + 1), beforeRetrievedJobs.size() + 1, numberOfJobs);

    List<Job> afterRetrievedJobs = schedulerService.getJobs();

    assertEquals(String.format("The correct number of jobs (%d) was not retrieved",
        beforeRetrievedJobs.size() + 1), beforeRetrievedJobs.size() + 1, afterRetrievedJobs.size());

    boolean foundJob = false;

    for (Job afterRetrievedJob : afterRetrievedJobs)
    {
      if (afterRetrievedJob.getId().equals(job.getId()))
      {
        compareJobs(job, afterRetrievedJob);

        foundJob = true;

        break;
      }
    }

    if (!foundJob)
    {
      fail(String.format("Failed to find the job (%s) in the list of jobs", job.getId()));
    }

    boolean foundUnscheduledJob = false;

    unscheduledJobs = schedulerService.getUnscheduledJobs();

    for (Job unscheduledJob : unscheduledJobs)
    {
      if (unscheduledJob.getId().equals(job.getId()))
      {
        foundUnscheduledJob = true;

        break;
      }
    }

    if (!foundUnscheduledJob)
    {
      fail(String.format("Failed to find the job (%s) in the list of unscheduled jobs",
          job.getId()));
    }

    while (schedulerService.scheduleNextUnscheduledJobForExecution()) {}

    unscheduledJobs = schedulerService.getUnscheduledJobs();

    for (Job unscheduledJob : unscheduledJobs)
    {
      if (unscheduledJob.getId().equals(job.getId()))
      {
        fail(String.format("The job (%s) was retrieved incorrectly as an unscheduled job",
            job.getId()));

        break;
      }
    }

    retrievedJob = schedulerService.getJob(job.getId());

    assertEquals("The status for the job (" + job.getId() + ") is incorrect", Job.Status.SCHEDULED,
        retrievedJob.getStatus());

    // ADD UPDATE AND DELETE TEST CALLS

  }

  private static synchronized Job getTestJobDetails()
  {
    jobCount++;

    Job job = new Job();
    job.setId(UUID.randomUUID());
    job.setName("Test Job Name " + jobCount);
    job.setSchedulingPattern("5 * * * *");
    job.setJobClass("guru.mmp.application.scheduler.TestJob");
    job.setIsEnabled(true);
    job.setStatus(Job.Status.UNSCHEDULED);
    job.setExecutionAttempts(0);
    job.setLockName(null);
    job.setLastExecuted(null);
    job.setUpdated(null);

    return job;
  }

  private void compareJobs(Job job1, Job job2)
  {
    assertEquals("The ID values for the two jobs do not match", job1.getId(), job2.getId());
    assertEquals("The name values for the two jobs do not match", job1.getName(), job2.getName());
    assertEquals("The scheduling pattern values for the two jobs do not match",
        job1.getSchedulingPattern(), job2.getSchedulingPattern());
    assertEquals("The job class values for the two jobs do not match", job1.getJobClass(),
        job2.getJobClass());
    assertEquals("The is enabled values for the two jobs do not match", job1.getIsEnabled(),
        job2.getIsEnabled());
    assertEquals("The status values for the two jobs do not match", job1.getStatus(),
        job2.getStatus());
    assertEquals("The execution attempts values for the two jobs do not match",
        job1.getExecutionAttempts(), job2.getExecutionAttempts());
    assertEquals("The lock name values for the two jobs do not match", job1.getLockName(),
        job2.getLockName());
    assertEquals("The last executed values for the two jobs do not match", job1.getLastExecuted(),
        job2.getLastExecuted());
    assertEquals("The updated values for the two jobs do not match", job1.getUpdated(),
        job2.getUpdated());
  }
}
