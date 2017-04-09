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

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SecurityAdministrationPage</code> class implements the
 * "Security Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_SECURITY_ADMINISTRATION)
public class SecurityAdministrationPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserAdministrationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Autowired
  private ISecurityService securityService;

  /**
   * Constructs a new <code>SecurityAdministrationPage</code>.
   */
  public SecurityAdministrationPage()
  {
    super("Security");

    try {}
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the SecurityAdministrationPage", e);
    }
  }
}
