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

package guru.mmp.application.web.template.component;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.web.WebApplicationException;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

/**
 * The <code>UserDirectoryChoiceRenderer</code> class implements a <code>ChoiceRenderer</code> for
 * <code>UserDirectory</code> instances.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class UserDirectoryChoiceRenderer
  implements IChoiceRenderer<UserDirectory>
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>UserDirectoryChoiceRenderer</code>.
   */
  public UserDirectoryChoiceRenderer() {}

  /**
   * Get the value for displaying to an end user.
   *
   * @param userDirectory the user directory
   *
   * @return the value meant for displaying to an end user
   */
  public Object getDisplayValue(UserDirectory userDirectory)
  {
    return userDirectory.getName();
  }

  /**
   * This method is called to get the id value of a user directory (used as the value attribute of
   * a choice element).
   *
   * @param userDirectory the user directory for which the id should be generated
   * @param index         the index of the object in the choices list
   *
   * @return the id value of the object
   */
  public String getIdValue(UserDirectory userDirectory, int index)
  {
    return String.valueOf(userDirectory.getId());
  }

  /**
   * This method is called to get an object back from its id representation. The id may be used to
   * find/load the object in a more efficient way than loading all choices and find the one with
   * the same id in the list.
   *
   * @param id      the id representation of the object
   * @param choices the model providing the list of all rendered choices
   *
   * @return a choice from the list that has this id
   */
  public UserDirectory getObject(String id, IModel<? extends List<? extends UserDirectory>> choices)
  {
    UUID userDirectoryId = UUID.fromString(id);

    for (UserDirectory choice : choices.getObject())
    {
      if (choice.getId() == userDirectoryId)
      {
        return choice;
      }
    }

    throw new WebApplicationException("Failed to find the user directory with ID (" + id + ")");
  }
}
