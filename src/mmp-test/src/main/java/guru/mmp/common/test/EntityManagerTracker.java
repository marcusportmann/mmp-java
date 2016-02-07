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

package guru.mmp.common.test;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * The <code>EntityManagerTracker</code> class implements tracks the <code>EntityManager</code>
 * instances associated with the current thread.
 *
 * @author Marcus Portmann
 */
public class EntityManagerTracker
{
  private static final long serialVersionUID = 1000000;
  private static ThreadLocal<List<EntityManager>> activeEntityManagers =
      new ThreadLocal<List<EntityManager>>()
  {
    @Override
    protected List<EntityManager> initialValue()
    {
      return new ArrayList<>();
    }
  };

  /**
   * Returns the active <code>EntityManager</code>s associated with the current thread.
   *
   * @return the active <code>EntityManager</code>s associated with the current thread
   */
  public static List<EntityManager> getActiveEntityManagers()
  {
    return activeEntityManagers.get();
  }
}
