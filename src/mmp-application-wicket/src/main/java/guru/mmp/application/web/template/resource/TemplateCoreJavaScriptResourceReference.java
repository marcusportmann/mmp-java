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

package guru.mmp.application.web.template.resource;

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TemplateCoreJavaScriptResourceReference</code> class implements the
 * JavaScript resource reference for the template-core.js resource that forms part of the
 * Web Application Template.
 *
 * @author Marcus Portmann
 */
public class TemplateCoreJavaScriptResourceReference
  extends JavaScriptResourceReference
{
  private static final TemplateCoreJavaScriptResourceReference INSTANCE = new
    TemplateCoreJavaScriptResourceReference();

  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM = JavaScriptHeaderItem
    .forReference(
    new TemplateCoreJavaScriptResourceReference());

  private static final long serialVersionUID = 1000000;

  /**
   * Returns the single instance of the JavaScript resource reference for the
   * template-custom.js resource that forms part of the Web Application Template.
   *
   * @return the single instance of the JavaScript resource reference for the
   * template-custom.js resource that forms part of the Web Application Template
   */
  public static TemplateCoreJavaScriptResourceReference get()
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

  private TemplateCoreJavaScriptResourceReference()
  {
    super(TemplateCoreJavaScriptResourceReference.class,
      Debug.inDebugMode() ? "js/template-core.js" : "js/template-core.min.js");
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

    dependencies.add(TemplateBootstrapJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TemplateCombinedJavaScriptResourceReference.getJavaScriptHeaderItem());

    return dependencies;
  }
}
