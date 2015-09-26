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

package guru.mmp.application.process.bpmn.gateway;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.FlowNode;

/**
 * The <code>Gateway</code> class provides the base class that all
 * Business Process Model and Notation (BPMN) gateway subclasses should be derived from.
 * <p>
 * Gateways are objects that control the flow of the process instead of performing some action.
 * <p>
 * Gateways have two behaviors:
 * <ul>
 *   <li><b>Converging</b>, that refers to what they do to the incoming flows.</li>
 *   <li><b>Diverging</b>, what they do to outgoing flows.</li>
 * </ul>
 * There are five types of gateways:
 * <ol>
 *   <li>Exclusive</li>
 *   <li>Parallel</li>
 *   <li>Event</li>
 *   <li>Inclusive</li>
 *   <li>Complex</li>
 * </ol>
 *
 * @author Marcus Portmann
 */
abstract class Gateway extends FlowNode
  implements IGateway
{
  /**
   * Constructs a new <code>Gateway</code>.
   *
   * @param id the ID uniquely identifying the gateway
   */
  public Gateway(String id)
  {
    super(id);
  }
}
