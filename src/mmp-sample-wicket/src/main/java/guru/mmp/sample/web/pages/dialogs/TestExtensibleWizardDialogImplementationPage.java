///*
// * Copyright 2016 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package guru.mmp.sample.web.pages.dialogs;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import guru.mmp.application.web.WebApplicationException;
//import guru.mmp.application.web.template.components.ExtensibleDialogButton;
//import guru.mmp.application.web.template.components.ExtensibleDialogImplementation;
//import guru.mmp.application.web.template.pages.TemplateExtensibleDialogWebPage;
//import org.apache.wicket.ajax.AjaxEventBehavior;
//import org.apache.wicket.ajax.AjaxRequestTarget;
//import org.apache.wicket.ajax.markup.html.AjaxLink;
//
//import java.util.ArrayList;
//import java.util.List;
//
////~--- JDK imports ------------------------------------------------------------
//
///**
// * The <code>TestExtensibleWizardDialogImplementationPage</code> class implements the
// * "Test Extensible Wizard Dialog Implementation" page for the web application.
// *
// * @author Marcus Portmann
// */
//public class TestExtensibleWizardDialogImplementationPage extends TemplateExtensibleDialogWebPage
//{
//  private static final long serialVersionUID = 1000000;
//
//  /**
//   * Constructs a new <code>TestExtensibleWizardDialogImplementationPage</code>.
//   */
//  public TestExtensibleWizardDialogImplementationPage()
//  {
//    super("Test Extensible Wizard Dialog Implementation", "The test extensible wizard dialog implementation");
//
//    try
//    {
//      AjaxLink showDialogLink = new AjaxLink("showDialogLink")
//      {
//        @Override
//        public void onClick(AjaxRequestTarget target)
//        {
//          getDialog().show(target, new TestExtensibleDialogImplementation());
//        }
//      };
//
//      add(showDialogLink);
//    }
//    catch (Throwable e)
//    {
//      throw new WebApplicationException(
//        "Failed to initialise the TestExtensibleWizardDialogImplementationPage", e);
//    }
//  }
//
//  private class TestExtensibleWizardDialogImplementation extends ExtensibleWizardDialogImplementation
//  {
//    /**
//     * Constructs a new <code>TestExtensibleWizardDialogImplementation</code>.
//     */
//    public TestExtensibleWizardDialogImplementation()
//    {
//      super("Test Extensible Wizard Dialog");
//    }
//
//
//  }
//}
