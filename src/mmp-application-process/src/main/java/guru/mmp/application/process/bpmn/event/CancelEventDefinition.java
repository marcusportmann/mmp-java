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

import org.w3c.dom.Element;

/**
 * The <code>CancelEventDefinition</code> class stores the details for a Business Process Model
 * and Notation (BPMN) cancel event that forms part of a Process.
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
public final class CancelEventDefinition extends EventDefinition
{
  /**
   * Constructs a new <code>CancelEventDefinition</code>.
   *
   * @param element the XML element containing the cancel event definition information
   */
  public CancelEventDefinition(Element element)
  {
    super(element);
  }
}
