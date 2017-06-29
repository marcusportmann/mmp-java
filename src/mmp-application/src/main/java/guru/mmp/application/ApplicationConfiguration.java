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

package guru.mmp.application;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationConfiguration</code> class provides access to the Spring application
 * configuration retrieved from the <b>classpath:application.yml</b> file.
 *
 * @author Marcus Portmann
 */
@ConfigurationProperties("application")
@PropertySource("classpath:application.yml")
public class ApplicationConfiguration
{
  /**
   * The database configuration for the application.
   */
  private DatabaseConfiguration database;

  /**
   * The mutual SSL configuration for the application.
   */
  private MutualSSLConfiguration mutualSSL;

  /**
   * The in-memory distributed cache cluster configuration.
   */
  private CacheClusterConfiguration cacheCluster;

  /**
   * Returns the in-memory distributed cache cluster configuration.
   *
   * @return in-memory distributed cache cluster configuration
   */
  public CacheClusterConfiguration getCacheCluster()
  {
    return cacheCluster;
  }

  /**
   * Returns the database configuration for the application.
   *
   * @return the database configuration for the application
   */
  public DatabaseConfiguration getDatabase()
  {
    return database;
  }

  /**
   * Returns the mutual SSL configuration for the application.
   *
   * @return the mutual SSL configuration for the application
   */
  public MutualSSLConfiguration getMutualSSL()
  {
    return mutualSSL;
  }

  /**
   * Returns whether mutual SSL has been enable and web services and RESTful web services should be
   * invoked securely.
   *
   * @return <code>true</code> if mutual SSL has been enable and web services and RESTful web
   *         services should be invoked securely or <code>false</code> otherwise
   */
  public boolean isMutualSSLEnabled()
  {
    return ((mutualSSL != null) && mutualSSL.getEnabled());
  }

  /**
   * Set the in-memory distributed cache cluster configuration.
   *
   * @param cacheCluster the in-memory distributed cache cluster configuration
   */
  public void setCacheCluster(CacheClusterConfiguration cacheCluster)
  {
    this.cacheCluster = cacheCluster;
  }

  /**
   * Set the database configuration for the application.
   *
   * @param database the database configuration for the application
   */
  public void setDatabase(DatabaseConfiguration database)
  {
    this.database = database;
  }

  /**
   * Set the mutual SSL configuration for the application.
   *
   * @param mutualSSL the mutual SSL configuration for the application
   */
  public void setMutualSSL(MutualSSLConfiguration mutualSSL)
  {
    this.mutualSSL = mutualSSL;
  }

  /**
   * The <code>CacheClusterConfiguration</code> class provides access to the distributed in-memory
   * cache cluster configuration.
   */
  public static class CacheClusterConfiguration
  {
    /**
     * The distributed in-memory caches.
     */
    private List<CacheConfiguration> caches;

    /**
     * Is the distributed in-memory cache cluster enabled?
     */
    private boolean enabled;

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
     * Returns the distributed in-memory caches.
     *
     * @return the distributed in-memory caches
     */
    public List<CacheConfiguration> getCaches()
    {
      return caches;
    }

    /**
     * Returns whether the distributed in-memory cache cluster is enabled.
     *
     * @return <code>true</code> if the distributed in-memory cache is enabled or <code>false</code>
     *         otherwise
     */
    public boolean getEnabled()
    {
      return enabled;
    }

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
     * Set the distributed in-memory caches.
     *
     * @param caches the distributed in-memory caches
     */
    public void setCaches(List<CacheConfiguration> caches)
    {
      this.caches = caches;
    }

    /**
     * Set whether the distributed in-memory cache cluster is enabled.
     *
     * @param enabled <code>true</code> if the distributed in-memory cache cluster is enabled or
     *                <code>false</code> otherwise
     */
    public void setEnabled(boolean enabled)
    {
      this.enabled = enabled;
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


  /**
   * The <code>DatabaseConfiguration</code> class provides access to the database configuration for
   * the application.
   */
  public static class DatabaseConfiguration
  {
    /**
     * The minimum size of the database connection pool used to connect to the application database.
     */
    private int minPoolSize;

    /**
     * The maximum size of the database connection pool used to connect to the application database.
     */
    private int maxPoolSize;

    /**
     * The fully qualified name of the data source class used to connect to the application
     * database.
     */
    private String dataSource;

    /**
     * The URL used to connect to the application database.
     */
    private String url;

    /**
     * Returns the fully qualified name of the data source class used to connect to the application
     * database.
     *
     * @return the fully qualified name of the data source class used to connect to the application
     *         database
     */
    public String getDataSource()
    {
      return dataSource;
    }

    /**
     * Returns the maximum size of the database connection pool used to connect to the application
     * database.
     *
     * @return the maximum size of the database connection pool used to connect to the application
     *         database
     */
    public int getMaxPoolSize()
    {
      return maxPoolSize;
    }

    /**
     * Returns the minimum size of the database connection pool used to connect to the application
     * database.
     *
     * @return the minimum size of the database connection pool used to connect to the application
     *         database
     */
    public int getMinPoolSize()
    {
      return minPoolSize;
    }

    /**
     * Returns the URL used to connect to the application database.
     *
     * @return the URL used to connect to the application database
     */
    public String getUrl()
    {
      return url;
    }

    /**
     * Set the fully qualified name of the data source class used to connect to the application
     * database.
     *
     * @param dataSource the fully qualified name of the data source class used to connect to the
     *                   application database
     */
    public void setDataSource(String dataSource)
    {
      this.dataSource = dataSource;
    }

    /**
     * Set the maximum size of the database connection pool used to connect to the application
     * database.
     *
     * @param maxPoolSize the maximum size of the database connection pool used to connect to the
     *                    application database
     */
    public void setMaxPoolSize(int maxPoolSize)
    {
      this.maxPoolSize = maxPoolSize;
    }

    /**
     * Set the minimum size of the database connection pool used to connect to the application
     * database.
     *
     * @param minPoolSize the minimum size of the database connection pool used to connect to the
     *                    application database
     */
    public void setMinPoolSize(int minPoolSize)
    {
      this.minPoolSize = minPoolSize;
    }

    /**
     * Set the URL used to connect to the application database.
     *
     * @param url the URL used to connect to the application database
     */
    public void setUrl(String url)
    {
      this.url = url;
    }
  }


  /**
   * The <code>KeyStoreConfiguration</code> class provides access to configuration for a key store
   * associated with the application.
   */
  public static class KeyStoreConfiguration
  {
    /**
     * The type of key store e.g. JKS, PKCS12, etc.
     */
    private String type;

    /**
     * The path to the key store.
     */
    private String path;

    /**
     * The alias for the certificate in the key store.
     */
    private String alias;

    /**
     * The password for the key store.
     */
    private String password;

    /**
     * Returns the alias for the certificate in the key store.
     *
     * @return the alias for the certificate in the key store
     */
    public String getAlias()
    {
      return alias;
    }

    /**
     * Returns the password for the key store.
     *
     * @return the password for the key store
     */
    public String getPassword()
    {
      return password;
    }

    /**
     * Returns the path to the key store.
     *
     * @return the path to the key store
     */
    public String getPath()
    {
      return path;
    }

    /**
     * Returns the type of key store e.g. JKS, PKCS12, etc.
     *
     * @return the type of key store e.g. JKS, PKCS12, etc
     */
    public String getType()
    {
      return type;
    }

    /**
     * Set the alias for the certificate in the key store.
     *
     * @param alias the alias for the certificate in the key store
     */
    public void setAlias(String alias)
    {
      this.alias = alias;
    }

    /**
     * Set the password for the key store.
     *
     * @param password the password for the key store
     */
    public void setPassword(String password)
    {
      this.password = password;
    }

    /**
     * Set the path to the key store.
     *
     * @param path the path to the key store
     */
    public void setPath(String path)
    {
      this.path = path;
    }

    /**
     * Set the type of key store e.g. JKS, PKCS12, etc.
     *
     * @param type the type of key store e.g. JKS, PKCS12, etc
     */
    public void setType(String type)
    {
      this.type = type;
    }
  }


  /**
   * The <code>MutualSSLConfiguration</code> class provides access to the mutual SSL configuration
   * for the application.
   */
  public static class MutualSSLConfiguration
  {
    /**
     * Should APIs be secured using mutual SSL.
     */
    private boolean secureAPIs = true;

    /**
     * Should web services be secured using mutual SSL.
     */
    private boolean secureWebServices = true;

    /**
     * The configuration for the mutual SSL key store for the application.
     */
    private KeyStoreConfiguration keyStore;

    /**
     * The configuration for the mutual SSL trust store for the application.
     */
    private TrustStoreConfiguration trustStore;

    /**
     * Should mutual SSL should be enabled for the application.
     */
    private boolean enabled;

    /**
     * Returns whether mutual SSL should be enabled for the application.
     *
     * @return <code>true</code> if mutual SSL should be enabled for the application or
     *         <code>false</code> otherwise
     */
    public boolean getEnabled()
    {
      return enabled;
    }

    /**
     * Returns the configuration for the mutual SSL key store for the application.
     *
     * @return the configuration for the mutual SSL key store for the application
     */
    public KeyStoreConfiguration getKeyStore()
    {
      return keyStore;
    }

    /**
     * Returns whether APIs should be secured using mutual SSL.
     *
     * @return <code>true</code> if APIs should be secured using mutual SSL or <code>false</code>
     *         otherwise
     */
    public boolean getSecureAPIs()
    {
      return secureAPIs;
    }

    /**
     * Returns whether web services should be secured using mutual SSL.
     *
     * @return <code>true</code> if web services should be secured using mutual SSL or
     *         <code>false</code> otherwise
     */
    public boolean getSecureWebServices()
    {
      return secureWebServices;
    }

    /**
     * Returns the configuration for the mutual SSL trust store for the application.
     *
     * @return the configuration for the mutual SSL trust store for the application
     */
    public TrustStoreConfiguration getTrustStore()
    {
      return trustStore;
    }

    /**
     * Set whether mutual SSL should be enabled for the application.
     *
     * @param enabled <code>true</code> if mutual SSL should be enabled for the application or
     *                <code>false</code> otherwise
     */
    public void setEnabled(boolean enabled)
    {
      this.enabled = enabled;
    }

    /**
     * Set the configuration for the mutual SSL key store for the application.
     *
     * @param keyStore the configuration for the mutual SSL key store for the application
     */
    public void setKeyStore(KeyStoreConfiguration keyStore)
    {
      this.keyStore = keyStore;
    }

    /**
     * Set whether APIs should be secured using mutual SSL.
     *
     * @param secureAPIs <code>true</code> if APIs should be secured using mutual SSL or
     *                   <code>false</code> otherwise
     */
    public void setSecureAPIs(boolean secureAPIs)
    {
      this.secureAPIs = secureAPIs;
    }

    /**
     * Set whether web services should be secured using mutual SSL.
     *
     * @param secureWebServices <code>true</code> if web services should be secured using mutual
     *                          SSL or <code>false</code> otherwise
     */

    public void setSecureWebServices(boolean secureWebServices)
    {
      this.secureWebServices = secureWebServices;
    }

    /**
     * Set the configuration for the mutual SSL trust store for the application.
     *
     * @param trustStore the configuration for the mutual SSL trust store for the application
     */
    public void setTrustStore(TrustStoreConfiguration trustStore)
    {
      this.trustStore = trustStore;
    }
  }


  /**
   * The <code>TrustStoreConfiguration</code> class provides access to configuration for a trust
   * store associated with the application.
   */
  public static class TrustStoreConfiguration
  {
    /**
     * The type of trust store e.g. JKS, PKCS12, etc.
     */
    private String type;

    /**
     * The path to the trust store.
     */
    private String path;

    /**
     * The password for the trust store.
     */
    private String password;

    /**
     * Returns the password for the trust store.
     *
     * @return the password for the trust store
     */
    public String getPassword()
    {
      return password;
    }

    /**
     * Returns the path to the trust store.
     *
     * @return the path to the trust store
     */
    public String getPath()
    {
      return path;
    }

    /**
     * Returns the type of trust store e.g. JKS, PKCS12, etc.
     *
     * @return the type of trust store e.g. JKS, PKCS12, etc
     */
    public String getType()
    {
      return type;
    }

    /**
     * Set the password for the trust store.
     *
     * @param password the password for the trust store
     */
    public void setPassword(String password)
    {
      this.password = password;
    }

    /**
     * Set the path to the trust store.
     *
     * @param path the path to the trust store
     */
    public void setPath(String path)
    {
      this.path = path;
    }

    /**
     * Set the type of trust store e.g. JKS, PKCS12, etc.
     *
     * @param type the type of trust store e.g. JKS, PKCS12, etc
     */
    public void setType(String type)
    {
      this.type = type;
    }
  }
}
