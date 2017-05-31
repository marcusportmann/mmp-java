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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleTestConfiguration</code> class provides the Spring configuration for JUnit test
 * classes for the Sample application.
 *
 * @author Marcus Portmann
 */
@ComponentScan(basePackages = { "guru.mmp.sample" })
public class SampleTestConfiguration extends TestConfiguration
{
  /**
   * Constructs a new <code>SampleTestConfiguration</code>.
   *
   * @param transactionManager the transaction manager
   */
  @Inject
  public SampleTestConfiguration(PlatformTransactionManager transactionManager)
  {
    super(transactionManager);
  }

  /**
   * Returns the application entity manager factory associated with the application data source.
   *
   * @return the application entity manager factory associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  @Override
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory()
  {
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
        super.applicationEntityManagerFactory();

    localContainerEntityManagerFactoryBean.setPackagesToScan("guru.mmp.application",
        "guru.mmp.sample");

    return localContainerEntityManagerFactoryBean;
  }

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
}
