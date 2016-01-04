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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>Assignment</code> class represents an Assignment that forms part of a Process.
 * <p>
 * The Assignment class is used to specify a simple mapping of data elements using a specified
 * Expression language.
 * <p>
 * The default Expression language for all Expressions is specified in the Definitions element,
 * using the expressionLanguage attribute. It can also be overridden on each individual Assignment
 * using the same attribute.
 * <p>
 * <b>Assignment</b> XML schema:
 * <pre>
 * &lt;xsd:element name="assignment" type="tAssignment" /&gt;
 * &lt;xsd:complexType name="tAssignment"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tBaseElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="from" type="tExpression" minOccurs="1" maxOccurs="1"/&gt;
 *         &lt;xsd:element name="to" type="tExpression" minOccurs="1" maxOccurs="1"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class Assignment extends BaseElement
{
  /**
   * The Expression that evaluates the source of the Assignment.
   */
  private Expression from;

  /**
   * The Expression that defines the actual Assignment operation and the target data element.
   */
  private Expression to;

  /**
   * Constructs a new <code>Assignment</code>.
   *
   * @param parent  the BPMN element that is the parent of this BPMN element
   * @param element the XML element containing the Assignment information
   */
  public Assignment(BaseElement parent, Element element)
  {
    super(parent, element);

    if (true)
    {
      throw new RuntimeException("TODO: IMPLEMENT ME");
    }

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
            case "from":
            {
              //from = new Expression(this, childElement);

              break;
            }

            case "to":
            {
              //to = new Expression(this, childElement);

              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Assignment XML data", e);
    }
  }

  /**
   * Returns the Expression that evaluates the source of the Assignment.
   *
   * @return the Expression that evaluates the source of the Assignment
   */
  public Expression getFrom()
  {
    return from;
  }

  /**
   * Returns the Expression that defines the actual Assignment operation and the target data
   * element.
   *
   * @return the Expression that defines the actual Assignment operation and the target data
   *         element
   */
  public Expression getTo()
  {
    return to;
  }
}
