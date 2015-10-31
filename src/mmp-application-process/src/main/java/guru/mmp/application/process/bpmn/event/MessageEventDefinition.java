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
import guru.mmp.common.util.StringUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>MessageEventDefinition</code> class stores the details for a Business Process
 * Model and Notation (BPMN) message event that forms part of a Process.
 * <p>
 * <b>Message Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="messageEventDefinition" type="tMessageEventDefinition"
 *              substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tMessageEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="operationRef" type="xsd:QName" minOccurs="0" maxOccurs="1"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="messageRef" type="xsd:QName"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class MessageEventDefinition extends EventDefinition
{
  /**
   * The ID of the message.
   */
  private String messageRef;

  /**
   * The optional ID of the operation;
   */
  private String operationRef;

  /**
   * Returns the optional ID of the operation.
   *
   * @return the optional ID of the operation
   */
  public String getOperationRef()
  {
    return operationRef;
  }

  /**
   * Constructs a new <code>MessageEventDefinition</code>.
   *
   * @param element the XML element containing the message event definition information
   */
  public MessageEventDefinition(Element element)
  {
    super(element);

    try
    {
      this.messageRef = StringUtil.notNull(element.getAttribute("messageRef"));

      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "operationRef":
            {

              // TODO: Parse the operationRef child element

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
      throw new ParserException("Failed to parse the message event definition XML data", e);
    }
  }

  /**
   * Returns the ID of the message.
   *
   * @return the ID of the message
   */
  public String getMessageRef()
  {
    return messageRef;
  }
}
