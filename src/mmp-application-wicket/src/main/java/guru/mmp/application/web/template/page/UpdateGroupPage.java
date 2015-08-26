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

package guru.mmp.application.web.template.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.Group;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.GroupInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UpdateGroupPage</code> class implements the
 * "Update Group" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_UPDATE_GROUP)
public class UpdateGroupPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateGroupPage.class);

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UpdateGroupPage</code>.
   *
   * @param previousPage the previous page
   * @param groupModel   the model for the group
   */
  public UpdateGroupPage(final PageReference previousPage, final IModel<Group> groupModel)
  {
    super("Update Group");

    try
    {
      Form<Group> updateForm = new Form<>("updateForm", new CompoundPropertyModel<>(groupModel));

      updateForm.add(new GroupInputPanel("group", groupModel, true));

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            Group group = groupModel.getObject();

            securityService.updateGroup(group, getRemoteAddress());

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the group: " + e.getMessage(), e);
            UpdateGroupPage.this.error("Failed to update the group");
          }
        }
      };
      updateButton.setDefaultFormProcessing(true);
      updateForm.add(updateButton);

      // The "cancelButton" button
      Button cancelButton = new Button("cancelButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          setResponsePage(previousPage.getPage());
        }
      };
      cancelButton.setDefaultFormProcessing(false);
      updateForm.add(cancelButton);

      add(updateForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the UpdateGroupPage", e);
    }
  }

  /**
   * Hidden <code>UpdateGroupPage</code> constructor.
   */
  protected UpdateGroupPage() {}
}
