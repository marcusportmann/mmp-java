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

package guru.mmp.sample.web;

import guru.mmp.application.web.pages.WebPage;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.navigation.NavigationLink;
import guru.mmp.application.web.template.pages.*;
import guru.mmp.sample.web.page.DashboardPage;
import guru.mmp.sample.web.page.HomePage;
import guru.mmp.sample.web.page.forms.TestFormPage;
import guru.mmp.sample.web.page.ui.BlocksPage;
import guru.mmp.sample.web.page.ui.GridPage;
import guru.mmp.sample.web.page.ui.TypographyPage;
import org.apache.wicket.Page;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * The <code>SampleApplication</code> provides the implementation of the Wicket Web
 * Application class for the web application.
 *
 * @author Marcus Portmann
 */
public class SampleApplication
  extends TemplateWebApplication
{
  /**
   * Constructs a new <code>SampleApplication</code>.
   */
  public SampleApplication()
  {
    super("Sample");
  }

  /**
   * Returns the CSS resource reference for the CSS resource that contains the application styles.
   *
   * @return the CSS resource reference for the CSS resource that contains the application styles
   */
  @Override
  public CssResourceReference getApplicationCssResourceReference()
  {
    return new CssResourceReference(SampleApplication.class, "resource/css/application.css");
  }

  /**
   * Returns the home page for the application.
   *
   * @return the home page for the application
   */
  public Class<? extends Page> getHomePage()
  {
    return HomePage.class;
  }

  /**
   * Returns the page that users will be redirected to once they have logged into the application.
   *
   * @return the page that users will be redirected to once they have logged into the application.
   */
  public Class<? extends WebPage> getSecureHomePage()
  {
    return DashboardPage.class;
  }

  /**
   * Abstract method that must be implement by all application-specific <code>WebApplication</code>
   * subclasses to setup the navigation hierarchy for the application.
   *
   * @param root the root of the navigation hierarchy
   */
  protected void initNavigation(NavigationGroup root)
  {
    root.addItem(new NavigationLink("Home", "fa fa-home", HomePage.class));
    root.addItem(new NavigationLink("Dashboard", "fa fa-home", DashboardPage.class));

    NavigationGroup administrationGroup = new NavigationGroup("Administration", "fa fa-gear");

    administrationGroup.addItem(
      new NavigationLink("Codes", "fa fa-list", CodeCategoryAdministrationPage.class));
    administrationGroup.addItem(
      new NavigationLink("Organisations", "fa fa-globe", OrganisationAdministrationPage.class));
    administrationGroup.addItem(new NavigationLink("Process Definitions", "fa fa-gears",
      ProcessDefinitionAdministrationPage.class));
    // TODO: Add Roles here
    administrationGroup.addItem(new NavigationLink("Report Definitions", "fa fa-file-image-o",
      ReportDefinitionAdministrationPage.class));
    administrationGroup.addItem(
      new NavigationLink("User Directories", "fa fa-users", UserDirectoryAdministrationPage.class));

    root.addItem(administrationGroup);

    NavigationGroup securityGroup = new NavigationGroup("Security", "fa fa-shield");
//    securityGroup.addItem(new NavigationLink("General", "fa fa-gear",
//        SecurityAdministrationPage.class));
    securityGroup.addItem(new NavigationLink("Users", "fa fa-user", UserAdministrationPage.class));
    securityGroup.addItem(
      new NavigationLink("Groups", "fa fa-group", GroupAdministrationPage.class));

    root.addItem(securityGroup);

    NavigationGroup uiElementsGroup = new NavigationGroup("UI Elements", "fa fa-sliders");

    uiElementsGroup.addItem(new NavigationLink("Blocks", BlocksPage.class));
    uiElementsGroup.addItem(new NavigationLink("Grid", GridPage.class));
    uiElementsGroup.addItem(new NavigationLink("Typography", TypographyPage.class));

    root.addItem(uiElementsGroup);

    NavigationGroup formsGroup = new NavigationGroup("Forms", "fa fa-pencil-square-o");

    formsGroup.addItem(new NavigationLink("Test Form", TestFormPage.class));

    root.addItem(formsGroup);

    NavigationGroup tablesGroup = new NavigationGroup("Tables", "fa fa-table");

//    tablesGroup.addItem(new NavigationLink("Basic Tables", BasicTablesPage.class));

    root.addItem(tablesGroup);
  }
}
