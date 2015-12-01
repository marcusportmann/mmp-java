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

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.security.SecurityException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.component.StringSelectOption;
import guru.mmp.application.web.page.SecureAnonymousWebPage;
import guru.mmp.application.web.page.WebPage;
import guru.mmp.application.web.resource.thirdparty.select2.Select2BootstrapCssResourceReference;
import guru.mmp.application.web.resource.thirdparty.select2.Select2CssResourceReference;
import guru.mmp.application.web.resource.thirdparty.select2.Select2JavaScriptResourceReference;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.component.Alerts;
import guru.mmp.application.web.template.component.DropDownChoiceWithFeedback;
import guru.mmp.application.web.template.resource.TemplateCssResourceReference;
import guru.mmp.application.web.template.resource.TemplateJavaScriptResourceReference;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SelectOrganisationPage</code> class implements the "Select Organisation"
 * page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SecureAnonymousWebPage
public class SelectOrganisationPage extends WebPage
{
  private transient static CssReferenceHeaderItem applicationCssHeaderItem;
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SelectOrganisationPage.class);
  private StringSelectOption organisation = null;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>SelectOrganisationPage</code>.
   *
   * @throws SecurityException
   */
  public SelectOrganisationPage()
    throws SecurityException
  {
    // Setup the page title
    String title = ((TemplateWebApplication) getApplication()).getDisplayName()
      + " | Select Organisation";

    Label titleLabel = new Label("pageTitle", title);
    titleLabel.setRenderBodyOnly(false);
    add(titleLabel);

    // Setup the alerts
    add(new Alerts("alerts"));

    Form<Void> form = new Form<>("selectOrganisationForm");
    add(form);

    // The "organisation" field
    ChoiceRenderer<StringSelectOption> choiceRenderer = new ChoiceRenderer<>("name", "value");

    DropDownChoice<StringSelectOption> organisationField =
      new DropDownChoiceWithFeedback<>("organisation", new PropertyModel<>(this, "organisation"),
        getOrganisationOptions(), choiceRenderer);
    organisationField.setRequired(true);
    form.add(organisationField);

    // The "continueButton" button
    form.add(new Button("continueButton")
    {
      private static final long serialVersionUID = 1000000;

      @Override
      public void onSubmit()
      {
        WebSession session = getWebApplicationSession();

        try
        {
          List<String> groupNames =
            securityService.getGroupNamesForUser(session.getUserDirectoryId(),
              session.getUsername());
          List<String> functionCodes =
            securityService.getFunctionCodesForUser(session.getUserDirectoryId(),
              session.getUsername());

          session.setOrganisation(organisation.getValue());
          session.setGroupNames(groupNames);
          session.setFunctionCodes(functionCodes);

          if (logger.isDebugEnabled())
          {
            logger.debug("Successfully authenticated user (" + session.getUsername()
                + ") for organisation (" + organisation.getValue() + ") with groups ("
                + StringUtil.delimit(groupNames, ",") + ") and function codes ("
                + StringUtil.delimit(functionCodes, ",") + ")");
          }

          // Redirect to the secure home page for the application
          throw new RedirectToUrlException(
              urlFor(
                ((TemplateWebApplication) getApplication()).getSecureHomePage(),
                  new PageParameters()).toString());
        }
        catch (RedirectToUrlException e)
        {
          throw e;
        }
        catch (Throwable e)
        {
          logger.error("Failed to select the organisation for the user (" + session.getUsername()
              + ")", e);

          session.invalidateNow();

          throw new RedirectToUrlException(
              urlFor(
                ((TemplateWebApplication) getApplication()).getHomePage(),
                  new PageParameters()).toString());
        }
      }
    });
  }

  /**
   * Returns the template web application.
   *
   * @return the template web application
   */
  public TemplateWebApplication getWebApplication()
  {
    return (TemplateWebApplication) getApplication();
  }

  /**
   * Render to the web response whatever the page wants to contribute to the head section.
   *
   * @param response the header response
   */
  @Override
  public void renderHead(IHeaderResponse response)
  {
    super.renderHead(response);

    // Add the Web Application Template theme CSS header item
    response.render(TemplateCssResourceReference.getCssHeaderItem());

    // Add the application CSS header item
    response.render(getApplicationCssHeaderItem());

    // Add the Web Application Template JavaScript header item
    response.render(TemplateJavaScriptResourceReference.getJavaScriptHeaderItem());

    response.render(Select2CssResourceReference.getCssHeaderItem());
    response.render(Select2BootstrapCssResourceReference.getCssHeaderItem());
    response.render(Select2JavaScriptResourceReference.getJavaScriptHeaderItem());
  }

  private CssReferenceHeaderItem getApplicationCssHeaderItem()
  {
    if (applicationCssHeaderItem == null)
    {
      applicationCssHeaderItem =
        CssHeaderItem.forReference(getWebApplication().getApplicationCssResourceReference());
    }

    return applicationCssHeaderItem;
  }

  private List<StringSelectOption> getOrganisationOptions()
    throws SecurityException
  {
    WebSession session = getWebApplicationSession();

    List<Organisation> organisations =
      securityService.getOrganisationsForUserDirectory(session.getUserDirectoryId());

    List<StringSelectOption> organisationOptions = new ArrayList<>();

    for (Organisation organisation : organisations)
    {
      organisationOptions.add(new StringSelectOption(organisation.getName(),
          organisation.getCode()));
    }

    return organisationOptions;
  }
}
