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

import guru.mmp.application.process.bpmn.FlowElement;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>GlobalTask</code> class represents a Business Process Model and Notation (BPMN) global
 * task.
 *
 * @author Marcus Portmann
 */
public final class GlobalTask extends FlowElement
{
  /**
   * The behavior for the global task.
   */
  private TaskBehavior taskBehavior;

  /**
   * Constructs a new <code>GlobalTask</code>.
   *
   * @param id           the ID uniquely identifying the global task
   * @param name         the name of the global task
   * @param taskBehavior the behavior for the global task
   */
  public GlobalTask(String id, String name, TaskBehavior taskBehavior)
  {
    super(id, name);

    this.taskBehavior = taskBehavior;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) global task.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) global task
   */
  public List<Token> execute(ProcessExecutionContext context)
  {
    return taskBehavior.execute(context);
  }

  /**
   * Returns the behavior for the global task.
   *
   * @return the behavior for the global task
   */
  public TaskBehavior getTaskBehavior()
  {
    return taskBehavior;
  }
}
