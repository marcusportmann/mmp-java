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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebApplicationServlet</code> servlet provides a servlet-based alternative to
 * the <code>WebApplicationFilter</code> filter.
 *
 * @author Marcus Portmann
 */
public class WebApplicationServlet extends HttpServlet
{
  /* Logger */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(WebApplicationServlet.class);
  private static final long serialVersionUID = 1000000;

  /* The filter where all the processing is done. */
  protected transient WebApplicationFilter webApplicationFilter;

  /**
   * Cleanup the <code>WebApplicationServlet</code>.
   */
  @Override
  public void destroy()
  {
    if (webApplicationFilter != null)
    {
      webApplicationFilter.destroy();
      webApplicationFilter = null;
    }
  }

  /**
   * Initialise the <code>WebApplicationServlet</code>.
   *
   * @param config the servlet configuration
   *
   * @throws ServletException
   */
  @Override
  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);

    // Create the WebApplicationFilter
    webApplicationFilter = new WebApplicationFilter();

    // Perform dependency injection on the WebApplicationFilter
    try
    {
      WebApplicationInjector webApplicationInjector = new WebApplicationInjector();

      webApplicationInjector.inject(webApplicationFilter);
    }
    catch (Throwable e)
    {
      throw new ServletException("Failed to initialise the WebApplicationServlet: "
          + "Failed to perform CDI injection " + "on the WebApplicationFilter", e);
    }

    // Initialise the WebApplicationFilter
    webApplicationFilter.init(true,
        new FilterConfig()
        {
          /**
           * @see javax.servlet.FilterConfig#getFilterName()
           */
          @Override
          public String getFilterName()
          {
            return WebApplicationServlet.this.getServletName();
          }

          /**
           * @see javax.servlet.FilterConfig#getInitParameter(java.lang.String)
           */
          @Override
          public String getInitParameter(String name)
          {
            return WebApplicationServlet.this.getInitParameter(name);
          }

          /**
           * @see javax.servlet.FilterConfig#getInitParameterNames()
           */
          @Override
          public Enumeration<String> getInitParameterNames()
          {
            return WebApplicationServlet.this.getInitParameterNames();
          }

          /**
           * @see javax.servlet.FilterConfig#getServletContext()
           */
          @Override
          public ServletContext getServletContext()
          {
            return WebApplicationServlet.this.getServletContext();
          }
        });
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    webApplicationFilter.doFilter(request, response, null);
  }

//private static String getURL(final HttpServletRequest request)
//{
//  /*
//   * Servlet 2.3 specification :
//   *
//   * Servlet Path: The path section that directly corresponds to the mapping which activated
//   * this request. This path starts with a "/" character except in the case where the request
//   * is matched with the "/*" pattern, in which case it is the empty string.
//   *
//   * PathInfo: The part of the request path that is not part of the Context Path or the
//   * Servlet Path. It is either null if there is no extra path, or is a string with a leading
//   * "/".
//   */
//  StringBuilder url = new StringBuilder(request.getServletPath());
//
//  final String pathInfo = request.getPathInfo();
//
//  if (pathInfo != null)
//  {
//    url.append(pathInfo);
//  }
//
//  String queryString = request.getQueryString();
//
//  if (queryString != null)
//  {
//    url.append("?").append(queryString);
//  }
//
//  // If url is non-empty it will start with '/', which we should lose
//  if ((url.length()) > 0 && (url.charAt(0) == '/'))
//  {
//    // Remove leading '/'
//    return url.substring(1);
//  }
//  else
//  {
//    return url.toString();
//  }
//}

//private void fallback(final HttpServletRequest request, final HttpServletResponse response)
//  throws IOException
//{
//  /*
//   * The ServletWebRequest is created here to avoid code duplication. The getURL call doesn't
//   * depend on anything wicket specific.
//   */
//  String url = getURL(request);
//
//  // WICKET-2185: strip off query string
//  if (url.indexOf('?') != -1)
//  {
//    url = Strings.beforeFirst(url, '?');
//  }
//
//  /*
//   * Get the relative URL we need for loading the resource from the servlet context.
//   *
//   * NOTE: We NEED to put the '/' in front as otherwise some versions of application servers
//   *       (e.g. Jetty 5.1.x) will fail for requests like '/mysubdir/myfile.css'
//   */
//  if (((url.length() > 0) && (url.charAt(0) != '/')) || (url.length() == 0))
//  {
//    url = '/' + url;
//  }
//
//  InputStream stream = getServletContext().getResourceAsStream(url);
//  String mimeType = getServletContext().getMimeType(url);
//
//  if (stream == null)
//  {
//    if (response.isCommitted())
//    {
//      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//    }
//    else
//    {
//      response.sendError(HttpServletResponse.SC_NOT_FOUND);
//    }
//  }
//  else
//  {
//    if (mimeType != null)
//    {
//      response.setContentType(mimeType);
//    }
//
//    try
//    {
//      Streams.copy(stream, response.getOutputStream());
//    }
//    finally
//    {
//      stream.close();
//    }
//  }
//}
}
