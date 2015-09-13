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

package guru.mmp.application.task;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>TestTask</code> class implements the test task.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class TestTask
  implements ITask
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestTask.class);

  /* Task Service */
  @Inject
  private ITaskService taskService;

  /**
   * Execute the task.
   *
   * @param context the task execution context
   *
   * @throws TaskExecutionFailedException
   */
  public void execute(TaskExecutionContext context)
    throws TaskExecutionFailedException
  {
    logger.info("Executing the test task (" + taskService + ")");
  }
}
