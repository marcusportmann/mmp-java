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

package guru.mmp.application.process.bpmn;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>FlowNode</code> class provides the base class that all flow nodes that form part of a
 * Business Process Model and Notation (BPMN) process should be derived from.
 *
 * @author Marcus Portmann
 */
public abstract class FlowNode
  implements IFlowNode
{
  /**
   * The ID uniquely identifying the flow node.
   */
  private String id;

  /**
   * Constructs a new <code>FlowNode</code>.
   *
   * @param id the ID uniquely identifying the flow node
   */
  public FlowNode(String id)
  {
    this.id = id;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) flow node.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) flow node
   */
  @Override
  public abstract List<Token> execute(ProcessExecutionContext context);

  /**
   * Returns the ID uniquely identifying the flow node.
   *
   * @return the ID uniquely identifying the flow node
   */
  public String getId()
  {
    return id;
  }
}
