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
import guru.mmp.common.xml.XmlUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.namespace.QName;

/**
 * The <code>BaseElement</code> class provides the base class that all BPMN elements that form part
 * of a Process should be derived from.
 * <p>
 * <b>BaseElement</b> XML schema:
 * <pre>
 * &lt;xsd:element name="baseElement" type="tBaseElement"/&gt;
 * &lt;xsd:complexType name="tBaseElement" abstract="true"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element ref="documentation" minOccurs="0" maxOccurs="unbounded"/&gt;
 *     &lt;xsd:element ref="extensionElements" minOccurs="0" maxOccurs="1"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="id" type="xsd:ID" use="optional"/&gt;
 *   &lt;xsd:anyAttribute namespace="##other" processContents="lax"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class BaseElement
{
  /**
   * The ID uniquely identifying the BPMN element.
   */
  private QName id;

  /**
   * The BPMN element that is the parent of this BPMN element or <code>null</code> if the BPMN
   * element does not have a parent.
   */
  private BaseElement parent;

  /**
   * Constructs a new <code>BaseElement</code>.
   *
   * @param parent  the BPMN element that is the parent of this BPMN element
   * @param element the XML element containing the BaseElement information
   */
  protected BaseElement(BaseElement parent, Element element)
  {
    try
    {
      this.id = XmlUtils.getQName(element, StringUtil.notNull(element.getAttribute("id")));

      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "documentation":
            {
              // TODO: Parse the documentation child element
              break;
            }

            case "extensionElements":
            {
              // TODO: Parse the extensionElements child element
              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the BaseElement XML data", e);
    }
  }

  /**
   * Returns the ID uniquely identifying the BPMN element.
   *
   * @return the ID uniquely identifying the BPMN element
   */
  public QName getId()
  {
    return id;
  }

  /**
   * The BPMN element that is the parent of this BPMN element or <code>null</code> if the BPMN
   * element does not have a parent.
   *
   * @return the BPMN element that is the parent of this BPMN element or <code>null</code> if the
   *         BPMN element does not have a parent
   */
  public BaseElement getParent()
  {
    return parent;
  }
}
