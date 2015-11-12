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

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import guru.mmp.common.util.StringUtil;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>StartEvent</code> class represents a Start Event that forms part of a Process.
 * <p>
 * A start event indicates the start of a process. Start events generate a token when they are
 * triggered. The token then moves down through the event's outgoing sequence flow.
 * <p>
 * Start events can only have one outgoing sequence flow and they cannot have incoming sequence
 * flows.
 * <p>
 * <b>Start Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="startEvent" type="tStartEvent" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tStartEvent"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tCatchEvent"&gt;
 *       &lt;xsd:attribute name="isInterrupting" type="xsd:boolean" default="true"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class StartEvent extends CatchEvent
{
  /**
   * Is the start event interrupting i.e. does the activity that triggered the event terminate
   * and the flow of the process continue from the event (interrupting) or does the activity
   * continue and the flow at the event execute in parallel (non-interrupting)?
   */
  private boolean isInterrupting;

  /**
   * Constructs a new <code>StartEvent</code>.
   *
   * @param parent  the BPMN element that is the parent of this Start Event
   * @param element the XML element containing the Start Event information
   */
  public StartEvent(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.isInterrupting = StringUtil
        .isNullOrEmpty(element.getAttribute("isInterrupting")) || Boolean
        .parseBoolean(element.getAttribute("isInterrupting"));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Start Event XML data", e);
    }
  }

  /**
   * Execute the Start Event.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Start Event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return super.execute(context);
  }

  /**
   * Is the Start Event interrupting?
   * <p>
   * Does the activity that triggered the Start Event terminate and the flow of the process continue
   * from the event (interrupting) or does the activity continue and the flow at the event execute
   * in parallel (non-interrupting)?
   *
   * @return <code>true</code> if the Start Event is interrupting or <code>false</code> otherwise
   */
  public boolean isInterrupting()
  {
    return isInterrupting;
  }
}
