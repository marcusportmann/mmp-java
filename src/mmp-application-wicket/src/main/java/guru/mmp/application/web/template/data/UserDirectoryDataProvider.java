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
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserDirectoryDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>UserDirectory</code> instances from the database.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class UserDirectoryDataProvider extends InjectableDataProvider<UserDirectory>
{
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching user directories from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the user directories retrieved from the database starting with index
   *         <code>first</code> and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   */
  public Iterator<UserDirectory> iterator(long first, long count)
  {
    try
    {
      List<UserDirectory> allUserDirectories = securityService.getUserDirectories();

      return allUserDirectories.subList((int) first, (int) Math.min(first + count,
          allUserDirectories.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the user directories from index (%d) to (%d)", first, first + count - 1),
          e);
    }
  }

  /**
   * Wraps the retrieved <code>UserDirectory</code> POJO with a Wicket model.
   *
   * @param userDirectory the <code>UserDirectory</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>UserDirectory</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   */
  public IModel<UserDirectory> model(UserDirectory userDirectory)
  {
    return new DetachableUserDirectoryModel(userDirectory);
  }

  /**
   * Returns the total number of user directories.
   *
   * @return the total number of user directories
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   */
  public long size()
  {
    try
    {
      return securityService.getNumberOfUserDirectories();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of user directories", e);
    }
  }
}
