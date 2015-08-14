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

package guru.mmp.sample.web.page.forms;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.resource.thirdparty.*;
import guru.mmp.application.web.resource.thirdparty.select2.Select2CssResourceReference;
import guru.mmp.application.web.template.page.TemplateWebPage;

import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * The <code>AdvancedElementsPage</code> class implements the "Advanced Elements"
 * page for the web application.
 *
 * @author Marcus Portmann
 */
public class AdvancedElementsPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>AdvancedElementsPage</code>.
   */
  public AdvancedElementsPage()
  {
    super("Advanced Form Elements", "The advanced Bootstrap and jQuery form elements");
  }

  /**
   * Render to the web response whatever the component wants to contribute to the head section.
   *
   * @param response  the header response
   */
  @Override
  public void renderHead(IHeaderResponse response)
  {
    super.renderHead(response);

    response.render(DateRangePickerCssResourceReference.getCssHeaderItem());
    response.render(Select2CssResourceReference.getCssHeaderItem());
    response.render(Select2BootstrapCssResourceReference.getCssHeaderItem());
    response.render(JQueryMultiSelectCssResourceReference.getCssHeaderItem());

    response.render(BootstrapDatePickerJavaScriptResourceReference.getJavaScriptHeaderItem());
    response.render(BootstrapTimePickerJavaScriptResourceReference.getJavaScriptHeaderItem());
    response.render(Select2JavaScriptResourceReference.getJavaScriptHeaderItem());
    response.render(DateRangePickerJavaScriptResourceReference.getJavaScriptHeaderItem());
    response.render(BootstrapTagsInputJavaScriptResourceReference.getJavaScriptHeaderItem());
    response.render(JQueryMultiSelectJavaScriptResourceReference.getJavaScriptHeaderItem());
    response.render(TypeaheadJavaScriptResourceReference.getJavaScriptHeaderItem());
  }
}
