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

package guru.mmp.application.web.resources.jqplot;

import guru.mmp.application.Debug;
import org.apache.wicket.Application;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>JQPlotJavaScriptResourceReference</code> class implements the resource reference for
 * the jqPlot JavaScript library bundled with the web application library.
 *
 * @author Marcus Portmann
 */
public class JQPlotJavaScriptResourceReference
  extends JavaScriptResourceReference
{
  private static final JQPlotJavaScriptResourceReference INSTANCE = new
    JQPlotJavaScriptResourceReference();

  private static final long serialVersionUID = 1000000;

  /**
   * Returns the single instance of the resource reference for the jqPlot JavaScript library
   * bundled with the web application library.
   *
   * @return the single instance of the resource reference for the jqPlot JavaScript library
   * bundled with the web application library
   */
  public static JQPlotJavaScriptResourceReference get()
  {
    return INSTANCE;
  }

  private JQPlotJavaScriptResourceReference()
  {
    super(JQPlotJavaScriptResourceReference.class,
      Debug.inDebugMode() ? "jquery.jqplot.less" : "jquery.jqplot.min.less");
  }

  /**
   * Returns the dependencies for the resource reference.
   *
   * @return the dependencies for the resource reference
   */
  @Override
  public List<HeaderItem> getDependencies()
  {
    List<HeaderItem> dependencies = new ArrayList<>();

    for (HeaderItem headerItem : super.getDependencies())
    {
      dependencies.add(headerItem);
    }

    dependencies.add(JavaScriptHeaderItem.forReference(
      Application.get().getJavaScriptLibrarySettings().getJQueryReference()));

    return dependencies;
  }
}
