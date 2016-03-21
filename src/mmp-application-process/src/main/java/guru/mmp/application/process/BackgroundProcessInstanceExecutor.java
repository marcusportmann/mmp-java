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

package guru.mmp.application.process;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.concurrent.Future;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BackgroundProcessInstanceExecutor</code> class implements the background process
 * instance executor.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundProcessInstanceExecutor
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundProcessInstanceExecutor.class);

  /* Process Service */
  @Inject
  private IProcessService processService;

  /**
   * Execute all the process instances scheduled for execution.
   *
   * @return <code>true</code> if the process instances were executed successfully or
   *         <code>false</code> otherwise
   */
  @Asynchronous
  public Future<Boolean> execute()
  {
    // If CDI injection was not completed successfully for the bean then stop here
    if (processService == null)
    {
      logger.error("Failed to execute the process instances: The Process Service was NOT injected");

      return new AsyncResult<>(false);
    }

    try
    {
      executeProcessInstances();

      return new AsyncResult<>(true);
    }
    catch (Throwable e)
    {
      logger.error("Failed to execute the process instances", e);

      return new AsyncResult<>(false);
    }
  }

  /**
   * Initialise the background process instance executor.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Background Process Instance Executor");

    if (processService != null)
    {
      /*
       * Reset any locks for process instances that were previously being executed.
       */
      try
      {
        logger.info("Resetting the locks for the process instances being executed");

        processService.resetProcessInstanceLocks(ProcessInstance.Status.EXECUTING, ProcessInstance
            .Status.SCHEDULED);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the locks for the process instances being executed", e);
      }
    }
    else
    {
      logger.error("Failed to initialise the Background Process Instance Executor:"
          + " The Process Service was NOT injected");
    }
  }

  private void executeProcessInstances()
  {
    ProcessInstance processInstance;

    while (true)
    {
      // Retrieve the next process instance scheduled for execution
      try
      {
        processInstance = processService.getNextProcessInstanceScheduledForExecution();

        if (processInstance == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No process instances scheduled for execution");
          }

          return;
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the next process instance scheduled for execution", e);

        return;
      }

      // Execute the process instance
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Executing the process instance (" + processInstance.getId() + ")");
        }

        processService.executeProcessInstance(processInstance);
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to execute the process instance (%s)",
            processInstance.getId()), e);

        try
        {
          processService.unlockProcessInstance(processInstance.getId(), ProcessInstance.Status
              .FAILED);
        }
        catch (Throwable f)
        {
          logger.error(String.format(
              "Failed to unlock and set the status for the process instance (%s)",
              processInstance.getId()), f);
        }
      }
    }
  }
}
