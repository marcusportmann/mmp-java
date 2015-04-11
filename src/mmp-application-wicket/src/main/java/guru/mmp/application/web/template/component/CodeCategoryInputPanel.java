/*
 * Copyright 2014 Marcus Portmann
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

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.CodesServiceException;
import guru.mmp.application.web.component.DropDownChoiceWithFeedback;
import guru.mmp.application.web.component.TextAreaWithFeedback;
import guru.mmp.application.web.component.TextFieldWithFeedback;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeCategoryInputPanel</code> class provides a Wicket component that can
 * be used to capture the information for a <code>CodeCategory</code>.
 *
 * @author Marcus Portmann
 */
public class CodeCategoryInputPanel extends InputPanel
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
   * @param id                the non-null id of this component
   * @param codeCategoryModel the <code>CodeCategory</code> model
   * @param isIdReadOnly      <code>true</code> if the ID for the <code>CodeCategory</code>
   *                          is readonly or <code>false</code> otherwise
   */
  public CodeCategoryInputPanel(String id, IModel<CodeCategory> codeCategoryModel,
      boolean isIdReadOnly)
  {
    super(id, codeCategoryModel);

    // The "id" field
    TextField<String> idField = new TextFieldWithFeedback<>("id");
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
    CodeCategoryTypeChoiceRenderer codeCategoryTypeChoiceRenderer =
      new CodeCategoryTypeChoiceRenderer();

    categoryTypeField = new DropDownChoiceWithFeedback<>("categoryType",
        getCodeCategoryTypeOptions(), codeCategoryTypeChoiceRenderer);

    categoryTypeField.add(new AjaxFormComponentUpdatingBehavior("onchange")
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
    add(codeDataContainer);

    // The "endPointContainer" container
    setupEndPointContainer(codeCategoryModel);
    add(endPointContainer);
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

    // codeDataField.clearInput();

    target.add(codeDataContainer);

    // Reset the "endPointContainer"
    endPointField.setModelObject(null);

    // endPointField.clearInput();

    isEndPointSecureField.setModelObject(false);

    // isEndPointSecureField.clearInput();

    isCacheableField.setModelObject(false);

    // isCacheableField.clearInput();

    cacheExpiryField.setModelObject(null);

    // cacheExpiryField.clearInput();
    cacheExpiryField.setEnabled(false);

    target.add(endPointContainer);
  }

  private void setupCodeDataContainer()
  {
    codeDataContainer = new WebMarkupContainer("codeDataContainer")
    {
      private static final long serialVersionUID = 1000000;

      private boolean isVisible;

      @Override
      public boolean isVisible()
      {
        return isVisible;
      }

      @Override
      protected void onConfigure()
      {
        super.onConfigure();

        isVisible = (categoryTypeField.getDefaultModelObject() == CodeCategoryType.LOCAL_CUSTOM);
      }
    };

    codeDataContainer.setOutputMarkupId(true);
    codeDataContainer.setOutputMarkupPlaceholderTag(true);

    // The "codeData" field
    codeDataField = new TextAreaWithFeedback<>("codeData");
    codeDataField.setRequired(true);
    codeDataContainer.add(codeDataField);
  }

  private void setupEndPointContainer(IModel<CodeCategory> codeCategoryModel)
  {
    endPointContainer = new WebMarkupContainer("endPointContainer")
    {
      private static final long serialVersionUID = 1000000;

      private boolean isVisible;

      @Override
      public boolean isVisible()
      {
        return isVisible;
      }

      @Override
      protected void onConfigure()
      {
        super.onConfigure();

        isVisible = ((categoryTypeField.getDefaultModelObject()
            == CodeCategoryType.REMOTE_HTTP_SERVICE) || (categoryTypeField.getDefaultModelObject()
              == CodeCategoryType.REMOTE_WEB_SERVICE));
      }
    };

    endPointContainer.setOutputMarkupId(true);
    endPointContainer.setOutputMarkupPlaceholderTag(true);

    // The "endPoint" field
    endPointField = new TextFieldWithFeedback<>("endPoint");
    endPointField.setRequired(true);
    endPointContainer.add(endPointField);

    // The "isEndPointSecure" field
    isEndPointSecureField = new CheckBox("isEndPointSecure");
    endPointContainer.add(isEndPointSecureField);

    // The "isCacheable" field
    isCacheableField = new CheckBox("isCacheable");

    isCacheableField.add(new AjaxFormComponentUpdatingBehavior("onchange")
    {
      private static final long serialVersionUID = 1000000;

      @Override
      protected void onUpdate(AjaxRequestTarget target)
      {
        cacheExpiryField.setModelObject(null);
        cacheExpiryField.setEnabled(isCacheableField.getModelObject());
        cacheExpiryField.setRequired(isCacheableField.getModelObject());

        target.add(cacheExpiryField);
      }
    });

    isCacheableField.setOutputMarkupId(true);
    endPointContainer.add(isCacheableField);

    // The "cacheExpiry" field
    cacheExpiryField = new TextFieldWithFeedback<>("cacheExpiry");
    cacheExpiryField.setEnabled(codeCategoryModel.getObject().getIsCacheable());
    cacheExpiryField.setRequired(codeCategoryModel.getObject().getIsCacheable());
    cacheExpiryField.setOutputMarkupId(true);
    endPointContainer.add(cacheExpiryField);
  }
}
