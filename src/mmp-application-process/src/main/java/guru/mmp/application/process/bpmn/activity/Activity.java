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
import guru.mmp.common.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Activity</code> class provides the base class that all Activity subclasses should be
 * derived from.
 * <p/>
 * Activities represent points in a Process flow where work is performed. They are the executable
 * elements of a BPMN Process. An Activity can be atomic or non-atomic (compound).
 * <p/>
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
@SuppressWarnings("unused")
public abstract class Activity extends FlowNode
{
  /**
   * The modeler-defined properties for the Activity.
   * <p/>
   * These properties are contained within the Activity.
   */
  private List<Property> properties = new ArrayList<>();

  /**
   * The resources that will perform or will be responsible for the Activity.
   * <p/>
   * A resource, e.g. a performer, can be specified in the form of a specific individual, a group,
   * an organization role or position, or an organization.
   */
  private List<ResourceRole> resources = new ArrayList<>();

  /**
   * The number of tokens will be sent down any outgoing Sequence Flow (assuming any Sequence Flow
   * conditions are satisfied).
   */
  private int completionQuantity;

  /**
   * The Sequence Flow that will receive a token when none of the <code>conditionExpression</code>s
   * on other outgoing Sequence Flows evaluate to <code>true</code>.
   * <p/>
   * The default Sequence Flow should not have a <code>conditionExpression</code>. Any such
   * Expression SHALL be ignored.
   */
  private String defaultRef;

  /**
   * The <code>InputOutputSpecification</code>, which defines the inputs and outputs and the
   * <code>InputSet</code>s and <code>OutputSet</code>s for the Activity.
   */
  private InputOutputSpecification ioSpecification;

  /**
   * Is the Activity intended for the purposes of compensation?
   * <p/>
   * If <code>false</code>, then this Activity executes as a result of normal execution flow.
   * If <code>true</code>, this Activity is only activated when a Compensation Event is detected
   * and initiated under Compensation Event visibility scope.
   */
  private boolean isForCompensation;

  /**
   * The loop characteristics for the Activity.
   * <p/>
   * An Activity MAY be performed once or MAY be repeated. If repeated, the Activity MUST have
   * <code>loopCharacteristics</code> that define the repetition criteria (if the isExecutable
   * attribute of the Process is set to true).
   */
  private LoopCharacteristics loopCharacteristics;

  /**
   * The number of tokens that MUST arrive before the Activity can begin.
   * <p/>
   * The default value is 1. The value MUST NOT be less than 1. This attribute defines the number
   * of tokens that MUST arrive before the Activity can begin. Note that any value for the attribute
   * that is greater than 1 is an advanced type of modeling and should be used with caution.
   */
  private int startQuantity;

  /**
   * Constructs a new <code>Activity</code>.
   *
   * @param parent  the BPMN element that is the parent of this Activity
   * @param element the XML element containing the Activity information
   */
  public Activity(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.isForCompensation = !StringUtil.isNullOrEmpty(element.getAttribute("isForCompensation"))
          && Boolean.parseBoolean(element.getAttribute("isForCompensation"));

      if (StringUtil.isNullOrEmpty(element.getAttribute("startQuantity")))
      {
        this.startQuantity = 1;
      }
      else
      {
        this.startQuantity = Integer.parseInt(element.getAttribute("startQuantity"));
      }

      if (StringUtil.isNullOrEmpty(element.getAttribute("completionQuantity")))
      {
        this.completionQuantity = 1;
      }
      else
      {
        this.completionQuantity = Integer.parseInt(element.getAttribute("completionQuantity"));
      }

      if (!StringUtil.isNullOrEmpty(element.getAttribute("default")))
      {
        this.defaultRef = element.getAttribute("default");
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
            case "ioSpecification":
            {
              ioSpecification = new InputOutputSpecification(childElement);

              break;
            }

            case "property":
            {
              properties.add(new Property(childElement));

              break;
            }

            case "dataInputAssociation":
            {
              throw new ParserException("Failed to parse the unsupported XML element ("
                  + childElement.getNodeName() + ")");
            }

            case "dataOutputAssociation":
            {
              throw new ParserException("Failed to parse the unsupported XML element ("
                  + childElement.getNodeName() + ")");
            }

            case "resourceRole":
            {
              resources.add(new ResourceRole(childElement));

              break;
            }

            case "loopCharacteristics":
            {
              loopCharacteristics = new LoopCharacteristics(childElement);

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
      throw new ParserException("Failed to parse the Activity XML data", e);
    }
  }

  /**
   * Returns the number of tokens will be sent down any outgoing Sequence Flow (assuming any
   * Sequence Flow conditions are satisfied).
   *
   * @return the number of tokens will be sent down any outgoing Sequence Flow (assuming any
   * Sequence Flow conditions are satisfied)
   */
  public int getCompletionQuantity()
  {
    return completionQuantity;
  }

  /**
   * Returns the ID of the Sequence Flow that will receive a token when none of the
   * <code>conditionExpression</code>s on other outgoing Sequence Flows evaluate to
   * <code>true</code>.
   * <p/>
   * The default Sequence Flow should not have a <code>conditionExpression</code>. Any such
   * Expression SHALL be ignored.
   *
   * @return the ID of the Sequence Flow that will receive a token when none of the
   *         <code>conditionExpression</code>s on other outgoing Sequence Flows evaluate to
   *         <code>true</code>
   */
  public String getDefaultRef()
  {
    return defaultRef;
  }

  /**
   * Returns the <code>InputOutputSpecification</code>, which defines the inputs and outputs and
   * the <code>InputSet</code>s and <code>OutputSet</code>s for the Activity.
   *
   * @return the <code>InputOutputSpecification</code>, which defines the inputs and outputs and
   *         the <code>InputSet</code>s and <code>OutputSet</code>s for the Activity
   */
  public InputOutputSpecification getIoSpecification()
  {
    return ioSpecification;
  }

  /**
   * Returns the loop characteristics for the Activity.
   * <p/>
   * An Activity MAY be performed once or MAY be repeated. If repeated, the Activity MUST have
   * <code>loopCharacteristics</code> that define the repetition criteria (if the isExecutable
   * attribute of the Process is set to true).
   *
   * @return the loop characteristics for the Activity
   */
  public LoopCharacteristics getLoopCharacteristics()
  {
    return loopCharacteristics;
  }

  /**
   * Returns the modeler-defined properties for the Activity.
   *
   * @return the modeler-defined properties for the Activity.
   */
  public List<Property> getProperties()
  {
    return properties;
  }

  /**
   * Returns the resources that will perform or will be responsible for the Activity.
   * <p/>
   * A resource, e.g. a Performer, can be specified in the form of a specific individual, a group,
   * an organization role or position, or an organization.
   *
   * @return the resources that will perform or will be responsible for the Activity
   */
  public List<ResourceRole> getResources()
  {
    return resources;
  }

  /**
   * Returns the number of tokens that MUST arrive before the Activity can begin.
   * <p/>
   * The default value is 1. The value MUST NOT be less than 1. This attribute defines the number
   * of tokens that MUST arrive before the Activity can begin. Note that any value for the attribute
   * that is greater than 1 is an advanced type of modeling and should be used with caution.
   *
   * @return the number of tokens that MUST arrive before the Activity can begin
   */
  public int getStartQuantity()
  {
    return startQuantity;
  }

  /**
   * Returns <code>true</code> if the Activity is intended for the purposes of compensation or
   * <code>false</code> otherwise.
   * <p/>
   * If <code>false</code>, then this Activity executes as a result of normal execution flow.
   * If <code>true</code>, this Activity is only activated when a Compensation Event is detected
   * and initiated under Compensation Event visibility scope.
   *
   * @return <code>true</code> if the Activity is intended for the purposes of compensation or
   *         <code>false</code> otherwise
   */
  public boolean isForCompensation()
  {
    return isForCompensation;
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
