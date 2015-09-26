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

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Activity</code> class provides the base class that all
 * Business Process Model and Notation (BPMN) subprocess subclasses should be derived from.
 * <p>
 * A subprocess has parts that are modeled in a child-level process, a process with its own activity
 * flow and start and end states.
 *
 * @author Marcus Portmann
 */
public final class Subprocess extends Activity
{
  /**
   * The behavior for the subprocess.
   */
  private ISubprocessBehavior subprocessBehavior;

  /**
   * Constructs a new <code>Subprocess</code>.
   *
   * @param id                 the ID uniquely identifying the subprocess
   * @param forCompensation    is the subprocess for compensation
   * @param loopType           the loop type for the subprocess
   * @param startQuantity      the start quantity for the subprocess
   * @param completionQuantity the completion quantity for the subprocess
   * @param subprocessBehavior the behavior for the subprocess
   */
  public Subprocess(String id, boolean forCompensation, LoopType loopType, int startQuantity,
      int completionQuantity, ISubprocessBehavior subprocessBehavior)
  {
    super(id, forCompensation, loopType, startQuantity, completionQuantity);

    this.subprocessBehavior = subprocessBehavior;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) subprocess.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) subprocess
   */
  public List<Token> execute(ProcessExecutionContext context)
  {
    return subprocessBehavior.execute(context);
  }

  /**
   * Returns the behavior for the subprocess.
   *
   * @return the behavior for the subprocess
   */
  public ISubprocessBehavior getSubprocessBehavior()
  {
    return subprocessBehavior;
  }
}
