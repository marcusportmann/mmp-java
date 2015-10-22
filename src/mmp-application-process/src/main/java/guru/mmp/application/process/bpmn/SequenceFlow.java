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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.FlowNode;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>SequenceFlow</code> class represents a Business Process Model and Notation (BPMN)
 * sequence flow that forms part of a BPMN process.
 * <p>
 * <b>Sequence Flow</b> XML schema:
 * <pre>
 * &lt;xsd:element name="sequenceFlow" type="tSequenceFlow" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tSequenceFlow"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tFlowElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="conditionExpression" type="tExpression" minOccurs="0"
 *                         maxOccurs="1"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="sourceRef" type="xsd:IDREF" use="required"/&gt;
 *       &lt;xsd:attribute name="targetRef" type="xsd:IDREF" use="required"/&gt;
 *       &lt;xsd:attribute name="isImmediate" type="xsd:boolean" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class SequenceFlow extends FlowElement
{
  /**
   * The ID uniquely identifying the source flow element.
   */
  private String sourceFlowElementId;

  /**
   * The ID uniquely identifying the target flow element.
   */
  private String targetFlowElementId;

  /**
   * Constructs a new <code>SequenceFlow</code>.
   *
   * @param id                  the ID uniquely identifying the sequence flow
   * @param sourceFlowElementId the ID uniquely identifying the source flow element
   * @param targetFlowElementId the ID uniquely identifying the target flow element
   */
  public SequenceFlow(String id, String sourceFlowElementId, String targetFlowElementId)
  {
    super(id, id);

    this.sourceFlowElementId = sourceFlowElementId;
    this.targetFlowElementId = targetFlowElementId;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) flow element.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) flow element
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the ID uniquely identifying the source flow element.
   *
   * @return the ID uniquely identifying the source flow element
   */
  public String getSourceFlowElementId()
  {
    return sourceFlowElementId;
  }

  /**
   * Returns the ID uniquely identifying the target flow element.
   *
   * @return ID uniquely identifying the target flow element
   */
  public String getTargetFlowElementId()
  {
    return targetFlowElementId;
  }
}
