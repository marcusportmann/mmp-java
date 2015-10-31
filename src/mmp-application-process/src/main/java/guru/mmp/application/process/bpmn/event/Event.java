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

import guru.mmp.application.process.bpmn.FlowNode;
import org.w3c.dom.Element;

/**
 * The <code>Event</code> class provides the base class that all
 * BPMN event subclasses should be derived from.
 * <p>
 * An event is something important to the process from a business perspective that just happens.
 * <p>
 * Events can be classified by their:
 * <ul>
 *   <li>Trigger</li>
 *   <li>Behavior</li>
 *   <li>Type</li>
 * </ul>
 * <p>
 * The event trigger describes why the event is fired e.g. after receiving a message, as a result
 * of a time condition, etc.
 * <p>
 * An event can exhibit four different types of behaviour.
 * <ul>
 *   <li>
 *     <b>Throwing</b>: The event waits for a token to throw a trigger as a result of the event.
 *     (represented by a solid symbol in a BPMN diagram)
 *   </li>
 *   <li>
 *     <b>Catching</b>: The event waits for its trigger and then emits a token.
 *     (represented by a outlined symbol in a BPMN diagram)
 *   </li>
 *   <li>
 *     <b>Interrupting</b>: The activity terminates and and the flow of the process continues from
 *     the catching event (represented by a solid boundary in a BPMN diagram).
 *   </li>
 *   <li>
 *     <b>Non-interrupting</b>: The activity continues and the flow at the catching event executes
 *     in parallel (represented by a dashed boundary in a BPMN diagram).
 *   </li>
 * </ul>
 * <p>
 * <b>Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="event" type="tEvent" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tEvent" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tFlowNode"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class Event extends FlowNode
{
  /**
   * Constructs a new <code>Event</code>.
   *
   * @param element the XML element containing the event information
   */
  protected Event(Element element)
  {
    super(element);
  }
}
