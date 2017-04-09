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

import guru.mmp.application.codes.Code;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DetachableCodeModel</code> class provides a detachable model
 * implementation for the <code>Code</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableCodeModel extends InjectableLoadableDetachableModel<Code>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category the code
   * is associated with.
   */
  private UUID categoryId;

  /* Codes Service */
  @Autowired
  private ICodesService codesService;

  /**
   * The ID used to uniquely identify the code.
   */
  private String id;

  /**
   * Constructs a new <code>DetachableCodeModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableCodeModel() {}

  /**
   * Constructs a new <code>DetachableCodeModel</code>.
   *
   * @param code the <code>Code</code> instance
   */
  public DetachableCodeModel(Code code)
  {
    this(code.getCategoryId(), code.getId());

    setObject(code);
  }

  /**
   * Constructs a new <code>DetachableCodeModel</code>.
   *
   * @param categoryId the Universally Unique Identifier (UUID) used to uniquely identify the code
   *                   category the code is associated with
   * @param id         the ID used to uniquely identify the code
   */
  public DetachableCodeModel(UUID categoryId, String id)
  {
    this.categoryId = categoryId;
    this.id = id;
  }

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected Code load()
  {
    try
    {
      return codesService.getCode(categoryId, id);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the code (%s) for the code category (%s)", id, categoryId), e);
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
