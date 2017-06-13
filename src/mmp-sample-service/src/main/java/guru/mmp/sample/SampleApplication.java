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

package guru.mmp.sample;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Application;
import guru.mmp.service.sample.ws.SampleServiceEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.inject.Inject;
import javax.xml.ws.Endpoint;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleApplication</code> provides the implementation of the Wicket Web
 * Application class for the web application.
 *
 * @author Marcus Portmann
 */
@ComponentScan(basePackages = { "guru.mmp.sample" }, lazyInit = true)
public class SampleApplication extends Application
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

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
}
