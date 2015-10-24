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

package guru.mmp.application.process.bpmn;

//~--- JDK imports ------------------------------------------------------------

import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>Process</code> class represents a Business Process Model and Notation (BPMN) process.
 * <p>
 * <b>User Task</b> XML schema:
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
 *
 * &lt;xsd:simpleType name="tProcessType"&gt;
 *   &lt;xsd:restriction base="xsd:string"&gt;
 *     &lt;xsd:enumeration value="None"/&gt;
 *     &lt;xsd:enumeration value="Public"/&gt;
 *     &lt;xsd:enumeration value="Private"/&gt;
 *   &lt;/xsd:restriction&gt;
 * &lt;/xsd:simpleType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public class Process extends CallableElement
{
  /**
   * The flow elements for the process.
   */
  private Map<String, FlowElement> flowElements = new ConcurrentHashMap<>();

  /**
   * Can interactions, such as sending and receiving messages and events, not modeled in the
   * process occur when the process is executed or performed. If the value is <code>true</code>,
   * they MAY NOT occur. If the value is <code>false</code>, they MAY occur.
   */
  private boolean isClosed;

  /**
   * Is the process executable?
   */
  private boolean isExecutable;

  /**
   * Constructs a new <code>Process</code>.
   *
   * @param id           the ID uniquely identifying the process
   * @param name         the name of the process
   * @param isClosed     can interactions, such as sending and receiving messages and events, not
   *                     modeled in the process occur when the process is executed or performed
   * @param isExecutable is the process executable
   */
  public Process(String id, String name, boolean isClosed, boolean isExecutable)
  {
    super(id, name);

    this.isClosed = isClosed;
    this.isExecutable = isExecutable;
  }

  /**
   * Add the flow element to the process.
   *
   * @param flowElement the flow element to add to the process
   */
  public void addFlowElement(FlowElement flowElement)
  {
    flowElements.put(flowElement.getId(), flowElement);
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
   * Returns the flow elements for the process.
   *
   * @return the flow elements for the process
   */
  public Collection<FlowElement> getFlowElements()
  {
    return flowElements.values();
  }

  /**
   * Returns whether interactions, such as sending and receiving messages and events, not modeled
   * in the process may occur when the process is executed or performed.
   *
   * @return <code>true</code> if they MAY NOT occur or <code>false</code> if the MAY occur
   */
  public boolean isClosed()
  {
    return isClosed;
  }

  /**
   * Returns whether the process is executable.
   * <p>
   * An executable process is a private process that has been modeled for the purpose of being
   * executed.
   * <p>
   * A non-executable process is a private process that has been modeled for the purpose of
   * documenting process behavior at a modeler-defined level of detail. Thus, information needed
   * for execution, such as formal condition expressions are typically not included in a
   * non-executable process.
   *
   * @return <code>true</code> if the process is executable or <code>false</code> otherwise
   */
  public boolean isExecutable()
  {
    return isExecutable;
  }

  /**
   * Parse the XML to retrieve the information for the process.
   *
   * @param element the XML for the process
   *
   * @return the process
   */
  public static Process fromXML(Element element)
  {



    return null;
  }
}
