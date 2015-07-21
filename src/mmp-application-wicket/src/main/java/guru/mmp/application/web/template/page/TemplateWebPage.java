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

package guru.mmp.application.web.template.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.page.WebPage;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.component.*;
import guru.mmp.application.web.template.resource.TemplateJavaScriptResourceReference;
import org.apache.wicket.PageReference;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;

/**
 * The <code>TemplateWebPage</code> class is the base class that all Wicket web page classes must
 * be derived from in web applications that make use of the Web Application Template.
 *
 * @author Marcus Portmann
 */
public abstract class TemplateWebPage extends WebPage
{
  private transient static CssReferenceHeaderItem applicationCssHeaderItem;
  private static final long serialVersionUID = 1000000;
  private Alerts alerts;
  private String heading;
  private String subHeading;
  private String title;

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param title   the page title
   * @param heading the page heading
   */
  public TemplateWebPage(String title, String heading)
  {
    this(title, heading, null, null);
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param title        the page title
   * @param heading      the page heading
   * @param previousPage the previous page or <code>null</code> if there is no previous page
   */
  public TemplateWebPage(String title, String heading, PageReference previousPage)
  {
    this(title, heading, null, previousPage);
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param title      the page title
   * @param heading    the page heading
   * @param subHeading the sub-heading for the page
   */
  public TemplateWebPage(String title, String heading, String subHeading)
  {
    this(title, heading, subHeading, null);
  }

  /**
   * Constructs a new <code>TemplateWebPage</code>.
   *
   * @param title        the page title
   * @param heading      the page heading
   * @param subHeading   the sub-heading for the page
   * @param previousPage the previous page or <code>null</code> if there is no previous page
   */
  public TemplateWebPage(final String title, final String heading, final String subHeading,
      final PageReference previousPage)
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
    this.title = title;

    Label titleLabel = new Label("pageTitle", new PropertyModel<String>(this, "title"));
    titleLabel.setRenderBodyOnly(false);
    add(titleLabel);

    // Setup the top navigation menu
    add(new TopNavigationMenu("topNavigationMenu"));

    // Setup the main navigation menu
    add(new MainNavigationMenu("mainNavigationMenu"));

    // Setup the breadcrumbs
    add(new Breadcrumbs("breadcrumbs"));

    // Setup the page heading
    this.heading = heading;

    Label headingLabel = new Label("pageHeading", new PropertyModel<String>(this, "heading"));
    headingLabel.setRenderBodyOnly(true);
    add(headingLabel);

    // Setup the page sub-heading
    this.subHeading = subHeading;

    Label subHeadingLabel = new Label("pageSubHeading",
      new PropertyModel<String>(this, "subHeading"));
    add(subHeadingLabel);

    // Setup the "backLink" link if required
    if (previousPage == null)
    {
      BlankComponent blankComponent = new BlankComponent("backLink");

      add(blankComponent);
    }
    else
    {
      Link<Void> backLink = new Link<Void>("backLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          setResponsePage(previousPage.getPage());
        }
      };

      add(backLink);
    }

    // Setup the alerts
    this.alerts = new Alerts("alerts");
    add(alerts);
  }

  /**
   * Hidden constructor for the the <code>TemplateWebPage</code> class.
   */
  protected TemplateWebPage() {}

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
   * Returns the page heading.
   *
   * @return the page heading
   */
  public String getHeading()
  {
    return heading;
  }

  /**
   * Returns the page sub-heading.
   *
   * @return the page sub-heading
   */
  public String getSubHeading()
  {
    return subHeading;
  }

  /**
   * Returns the page title.
   *
   * @return the page title
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Returns the template web application.
   *
   * @return the template web application
   */
  public TemplateWebApplication getWebApplication()
  {
    return (TemplateWebApplication) getApplication();
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
    response.render(TemplateWebApplication.getThemeCssHeaderItem());

    // Add the application CSS header item
    response.render(getApplicationCssHeaderItem());

    // Add the Web Application Template JavaScript header item
    response.render(TemplateJavaScriptResourceReference.getJavaScriptHeaderItem());

    // Add the JavaScript script that should be executed when the DOM is ready
    response.render(OnDomReadyHeaderItem.forScript("TemplateWebApplication.init();"));
  }

  /**
   * Set the page heading.
   *
   * @param heading the heading to set
   */
  public void setHeading(String heading)
  {
    this.heading = heading;
  }

  /**
   * Set the page sub-heading.
   *
   * @param subHeading the sub-heading to set
   */
  public void setSubHeading(String subHeading)
  {
    this.subHeading = subHeading;
  }

  /**
   * Set the page title.
   *
   * @param title the new page title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  private CssReferenceHeaderItem getApplicationCssHeaderItem()
  {
    if (applicationCssHeaderItem == null)
    {
      applicationCssHeaderItem =
        CssHeaderItem.forReference(getWebApplication().getApplicationCssResourceReference());
    }

    return applicationCssHeaderItem;
  }
}
