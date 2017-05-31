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

package guru.mmp.sample.web;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.navigation.NavigationLink;
import guru.mmp.sample.web.pages.DashboardPage;
import guru.mmp.sample.web.pages.HomePage;
import guru.mmp.sample.web.pages.dialogs.TestExtensibleDialogImplementationPage;
import guru.mmp.sample.web.pages.dialogs.TestExtensibleFormDialogImplementationPage;
import guru.mmp.sample.web.pages.forms.TestFormPage;
import org.apache.wicket.Page;
import org.apache.wicket.request.resource.CssResourceReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleApplication</code> provides the implementation of the Wicket Web
 * Application class for the web application.
 *
 * @author Marcus Portmann
 */
@Component("webApplication")
@ComponentScan(basePackages = { "guru.mmp.sample" }, lazyInit = true)
@SpringBootApplication
public class SampleApplication extends TemplateWebApplication
{
  /**
   * Constructs a new <code>TemplateWebApplication</code>.
   */
  @Inject
  public SampleApplication() {}

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args)
  {
    SpringApplication.run(SampleApplication.class, args);
  }

  /**
   * Returns the CSS resource reference for the CSS resource that contains the application styles.
   *
   * @return the CSS resource reference for the CSS resource that contains the application styles
   */
  @Override
  public CssResourceReference getApplicationCssResourceReference()
  {
    return new CssResourceReference(SampleApplication.class, "resources/css/application.css");
  }

  /**
   * Returns the user-friendly name that should be displayed for the application.
   *
   * @return the user-friendly name that should be displayed for the application
   */
  public String getDisplayName()
  {
    return "Sample";
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
  public Class<? extends Page> getSecureHomePage()
  {
    return DashboardPage.class;
  }

  /**
   * Returns the data source that can be used to interact with the application database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Override
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  protected DataSource dataSource()
  {
    return inMemoryDataSource();
  }

  /**
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialise the in-memory application database.
   */
  @Override
  protected List<String> getInMemoryDatabaseInitResources()
  {
    List<String> resources = super.getInMemoryDatabaseInitResources();

    resources.add("guru/mmp/sample/persistence/SampleH2.sql");

    return resources;
  }

  /**
   * Returns the names of the packages to scan for JPA classes.
   *
   * @return the names of the packages to scan for JPA classes
   */
  @Override
  protected List<String> getJpaPackagesToScan()
  {
    List<String> packagesToScan = super.getJpaPackagesToScan();

    packagesToScan.add("guru.mmp.sample");

    return packagesToScan;
  }

  /**
   * Setup the navigation hierarchy for the application.
   *
   * @param root the root of the navigation hierarchy
   */
  @Override
  protected void initNavigation(NavigationGroup root)
  {
    root.addItem(new NavigationLink("Home", "fa fa-home", HomePage.class));
    root.addItem(new NavigationLink("Dashboard", "fa fa-home", DashboardPage.class));

    NavigationGroup dialogsGroup = new NavigationGroup("Dialogs", "fa fa-window-restore");

    dialogsGroup.addItem(new NavigationLink("Test Extensible Dialog",
        TestExtensibleDialogImplementationPage.class));
    dialogsGroup.addItem(new NavigationLink("Test Extensible Form Dialog",
        TestExtensibleFormDialogImplementationPage.class));

    root.addItem(dialogsGroup);

    NavigationGroup formsGroup = new NavigationGroup("Forms", "fa fa-pencil-square-o");

    formsGroup.addItem(new NavigationLink("Test Form", TestFormPage.class));

    root.addItem(formsGroup);

    super.initNavigation(root);
  }
}
