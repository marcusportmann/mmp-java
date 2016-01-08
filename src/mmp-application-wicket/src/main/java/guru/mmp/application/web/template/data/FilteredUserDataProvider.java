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

package guru.mmp.application.web.template.data;

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.User;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * The <code>FilteredUserDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves a filtered list of <code>User</code> instances from the
 * Security Service that are associated with a particular user directory.
 *
 * @author Marcus Portmann
 */
public class FilteredUserDataProvider
  extends InjectableDataProvider<User>
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
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>UserDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected FilteredUserDataProvider() {}

  /**
   * Constructs a new <code>UserDataProvider</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  public FilteredUserDataProvider(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }

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
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the users retrieved from the Security Service starting with index <code>first</code>
   * and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
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
      throw new WebApplicationException(String.format(
        "Failed to load the users from index (%d) to (%d) for the user directory (%s)", first,
        first + count, userDirectoryId), e);
    }
  }

  /**
   * Wraps the retrieved <code>User</code> POJO with a Wicket model.
   *
   * @param user the <code>User</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>User</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
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
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  public void setUserDirectoryId(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Returns the total number of users.
   *
   * @return the total number of users
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
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
        String.format("Failed to retrieve the number of users for the user directory (%s)",
          userDirectoryId), e);
    }
  }
}
