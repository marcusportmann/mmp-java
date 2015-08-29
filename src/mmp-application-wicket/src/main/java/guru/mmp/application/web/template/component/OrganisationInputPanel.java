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

import guru.mmp.application.web.component.TextFieldWithFeedback;
import org.apache.wicket.markup.html.form.TextField;

/**
 * The <code>OrganisationInputPanel</code> class provides a Wicket component that can
 * be used to capture the information for a <code>Organisation</code>.
 *
 * @author Marcus Portmann
 */
public class OrganisationInputPanel extends InputPanel
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>OrganisationInputPanel</code>.
   *
   * @param id                the non-null id of this component
   * @param isCodeReadOnly    <code>true</code> if the code for the <code>Organisation</code>
   *                          is readonly or <code>false</code> otherwise
   */
  public OrganisationInputPanel(String id, boolean isCodeReadOnly)
  {
    super(id);

    // The "code" field
    TextField<String> codeField = new TextFieldWithFeedback<>("code");
    codeField.setRequired(true);
    codeField.setEnabled(!isCodeReadOnly);
    add(codeField);

    // The "name" field
    TextField<String> nameField = new TextFieldWithFeedback<>("name");
    nameField.setRequired(true);
    add(nameField);

    // The "description" field
    TextField<String> descriptionField = new TextFieldWithFeedback<>("description");
    descriptionField.setRequired(false);
    add(descriptionField);
  }
}
