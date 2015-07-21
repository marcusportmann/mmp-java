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

package guru.mmp.common.tomcat;

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.icatch.jta.UserTransactionManager;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

import java.util.logging.Logger;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AtomikosLifecycleListener</code> is a Tomcat Lifecycle Listener that starts and stops
 * the Atomikos Transaction Manager.
 *
 * @author Marcus Portmann
 */
public class AtomikosLifecycleListener
  implements LifecycleListener
{
  Logger logger = Logger.getLogger(AtomikosLifecycleListener.class.getName());
  private UserTransactionManager userTransactionManager;

  /**
   * Process the Tomcat lifecycle event.
   *
   * @param event the Tomcat lifecycle event
   */
  public void lifecycleEvent(LifecycleEvent event)
  {
    if (Lifecycle.START_EVENT.equals(event.getType()))
    {
      if (userTransactionManager == null)
      {
        logger.info("Starting the Atomikos Transaction Manager");
        userTransactionManager = new UserTransactionManager();

        /*
         * logger.info("Binding the Atomikos Transaction Manager in the JNDI tree");
         *
         * InitialContext ic = null;
         *
         * try
         * {
         * ic = new InitialContext();
         *
         * ic.bind("TransactionManager", userTransactionManager);
         * }
         * catch(Throwable e)
         * {
         * logger.severe("Failed to bind the Atomikos Transaction Manager in the JNDI tree: "
         *     + e.getMessage());
         * }
         * finally
         * {
         * if (ic != null)
         * {
         *   try
         *   {
         *     ic.close();
         *   }
         *   catch(Throwable e)
         *   {}
         * }
         * }
         */
      }
    }
    else if (Lifecycle.STOP_EVENT.equals(event.getType()))
    {
      if (userTransactionManager != null)
      {
        logger.info("Shutting down the Atomikos Transaction Manager");
        userTransactionManager.close();
      }
    }
  }
}
