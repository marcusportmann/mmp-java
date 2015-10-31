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

import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

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
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class TransactionSubProcess extends SubProcess
{
  /**
   * The transaction method for the transaction sub-process.
   */
  private TransactionMethod method;

  /**
   * Constructs a new <code>TransactionSubProcess</code>.
   *
   * @param element the XML element containing the sub-process information
   */
  public TransactionSubProcess(Element element)
  {
    super(element);

    try
    {
      method = TransactionMethod.fromId(element.getAttribute("method"));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the transaction sub-process XML data", e);
    }
  }

  /**
   * Execute the BPMN transaction sub-process.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) transaction sub-process
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the transaction method for the transaction sub-process.
   *
   * @return the transaction method for the transaction sub-process
   */
  public TransactionMethod getMethod()
  {
    return method;
  }
}
