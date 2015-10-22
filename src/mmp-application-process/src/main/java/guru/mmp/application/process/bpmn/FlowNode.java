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

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>FlowNode</code> class provides the base class that all flow nodes that form part of a
 * Business Process Model and Notation (BPMN) process should be derived from.
 * <p>
 * <b>Flow Node</b> XML schema:
 * <pre>
 * &lt;xsd:element name="flowNode" type="tFlowNode"/&gt;
 * &lt;xsd:complexType name="tFlowNode" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tFlowElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="incoming" type="xsd:QName" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="outgoing" type="xsd:QName" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class FlowNode extends FlowElement
{
  /**
   * The IDs uniquely identifying the incoming flow elements for the flow node.
   */
  private List<String> incomingFlowElementIds = new ArrayList<>();

  /**
   * The IDs uniquely identifying the incoming flow elements for the flow node.
   */
  private List<String> outgoingFlowElementIds = new ArrayList<>();

  /**
   * Constructs a new <code>FlowNode</code>.
   *
   * @param id   the ID uniquely identifying the flow node
   * @param name the name of the flow node
   */
  public FlowNode(String id, String name)
  {
    super(id, name);
  }

  /**
   * Add the ID uniquely identifying the incoming flow element for the flow node.
   *
   * @param id the ID uniquely identifying the incoming flow element for the flow node
   */
  public void addIncomingFlowElement(String id)
  {
    incomingFlowElementIds.add(id);
  }

  /**
   * Add the ID uniquely identifying the outgoing flow element for the flow node.
   *
   * @param id the ID uniquely identifying the outgoing flow element for the flow node
   */
  public void addOutgoingFlowElement(String id)
  {
    outgoingFlowElementIds.add(id);
  }

  /**
   * Returns the IDs uniquely identifying the incoming flow elements for the flow node.
   *
   * @return IDs uniquely identifying the incoming flow elements for the flow node
   */
  public List<String> getIncomingFlowElementIds()
  {
    return incomingFlowElementIds;
  }

  /**
   * Returns the IDs uniquely identifying the incoming flow elements for the flow node.
   *
   * @return the IDs uniquely identifying the incoming flow elements for the flow node
   */
  public List<String> getOutgoingFlowElementIds()
  {
    return outgoingFlowElementIds;
  }
}
