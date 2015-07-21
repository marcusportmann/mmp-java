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

package guru.mmp.application.web;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.IRequestCycleProvider;
import org.apache.wicket.request.UrlRenderer;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.cycle.RequestCycleContext;

/**
 * The <code>WebSphereAbsoluteUrlRequestCycleProvider</code> class implements a {@link RequestCycle}
 * provider which overrides the {@link UrlRenderer} to generate absolute links that are compatible
 * with WebSphere.
 *
 * @see {@link WebSphereAbsoluteUrlRenderer}
 * @see {@link RequestCycle#getUrlRenderer()}
 *
 * @author Marcus Portmann
 */
public class WebSphereAbsoluteUrlRequestCycleProvider
  implements IRequestCycleProvider
{
  /**
   * Constructs a new <code>WebSphereAbsoluteUrlRequestCycleProvider</code>.
   */
  public WebSphereAbsoluteUrlRequestCycleProvider() {}

  /**
   * Returns the request cycle.
   *
   * @param context the request cycle context
   *
   * @return the request cycle
   */
  @Override
  public RequestCycle get(RequestCycleContext context)
  {
    return new RequestCycle(context)
    {
      @Override
      protected UrlRenderer newUrlRenderer()
      {
        return new WebSphereAbsoluteUrlRenderer(getRequest());
      }
    };
  }
}
