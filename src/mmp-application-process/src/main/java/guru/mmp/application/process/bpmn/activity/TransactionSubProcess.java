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

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TransactionSubProcess</code> class represents a  Business Process Model and Notation
 * (BPMN) transaction sub-process.
 *
 * Transaction sub-processes have the following properties:
 * <ul>
 *   <li>
 *     <b>Atomic</b>: Activities inside the transaction sub-process are treated as a unit.
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
 * <p>
 * <b>Transaction Sub-Process</b> XML schema:
 * <pre>
 * &lt;xsd:element name="transaction" type="tTransaction" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tTransaction"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tSubProcess"&gt;
 *       &lt;xsd:attribute name="method" type="tTransactionMethod" default="Compensate"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:simpleType name="tTransactionMethod"&gt;
 *   &lt;xsd:restriction base="xsd:string"&gt;
 *     &lt;xsd:enumeration value="Compensate"/&gt;
 *     &lt;xsd:enumeration value="Image"/&gt;
 *     &lt;xsd:enumeration value="Store"/&gt;
 *   &lt;/xsd:restriction&gt;
 * &lt;/xsd:simpleType&gt;  
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class TransactionSubProcess extends SubProcess
{
  /**
   * Constructs a new <code>TransactionSubProcess</code>.
   *
   * @param id                 the ID uniquely identifying the transaction sub-process
   * @param name               the name of the transaction sub-process
   * @param forCompensation    is the transaction sub-process for compensation
   * @param startQuantity      the start quantity for the transaction sub-process
   * @param completionQuantity the completion quantity for the transaction sub-process
   * @param triggeredByEvent   is the transaction sub-process triggered by an event
   */
  public TransactionSubProcess(String id, String name, boolean forCompensation, int startQuantity,
      int completionQuantity, boolean triggeredByEvent)
  {
    super(id, name, forCompensation, startQuantity, completionQuantity, triggeredByEvent);
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) transaction sub-process.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) transaction sub-process
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
