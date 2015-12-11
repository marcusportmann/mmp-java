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

package guru.mmp.application.web.template.component;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.markup.html.panel.Panel;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 * The <code>UserDirectoryAdministrationPanel</code> class provides the base class that all Wicket
 * components that are used to administer the configuration for a user directory type should extend.
 *
 * @author Marcus Portmann
 */
public abstract class UserDirectoryAdministrationPanel extends Panel
{
  /**
   * Constructs a new <code>UserDirectoryAdministrationPanel</code>.
   *
   * @param id         the non-null id of this component
   * @param parameters the parameters for the user directory
   */
  public UserDirectoryAdministrationPanel(String id, Map<String, String> parameters)
  {
    super(id);

    initParameters(parameters);
  }

  /**
   * Initialise the user directory parameters.
   *
   * @param parameters the user directory parameters
   */
  protected abstract void initParameters(Map<String, String> parameters);
}