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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>EscalationEventDefinition</code> class stores the details for a Business Process
 * Model and Notation (BPMN) escalation event that forms part of a Process.
 * <p>
 * <b>Escalation Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="escalationEventDefinition" type="tEscalationEventDefinition"
 *              substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tEscalationEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:attribute name="escalationRef" type="xsd:QName"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class EscalationEventDefinition extends EventDefinition
{
  /**
   * Constructs a new <code>EscalationEventDefinition</code>.
   *
   * @param element the XML element containing the escalation event definition information
   */
  public EscalationEventDefinition(Element element)
  {
    super(element);

    try
    {
      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "escalationRef":
            {
              // TODO: Parse the escalationRef child element

              break;
            }

            default:
            {
              throw new ParserException("Failed to parse the unknown XML element ("
                  + childElement.getNodeName() + ")");
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the escalation event definition XML data", e);
    }
  }
}
