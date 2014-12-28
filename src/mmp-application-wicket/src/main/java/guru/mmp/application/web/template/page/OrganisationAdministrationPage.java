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

package guru.mmp.application.web.template.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.component.Dialog;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>OrganisationAdministrationPage</code> class implements the
 * "Organisation Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_ORGANISATION_ADMINISTRATION)
public class OrganisationAdministrationPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger =
    LoggerFactory.getLogger(OrganisationAdministrationPage.class);

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>OrganisationAdministrationPage</code>.
   */
  public OrganisationAdministrationPage()
  {
    super("Organisation Administration", "Organisation Administration");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Organisation Administration");

    /*
     * The table container, which allows the table and its associated navigator to be updated
     * using AJAX.
     */
    final WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
    tableContainer.setOutputMarkupId(true);
    add(tableContainer);

    // The dialog used to confirm the removal of an organisation
    final RemoveDialog removeDialog = new RemoveDialog(tableContainer);
    add(removeDialog);

    // The "addLink" used to add a new organisation
    Link<Void> addLink = new Link<Void>("addLink")
    {
      private static final long serialVersionUID = 1000000;

      @Override
      public void onClick()
      {
        setResponsePage(new AddOrganisationPage(getPageReference()));
      }
    };

    add(addLink);

    // The organisation list view
    LoadableDetachableModel<List<Organisation>> ldm =
      new LoadableDetachableModel<List<Organisation>>()
    {
      private static final long serialVersionUID = 1000000;

      @Override
      protected List<Organisation> load()
      {
        try
        {
          return securityService.getOrganisations(getRemoteAddress());
        }
        catch (Throwable e)
        {
          throw new WebApplicationException(
              "Failed to retrieve a complete list of organisations from the Security Service", e);
        }
      }
    };

    ListView<Organisation> listView = new ListView<Organisation>("organisation", ldm)
    {
      private static final long serialVersionUID = 1000000;

      @Override
      protected void populateItem(ListItem<Organisation> item)
      {
        final IModel<Organisation> organisationModel = item.getModel();

        item.add(new Label("code", new PropertyModel<String>(organisationModel, "code")));
        item.add(new Label("name", new PropertyModel<String>(organisationModel, "name")));

        // The "updateLink" link
        final Link<Void> updateLink = new Link<Void>("updateLink")
        {
          private static final long serialVersionUID = 1000000;

          @Override
          public void onClick()
          {
            UpdateOrganisationPage page = new UpdateOrganisationPage(getPageReference(),
              new Model<>(organisationModel.getObject()));

            setResponsePage(page);
          }
        };
        item.add(updateLink);

        // The "removeLink" link
        final AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
        {
          private static final long serialVersionUID = 1000000;

          @Override
          public void onClick(AjaxRequestTarget target)
          {
            removeDialog.show(target, organisationModel);
          }
        };
        item.add(removeLink);
      }
    };
    tableContainer.add(listView);
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal of an
   * organisation to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private String code;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the organisation table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(final WebMarkupContainer tableContainer)
    {
      super("removeDialog");

      nameLabel = new Label("name", Model.of(""));
      nameLabel.setOutputMarkupId(true);
      add(nameLabel);

      add(new AjaxLink<Void>("removeLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick(AjaxRequestTarget target)
        {
          try
          {
            securityService.deleteOrganisation(code, getRemoteAddress());

            target.add(tableContainer);

            OrganisationAdministrationPage.this.info("Successfully removed the organisation "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error("Failed to remove the organisation (" + code + "): " + e.getMessage(), e);

            OrganisationAdministrationPage.this.error("Failed to remove the organisation "
                + nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      });
    }

    /**
     * @see Dialog#hide(org.apache.wicket.ajax.AjaxRequestTarget)
     *
     * @param target the AJAX request target
     */
    public void hide(AjaxRequestTarget target)
    {
      super.hide(target);
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target            the AJAX request target
     * @param organisationModel the model for the organisation being removed
     */
    public void show(AjaxRequestTarget target, IModel<Organisation> organisationModel)
    {
      Organisation organisation = organisationModel.getObject();

      code = organisation.getCode();
      nameLabel.setDefaultModelObject(organisation.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}
