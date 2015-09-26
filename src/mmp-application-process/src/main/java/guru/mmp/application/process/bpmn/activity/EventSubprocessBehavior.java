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
import guru.mmp.application.process.bpmn.event.EventType;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>EventSubprocessBehavior</code> class implements the behavior for a Business Process
 * Model and Notation (BPMN) event subprocess that forms part of a BPMN process.
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
public class EventSubprocessBehavior
  implements ISubprocessBehavior
{
  /**
   * The type of start event for the event subprocess.
   */
  private EventType eventType;

  /**
   * Constructs a new <code>EventSubprocessBehavior</code>.
   *
   * @param eventType the type of start event for the event subprocess
   */
  public EventSubprocessBehavior(EventType eventType)
  {
    this.eventType = eventType;
  }

  /**
   * Execute the behavior for the Business Process Model and Notation (BPMN) event subprocess.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the behavior for the
   *         Business Process Model and Notation (BPMN) event subprocess
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the type of start event for the event subprocess.
   *
   * @return the type of start event for the event subprocess
   */
  public EventType getEventType()
  {
    return eventType;
  }
}
