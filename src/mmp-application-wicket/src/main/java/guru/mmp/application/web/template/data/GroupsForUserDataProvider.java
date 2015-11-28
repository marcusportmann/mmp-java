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

import guru.mmp.application.security.Group;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * The <code>GroupsForUserDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves the <code>Group</code> instances for the groups a user
 * is associated with from the database.
 *
 * @author Marcus Portmann
 */
public class GroupsForUserDataProvider extends InjectableDataProvider<Group>
{
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * The unique ID for the user directory the user is associated with.
   */
  private long userDirectoryId;

  /**
   * The username identifying the user the groups are associated with.
   */
  private String username;

  /**
   * Constructs a new <code>GroupsForUserDataProvider</code>.
   *
   * @param userDirectoryId the unique ID for the user directory the user is associated with
   * @param username        the username identifying the user the groups are associated with
   */
  public GroupsForUserDataProvider(long userDirectoryId, String username)
  {
    this.userDirectoryId = userDirectoryId;
    this.username = username;
  }

  /**
   * Constructs a new <code>GroupsForUserDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected GroupsForUserDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching groups from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the groups from the database starting with index <code>first</code> and
   *         ending with <code>first+count</code>
   */
  public Iterator<Group> iterator(long first, long count)
  {
    try
    {
      List<Group> groups = securityService.getGroupsForUser(userDirectoryId, username);

      return groups.iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the groups from index (" + first + ") to ("
          + (first + count) + ") for the user directory (" + userDirectoryId + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>Group</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param group the <code>Group</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>Group</code> instance
   */
  public IModel<Group> model(Group group)
  {
    return new DetachableGroupModel(group);
  }

  /**
   * Returns the total number of matching groups in the database.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of matching groups in the database
   */
  public long size()
  {
    try
    {
      List<Group> groups = securityService.getGroupsForUser(userDirectoryId, username);

      return groups.size();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to retrieve the number of groups for the user directory (" + userDirectoryId
          + ")", e);
    }
  }
}
