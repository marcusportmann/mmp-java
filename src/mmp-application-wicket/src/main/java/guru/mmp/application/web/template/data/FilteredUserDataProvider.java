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

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * The <code>FilteredUserDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves a filtered list of <code>User</code> instances from the
 * Security Service that are associated with a particular user directory.
 *
 * @author Marcus Portmann
 */
public class FilteredUserDataProvider extends InjectableDataProvider<User>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The filter used to limit the matching users.
   */
  private String filter;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * The unique ID for the user directory the users are associated with.
   */
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>UserDataProvider</code>.
   *
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   */
  public FilteredUserDataProvider(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Constructs a new <code>UserDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected FilteredUserDataProvider() {}

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
   * Retrieves the matching users from the Security Service starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the users retrieved from the Security Service starting with index <code>first</code>
   *         and ending with <code>first+count</code>
   */

  public Iterator<User> iterator(long first, long count)
  {
    try
    {
      List<User> allUsers = securityService.getFilteredUsers(userDirectoryId, filter);

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
          + (first + count) + ") for the user directory (" + userDirectoryId + ")", e);
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
   * Set the unique ID for the user directory the users are associated with.
   *
   * @param userDirectoryId the unique ID for the user directory the users are associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Returns the total number of users.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of users
   */
  public long size()
  {
    try
    {
      return securityService.getNumberOfFilteredUsers(userDirectoryId, filter);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to retrieve the number of users for the user directory (" + userDirectoryId
          + ")", e);
    }
  }
}
