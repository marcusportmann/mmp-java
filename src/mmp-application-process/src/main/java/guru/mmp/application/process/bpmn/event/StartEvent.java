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

import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>StartEvent</code> class represents a Business Process Model and Notation (BPMN)
 * start event that forms part of a BPMN process.
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
 * @author Marcus Portmann
 */
public final class StartEvent extends CatchingEvent
{
  /**
   * Is the start event interrupting i.e. does the activity that triggered the event terminate
   * and the flow of the process continue from the event (interrupting) or does the activity
   * continue and the flow at the event execute in parallel (non-interrupting)?
   */
  private boolean interrupting;

  /**
   * Constructs a new <code>StartEvent</code>.
   *
   * @param id           the ID uniquely identifying the start event
   * @param name         the name of the start event
   * @param interrupting is the start event interrupting
   */
  public StartEvent(String id, String name, boolean interrupting)
  {
    super(id, name);

    this.interrupting = interrupting;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) start event.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) start event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return super.execute(context);
  }

  /**
   * Is the start event interrupting?
   * <p>
   * Does the activity that triggered the start event terminate and the flow of the process continue
   * from the event (interrupting) or does the activity continue and the flow at the event execute
   * in parallel (non-interrupting)?
   *
   * @return <code>true</code> if the start event is interrupting or <code>false</code> otherwise
   */
  public boolean isInterrupting()
  {
    return interrupting;
  }
}
