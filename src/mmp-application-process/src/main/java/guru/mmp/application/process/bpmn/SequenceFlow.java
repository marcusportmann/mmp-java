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

package guru.mmp.application.process.bpmn;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.FlowNode;

/**
 * The <code>SequenceFlow</code> class represents a Business Process Model and Notation (BPMN)
 * sequence flow that forms part of a BPMN process.
 *
 * @author Marcus Portmann
 */
public final class SequenceFlow extends FlowElement
{
  /**
   * Constructs a new <code>SequenceFlow</code>.
   *
   * @param id   the ID uniquely identifying the sequence flow
   * @param name the name of the sequence flow
   */
  public SequenceFlow(String id, String name)
  {
    super(id, name);
  }
}

