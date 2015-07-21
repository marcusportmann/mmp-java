/*
 * Copyright 2015 Marcus Portmann
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
    DataSource dataSource = null;

    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored) {}
    }

    if (dataSource != null)
    {
      return dataSource;
    }
    else
    {
      throw new RuntimeException("Failed to lookup the application data source using the"
          + " JNDI names (java:app/jdbc/ApplicationDataSource) and"
          + " (java:comp/env/jdbc/ApplicationDataSource)");
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
    String databaseSchema = null;

    try
    {
      databaseSchema = InitialContext.doLookup("java:app/env/ApplicationDatabaseSchema");
    }
    catch (Throwable ignored) {}

    if (databaseSchema == null)
    {
      try
      {
        databaseSchema = InitialContext.doLookup("java:comp/env/ApplicationDatabaseSchema");
      }
      catch (Throwable ignored) {}
    }

    if (databaseSchema != null)
    {
      return databaseSchema;
    }
    else
    {
      throw new RuntimeException("Failed to lookup the application database schema using the"
          + " JNDI names (java:app/env/ApplicationDatabaseSchema) and"
          + " (java:comp/env/ApplicationDatabaseSchema)");
    }
  }
}
