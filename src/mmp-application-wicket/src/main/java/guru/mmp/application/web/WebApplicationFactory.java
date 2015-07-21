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

package guru.mmp.application.web;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import javax.naming.InitialContext;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebApplicationFactory</code> class implements the <code>IWebApplicationFactory</code>
 * interface and creates <code>WebApplication</code> instances.
 *
 * @author Marcus Portmann
 */
public class WebApplicationFactory
  implements IWebApplicationFactory
{
  /**
   * Constructs a new <code>WebApplicationFactory</code>.
   */
  public WebApplicationFactory() {}

  /**
   * Create the <code>WebApplication</code> instance.
   *
   * @param filter the wicket filter
   *
   * @return the <code>WebApplication</code> instance
   */
  public WebApplication createApplication(WicketFilter filter)
  {
    String applicationClass = null;

    try
    {
      applicationClass = InitialContext.doLookup("java:app/env/ApplicationClass");
    }
    catch (Throwable ignored) {}

    if (applicationClass == null)
    {
      try
      {
        applicationClass = InitialContext.doLookup("java:comp/env/ApplicationClass");
      }
      catch (Throwable ignored) {}
    }

    if (applicationClass == null)
    {
      throw new WebApplicationException("Failed to retrieve the web application class using the"
          + " JNDI environment entries (java:app/env/ApplicationClass)"
          + " and (java:comp/env/ApplicationClass)");
    }

    Object webApplicationObject;

    try
    {
      Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(applicationClass);

      webApplicationObject = clazz.newInstance();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("The web application class (" + applicationClass + ")"
          + " could not be instantiated", e);
    }

    try
    {
      WebApplicationInjector webApplicationInjector = new WebApplicationInjector();

      guru.mmp.application.web.WebApplication webApplication =
        guru.mmp.application.web.WebApplication.class.cast(webApplicationObject);

      webApplication.setWebApplicationInjector(webApplicationInjector);

      webApplicationInjector.inject(webApplicationObject);

      // CDIUtil.inject(webApplication, true);

      webApplication.getComponentInstantiationListeners().add(webApplicationInjector);

      return webApplication;
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to create the WebApplication: " + e.getMessage(),
          e);
    }
  }

  /**
   * Called when the filter instance that used this factory is destroyed.
   *
   * @param filter the wicket filter
   */
  public void destroy(WicketFilter filter) {}
}
