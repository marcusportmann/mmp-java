/*
 * Copyright 2014 Marcus Portmann
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
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeCategoryDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>CodeCategory</code> instances from the database.
 *
 * @author Marcus Portmann
 */
public class CodeCategoryDataProvider extends InjectableDataProvider<CodeCategory>
{
  private static final long serialVersionUID = 1000000;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * The organisation code identifying the organisation the code categories are associated with.
   */
  private String organisation;

  /**
   * Whether the codes and/or code data for the code categories should be retrieved.
   */
  private boolean retrieveCodes;

  /**
   * Constructs a new <code>CodeCategoryDataProvider</code>.
   *
   * @param organisation  the organisation code identifying the organisation the code categories
   *                      are associated with
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   */
  public CodeCategoryDataProvider(String organisation, boolean retrieveCodes)
  {
    this.organisation = organisation;
    this.retrieveCodes = retrieveCodes;
  }

  /**
   * Constructs a new <code>CodeCategoryDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected CodeCategoryDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching code categories from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the code categories from the database starting with index <code>first</code> and
   *         ending with <code>first+count</code>
   */
  public Iterator<CodeCategory> iterator(long first, long count)
  {
    try
    {
      List<CodeCategory> allCodeCategories =
        codesService.getCodeCategoriesForOrganisation(organisation, retrieveCodes);

      return allCodeCategories.subList((int) first,
          (int) Math.min(first + count, allCodeCategories.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the code categories from index (" + first
          + ") to (" + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>CodeCategory</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param codeCategory the <code>CodeCategory</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>CodeCategory</code> instance
   */
  public IModel<CodeCategory> model(CodeCategory codeCategory)
  {
    return new DetachableCodeCategoryModel(codeCategory);
  }

  /**
   * Returns the total number of matching code categories in the database.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of matching code categories in the database
   */
  public long size()
  {
    try
    {
      return codesService.getNumberOfCodeCategoriesForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of code categories", e);
    }
  }
}
