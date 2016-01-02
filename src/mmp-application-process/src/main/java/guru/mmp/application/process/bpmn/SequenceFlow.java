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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>SequenceFlow</code> class represents a SequenceFlow that forms part of a Process.
 * <p>
 * <b>SequenceFlow</b> XML schema:
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
   * If <code>false</code> the participants MAY send messages to each other between the elements
   * connected by the SequenceFlow without additional choreography activities in the choreography.
   * If <code>true</code> then participants MAY NOT send messages to each other between the
   * elements connected by the SequenceFlow without additional choreography activities in the
   * choreography.
   */
  private boolean isImmediate;

  /**
   * The ID uniquely identifying the source FlowElement.
   */
  private String sourceFlowElementId;

  /**
   * The ID uniquely identifying the target FlowElement.
   */
  private String targetFlowElementId;

  /**
   * Constructs a new <code>SequenceFlow</code>.
   *
   * @param parent  the BPMN element that is the parent of this SequenceFlow
   * @param element the XML element containing the SequenceFlow element information
   */
  public SequenceFlow(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.sourceFlowElementId = StringUtil.notNull(element.getAttribute("sourceRef"));

      this.targetFlowElementId = StringUtil.notNull(element.getAttribute("targetRef"));

      if (StringUtil.isNullOrEmpty(element.getAttribute("isImmediate")))
      {
        this.isImmediate = false;
      }
      else
      {
        this.isImmediate = Boolean.parseBoolean(element.getAttribute("isImmediate"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the SequenceFlow element XML data", e);
    }
  }

  /**
   * Execute the SequenceFlow.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the SequenceFlow
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the ID uniquely identifying the source FlowElement.
   *
   * @return the ID uniquely identifying the source FlowElement
   */
  public String getSourceFlowElementId()
  {
    return sourceFlowElementId;
  }

  /**
   * Returns the ID uniquely identifying the target FlowElement.
   *
   * @return ID uniquely identifying the target FlowElement
   */
  public String getTargetFlowElementId()
  {
    return targetFlowElementId;
  }
}
