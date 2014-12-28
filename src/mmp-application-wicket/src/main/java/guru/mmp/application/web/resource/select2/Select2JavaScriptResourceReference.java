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

package guru.mmp.application.web.resource.select2;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import guru.mmp.application.web.resource.jquery.JQueryJavaScriptResourceReference;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Select2JavaScriptResourceReference</code> class implements the JavaScript resource
 * reference for the Select2 library bundled with the web application library.
 *
 * @author Marcus Portmann
 */
public class Select2JavaScriptResourceReference extends JavaScriptResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM =
    JavaScriptHeaderItem.forReference(new Select2JavaScriptResourceReference());
  private static final Select2JavaScriptResourceReference INSTANCE =
    new Select2JavaScriptResourceReference();

  private Select2JavaScriptResourceReference()
  {
    super(Select2JavaScriptResourceReference.class, Debug.inDebugMode()
        ? "js/select2.min.js"
        : "js/select2.min.js");
  }

  /**
   * Returns the single instance of the JavaScript resource reference for the Select2 library
   * bundled with the web application library.
   *
   * @return the single instance of the JavaScript resource reference for the Select2 library
   *         bundled with the web application library
   */
  public static Select2JavaScriptResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the JavaScript header item for the Select2 library bundled with the web application
   * library.
   *
   * @return the JavaScript header item for the Select2 library bundled with the web application
   *         library
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
  public Iterable<? extends HeaderItem> getDependencies()
  {
    List<HeaderItem> dependencies = new ArrayList<>();

    dependencies.add(JQueryJavaScriptResourceReference.getJavaScriptHeaderItem());

    return dependencies;
  }
}
