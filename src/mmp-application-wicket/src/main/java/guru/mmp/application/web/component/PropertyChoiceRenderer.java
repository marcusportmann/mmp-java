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

package guru.mmp.application.web.component;

import guru.mmp.application.web.WebApplicationException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * The <code>PropertyChoiceRenderer</code> class implements a simple property-based ChoiceRenderer.
 * <p/>
 * Use the given property names as the source to obtain the respective ID and LABEL strings used to
 * render the Choice to the Hosting component.
 * <p/>
 * Example:
 * <p/>
 * // Somewhere, in SomePersistentData.java file...
 * public class SomePersistentData implements Serializable
 * {
 * private Integer id;
 * private String  description;
 * //plus, getters and setters....
 * }
 * <p/>
 * // Extract Objects from DB
 * List values = myService.findDataElements();
 * <p/>
 * add(new DropDownChoice("myWicketId",
 * new PropertyModel(myDataObject, "propertyName"), values,
 * new PropertyChoiceRenderer("id", "description")))
 *
 * @param <T>
 *
 * @author Davide Alberto Molin (davide.molin@gmail.com)
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class PropertyChoiceRenderer<T>
  implements IChoiceRenderer<T>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The name of the property used to extract the ID for the choice option.
   */
  private String idProperty;

  /**
   * The name of the property used to extract the LABEL for the choice option.
   */
  private String valueProperty;

  /**
   * Constructs a new <code>PropertyChoiceRenderer</code>.
   *
   * @param idProperty    the name of the property used to extract the ID for the choice option
   * @param valueProperty the name of the property used to extract the LABEL for the choice option
   */
  public PropertyChoiceRenderer(String idProperty, String valueProperty)
  {
    this.idProperty = idProperty;
    this.valueProperty = valueProperty;
  }

  /**
   * Get the value for displaying to an end user.
   *
   * @param object the actual object
   *
   * @return the value meant for displaying to an end user
   */
  public Object getDisplayValue(T object)
  {
    return getPropertyValue(object, valueProperty);
  }

  /**
   * This method is called to get the id value of an object (used as the value attribute of a
   * choice element). The id can be extracted from the object like a primary key, or if the list is
   * stable you could just return a <code>toString</code> of the index.
   *
   * @param object the object for which the id should be generated
   * @param index  the index of the object in the choices list
   *
   * @return the id value of the object
   */
  public String getIdValue(T object, int index)
  {
    return getPropertyValue(object, idProperty).toString();
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
  public T getObject(String id, IModel<? extends List<? extends T>> choices)
  {
    for (T choice : choices.getObject())
    {
      String choiceId = getPropertyValue(choice, idProperty).toString();

      if (id.equals(choiceId))
      {
        return choice;
      }
    }

    throw new WebApplicationException("Failed to find the choice with ID (" + id + ")");
  }

  private Object getPropertyValue(T object, String property)
  {
    try
    {
      return BeanUtils.getProperty(object, property);
    }
    catch (Exception err)
    {
      // In case of exception, fall back to simple toString...
      return object.toString();
    }
  }
}
