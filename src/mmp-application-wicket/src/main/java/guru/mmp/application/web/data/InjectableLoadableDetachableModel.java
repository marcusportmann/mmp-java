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

package guru.mmp.application.web.data;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * The <code>InjectableLoadableDetachableModel</code> class provides the
 * <code>LoadableDetachableModel</code> base class that provides support for Spring Dependency
 * Injection.
 *
 * @param <T>
 *
 * @author Marcus Portmann
 */
public abstract class InjectableLoadableDetachableModel<T> extends LoadableDetachableModel<T>
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>InjectableLoadableDetachableModel</code>.
   */
  public InjectableLoadableDetachableModel()
  {
    Injector.get().inject(this);
  }
}
