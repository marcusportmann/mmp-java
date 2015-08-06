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

package guru.mmp.application.web.resource.thirdparty;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BootstrapTagsInputJavaScriptResourceReference</code> class implements the JavaScript
 * resource reference for the Bootstrap Tags Input thirdparty resource bundled with the Web
 * Application Template.
 *
 * @author Marcus Portmann
 */
public class BootstrapTagsInputJavaScriptResourceReference extends JavaScriptResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM =
    JavaScriptHeaderItem.forReference(new BootstrapTagsInputJavaScriptResourceReference());
  private static final BootstrapTagsInputJavaScriptResourceReference INSTANCE =
    new BootstrapTagsInputJavaScriptResourceReference();

  private BootstrapTagsInputJavaScriptResourceReference()
  {
    super(BootstrapTagsInputJavaScriptResourceReference.class, Debug.inDebugMode()
        ? "bootstrap-tagsinput/bootstrap-tagsinput.js"
        : "bootstrap-tagsinput/bootstrap-tagsinput.min.js");
  }

  /**
   * Returns the single instance of the JavaScript resource reference for the Bootstrap Tags Input
   * thirdparty resource bundled with the Web Application Template.
   *
   * @return the single instance of the JavaScript resource reference for the Bootstrap Tags Input
   *         thirdparty resource bundled with the Web Application Template
   */
  public static BootstrapTagsInputJavaScriptResourceReference get()
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

    dependencies.add(BootstrapJavaScriptResourceReference.getJavaScriptHeaderItem());

    return dependencies;
  }
}
