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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.security.context.ServiceSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SecureWebServiceContextListener</code> context listener supports the development of
 * secure JAX-WS web services using various JAX-WS frameworks. This context listener acts as a
 * replacement for the framework-specific context listener.
 *
 * @author Marcus Portmann
 */
public class SecureWebServiceListener
  implements ServletContextListener
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SecureWebServiceListener.class);

  /**
   * Constructs a new <code>SecureWebServiceContextListener</code>.
   */
  public SecureWebServiceListener() {}

  /**
   * @param event the context event
   * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
   */
  public void contextDestroyed(ServletContextEvent event) {}

  /**
   * @param event the context event
   * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
   */
  public void contextInitialized(ServletContextEvent event)
  {
    String applicationName = null;

    // Retrieve the application name from JNDI
    try
    {
      applicationName = InitialContext.doLookup("java:app/AppName");
    }
    catch (Throwable ignored) {}

    if (applicationName == null)
    {
      logger.error("Failed to initialise the web service security context:"
          + " Failed to retrieve the application name from JNDI using the path (java:app/AppName)");

      return;
    }

    // Initialise the JAX-WS web services security configuration
    if (ServiceSecurityContext.getContext().exists(applicationName))
    {
      logger.info("Initialising the web service security context using the configuration file"
          + " (META-INF/" + applicationName + ".ServiceSecurity)");

      try
      {
        ServiceSecurityContext.getContext().init(applicationName);
      }
      catch (Throwable e)
      {
        logger.error("Failed to initialise the web service security context using the"
            + " configuration file (META-INF/" + applicationName + ".ServiceSecurity)", e);

        return;
      }
    }
  }
}
