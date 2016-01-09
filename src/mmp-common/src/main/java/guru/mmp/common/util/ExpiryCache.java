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

package guru.mmp.common.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>ExpiryCache</code> class implements a cache of values indexed by keys.
 * <p/>
 * An entry is removed from the cache when it has not been accessed for a specific period of time.
 * <p/>
 * NOTE: Please see the <code>ExpiryCacheThread</code> class for more information on the memory
 * management of <code>ExpiryCache</code> instances.
 * <p/>
 * TODO: Implement expiry when the cache reaches a certain size.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ExpiryCache<K, V>
{
  private ConcurrentHashMap<K, ExpiryCacheEntry<V>> entries = null;

  private int expiryPeriod = -1;

  /**
   * Default Constructor.
   * <p/>
   * Cache entries will NOT expire.
   */
  public ExpiryCache()
  {
    this.expiryPeriod = -1;

    this.entries = new ConcurrentHashMap<>();
  }

  /**
   * Constructor.
   *
   * @param expiryPeriod the number of seconds since a cache entry was last accessed after which
   *                     it will be expired
   */
  public ExpiryCache(int expiryPeriod)
  {
    this.expiryPeriod = expiryPeriod;

    this.entries = new ConcurrentHashMap<>();

    ExpiryCacheThread.getExpiryCacheThread().addCache(this);
  }

  /**
   * Remove all entries from the cache.
   */
  public void clear()
  {
    entries.clear();
  }

  /**
   * Remove any expired entries in the cache who were not accessed in the default expiry interval
   * for this cache.
   *
   * @return the number of cache entries expired
   */
  public int expire()
  {
    return expire(expiryPeriod);
  }

  /**
   * Remove any expired entries in the cache who were not accessed in the specified expiry interval.
   *
   * @param expiryPeriod any cache entries that have not been accessed in the number of seconds
   *                     specified by this parameter will be expired
   *
   * @return the number of cache entries expired
   */
  public int expire(int expiryPeriod)
  {
    // Do not expire if expiry period not set
    if (expiryPeriod == -1)
    {
      return 0;
    }

    ExpiryCacheEntry<V> entry;
    Map.Entry<K, ExpiryCacheEntry<V>> each;
    long now = System.currentTimeMillis();
    int count = 0;

    for (Map.Entry<K, ExpiryCacheEntry<V>> objectExpiryCacheEntryEntry : entries.entrySet())
    {
      each = objectExpiryCacheEntryEntry;
      entry = each.getValue();

      if (entry != null)
      {
        if ((now - entry.getTimestamp()) >= (expiryPeriod * 1000L))
        {
          count++;
          entries.remove(each.getKey());
        }
      }
    }

    return count;
  }

  /**
   * Returns the cached value with the specified key or <code>null</code> if the value could not be
   * found.
   * <p/>
   * The cache entry itself is "touched" so it is allowed to stay in the cache a little longer
   * (expiryPeriod).
   * <p/>
   * This method is "null friendly" specifying a <code>null</code> key will return a
   * <code>null</code> value.
   *
   * @param key the key identifying the cached value
   *
   * @return the cached value
   */
  public V get(K key)
  {
    if (key == null)
    {
      return null;
    }

    ExpiryCacheEntry<V> entry = entries.get(key);

    if (entry != null)
    {
      entry.touch();

      return entry.getValue();
    }

    return null;
  }

  /**
   * Retrieve the keys for all the entries in the cache.
   *
   * @return an Enumeration of the keys for all the entries in the cache
   */
  public Enumeration<K> keys()
  {
    return entries.keys();
  }

  /**
   * Insert a new entry into the cache with the specified key and value.
   * <p/>
   * This method is "null friendly" specifying a <code>null</code> key or <code>null</code> value
   * will result in no action being taken.
   *
   * @param key   the key uniquely identifying the entry
   * @param value the value of the new cache entry
   *
   * @return the value of the new cache entry
   */
  public V put(K key, V value)
  {
    if ((key != null) && (value != null))
    {
      entries.put(key, new ExpiryCacheEntry<>(value));

      return value;
    }
    else
    {
      return null;
    }
  }

  /**
   * Remove an entry from the cache.
   * <p/>
   * This method is "null friendly" specifying a <code>null</code> key will result in no action
   * being taken.
   *
   * @param key the key uniquely identifying the cache entry
   *
   * @return the value of the removed cache entry
   */
  public V remove(K key)
  {
    if (key != null)
    {
      ExpiryCacheEntry<V> entry = entries.remove(key);

      if (entry != null)
      {
        return entry.getValue();
      }
      else
      {
        return null;
      }
    }
    else
    {
      return null;
    }
  }

  /**
   * Retrieve the number of entries in the expiry cache.
   *
   * @return the number of entries in the expiry cache
   */
  public int size()
  {
    return entries.size();
  }

  /**
   * The Entry class wraps actual values that are stored in the Cache in order to associate the
   * value with a timestamp indicating when it was last accessed.
   *
   * @param <T> the type of cached value
   */
  private class ExpiryCacheEntry<T>
  {
    /**
     * the EPOCH timestamp in milliseconds giving the time the Entry was last accessed
     */
    private long timestamp;

    /**
     * the value associated with the cache entry
     */
    private T value;

    /**
     * Constructor.
     *
     * @param value the value associated with the cache entry
     */
    public ExpiryCacheEntry(T value)
    {
      this.timestamp = System.currentTimeMillis();
      this.value = value;
    }

    /**
     * Get the EPOCH timestamp in milliseconds giving the time the cache entry was last accessed
     *
     * @return the EPOCH timestamp in milliseconds giving the time the cache entry was last accessed
     */
    public long getTimestamp()
    {
      return timestamp;
    }

    /**
     * Retrieve the value associated with this cache entry.
     *
     * @return the value associated with this cache entry
     */
    public T getValue()
    {
      return value;
    }

    /**
     * Update the EPOCH timestamp in milliseconds giving the time the cache entry was last accessed.
     */
    public void touch()
    {
      timestamp = System.currentTimeMillis();
    }
  }
}
