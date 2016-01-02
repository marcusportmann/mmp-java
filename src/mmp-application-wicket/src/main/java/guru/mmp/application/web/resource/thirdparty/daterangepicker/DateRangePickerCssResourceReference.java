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

package guru.mmp.application.web.resource.thirdparty.daterangepicker;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * The <code>DateRangePickerCssResourceReference</code> class implements the CSS resource
 * reference for the Date Range Picker thirdparty resource bundled with the
 * Web Application Template.
 *
 * @author Marcus Portmann
 */
public class DateRangePickerCssResourceReference extends CssResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final DateRangePickerCssResourceReference INSTANCE =
    new DateRangePickerCssResourceReference();
  private static final CssHeaderItem CSS_HEADER_ITEM =
    CssHeaderItem.forReference(new DateRangePickerCssResourceReference());

  private DateRangePickerCssResourceReference()
  {
    super(DateRangePickerCssResourceReference.class, Debug.inDebugMode()
        ? "daterangepicker-bs3.css"
        : "daterangepicker-bs3.css");
  }

  /**
   * Returns the single instance of the CSS resource reference for the thirdparty resource bundled
   * with the Web Application Template.
   *
   * @return the single instance of the CSS resource reference for the thirdparty resource bundled
   *         with the Web Application Template
   */
  public static DateRangePickerCssResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the CSS header item for the thirdparty resource bundled with the Web Application
   * Template.
   *
   * @return the CSS header item for the thirdparty resource bundled with the Web Application
   *         Template
   */
  public static CssHeaderItem getCssHeaderItem()
  {
    return CSS_HEADER_ITEM;
  }
}
