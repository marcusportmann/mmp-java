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

package guru.mmp.application.process.bpmn.activity;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>GlobalTask</code> class represents a Global Task that forms part of a Process.
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
@SuppressWarnings("unused")
public abstract class GlobalTask extends CallableElement
{
  /**
   * The resources that will perform or will be responsible for the Global Task.
   * <p>
   * In the case where the Call Activity that references this Global Task defines its own resources,
   * they will override the ones defined here.
   */
  private List<String> resources = new ArrayList<>();

  /**
   * Constructs a new <code>GlobalTask</code>.
   *
   * @param parent  the BPMN element that is the parent of this Global Task
   * @param element the XML element containing the Global Task information
   */
  protected GlobalTask(BaseElement parent, Element element)
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
            case "resourceRole":
            {
              resources.add(childElement.getTextContent());

              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Global Task XML data", e);
    }
  }

  /**
   * Execute the Global Task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Global Task
   */
  public abstract List<Token> execute(ProcessExecutionContext context);

  /**
   * Returns the resources that will perform or will be responsible for the Global Task.
   *
   * @return the resources that will perform or will be responsible for the Global Task
   */
  public List<String> getResources()
  {
    return resources;
  }
}
