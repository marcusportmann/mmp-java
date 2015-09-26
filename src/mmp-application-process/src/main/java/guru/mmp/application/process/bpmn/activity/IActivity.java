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

import guru.mmp.application.process.bpmn.IFlowNode;

/**
 * The <code>IActivity</code> interface defines the interface that must be implemented by a
 * Business Process Model and Notation (BPMN) activity subclass.
 * <p>
 * An activity represents a unit of work performed. A step inside a process. It has a defined start
 * and end and generally requires some kind of input to produce an output.
 *
 * @author Marcus Portmann
 */
public interface IActivity extends IFlowNode
{
  /**
   * Returns the loop type for the activity.
   *
   * @return the loop type for the activity
   */
  LoopType getLoopType();

  /**
   * Set the loop type for the activity.
   *
   * @param loopType the loop type for the activity
   */
  void setLoopType(LoopType loopType);
}
