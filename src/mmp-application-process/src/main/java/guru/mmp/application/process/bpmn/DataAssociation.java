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

import guru.mmp.common.xml.XmlUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * The <code>DataAssociation</code> class represents a DataAssociation that forms part of a Process.
 * <p>
 * Data Associations are used to move data between Data Objects, Properties, and inputs and outputs
 * of Activities, Processes, and GlobalTasks.
 * <p>
 * The DataAssociation class is a BaseElement contained by an Activity or Event, used to model how
 * data is pushed into or pulled from item-aware elements. DataAssociation elements have one or
 * more sources and a target; the source of the association is copied into the target.
 * <p>
 * The ItemDefinition from the souceRef and targetRef MUST have the same ItemDefinition or the
 * DataAssociation MUST have a transformation Expression that transforms the source ItemDefinition
 * into the target ItemDefinition.
 * <p>
 * <b>DataAssociation</b> XML schema:
 * <pre>
 * &lt;xsd:element name="dataAssociation" type="tDataAssociation" /&gt;
 * &lt;xsd:complexType name="tDataAssociation" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tBaseElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="sourceRef" type="xsd:IDREF" minOccurs="0"
 *                         maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="targetRef" type="xsd:IDREF" minOccurs="1" maxOccurs="1"/&gt;
 *         &lt;xsd:element name="transformation" type="tFormalExpression" minOccurs="0"
 *                         maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="assignment" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class DataAssociation extends BaseElement
{
  /**
   * The data element Assignments.
   * <p>
   * By using an Assignment, single data structure elements can be assigned from the source
   * structure to the target structure.
   */
  private List<Assignment> assignments = new ArrayList<>();

  /**
   * The ID uniquely identifying the source of the Data Association.
   * <p>
   * The source MUST be an ItemAwareElement.
   */
  private QName sourceId;

  /**
   * The ID uniquely identifying the target of the Data Association.
   * <p>
   * The target MUST be an ItemAwareElement.
   */
  private QName targetId;

  /**
   * The optional transformation Expression.
   * <p>
   * The actual scope of accessible data for that Expression is defined by the source and target of
   * the specific Data Association types.
   */
  private FormalExpression transformation;

  /**
   * Constructs a new <code>DataAssociation</code>.
   *
   * @param parent  the BPMN element that is the parent of this BPMN element
   * @param element the XML element containing the DataAssociation information
   */
  protected DataAssociation(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      NodeList childElements = element.getChildNodes();

      for (int i = 0; i < childElements.getLength(); i++)
      {
        Node node = childElements.item(i);

        if (node instanceof Element)
        {
          Element childElement = (Element) node;

          switch (childElement.getNodeName())
          {
            case "sourceRef":
            {
              sourceId = XmlUtils.getQName(childElement, childElement.getTextContent());

              break;
            }

            case "targetRef":
            {
              targetId = XmlUtils.getQName(childElement, childElement.getTextContent());

              break;
            }

            case "transformation":
            {
              transformation = new FormalExpression(childElement);

              break;
            }

            case "assignment":
            {
              assignments.add(new Assignment(this, childElement));

              break;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the DataAssociation XML data", e);
    }
  }

  /**
   * Returns the data element Assignments.
   * <p>
   * By using an Assignment, single data structure elements can be assigned from the source
   * structure to the target structure.
   *
   * @return the data element Assignments
   */
  public List<Assignment> getAssignments()
  {
    return assignments;
  }

  /**
   * Returns the ID uniquely identifying the source of the Data Association.
   * <p>
   * The source MUST be an ItemAwareElement.
   *
   * @return the ID uniquely identifying the source of the Data Association
   */
  public QName getSourceId()
  {
    return sourceId;
  }

  /**
   * Returns the ID uniquely identifying the target of the Data Association.
   * <p>
   * The source MUST be an ItemAwareElement.
   *
   * @return the ID uniquely identifying the target of the Data Association
   */
  public QName getTargetId()
  {
    return targetId;
  }

  /**
   * Returns the optional transformation Expression or <code>null</code> if no transformation
   * Expression is specified.
   * <p>
   * The actual scope of accessible data for that Expression is defined by the source and target of
   * the specific Data Association types.
   *
   * @return the optional transformation Expression
   */
  public FormalExpression getTransformation()
  {
    return transformation;
  }
}
