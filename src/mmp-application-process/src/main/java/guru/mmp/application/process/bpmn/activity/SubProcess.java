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
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 *
 * @author Marcus Portmann
 */
public class SubProcess extends Activity
{
  /**
   * The flow nodes for the sub-process.
   */
  private Map<String, FlowNode> flowNodes = new ConcurrentHashMap<>();

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
   * Add the flow node to the sub-process.
   *
   * @param flowNode the flow node to add to the sub-process
   */
  public void addFlowNode(FlowNode flowNode)
  {
    flowNodes.put(flowNode.getId(), flowNode);
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
   * Returns the flow node with the specified ID.
   *
   * @param id the ID uniquely identifying the flow node
   *
   * @return the flow node with the specified ID or <code>null</code> if the flow node could
   *         not be found
   */
  public FlowNode getFlowNode(String id)
  {
    return flowNodes.get(id);
  }

  /**
   * Returns the flow nodes for the sub-process.
   *
   * @return the flow nodes for the sub-process
   */
  public Collection<FlowNode> getFlowNodes()
  {
    return flowNodes.values();
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
