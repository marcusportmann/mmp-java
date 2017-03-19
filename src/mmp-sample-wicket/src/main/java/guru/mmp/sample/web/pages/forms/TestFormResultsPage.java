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

package guru.mmp.sample.web.pages.forms;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.template.pages.TemplateWebPage;
import guru.mmp.sample.model.TestData;
import org.apache.wicket.model.IModel;

/**
 * The <code>TestFormResultsPage</code> class implements the "Test Form Results"
 * page for the web application.
 *
 * @author Marcus Portmann
 */
public class TestFormResultsPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>TestFormResultsPage</code>.
   *
   * @param model the model
   */
  public TestFormResultsPage(IModel<TestData> model)
  {
    super("Test Form Results", "The test form results");
  }
}
