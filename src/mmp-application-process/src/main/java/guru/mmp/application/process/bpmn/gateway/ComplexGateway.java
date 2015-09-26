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

package guru.mmp.application.process.bpmn.gateway;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ComplexGateway</code> class represents a Business Process Model and Notation (BPMN)
 * complex gateway that forms part of a BPMN process.
 *
 * @author Marcus Portmann
 */
public class ComplexGateway extends Gateway
{
  /**
   * Execute the Business Process Model and Notation (BPMN) complex gateway.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) complex gateway
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
