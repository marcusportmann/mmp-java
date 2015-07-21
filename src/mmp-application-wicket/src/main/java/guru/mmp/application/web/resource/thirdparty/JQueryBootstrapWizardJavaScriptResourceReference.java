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

package guru.mmp.application.web.resource.thirdparty;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>JQueryBootstrapWizardJavaScriptResourceReference</code> class implements the
 * JavaScript resource reference for the jQuery Bootstrap Wizard thirdparty resource bundled
 * with the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class JQueryBootstrapWizardJavaScriptResourceReference extends JavaScriptResourceReference
{
  private static final long serialVersionUID = 1000000;
  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM =
    JavaScriptHeaderItem.forReference(new JQueryBootstrapWizardJavaScriptResourceReference());
  private static final JQueryBootstrapWizardJavaScriptResourceReference INSTANCE =
    new JQueryBootstrapWizardJavaScriptResourceReference();

  private JQueryBootstrapWizardJavaScriptResourceReference()
  {
    super(JQueryBootstrapWizardJavaScriptResourceReference.class, Debug.inDebugMode()
        ? "jquery-bootstrap-wizard/jquery.bootstrap.wizard.js"
        : "jquery-bootstrap-wizard/jquery.bootstrap.wizard.min.js");
  }

  /**
   * Returns the single instance of the JavaScript resource reference for the jQuery Bootstrap
   * Wizard thirdparty resource bundled with the Web Application Template
   *
   * @return the single instance of the JavaScript resource reference for the jQuery Bootstrap
   *         Wizard thirdparty resource bundled with the Web Application Template
   */
  public static JQueryBootstrapWizardJavaScriptResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the JavaScript header item for the JavaScript resource reference.
   *
   * @return the JavaScript header item for the JavaScript resource reference
   */
  public static JavaScriptHeaderItem getJavaScriptHeaderItem()
  {
    return JAVA_SCRIPT_HEADER_ITEM;
  }

  /**
   * Returns the dependencies for the JavaScript resource reference.
   *
   * @return the dependencies for the JavaScript resource reference
   */
  @Override
  public Iterable<? extends HeaderItem> getDependencies()
  {
    List<HeaderItem> dependencies = new ArrayList<>();

    dependencies.add(JQueryJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(BootstrapJavaScriptResourceReference.getJavaScriptHeaderItem());
    dependencies.add(BootstrapCssResourceReference.getCssHeaderItem());

    return dependencies;
  }
}
