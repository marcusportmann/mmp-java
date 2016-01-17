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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.io.IOException;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebApplicationFilter</code> extends the Wicket filter by initialising and managing the
 * Transaction associated with each web request.
 *
 * @author Marcus Portmann
 */
public class WebApplicationFilter extends org.apache.wicket.protocol.http.WicketFilter
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WebApplicationFilter.class);

  /* User Transaction */
  @Inject
  private UserTransaction userTransaction;

  /* The prefix for all Wicket resource URIs. */
  private String wicketResourceUriPrefix;

  /* The prefix for all Wicket URIs. */
  private String wicketUriPrefix;

  /**
   * Called by the web container to indicate to a filter that it is being taken out of service.
   */
  @Override
  public void destroy() {}

  /**
   * The doFilter method of the Filter is called by the container each time a request/response pair
   * is passed through the chain due to a client request for a resource at the end of the chain.
   * The <code>FilterChain</code> passed in to this method allows the Filter to pass on the request
   * and response to the next entity in the chain.
   *
   * @param request  the request
   * @param response the response
   * @param chain    the filter chain
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    boolean isWicketRequest = isWicketRequest(request);

    boolean startedNewTransaction = false;

    if (isWicketRequest)
    {
      // Start a new JTA transaction if one is not present
      try
      {
        if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
        {
          userTransaction.begin();

          startedNewTransaction = true;
        }
      }
      catch (Throwable e)
      {
        throw new ServletException("Failed to start the JTA transaction for the request", e);
      }
    }

    try
    {
      super.doFilter(request, response, chain);
    }
    catch (java.io.FileNotFoundException e)
    {
      if (response instanceof HttpServletResponse)
      {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, String.format(
            "The requested resource (%s) could not be found", StringUtil.notNull(getRequestURI(
            request))));
      }
    }
    finally
    {
      if (startedNewTransaction)
      {
        try
        {
          if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
          {
            userTransaction.commit();
          }
          else if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
          {
            userTransaction.rollback();

            if (logger.isDebugEnabled())
            {
              logger.debug("Rolled-back the JTA transaction for the request");
            }
          }
        }
        catch (Throwable e)
        {
          logger.error("Failed to finalize the JTA transaction for the request", e);

          throw new ServletException("Failed to finalize the JTA transaction for the request", e);
        }
      }
    }
  }

  /**
   * Creates the web application factory instance.
   *
   * @return the web application factory instance
   */
  @Override
  protected IWebApplicationFactory getApplicationFactory()
  {
    return new WebApplicationFactory();
  }

  private String getRequestURI(ServletRequest request)
    throws ServletException
  {
    if (request instanceof HttpServletRequest)
    {
      return ((HttpServletRequest) request).getRequestURI();
    }

    return null;
  }

  private boolean isWicketRequest(ServletRequest request)
  {
    if (request instanceof HttpServletRequest)
    {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;

      // If we have not determined the wicket URI prefix then do so now
      if (wicketUriPrefix == null)
      {
        wicketUriPrefix = httpServletRequest.getContextPath() + "/wicket/";

        wicketResourceUriPrefix = httpServletRequest.getContextPath() + "/wicket/resource/";
      }

      String requestURI = httpServletRequest.getRequestURI();

      if (requestURI.startsWith(wicketUriPrefix))
      {
        return !requestURI.startsWith(wicketResourceUriPrefix);
      }
    }

    /*
     *  If we are unable to confirm whether this is a Wicket request we assume it isn't one
     *  by default.
     */
    return false;
  }
}
