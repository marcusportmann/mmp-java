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

package guru.mmp.application.process.bpmn;

import guru.mmp.common.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>FlowNode</code> class provides the base class that all FlowNodes that form part of a
 * Process should be derived from.
 * <p/>
 * <b>FlowNode</b> XML schema:
 * <pre>
 * &lt;xsd:element name="flowNode" type="tFlowNode"/&gt;
 * &lt;xsd:complexType name="tFlowNode" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tFlowElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="incoming" type="xsd:QName" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="outgoing" type="xsd:QName" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class FlowNode
  extends FlowElement
{
  /**
   * The IDs uniquely identifying the incoming FlowElements for the FlowNode.
   */
  private List<QName> incomingFlowElementIds = new ArrayList<>();

  /**
   * The IDs uniquely identifying the incoming FlowElements for the FlowNode.
   */
  private List<QName> outgoingFlowElementIds = new ArrayList<>();

  /**
   * Constructs a new <code>FlowNode</code>.
   *
   * @param parent  the BPMN element that is the parent of this FlowNode
   * @param element the XML element containing the FlowNode information
   */
  protected FlowNode(BaseElement parent, Element element)
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
            case "incoming":
            {
              System.out.println("[DEBUG] id = " + element.getTextContent());

              addIncomingFlowElement(XmlUtils.getQName(childElement, element.getTextContent()));

              break;
            }

            case "outgoing":
            {
              System.out.println("[DEBUG] id = " + element.getTextContent());

              addOutgoingFlowElement(XmlUtils.getQName(childElement, element.getTextContent()));

              break;
            }

            default:
            {
              throw new ParserException(
                "Failed to parse the unknown XML element (" + childElement.getNodeName() + ")");
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the FlowNode XML data", e);
    }
  }

  /**
   * Add the ID uniquely identifying the incoming FlowElement for the FlowNode.
   *
   * @param id the ID uniquely identifying the incoming FlowElement for the FlowNode
   */
  public void addIncomingFlowElement(QName id)
  {
    incomingFlowElementIds.add(id);
  }

  /**
   * Add the ID uniquely identifying the outgoing FlowElement for the FlowNode.
   *
   * @param id the ID uniquely identifying the outgoing FlowElement for the FlowNode
   */
  public void addOutgoingFlowElement(QName id)
  {
    outgoingFlowElementIds.add(id);
  }

  /**
   * Returns the IDs uniquely identifying the incoming FlowElements for the FlowNode.
   *
   * @return IDs uniquely identifying the incoming FlowElements for the FlowNode
   */
  public List<QName> getIncomingFlowElementIds()
  {
    return incomingFlowElementIds;
  }

  /**
   * Returns the IDs uniquely identifying the incoming FlowElements for the FlowNode.
   *
   * @return the IDs uniquely identifying the incoming FlowElements for the FlowNode
   */
  public List<QName> getOutgoingFlowElementIds()
  {
    return outgoingFlowElementIds;
  }
}
