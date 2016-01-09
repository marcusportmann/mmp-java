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

package guru.mmp.application.web.behavior;

import br.com.digilabs.jqplot.Chart;
import br.com.digilabs.jqplot.JqPlotUtils;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.resource.jqplot.JQPlotCssResourceReference;
import guru.mmp.application.web.resource.jqplot.JQPlotJavaScriptResourceReference;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.List;

/**
 * The <code>JQPlotBehavior</code> class implements the Wicket behavior that enables jqPlot
 * for a control that is assigned the behavior on page load.
 *
 * @author Marcus Portmann
 */
public class JQPlotBehavior
  extends Behavior
{
  private static final long serialVersionUID = 1000000;

  private Chart<?> chart;

  private String divId;

  private List<String> resources;

  /**
   * Constructs a new <code>JQPlotBehavior</code>.
   *
   * @param chart the chart the behaviour is associated with
   * @param divId the ID of the div for the chart
   */
  public JQPlotBehavior(Chart<?> chart, String divId)
  {
    this.chart = chart;
    this.divId = divId;
    this.resources = JqPlotUtils.retriveJavaScriptResources(chart);
  }

  /**
   * Render to the web response whatever the component wants to contribute to the head section.
   *
   * @param component the component
   * @param response  the header response
   */
  @Override
  public void renderHead(Component component, IHeaderResponse response)
  {
    try
    {
      super.renderHead(component, response);

      response.render(JavaScriptHeaderItem.forReference(JQPlotJavaScriptResourceReference.get()));
      response.render(JavaScriptHeaderItem.forReference(JQPlotCssResourceReference.get()));

      for (String resource : resources)
      {
        response.render(JavaScriptHeaderItem.forReference(
          new JavaScriptResourceReference(JQPlotJavaScriptResourceReference.class, resource)));
      }

      String json = JqPlotUtils.createJquery(chart, divId);

      response.render(JavaScriptHeaderItem.forScript(json, null));
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
        "Failed the add the jqPlot JavaScript, CSS and JSON header items to the response", e);
    }
  }
}
