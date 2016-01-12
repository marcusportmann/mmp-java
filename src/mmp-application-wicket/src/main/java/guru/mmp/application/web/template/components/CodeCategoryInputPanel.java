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

import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.CodesServiceException;
import guru.mmp.application.web.WebApplicationException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.string.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The <code>CodeCategoryInputPanel</code> class provides a Wicket component that can
 * be used to capture the information for a <code>CodeCategory</code>.
 *
 * @author Marcus Portmann
 */
public class CodeCategoryInputPanel
  extends InputPanel
{
  private static final long serialVersionUID = 1000000;

  private TextField<Integer> cacheExpiryField;

  private DropDownChoice<CodeCategoryType> categoryTypeField;

  private WebMarkupContainer codeDataContainer;

  private TextArea<String> codeDataField;

  private WebMarkupContainer endPointContainer;

  private TextField<String> endPointField;

  private CheckBox isCacheableField;

  private CheckBox isEndPointSecureField;

  /**
   * Constructs a new <code>CodeCategoryInputPanel</code>.
   *
   * @param id           the non-null id of this component
   * @param isIdReadOnly <code>true</code> if the ID for the <code>CodeCategory</code>
   *                     is readonly or <code>false</code> otherwise
   */
  public CodeCategoryInputPanel(String id, boolean isIdReadOnly)
  {
    super(id);

    try
    {
      // The "id" field
      TextField<UUID> idField = new TextFieldWithFeedback<>("id");
      idField.setRequired(true);
      idField.setEnabled(!isIdReadOnly);
      add(idField);

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      add(nameField);

      // The "description" field
      TextField<String> descriptionField = new TextFieldWithFeedback<>("description");
      descriptionField.setRequired(true);
      add(descriptionField);

      // The "categoryType" field
      CodeCategoryTypeChoiceRenderer codeCategoryTypeChoiceRenderer = new
        CodeCategoryTypeChoiceRenderer();

      categoryTypeField = new DropDownChoiceWithFeedback<>("categoryType",
        getCodeCategoryTypeOptions(), codeCategoryTypeChoiceRenderer);

      categoryTypeField.add(new AjaxFormComponentUpdatingBehavior("change")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void onUpdate(AjaxRequestTarget target)
        {
          try
          {
            target.add(categoryTypeField);

            resetContainers(target);
          }
          catch (Throwable e)
          {
            throw new RuntimeException("Failed to update the categoryType field", e);
          }
        }
      });

      categoryTypeField.setRequired(true);
      add(categoryTypeField);

      // The "codeDataContainer" container
      setupCodeDataContainer();

      // The "endPointContainer" container
      setupEndPointContainer();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the CodeCategoryInputPanel", e);
    }
  }

  private List<CodeCategoryType> getCodeCategoryTypeOptions()
    throws CodesServiceException
  {
    List<CodeCategoryType> codeCategoryTypes = new ArrayList<>();

    codeCategoryTypes.add(CodeCategoryType.LOCAL_STANDARD);
    codeCategoryTypes.add(CodeCategoryType.LOCAL_CUSTOM);
    codeCategoryTypes.add(CodeCategoryType.REMOTE_HTTP_SERVICE);
    codeCategoryTypes.add(CodeCategoryType.REMOTE_WEB_SERVICE);

    return codeCategoryTypes;
  }

  private void resetContainers(AjaxRequestTarget target)
  {
    // Reset the "codeDataContainer" if required
    codeDataField.setModelObject(null);

    target.add(codeDataContainer);

    // Reset the "endPointContainer"
    endPointField.setModelObject(null);

    isEndPointSecureField.setModelObject(false);
    isCacheableField.setModelObject(false);
    cacheExpiryField.setModelObject(null);

    target.add(endPointContainer);
  }

  private void setupCodeDataContainer()
  {
    codeDataContainer = new WebMarkupContainer("codeDataContainer")
    {
      private static final long serialVersionUID = 1000000;

      @Override
      protected void onConfigure()
      {
        super.onConfigure();

        setVisible(
          CodeCategoryType.LOCAL_CUSTOM.getCodeAsString().equals(categoryTypeField.getValue()));
      }
    };

    codeDataContainer.setOutputMarkupId(true);
    codeDataContainer.setOutputMarkupPlaceholderTag(true);
    add(codeDataContainer);

    // The "codeData" field
    codeDataField = new TextAreaWithFeedback<String>("codeData")
    {
      @Override
      protected void onConfigure()
      {
        super.onConfigure();

        setRequired(
          CodeCategoryType.LOCAL_CUSTOM.getCodeAsString().equals(categoryTypeField.getValue()));
      }
    };
    codeDataContainer.add(codeDataField);
  }

  private void setupEndPointContainer()
  {
    endPointContainer = new WebMarkupContainer("endPointContainer")
    {
      private static final long serialVersionUID = 1000000;

      @Override
      protected void onConfigure()
      {
        super.onConfigure();

        setVisible(CodeCategoryType.REMOTE_HTTP_SERVICE.getCodeAsString().equals(
          categoryTypeField.getValue()) ||
          CodeCategoryType.REMOTE_WEB_SERVICE.getCodeAsString().equals(
            categoryTypeField.getValue()));
      }
    };

    endPointContainer.setOutputMarkupId(true);
    endPointContainer.setOutputMarkupPlaceholderTag(true);
    add(endPointContainer);

    // The "endPoint" field
    endPointField = new TextFieldWithFeedback<String>("endPoint")
    {
      @Override
      protected void onConfigure()
      {
        super.onConfigure();

        setRequired((CodeCategoryType.REMOTE_HTTP_SERVICE.getCodeAsString().equals(
          categoryTypeField.getValue()) ||
          CodeCategoryType.REMOTE_WEB_SERVICE.getCodeAsString().equals(
            categoryTypeField.getValue())));
      }
    };
    endPointContainer.add(endPointField);

    // The "isEndPointSecure" field
    isEndPointSecureField = new CheckBox("isEndPointSecure");
    endPointContainer.add(isEndPointSecureField);

    // The "isCacheable" field
    isCacheableField = new CheckBox("isCacheable");

    isCacheableField.add(new AjaxFormComponentUpdatingBehavior("change")
    {
      private static final long serialVersionUID = 1000000;

      @Override
      protected void onUpdate(AjaxRequestTarget target)
      {
        cacheExpiryField.setModelObject(null);

        target.add(cacheExpiryField);
      }
    });

    isCacheableField.setOutputMarkupId(true);
    endPointContainer.add(isCacheableField);

    // The "cacheExpiry" field
    cacheExpiryField = new TextFieldWithFeedback<Integer>("cacheExpiry")
    {
      @Override
      protected void onConfigure()
      {
        super.onConfigure();

        setRequired(Strings.isTrue(isCacheableField.getValue()));
        setEnabled(Strings.isTrue(isCacheableField.getValue()));
      }
    };

    cacheExpiryField.setOutputMarkupId(true);
    endPointContainer.add(cacheExpiryField);
  }
}
