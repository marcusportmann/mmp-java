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

package guru.mmp.application.web.resource.perfectscrollbar;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * The <code>PerfectScrollbarCssResourceReference</code> class implements the CSS resource reference
 * for the Perfect Scrollbar library bundled with the web application library.
 *
 * @author Marcus Portmann
 */
public class PerfectScrollbarCssResourceReference extends CssResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final PerfectScrollbarCssResourceReference INSTANCE =
    new PerfectScrollbarCssResourceReference();
  private static final CssHeaderItem CSS_HEADER_ITEM =
    CssHeaderItem.forReference(new PerfectScrollbarCssResourceReference());

  private PerfectScrollbarCssResourceReference()
  {
    super(PerfectScrollbarCssResourceReference.class, Debug.inDebugMode()
        ? "css/perfect-scrollbar.css"
        : "css/perfect-scrollbar.min.css");
  }

  /**
   * Returns the single instance of the CSS resource reference for the Perfect Scrollbar library
   * bundled with the web application library.
   *
   * @return the single instance of the CSS resource reference for the Perfect Scrollbar library
   *         bundled with the web application library
   */
  public static PerfectScrollbarCssResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the CSS header item for the Perfect Scrollbar library bundled with the web application
   * library.
   *
   * @return the CSS header item for the Perfect Scrollbar library bundled with the web application
   *         library
   */
  public static CssHeaderItem getCssHeaderItem()
  {
    return CSS_HEADER_ITEM;
  }
}
