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

package guru.mmp.application.web.template.components;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * The <code>Dialog</code> class provides a modal dialog box which appears over other content.
 * <p/>
 * The dialog can be opened or closed by straight JavaScript or by a Wicket AjaxRequestTarget.
 * <p/>
 * It can optionally be closed by clicking outside the dialog.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class Dialog extends WebMarkupContainer
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>Dialog</code>.
   *
   * @param id the non-null id of this component
   */
  public Dialog(String id)
  {
    super(id);

    setOutputMarkupId(true);
  }

  /**
   * Hide the dialog using Ajax.
   *
   * @param target the AJAX request target
   */
  public void hide(AjaxRequestTarget target)
  {
    target.appendJavaScript(getHideJavaScript());
  }

  /**
   * Show the dialog using Ajax.
   *
   * @param target the AJAX request target
   */
  public void show(AjaxRequestTarget target)
  {
    target.appendJavaScript(getShowJavaScript());
  }

  /**
   * Returns the JavaScript required to hide the dialog in the client browser.
   *
   * @return the JavaScript required to hide the dialog in the client browser
   */
  protected String getHideJavaScript()
  {
    return "$('#" + getMarkupId() + "').modal('hide')";
  }

  /**
   * Returns the JavaScript required to show the dialog in the client browser.
   *
   * @return the JavaScript required to show the dialog in the client browser
   */
  protected String getShowJavaScript()
  {
    if (!isEnabled())
    {
      return "";
    }

    return "$('#" + getMarkupId() + "').modal('show')";
  }
}
