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

package guru.mmp.application.web.template.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.Group;
import guru.mmp.application.security.GroupNotFoundException;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.GroupInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AddGroupPage</code> class implements the
 * "Add Group" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_ADD_GROUP)
public class AddGroupPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddGroupPage.class);
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Hidden <code>AddGroupPage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected AddGroupPage() {}

  /**
   * Constructs a new <code>AddGroupPage</code>.
   *
   * @param previousPage    the previous page
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  public AddGroupPage(PageReference previousPage, UUID userDirectoryId)
  {
    super("Add Group");

    try
    {
      Form<Group> addForm = new Form<>("addForm", new CompoundPropertyModel<>(new Model<>(
          new Group())));

      addForm.add(new GroupInputPanel("group", false));

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            Group group = addForm.getModelObject();

            // Check if a group with the specified code already exists and if so return an error
            try
            {
              securityService.getGroup(userDirectoryId, group.getGroupName());

              AddGroupPage.this.error("A group with the specified group name already exists.");

              return;
            }
            catch (GroupNotFoundException e)
            {
              // Do nothing, this is not an error
            }

            securityService.createGroup(userDirectoryId, group);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to add the group: " + e.getMessage(), e);
            AddGroupPage.this.error("Failed to add the group");
          }
        }
      };
      addButton.setDefaultFormProcessing(true);
      addForm.add(addButton);

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
      addForm.add(cancelButton);

      add(addForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the AddGroupPage", e);
    }
  }
}
