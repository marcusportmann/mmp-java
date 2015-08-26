/*
 * Copyright 2015 Marcus Portmann
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

package guru.mmp.application.web.resource.thirdparty.selectboxit;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import guru.mmp.application.web.resource.thirdparty.jquery.JQueryJavaScriptResourceReference;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>JQuerySelectBoxItJavaScriptResourceReference</code> class implements the
 * JavaScript resource reference for the jQuery Select Box It thirdparty resource bundled
 * with the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class JQuerySelectBoxItJavaScriptResourceReference extends JavaScriptResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM =
    JavaScriptHeaderItem.forReference(new JQuerySelectBoxItJavaScriptResourceReference());
  private static final JQuerySelectBoxItJavaScriptResourceReference INSTANCE =
    new JQuerySelectBoxItJavaScriptResourceReference();

  private JQuerySelectBoxItJavaScriptResourceReference()
  {
    super(JQuerySelectBoxItJavaScriptResourceReference.class, Debug.inDebugMode()
        ? "jquery.selectBoxIt.js"
        : "jquery.selectBoxIt.min.js");
  }

  /**
   * Returns the single instance of the JavaScript resource reference for the jQuery Select Box It
   * thirdparty resource bundled with the Web Application Template
   *
   * @return the single instance of the JavaScript resource reference for the jQuery Select Box It
   *         thirdparty resource bundled with the Web Application Template
   */
  public static JQuerySelectBoxItJavaScriptResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the JavaScript header item for the JavaScript resource reference.
   *
   * @return the JavaScript header item for the JavaScript resource reference
   */
  public static JavaScriptHeaderItem getJavaScriptHeaderItem()
  {
    return JAVA_SCRIPT_HEADER_ITEM;
  }

  /**
   * Returns the dependencies for the JavaScript resource reference.
   *
   * @return the dependencies for the JavaScript resource reference
   */
  @Override
  public List<HeaderItem> getDependencies()
  {
    List<HeaderItem> dependencies = new ArrayList<>();

    dependencies.add(JQueryJavaScriptResourceReference.getJavaScriptHeaderItem());

    // dependencies.add(JQuerySelectBoxItCssResourceReference.getCssHeaderItem());

    return dependencies;
  }
}
