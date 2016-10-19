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
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ProcessDefinitionInputPanel</code> class provides a Wicket component that can
 * be used to capture the information for a <code>ProcessDefinition</code>.
 *
 * @author Marcus Portmann
 */
public class ProcessDefinitionInputPanel extends InputPanel
{
  private static final long serialVersionUID = 1000000;
  @SuppressWarnings({ "unused", "MismatchedQueryAndUpdateOfCollection" })
  private List<FileUpload> fileUploads;

  /**
   * Constructs a new <code>ProcessDefinitionInputPanel</code>.
   *
   * @param id           the non-null id of this component
   * @param isIdReadOnly <code>true</code> if the ID for the <code>ProcessDefinition</code>
   *                     is readonly or <code>false</code> otherwise
   */
  public ProcessDefinitionInputPanel(String id, boolean isIdReadOnly)
  {
    super(id);

    // The "id" field
    TextField<String> idField = new TextFieldWithFeedback<>("id");
    idField.setRequired(true);
    idField.setEnabled(!isIdReadOnly);
    add(idField);

    // The "name" field
    TextField<String> nameField = new TextFieldWithFeedback<>("name");
    nameField.setRequired(true);
    add(nameField);

    // The "fileUpload" field
    FileUploadField fileUploadField = new FileUploadFieldWithFeedback<>("fileUpload",
        new PropertyModel<>(this, "fileUploads"));
    fileUploadField.setRequired(true);
    add(fileUploadField);
  }

  /**
   * Returns the file upload
   *
   * @return the file upload
   */
  public FileUpload getFileUpload()
  {
    if ((fileUploads != null) && (fileUploads.get(0) != null))
    {
      return fileUploads.get(0);
    }

    return null;
  }
}
