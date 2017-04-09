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

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.User;
import guru.mmp.application.security.UserNotFoundException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DetachableUserModel</code> class provides a detachable model
 * implementation for the <code>User</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableUserModel extends InjectableLoadableDetachableModel<User>
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
   * The username for the user.
   */
  private String username;

  /**
   * Constructs a new <code>DetachableUserModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableUserModel() {}

  /**
   * Constructs a new <code>DetachableUserModel</code>.
   *
   * @param user the <code>User</code> instance
   */
  public DetachableUserModel(User user)
  {
    this(user.getUserDirectoryId(), user.getUsername());

    setObject(user);
  }

  /**
   * Constructs a new <code>DetachableUserModel</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username for the user
   */
  public DetachableUserModel(UUID userDirectoryId, String username)
  {
    this.userDirectoryId = userDirectoryId;
    this.username = username;
  }

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected User load()
  {
    try
    {
      return securityService.getUser(userDirectoryId, username);
    }
    catch (UserNotFoundException e)
    {
      return null;
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to load the user (%s)", username), e);
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
