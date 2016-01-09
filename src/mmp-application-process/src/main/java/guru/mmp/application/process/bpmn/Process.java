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

import guru.mmp.application.process.bpmn.activity.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>Process</code> class represents a Process.
 * <p/>
 * <b>Process</b> XML schema:
 * <pre>
 * &lt;xsd:element name="process" type="tProcess" substitutionGroup="rootElement"/&gt;
 * &lt;xsd:complexType name="tProcess"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tCallableElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="auditing" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="monitoring" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="processRole" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="property" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="laneSet" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="flowElement" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="artifact" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="resourceRole" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="correlationSubcription" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="supports" type="xsd:QName" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="processType" type="tProcessType" default="None"/&gt;
 *       &lt;xsd:attribute name="isExecutable" type="xsd:boolean"use="optional"/&gt;
 *       &lt;xsd:attribute name="isClosed" type="xsd:boolean" default="false"/&gt;
 *       &lt;xsd:attribute name="definitionalCollaborationRef" type="xsd:QName" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class Process
  extends CallableElement
{
  /**
   * The FlowElements for the process.
   */
  private Map<QName, FlowElement> flowElements = new ConcurrentHashMap<>();

  /**
   * Can interactions, such as sending and receiving messages and events, not modeled in the
   * process occur when the Process is executed or performed. If the value is <code>true</code>,
   * they MAY NOT occur. If the value is <code>false</code>, they MAY occur.
   */
  private boolean isClosed;

  /**
   * Is the Process executable?
   */
  private boolean isExecutable;

  /**
   * The type of Process.
   */
  private ProcessType processType;

  /**
   * Constructs a new <code>Process</code>.
   *
   * @param element the XML element containing the Process information
   */
  public Process(Element element)
  {
    super(null, element);

    try
    {
      this.processType = ProcessType.fromId(element.getAttribute("processType"));

      this.isExecutable = Boolean.parseBoolean(element.getAttribute("isExecutable"));

      this.isClosed = Boolean.parseBoolean(element.getAttribute("isClosed"));

      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "auditing":
            {
              break;
            }

            case "monitoring":
            {
              break;
            }

            case "processRole":
            {
              break;
            }

            case "property":
            {
              break;
            }

            case "laneSet":
            {
              break;
            }

            case "artifact":
            {
              break;
            }

            case "resourceRole":
            {
              break;
            }

            case "correlationSubcription":
            {
              break;
            }

            case "supports":
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
              throw new ParserException(
                "Failed to parse the unknown XML element (" + childElement.getNodeName() + ")");
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Process XML data", e);
    }
  }

  /**
   * Add the FlowElement to the Process.
   *
   * @param flowElement the FlowElement to add to the Process
   */
  public void addFlowElement(FlowElement flowElement)
  {
    flowElements.put(flowElement.getId(), flowElement);
  }

  /**
   * Returns the FlowElement.
   *
   * @param id the ID uniquely identifying the FlowElement
   *
   * @return the FlowElement or <code>null</code> if the FlowElement could
   * not be found
   */
  public FlowElement getFlowElement(QName id)
  {
    return flowElements.get(id);
  }

  /**
   * Returns the FlowElements for the Process.
   *
   * @return the FlowElements for the Process
   */
  public Collection<FlowElement> getFlowElements()
  {
    return flowElements.values();
  }

  /**
   * Returns the type of Process.
   *
   * @return the type of Process
   */
  public ProcessType getProcessType()
  {
    return processType;
  }

  /**
   * Returns whether interactions, such as sending and receiving messages and events, not modeled
   * in the process may occur when the Process is executed or performed.
   *
   * @return <code>true</code> if they MAY NOT occur or <code>false</code> if the MAY occur
   */
  public boolean isClosed()
  {
    return isClosed;
  }

  /**
   * Returns whether the Process is executable.
   * <p/>
   * An executable Process is a private Process that has been modeled for the purpose of being
   * executed.
   * <p/>
   * A non-executable process is a private Process that has been modeled for the purpose of
   * documenting process behavior at a modeler-defined level of detail. Thus, information needed
   * for execution, such as formal condition expressions are typically not included in a
   * non-executable Process.
   *
   * @return <code>true</code> if the Process is executable or <code>false</code> otherwise
   */
  public boolean isExecutable()
  {
    return isExecutable;
  }
}
