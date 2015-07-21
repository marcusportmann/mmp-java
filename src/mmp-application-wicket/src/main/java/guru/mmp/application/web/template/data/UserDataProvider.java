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

package guru.mmp.application.web.template.data;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.User;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>User</code> instances from the database.
 *
 * @author Marcus Portmann
 */
public class UserDataProvider extends InjectableDataProvider<User>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The filter used to limit the matching users.
   */
  private String filter;

  /**
   * The organisation code identifying the organisation the users are associated with.
   */
  private String organisation;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UserDataProvider</code>.
   *
   * @param organisation the organisation code identifying the organisation the users are
   *                     associated with
   */
  public UserDataProvider(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Constructs a new <code>UserDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected UserDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Returns the filter used to limit the matching users.
   *
   * @return the filter used to limit the matching users
   */
  public String getFilter()
  {
    return filter;
  }

  /**
   * Retrieves the matching users from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the users from the database starting with index <code>first</code> and
   *         ending with <code>first+count</code>
   */
  public Iterator<User> iterator(long first, long count)
  {
    try
    {
      ServletWebRequest servletWebRequest = (ServletWebRequest) RequestCycle.get().getRequest();

      List<User> allUsers;

      if (StringUtil.isNullOrEmpty(filter))
      {
        allUsers = securityService.getUsersForOrganisation(organisation,
            servletWebRequest.getContainerRequest().getRemoteAddr());
      }
      else
      {
        allUsers = securityService.getFilteredUsersForOrganisation(organisation, filter,
            servletWebRequest.getContainerRequest().getRemoteAddr());
      }

      List<User> users = new ArrayList<>();

      long end = first + count;

      for (long i = first; ((i < end) && (i < allUsers.size())); i++)
      {
        User user = allUsers.get((int) i);

        if (!user.getUsername().equalsIgnoreCase("Administrator"))
        {
          users.add(user);
        }
      }

      return users.iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the users from index (" + first + ") to ("
          + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>User</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param user the <code>User</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>User</code> instance
   */
  public IModel<User> model(User user)
  {
    return new DetachableUserModel(user);
  }

  /**
   * Set the filter used to limit the matching users.
   *
   * @param filter the filter used to limit the matching users
   */
  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  /**
   * Returns the total number of matching users in the database.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of matching users in the database
   */
  public long size()
  {
    try
    {
      ServletWebRequest servletWebRequest = (ServletWebRequest) RequestCycle.get().getRequest();

      if (StringUtil.isNullOrEmpty(filter))
      {
        return securityService.getNumberOfUsersForOrganisation(organisation,
            servletWebRequest.getContainerRequest().getRemoteAddr());
      }
      else
      {
        return securityService.getNumberOfFilteredUsersForOrganisation(organisation, filter,
            servletWebRequest.getContainerRequest().getRemoteAddr());
      }
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of users", e);
    }
  }
}
