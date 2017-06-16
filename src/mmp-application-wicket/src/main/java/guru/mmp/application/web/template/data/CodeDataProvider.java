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
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>Code</code> instances from the database.
 *
 * @author Marcus Portmann
 */
public class CodeDataProvider extends InjectableDataProvider<Code>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category.
   */
  private UUID codeCategoryId;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Constructs a new <code>CodeDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected CodeDataProvider() {}

  /**
   * Constructs a new <code>CodeDataProvider</code>.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   */
  public CodeDataProvider(UUID codeCategoryId)
  {
    this.codeCategoryId = codeCategoryId;
  }

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching codes from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the codes retrieved from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   */
  public Iterator<Code> iterator(long first, long count)
  {
    try
    {
      List<Code> allCodes = codesService.getCodesForCodeCategory(codeCategoryId);

      return allCodes.subList((int) first, (int) Math.min(first + count, allCodes.size()))
          .iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the codes from index (%d) to (%d)", first, first + count - 1), e);
    }
  }

  /**
   * Wraps the retrieved <code>Code</code> POJO with a Wicket model.
   *
   * @param code the <code>Code</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>Code</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   */
  public IModel<Code> model(Code code)
  {
    return new DetachableCodeModel(code);
  }

  /**
   * Returns the total number of codes.
   *
   * @return the total number of codes
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   */
  public long size()
  {
    try
    {
      return codesService.getNumberOfCodesForCodeCategory(codeCategoryId);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of codes", e);
    }
  }
}
