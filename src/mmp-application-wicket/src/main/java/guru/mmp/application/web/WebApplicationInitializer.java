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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;

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
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WebApplicationInitializer.class);
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
    try
    {
      Class<? extends Servlet> dispatcherServletClass = Thread.currentThread()
          .getContextClassLoader().loadClass("org.springframework.web.servlet.DispatcherServlet")
          .asSubclass(Servlet.class);

      ServletRegistration dispatcherServlet = servletContext.addServlet("DispatcherServlet",
          (dispatcherServletClass));
      dispatcherServlet.addMapping("/*");

      dispatcherServlet.setInitParameter("contextClass",
          "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");

      logger.info("Initialising the Spring Dispatcher servlet");
    }
    catch (ClassNotFoundException ignored) {}

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

    FilterRegistration wicketFilter = servletContext.addFilter(WICKET_FILTER_NAME, org.apache.wicket
        .protocol.http.WicketFilter.class);

    wicketFilter.setInitParameter(WicketFilter.APP_FACT_PARAM,
        SpringWebApplicationFactory.class.getName());

    wicketFilter.setInitParameter(WICKET_APPLICATION_BEAN_PARAMETER, "webApplication");
    wicketFilter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    wicketFilter.addMappingForUrlPatterns(null, false, "/*");

    try
    {
      Class<? extends Servlet> viewReportServletClass = Thread.currentThread()
          .getContextClassLoader().loadClass("guru.mmp.application.web.servlets.ViewReportServlet")
          .asSubclass(Servlet.class);

      ServletRegistration viewReportServlet = servletContext.addServlet("ViewReportServlet",
          (viewReportServletClass));
      viewReportServlet.addMapping("/viewReport");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class<? extends Servlet> cxfServletClass = Thread.currentThread().getContextClassLoader()
          .loadClass("org.apache.cxf.transport.servlet.CXFServlet").asSubclass(Servlet.class);

      ServletRegistration cxfServlet = servletContext.addServlet("CXFServlet", (cxfServletClass));
      cxfServlet.addMapping("/service/*");

      logger.info("Initialising the Apache CXF framework");
    }
    catch (ClassNotFoundException ignored) {}
  }
}
