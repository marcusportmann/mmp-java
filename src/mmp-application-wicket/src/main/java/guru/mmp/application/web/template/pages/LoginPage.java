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

import guru.mmp.application.Debug;
import guru.mmp.application.security.*;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.behaviors.DefaultFocusBehavior;
import guru.mmp.application.web.pages.WebPage;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.components.Alerts;
import guru.mmp.application.web.template.components.PasswordTextFieldWithFeedback;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;
import guru.mmp.application.web.template.resources.TemplateCssResourceReference;
import guru.mmp.application.web.template.resources.TemplateJavaScriptResourceReference;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>LoginPage</code> class implements the "Login"
 * page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class LoginPage extends WebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
  private static final long serialVersionUID = 1000000;
  private transient static CssReferenceHeaderItem applicationCssHeaderItem;
  private String password;

  /* Security Service */
  @Inject
  private ISecurityService securityService;
  private String username;

  /**
   * Constructs a new <code>LoginPage</code>.
   */
  public LoginPage()
  {
    try
    {
      // Setup the page title
      String title = ((TemplateWebApplication) getApplication()).getDisplayName() + " | Login";

      Label titleLabel = new Label("pageTitle", title);
      titleLabel.setRenderBodyOnly(false);
      add(titleLabel);

      // Setup the alerts
      Alerts alerts = new Alerts("alerts");
      add(alerts);

      // Setup the loginForm
      Form<Void> loginForm = new Form<>("loginForm");
      loginForm.setMarkupId("loginForm");
      loginForm.setOutputMarkupId(true);
      add(loginForm);

      // The "username" field
      TextField<String> usernameField = new TextFieldWithFeedback<>("username", new PropertyModel<>(
          this, "username"));
      usernameField.setRequired(true);
      usernameField.add(new DefaultFocusBehavior());
      loginForm.add(usernameField);

      // The "password" field
      PasswordTextField passwordField = new PasswordTextFieldWithFeedback("password",
          new PropertyModel<>(this, "password"));
      passwordField.setRequired(true);
      loginForm.add(passwordField);

      // The "loginButton" button
      Button loginButton = new Button("loginButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            if (Debug.inDebugMode() && ("s".equals(username)))
            {
              username = "Administrator";
              password = "Password1";
            }

            // Authenticate the user
            UUID userDirectoryId = securityService.authenticate(username, password);

            // Retrieve the user details
            User user = securityService.getUser(userDirectoryId, username);

            // Initialise the web session for the user
            WebSession session = getWebApplicationSession();

            session.setUserDirectoryId(user.getUserDirectoryId());
            session.setUsername(user.getUsername());
            session.setUserFullName(user.getFirstName() + " " + user.getLastName());

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
              ((TemplateWebSession) session).getNavigationState().invalidate();
            }

            // Check whether the user is associated with more than 1 organisation
            List<Organisation> organisations = securityService.getOrganisationsForUserDirectory(
                userDirectoryId);

            if (organisations.size() == 0)
            {
              error(String.format("The user (%s) is not associated with any organisations.",
                  username));
            }
            else if (organisations.size() == 1)
            {
              Organisation organisation = organisations.get(0);

              if (organisation.getStatus() != OrganisationStatus.ACTIVE)
              {
                error("The organisation (" + organisation.getName() + ") is not active.");

                return;
              }

              List<String> groupNames = securityService.getGroupNamesForUser(userDirectoryId,
                  username);
              List<String> functionCodes = securityService.getFunctionCodesForUser(userDirectoryId,
                  username);

              logger.info("The user (" + username + ") is a member of the following groups: "
                  + ((groupNames.size() == 0)
                  ? "None"
                  : StringUtil.delimit(groupNames, ",")));

              logger.info("The user (" + username + ") has access to the following functions: "
                  + ((functionCodes.size() == 0)
                  ? "None"
                  : StringUtil.delimit(functionCodes, ",")));

              session.setOrganisation(organisation);
              session.setGroupNames(groupNames);
              session.setFunctionCodes(functionCodes);

              if (logger.isDebugEnabled())
              {
                logger.debug(String.format(
                    "Successfully authenticated user (%s) for organisation (%s) with groups (%s) "
                    + "and function codes (%s)", username, organisations.get(0).getId(),
                    StringUtil.delimit(groupNames, ","), StringUtil.delimit(functionCodes, ",")));
              }

              // Redirect to the secure home page for the application
              throw new RedirectToUrlException(urlFor(
                  ((TemplateWebApplication) getApplication()).getSecureHomePage(),
                  new PageParameters()).toString());
            }
            else
            {
              /*
               * Redirect to the page allowing the user to select which organisation they wish to
               * work with.
               */
              throw new RedirectToUrlException(urlFor(SelectOrganisationPage.class,
                  new PageParameters()).toString());
            }
          }
          catch (RedirectToUrlException e)
          {
            throw e;
          }
          catch (AuthenticationFailedException | UserNotFoundException e)
          {
            error("The specified username or password is incorrect.");
          }
          catch (UserLockedException e)
          {
            error("Your user account has been locked.");
          }
          catch (ExpiredPasswordException e)
          {
            getRequestCycle().setResponsePage(new ChangePasswordPage(username));
          }
          catch (Throwable e)
          {
            logger.error(String.format("Failed to authenticate the user (%s): %s", username,
                e.getMessage()), e);
            error("The system is currently unavailable.");
          }
        }
      };
      loginForm.add(loginButton);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the LoginPage", e);
    }
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

  /**
   * Returns the template web application.
   *
   * @return the template web application
   */
  private TemplateWebApplication getWebApplication()
  {
    return (TemplateWebApplication) getApplication();
  }
}
