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

package guru.mmp.sample.jobs;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.scheduler.IJob;
import guru.mmp.application.scheduler.ISchedulerService;
import guru.mmp.application.scheduler.JobExecutionContext;
import guru.mmp.application.scheduler.JobExecutionFailedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * The <code>TestJob</code> class implements the test job.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SampleJob
  implements IJob
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleJob.class);

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

  /**
   * Execute the job.
   *
   * @param context the job execution context
   *
   * @throws JobExecutionFailedException
   */
  public void execute(JobExecutionContext context)
    throws JobExecutionFailedException
  {
    logger.info("Executing the sample job (" + schedulerService + ")");

    try
    {
      DataSource dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");

      logger.info("Successfully retrieved the data source from JNDI (" + dataSource + ")");
    }
    catch (Throwable ignored)
    {}

  }
}
