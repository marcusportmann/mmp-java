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

import guru.mmp.application.process.bpmn.BaseElement;

import org.w3c.dom.Element;

/**
 * The <code>CancelEventDefinition</code> class represents a Cancel Event Definition that forms part
 * of a Process.
 * <p>
 * <b>Cancel Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="cancelEventDefinition" type="tCancelEventDefinition"
 *                 substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tCancelEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class CancelEventDefinition extends EventDefinition
{
  /**
   * Constructs a new <code>CancelEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Cancel Event Definition
   * @param element the XML element containing the Cancel Event Definition information
   */
  public CancelEventDefinition(BaseElement parent, Element element)
  {
    super(parent, element);
  }
}
