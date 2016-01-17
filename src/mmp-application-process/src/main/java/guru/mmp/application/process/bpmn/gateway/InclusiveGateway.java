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

package guru.mmp.application.process.bpmn.gateway;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

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
   * @param parent  the BPMN element that is the parent of this Inclusive Gateway
   * @param element the XML element containing the Inclusive Gateway information
   */
  public InclusiveGateway(BaseElement parent, Element element)
  {
    super(parent, element);

    if (true)
    {
      throw new RuntimeException("TODO: IMPLEMENT ME");
    }
  }

  /**
   * Execute the Inclusive Gateway.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Inclusive Gateway
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}
