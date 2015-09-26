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

import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TransactionSubprocess</code> class represents a Business Process Model and Notation
 * (BPMN) Transaction Subprocess that forms part of a BPMN process.
 * <p>
 * Transaction subprocesses have the following properies:
 * <ul>
 *   <li>
 *     <b>Atomic</b>: Activities inside the transaction subprocess are treated as a unit.
 *     Either all are performed or none.
 *   </li>
 *   <li>
 *     <b>Consistency</b>: The transaction leaves the process (or system) in a valid state.
 *   </li>
 *   <li>
 *     <b>Isolation</b>: The effects of one transaction might not be visible to other parts of the
 *     process (or system).
 *   </li>
 *   <li>
 *     <b>Durability</b>: Once a transaction has finished successfully, changes are persisted
 *     permanently.
 *   </li>
 * </ul>
 * <p>
 * Transactions have only three possible outcomes:
 * <ul>
 *   <li>Success</li>
 *   <li>Cancellation</li>
 *   <li>Exception (error)</li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public class TransactionSubprocess extends Subprocess
{
  /**
   * Execute the Business Process Model and Notation (BPMN) transaction subprocess.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) transaction subprocess
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
