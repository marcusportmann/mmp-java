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
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
@SuppressWarnings("unused")
@Configuration
public class WebApplicationInitializer
  implements ServletContextInitializer
{
  private static final String WICKET_FILTER_NAME = "wicket-filter";
  private static final String WICKET_APPLICATION_BEAN_PARAMETER = "applicationBean";

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

    filter.setInitParameter(WICKET_APPLICATION_BEAN_PARAMETER, "webApplication");
    filter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    filter.addMappingForUrlPatterns(null, false, "/*");
  }
}
