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

package guru.mmp.application.web.component;

//~--- non-JDK imports --------------------------------------------------------

import br.com.digilabs.jqplot.Chart;
import guru.mmp.application.web.behavior.JQPlotBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * The <code>JQPlotChart</code> class implements the Wicket markup container for a jqPlot chart.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class JQPlotChart extends WebMarkupContainer
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>JQPlotChart</code>.
   *
   * @param id    the non-null id of this component
   * @param chart the chart
   */
  public JQPlotChart(String id, Chart<?> chart)
  {
    super(id);
    setOutputMarkupId(true);
    add(new JQPlotBehavior(chart, getMarkupId()));
  }
}
