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

package guru.mmp.application.web;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Properties;
import java.util.concurrent.Executor;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebApplicationInitializer</code> class implements the
 * <code>ServletContextInitializer</code> that configures the servlets, filters, listeners,
 * context-params and attributes necessary for initializing the Wicket web application framework.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories
@ComponentScan(basePackages = { "guru.mmp.application" })
public abstract class WebApplicationInitializer
  implements ServletContextInitializer
{
  private static final String WICKET_FILTER_NAME = "wicket-filter";

  /**
   * Configure the given {@link ServletContext} with any servlets, filters, listeners,
   * context-params and attributes necessary for initialization.
   *
   * @param servletContext the {@code ServletContext} to initialize
   */
  @Override
  public void onStartup(ServletContext servletContext)
    throws ServletException
  {
    if (Debug.inDebugMode())
    {
      servletContext.setInitParameter("configuration", "development");
    }
    else
    {
      servletContext.setInitParameter("configuration", "deployment");
    }

    // Is Multiple Organisation Support Enabled
    servletContext.setInitParameter("multipleOrganisationSupportEnabled", "true");

    FilterRegistration filter = servletContext.addFilter(WICKET_FILTER_NAME, org.apache.wicket
        .protocol.http.WicketFilter.class);

    filter.setInitParameter(WicketFilter.APP_FACT_PARAM,
        SpringWebApplicationFactory.class.getName());

    // filter.setInitParameter("applicationBean", beanNamesForType[0]);

//  filter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, props.getFilterMappingParam());
//  filter.addMappingForUrlPatterns(null, false, props.getFilterMappingParam());

//  Map<String, String> initParameters = props.getInitParameters();
//  for (Entry<String, String> initParam : initParameters.entrySet()) {
//    filter.setInitParameter(initParam.getKey(), initParam.getValue());
//  }

//  wicketEndpointRepository.add(new WicketAutoConfig.Builder(this.getClass())
//    .withDetail("wicketFilterName", WICKET_FILTERNAME)
//    .withDetail("wicketFilterClass", wicketWebInitializerConfig.filterClass())
//    .withDetail("properties", props)
//    .build());
//
//  WicketEndpointRepository

  }

  /**
   * Returns the application entity manager factory associated with the application data source.
   *
   * @return the application entity manager factory associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory()
  {
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
      new LocalContainerEntityManagerFactoryBean();

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(true);
    jpaVendorAdapter.setDatabase(Database.H2);

    localContainerEntityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
    localContainerEntityManagerFactoryBean.setJtaDataSource(dataSource());
    localContainerEntityManagerFactoryBean.setPackagesToScan("guru.mmp.application");
    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

    Properties properties = new Properties();

    properties.setProperty("hibernate.transaction.jta.platform",
      AtomikosJtaPlatform.class.getName());

    localContainerEntityManagerFactoryBean.setJpaProperties(properties);

    localContainerEntityManagerFactoryBean.afterPropertiesSet();

    return localContainerEntityManagerFactoryBean;
  }

  /**
   * Returns the Spring task executor to use for @Async method invocations.
   *
   * @return the Spring task executor to use for @Async method invocations
   */
  @Bean
  public Executor taskExecutor()
  {
    return new SimpleAsyncTaskExecutor();
  }

  /**
   * Returns the Spring task scheduler.
   *
   * @return the Spring task scheduler
   */
  @Bean
  public TaskScheduler taskScheduler()
  {
    return new ConcurrentTaskScheduler();
  }

  /**
   * Returns the Wicket web application.
   *
   * @return the Wicket web application
   */
  @Bean
  public abstract WebApplication webApplication();
}
