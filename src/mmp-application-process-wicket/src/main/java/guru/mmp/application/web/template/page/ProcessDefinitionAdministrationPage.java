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

package guru.mmp.application.web.template.page;

import guru.mmp.application.process.IProcessService;
import guru.mmp.application.process.ProcessDefinition;
import guru.mmp.application.process.ProcessDefinitionSummary;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateProcessSecurity;
import guru.mmp.application.web.template.component.Dialog;
import guru.mmp.application.web.template.component.PagingNavigator;
import guru.mmp.application.web.template.data.ProcessDefinitionSummaryDataProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

/**
 * The <code>ProcessDefinitionAdministrationPage</code> class implements the
 * "Process Definition Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateProcessSecurity.FUNCTION_CODE_PROCESS_DEFINITION_ADMINISTRATION)
public class ProcessDefinitionAdministrationPage
  extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(
    ProcessDefinitionAdministrationPage.class);

  private static final long serialVersionUID = 1000000;

  /* Process Service */
  @Inject
  private IProcessService processService;

  /**
   * Constructs a new <code>ProcessDefinitionAdministrationPage</code>.
   */
  public ProcessDefinitionAdministrationPage()
  {
    super("Process Definitions");

    try
    {
      WebSession session = getWebApplicationSession();

      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a group
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" link
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddProcessDefinitionPage page = new AddProcessDefinitionPage(getPageReference());

          setResponsePage(page);
        }
      };
      tableContainer.add(addLink);

      ProcessDefinitionSummaryDataProvider dataProvider = new
        ProcessDefinitionSummaryDataProvider();

      // The process definition data view
      DataView<ProcessDefinitionSummary> dataView = new DataView<ProcessDefinitionSummary>(
        "processDefinition", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        protected void populateItem(Item<ProcessDefinitionSummary> item)
        {
          item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
          item.add(new Label("version", new PropertyModel<String>(item.getModel(), "version")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              ProcessDefinitionSummary processDefinitionSummary = item.getModelObject();

              try
              {
                ProcessDefinition processDefinition = processService.getProcessDefinition(
                  processDefinitionSummary.getId(), processDefinitionSummary.getVersion());

                UpdateProcessDefinitionPage page = new UpdateProcessDefinitionPage(
                  getPageReference(), new Model<>(processDefinition));

                setResponsePage(page);
              }
              catch (Throwable e)
              {
                logger.error(String.format(
                  "Failed to retrieve the process definition with ID (%s) and version (%d)",
                  processDefinitionSummary.getId(), processDefinitionSummary.getVersion()), e);

                error(String.format(
                  "Failed to retrieve the process definition with ID (%s) and version (%d)",
                  processDefinitionSummary.getId(), processDefinitionSummary.getVersion()));
              }
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
              ProcessDefinitionSummary processDefinitionSummary = item.getModelObject();

              if (processDefinitionSummary != null)
              {
                removeDialog.show(target, processDefinitionSummary);
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
      throw new WebApplicationException(
        "Failed to initialise the ProcessDefinitionAdministrationPage", e);
    }
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal
   * of a process definition to be confirmed.
   */
  private class RemoveDialog
    extends Dialog
  {
    private static final long serialVersionUID = 1000000;

    private UUID id;

    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the code table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(WebMarkupContainer tableContainer)
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
            processService.deleteProcessDefinition(id);

            target.add(tableContainer);

            ProcessDefinitionAdministrationPage.this.info(
              "Successfully removed the process definition " +
                nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error(
              String.format("Failed to remove the process definition (%s): %s", id, e.getMessage()),
              e);

            ProcessDefinitionAdministrationPage.this.error(
              "Failed to remove the process definition " +
                nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      });
    }

    /**
     * @param target the AJAX request target
     *
     * @see Dialog#hide(org.apache.wicket.ajax.AjaxRequestTarget)
     */
    public void hide(AjaxRequestTarget target)
    {
      super.hide(target);
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target                   the AJAX request target
     * @param processDefinitionSummary the summary for the process definition being removed
     */
    public void show(AjaxRequestTarget target, ProcessDefinitionSummary processDefinitionSummary)
    {
      id = processDefinitionSummary.getId();
      nameLabel.setDefaultModelObject(processDefinitionSummary.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}
