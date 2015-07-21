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
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TemplateCoreCssResourceReference</code> class implements the CSS resource
 * reference for the template-core.css CSS file that forms part of the
 * Web Application Template.
 *
 * @author Marcus Portmann
 */
public class TemplateCoreCssResourceReference
  extends CssResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final TemplateCoreCssResourceReference INSTANCE =
    new TemplateCoreCssResourceReference();
  private static final CssHeaderItem CSS_HEADER_ITEM =
    CssHeaderItem.forReference(new TemplateCoreCssResourceReference());

  private TemplateCoreCssResourceReference()
  {
    super(TemplateCoreCssResourceReference.class, Debug.inDebugMode()
      ? "css/template-core.css"
      : "css/template-core.css");
  }

  /**
   * Returns the single instance of the CSS resource reference for the template-core.css
   * CSS file that forms part of the Web Application Template.
   *
   * @return the single instance of the CSS resource reference for the template-core.css
   *         CSS file that forms part of the Web Application Template
   */
  public static TemplateCoreCssResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the CSS header item for the template-core.css CSS file that forms part of the
   * Web Application Template.
   *
   * @return the CSS header item for the template-core.css CSS file that forms part of the
   *         Web Application Template
   */
  public static CssHeaderItem getCssHeaderItem()
  {
    return CSS_HEADER_ITEM;
  }

  /**
   * Returns the dependencies for the CSS resource reference.
   *
   * @return the dependencies for the CSS resource reference
   */
  @Override
  public Iterable<? extends HeaderItem> getDependencies()
  {
    List<HeaderItem> dependencies = new ArrayList<>();

    dependencies.add(TemplateCoreCssResourceReference.getCssHeaderItem());

    return dependencies;
  }
}
