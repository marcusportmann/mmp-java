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
 * The <code>ManualTaskBehavior</code> class implements the behavior for a Business Process Model
 * and Notation (BPMN) manual task that forms part of a BPMN process.
 * <p>
 * This task represents work that is not automated and is performed outside the control of the
 * BPM engine.
 * <p>
 * <b>Manual Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="manualTask" type="tManualTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tManualTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class ManualTaskBehavior extends TaskBehavior
{
  /**
   * Constructs a new <code>ManualTaskBehavior</code>.
   */
  public ManualTaskBehavior() {}

  /**
   * Execute the behavior for the Business Process Model and Notation (BPMN) manual task.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the behavior for the
   *         Business Process Model and Notation (BPMN) manual task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
