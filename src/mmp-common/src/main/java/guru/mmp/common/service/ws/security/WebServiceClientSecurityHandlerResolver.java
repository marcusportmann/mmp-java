/*
 * Copyright 2014 Marcus Portmann
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
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

/**
 * The <code>WebServiceClientSecurityHandlerResolver</code> class is responsible for adding the
 * JAX-WS handlers to a web service client proxy for a web service that implements the custom
 * Web Service Security Model.
 *
 * @author Marcus Portmann
 */
public class WebServiceClientSecurityHandlerResolver
  implements HandlerResolver
{
  /**
   * Gets the handler chain for the specified port.
   *
   * @param portInfo contains information about the port being accessed
   *
   * @return the handler chain for the specified port
   */
  public List<Handler> getHandlerChain(PortInfo portInfo)
  {
    List<Handler> handlers = new ArrayList<>();

    handlers.add(new WebServiceClientSecurityHandler());

    return handlers;
  }
}
