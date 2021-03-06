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

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>GroupDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves a list of <code>Group</code> instances from the Security Service
 * that are associated with a particular user directory.
 *
 * @author Marcus Portmann
 */
public class GroupDataProvider extends InjectableDataProvider<Group>
{
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>GroupDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected GroupDataProvider() {}

  /**
   * Constructs a new <code>GroupDataProvider</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  public GroupDataProvider(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
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
   * @return the groups retrieved from the Security Service starting with index <code>first</code>
   * and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   */
  public Iterator<Group> iterator(long first, long count)
  {
    try
    {
      List<Group> allGroups = securityService.getGroups(userDirectoryId);

      return allGroups.subList((int) first, (int) Math.min(first + count, allGroups.size()))
          .iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the groups from index (%d) to (%d) for the user directory (%s)", first,
          first + count - 1, userDirectoryId), e);
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
      return securityService.getNumberOfGroups(userDirectoryId);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to retrieve the number of groups for the user directory (%s)", userDirectoryId),
          e);
    }
  }
}
