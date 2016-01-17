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

import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.UrlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>WebSphereAbsoluteUrlRenderer</code> provides a customised <code>UrlRenderer</code>
 * that renders absolute URLs that are compatible with WebSphere instead of relative URLs.
 *
 * @author Marcus Portmann
 */
public class WebSphereAbsoluteUrlRenderer extends UrlRenderer
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WebSphereAbsoluteUrlRenderer.class);

  /* The context path. */
  private String contextPath;

  /**
   * Constructs a new <code>WebSphereAbsoluteUrlRenderer</code>.
   *
   * @param request the wicket request
   */
  public WebSphereAbsoluteUrlRenderer(Request request)
  {
    super(request);

    this.contextPath = request.getContextPath();
  }

  /**
   * Overrides the normal context-relative URL rendering to render an absolute URL that is
   * compatible with WebSphere.
   *
   * @param url the URL to render
   *
   * @return the WebSphere compatible absolute URL
   */
  @Override
  public String renderContextRelativeUrl(String url)
  {
    if (url == null)
    {
      throw new WebApplicationException("Fail to render context relative URL for null URL");
    }

    // If this is already an absolute URL that starts with the context path then stop here
    if (url.startsWith(contextPath))
    {
      return url;
    }

    if (url.startsWith("/"))
    {
      logger.warn("Found unexpected URL (" + url + ")");

      url = url.substring(1);
    }

    return contextPath + "/" + url;
  }

  /**
   * Overrides the normal relative URL rendering to render an absolute URL that is compatible with
   * WebSphere.
   *
   * @param url the URL to render
   *
   * @return the WebSphere compatible absolute URL
   */
  @Override
  public String renderRelativeUrl(Url url)
  {
    if (url == null)
    {
      throw new WebApplicationException("Fail to render relative URL for null URL");
    }

    // If this is already a context absolute or full URL then stop here
    if (url.isContextAbsolute() || url.isFull())
    {
      return url.toString();
    }
    else
    {
      return contextPath + "/" + url.toString();
    }
  }
}
