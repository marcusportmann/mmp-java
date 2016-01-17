/*
 * Copyright 2016 Marcus Portmann
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

import guru.mmp.application.process.bpmn.BaseElement;
import org.w3c.dom.Element;

/**
 * The <code>TerminateEventDefinition</code> class represents a Terminate Event Definition that
 * forms part of a Process.
 * <p/>
 * <b>Terminate Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="terminateEventDefinition" type="tTerminateEventDefinition"
 *              substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tTerminateEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class TerminateEventDefinition extends EventDefinition
{
  /**
   * Constructs a new <code>TerminateEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Terminate Event Definition
   * @param element the XML element containing the Terminate Event Definition information
   */
  public TerminateEventDefinition(BaseElement parent, Element element)
  {
    super(parent, element);
  }
}
