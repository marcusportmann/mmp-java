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
 * The <code>ReceiveTaskBehavior</code> class implements the behavior for a Business Process Model
 * and Notation (BPMN) receive task that forms part of a BPMN process.
 * <p>
 * This task waits for the arrival of a message from an external participant. Once received, the
 * task is completed.
 * <p>
 * <b>Receive Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="receiveTask" type="tReceiveTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tReceiveTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:attribute name="implementation" type="tImplementation" default="##WebService"/&gt;
 *       &lt;xsd:attribute name="instantiate" type="xsd:boolean" default="false"/&gt;
 *       &lt;xsd:attribute name="messageRef" type="xsd:QName" use="optional"/&gt;
 *       &lt;xsd:attribute name="operationRef" type="xsd:QName" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class ReceiveTaskBehavior extends TaskBehavior
{
  /**
   * The implementation type for the receive task.
   */
  private ImplementationType implementationType;

  /**
   * Can the receive task be used as the instantiation mechanism for the process?
   */
  private boolean instantiateProcess;

  /**
   * Constructs a new <code>ReceiveTaskBehavior</code>.
   *
   * @param implementationType the implementation type for the receive task
   * @param instantiateProcess <code>true</code> if the receive task be used as the instantiation
   *                           mechanism for the process or <code>false</code> otherwise
   */
  public ReceiveTaskBehavior(ImplementationType implementationType, boolean instantiateProcess)
  {
    this.implementationType = implementationType;
    this.instantiateProcess = instantiateProcess;
  }

  /**
   * Execute the behavior for the Business Process Model and Notation (BPMN) receive task.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the behavior for the
   *         Business Process Model and Notation (BPMN) receive task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the implementation type for the receive task.
   *
   * @return the implementation type for the receive task
   */
  public ImplementationType getImplementationType()
  {
    return implementationType;
  }

  /**
   * Returns whether the receive task be used as the instantiation mechanism for the process.
   *
   * @return <code>true</code> if the receive task be used as the instantiation mechanism for the
   *         process or <code>false</code> otherwise
   */
  public boolean getInstantiateProcess()
  {
    return instantiateProcess;
  }
}
