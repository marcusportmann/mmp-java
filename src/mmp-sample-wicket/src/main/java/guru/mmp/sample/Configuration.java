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

package guru.mmp.sample;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.ApplicationConfiguration;
import guru.mmp.application.cache.CacheManagerConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * The <code>Configuration</code> class provides access to the Spring configuration for the
 * application.
 *
 * @author Marcus Portmann
 */
@Component
@Primary
public class Configuration extends ApplicationConfiguration
{
  /**
   * The in-memory distributed cache manager configuration.
   */
  private CacheManagerConfiguration cacheManager;

  /**
   * Returns the in-memory distributed cache manager configuration.
   *
   * @return in-memory distributed cache manager configuration
   */
  public CacheManagerConfiguration getCacheManager()
  {
    return cacheManager;
  }

  /**
   * Set the in-memory distributed cache manager configuration.
   *
   * @param cacheCluster the in-memory distributed cache manager configuration
   */
  public void setCacheManager(CacheManagerConfiguration cacheCluster)
  {
    this.cacheManager = cacheCluster;
  }
}
