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
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * The <code>LinkEventDefinition</code> class represents a Link Event Definition that forms part
 * of a Process.
 * <p>
 * <b>Link Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="linkEventDefinition" type="tLinkEventDefinition"
 *                 substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tLinkEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="source" type="xsd:QName" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="target" type="xsd:QName" minOccurs="0" maxOccurs="1"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="name" type="xsd:string" use="required"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class LinkEventDefinition extends EventDefinition
{
  /**
   * The name of the link that triggered the event.
   */
  private String name;

  /**
   * The references to the corresponding 'catch' or 'target' Link Event Definition(s), when this
   * Link Event Definition represents a 'throw' or 'source' Link Event Definition.
   */
  private List<QName> sourceRefs = new ArrayList<>();

  /**
   * The reference to the corresponding 'throw' or 'source' Link Event Definition, when this Link
   * Event Definition represents a 'catch' or 'target' Link Event Definition.
   */
  private QName targetRef;

  /**
   * Constructs a new <code>LinkEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Link Event Definition
   * @param element the XML element containing the Link Event Definition information
   */
  public LinkEventDefinition(BaseElement parent, Element element)
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
            case "source":
            {
              sourceRefs.add(XmlUtils.getQName(childElement, childElement.getTextContent()));

              break;
            }

            case "target":
            {
              targetRef = XmlUtils.getQName(childElement, childElement.getTextContent());

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
      throw new ParserException("Failed to parse the Link Event Definition XML data", e);
    }
  }

  /**
   * Returns the name of the link that triggered the event.
   *
   * @return the name of the link that triggered the event
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the references to the corresponding 'catch' or 'target' Link Event Definition(s), when
   * this Link Event Definition represents a 'throw' or 'source' Link Event Definition.
   *
   * @return the references to the corresponding 'catch' or 'target' Link Event Definition(s), when
   *         this Link Event Definition represents a 'throw' or 'source' Link Event Definition
   */
  public List<QName> getSourceRefs()
  {
    return sourceRefs;
  }

  /**
   * Returns the reference to the corresponding 'throw' or 'source' Link Event Definition, when
   * this Link Event Definition represents a 'catch' or 'target' Link Event Definition.
   *
   * @return the reference to the corresponding 'throw' or 'source' Link Event Definition, when
   *         this Link Event Definition represents a 'catch' or 'target' Link Event Definition
   */
  public QName getTargetRef()
  {
    return targetRef;
  }
}
