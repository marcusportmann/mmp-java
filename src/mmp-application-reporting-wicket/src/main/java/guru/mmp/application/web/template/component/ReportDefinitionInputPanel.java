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

import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.component.TextFieldWithFeedback;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportDefinitionInputPanel</code> class provides a Wicket component that can
 * be used to capture the information for a <code>ReportDefinition</code>.
 *
 * @author Marcus Portmann
 */
public class ReportDefinitionInputPanel extends InputPanel
{
  private static final long serialVersionUID = 1000000;
  @SuppressWarnings("unused")
  private List<FileUpload> fileUploads;

  /**
   * Constructs a new <code>ReportDefinitionInputPanel</code>.
   *
   * @param id                    the non-null id of this component
   * @param reportDefinitionModel the <code>ReportDefinition</code> model
   * @param isIdReadOnly          <code>true</code> if the ID for the <code>Code</code>
   *                              is readonly or <code>false</code> otherwise
   */
  public ReportDefinitionInputPanel(String id, IModel<ReportDefinition> reportDefinitionModel,
      boolean isIdReadOnly)
  {
    super(id, reportDefinitionModel);

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
    FileUploadField fileUploadField = new FileUploadField("fileUpload",
      new PropertyModel<List<FileUpload>>(this, "fileUploads"));
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
