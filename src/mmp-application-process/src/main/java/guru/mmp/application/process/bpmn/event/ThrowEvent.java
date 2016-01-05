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

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ThrowEvent</code> class provides the base class that all Throw Event subclasses should
 * be derived from.
 * <p/>
 * <b>Throw Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="throwEvent" type="tThrowEvent"/&gt;
 * &lt;xsd:complexType name="tThrowEvent" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEvent"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="dataInput" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="dataInputAssociation" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="inputSet" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="eventDefinition" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="eventDefinitionRef" type="xsd:QName" minOccurs="0"
 *                         maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class ThrowEvent
  extends Event
{
  /**
   * Constructs a new <code>ThrowEvent</code>.
   *
   * @param parent  the BPMN element that is the parent of this Throw Event
   * @param element the XML element containing the Throw Event information
   */
  protected ThrowEvent(BaseElement parent, Element element)
  {
    super(parent, element);

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
            case "dataInput":
            {
              // TODO: Parse the dataInput child element

              break;
            }

            case "dataInputAssociation":
            {
              // TODO: Parse the dataInputAssociation child element

              break;
            }

            case "inputSet":
            {
              // TODO: Parse the inputSet child element

              break;
            }

            case "eventDefinition":
            {
              // TODO: Parse the eventDefinition child element

              break;
            }

            case "eventDefinitionRef":
            {
              // TODO: Parse the eventDefinitionRef child element

              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Throw Event XML data", e);
    }
  }

  /**
   * Execute the Throw Event.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Throw Event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
