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

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CallActivity</code> class represents a Business Process Model and Notation (BPMN)
 * call activity that forms part of a BPMN process.
 * <p>
 * A call activity is just a reusable activity. If a subprocess is referenced in more than one
 * process (diagram), it can be defined in its own diagram and "called" from each process that
 * uses it.
 *
 * @author Marcus Portmann
 */
public class CallActivity extends Activity
{
  /**
   * Constructs a new <code>CallActivity</code>.
   *
   * @param id                 the ID uniquely identifying the call activity
   * @param forCompensation    is the call activity for compensation
   * @param loopType           the loop type for the call activity
   * @param startQuantity      the start quantity for the call activity
   * @param completionQuantity the completion quantity for the call activity
   */
  public CallActivity(String id, boolean forCompensation, LoopType loopType, int startQuantity,
      int completionQuantity)
  {
    super(id, forCompensation, loopType, startQuantity, completionQuantity);
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) call activity.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) call activity
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
