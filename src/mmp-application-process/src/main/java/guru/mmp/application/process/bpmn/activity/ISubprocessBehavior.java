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

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ISubprocessBehavior</code> interface defines the interface that must be implemented by
 * a subprocess behavior class, which implements the behavior for a Business Process Model and
 * Notation (BPMN) subprocess type.
 *
 * @author Marcus Portmann
 */
public interface ISubprocessBehavior
{
  /**
   * Execute the Business Process Model and Notation (BPMN) subprocess behavior.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) subprocess behavior
   */
  List<Token> execute(ProcessExecutionContext context);
}
