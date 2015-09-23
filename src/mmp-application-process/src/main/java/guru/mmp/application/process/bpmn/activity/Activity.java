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

import guru.mmp.application.process.bpmn.Element;

/**
 * The <code>Activity</code> class provides the base class that all
 * Business Process Model and Notation (BPMN) activity subclasses should be derived from.
 * <p>
 * An activity represents a unit of work performed. A step inside a process. It has a defined start
 * and end and generally requires some kind of input to produce an output.
 *
 * @author Marcus Portmann
 */
abstract class Activity extends Element
  implements IActivity
{
  /**
   * The loop type for the activity.
   */
  private LoopType loopType;

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
   * Set the loop type for the activity.
   *
   * @param loopType the loop type for the activity
   */
  public void setLoopType(LoopType loopType)
  {
    this.loopType = loopType;
  }
}
