/*
 * Copyright 2017 Marcus Portmann
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

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.SecurityException;
import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.security.UserDirectoryType;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.*;
import guru.mmp.application.web.template.data.FilteredUserDirectoryDataProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserDirectoryAdministrationPage</code> class implements the
 * "UserDirectory Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_SECURITY_ADMINISTRATION)
public class UserDirectoryAdministrationPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(
      UserDirectoryAdministrationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UserDirectoryAdministrationPage</code>.
   */
  public UserDirectoryAdministrationPage()
  {
    super("User Directories");

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a user directory
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The dialog used to select the type of user directory being added
      AddDialog selectUserDirectoryTypeDialog = new AddDialog();
      add(selectUserDirectoryTypeDialog);

      // The "addLink" used to add a new user directory
      AjaxLink<Void> addLink = new AjaxLink<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick(AjaxRequestTarget target)
        {
          selectUserDirectoryTypeDialog.show(target);
        }
      };
      tableContainer.add(addLink);

      FilteredUserDirectoryDataProvider dataProvider = new FilteredUserDirectoryDataProvider();

      // The "filterForm" form
      Form<Void> filterForm = new Form<>("filterForm");
      filterForm.setMarkupId("filterForm");
      filterForm.setOutputMarkupId(true);

      // The "filter" field
      TextField<String> filterField = new TextField<>("filter", new PropertyModel<>(dataProvider,
          "filter"));
      filterForm.add(filterField);

      // The "filterButton" button
      Button filterButton = new Button("filterButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit() {}
      };
      filterButton.setDefaultFormProcessing(true);
      filterForm.add(filterButton);

      // The "resetButton" button
      Button resetButton = new Button("resetButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          dataProvider.setFilter("");
        }
      };
      filterForm.add(resetButton);

      tableContainer.add(filterForm);

      // The user directory data view
      DataView<UserDirectory> dataView = new DataView<UserDirectory>("userDirectory", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<UserDirectory> item)
        {
          item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
          item.add(new Label("type", new PropertyModel<String>(item.getModel(), "type.name")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateUserDirectoryPage page = new UpdateUserDirectoryPage(getPageReference(),
                  item.getModel());

              setResponsePage(page);
            }
          };
          item.add(updateLink);

          // The "removeLink" link
          AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              UserDirectory userDirectory = item.getModelObject();

              if (userDirectory != null)
              {
                removeDialog.show(target, userDirectory);
              }
              else
              {
                target.add(tableContainer);
              }
            }
          };
          item.add(removeLink);
        }
      };
      dataView.setItemsPerPage(10);
      dataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
      tableContainer.add(dataView);

      tableContainer.add(new PagingNavigator("navigator", dataView));
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the UserDirectoryAdministrationPage",
          e);
    }
  }

  /**
   * The <code>AddDialog</code> class implements a dialog that allows the user directory type to be
   * selected when adding a user directory.
   */
  private class AddDialog extends FormDialog
  {
    private static final long serialVersionUID = 1000000;
    @SuppressWarnings("unused")
    private UserDirectoryType userDirectoryType;

    /**
     * Constructs a new <code>AddDialog</code>.
     */
    AddDialog()
    {
      super("addDialog", "Add User Directory", "OK", "Cancel");

      try
      {
        UserDirectoryTypeChoiceRenderer userDirectoryTypeChoiceRenderer =
            new UserDirectoryTypeChoiceRenderer();

        // The "userDirectoryType" field
        DropDownChoice<UserDirectoryType> userDirectoryTypeField = new DropDownChoiceWithFeedback<>(
            "userDirectoryType", new PropertyModel<>(this, "userDirectoryType"),
            getUserDirectoryTypeOptions(), userDirectoryTypeChoiceRenderer);
        userDirectoryTypeField.setRequired(true);
        userDirectoryTypeField.setOutputMarkupId(true);
        getForm().add(userDirectoryTypeField);
      }
      catch (Throwable e)
      {
        throw new WebApplicationException("Failed to initialise the AddDialog", e);
      }
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target the AJAX request target
     */
    @Override
    public void show(AjaxRequestTarget target)
    {
      userDirectoryType = null;

      super.show(target);
    }

    /**
     * Process the cancellation of the form associated with the dialog.
     *
     * @param target the AJAX request target
     * @param form   the form
     */
    @Override
    protected void onCancel(AjaxRequestTarget target, Form form) {}

    /**
     * Process the errors for the form associated with the dialog.
     *
     * @param target the AJAX request target
     * @param form   the form
     */
    @Override
    protected void onError(AjaxRequestTarget target, Form form) {}

    /**
     * Process the submission of the form associated with the dialog.
     *
     * @param target the AJAX request target
     * @param form   the form
     *
     * @return <code>true</code> if the form was submitted successfully without errors or
     *         <code>false</code> otherwise
     */
    @Override
    protected boolean onSubmit(AjaxRequestTarget target, Form form)
    {
      try
      {
        Class administrationClass = userDirectoryType.getAdministrationClass();

        if (!UserDirectoryAdministrationPanel.class.isAssignableFrom(administrationClass))
        {
          throw new WebApplicationException(String.format(
              "The administration class (%s) does not extend the UserDirectoryAdministrationPanel "
              + "class", administrationClass.getName()));
        }

        setResponsePage(new AddUserDirectoryPage(getPageReference(), userDirectoryType));

        return true;
      }
      catch (Throwable e)
      {
        logger.error(String.format(
            "Failed to retrieve the administration class (%s) for the user directory type (%s)",
            userDirectoryType.getAdministrationClassName(), userDirectoryType.getName()), e);

        error(target, "Failed to retrieve the administration class for the user directory type");

        return false;
      }
    }

    private List<UserDirectoryType> getUserDirectoryTypeOptions()
      throws SecurityException
    {
      return securityService.getUserDirectoryTypes();
    }
  }


  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal of a
   * user directory to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private UUID id;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the user directory table and its
     *                       associated navigator to be updated using AJAX
     */
    RemoveDialog(WebMarkupContainer tableContainer)
    {
      super("removeDialog");

      nameLabel = new Label("name", Model.of(""));

      nameLabel.setOutputMarkupId(true);
      add(nameLabel);

      // The "removeLink" link
      AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick(AjaxRequestTarget target)
        {
          try
          {
            securityService.deleteUserDirectory(id);

            target.add(tableContainer);

            UserDirectoryAdministrationPage.this.info("Successfully removed the user directory "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error(String.format("Failed to remove the user directory (%s): %s", id,
                e.getMessage()), e);

            UserDirectoryAdministrationPage.this.error("Failed to remove the user directory "
                + nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      };
      add(removeLink);
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target        the AJAX request target
     * @param userDirectory the user directory being removed
     */
    public void show(AjaxRequestTarget target, UserDirectory userDirectory)
    {
      id = userDirectory.getId();

      nameLabel.setDefaultModelObject(userDirectory.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}
