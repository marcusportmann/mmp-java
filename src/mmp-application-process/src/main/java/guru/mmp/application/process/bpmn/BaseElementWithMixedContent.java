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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BaseElementWithMixedContent</code> class provides the base class that all BPMN
 * elements with mixed content that form part of a Process should be derived from.
 * <p/>
 * <b>BaseElementWithMixedContent</b> XML schema:
 * <pre>
 * &lt;xsd:element name="baseElementWithMixedContent" type="tBaseElementWithMixedContent"/&gt;
 * &lt;xsd:complexType name="tBaseElementWithMixedContent" abstract="true" mixed="true"&gt;
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
public abstract class BaseElementWithMixedContent
{
  /**
   * The ID uniquely identifying the BPMN element.
   */
  private QName id;

  /**
   * Constructs a new <code>BaseElementWithMixedContent</code>.
   *
   * @param element the XML element containing the BaseElementWithMixedContent information
   */
  protected BaseElementWithMixedContent(Element element)
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
      throw new ParserException("Failed to parse the BaseElementWithMixedContent XML data", e);
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
}
