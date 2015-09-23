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

import guru.mmp.application.process.bpmn.ModelExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import guru.mmp.application.process.bpmn.event.StartEventType;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>EventSubprocess</code> class represents a Business Process Model and Notation (BPMN)
 * event subprocess that forms part of a BPMN model.
 * <p>
 * An event subprocess is not part of the normal flow of the process. Instead, it is triggered by
 * one of the following events:
 * <ul>
 *   <li>Message</li>
 *   <li>Timer</li>
 *   <li>Multiple</li>
 *   <li>Multiple-Parallel</li>
 *   <li>Conditional</li>
 *   <li>Signal</li>
 *   <li>Escalation</li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public class EventSubprocess extends Subprocess
{
  /**
   * The type of start event for the event subprocess.
   */
  private EventSubprocessStartEventType startEventType;

  /**
   * Execute the Business Process Model and Notation (BPMN) element.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) model
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) element
   */
  @Override
  public List<Token> execute(ModelExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the type of start event for the event subprocess.
   *
   * @return the type of start event for the event subprocess
   */
  public EventSubprocessStartEventType getStartEventType()
  {
    return startEventType;
  }

  /**
   * Set the type of start event for the event subprocess.
   *
   * @param startEventType the type of start event for the event subprocess
   */
  public void setStartEventType(EventSubprocessStartEventType startEventType)
  {
    this.startEventType = startEventType;
  }
}
