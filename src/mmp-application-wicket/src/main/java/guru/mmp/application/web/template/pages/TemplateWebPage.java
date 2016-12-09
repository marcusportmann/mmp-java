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

package guru.mmp.application.web.template.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPage;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.components.*;
import guru.mmp.application.web.template.resources.TemplateCssResourceReference;
import guru.mmp.application.web.template.resources.TemplateJavaScriptResourceReference;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The <code>TemplateWebPage</code> class is the base class that all Wicket web page classes must
 * be derived from in web applications that make use of the Web Application Template.
 *
 * @author Marcus Portmann
 */
public abstract class TemplateWebPage extends WebPage
{
  private static final long serialVersionUID = 1000000;
  private transient static CssReferenceHeaderItem applicationCssHeaderItem;
  private Alerts alerts;

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param heading the page heading
   */
  protected TemplateWebPage(String heading)
  {
    super();

    commonInit(Model.of(heading), Model.of(""));
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param headingModel    the model for the page heading
   * @param subHeadingModel the model for the sub-heading for the page
   */
  public TemplateWebPage(IModel<String> headingModel, IModel<String> subHeadingModel)
  {
    super();

    commonInit(headingModel, subHeadingModel);
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param heading        the page heading
   * @param pageParameters the parameters for the page
   */
  protected TemplateWebPage(String heading, PageParameters pageParameters)
  {
    super(pageParameters);

    commonInit(Model.of(heading), Model.of(""));
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param heading    the page heading
   * @param subHeading the sub-heading for the page
   */
  public TemplateWebPage(String heading, String subHeading)
  {
    this(Model.of(heading), Model.of(subHeading));
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param headingModel    the model for the page heading
   * @param subHeadingModel the model for the sub-heading for the page
   * @param model           the model for the page
   */
  public TemplateWebPage(IModel<String> headingModel, IModel<String> subHeadingModel,
      IModel<?> model)
  {
    super(model);

    commonInit(headingModel, subHeadingModel);
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param headingModel    the model for the page heading
   * @param subHeadingModel the model for the sub-heading for the page
   * @param pageParameters  the model for the page
   */
  public TemplateWebPage(IModel<String> headingModel, IModel<String> subHeadingModel,
      PageParameters pageParameters)
  {
    super(pageParameters);

    commonInit(headingModel, subHeadingModel);
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param heading    the page heading
   * @param subHeading the sub-heading for the page
   * @param model      the model for the page
   */
  public TemplateWebPage(String heading, String subHeading, IModel<?> model)
  {
    super(model);

    commonInit(Model.of(heading), Model.of(subHeading));
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param heading        the page heading
   * @param subHeading     the sub-heading for the page
   * @param pageParameters the parameters for the page
   */
  protected TemplateWebPage(String heading, String subHeading, PageParameters pageParameters)
  {
    super(pageParameters);

    commonInit(Model.of(heading), Model.of(subHeading));
  }

  /**
   * Returns the alerts.
   *
   * @return the alerts
   */
  public Alerts getAlerts()
  {
    return alerts;
  }

  /**
   * Render to the web response whatever the page wants to contribute to the head section.
   *
   * @param response the header response
   */
  @Override
  public void renderHead(IHeaderResponse response)
  {
    super.renderHead(response);

    // Add the Web Application Template theme CSS header item
    response.render(TemplateCssResourceReference.getCssHeaderItem());

    // Add the application CSS header item
    response.render(getApplicationCssHeaderItem());

    // Add the Web Application Template JavaScript header item
    response.render(TemplateJavaScriptResourceReference.getJavaScriptHeaderItem());
  }

  /**
   * Returns the template web application.
   *
   * @return the template web application
   */
  protected TemplateWebApplication getWebApplication()
  {
    return (TemplateWebApplication) getApplication();
  }

  private void commonInit(IModel<String> headingModel, IModel<String> subHeadingModel)
  {
    try
    {
      if (getApplication().usesDevelopmentConfig())
      {
        add(new DebugBar("debug"));
      }
      else
      {
        add(new Label("debug", ""));
      }

      // Setup the page title
      Label applicationNameLabel = new Label("applicationName", Model.of(
          getWebApplication().getDisplayName()));
      add(applicationNameLabel);

      Label pageTitleLabel = new Label("pageTitle", headingModel);
      add(pageTitleLabel);

      // Setup the backend user panel
      add(new BackendUserPanel("backendUserPanel"));

      // Setup the backend main navigation
      add(new BackendMainNavigation("backendMainNavigation"));

      // Setup the backend user menu
      add(new BackendUserMenu("backendUserMenu"));

      // Setup the breadcrumbs
      add(new Breadcrumbs("breadcrumbs"));

      // Setup the page heading
      add(new Label("pageHeading", headingModel));

      // Setup the page sub-heading
      add(new Label("pageSubHeading", subHeadingModel));

      // Setup the alerts
      this.alerts = new Alerts("alerts");

      add(alerts);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the TemplateWebPage", e);
    }
  }

  private CssReferenceHeaderItem getApplicationCssHeaderItem()
  {
    if (applicationCssHeaderItem == null)
    {
      applicationCssHeaderItem = CssHeaderItem.forReference(
          getWebApplication().getApplicationCssResourceReference());
    }

    return applicationCssHeaderItem;
  }
}
