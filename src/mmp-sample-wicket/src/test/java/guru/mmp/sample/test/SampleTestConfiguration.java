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

package guru.mmp.sample.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.test.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleTestConfiguration</code> class provides the Spring configuration for JUnit test
 * classes for the Sample application.
 *
 * @author Marcus Portmann
 */
@Configuration
@ComponentScan(basePackages = { "guru.mmp.sample" }, lazyInit = true)
public class SampleTestConfiguration extends TestConfiguration
{
  /**
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialise the in-memory application database.
   */
  @Override
  protected List<String> getDatabaseInitResources()
  {
    List<String> resources = super.getDatabaseInitResources();

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
}
