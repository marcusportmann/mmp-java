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

package guru.mmp.application.web.template;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.page.WebPage;
import guru.mmp.application.web.resource.bootstrap.BootstrapCssResourceReference;
import guru.mmp.application.web.resource.bootstrap
  .BootstrapHoverDropdownJavaScriptResourceReference;
import guru.mmp.application.web.resource.bootstrap.BootstrapJavaScriptResourceReference;
import guru.mmp.application.web.resource.jquery.JQueryCookieJavaScriptResourceReference;
import guru.mmp.application.web.resource.jquery.JQueryJavaScriptResourceReference;
import guru.mmp.application.web.resource.jquery.JQueryUIJavaScriptResourceReference;
import guru.mmp.application.web.resource.less.LessJavaScriptResourceReference;
import guru.mmp.application.web.resource.perfectscrollbar.PerfectScrollbarCssResourceReference;
import guru.mmp.application.web.resource.perfectscrollbar
  .PerfectScrollbarJavaScriptResourceReference;
import guru.mmp.application.web.resource.select2.Select2CssResourceReference;
import guru.mmp.application.web.resource.select2.Select2JavaScriptResourceReference;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.page.LoginPage;
import guru.mmp.application.web.template.resource.*;
import guru.mmp.common.util.StringUtil;

import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * The <code>TemplateWebApplication</code> class provides a base class for all
 * application-specific <code>WebApplication</code> classes for applications that make use
 * of the Web Application Template.
 *
 * @author Marcus Portmann
 */
public abstract class TemplateWebApplication extends guru.mmp.application.web.WebApplication
{
  /* The user-friendly name that should be displayed for the application. */
  private String displayName;

  /* Is multiple organisation support enabled for the application? */
  private boolean isMultipleOrganisationSupportEnabled;

  /* The complete navigation hierarchy for the application. */
  private NavigationGroup navigationRoot;

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
   * Returns the CSS header item for the theme for the Web Application Template.
   *
   * @return the CSS header item for the theme for the Web Application Template
   */
  public static CssHeaderItem getThemeCssHeaderItem()
  {
    return TemplateLightThemeCssResourceReference.getCssHeaderItem();
  }

  /**
   * Returns the CSS resource reference for the theme for the Web Application Template.
   *
   * @return the CSS resource reference for the theme for the Web Application Template
   */
  public static CssResourceReference getThemeCssResourceReference()
  {
    return TemplateLightThemeCssResourceReference.get();
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
  public Class<? extends WebPage> getLoginPage()
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
  public Class<? extends WebPage> getLogoutPage()
  {
    return LoginPage.class;
  }

  /**
   * Returns the complete navigation hierarchy for the application.
   *
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
   *         otherwise
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
    Session session = new TemplateWebSession(request);

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

    // Initialise the CSS resource bundle for the Web Application Template
    getResourceBundles().addCssBundle(TemplateCssResourceReference.class,
        "css/template-web-application.css", BootstrapCssResourceReference.get(),
        PerfectScrollbarCssResourceReference.get(), Select2CssResourceReference.get(),
        TemplateClipFontCssResourceReference.get(),
        TemplateFontAwesomeFontCssResourceReference.get(), TemplateCssResourceReference.get(),
        TemplateResponsiveCssResourceReference.get(), getThemeCssResourceReference());

    // Initialise the JavaScript resource bundle for the Web Application Template
    getResourceBundles().addJavaScriptBundle(TemplateJavaScriptResourceReference.class,
        "js/template-web-application.js", JQueryJavaScriptResourceReference.get(),
        JQueryUIJavaScriptResourceReference.get(), JQueryCookieJavaScriptResourceReference.get(),
        BootstrapJavaScriptResourceReference.get(),
        BootstrapHoverDropdownJavaScriptResourceReference.get(),
        PerfectScrollbarJavaScriptResourceReference.get(),
        Select2JavaScriptResourceReference.get(), LessJavaScriptResourceReference.get(),
        TemplateJavaScriptResourceReference.get());

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
        throw new RuntimeException("Failed to parse the value ("
            + getServletContext().getInitParameter("multipleOrganisationSupportEnabled")
            + ") for the \"multipleOrganisationSupportEnabled\" parameter", e);
      }
    }
  }

  /**
   * This method must be implemented by all application-specific <code>WebApplication</code>
   * subclasses to setup the navigation hierarchy for the application.
   *
   * @param root the root of the navigation hierarchy
   */
  protected abstract void initNavigation(NavigationGroup root);
}
