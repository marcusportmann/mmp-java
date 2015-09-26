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

import guru.mmp.application.process.bpmn.FlowNode;

/**
 * The <code>Activity</code> class provides the base class that all Business Process Model and
 * Notation (BPMN) activity subclasses should be derived from.
 * <p>
 * An activity represents a unit of work performed. A step inside a process. It has a defined start
 * and end and generally requires some kind of input to produce an output.
 *
 * @author Marcus Portmann
 */
abstract class Activity extends FlowNode
{
  /**
   * The completion quantity for the activity.
   */
  private int completionQuantity;

  /**
   * Is the activity for compensation?
   */
  private boolean forCompensation;

  /**
   * The loop type for the activity.
   */
  private LoopType loopType;

  /**
   * The start quantity for the activity.
   */
  private int startQuantity;

  /**
   * Constructs a new <code>Activity</code>.
   *
   * @param id                 the ID uniquely identifying the activity
   * @param forCompensation    is the activity for compensation
   * @param loopType           the loop type for the activity
   * @param startQuantity      the start quantity for the activity
   * @param completionQuantity the completion quantity for the activity
   */
  public Activity(String id, boolean forCompensation, LoopType loopType, int startQuantity,
      int completionQuantity)
  {
    super(id);

    this.forCompensation = forCompensation;
    this.loopType = loopType;
    this.startQuantity = startQuantity;
    this.completionQuantity = completionQuantity;
  }

  /**
   * Returns the completion quantity for the activity.
   *
   * @return the completion quantity for the activity
   */
  public int getCompletionQuantity()
  {
    return completionQuantity;
  }

  /**
   * Returns the loop type for the activity.
   *
   * @return the loop type for the activity
   */
  public LoopType getLoopType()
  {
    return loopType;
  }

  /**
   * Returns the start quantity for the activity.
   *
   * @return the start quantity for the activity
   */
  public int getStartQuantity()
  {
    return startQuantity;
  }

  /**
   * Returns <code>true</code> if the activity is for compensation or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the activity is for compensation or <code>false</code> otherwise
   */
  public boolean isForCompensation()
  {
    return forCompensation;
  }

  /**
   * Set the loop type for the activity.
   *
   * @param loopType the loop type for the activity
   */
  public void setLoopType(LoopType loopType)
  {
    this.loopType = loopType;
  }

  /**
   * Throw an error event.
   *
   * @param name the name of the error event
   */
  public void throwErrorEvent(String name)
  {
    throw new RuntimeException("NOT IMPLEMENTED");
  }

  /**
   * Throw an escalation event.
   *
   * @param name the name of the escalation event
   */
  public void throwEscalationEvent(String name)
  {
    throw new RuntimeException("NOT IMPLEMENTED");
  }
}
