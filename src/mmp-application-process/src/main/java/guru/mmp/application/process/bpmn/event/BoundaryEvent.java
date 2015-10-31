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

package guru.mmp.application.process.bpmn.event;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.xml.namespace.QName;

/**
 * The <code>BoundaryEvent</code> class represents a BPMN
 * boundary event that forms part of a Process.
 * <p>
 * Boundary events are placed on the boundary of an activity.
 * <p>
 * Boundary events are catching only intermediate events that may or may not be interrupting to the
 * activity. Events thrown inside an activity are passed up the process hierarchy until some
 * activity catches them.
 * <p>
 * Common uses of boundary events include deadlines and timeouts.
 * <p>
 * <b>Boundary Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="boundaryEvent" type="tBoundaryEvent" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tBoundaryEvent"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tCatchEvent"&gt;
 *       &lt;xsd:attribute name="cancelActivity" type="xsd:boolean" default="true"/&gt;
 *       &lt;xsd:attribute name="attachedToRef" type="xsd:QName"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class BoundaryEvent extends CatchEvent
{
  /**
   * The QName for the element the boundary event is attached to.
   */
  private QName attachedToRef;

  /**
   * Should the activity be cancelled or not, i.e., does the boundary catch event act as an error
   * or an escalation. If the activity is not cancelled, multiple instances of that handler can run
   * concurrently.
   */
  private boolean cancelActivity;

  /**
   * Constructs a new <code>BoundaryEvent</code>.
   *
   * @param element the XML element containing the boundary event information
   */
  protected BoundaryEvent(Element element)
  {
    super(element);

    try
    {
      if (StringUtil.isNullOrEmpty(element.getAttribute("cancelActivity")))
      {
        this.cancelActivity = true;
      }
      else
      {
        this.cancelActivity = Boolean.parseBoolean(element.getAttribute("cancelActivity"));
      }

      if (!StringUtil.isNullOrEmpty("attachedToRef"))
      {
        this.attachedToRef = XmlUtils.getQName(element, element.getAttribute("attachedToRef"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the boundary event XML data", e);
    }
  }

  /**
   * Execute the BPMN boundary event.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) boundary event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return super.execute(context);
  }

  /**
   * Returns the QName for the element the boundary event is attached to.
   *
   * @return the QName for the element the boundary event is attached to
   */
  public QName getAttachedToRef()
  {
    return attachedToRef;
  }

  /**
   * Returns <code>true</code> if the activity should be cancelled or <code>false</code> otherwise.
   * <p>
   * If the activity is cancelled the boundary catch event acts as an error or an escalation.
   *
   * @return <code>true</code> if the activity should be cancelled or <code>false</code> otherwise
   */
  public boolean getCancelActivity()
  {
    return cancelActivity;
  }
}
