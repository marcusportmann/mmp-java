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

import guru.mmp.application.codes.Code;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

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
   * The ID identifying the code category the codes are associated with.
   */
  private String codeCategoryId;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Constructs a new <code>CodeDataProvider</code>.
   *
   * @param codeCategoryId the ID identifying the code category the codes are associated with
   */
  public CodeDataProvider(String codeCategoryId)
  {
    this.codeCategoryId = codeCategoryId;
  }

  /**
   * Constructs a new <code>CodeDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected CodeDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching codes from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the codes from the database starting with index <code>first</code> and
   *         ending with <code>first+count</code>
   */
  public Iterator<Code> iterator(long first, long count)
  {
    try
    {
      List<Code> allCodes = codesService.getCodesForCodeCategory(codeCategoryId);

      return allCodes.subList((int) first,
          (int) Math.min(first + count, allCodes.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the codes from index (" + first + ") to ("
          + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>Code</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param code the <code>Code</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>Code</code> instance
   */
  public IModel<Code> model(Code code)
  {
    return new DetachableCodeModel(code);
  }

  /**
   * Returns the total number of matching codes in the database.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of matching codes in the database
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
