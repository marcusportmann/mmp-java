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

package guru.mmp.sample.web.page.ui;

//~--- non-JDK imports --------------------------------------------------------

  import guru.mmp.application.web.page.AnonymousOnlyWebPage;
  import guru.mmp.application.web.template.page.TemplateWebPage;

  import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * The <code>AlertsPage</code> class implements the "Alerts"
 * page for the web application.
 *
 * @author Marcus Portmann
 */
public class AlertsPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>AlertsPage</code>.
   */
  public AlertsPage()
  {
    super("Alerts", "Alert boxes and their variants");
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
  }
}
