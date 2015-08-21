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

import guru.mmp.application.web.resource.thirdparty.greensockjs.TweenMaxJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.jquery.JQueryJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.jqueryui.JQueryUIJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.moment.MomentJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.rwdtable.RWDTableJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.selectboxit.JQuerySelectBoxItJavaScriptResourceReference;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TemplateJavaScriptResourceReference</code> class implements the
 * JavaScript resource reference for the template.js resource that forms part of the
 * Web Application Template.
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
   * Returns the single instance of the JavaScript resource reference for the
   * template.js resource that forms part of the Web Application Template.
   *
   * @return the single instance of the JavaScript resource reference for the
   *         template.js resource that forms part of the Web Application Template
   */
  public static TemplateJavaScriptResourceReference get()
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
   * Returns the dependencies for the CSS resource reference.
   *
   * @return the dependencies for the CSS resource reference
   */
  @Override
  public List<HeaderItem> getDependencies()
  {
    List<HeaderItem> dependencies = new ArrayList<>();

    dependencies.add(JQueryJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(JQueryUIJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TweenMaxJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(MomentJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(JQuerySelectBoxItJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(RWDTableJavaScriptResourceReference.getJavaScriptHeaderItem());

    dependencies.add(TemplateBootstrapJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TemplateResizeableJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TemplateJoinableJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TemplateApiJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TemplateTogglesJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TemplateCoreJavaScriptResourceReference.getJavaScriptHeaderItem());

    return dependencies;
  }
}
