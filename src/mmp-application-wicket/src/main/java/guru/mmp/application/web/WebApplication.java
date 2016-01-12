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

package guru.mmp.application.web;

import guru.mmp.application.web.template.resources.TemplateJavaScriptResourceReference;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * The <code>WebApplication</code> class provides a base class for all "application specific"
 * Wicket web application classes.
 *
 * @author Marcus Portmann
 */
public abstract class WebApplication
  extends org.apache.wicket.protocol.http.WebApplication
{
  /* Logger */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

  /* The web application injector. */
  private WebApplicationInjector webApplicationInjector;

  /**
   * Constructs a new <code>WebApplication</code>.
   */
  public WebApplication() {}

  /**
   * Returns the page that users will be redirected to in order to login to the application.
   *
   * @return the page that users will be redirected to in order to login to the application
   */
  public abstract Class<? extends Page> getLoginPage();

  /**
   * Returns the page that will log a user out of the application.
   * <p/>
   * A user will be redirected to this page when they attempt to access a secure page which they
   * do not have access to.
   *
   * @return the page that will log a user out of the application
   */
  public abstract Class<? extends Page> getLogoutPage();

  /**
   * Returns the page that users will be redirected to once they have logged into the application.
   * <p/>
   *
   * @return the page that users will be redirected to once they have logged into the application
   */
  public abstract Class<? extends Page> getSecureHomePage();

  /**
   * Sets the web application injector.
   *
   * @return the web application injector
   */
  public WebApplicationInjector getWebApplicationInjector()
  {
    return webApplicationInjector;
  }

  /**
   * Creates a new session.
   *
   * @param request  the request that will create this session
   * @param response the response to initialise, for example with cookies
   *
   * @return the new session
   */
  @Override
  public Session newSession(Request request, Response response)
  {
    Session session = new WebSession(request);

    session.bind();

    return session;
  }

  /**
   * Set the web application injector.
   *
   * @param webApplicationInjector the web application injector
   */
  public void setWebApplicationInjector(WebApplicationInjector webApplicationInjector)
  {
    this.webApplicationInjector = webApplicationInjector;
  }

  @Override
  protected void init()
  {
    super.init();

    getSecuritySettings().setAuthorizationStrategy(new WebAuthorizationStrategy());

    // Override the version of the jQuery library that ships with Wicket
    getJavaScriptLibrarySettings().setJQueryReference(TemplateJavaScriptResourceReference.get());

    if ((System.getProperty("was.install.root") != null) || (System.getProperty("wlp.user.dir") !=
      null))
    {
      setRequestCycleProvider(new WebSphereAbsoluteUrlRequestCycleProvider());
    }
  }

  /**
   * Creates and returns a new instance of <code>IConverterLocator</code>.
   *
   * @return a new <code>IConverterLocator</code> instance
   */
  @Override
  protected IConverterLocator newConverterLocator()
  {
    ConverterLocator converterLocator = new ConverterLocator();

    converterLocator.set(Date.class, new DateConverter()
    {
      private static final long serialVersionUID = 1000000;

      @Override
      public DateFormat getDateFormat(Locale ignore)
      {
        return new SimpleDateFormat("yyyy-MM-dd");
      }
    });

    converterLocator.set(UUID.class, new IConverter<Object>()
    {
      private static final long serialVersionUID = 1000000;

      @Override
      public Object convertToObject(String value, Locale locale)
        throws ConversionException
      {
        return UUID.fromString(value);
      }

      @Override
      public String convertToString(Object value, Locale locale)
      {
        return value.toString();
      }
    });

    return converterLocator;
  }
}
