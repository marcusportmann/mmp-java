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

package guru.mmp.application.process.bpmn.activity;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.CallableElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>GlobalTask</code> class represents a BPMN global
 * task.
 * <p>
 * <b>Global Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="globalTask" type="tGlobalTask" substitutionGroup="rootElement"/&gt;
 * &lt;xsd:complexType name="tGlobalTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tCallableElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="resourceRole" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class GlobalTask extends CallableElement
{
  /**
   * Constructs a new <code>GlobalTask</code>.
   *
   * @param element the XML element containing the global task information
   */
  protected GlobalTask(Element element)
  {
    super(element);

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
            case "resourceRole":
            {
              // TODO: Parse the resourceRole child element

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
      throw new ParserException("Failed to parse the global task XML data", e);
    }
  }

  /**
   * Execute the BPMN global task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) global task
   */
  public abstract List<Token> execute(ProcessExecutionContext context);
}
