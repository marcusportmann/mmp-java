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

import guru.mmp.application.process.bpmn.ParserException;

import org.w3c.dom.Element;

/**
 * The <code>CompensationEventDefinition</code> class stores the details for a Business Process
 * Model and Notation (BPMN) compensate event that forms part of a Process.
 * <p>
 * <b>Compensate Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="compensateEventDefinition" type="tCompensateEventDefinition"
 *              substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tCompensateEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:attribute name="waitForCompletion" type="xsd:boolean"/&gt;
 *       &lt;xsd:attribute name="activityRef" type="xsd:QName"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class CompensateEventDefinition extends EventDefinition
{
  private boolean waitForCompletion;

  /**
   * Constructs a new <code>CompensateEventDefinition</code>.
   *
   * @param element the XML element containing the compensate event definition information
   */
  public CompensateEventDefinition(Element element)
  {
    super(element);

    try
    {
      this.waitForCompletion = Boolean.parseBoolean(element.getAttribute("waitForCompletion"));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the compensate event definition XML data", e);
    }
  }
}
