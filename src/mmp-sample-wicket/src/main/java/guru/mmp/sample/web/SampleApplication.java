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

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.navigation.NavigationLink;
import guru.mmp.common.util.ResourceUtil;
import guru.mmp.sample.web.pages.DashboardPage;
import guru.mmp.sample.web.pages.HomePage;
import guru.mmp.sample.web.pages.dialogs.TestExtensibleDialogImplementationPage;
import guru.mmp.sample.web.pages.dialogs.TestExtensibleFormDialogImplementationPage;
import guru.mmp.sample.web.pages.forms.TestFormPage;
import guru.mmp.service.sample.ws.SampleServiceEndpoint;
import org.apache.wicket.Page;
import org.apache.wicket.request.resource.CssResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.ws.Endpoint;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleApplication</code> provides the implementation of the Wicket Web
 * Application class for the web application.
 *
 * @author Marcus Portmann
 */
@Component("webApplication")
@ComponentScan(basePackages = { "guru.mmp.sample" }, lazyInit = true)
public class SampleApplication extends TemplateWebApplication
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /* Sample Configuration */
  @Inject
  private SampleConfiguration sampleConfiguration;

  /**
   * Constructs a new <code>SampleApplication</code>.
   */
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
  @Override
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

  /**
   * Returns the Spring bean for the Sample Service web service.
   *
   * @return the Spring bean for the Sample Service web service
   */
  @Bean
  protected Endpoint sampleServiceEndpont()
  {
    return createWebServiceEndpoint("SampleService", new SampleServiceEndpoint(),
        "SampleService.wsdl");
  }

  /**
   * Initialise the Sample application.
   */
  @PostConstruct
  private void initSampleApplication()
  {
    try
    {
      byte[] sampleReportDefinitionData = ResourceUtil.getClasspathResource(
          "guru/mmp/sample/report/SampleReport.jasper");

      ReportDefinition sampleReportDefinition = new ReportDefinition(UUID.fromString(
          "2a4b74e8-7f03-416f-b058-b35bb06944ef"), "Sample Report", sampleReportDefinitionData);

      if (!reportingService.reportDefinitionExists(sampleReportDefinition.getId()))
      {
        reportingService.saveReportDefinition(sampleReportDefinition);
        logger.info("Saved the \"Sample Report\" report definition");
      }
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the Sample application data", e);
    }
  }
}
