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

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;

/**
 * The <code>InjectableSortableDataProvider</code> class provides the
 * <code>SortableDataProvider</code> base class that provides support for Spring Dependency
 * Injection.
 *
 * @param <T>
 * @param <S>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class InjectableSortableDataProvider<T, S> extends SortableDataProvider<T, S>
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>InjectableSortableDataProvider</code>.
   */
  public InjectableSortableDataProvider()
  {
    Injector.get().inject(this);
  }
}
