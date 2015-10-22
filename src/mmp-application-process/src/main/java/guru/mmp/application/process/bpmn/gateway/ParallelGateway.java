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
 * The <code>ParallelGateway</code> class represents a Business Process Model and Notation (BPMN)
 * parallel gateway that forms part of a BPMN process.
 *
 * @author Marcus Portmann
 */
public final class ParallelGateway extends Gateway
{
  /**
   * The gateway direction for the parallel gateway.
   */
  private GatewayDirection gatewayDirection;

  /**
   * Constructs a new <code>ParallelGateway</code>.
   *
   * @param id               the ID uniquely identifying the parallel gateway
   * @param name             the name of the parallel gateway
   * @param gatewayDirection the gateway direction for the parallel gateway
   */
  public ParallelGateway(String id, String name, GatewayDirection gatewayDirection)
  {
    super(id, name);

    this.gatewayDirection = gatewayDirection;
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) parallel gateway.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) parallel gateway
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the gateway direction for the parallel gateway.
   *
   * @return the gateway direction for the parallel gateway
   */
  public GatewayDirection getGatewayDirection()
  {
    return gatewayDirection;
  }
}
