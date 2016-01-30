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

package guru.mmp.application.web.template.components;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.markup.html.form.TextField;

import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ConfigurationValueInputPanel</code> class provides a Wicket component that can
 * be used to capture the information for a <code>ConfigurationValue</code>.
 *
 * @author Marcus Portmann
 */
public class ConfigurationValueInputPanel extends InputPanel
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ConfigurationValueInputPanel</code>.
   *
   * @param id           the non-null id of this component
   * @param isIdReadOnly <code>true</code> if the ID for the <code>ConfigurationValue</code>
   *                     is readonly or <code>false</code> otherwise
   */
  public ConfigurationValueInputPanel(String id, boolean isIdReadOnly)
  {
    super(id);

    // The "key" field
    TextField<UUID> keyField = new TextFieldWithFeedback<>("key");
    keyField.setRequired(true);
    keyField.setEnabled(!isIdReadOnly);
    add(keyField);

    // The "value" field
    TextField<String> valueField = new TextFieldWithFeedback<>("value");
    valueField.setRequired(true);
    add(valueField);

    // The "description" field
    TextField<String> descriptionField = new TextFieldWithFeedback<>("description");
    descriptionField.setRequired(false);
    add(descriptionField);
  }
}
