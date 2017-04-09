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

package guru.mmp.application.web.template.data;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.Group;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>GroupsForUserDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves the <code>Group</code> instances for the groups a user
 * is associated with from the Security Service for a particular user directory.
 *
 * @author Marcus Portmann
 */
public class GroupsForUserDataProvider extends InjectableDataProvider<Group>
{
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Autowired
  private ISecurityService securityService;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  private UUID userDirectoryId;

  /**
   * The username identifying the user the groups are associated with.
   */
  private String username;

  /**
   * Constructs a new <code>GroupsForUserDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected GroupsForUserDataProvider() {}

  /**
   * Constructs a new <code>GroupsForUserDataProvider</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user the groups are associated with
   */
  public GroupsForUserDataProvider(UUID userDirectoryId, String username)
  {
    this.userDirectoryId = userDirectoryId;
    this.username = username;
  }

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching groups from the Security Service starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the groups retrieved from the Security Service starting with
   * index <code>first</code> and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   */
  public Iterator<Group> iterator(long first, long count)
  {
    try
    {
      List<Group> allGroups = securityService.getGroupsForUser(userDirectoryId, username);

      return allGroups.subList((int) first, (int) Math.min(first + count, allGroups.size()))
          .iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to load the groups from index (%d)"
          + " to (%d) for the user (%s) for the user directory (%s)", first, first + count - 1,
          username, userDirectoryId), e);
    }
  }

  /**
   * Wraps the retrieved <code>Group</code> POJO with a Wicket model.
   *
   * @param group the <code>Group</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>Group</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   */
  public IModel<Group> model(Group group)
  {
    return new DetachableGroupModel(group);
  }

  /**
   * Returns the total number of groups.
   *
   * @return the total number of groups
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
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
      throw new WebApplicationException(String.format("Failed to retrieve the number of groups"
          + " for the user (%s) for the user directory (%s)", username, userDirectoryId), e);
    }
  }
}
