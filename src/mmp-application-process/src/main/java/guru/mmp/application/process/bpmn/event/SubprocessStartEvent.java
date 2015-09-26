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

package guru.mmp.application.process.bpmn.event;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.ModelExecutionContext;
import guru.mmp.application.process.bpmn.Token;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>SubprocessStartEvent</code> class represents a Business Process Model and Notation
 * (BPMN) subprocess start event that forms part of a BPMN model.
 * <p>
 * A subprocess start event indicates the start of a sub0process. Sub-process start events generate
 * a token when they are triggered. The token then moves down through the event's outgoing sequence
 * flow.
 * <p>
 * Sub-process start events can only have one outgoing sequence flow and they cannot have incoming
 * sequence flows.
 *
 * @author Marcus Portmann
 */
public class SubprocessStartEvent
  extends CatchingEvent
{
  /**
   * Is the catching event interrupting i.e. does the activity that triggered the event terminate
   * and the flow of the process continue from the event (interrupting) or does the activity
   * continue and the flow at the event execute in parallel (non-interrupting)?
   */
  private boolean interrupting;

  /**
   * Constructs a new <code>SubprocessStartEvent</code>.
   *
   * @param name         the name of the subprocess start event
   * @param type         the type of subprocess start event
   * @param interrupting is the subprocess start event interrupting
   */
  public SubprocessStartEvent(String name, EventType type, boolean interrupting)
  {
    super(name, type);

    this.interrupting = interrupting;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) subprocess start event.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) model
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) subprocess start event
   */
  @Override
  public List<Token> execute(ModelExecutionContext context)
  {
    return super.execute(context);
  }

  /**
   * Is the subprocess start event interrupting?
   * <p>
   * Does the activity that triggered the subprocess start event terminate and the flow of the
   * process continue from the event (interrupting) or does the activity continue and the flow at
   * the event execute in parallel (non-interrupting)?
   *
   * @return <code>true</code> if the subprocess start event is interrupting or <code>false</code>
   *         otherwise
   */
  public boolean isInterrupting()
  {
    return interrupting;
  }

  /**
   * Set whether the subprocess start event is interrupting.
   * <p>
   * If the subprocess start event is interrupting, the activity that triggered the event will be
   * terminated and the flow of the process will continue from the event. If the subprocess start
   * event is non-interrupting the activity that triggered the event will continue and the flow at
   * the event will execute in parallel.
   *
   * @param interrupting <code>true</code> if the subprocess start event is interrupting or
   *                     <code>false</code> otherwise
   */
  public void setInterrupting(boolean interrupting)
  {
    this.interrupting = interrupting;
  }
}
