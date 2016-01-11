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

import guru.mmp.application.security.*;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.behavior.DefaultFocusBehavior;
import guru.mmp.application.web.page.WebPage;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.component.Alerts;
import guru.mmp.application.web.template.component.PasswordTextFieldWithFeedback;
import guru.mmp.application.web.template.resource.TemplateCssResourceReference;
import guru.mmp.application.web.template.resource.TemplateJavaScriptResourceReference;
import guru.mmp.application.web.validation.PasswordPolicyValidator;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * The <code>ChangePasswordPage</code> class implements the "Change Password" page for the Web
 * Application Template.
 *
 * @author Marcus Portmann
 */
public class ChangePasswordPage
  extends WebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ChangePasswordPage.class);

  private static final long serialVersionUID = 1000000;

  private transient static CssReferenceHeaderItem applicationCssHeaderItem;

  @SuppressWarnings("unused")
  private String confirmPassword;

  @SuppressWarnings("unused")
  private String newPassword;

  @SuppressWarnings("unused")
  private String oldPassword;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>ChangePasswordPage</code>.
   */
  @SuppressWarnings("unused")
  protected ChangePasswordPage()
  {}

  /**
   * Constructs a new <code>ChangePasswordPage</code>.
   *
   * @param username the username
   */
  public ChangePasswordPage(String username)
  {
    try
    {
      // Setup the page title
      String title = ((TemplateWebApplication) getApplication()).getDisplayName() + " | Change " +
        "Password";

      Label titleLabel = new Label("pageTitle", title);
      titleLabel.setRenderBodyOnly(false);
      add(titleLabel);

      // Setup the alerts
      Alerts alerts = new Alerts("alerts");
      add(alerts);

      // Setup the changePasswordForm
      Form<Void> changePasswordForm = new Form<>("changePasswordForm");
      changePasswordForm.setMarkupId("changePasswordForm");
      changePasswordForm.setOutputMarkupId(true);
      add(changePasswordForm);

      // The "oldPassword" field
      PasswordTextField oldPasswordField = new PasswordTextFieldWithFeedback("oldPassword",
        new PropertyModel<>(this, "oldPassword"));
      oldPasswordField.setRequired(true);
      oldPasswordField.add(new DefaultFocusBehavior());
      changePasswordForm.add(oldPasswordField);

      // The "newPassword" field
      PasswordTextField newPasswordField = new PasswordTextFieldWithFeedback("newPassword",
        new PropertyModel<>(this, "newPassword"));
      newPasswordField.setRequired(true);
      newPasswordField.add(StringValidator.minimumLength(6));
      newPasswordField.add(new PasswordPolicyValidator());
      changePasswordForm.add(newPasswordField);

      // The "confirmPassword" field
      PasswordTextField confirmPasswordField = new PasswordTextFieldWithFeedback("confirmPassword",
        new PropertyModel<>(this, "confirmPassword"));
      confirmPasswordField.setRequired(true);
      changePasswordForm.add(confirmPasswordField);

      changePasswordForm.add(new EqualPasswordInputValidator(newPasswordField, confirmPasswordField));

      // The "changePasswordButton" button
      changePasswordForm.add(new Button("changePasswordButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            // Authenticate the user
            UUID userDirectoryId = securityService.changePassword(username, oldPassword,
              newPassword);

            // Retrieve the user details
            User user = securityService.getUser(userDirectoryId, username);

            // Initialise the web session for the user
            WebSession session = getWebApplicationSession();

            session.setUserDirectoryId(user.getUserDirectoryId());
            session.setUsername(user.getUsername());
            session.setUserFullName(user.getFirstNames() + user.getLastName());

            // Make session permanent after login
            if (session.isTemporary())
            {
              session.bind();
            }
            else
            {
              session.dirty();  // for cluster replication
            }

            // Invalidate the cached navigation state
            if (session instanceof TemplateWebSession)
            {
              ((TemplateWebSession)session).getNavigationState().invalidate();
            }

            // Check whether the user is associated with more than 1 organisation
            List<Organisation> organisations = securityService.getOrganisationsForUserDirectory(
              userDirectoryId);

            if (organisations.size() == 0)
            {
              error("Authentication Failed.");
              error(
                String.format("The user (%s) is not associated with any organisations.", username));
            }
            else if (organisations.size() == 1)
            {
              List<String> groupNames = securityService.getGroupNamesForUser(userDirectoryId,
                username);
              List<String> functionCodes = securityService.getFunctionCodesForUser(userDirectoryId,
                username);

              session.setOrganisation(organisations.get(0));
              session.setGroupNames(groupNames);
              session.setFunctionCodes(functionCodes);

              if (logger.isDebugEnabled())
              {
                logger.debug(String.format(
                  "Successfully authenticated user (%s) for organisation (%s) with groups (%s) " +
                    "and function codes (%s)", username, organisations.get(0).getId(),
                  StringUtil.delimit(groupNames, ","), StringUtil.delimit(functionCodes, ",")));
              }

              // Redirect to the secure home page for the application
              throw new RedirectToUrlException(
                urlFor(((TemplateWebApplication) getApplication()).getSecureHomePage(),
                  new PageParameters()).toString());
            }
            else
            {
              /*
               * Redirect to the page allowing the user to select which organisation they wish to
               * work with.
               */
              throw new RedirectToUrlException(
                urlFor(SelectOrganisationPage.class, new PageParameters()).toString());
            }
          }
          catch (RedirectToUrlException e)
          {
            throw e;
          }
          catch (ExistingPasswordException e)
          {
            error("The specified new password has been used recently.");
          }
          catch (AuthenticationFailedException | UserNotFoundException e)
          {
            error("The specified old password is incorrect.");
          }
          catch (UserLockedException e)
          {
            error("Your user account has been locked.");
          }
          catch (Throwable e)
          {
            logger.error(
              String.format("Failed to authenticate the user (%s): %s", username, e.getMessage()),
              e);
            error("The system is currently unavailable.");
          }
        }
      });
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the ChangePasswordPage", e);
    }
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
  }

  private CssReferenceHeaderItem getApplicationCssHeaderItem()
  {
    if (applicationCssHeaderItem == null)
    {
      applicationCssHeaderItem = CssHeaderItem.forReference(
        getWebApplication().getApplicationCssResourceReference());
    }

    return applicationCssHeaderItem;
  }
}
