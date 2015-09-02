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
import guru.mmp.application.security.UserNotFoundException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

import javax.inject.Inject;

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
  @Inject
  private ISecurityService securityService;

  /**
   * The username for the user.
   */
  private String username;

  /**
   * Constructs a new <code>DetachableUserModel</code>.
   *
   * @param username the username for the user
   */
  public DetachableUserModel(String username)
  {
    this.username = username;
  }

  /**
   * Constructs a new <code>DetachableUserModel</code>.
   *
   * @param user the <code>User</code> instance
   */
  public DetachableUserModel(User user)
  {
    this(user.getUsername());

    setObject(user);
  }

  /**
   * Constructs a new <code>DetachableUserModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableUserModel() {}

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected User load()
  {
    try
    {
      ServletWebRequest servletWebRequest = (ServletWebRequest) RequestCycle.get().getRequest();

      return securityService.getUser(username,
          servletWebRequest.getContainerRequest().getRemoteAddr());
    }
    catch (UserNotFoundException e)
    {
      return null;
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the user (" + username + ")", e);
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
