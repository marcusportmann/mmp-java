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
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;

/**
 * The <code>MessageEventDefinition</code> class represents a Message Event Definition that forms
 * part of a Process.
 * <p/>
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
@SuppressWarnings("unused")
public final class MessageEventDefinition
  extends EventDefinition
{
  /**
   * The reference to the message for the Message Event.
   */
  private QName messageRef;

  /**
   * The reference to the Operation that is used by the Message Event.
   * <p/>
   * It MUST be specified for executable Processes.
   */
  private QName operationRef;

  /**
   * Constructs a new <code>MessageEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Message Event Definition
   * @param element the XML element containing the message event definition information
   */
  public MessageEventDefinition(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      if (!StringUtil.isNullOrEmpty(element.getAttribute("messageRef")))
      {
        this.messageRef = XmlUtils.getQName(element, element.getAttribute("messageRef"));
      }

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
              if (!StringUtil.isNullOrEmpty(childElement.getTextContent()))
              {
                this.operationRef = XmlUtils.getQName(element, childElement.getTextContent());
              }

              break;
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
   * Returns the reference to the message for the Message Event.
   *
   * @return the reference to the message for the Message Event
   */
  public QName getMessageRef()
  {
    return messageRef;
  }

  /**
   * Returns the reference to the Operation that is used by the Message Event.
   * <p/>
   * It MUST be specified for executable Processes.
   *
   * @return the reference to the Operation that is used by the Message Event
   */
  public QName getOperationRef()
  {
    return operationRef;
  }
}
