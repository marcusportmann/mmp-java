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

package guru.mmp.application.web;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import guru.mmp.application.web.page.SecureAnonymousWebPage;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.common.util.StringUtil;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.settings.ISecuritySettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>WebAuthorizationStrategy</code> class provides an implementation of an authorization
 * strategy as defined by the <code>IAuthorizationStrategy</code> interface for Wicket web
 * applications making use of the web application library.
 *
 * @author Marcus Portmann
 */
public class WebAuthorizationStrategy
  implements IAuthorizationStrategy
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WebAuthorizationStrategy.class);

  /**
   * Gets whether the given action is permitted.
   *
   * @param component the component to be acted upon
   * @param action    the action to authorize on the component
   *
   * @return <code>true</code> if the action is permitted or <code>false</code> otherwise
   */
  public boolean isActionAuthorized(Component component, Action action)
  {
//  if (component instanceof WebPage)
//  {
//    WebPage page = (WebPage) component;
//
//    if (page.isSecure())
//    {
//      WebSession session = (WebSession) component.getSession();
//
//      if (!session.hasAcccessToFunction(page.getFunctionCode()))
//      {
//        return false;
//      }
//    }
//  }

    return true;
  }

  /**
   * Checks whether an instance of the given component class may be created. If this method
   * returns false, the {@link IUnauthorizedComponentInstantiationListener} that is configured in
   * the {@link ISecuritySettings security settings} will be called. The default implementation of
   * that listener throws a {@link UnauthorizedInstantiationException}.
   * <p>
   * If you wish to implement a strategy that authenticates users which cannot access a given Page
   * (or other Component), you can simply throw a
   * {@link org.apache.wicket.RestartResponseAtInterceptPageException} in your implementation of
   * this method.
   *
   * @param <T> the type of component
   *
   * @param componentClass the component class to check
   *
   * @return <code>true</code> if the given component may be created or <code>false</code>
   *         otherwise
   */
  public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
      Class<T> componentClass)
  {
    if (guru.mmp.application.web.page.WebPage.class.isAssignableFrom(componentClass))
    {
      WebPageSecurity webPageSecurity = componentClass.getAnnotation(WebPageSecurity.class);

      if (webPageSecurity != null)
      {
        WebSession session = (WebSession) WebSession.get();

        if (session != null)
        {
          if ((!session.isUserLoggedIn())
              || (!session.hasAcccessToFunction(webPageSecurity.value())))
          {
            logger.warn("The user (" + (StringUtil.isNullOrEmpty(session.getUsername())
                ? "Unknown"
                : session.getUsername()) + ") does not have access to the function ("
                + webPageSecurity.value() + ")");

            if (Debug.inDebugMode())
            {
              logger.info("The user (" + (StringUtil.isNullOrEmpty(session.getUsername())
                  ? "Unknown"
                  : session.getUsername()) + ") has access to the following functions: "
                  + ((session.getFunctionCodes().size() == 0)
                  ? "None"
                  : StringUtil.delimit(session.getFunctionCodes(), ",")));
            }

            session.invalidate();

            throw new RestartResponseAtInterceptPageException(
                ((WebApplication) session.getApplication()).getLogoutPage());
          }
        }
      }
      else
      {
        SecureAnonymousWebPage secureAnonymousWebPage =
          componentClass.getAnnotation(SecureAnonymousWebPage.class);

        if (secureAnonymousWebPage != null)
        {
          WebSession session = (WebSession) WebSession.get();

          if (!session.isUserLoggedIn())
          {
            session.invalidate();

            throw new RestartResponseAtInterceptPageException(
                ((WebApplication) session.getApplication()).getLogoutPage());
          }
        }
      }
    }

    return true;
  }
}
