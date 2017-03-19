/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application.web.template.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.navigation.NavigationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>LogoutPage</code> class implements the "Logout"
 * page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class LogoutPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(LogoutPage.class);
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>LogoutPage</code>.
   */
  public LogoutPage()
  {
    super("Logout");
  }

  @Override
  protected void onBeforeRender()
  {
    try
    {
      TemplateWebSession webSession = ((TemplateWebSession) getWebApplicationSession());

      webSession.invalidateNow();

      NavigationState navigationState = webSession.getNavigationState();

      navigationState.invalidate();
    }
    catch (Throwable e)
    {
      logger.error("Failed to invalidate the web session and navigation state", e);
    }

    super.onBeforeRender();
  }
}
