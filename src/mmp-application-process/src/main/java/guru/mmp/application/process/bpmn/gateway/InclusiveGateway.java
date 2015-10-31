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

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>InclusiveGateway</code> class represents a BPMN
 * inclusive gateway that forms part of a Process.
 *
 * @author Marcus Portmann
 */
public final class InclusiveGateway extends Gateway
{
  /**
   * Constructs a new <code>InclusiveGateway</code>.
   *
   * @param id   the ID uniquely identifying the inclusive gateway
   * @param name the name of the inclusive gateway
   */
  public InclusiveGateway(String id, String name)
  {
    super(id, name);
  }

  /**
   * Execute the BPMN inclusive gateway.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) inclusive gateway
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
