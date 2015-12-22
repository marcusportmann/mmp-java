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

import guru.mmp.application.process.bpmn.*;
import guru.mmp.common.util.StringUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

/**
 * The <code>SubProcess</code> class represents a Sub-Process that forms part of a Process.
 * <p>
 * A Sub-Process has parts that are modeled in a child-level process, a process with its own
 * activity flow and start and end states.
 * <p>
 * A Sub-Process can be triggered by an event making it an event Sub-Process. An event Sub-Process
 * is not part of the normal flow of the Process. Instead, it is triggered by one of the following
 * events:
 * <ul>
 *   <li>Message</li>
 *   <li>Timer</li>
 *   <li>Multiple</li>
 *   <li>Multiple-Parallel</li>
 *   <li>Conditional</li>
 *   <li>Signal</li>
 *   <li>Escalation</li>
 * </ul>
 * <p>
 * <b>Sub-Process</b> XML schema:
 * <pre>
 * &lt;xsd:element name="subProcess" type="tSubProcess" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tSubProcess"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tActivity"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="laneSet" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="flowElement" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="artifact" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="triggeredByEvent" type="xsd:boolean" default="false"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SubProcess extends Activity
{
  /**
   * The FlowElements for the Sub-Process.
   */
  private Map<QName, FlowElement> flowElements = new ConcurrentHashMap<>();

  /**
   * Is the Sub-Process triggered by an event?
   */
  private boolean triggeredByEvent;

  /**
   * Constructs a new <code>Sub-Process</code>.
   *
   * @param parent  the BPMN element that is the parent of this Sub-Process
   * @param element the XML element containing the Sub-Process information
   */
  public SubProcess(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.triggeredByEvent = !StringUtil.isNullOrEmpty(element.getAttribute("triggeredByEvent"))
          && Boolean.parseBoolean(element.getAttribute("triggeredByEvent"));

      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "laneSet":
            {
              break;
            }

            case "artifact":
            {
              break;
            }

            // Flow elements
            case "businessRuleTask":
            {
              addFlowElement(new BusinessRuleTask(this, childElement));

              break;
            }

            case "manualTask":
            {
              addFlowElement(new ManualTask(this, childElement));

              break;
            }

            case "receiveTask":
            {
              addFlowElement(new ReceiveTask(this, childElement));

              break;
            }

            case "scriptTask":
            {
              addFlowElement(new ScriptTask(this, childElement));

              break;
            }

            case "sendTask":
            {
              addFlowElement(new SendTask(this, childElement));

              break;
            }

            case "serviceTask":
            {
              addFlowElement(new ServiceTask(this, childElement));

              break;
            }

            case "task":
            {
              addFlowElement(new DefaultTask(this, childElement));

              break;
            }

            case "userTask":
            {
              addFlowElement(new UserTask(this, childElement));

              break;
            }

            case "sequenceFlow":
            {
              addFlowElement(new SequenceFlow(this, childElement));

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
      throw new ParserException("Failed to parse the Sub-Process XML data", e);
    }
  }

  /**
   * Add the flow element to the Sub-Process.
   *
   * @param flowElement the flow element to add to the Sub-Process
   */
  public void addFlowElement(FlowElement flowElement)
  {
    flowElements.put(flowElement.getId(), flowElement);
  }

  /**
   * Execute the Sub-Process.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Sub-Process
   */
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the flow element.
   *
   * @param id the ID uniquely identifying the flow element
   *
   * @return the flow element or <code>null</code> if the flow element could
   *         not be found
   */
  public FlowElement getFlowElement(QName id)
  {
    return flowElements.get(id);
  }

  /**
   * Returns the FlowElements for the Sub-Process.
   *
   * @return the FlowElements for the Sub-Process
   */
  public Collection<FlowElement> getFlowElements()
  {
    return flowElements.values();
  }

  /**
   * Returns whether the Sub-Process is triggered by an event.
   *
   * @return <code>true</code> if the Sub-Process is triggered by an event or <code>false</code>
   *         otherwise
   */
  public boolean isTriggeredByEvent()
  {
    return triggeredByEvent;
  }
}
