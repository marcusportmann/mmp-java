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

import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.web.WebApplicationException;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeCategoryTypeChoiceRenderer</code> class implements a <code>ChoiceRenderer</code>
 * for <code>CodeCategoryType</code> instances.
 *
 * @author Marcus Portmann
 */
public class CodeCategoryTypeChoiceRenderer
  implements IChoiceRenderer<CodeCategoryType>
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>CodeCategoryTypeChoiceRenderer</code>.
   */
  public CodeCategoryTypeChoiceRenderer() {}

  /**
   * Get the value for displaying to an end user.
   *
   * @param codeCategoryType the code category type
   *
   * @return the value meant for displaying to an end user
   */
  public Object getDisplayValue(CodeCategoryType codeCategoryType)
  {
    return codeCategoryType.getName();
  }

  /**
   * This method is called to get the id value of a code category type (used as the value attribute
   * of a choice element).
   *
   * @param codeCategoryType the code category type for which the id should be generated
   * @param index            the index of the object in the choices list
   *
   * @return the id value of the object
   */
  public String getIdValue(CodeCategoryType codeCategoryType, int index)
  {
    return String.valueOf(codeCategoryType.getCode());
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
  public CodeCategoryType getObject(String id,
      IModel<? extends List<? extends CodeCategoryType>> choices)
  {
    int code = Integer.valueOf(id);

    for (CodeCategoryType choice : choices.getObject())
    {
      if (choice.getCode() == code)
      {
        return choice;
      }
    }

    throw new WebApplicationException("Failed to find the code category choice with ID (" + id
        + ")");
  }
}
