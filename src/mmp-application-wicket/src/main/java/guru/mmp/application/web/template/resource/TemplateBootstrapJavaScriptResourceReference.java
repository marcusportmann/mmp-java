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
import guru.mmp.application.web.resource.thirdparty.greensockjs
  .TweenLiteJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.jquery.JQueryJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.jqueryui.JQueryUIJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.moment.MomentJavaScriptResourceReference;
import guru.mmp.application.web.resource.thirdparty.rwdtable.RWDTableJavaScriptResourceReference;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TemplateBootstrapJavaScriptResourceReference</code> class implements the
 * JavaScript resource reference for the template-bootstrap.js resource that forms part of the
 * Web Application Template.
 *
 * @author Marcus Portmann
 */
public class TemplateBootstrapJavaScriptResourceReference
  extends JavaScriptResourceReference
{
  private static final TemplateBootstrapJavaScriptResourceReference INSTANCE = new
    TemplateBootstrapJavaScriptResourceReference();

  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM = JavaScriptHeaderItem
    .forReference(
    new TemplateBootstrapJavaScriptResourceReference());

  private static final long serialVersionUID = 1000000;

  /**
   * Returns the single instance of the JavaScript resource reference for the
   * template-bootstrap.js resource that forms part of the Web Application Template.
   *
   * @return the single instance of the JavaScript resource reference for the
   * template-bootstrap.js resource that forms part of the Web Application Template
   */
  public static TemplateBootstrapJavaScriptResourceReference get()
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

  private TemplateBootstrapJavaScriptResourceReference()
  {
    super(TemplateBootstrapJavaScriptResourceReference.class,
      Debug.inDebugMode() ? "js/template-bootstrap.js" : "js/template-bootstrap.js");
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
    dependencies.add(JQueryUIJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(MomentJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(TweenLiteJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(RWDTableJavaScriptResourceReference.getJavaScriptHeaderItem());

    return dependencies;
  }
}
