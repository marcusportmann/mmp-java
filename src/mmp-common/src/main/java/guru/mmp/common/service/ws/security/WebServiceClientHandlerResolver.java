/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.common.service.ws.security;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>WebServiceClientHandlerResolver</code> class manages the set of JAX-WS handlers that
 * should be added to a web service client proxy for a web service.
 *
 * @author Marcus Portmann
 */
public class WebServiceClientHandlerResolver
  implements HandlerResolver
{
  private List<SOAPHandler<SOAPMessageContext>> handlers = new ArrayList<>();

  /**
   * Constructs a new <code>WebServiceClientHandlerResolver</code>.
   */
  public WebServiceClientHandlerResolver() {}

  /**
   * Add the JAX-WS handler.
   *
   * @param handler the JAX-WS handler to add
   *
   * the JAX-WS handlers
   */
  public void addHandler(SOAPHandler<SOAPMessageContext> handler)
  {
    handlers.add(handler);
  }

  /**
   * Clear the JAX-WS handlers.
   */
  public void clearHandlers()
  {
    handlers.clear();
  }

  /**
   * Gets the handler chain for the specified port.
   *
   * @param portInfo contains information about the port being accessed
   *
   * @return the handler chain for the specified port
   */
  public List<Handler> getHandlerChain(PortInfo portInfo)
  {
    return Collections.unmodifiableList(handlers);
  }
}
