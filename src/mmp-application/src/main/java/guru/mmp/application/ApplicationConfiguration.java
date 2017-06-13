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
