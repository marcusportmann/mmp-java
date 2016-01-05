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

package guru.mmp.application.web.template;

import guru.mmp.application.web.resource.thirdparty.greensockjs
  .TweenLiteJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.moment.MomentJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.rwdtable.RWDTableJavaScriptResourceReference;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.navigation.NavigationItem;
import guru.mmp.application.web.template.navigation.NavigationLink;
import guru.mmp.application.web.template.page.LoginPage;
import guru.mmp.application.web.template.page.LogoutPage;
import guru.mmp.application.web.template.resource.TemplateBootstrapJavaScriptResourceReference;
import guru.mmp.application.web.template.resource.TemplateCombinedJavaScriptResourceReference;
import guru.mmp.application.web.template.resource.TemplateCoreJavaScriptResourceReference;
import guru.mmp.application.web.template.resource.TemplateJavaScriptResourceReference;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.CssResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>TemplateWebApplication</code> class provides a base class for all
 * application-specific <code>WebApplication</code> classes for applications that make use
 * of the Web Application Template.
 *
 * @author Marcus Portmann
 */
public abstract class TemplateWebApplication
  extends guru.mmp.application.web.WebApplication
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TemplateWebApplication.class);

  /* The user-friendly name that should be displayed for the application. */
  private String displayName;

  /* Is multiple organisation support enabled for the application? */
  private boolean isMultipleOrganisationSupportEnabled;

  /* The complete navigation hierarchy for the application. */
  private NavigationGroup navigationRoot;

  /**
   * Returns the <code>TemplateWebApplication</code> instance.
   *
   * @return the <code>TemplateWebApplication</code> instance
   */
  public static TemplateWebApplication getTemplateWebApplication()
  {
    return (TemplateWebApplication) get();
  }

  /**
   * Constructs a new <code>TemplateWebApplication</code>.
   *
   * @param displayName the user-friendly name that should be displayed for the application
   */
  public TemplateWebApplication(String displayName)
  {
    this.displayName = displayName;
  }

  /**
   * Returns the CSS resource reference for the CSS resource that contains the application styles.
   *
   * @return the CSS resource reference for the CSS resource that contains the application styles
   */
  public abstract CssResourceReference getApplicationCssResourceReference();

  /**
   * Returns the user-friendly name that should be displayed for the application.
   *
   * @return the user-friendly name that should be displayed for the application
   */
  public String getDisplayName()
  {
    return displayName;
  }

  /**
   * Returns the default page that users will be redirected to in order to login to the application.
   *
   * @return the default page that users will be redirected to in order to login to the application
   */
  public Class<? extends Page> getLoginPage()
  {
    return LoginPage.class;
  }

  /**
   * Returns the default page that will log a user out of the application.
   * <p/>
   * A user will be redirected to this page when they attempt to access a secure page which they
   * do not have access to.
   *
   * @return the default page that will log a user out of the application
   */
  public Class<? extends Page> getLogoutPage()
  {
    return LogoutPage.class;
  }

  /**
   * Returns the complete navigation hierarchy for the application.
   * <p/>
   * This hierarchy is not filter according to the security permissions of specific users.
   *
   * @return the complete navigation hierarchy for the application
   */
  public NavigationGroup getNavigation()
  {
    return navigationRoot;
  }

  /**
   * Returns <code>true</code> if multiple organisation support is enabled or <code>false</code>
   * otherwise.
   *
   * @return <code>true</code> if multiple organisation support is enabled or <code>false</code>
   * otherwise
   */
  public boolean isMultipleOrganisationSupportEnabled()
  {
    return isMultipleOrganisationSupportEnabled;
  }

  /**
   * Creates a new session.
   *
   * @param request  the request that will create this session
   * @param response the response to initialise, for example with cookies
   *
   * @return the new session
   */
  @Override
  public Session newSession(Request request, Response response)
  {
    TemplateWebSession session = new TemplateWebSession(request);

    session.bind();

    return session;
  }

  /**
   * Initialise the application.
   */
  @Override
  protected void init()
  {
    super.init();

    // Initialise the navigation hierarchy for the application
    navigationRoot = new NavigationGroup("");
    initNavigation(navigationRoot);

/*
    // Initialise the template-web-application.css resource bundle for the Web Application Template
    getResourceBundles().addCssBundle(TemplateCssResourceReference.class,
        "css/template-web-application.css", TemplateBootstrapCssResourceReference.get(),
        TemplateCoreCssResourceReference.get(), TemplateCssResourceReference.get());
*/
    // Initialise the template-web-application.js resource bundle for the Web Application Template
    getResourceBundles().addJavaScriptBundle(TemplateJavaScriptResourceReference.class,
      "js/template-web-application.js", MomentJavaScriptResourceReference.get(),
      TweenLiteJavaScriptResourceReference.get(), RWDTableJavaScriptResourceReference.get(),
      TemplateBootstrapJavaScriptResourceReference.get(),
      TemplateCombinedJavaScriptResourceReference.get(),
      TemplateCoreJavaScriptResourceReference.get(), TemplateJavaScriptResourceReference.get());

    // Check it multiple organisation support is enabled
    if (!StringUtil.isNullOrEmpty(
      getServletContext().getInitParameter("multipleOrganisationSupportEnabled")))
    {
      try
      {
        isMultipleOrganisationSupportEnabled = Boolean.parseBoolean(
          getServletContext().getInitParameter("multipleOrganisationSupportEnabled"));
      }
      catch (Throwable e)
      {
        throw new RuntimeException(String.format(
          "Failed to parse the value (%s) for the \"multipleOrganisationSupportEnabled\" parameter",
          getServletContext().getInitParameter("multipleOrganisationSupportEnabled")), e);
      }
    }

    // Mount the navigable pages
    mountPage("/home", getHomePage());
    mountPage("/secure-home", getSecureHomePage());
    mountPage("/login", getLoginPage());
    mountPage("/logout", getLogoutPage());
    mountNavigationPages(getNavigation(), "");
  }

  /**
   * This method must be implemented by all application-specific <code>WebApplication</code>
   * subclasses to setup the navigation hierarchy for the application.
   *
   * @param root the root of the navigation hierarchy
   */
  protected abstract void initNavigation(NavigationGroup root);

  private String getNavigationItemPathComponent(NavigationItem navigationItem)
  {
    StringBuilder pathComponent = new StringBuilder();

    for (String nameComponent : navigationItem.getName().split(" "))
    {
      if (pathComponent.length() > 0)
      {
        pathComponent.append("-");
      }

      if (nameComponent.equals("&") || nameComponent.equals("&amp;"))
      {
        pathComponent.append("and");
      }
      else
      {
        pathComponent.append(nameComponent.toLowerCase());
      }
    }

    return pathComponent.toString();
  }

  private void mountNavigationPages(NavigationGroup navigationGroup, String path)
  {
    for (NavigationItem navigationItem : navigationGroup.getItems())
    {
      if (navigationItem instanceof NavigationGroup)
      {
        String navigationItemPath = path + "/" + getNavigationItemPathComponent(navigationItem);

        mountNavigationPages(((NavigationGroup) navigationItem), navigationItemPath);
      }
      else if (navigationItem instanceof NavigationLink)
      {
        String navigationItemPath = path + "/" + getNavigationItemPathComponent(navigationItem);

        NavigationLink navigationLink = (NavigationLink) navigationItem;

        if (getHomePage().isAssignableFrom(navigationLink.getPageClass()))
        {
          logger.debug(
            String.format("Ignoring home page navigation item \"%s\" under the path \"%s\"",
              navigationLink.getName(), navigationItemPath));
        }
        else if (getSecureHomePage().isAssignableFrom(navigationLink.getPageClass()))
        {
          logger.debug(
            String.format("Ignoring secure home page navigation item \"%s\" under the path \"%s\"",
              navigationLink.getName(), navigationItemPath));
        }
        else if (getLoginPage().isAssignableFrom(navigationLink.getPageClass()))
        {
          logger.debug(
            String.format("Ignoring login page navigation item \"%s\" under the path \"%s\"",
              navigationLink.getName(), navigationItemPath));
        }
        else if (getLogoutPage().isAssignableFrom(navigationLink.getPageClass()))
        {
          logger.debug(
            String.format("Ignoring logout page navigation item \"%s\" under the path \"%s\"",
              navigationLink.getName(), navigationItemPath));
        }
        else
        {
          mountPage(navigationItemPath, navigationLink.getPageClass());

          logger.info(String.format("Mounting the page \"%s\" under the path \"%s\"",
            navigationLink.getPageClass().getName(), navigationItemPath));
        }
      }
    }
  }
}
