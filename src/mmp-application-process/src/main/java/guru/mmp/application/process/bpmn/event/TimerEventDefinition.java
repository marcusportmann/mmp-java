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
import guru.mmp.application.process.bpmn.ParserException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>TimerEventDefinition</code> class represents a Timer Event Definition that
 * forms part of a Process.
 * <p/>
 * <b>Timer Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="timerEventDefinition" type="tTimerEventDefinition"
 *              substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tTimerEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:choice&gt;
 *         &lt;xsd:element name="timeDate" type="tExpression" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element name="timeDuration" type="tExpression" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element name="timeCycle" type="tExpression" minOccurs="0" maxOccurs="1"/&gt;
 *       &lt;/xsd:choice&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class TimerEventDefinition extends EventDefinition
{
  /**
   * Constructs a new <code>TimerEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Timer Event Definition
   * @param element the XML element containing the timer event definition information
   */
  public TimerEventDefinition(BaseElement parent, Element element)
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
            case "timeDate":
            {
              // TODO: Parse the timeDate child element

              break;
            }

            case "timeDuration":
            {
              // TODO: Parse the timeDuration child element

              break;
            }

            case "timeCycle":
            {
              // TODO: Parse the timeCycle child element

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
      throw new ParserException("Failed to parse the Timer Event Definition XML data", e);
    }
  }
}
