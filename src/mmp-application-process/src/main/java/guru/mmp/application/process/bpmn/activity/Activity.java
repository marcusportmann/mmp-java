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

import guru.mmp.application.process.bpmn.FlowNode;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>Activity</code> class provides the base class that all Business Process Model and
 * Notation (BPMN) activity subclasses should be derived from.
 * <p>
 * An activity represents a unit of work performed. A step inside a process. It has a defined start
 * and end and generally requires some kind of input to produce an output.
 * <p>
 * <b>Activity</b> XML schema:
 * <pre>
 * &lt;xsd:element name="activity" type="tActivity"/&gt;
 * &lt;xsd:complexType name="tActivity" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tFlowNode"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="ioSpecification" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="property" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="dataInputAssociation" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="dataOutputAssociation" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="resourceRole" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="loopCharacteristics" minOccurs="0"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="isForCompensation" type="xsd:boolean" default="false"/&gt;
 *       &lt;xsd:attribute name="startQuantity" type="xsd:integer" default="1"/&gt;
 *       &lt;xsd:attribute name="completionQuantity" type="xsd:integer" default="1"/&gt;
 *       &lt;xsd:attribute name="default" type="xsd:IDREF" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
abstract class Activity extends FlowNode
{
  /**
   * The completion quantity for the activity.
   */
  private int completionQuantity;

  /**
   * Is the activity for compensation?
   */
  private boolean forCompensation;

  /**
   * The loop characteristics for the activity.
   */
  private List<LoopCharacteristics> loopCharacteristics = new ArrayList<>();

  /**
   * The start quantity for the activity.
   */
  private int startQuantity;

  /**
   * Constructs a new <code>Activity</code>.
   *
   * @param id                 the ID uniquely identifying the activity
   * @param name               the name of the activity
   * @param forCompensation    is the activity for compensation
   * @param startQuantity      the start quantity for the activity
   * @param completionQuantity the completion quantity for the activity
   */
  public Activity(String id, String name, boolean forCompensation, int startQuantity,
      int completionQuantity)
  {
    super(id, name);

    this.forCompensation = forCompensation;
    this.startQuantity = startQuantity;
    this.completionQuantity = completionQuantity;
  }

  /**
   * Returns the completion quantity for the activity.
   *
   * @return the completion quantity for the activity
   */
  public int getCompletionQuantity()
  {
    return completionQuantity;
  }

  /**
   * Returns the loop characteristics for the activity.
   *
   * @return the loop characteristics for the activity
   */
  public List<LoopCharacteristics> getLoopCharacteristics()
  {
    return loopCharacteristics;
  }

  /**
   * Returns the start quantity for the activity.
   *
   * @return the start quantity for the activity
   */
  public int getStartQuantity()
  {
    return startQuantity;
  }

  /**
   * Returns <code>true</code> if the activity is for compensation or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the activity is for compensation or <code>false</code> otherwise
   */
  public boolean isForCompensation()
  {
    return forCompensation;
  }

  /**
   * Throw an error event.
   *
   * @param name the name of the error event
   */
  public void throwErrorEvent(String name)
  {
    throw new RuntimeException("NOT IMPLEMENTED");
  }

  /**
   * Throw an escalation event.
   *
   * @param name the name of the escalation event
   */
  public void throwEscalationEvent(String name)
  {
    throw new RuntimeException("NOT IMPLEMENTED");
  }
}
