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

package guru.mmp.application.web.template.resource;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import guru.mmp.application.web.resource.bootstrap.BootstrapHoverDropdownJavaScriptResourceReference;
import guru.mmp.application.web.resource.bootstrap.BootstrapJavaScriptResourceReference;
import guru.mmp.application.web.resource.jquery.JQueryCookieJavaScriptResourceReference;
import guru.mmp.application.web.resource.jquery.JQueryJavaScriptResourceReference;
import guru.mmp.application.web.resource.jquery.JQueryUIJavaScriptResourceReference;
import guru.mmp.application.web.resource.less.LessJavaScriptResourceReference;
import guru.mmp.application.web.resource.perfectscrollbar.PerfectScrollbarJavaScriptResourceReference;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TemplateJavaScriptResourceReference</code> class implements the JavaScript resource
 * reference for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class TemplateJavaScriptResourceReference extends JavaScriptResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM =
    JavaScriptHeaderItem.forReference(new TemplateJavaScriptResourceReference());
  private static final TemplateJavaScriptResourceReference INSTANCE =
    new TemplateJavaScriptResourceReference();

  private TemplateJavaScriptResourceReference()
  {
    super(TemplateJavaScriptResourceReference.class, Debug.inDebugMode()
        ? "js/template.js"
        : "js/template.js");
  }

  /**
   * Returns the single instance of the JavaScript resource reference for the Web Application
   * Template.
   *
   * @return the single instance of the JavaScript resource reference for the Web Application
   *         Template
   */
  public static TemplateJavaScriptResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the JavaScript header item for the Web Application Template.
   *
   * @return the JavaScript header item for the Web Application Template
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
    dependencies.add(JQueryUIJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(JQueryCookieJavaScriptResourceReference.getJavaScriptHeaderItem());

    dependencies.add(BootstrapJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(BootstrapHoverDropdownJavaScriptResourceReference.getJavaScriptHeaderItem());

    dependencies.add(PerfectScrollbarJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(LessJavaScriptResourceReference.getJavaScriptHeaderItem());

    return dependencies;
  }
}
