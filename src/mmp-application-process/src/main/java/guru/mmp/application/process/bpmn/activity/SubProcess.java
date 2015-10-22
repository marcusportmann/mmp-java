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

import guru.mmp.application.process.bpmn.FlowElement;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SubProcess</code> class represents a  Business Process Model and Notation (BPMN)
 * sub-process.
 * <p>
 * A sub-process has parts that are modeled in a child-level process, a process with its own
 * activity flow and start and end states.
 * <p>
 * A sub-process can be triggered by an event making it an event sub-process. An event sub-process
 * is not part of the normal flow of the process. Instead, it is triggered by one of the following
 * events:
 * <ul>
 *   <li>Message</li>
 *   <li>Timer</li>
 *   <li>Multiple</li>
 *   <li>Multiple-Parallel</li>
 *   <li>Conditional</li>
 *   <li>Signal</li>
 *   <li>Escalation</li>
 * </ul>
 * <p>
 * <b>Sub-Process</b> XML schema:
 * <pre>
 * </pre>
 *
 * @author Marcus Portmann
 */
public class SubProcess extends Activity
{
  /**
   * The flow elements for the sub-process.
   */
  private Map<String, FlowElement> flowElements = new ConcurrentHashMap<>();

  /**
   * Is the sub-process triggered by an event?
   */
  private boolean triggeredByEvent;

  /**
   * Constructs a new <code>SubProcess</code>.
   *
   * @param id                 the ID uniquely identifying the sub-process
   * @param name               the name of the sub-process
   * @param forCompensation    is the sub-process for compensation
   * @param startQuantity      the start quantity for the sub-process
   * @param completionQuantity the completion quantity for the sub-process
   * @param triggeredByEvent   is the sub-process triggered by an event
   */
  public SubProcess(String id, String name, boolean forCompensation, int startQuantity,
      int completionQuantity, boolean triggeredByEvent)
  {
    super(id, name, forCompensation, startQuantity, completionQuantity);

    this.triggeredByEvent = triggeredByEvent;
  }

  /**
   * Add the flow element to the sub-process.
   *
   * @param flowElement the flow element to add to the sub-process
   */
  public void addFlowElement(FlowElement flowElement)
  {
    flowElements.put(flowElement.getId(), flowElement);
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) sub-process.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) sub-process
   */
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the flow element with the specified ID.
   *
   * @param id the ID uniquely identifying the flow element
   *
   * @return the flow element with the specified ID or <code>null</code> if the flow element could
   *         not be found
   */
  public FlowElement getFlowElement(String id)
  {
    return flowElements.get(id);
  }

  /**
   * Returns the flow elements for the sub-process.
   *
   * @return the flow elements for the sub-process
   */
  public Collection<FlowElement> getFlowElements()
  {
    return flowElements.values();
  }

  /**
   * Returns whether the sub-process is triggered by an event.
   *
   * @return <code>true</code> if the sub-process is triggered by an event or <code>false</code>
   *         otherwise
   */
  public boolean isTriggeredByEvent()
  {
    return triggeredByEvent;
  }
}
