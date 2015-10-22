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

package guru.mmp.application.process.bpmn.activity;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>Task</code> class represents a Business Process Model and Notation (BPMN) task.
 * <p>
 * A task represents a single action.
 *
 * @author Marcus Portmann
 */
public final class Task extends Activity
{
  /**
   * The behavior for the task.
   */
  private TaskBehavior taskBehavior;

  /**
   * Constructs a new <code>Task</code>.
   *
   * @param id                 the ID uniquely identifying the task
   * @param name               the name of the task
   * @param forCompensation    is the task for compensation
   * @param startQuantity      the start quantity for the task
   * @param completionQuantity the completion quantity for the task
   * @param taskBehavior       the behavior for the task
   */
  public Task(String id, String name, boolean forCompensation, int startQuantity,
      int completionQuantity, TaskBehavior taskBehavior)
  {
    super(id, name, forCompensation, startQuantity, completionQuantity);

    this.taskBehavior = taskBehavior;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) task.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) task
   */
  public List<Token> execute(ProcessExecutionContext context)
  {
    return taskBehavior.execute(context);
  }

  /**
   * Returns the behavior for the task.
   *
   * @return the behavior for the task
   */
  public TaskBehavior getTaskBehavior()
  {
    return taskBehavior;
  }
}
