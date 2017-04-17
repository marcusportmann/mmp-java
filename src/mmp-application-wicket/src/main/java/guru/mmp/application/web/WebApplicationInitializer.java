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
import guru.mmp.application.configuration.ApplicationInitializer;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebApplicationInitializer</code> class implements the
 * <code>ServletContextInitializer</code> that configures the servlets, filters, listeners,
 * context-params and attributes necessary for initializing the Wicket web application framework.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
public abstract class WebApplicationInitializer extends ApplicationInitializer
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
   * Returns the Wicket web application.
   *
   * @return the Wicket web application
   */
  @Bean
  public abstract WebApplication webApplication();
}
