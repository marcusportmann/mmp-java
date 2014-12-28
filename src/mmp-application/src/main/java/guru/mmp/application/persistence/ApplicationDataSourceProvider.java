/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.persistence;

//~--- JDK imports ------------------------------------------------------------

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * The <code>ApplicationDataSourceProvider</code> class implements the application-scoped
 * application data source provider.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ApplicationDataSourceProvider
{
  private static final String APPLICATION_DATABASE_SCHEMA_JNDI_NAME =
    "java:app/env/ApplicationDatabaseSchema";
  private static final String APPLICATION_DATA_SOURCE_JNDI_NAME =
    "java:app/jdbc/ApplicationDataSource";

  /**
   * Returns the application data source.
   *
   * @return the application data source
   */
  @Produces
  @Named
  @ApplicationDataSource
  public DataSource getApplicationDataSource()
  {
    try
    {
      return (DataSource) InitialContext.doLookup(APPLICATION_DATA_SOURCE_JNDI_NAME);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to lookup the application data source ("
          + APPLICATION_DATA_SOURCE_JNDI_NAME + ")", e);
    }
  }

  /**
   * Returns the application database schema.
   *
   * @return the application database schema
   */
  @Produces
  @Named
  @ApplicationDatabaseSchema
  public String getApplicationDatabaseSchema()
  {
    try
    {
      return (String) InitialContext.doLookup(APPLICATION_DATABASE_SCHEMA_JNDI_NAME);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to lookup the application database schema ("
          + APPLICATION_DATABASE_SCHEMA_JNDI_NAME + ")", e);
    }
  }
}
