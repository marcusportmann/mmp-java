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
 * The <code>BoundaryEvent</code> class represents a Business Process Model and Notation (BPMN)
 * boundary event that forms part of a BPMN model.
 * <p>
 * Boundary events are placed on the boundary of an activity.
 * <p>
 * Boundary events are catching only intermediate events that may or may not be interrupting to the
 * activity. Events thrown inside an activity are passed up the process hierarchy until some
 * activity catches them.
 * <p>
 * Common uses of boundary events include deadlines and timeouts.
 *
 * @author Marcus Portmann
 */
public class BoundaryEvent extends CatchingEvent
{
  /**
   * Is the boundary event interrupting i.e. does the activity that triggered the event terminate
   * and the flow of the process continue from the boundary event (interrupting) or does the
   * activity continue and the flow at the boundary event execute in parallel (non-interrupting)?
   */
  private boolean interrupting;

  /**
   * Constructs a new <code>BoundaryEvent</code>.
   *
   * @param name         the name of the boundary event
   * @param type         the type of boundary event
   * @param interrupting is the boundary event interrupting
   */
  public BoundaryEvent(String name, EventType type, boolean interrupting)
  {
    super(name, type);

    this.interrupting = interrupting;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) boundary event.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) model
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) boundary event
   */
  @Override
  public List<Token> execute(ModelExecutionContext context)
  {
    return super.execute(context);
  }

  /**
   * Is the boundary event interrupting?
   * <p>
   * Does the activity that triggered the boundary event terminate and the flow of the process
   * continue from the event (interrupting) or does the activity continue and the flow at the
   * event execute in parallel (non-interrupting)?
   *
   * @return <code>true</code> if the boundary event is interrupting or <code>false</code> otherwise
   */
  public boolean isInterrupting()
  {
    return interrupting;
  }

  /**
   * Set whether the boundary event is interrupting.
   * <p>
   * If the boundary event is interrupting, the activity that triggered the event will be
   * terminated and the flow of the process will continue from the event. If the boundary
   * event is non-interrupting the activity that triggered the event will continue and the flow at
   * the event will execute in parallel.
   *
   * @param interrupting <code>true</code> if the boundary event is interrupting or
   *                     <code>false</code> otherwise
   */
  public void setInterrupting(boolean interrupting)
  {
    this.interrupting = interrupting;
  }
}
