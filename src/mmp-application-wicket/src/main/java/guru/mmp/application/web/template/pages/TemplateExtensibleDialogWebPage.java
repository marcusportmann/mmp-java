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

import guru.mmp.application.web.template.components.ExtensibleDialog;
import guru.mmp.application.web.template.components.ExtensibleDialogImplementation;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The <code>TemplateWebPage</code> class is the base class that Wicket web page classes that make
 * use of an extensible popup dialog must be derived from in web applications that make use of the
 * Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class TemplateExtensibleDialogWebPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;
  private ExtensibleDialog dialog;

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param heading the page heading
   */
  protected TemplateExtensibleDialogWebPage(String heading)
  {
    super(heading);

    commonInit();
  }

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param headingModel    the model for the page heading
   * @param subHeadingModel the model for the sub-heading for the page
   */
  public TemplateExtensibleDialogWebPage(IModel<String> headingModel,
      IModel<String> subHeadingModel)
  {
    super(headingModel, subHeadingModel);

    commonInit();
  }

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param heading        the page heading
   * @param pageParameters the parameters for the page
   */
  protected TemplateExtensibleDialogWebPage(String heading, PageParameters pageParameters)
  {
    super(heading, pageParameters);

    commonInit();
  }

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param heading    the page heading
   * @param subHeading the sub-heading for the page
   */
  public TemplateExtensibleDialogWebPage(String heading, String subHeading)
  {
    super(heading, subHeading);

    commonInit();
  }

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param headingModel    the model for the page heading
   * @param subHeadingModel the model for the sub-heading for the page
   * @param model           the model for the page
   */
  public TemplateExtensibleDialogWebPage(IModel<String> headingModel,
      IModel<String> subHeadingModel, IModel<?> model)
  {
    super(headingModel, subHeadingModel, model);

    commonInit();
  }

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param headingModel    the model for the page heading
   * @param subHeadingModel the model for the sub-heading for the page
   * @param pageParameters  the model for the page
   */
  public TemplateExtensibleDialogWebPage(IModel<String> headingModel,
      IModel<String> subHeadingModel, PageParameters pageParameters)
  {
    super(headingModel, subHeadingModel, pageParameters);

    commonInit();
  }

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param heading    the page heading
   * @param subHeading the sub-heading for the page
   * @param model      the model for the page
   */
  public TemplateExtensibleDialogWebPage(String heading, String subHeading, IModel<?> model)
  {
    super(heading, subHeading, model);

    commonInit();
  }

  /**
   * Constructs a new <code>TemplateDialogWebPage</code>.
   *
   * @param heading        the page heading
   * @param subHeading     the sub-heading for the page
   * @param pageParameters the parameters for the page
   */
  protected TemplateExtensibleDialogWebPage(String heading, String subHeading,
      PageParameters pageParameters)
  {
    super(heading, subHeading, pageParameters);

    commonInit();
  }

  /**
   * Returns the extensible and reusable modal dialog associated with the page.
   *
   * @return the extensible and reusable modal dialog associated with the page
   */
  public ExtensibleDialog getDialog()
  {
    return dialog;
  }

  /**
   * Show the extensible and reusable dialog associated with the page using the specified
   * implementation.
   *
   * @param target         the AJAX request target
   * @param implementation the implementation for the extensible and reusable dialog
   */
  public void showDialog(AjaxRequestTarget target, ExtensibleDialogImplementation implementation)
  {
    dialog.show(target, implementation);
  }

  private void commonInit()
  {
    dialog = new ExtensibleDialog("dialog");

    add(dialog);
  }
}
