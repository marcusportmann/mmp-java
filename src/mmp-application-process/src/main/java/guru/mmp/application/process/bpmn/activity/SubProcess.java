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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SubProcess</code> class represents a  BPMN
 * sub-process.
 * <p>
 * A sub-process has parts that are modeled in a child-level process, a process with its own
 * activity flow and start and end states.
 * <p>
 * A sub-process can be triggered by an event making it an event sub-process. An event sub-process
 * is not part of the normal flow of the process. Instead, it is triggered by one of the following
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
public class SubProcess extends Activity
{
  /**
   * The FlowElements for the sub-process.
   */
  private Map<String, FlowElement> flowElements = new ConcurrentHashMap<>();

  /**
   * Is the sub-process triggered by an event?
   */
  private boolean triggeredByEvent;

  /**
   * Constructs a new <code>SubProcess</code>.
   *
   * @param element the XML element containing the sub-process information
   */
  public SubProcess(Element element)
  {
    super(element);

    try
    {
      if (StringUtil.isNullOrEmpty(element.getAttribute("triggeredByEvent")))
      {
        this.triggeredByEvent = false;
      }
      else
      {
        this.triggeredByEvent = Boolean.parseBoolean(element.getAttribute("triggeredByEvent"));
      }

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
              addFlowElement(new BusinessRuleTask(childElement));

              break;
            }

            case "manualTask":
            {
              addFlowElement(new ManualTask(childElement));

              break;
            }

            case "receiveTask":
            {
              addFlowElement(new ReceiveTask(childElement));

              break;
            }

            case "scriptTask":
            {
              addFlowElement(new ScriptTask(childElement));

              break;
            }

            case "sendTask":
            {
              addFlowElement(new SendTask(childElement));

              break;
            }

            case "serviceTask":
            {
              addFlowElement(new ServiceTask(childElement));

              break;
            }

            case "task":
            {
              addFlowElement(new DefaultTask(childElement));

              break;
            }

            case "userTask":
            {
              addFlowElement(new UserTask(childElement));

              break;
            }

            case "sequenceFlow":
            {
              addFlowElement(new SequenceFlow(childElement));

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
      throw new ParserException("Failed to parse the sub-process XML data", e);
    }
  }

  /**
   * Add the flow element to the sub-process.
   *
   * @param flowElement the flow element to add to the sub-process
   */
  public void addFlowElement(FlowElement flowElement)
  {
    flowElements.put(flowElement.getId(), flowElement);
  }

  /**
   * Execute the BPMN sub-process.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) sub-process
   */
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the flow element with the specified ID.
   *
   * @param id the ID uniquely identifying the flow element
   *
   * @return the flow element with the specified ID or <code>null</code> if the flow element could
   *         not be found
   */
  public FlowElement getFlowElement(String id)
  {
    return flowElements.get(id);
  }

  /**
   * Returns the FlowElements for the sub-process.
   *
   * @return the FlowElements for the sub-process
   */
  public Collection<FlowElement> getFlowElements()
  {
    return flowElements.values();
  }

  /**
   * Returns whether the sub-process is triggered by an event.
   *
   * @return <code>true</code> if the sub-process is triggered by an event or <code>false</code>
   *         otherwise
   */
  public boolean isTriggeredByEvent()
  {
    return triggeredByEvent;
  }
}
