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

package guru.mmp.application.web.template.resource;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;

import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * The <code>TemplateApiJavaScriptResourceReference</code> class implements the
 * JavaScript resource reference for the template-api.js resource that forms part of the
 * Web Application Template.
 *
 * @author Marcus Portmann
 */
public class TemplateApiJavaScriptResourceReference extends JavaScriptResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM =
    JavaScriptHeaderItem.forReference(new TemplateApiJavaScriptResourceReference());
  private static final TemplateApiJavaScriptResourceReference INSTANCE =
    new TemplateApiJavaScriptResourceReference();

  private TemplateApiJavaScriptResourceReference()
  {
    super(TemplateApiJavaScriptResourceReference.class, Debug.inDebugMode()
        ? "js/template-api.js"
        : "js/template-api.js");
  }

  /**
   * Returns the single instance of the JavaScript resource reference for the
   * template-api.js resource that forms part of the Web Application Template.
   *
   * @return the single instance of the JavaScript resource reference for the
   *         template-api.js resource that forms part of the Web Application Template
   */
  public static TemplateApiJavaScriptResourceReference get()
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
}
