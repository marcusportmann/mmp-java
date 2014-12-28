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

package guru.mmp.application.web.resource.jquery;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;

/**
 * The <code>JQueryJavaScriptResourceReference</code> class implements the JavaScript resource
 * reference for the jQuery library bundled with the web application library.
 * <p/>
 * To add a jQuery resource reference to a component, do not use this reference, but use
 * {@link IJavaScriptLibrarySettings#getJQueryReference()} to prevent version conflicts.
 *
 * @author Marcus Portmann
 */
public class JQueryJavaScriptResourceReference extends JavaScriptResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM =
    JavaScriptHeaderItem.forReference(new JQueryJavaScriptResourceReference());
  private static final JQueryJavaScriptResourceReference INSTANCE =
    new JQueryJavaScriptResourceReference();

  private JQueryJavaScriptResourceReference()
  {
    super(JQueryJavaScriptResourceReference.class, Debug.inDebugMode()
        ? "js/jquery.js"
        : "js/jquery.min.js");
  }

  /**
   * Returns the single instance of the JavaScript resource reference for the jQuery library
   * bundled with the web application library.
   * <p/>
   * NOTE: Normally you should not use this method, but use
   * {@link IJavaScriptLibrarySettings#getJQueryReference()} to prevent version conflicts.
   *
   * @return the single instance of the JavaScript resource reference for the jQuery library
   *         bundled with the web application library
   */
  public static JQueryJavaScriptResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the JavaScript header item for the jQuery library bundled with the web application
   * library.
   *
   * @return the JavaScript header item for the jQuery library bundled with the web application
   *         library
   */
  public static JavaScriptHeaderItem getJavaScriptHeaderItem()
  {
    return JAVA_SCRIPT_HEADER_ITEM;
  }
}
