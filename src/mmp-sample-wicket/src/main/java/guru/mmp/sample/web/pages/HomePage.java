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

package guru.mmp.sample.web.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.AnonymousOnlyWebPage;
import guru.mmp.application.web.template.components.UserDirectoryChoiceRenderer;
import guru.mmp.application.web.template.pages.TemplateWebPage;
import guru.mmp.sample.model.Data;
import guru.mmp.sample.model.ISampleService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>HomePage</code> class implements the "Home"
 * page for the web application.
 *
 * @author Marcus Portmann
 */
@AnonymousOnlyWebPage
public class HomePage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

  private static final long serialVersionUID = 1000000;

  @Inject
  private ISampleService sampleService;

  /**
   * Constructs a new <code>HomePage</code>.
   */
  public HomePage()
  {
    super("Home");

    try
    {
      AjaxLink<Void> testLink = new AjaxLink<Void>("testLink")
      {
        @Override
        public void onClick(AjaxRequestTarget target)
        {
          try
          {
            for (Data data : sampleService.getData())
            {
              logger.info(data.toString());
            }
          }
          catch (Throwable e)
          {
            logger.error("Failed to retrieve the data", e);
          }

        }
      };

      add(testLink);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the HomePage", e);
    }
  }
}
