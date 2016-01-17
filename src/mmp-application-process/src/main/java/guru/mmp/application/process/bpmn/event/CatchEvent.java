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
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import guru.mmp.common.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CatchEvent</code> class provides the base class that all Catch Event subclasses should
 * be derived from.
 * <p/>
 * <b>Catch Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="catchEvent" type="tCatchEvent"/&gt;
 * &lt;xsd:complexType name="tCatchEvent" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEvent"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="dataOutput" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="dataOutputAssociation" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="outputSet" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="eventDefinition" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="eventDefinitionRef" type="xsd:QName" minOccurs="0"
 *                      maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="parallelMultiple" type="xsd:boolean" default="false"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class CatchEvent extends Event
{
  /**
   * Must all of the types of triggers that are listed in the Catch Event must be triggered before
   * the Process is instantiated.
   */
  private boolean isParallelMultiple;

  /**
   * Constructs a new <code>CatchEvent</code>.
   *
   * @param parent  the BPMN element that is the parent of this Catch Event
   * @param element the XML element containing the Catch Event information
   */
  protected CatchEvent(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.isParallelMultiple = !StringUtil.isNullOrEmpty(element.getAttribute("parallelMultiple"))
          && Boolean.parseBoolean(element.getAttribute("parallelMultiple"));

      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "dataOutput":
            {
              // TODO: Parse the dataOutput child element

              break;
            }

            case "dataOutputAssociation":
            {
              // TODO: Parse the dataOutputAssociation child element

              break;
            }

            case "outputSet":
            {
              // TODO: Parse the outputSet child element

              break;
            }

            case "eventDefinition":
            {
              // TODO: Parse the eventDefinition child element

              break;
            }

            case "eventDefinitionRef":
            {
              // TODO: Parse the eventDefinitionRef child element

              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Catch Event XML data", e);
    }
  }

  /**
   * Execute the Catch Event.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Catch Event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns <code>true</code> if all of the types of triggers that are listed in the Catch Event
   * must be triggered before the Process is instantiated or <code>false</code> otherwise.
   *
   * @return <code>true</code> if all of the types of triggers that are listed in the Catch Event
   * must be triggered before the Process is instantiated or <code>false</code> otherwise
   */
  public boolean isParallelMultiple()
  {
    return isParallelMultiple;
  }
}
