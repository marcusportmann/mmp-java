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

package guru.mmp.application.web.resource.jqplot;

import guru.mmp.application.Debug;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * The <code>JQPlotCssResourceReference</code> class implements the resource reference for
 * the jqPlot CSS file bundled with the web application library.
 *
 * @author Marcus Portmann
 */
public class JQPlotCssResourceReference
  extends JavaScriptResourceReference
{
  private static final JQPlotCssResourceReference INSTANCE = new JQPlotCssResourceReference();

  private static final long serialVersionUID = 1000000;

  /**
   * Returns the single instance of the resource reference for the jqPlot JavaScript library
   * bundled with the web application library.
   *
   * @return the single instance of the resource reference for the jqPlot JavaScript library
   * bundled with the web application library
   */
  public static JQPlotCssResourceReference get()
  {
    return INSTANCE;
  }

  private JQPlotCssResourceReference()
  {
    super(JQPlotCssResourceReference.class,
      Debug.inDebugMode() ? "jquery.jqplot.css" : "jquery.jqplot.min.css");
  }
}
