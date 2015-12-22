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

import guru.mmp.application.job.IJobService;
import guru.mmp.application.job.Job;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>JobServiceTest</code> class contains the implementation of the JUnit tests for
 * the Job Service.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class JobServiceTest
{
  private static int jobCount;
  @Inject
  private IJobService jobService;

  /**
   * Test the job parameters functionality.
   *
   * @throws Exception
   */
  @Test
  public void jobParametersTest()
    throws Exception
  {
    Job job = getTestJobDetails();

    jobService.createJob(job);

    for (int i = 0; i < 10; i++)
    {
     // jobService.createJobParameter(job.getId(), job.getName() + " Parameter " + i, job.getName() + " Value " + i);
    }



  }

  /**
   * Test the job functionality.
   *
   * @throws Exception
   */
  @Test
  public void jobTest()
    throws Exception
  {
    Job job = getTestJobDetails();

    List<Job> beforeRetrievedJobs = jobService.getJobs();

    jobService.createJob(job);

    Job retrievedJob = jobService.getJob(job.getId());

    compareJobs(job, retrievedJob);

    int numberOfJobs = jobService.getNumberOfJobs();

    assertEquals("The correct number of jobs (" + (beforeRetrievedJobs.size() + 1)
        + ") was not retrieved", beforeRetrievedJobs.size() + 1,
      numberOfJobs);

    List<Job> afterRetrievedJobs = jobService.getJobs();

    assertEquals("The correct number of jobs (" + (beforeRetrievedJobs.size() + 1)
        + ") was not retrieved", beforeRetrievedJobs.size() + 1,
      afterRetrievedJobs.size());

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
      fail("Failed to find the job (" + job.getId() + ") in the list of jobs");
    }

    //ADD UPDATE AND DELETE TEST CALLS

  }

  /**
   * Test the execute job functionality.
   *
   * @throws Exception
   */
  @Test
  public void executeJobTest()
    throws Exception
  {
    Job job = getTestJobDetails();

    jobService.executeJob(job);
  }

  private static synchronized Job getTestJobDetails()
  {
    jobCount++;

    Job job = new Job();
    job.setId(UUID.randomUUID().toString());
    job.setName("Test Job Name " + jobCount);
    job.setSchedulingPattern("5 * * * *");
    job.setJobClass("guru.mmp.application.job.TestJob");
    job.setStatus(Job.Status.SCHEDULED);
    job.setExecutionAttempts(0);
    job.setLockName(null);
    job.setLastExecuted(null);
    job.setUpdated(null);



    return job;
  }

  private void compareJobs(Job job1, Job job2)
  {
    assertEquals("The ID values for the two jobs do not match", job1.getId(),
        job2.getId());
    assertEquals("The name values for the two jobs do not match",
        job1.getName(), job2.getName());
    assertEquals("The scheduling pattern values for the two jobs do not match",
        job1.getSchedulingPattern(), job2.getSchedulingPattern());
    assertEquals("The job class values for the two jobs do not match",
        job1.getJobClass(), job2.getJobClass());
    assertEquals("The status values for the two jobs do not match",
        job1.getStatus(), job2.getStatus());
    assertEquals("The execution attempts values for the two jobs do not match",
        job1.getExecutionAttempts(), job2.getExecutionAttempts());
    assertEquals("The lock name values for the two jobs do not match",
        job1.getLockName(), job2.getLockName());
    assertEquals("The last executed values for the two jobs do not match",
        job1.getLastExecuted(), job2.getLastExecuted());
    assertEquals("The updated values for the two jobs do not match",
        job1.getUpdated(), job2.getUpdated());
  }
}
