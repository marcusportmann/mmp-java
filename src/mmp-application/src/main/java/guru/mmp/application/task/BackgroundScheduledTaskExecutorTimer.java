/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.task;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.concurrent.Future;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BackgroundScheduledTaskExecutorTimer</code> class implements the timer for the
 * background scheduled task executor.
 *
 * @author Marcus Portmann
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundScheduledTaskExecutorTimer
{
  /* Logger */
  private static Logger logger =
    LoggerFactory.getLogger(BackgroundScheduledTaskExecutorTimer.class);

  /* Background Scheduled Task Executor */
  @Inject
  private BackgroundScheduledTaskExecutor backgroundScheduledTaskExecutor;

  /* The result of executing the scheduled tasks. */
  private Future<Boolean> executeScheduledTasksResult;

  /**
   * Constructs a new <code>BackgroundScheduledTaskExecutorTimer</code>.
   */
  public BackgroundScheduledTaskExecutorTimer()
  {
    executeScheduledTasksResult = new AsyncResult<>(false);
  }

  /**
   * Execute all the tasks scheduled for execution.
   */
  @Schedule(hour = "*", minute = "*", second = "*/55", persistent = false)
  public void executeScheduledTasks()
  {
    if (executeScheduledTasksResult.isDone())
    {
      /*
       * Asynchronously inform the background scheduled task executor that all tasks scheduled for
       * execution should be executed.
       */
      try
      {
        executeScheduledTasksResult = backgroundScheduledTaskExecutor.execute();
      }
      catch (Throwable e)
      {
        logger.error("Failed to invoke the background scheduled task executor to asynchronously "
            + "execute all the scheduled tasks", e);
      }
    }
  }
}
