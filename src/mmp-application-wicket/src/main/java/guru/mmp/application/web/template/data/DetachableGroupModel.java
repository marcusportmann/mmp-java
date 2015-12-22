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
import guru.mmp.application.security.GroupNotFoundException;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>DetachableGroupModel</code> class provides a detachable model
 * implementation for the <code>Group</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableGroupModel extends InjectableLoadableDetachableModel<Group>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The group name for the group.
   */
  private String groupName;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * The unique ID for the user directory the group is associated with;
   */
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>DetachableGroupModel</code>.
   *
   * @param group the <code>Group</code> instance
   */
  public DetachableGroupModel(Group group)
  {
    this(group.getUserDirectoryId(), group.getGroupName());

    setObject(group);
  }

  /**
   * Constructs a new <code>DetachableGroupModel</code>.
   *
   * @param userDirectoryId the unique ID for the user directory the group is associated with
   * @param groupName       the group name for the group
   */
  public DetachableGroupModel(UUID userDirectoryId, String groupName)
  {
    this.userDirectoryId = userDirectoryId;
    this.groupName = groupName;
  }

  /**
   * Constructs a new <code>DetachableGroupModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableGroupModel() {}

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected Group load()
  {
    try
    {
      return securityService.getGroup(userDirectoryId, groupName);
    }
    catch (GroupNotFoundException e)
    {
      return null;
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the group (" + groupName + ")", e);
    }
  }

  /**
   * Invoked when the model is detached after use.
   */
  @Override
  protected void onDetach()
  {
    super.onDetach();

    setObject(null);
  }
}
