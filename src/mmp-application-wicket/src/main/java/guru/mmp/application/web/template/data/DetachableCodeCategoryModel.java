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

package guru.mmp.application.web.template.data;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;

import javax.inject.Inject;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DetachableCodeCategoryModel</code> class provides a detachable model
 * implementation for the <code>CodeCategory</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableCodeCategoryModel extends InjectableLoadableDetachableModel<CodeCategory>
{
  private static final long serialVersionUID = 1000000;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category.
   */
  private UUID id;

  /**
   * Constructs a new <code>DetachableCodeCategoryModel</code>.
   *
   * @param codeCategory the <code>CodeCategory</code> instance
   */
  public DetachableCodeCategoryModel(CodeCategory codeCategory)
  {
    this(codeCategory.getId());

    setObject(codeCategory);
  }

  /**
   * Constructs a new <code>DetachableCodeCategoryModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           code category
   */
  public DetachableCodeCategoryModel(UUID id)
  {
    this.id = id;
  }

  /**
   * Constructs a new <code>DetachableCodeCategoryModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableCodeCategoryModel() {}

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected CodeCategory load()
  {
    try
    {
      return codesService.getCodeCategory(id, false);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the code category (" + id + ")", e);
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
