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

package guru.mmp.common.ws.security;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>WSSUsernameTokenSecurityHandlerResolver</code> class is responsible for adding the
 * JAX-WS handlers to a web service client proxy for a web service that implements the
 * WS-Security Username Token security model for JAX-WS web service clients.
 *
 * @author Marcus Portmann
 */
public class WSSUsernameTokenSecurityHandlerResolver
  implements HandlerResolver
{
  /**
   * The password to use when accessing a web service with WS-Security Username Token security
   * enabled.
   */
  private String password;

  /**
   * The username to use when accessing a web service with WS-Security Username Token security
   * enabled.
   */
  private String username;

  /**
   * Constructs a new <code>UsernamePasswordSecurityHandlerResolver</code>.
   *
   * @param username the username to use when accessing a web service with WS-Security Username
   *                 Token security enabled
   * @param password the password to use when accessing a web service with WS-Security Username
   *                 Token security enabled
   */
  public WSSUsernameTokenSecurityHandlerResolver(String username, String password)
  {
    this.username = username;
    this.password = password;
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
    ArrayList<Handler> handlers = new ArrayList<>();

    handlers.add(new WSSUsernameTokenSecurityHandler(username, password));

    return handlers;
  }
}
