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
import guru.mmp.application.process.bpmn.Expression;
import guru.mmp.application.process.bpmn.FormalExpression;
import guru.mmp.application.process.bpmn.ParserException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>ConditionalEventDefinition</code> class represents a Conditional Event Definition that
 * forms part of a Process.
 * <p/>
 * <b>Conditional Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="conditionalEventDefinition" type="tConditionalEventDefinition"
 *              substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tConditionalEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="condition" type="tExpression"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class ConditionalEventDefinition extends EventDefinition
{
  /**
   * The condition.
   * <p/>
   * The condition Expression might be underspecified and provided in the form of natural language.
   * For executable Processes (isExecutable = true), if the trigger is Conditional, then a
   * FormalExpression MUST be entered.
   */
  private Expression condition;

  /**
   * Constructs a new <code>ConditionalEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Conditional Event Definition
   * @param element the XML element containing the Conditional Event Definition information
   */
  public ConditionalEventDefinition(BaseElement parent, Element element)
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
            case "condition":
            {
              if (getProcess().isExecutable())
              {
                condition = new FormalExpression(childElement);
              }
              else
              {
                // TODO: Handle an underspecified expression possibly in the form of a natural
                // language
              }

              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Conditional Event Definition XML data", e);
    }
  }

  /**
   * Returns the condition.
   * <p/>
   * The condition Expression might be underspecified and provided in the form of natural language.
   * For executable Processes (isExecutable = true), if the trigger is Conditional, then a
   * FormalExpression MUST be entered.
   *
   * @return the condition
   */
  public Expression getCondition()
  {
    return condition;
  }
}
