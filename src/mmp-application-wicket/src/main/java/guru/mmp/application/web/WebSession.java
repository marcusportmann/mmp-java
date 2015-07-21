/*
 * Copyright 2015 Marcus Portmann
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

import guru.mmp.application.web.page.WebPage;
import guru.mmp.application.web.servlet.ViewReportParameters;
import org.apache.wicket.request.Request;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebSession</code> class stores the session information for a user accessing a Wicket
 * web application.
 *
 * @author Marcus Portmann
 */
public class WebSession extends org.apache.wicket.protocol.http.WebSession
{
  private static final long serialVersionUID = 1000000;

  /** The empty list of group names for anonymous users. */
  public static final List<String> NO_GROUP_NAMES = new ArrayList<>();

  /** The empty list of function codes for anonymous users. */
  public static final List<String> NO_FUNCTION_CODES = new ArrayList<>();

  /** The active <code>ViewReportParameters</code> instances for reports being viewed. */
  Map<String, ViewReportParameters> activeViewReportParameters = new ConcurrentHashMap<>();

  /** The function codes identifying the functionality assigned to the logged in user. */
  private Map<String, String> functionCodes;

  /** The names of the groups the logged in user is a member of. */
  private List<String> groupNames;

  /**
   * The organisation code identifying the organisational context for the user associated with the
   * web session.
   */
  private String organisation;

  /** The unique user ID for the logged in user associated with the web session. */
  private long userId;

  /** The user properties for the logged in user associated with the web session. */
  private Map<String, Serializable> userProperties;

  /** The username for the logged in user associated with the web session. */
  private String username;

  /**
   * Constructs a new <code>WebSession</code>.
   *
   * @param request the request
   */
  public WebSession(Request request)
  {
    super(request);
  }

  /**
   * Add the <code>ViewReportParameters</code> instance to the user's web session.
   *
   * @param parameters the <code>ViewReportParameters</code> instance
   */
  public void addViewReportParameters(ViewReportParameters parameters)
  {
    activeViewReportParameters.put(parameters.getId(), parameters);
  }

  /**
   * Returns the function codes identifying the functionality assigned to the logged in user.
   *
   * @return the function codes identifying the functionality assigned to the logged in user
   */
  public Collection<String> getFunctionCodes()
  {
    if (functionCodes == null)
    {
      return NO_FUNCTION_CODES;
    }
    else
    {
      return functionCodes.values();
    }
  }

  /**
   * Returns the names of the groups the logged in user is a member of.
   *
   * @return the names of the groups the logged in user is a member of
   */
  public List<String> getGroupNames()
  {
    if (groupNames == null)
    {
      return NO_GROUP_NAMES;
    }
    else
    {
      return groupNames;
    }
  }

  /**
   * Returns the organisation code identifying the organisational context for the user associated
   * with the web session.
   *
   * @return the organisation code identifying the organisational context for the user associated
   *         with the web session
   */
  public String getOrganisation()
  {
    return organisation;
  }

  /**
   * Returns the unique numeric ID for the logged in user associated with the web session or -1 if
   * this is an 'anonymous' web session.
   *
   * @return the the unique numeric ID for the logged in user associated with the web session or
   *         -1 if this is an 'anonymous' web session
   */
  public long getUserId()
  {
    return userId;
  }

  /**
   * Returns the value for the user property with the specified name for the logged in user
   * associated with the web session or <code>null</code> if the user property does not exist.
   *
   * @param name the name of the user property
   *
   * @return the value for the user property with the specified name for the logged in user
   * associated with the web session or <code>null</code> if the user property does not exist
   */
  public Object getUserProperty(String name)
  {
    if (userProperties != null)
    {
      return userProperties.get(name);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns the <code>String</code> value for the user property with the specified name for the
   * logged in user associated with the web session or <code>null</code> if the user property does
   * not exist.
   *
   * @param name the name of the user property
   *
   * @return the <code>String</code> value for the user property with the specified name for the
   *         logged in user associated with the web session or <code>null</code> if the user
   *         property does not exist
   */
  public String getUserPropertyAsString(String name)
  {
    if (userProperties != null)
    {
      Object value = userProperties.get(name);

      return value.toString();
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns the username for the logged in user associated with the web session or
   * <code>null</code> if this is an 'anonymous' web session.
   *
   * @return the username for the logged in user associated with the web session or
   *         <code>null</code> if this is an 'anonymous' web session
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Returns the <code>ViewReportParameters</code> instance with the specified ID from the user's
   * web session or <code>null</code> if the <code>ViewReportParameters</code> instance could not
   * be found.
   *
   * @param id the ID uniquely identifying the <code>ViewReportParameters</code> instance stored
   *        in the user's web session
   *
   * @return the <code>ViewReportParameters</code> instance with the specified ID stored in the
   *         user's web session or <code>null</code> if the <code>ViewReportParameters</code>
   *         instance could not found
   */
  public ViewReportParameters getViewReportParameters(String id)
  {
    return activeViewReportParameters.get(id);
  }

  /**
   * Check whether the logged in user associated with the web session as access to the function
   * with the specified code.
   * <p/>
   * This method always returns false for anonymous users.
   *
   * @param functionCode the function code uniquely identifying the function
   *
   * @return <code>true</code> if the logged in user has access to the function or
   *         <code>false</code> otherwise
   */
  public boolean hasAcccessToFunction(String functionCode)
  {
    if (functionCode.equals(WebPage.FUNCTION_CODE_SECURE_ANONYMOUS_ACCESS))
    {
      return isUserLoggedIn();
    }

    return functionCodes != null && functionCodes.containsKey(functionCode);

  }

  /**
   * Returns <code>true</code> if the logged in user associated with the web session has a user
   * property with the specified name or <code>false</code> otherwise.
   *
   * @param name the name of the user property
   *
   * @return <code>true</code> if the logged in user associated with the web session has a user
   *         property with the specified name or <code>false</code> otherwise
   */
  public boolean hasUserProperty(String name)
  {
    return userProperties != null && userProperties.containsKey(name);
  }

  /**
   * Checks whether the user associated with the web session is a member of the group given by the
   * specified group name.
   *
   * @param groupName the name of the group uniquely identifying the group
   *
   * @return <code>true</code> if the user associated with the web session is a member of the group
   *         given by the specified group name or <code>false</code> otherwise
   */
  public boolean isUserInGroup(String groupName)
  {
    if ((username == null) || (groupNames == null))
    {
      return false;
    }

    for (String tmpGroupName : groupNames)
    {
      if (tmpGroupName.equalsIgnoreCase(groupName))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks whether the user has logged in.
   *
   * @return <code>true</code> if the user has logged in or <code>false</code> otherwise
   */
  public boolean isUserLoggedIn()
  {
    return (username != null);
  }

  /**
   * Logout the logged in user.
   */
  public void logoutUser()
  {
    username = null;
    groupNames = null;
    functionCodes = null;
  }

  /**
   * Remove the <code>ViewReportParameters</code> instance with the specified ID stored from the
   * user's web session.
   *
   * @param id the ID uniquely identifying the <code>ViewReportParameters</code> instance stored
   *        in the user's web session
   */
  public void removeViewReportParameters(String id)
  {
    activeViewReportParameters.remove(id);
  }

  /**
   * Set the function codes identifying the functionality assigned to the logged in user.
   *
   * @param functionCodes the function codes identifying the functionality assigned to the logged
   *                      in user
   */
  public void setFunctionCodes(List<String> functionCodes)
  {
    this.functionCodes = new HashMap<>();

    for (String functionCode : functionCodes)
    {
      this.functionCodes.put(functionCode, functionCode);
    }
  }

  /**
   * Set the names of the groups the logged in user is a member of.
   *
   * @param groupNames the names of the groups the logged in user is a member of
   */
  public void setGroupNames(List<String> groupNames)
  {
    this.groupNames = groupNames;
  }

  /**
   * Set the organisation code identifying the organisational context for the user associated with
   * the web session.
   *
   * @param organisation the organisation code identifying the organisational context for the
   *                     user associated with the web session
   */
  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Set the unique numeric ID for the logged in user associated with the web session or -1 if this
   * is an 'anonymous' web session.
   *
   * @param userId the the unique numeric ID for the logged in user associated with the web
   *               session or -1 if this is an 'anonymous' web session
   */
  public void setUserId(long userId)
  {
    this.userId = userId;
  }

  /**
   * Set the value for the user property with the specified name for the logged in user
   * associated with the web session.
   *
   * @param name  the name of the user property
   * @param value the value for the user property
   */
  public void setUserProperty(String name, Serializable value)
  {
    if (userProperties == null)
    {
      synchronized (this)
      {
        if (userProperties == null)
        {
          userProperties = new HashMap<>();
        }
      }
    }

    userProperties.put(name, value);
  }

  /**
   * Set the username for the logged in user associated with the web session.
   *
   * @param username the username for the logged in user associated with the web session
   */
  public void setUsername(String username)
  {
    this.username = username;
  }
}
