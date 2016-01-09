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

package guru.mmp.application.process.bpmn.event;

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * The <code>BoundaryEvent</code> class represents a Boundary Event that forms part of a Process.
 * <p/>
 * Boundary Events are placed on the boundary of an Activity.
 * <p/>
 * Boundary Events are catching only Intermediate Events that may or may not be interrupting to the
 * Activity. Events thrown inside an Activity are passed up the Process hierarchy until some
 * Activity catches them.
 * <p/>
 * Common uses of Boundary Events include deadlines and timeouts.
 * <p/>
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
@SuppressWarnings("unused")
public final class BoundaryEvent
  extends CatchEvent
{
  /**
   * The reference to the element the Boundary Event is attached to.
   */
  private QName attachedToRef;

  /**
   * Should the Activity be cancelled or not, i.e., does the Boundary Event act as an error or an
   * escalation. If the Activity is not cancelled, multiple instances of that handler can run
   * concurrently.
   */
  private boolean cancelActivity;

  /**
   * Constructs a new <code>BoundaryEvent</code>.
   *
   * @param parent  the BPMN element that is the parent of this Boundary Event
   * @param element the XML element containing the Boundary Event information
   */
  protected BoundaryEvent(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.cancelActivity = StringUtil.isNullOrEmpty(element.getAttribute("cancelActivity")) ||
        Boolean.parseBoolean(element.getAttribute("cancelActivity"));

      if (!StringUtil.isNullOrEmpty("attachedToRef"))
      {
        this.attachedToRef = XmlUtils.getQName(element, element.getAttribute("attachedToRef"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Boundary Event XML data", e);
    }
  }

  /**
   * Execute the Boundary Event.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Boundary Event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return super.execute(context);
  }

  /**
   * Returns the reference to the element the Boundary Event is attached to.
   *
   * @return the reference to the element the Boundary Event is attached to
   */
  public QName getAttachedToRef()
  {
    return attachedToRef;
  }

  /**
   * Returns <code>true</code> if the Activity should be cancelled or <code>false</code> otherwise.
   * <p/>
   * If the Activity is cancelled the Boundary Event acts as an error or an escalation.
   *
   * @return <code>true</code> if the Activity should be cancelled or <code>false</code> otherwise
   */
  public boolean getCancelActivity()
  {
    return cancelActivity;
  }
}
