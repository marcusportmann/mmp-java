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

package guru.mmp.application.web.data;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * The <code>InjectableDataProvider</code> class provides the <code>DataProvider</code> base class
 * that provides support for container-based dependency injection (CDI).
 *
 * @author Marcus Portmann
 *
 * @param <T>
 */
public abstract class InjectableDataProvider<T>
  implements IDataProvider<T>
{
  /**
   * Constructs a new <code>InjectableDataProvider</code>.
   */
  public InjectableDataProvider()
  {
    guru.mmp.application.web.WebApplication webApplication =
      guru.mmp.application.web.WebApplication.class.cast(WebApplication.get());

    webApplication.getWebApplicationInjector().inject(this);
  }
}
