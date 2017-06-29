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

package guru.mmp.application.cache;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>CacheManagerConfiguration</code> class provides access to the distributed in-memory
 * cache manager configuration.
 *
 * @author Marcus Portmann
 */
public class CacheManagerConfiguration
{
  /**
   * The distributed in-memory caches.
   */
  private List<CacheConfiguration> caches;

  /**
   * The distributed in-memory cache cluster configuration.
   */
  private CacheClusterConfiguration cluster;

  /**
   * Returns the distributed in-memory caches.
   *
   * @return the distributed in-memory caches
   */
  public List<CacheConfiguration> getCaches()
  {
    return caches;
  }

  /**
   * Returns the distributed in-memory cache cluster configuration.
   *
   * @return the distributed in-memory cache cluster configuration
   */
  public CacheClusterConfiguration getCluster()
  {
    return cluster;
  }

  /**
   * Set the distributed in-memory caches.
   *
   * @param caches the distributed in-memory caches
   */
  public void setCaches(List<CacheConfiguration> caches)
  {
    this.caches = caches;
  }

  /**
   * Set the distributed in-memory cache cluster configuration.
   *
   * @param cluster the distributed in-memory cache cluster configuration
   */
  public void setCluster(CacheClusterConfiguration cluster)
  {
    this.cluster = cluster;
  }

  /**
   * The <code>CacheClusterConfiguration</code> class provides access to the distributed in-memory
   * cache cluster  configuration.
   */
  public static class CacheClusterConfiguration
  {
    /**
     * The port for the distributed in-memory cache cluster.
     */
    private int port;

    /**
     * The password used to connect to the distributed in-memory cache cluster.
     */
    private String password;

    /**
     * The comma-delimited list of IP addresses or hostnames for the members of the distributed
     * in-memory cache cluster.
     */
    private String members;

    /**
     * The name of the distributed in-memory cache cluster.
     */
    private String name;

    /**
     * Returns the comma-delimited list of IP addresses or hostnames for the members of the
     * distributed in-memory cache cluster.
     *
     * @return the comma-delimited list of IP addresses or hostnames for the members of the
     *         distributed in-memory cache cluster
     */
    public String getMembers()
    {
      return members;
    }

    /**
     * Returns the name of the distributed in-memory cache cluster.
     *
     * @return the name of the distributed in-memory cache cluster
     */
    public String getName()
    {
      return name;
    }

    /**
     * Returns the password used to connect to the distributed in-memory cache cluster.
     *
     * @return the password used to connect to the distributed in-memory cache cluster
     */
    public String getPassword()
    {
      return password;
    }

    /**
     * Returns the port for the distributed in-memory cache cluster.
     *
     * @return the port for the distributed in-memory cache cluster
     */
    public int getPort()
    {
      return port;
    }

    /**
     * Set the comma-delimited list of IP addresses or hostnames for the members of the distributed
     * in-memory cache cluster.
     *
     * @param members the comma-delimited list of IP addresses or hostnames for the members of the
     *                distributed in-memory cache cluster
     */
    public void setMembers(String members)
    {
      this.members = members;
    }

    /**
     * Set the name of the distributed in-memory cache cluster.
     *
     * @param name the name of the distributed in-memory cache cluster
     */
    public void setName(String name)
    {
      this.name = name;
    }

    /**
     * Set the password used to connect to the distributed in-memory cache cluster.
     *
     * @param password the password used to connect to the distributed in-memory cache cluster
     */
    public void setPassword(String password)
    {
      this.password = password;
    }

    /**
     * Set the port for the distributed in-memory cache cluster.
     *
     * @param port the port for the distributed in-memory cache cluster
     */
    public void setPort(int port)
    {
      this.port = port;
    }
  }


  /**
   * The <code>CacheConfiguration</code> class provides access to the distributed in-memory cache
   * configuration.
   */
  public static class CacheConfiguration
  {
    /**
     * The name of the distributed in-memory cache.
     */
    private String name;

    /**
     * The maximum size policy for the distributed in-memory cache.
     */
    private String maxSizePolicy;

    /**
     * The maximum size for the distributed in-memory cache.
     */
    private int maxSize;

    /**
     * The in-memory format for the distributed in-memory cache.
     */
    private String inMemoryFormat;

    /**
     * The eviction policy for the distributed in-memory cache.
     */
    private String evictionPolicy;

    /**
     * Are statistics enabled for the distributed in-memory cache?
     */
    boolean statisticsEnabled;

    /**
     * The maximum idle seconds for the distributed in-memory cache.
     */
    private int maxIdleSeconds;

    /**
     * The number of synchronous backups for the distributed in-memory cache.
     */
    private int backupCount;

    /**
     * The number of asynchronous backups for the distributed in-memory cache.
     */
    private int asyncBackupCount;

    /**
     * Is read-backup-data enabled for the distributed in-memory cache.
     */
    private boolean readBackupData;

    /**
     * Returns the number of asynchronous backups for the distributed in-memory cache.
     *
     * @return the number of asynchronous backups for the distributed in-memory cache
     */
    public int getAsyncBackupCount()
    {
      return asyncBackupCount;
    }

    /**
     * Returns the number of synchronous backups for the distributed in-memory cache.
     *
     * @return the number of synchronous backups for the distributed in-memory cache
     */
    public int getBackupCount()
    {
      return backupCount;
    }

    /**
     * Returns the eviction policy for the distributed in-memory cache.
     *
     * @return the eviction policy for the distributed in-memory cache
     */
    public String getEvictionPolicy()
    {
      return evictionPolicy;
    }

    /**
     * Returns the in-memory format for the distributed in-memory cache.
     *
     * @return the in-memory format for the distributed in-memory cache
     */
    public String getInMemoryFormat()
    {
      return inMemoryFormat;
    }

    /**
     * Returns the maximum idle seconds for the distributed in-memory cache.
     *
     * @return the maximum idle seconds for the distributed in-memory cache
     */
    public int getMaxIdleSeconds()
    {
      return maxIdleSeconds;
    }

    /**
     * Returns the maximum size for the distributed in-memory cache.
     *
     * @return the maximum size for the distributed in-memory cache
     */
    public int getMaxSize()
    {
      return maxSize;
    }

    /**
     * Returns the maximum size policy for the distributed in-memory cache.
     *
     * @return the maximum size policy for the distributed in-memory cache
     */
    public String getMaxSizePolicy()
    {
      return maxSizePolicy;
    }

    /**
     * Returns the name of the distributed in-memory cache.
     *
     * @return the name of the distributed in-memory cache
     */
    public String getName()
    {
      return name;
    }

    /**
     * Returns whether read-backup-data is enabled for the distributed in-memory cache.
     *
     * @return <code>true</code> if read-backup-data is enabled for the distributed in-memory
     *         cache or <code>false</code> otherwise
     */
    public boolean getReadBackupData()
    {
      return readBackupData;
    }

    /**
     * Returns whether statistics are enabled for the distributed in-memory cache?
     *
     * @return <code>true</code> if statistics are enabled for the distributed in-memory cache
     * or <code>false</code> otherwise
     */
    public boolean getStatisticsEnabled()
    {
      return statisticsEnabled;
    }

    /**
     * Set the number of asynchronous backups for the distributed in-memory cache.
     *
     * @param asyncBackupCount the number of asynchronous backups for the distributed in-memory
     *                         cache
     */
    public void setAsyncBackupCount(int asyncBackupCount)
    {
      this.asyncBackupCount = asyncBackupCount;
    }

    /**
     * Set the number of synchronous backups for the distributed in-memory cache.
     *
     * @param backupCount the number of synchronous backups for the distributed in-memory cache
     */
    public void setBackupCount(int backupCount)
    {
      this.backupCount = backupCount;
    }

    /**
     * Set the eviction policy for the distributed in-memory cache.
     *
     * @param evictionPolicy the eviction policy for the distributed in-memory cache
     */
    public void setEvictionPolicy(String evictionPolicy)
    {
      this.evictionPolicy = evictionPolicy;
    }

    /**
     * Set the in-memory format for the distributed in-memory cache.
     *
     * @param inMemoryFormat the in-memory format for the distributed in-memory cache
     */
    public void setInMemoryFormat(String inMemoryFormat)
    {
      this.inMemoryFormat = inMemoryFormat;
    }

    /**
     * Set the maximum idle seconds for the distributed in-memory cache.
     *
     * @param maxIdleSeconds the maximum idle seconds for the distributed in-memory cache
     */
    public void setMaxIdleSeconds(int maxIdleSeconds)
    {
      this.maxIdleSeconds = maxIdleSeconds;
    }

    /**
     * Set the maximum size for the distributed in-memory cache.
     *
     * @param maxSize the maximum size for the distributed in-memory cache
     */
    public void setMaxSize(int maxSize)
    {
      this.maxSize = maxSize;
    }

    /**
     * Set the maximum size policy for the distributed in-memory cache.
     *
     * @param maxSizePolicy the maximum size policy for the distributed in-memory cache
     */
    public void setMaxSizePolicy(String maxSizePolicy)
    {
      this.maxSizePolicy = maxSizePolicy;
    }

    /**
     * Set the name of the distributed in-memory cache.
     *
     * @param name the name of the distributed in-memory cache
     */
    public void setName(String name)
    {
      this.name = name;
    }

    /**
     * Set whether read-backup-data is enabled for the distributed in-memory cache.
     *
     * @param readBackupData <code>true</code> if read-backup-data enabled for the distributed
     *                       in-memory cache or <code>false</code> otherwise
     */
    public void setReadBackupData(boolean readBackupData)
    {
      this.readBackupData = readBackupData;
    }

    /**
     * Set whether statistics are enabled for the distributed in-memory cache.
     *
     * @param statisticsEnabled <code>true</code> if statistics are enabled for the distributed
     *                         in-memory cache or <code>false</code> otherwise
     */
    public void setStatisticsEnabled(boolean statisticsEnabled)
    {
      this.statisticsEnabled = statisticsEnabled;
    }
  }
}
