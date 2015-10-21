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
 * The <code>ServiceTaskBehavior</code> class implements the behavior for a Business Process Model
 * and Notation (BPMN) service task that forms part of a BPMN process.
 * <p>
 * This task represents work that is performed by an external system where there is no human
 * intervention, like a web service.
 * <p>
 * <b>Script Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="serviceTask" type="tServiceTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tServiceTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:attribute name="implementation" type="tImplementation" default="##WebService"/&gt;
 *       &lt;xsd:attribute name="operationRef" type="xsd:QName" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class ServiceTaskBehavior extends TaskBehavior
{
  /**
   * The implementation type for the service task.
   */
  private ImplementationType implementationType;

  /**
   * Constructs a new <code>ServiceTaskBehavior</code>.
   */
  public ServiceTaskBehavior() {}

  /**
   * Constructs a new <code>ServiceTaskBehavior</code>.
   *
   * @param implementationType the implementation type for the service task
   */
  public ServiceTaskBehavior(ImplementationType implementationType)
  {
    this.implementationType = implementationType;
  }

  /**
   * Execute the behavior for the Business Process Model and Notation (BPMN) service task.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the behavior for the
   *         Business Process Model and Notation (BPMN) service task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the implementation type for the service task.
   *
   * @return the implementation type for the service task
   */
  public ImplementationType getImplementationType()
  {
    return implementationType;
  }
}
