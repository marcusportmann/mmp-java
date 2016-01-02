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

package guru.mmp.application.process.bpmn.activity;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TransactionSubProcess</code> class represents a Transaction Sub-Process that forms
 * part of a Process.
 *
 * Transaction Sub-Processes have the following properties:
 * <ul>
 *   <li>
 *     <b>Atomic</b>: Activities inside the Transaction Sub-Process are treated as a unit.
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
@SuppressWarnings("unused")
public final class TransactionSubProcess extends SubProcess
{
  /**
   * The transaction method for the Transaction Sub-Process.
   */
  private TransactionMethod method;

  /**
   * Constructs a new <code>TransactionSubProcess</code>.
   *
   * @param parent  the BPMN element that is the parent of this Transaction Sub-Process
   * @param element the XML element containing the Transaction Sub-Process information
   */
  public TransactionSubProcess(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      method = TransactionMethod.fromId(element.getAttribute("method"));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Transaction Sub-Process XML data", e);
    }
  }

  /**
   * Execute the Transaction Sub-Process.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Transaction Sub-Process
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the transaction method for the Transaction Sub-Process.
   *
   * @return the transaction method for the Transaction Sub-Process
   */
  public TransactionMethod getMethod()
  {
    return method;
  }
}
