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

import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>CallableElement</code> class provides the base class that all CallableElements that
 * form part of a Process should be derived from.
 * <p/>
 * <b>CallableElement</b> XML schema:
 * <pre>
 * &lt;xsd:element name="callableElement" type="tCallableElement"/&gt;
 * &lt;xsd:complexType name="tCallableElement"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tRootElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="supportedInterfaceRef" type="xsd:QName" minOccurs="0"
 *                      maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="ioSpecification" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="ioBinding" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="name" type="xsd:string"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class CallableElement
  extends RootElement
{
  /**
   * The InputOutputBinding, which defines a combination of one InputSet and one OutputSet in order
   * to bind this to an operation defined in an interface.
   */
  private InputOutputBinding inputOutputBinding;

  /**
   * The InputOutputSpecification, which defines the inputs and outputs and the
   * InputSets and OutputSets for the CallableElement.
   */
  private InputOutputSpecification inputOutputSpecification;

  /**
   * The name of the CallableElement.
   */
  private String name;

  /**
   * The IDs uniquely identifying the Interfaces describing the external behavior provided by this
   * element.
   */
  private List<QName> supportedInterfaceIds = new ArrayList<>();

  /**
   * Constructs a new <code>CallableElement</code>.
   *
   * @param parent  the BPMN element that is the parent of this CallableElement
   * @param element the XML element containing the CallableElement information
   */
  protected CallableElement(BaseElement parent, Element element)
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
            case "supportedInterfaceRef":
            {
              supportedInterfaceIds.add(
                XmlUtils.getQName(childElement, childElement.getTextContent()));

              break;
            }

            case "ioSpecification":
            {
              inputOutputSpecification = new InputOutputSpecification(childElement);

              break;
            }

            case "ioBinding":
            {
              inputOutputBinding = new InputOutputBinding(childElement);

              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the CallableElement XML data", e);
    }
  }

  /**
   * Returns the InputOutputBinding, which defines a combination of one InputSet and one OutputSet
   * in order to bind this to an operation defined in an interface.
   *
   * @return the InputOutputBinding, which defines a combination of one InputSet and one OutputSet
   * in order to bind this to an operation defined in an interface
   */
  public InputOutputBinding getInputOutputBinding()
  {
    return inputOutputBinding;
  }

  /**
   * Returns the InputOutputSpecification, which defines the inputs and outputs and the InputSets
   * and OutputSets for the CallableElement.
   *
   * @return the InputOutputSpecification, which defines the inputs and outputs and the InputSets
   * and OutputSets for the CallableElement
   */
  public InputOutputSpecification getInputOutputSpecification()
  {
    return inputOutputSpecification;
  }

  /**
   * Returns the name of the CallableElement.
   *
   * @return the name of the CallableElement
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the IDs uniquely identifying the Interfaces describing the external behavior provided
   * by this element.
   *
   * @return the IDs uniquely identifying the Interfaces describing the external behavior provided
   * by this element
   */
  public List<QName> getSupportedInterfaceIds()
  {
    return supportedInterfaceIds;
  }
}
