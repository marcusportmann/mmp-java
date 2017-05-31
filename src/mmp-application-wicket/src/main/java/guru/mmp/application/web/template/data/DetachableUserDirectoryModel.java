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
import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.security.UserDirectoryNotFoundException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DetachableUserDirectoryModel</code> class provides a detachable model
 * implementation for the <code>UserDirectory</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableUserDirectoryModel extends InjectableLoadableDetachableModel<UserDirectory>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  private UUID id;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>DetachableUserDirectoryModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableUserDirectoryModel() {}

  /**
   * Constructs a new <code>DetachableUserDirectoryModel</code>.
   *
   * @param userDirectory the <code>UserDirectory</code> instance
   */
  public DetachableUserDirectoryModel(UserDirectory userDirectory)
  {
    this(userDirectory.getId());

    setObject(userDirectory);
  }

  /**
   * Constructs a new <code>DetachableUserDirectoryModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public DetachableUserDirectoryModel(UUID id)
  {
    this.id = id;
  }

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected UserDirectory load()
  {
    try
    {
      return securityService.getUserDirectory(id);
    }
    catch (UserDirectoryNotFoundException e)
    {
      return null;
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to load the user directory (%s)",
          id), e);
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
