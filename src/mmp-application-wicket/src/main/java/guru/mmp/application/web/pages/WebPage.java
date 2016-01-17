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

package guru.mmp.application.web.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.WebSession;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * The <code>WebPage</code> class is the base class that all Wicket web page classes must
 * be derived from in web applications that make use of the library.
 * <p/>
 * It provides a mechanism for securing pages through the use of authorised functions. Every
 * authorised function is identified through a unique authorised function code. A page or pages
 * that provide a unit of functionality are assigned an authorised function code. Authorised
 * function codes are also assigned to users and groups using the Security Service for the web
 * application. When a  user attempts to access a page this class implements an access
 * control check which compares the authorised function code for the page to the authorised
 * function codes assigned directly and indirectly (via groups) to the user.
 * <p/>
 * A page is marked as secure and assigned an authorised function code by applying the
 * <code>WebPageSecurity</code> annotation.
 * </p>
 *
 * @author Marcus Portmann
 * @see guru.mmp.application.web.pages.WebPageSecurity
 */
@SuppressWarnings("unused")
public abstract class WebPage extends org.apache.wicket.markup.html.WebPage
{
  /**
   * The Application.AnonymousAccess function code applied to unsecured web pages.
   */
  public static final String FUNCTION_CODE_ANONYMOUS_ACCESS = "Application.AnonymousAccess";

  /**
   * The Application.SecureAnonymousAccess function code applied to secure web pages that can be
   * accessed any logged in user regardless of their role.
   */
  public static final String FUNCTION_CODE_SECURE_ANONYMOUS_ACCESS = "Application"
      + ".SecureAnonymousAccess";
  private static final long serialVersionUID = 1000000;
  private String functionCode;
  private boolean isSecure;

  /**
   * Constructs a new <code>WebPage</code>.
   */
  public WebPage()
  {
    // Retrieve the web page security information from WebPageSecurity annotation
    WebPageSecurity webPageSecurity = getClass().getAnnotation(WebPageSecurity.class);

    if (webPageSecurity != null)
    {
      this.functionCode = webPageSecurity.value();
      this.isSecure = true;
    }
    else
    {
      SecureAnonymousWebPage secureAnonymousWebPage = getClass().getAnnotation(
          SecureAnonymousWebPage.class);

      if (secureAnonymousWebPage != null)
      {
        this.functionCode = WebPage.FUNCTION_CODE_SECURE_ANONYMOUS_ACCESS;
        this.isSecure = true;
      }
      else
      {
        this.functionCode = WebPage.FUNCTION_CODE_ANONYMOUS_ACCESS;
        this.isSecure = false;
      }
    }
  }

  /**
   * Returns the function code uniquely identifying the function associated with the page
   * e.g. Security.CreateUser.
   *
   * @return the function code uniquely identifying the function associated with the page
   */
  public String getFunctionCode()
  {
    return functionCode;
  }

  /**
   * Returns the IP address of the remote client associated with the request.
   *
   * @return the IP address of the remote client associated with the request
   */
  public String getRemoteAddress()
  {
    ServletWebRequest servletWebRequest = (ServletWebRequest) RequestCycle.get().getRequest();

    return servletWebRequest.getContainerRequest().getRemoteAddr();
  }

  /**
   * Returns the <code>WebSession</code> for the user associated with the current request.
   *
   * @return the <code>WebSession</code> for the user associated with the current request
   */
  public WebSession getWebApplicationSession()
  {
    return WebSession.class.cast(getSession());
  }

  /**
   * Returns <code>true<code> if the page is secure and a security check should be performed before
   * allowing the user to access the page or <code>false</code> otherwise.
   *
   * @return <code>true<code> if the page is secure otherwise <code>false</code>
   */
  public boolean isSecure()
  {
    return isSecure;
  }
}
