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

package guru.mmp.application.tests;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeProviderConfig;
import guru.mmp.application.codes.CodeProviderException;
import guru.mmp.application.codes.ICodeProvider;

import java.util.Date;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestCodeProvider</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class TestCodeProvider
  implements ICodeProvider
{
  /**
   * Constructs a new <code>TestCodeProvider</code>.
   *
   * @param codeProviderConfig the configuration for the code provider
   */
  public TestCodeProvider(CodeProviderConfig codeProviderConfig) {}

  /**
   * Retrieve the code category.
   *
   * @param codeCategory         the code provider code category
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the code provider code category including the <b>Standard</b> codes and/or
   * <b>Custom</b> code data or <code>null</code> if the code category could not be found
   */
  @Override
  public CodeCategory getCodeCategory(CodeCategory codeCategory, Date lastRetrieved,
      boolean returnCodesIfCurrent)
    throws CodeProviderException
  {
    throw new CodeProviderException("Not Implemented");
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategory         the code provider code category
   * @param parameters           the parameters
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the code provider code category including the <b>Standard</b> codes and/or
   * <b>Custom</b> code data or <code>null</code> if the code category could not be found
   */
  public CodeCategory getCodeCategoryWithParameters(CodeCategory codeCategory, Map<String,
      String> parameters, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodeProviderException
  {
    try
    {
      throw new RuntimeException("Testing 1.. 2.. 3..");
    }
    catch (Throwable e)
    {
      throw new CodeProviderException("Not Implemented", e);
    }
  }
}
