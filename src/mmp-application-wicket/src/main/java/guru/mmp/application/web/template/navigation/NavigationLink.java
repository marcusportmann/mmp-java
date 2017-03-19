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

package guru.mmp.application.web.template.navigation;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.pages.AnonymousOnlyWebPage;
import guru.mmp.application.web.pages.SecureAnonymousWebPage;
import guru.mmp.application.web.pages.WebPage;
import guru.mmp.application.web.pages.WebPageSecurity;
import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.io.Serializable;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>NavigationLink</code> class stores the information for a navigation link that forms
 * part of a web application's navigation hierarchy and links to a specific Wicket-based web page.
 *
 * @author Marcus Portmann
 */
public class NavigationLink extends NavigationItem
  implements Serializable
{
  private static final long serialVersionUID = 1000000;
  private String[] functionCodes;
  private boolean isAnonymousOnly;
  private boolean isSecure;
  private Class<? extends Page> pageClass;
  private PageParameters pageParameters;

  /**
   * Constructs a new <code>NavigationLink</code>.
   *
   * NOTE: This constructor is required to support Java serialization.
   */
  @SuppressWarnings("unused")
  NavigationLink() {}

  /**
   * Constructs a new <code>NavigationLink</code>.
   *
   * @param name      the name of the navigation link
   * @param pageClass the class for the Wicket <code>Page</code> associated with the link
   */
  public NavigationLink(String name, Class<? extends Page> pageClass)
  {
    this(name, null, pageClass, new PageParameters());
  }

  /**
   * Constructs a new <code>NavigationLink</code>.
   *
   * @param name           the name of the navigation link
   * @param pageClass      the class for the Wicket <code>Page</code> associated with the link
   * @param pageParameters the parameters for the page associated with the link
   */
  NavigationLink(String name, Class<? extends Page> pageClass, PageParameters pageParameters)
  {
    this(name, null, pageClass, pageParameters);
  }

  /**
   * Constructs a new <code>NavigationLink</code>.
   *
   * @param name      the name of the navigation link
   * @param iconClass the CSS class for the icon for the navigation item
   * @param pageClass the class for the Wicket <code>Page</code> associated with the link
   */
  public NavigationLink(String name, String iconClass, Class<? extends Page> pageClass)
  {
    this(name, iconClass, pageClass, new PageParameters());
  }

  /**
   * Constructs a new <code>NavigationLink</code>.
   *
   * @param name           the name of the navigation link
   * @param iconClass      the CSS class for the icon for the navigation item
   * @param pageClass      the class for the Wicket <code>Page</code> associated with the link
   * @param pageParameters the parameters for the page associated with the link
   */
  NavigationLink(String name, String iconClass, Class<? extends Page> pageClass,
      PageParameters pageParameters)
  {
    super(name, iconClass);
    this.pageClass = pageClass;
    this.pageParameters = pageParameters;

    WebPageSecurity webPageSecurity = pageClass.getAnnotation(WebPageSecurity.class);

    if (webPageSecurity != null)
    {
      this.functionCodes = webPageSecurity.value();
      this.isSecure = true;
    }
    else
    {
      SecureAnonymousWebPage secureAnonymousWebPage = pageClass.getAnnotation(
          SecureAnonymousWebPage.class);

      if (secureAnonymousWebPage != null)
      {
        this.functionCodes = new String[] { WebPage.FUNCTION_CODE_SECURE_ANONYMOUS_ACCESS };
        this.isSecure = true;
      }
      else
      {
        this.functionCodes = new String[] { WebPage.FUNCTION_CODE_ANONYMOUS_ACCESS };
        this.isSecure = false;

        AnonymousOnlyWebPage anonymousOnlyWebPage = pageClass.getAnnotation(
            AnonymousOnlyWebPage.class);

        if (anonymousOnlyWebPage != null)
        {
          isAnonymousOnly = true;
        }
      }
    }
  }

  /**
   * Returns the function codes uniquely identifying the functions associated with the page class
   * for the link e.g. Security.CreateUser.
   *
   * @return the function code uniquely identifying the function associated with the page class
   *         for the link e.g. Security.CreateUser.
   */
  public String[] getFunctionCodes()
  {
    return functionCodes;
  }

  /**
   * Returns the class for the Wicket <code>Page</code> associated with the link.
   *
   * @return the class for the Wicket <code>Page</code> associated with the link
   */
  public Class<? extends Page> getPageClass()
  {
    return pageClass;
  }

  /**
   * Returns the parameters for the page associated with the link.
   *
   * @return the parameters for the page associated with the link
   */
  public PageParameters getPageParameters()
  {
    return pageParameters;
  }

  /**
   * Returns <code>true<code> if the page associated with the link is only accessible to anonymous
   * users (users who have not authenticated) or <code>false</code> otherwise.
   *
   * @return <code>true<code> if the page associated with the link is only accessible to anonymous
   *         users (users who have not authenticated) or <code>false</code> otherwise
   */
  public boolean isAnonymousOnly()
  {
    return isAnonymousOnly;
  }

  /**
   * Returns <code>true</code> if the page is in the navigation item's hierarchy or
   * <code>false</code> otherwise.
   *
   * @param page the page
   *
   * @return <code>true</code> if the page is in the navigation item's hierarchy or
   *         <code>false</code> otherwise
   */
  public boolean isPageInNavigationHierarchy(Page page)
  {
    if (!pageClass.equals(page.getClass()))
    {
      return false;
    }

    if (pageParameters.isEmpty())
    {
      return true;
    }
    else
    {
      for (String namedKey : pageParameters.getNamedKeys())
      {
        List<StringValue> pageParameterValues = pageParameters.getValues(namedKey);

        List<StringValue> tmpPageParameterValues = page.getPageParameters().getValues(namedKey);

        if (pageParameterValues.size() != tmpPageParameterValues.size())
        {
          return false;
        }

        for (int i = 0; i < pageParameterValues.size(); i++)
        {
          if (!pageParameterValues.get(i).toString().equals(tmpPageParameterValues.get(i)
              .toString()))
          {
            return false;
          }
        }
      }

      return true;
    }
  }

  /**
   * Returns <code>true<code> if the page associated with the link is secure and a security check
   * should be performed before allowing the user to access the page or <code>false</code>
   * otherwise.
   *
   * @return <code>true<code> if the page is secure otherwise <code>false</code>
   */
  public boolean isSecure()
  {
    return isSecure;
  }
}
