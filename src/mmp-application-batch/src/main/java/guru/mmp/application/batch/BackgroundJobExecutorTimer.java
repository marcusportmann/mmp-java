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

package guru.mmp.application.batch;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.concurrent.Future;

import javax.ejb.*;

import javax.inject.Inject;

/**
 * The <code>BackgroundJobExecutorTimer</code> class implements the timer for the Background Job
 * Executor.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundJobExecutorTimer
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundJobExecutorTimer.class);

  /* Background Job Executor */
  @Inject
  private BackgroundJobExecutor backgroundJobExecutor;

  /* The result of executing the jobs. */
  private Future<Boolean> executeJobsResult;

  /**
   * Constructs a new <code>BackgroundJobExecutorTimer</code>.
   */
  public BackgroundJobExecutorTimer()
  {
    executeJobsResult = new AsyncResult<>(false);
  }

  /**
   * Execute all the jobs scheduled for execution.
   */
  @Schedule(hour = "*", minute = "*", second = "*/55", persistent = false)
  public void executeJobs()
  {
    if (executeJobsResult.isDone())
    {
      /*
       * Asynchronously inform the Background Job Executor that all jobs scheduled for execution
       * should be executed.
       */
      try
      {
        executeJobsResult = backgroundJobExecutor.execute();
      }
      catch (Throwable e)
      {
        logger.error(
            "Failed to invoke the Background Job Executor to asynchronously execute all the jobs",
            e);
      }
    }
  }
}
