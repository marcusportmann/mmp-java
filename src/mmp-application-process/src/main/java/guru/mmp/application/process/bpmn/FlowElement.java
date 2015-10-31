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

package guru.mmp.application.process.bpmn;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>FlowElement</code> class provides the base class that all FlowElements that form part
 * of a Process should be derived from.
 * <p>
 * <b>FlowElement</b> XML schema:
 * <pre>
 * &lt;xsd:element name="flowElement" type="tFlowElement"/&gt;
 * &lt;xsd:complexType name="tFlowElement" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tBaseElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="auditing" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="monitoring" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element name="categoryValueRef" type="xsd:QName" minOccurs="0"
 *                      maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="name" type="xsd:string"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class FlowElement extends BaseElement
{
  /**
   * The name of the FlowElement.
   */
  private String name;

  /**
   * Constructs a new <code>FlowElement</code>.
   *
   * @param parent  the BPMN element that is the parent of this FlowElement
   * @param element the XML element containing the FlowElement information
   */
  protected FlowElement(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.name = StringUtil.notNull(element.getAttribute("name"));

      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "auditing":
            {
              // TODO: Parse the auditing child element

              break;
            }

            case "monitoring":
            {
              // TODO: Parse the monitoring child element

              break;
            }

            case "categoryValueRef":
            {
              // TODO: Parse the categoryValueRef child element
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the FlowElement XML data", e);
    }
  }

  /**
   * Execute the FlowElement.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the FlowElement
   */
  public abstract List<Token> execute(ProcessExecutionContext context);

  /**
   * Returns the name of the FlowElement.
   *
   * @return the name of the FlowElement
   */
  public String getName()
  {
    return name;
  }
}
