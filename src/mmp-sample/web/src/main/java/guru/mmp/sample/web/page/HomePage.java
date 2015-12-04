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

package guru.mmp.sample.web.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.web.page.AnonymousOnlyWebPage;
import guru.mmp.application.web.template.component.CodeCategoryTypeChoiceRenderer;
import guru.mmp.application.web.template.component.DropDownChoiceWithFeedback;
import guru.mmp.application.web.template.component.DropdownButton;
import guru.mmp.application.web.template.component.UserDirectoryChoiceRenderer;
import guru.mmp.application.web.template.page.TemplateWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.List;

/**
 * The <code>HomePage</code> class implements the "Home"
 * page for the web application.
 *
 * @author Marcus Portmann
 */
@AnonymousOnlyWebPage
public class HomePage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  @Inject
  private ISecurityService securityService;

  private UserDirectory userDirectory;

  /**
   * Constructs a new <code>HomePage</code>.
   */
  public HomePage()
  {
    super("Home");

    UserDirectoryChoiceRenderer userDirectoryChoiceRenderer =
      new UserDirectoryChoiceRenderer();

    List<UserDirectory> userDirectories = securityService.getUserDirectories();


    add(new Label("name", new PropertyModel<>(this, "userDirectory.name")));

    add(new DropdownButton<>("dropdownButton", new PropertyModel(this, "userDirectory"),
      userDirectories, userDirectoryChoiceRenderer, "fa fa-users"));
  }
}
