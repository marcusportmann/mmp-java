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
import org.springframework.stereotype.Component;

/**
 * The <code>ApplicationConfiguration</code> class provides access to the Spring application
 * configuration retrieved from the <b>classpath:application.yml</b> file.
 *
 * @author Marcus Portmann
 */
@Component
@ConfigurationProperties("application")
@PropertySource("classpath:application.yml")
public class ApplicationConfiguration
{
  /**
   * The database configuration for the application.
   */
  private DatabaseConfiguration database;

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
   * Set the database configuration for the application.
   *
   * @param database the database configuration for the application
   */
  public void setDatabase(DatabaseConfiguration database)
  {
    this.database = database;
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
}
