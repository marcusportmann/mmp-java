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

package guru.mmp.common.util;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ExpiryCacheThread</code> class implements the background thread responsible for
 * cleaning up expired entries in all the <code>ExpiryCache</code> instances.
 * <p/>
 * A new <code>ExpiryCache</code> instance will register itself with the
 * <code>ExpiryCacheThread</code> singleton.
 * <p/>
 * NOTE: Since a reference to every <code>ExpiryCache</code> instance is kept by the
 * <code>ExpiryCacheThread</code>, they will not be garbage collected unless explicitly removed.
 * <p/>
 * TODO: Implement more fine-grained locking.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ExpiryCacheThread extends Thread
{
  /**
   * The number of seconds the thread will "sleep" between expiry runs.
   */
  private static final int SLEEP_TIME = 300;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ExpiryCacheThread.class);
  private static ExpiryCacheThread singleton = null;
  private final List<ExpiryCache<?, ?>> expiryCaches = new ArrayList<>();

  /**
   * Private constructor to enforce singleton pattern.
   */
  private ExpiryCacheThread() {}

  /**
   * Singleton accessor.
   *
   * @return the ExpiryThread singleton
   */
  public static synchronized ExpiryCacheThread getExpiryCacheThread()
  {
    if (singleton == null)
    {
      singleton = new ExpiryCacheThread();
      singleton.setDaemon(true);
      singleton.start();
    }

    return singleton;
  }

  /**
   * Add the <code>ExpiryCache</code> to the list of caches monitored by the
   * <code>ExpiryCacheThread</code>.
   *
   * @param expiryCache the <code>ExpiryCache</code> to add
   */
  public void addCache(ExpiryCache<?, ?> expiryCache)
  {
    synchronized (expiryCaches)
    {
      expiryCaches.add(expiryCache);
    }
  }

  /**
   * Remove the <code>ExpiryCache</code> from the list of caches monitored by the
   * <code>ExpiryCacheThread</code>.
   *
   * @param expiryCache the <code>ExpiryCache</code> to remove
   */
  public void removeCache(ExpiryCache<?, ?> expiryCache)
  {
    synchronized (expiryCaches)
    {
      expiryCaches.remove(expiryCache);
    }
  }

  /**
   * @see Thread#run()
   */
  @Override
  public void run()
  {
    boolean isRunning = true;

    while (isRunning)
    {
      /*
       * NOTE: This is not the most effective way to lock the cache list.
       *
       * If this proves to be a problem we can lock the cache list and make a copy of it before
       * processing the cache entries in the copy.
       */
      synchronized (expiryCaches)
      {
        try
        {
          for (ExpiryCache<?, ?> expiryCache : expiryCaches)
          {
            expiryCache.expire();
          }
        }
        catch (Exception e)
        {
          logger.error("Failed to expire the entries in the expiry cache. This operation will be "
              + "retried: " + e.getMessage(), e);
        }
      }

      try
      {
        Thread.sleep(SLEEP_TIME * 1000L);
      }
      catch (Exception e)
      {
        // Do nothing
      }
    }
  }
}
