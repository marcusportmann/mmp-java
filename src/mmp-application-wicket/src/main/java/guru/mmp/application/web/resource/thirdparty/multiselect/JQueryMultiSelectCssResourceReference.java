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

package guru.mmp.application.web.resource.thirdparty.multiselect;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * The <code>RWDTableCssResourceReference</code> class implements the CSS resource
 * reference for the JQuery Multi Select thirdparty resource bundled with the Web Application
 * Template.
 *
 * @author Marcus Portmann
 */
public class JQueryMultiSelectCssResourceReference extends CssResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JQueryMultiSelectCssResourceReference INSTANCE =
    new JQueryMultiSelectCssResourceReference();
  private static final CssHeaderItem CSS_HEADER_ITEM =
    CssHeaderItem.forReference(new JQueryMultiSelectCssResourceReference());

  private JQueryMultiSelectCssResourceReference()
  {
    super(JQueryMultiSelectCssResourceReference.class, Debug.inDebugMode()
        ? "css/multi-select.css"
        : "css/multi-select.css");
  }

  /**
   * Returns the single instance of the CSS resource reference for the thirdparty resource bundled
   * with the Web Application Template.
   *
   * @return the single instance of the CSS resource reference for the thirdparty resource bundled
   *         with the Web Application Template
   */
  public static JQueryMultiSelectCssResourceReference get()
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
