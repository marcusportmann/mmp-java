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

import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserTask</code> class represents a BPMN
 * user task that forms part of a Process.
 * <p>
 * This task represents work that is performed by a human user with the help of the BPM engine or
 * another software application.
 * <p>
 * <b>User Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="userTask" type="tUserTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tUserTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="rendering" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="implementation" type="tImplementation" default="##unspecified"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class UserTask extends Task
{
  /**
   * The technology that the user task will use to send and receive messages.
   */
  private Implementation implementation;

  /**
   * Constructs a new <code>UserTask</code>.
   *
   * @param element the XML element containing the user task information
   */
  public UserTask(Element element)
  {
    super(element);

    try
    {
      this.implementation = Implementation.fromId(element.getAttribute("implementation"));

      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "rendering":
            {
              // TODO: Parse the rendering child element

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
      throw new ParserException("Failed to parse the user task XML data", e);
    }
  }

  /**
   * Execute the BPMN task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the technology that the user task will use to send and receive messages.
   *
   * @return the technology that the user task will use to send and receive messages
   */
  public Implementation getImplementation()
  {
    return implementation;
  }
}
