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

package guru.mmp.application.web;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.security.context.ApplicationSecurityContext;

import guru.mmp.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.util.List;

import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.sql.DataSource;

/**
 * The <code>WebApplicationListener</code> class initialises the context of a web application that
 * makes use of the web application library.
 *
 * @author Marcus Portmann
 */
public class WebApplicationListener
  implements ServletContextListener
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(WebApplicationListener.class);

  /**
   * Constructs a new <code>WebApplicationListener</code>.
   */
  public WebApplicationListener() {}

  /**
   * Notification that the servlet context is about to be shut down.
   *
   * @param event the <code>ServletContextEvent</code> instance containing the event details
   */
  public void contextDestroyed(ServletContextEvent event) {}

  /**
   * Notification that the web application is ready to process requests.
   *
   * @param event the <code>ServletContextEvent</code> instance containing the event details
   */
  public void contextInitialized(ServletContextEvent event)
  {
    ServletContext context = event.getServletContext();

    // Retrieve the Wicket configuration
    String configuration = context.getInitParameter("configuration");

    if ((configuration != null) && (configuration.equalsIgnoreCase("development")))
    {
      logger.info("Initialising the WebApplicationListener in DEVELOPMENT mode");
    }

    // Initialise the default database tables if required
    initDefaultDatabaseTables();

    String applicationName = null;

    try
    {
      applicationName = InitialContext.doLookup("java:app/AppName");
    }
    catch (Throwable ignored) {}

    if (applicationName == null)
    {
      try
      {
        applicationName = InitialContext.doLookup("java:comp/env/ApplicationName");
      }
      catch (Throwable ignored) {}
    }

    if (StringUtil.isNullOrEmpty(applicationName))
    {
      logger.warn("Failed to retrieve the application name from JNDI using the names ("
        + "java:app/AppName) and (java:comp/env/ApplicationName)."
        + " The application security context will not be initialised");
    }
    else
    {
      // Initialise the application's security context
      if (ApplicationSecurityContext.getContext().exists(applicationName))
      {
        logger.info("Initialising the application security context using the configuration file ("
          + applicationName + ".ApplicationSecurity)");

        try
        {
          ApplicationSecurityContext.getContext().init(applicationName);
        }
        catch (Throwable e)
        {
          throw new WebApplicationException(
            "Failed to initialise the application security context using the"
              + " configuration file (META-INF/" + applicationName + ".ApplicationSecurity): "
              + e.getMessage(), e);
        }
      }
      else
      {
        logger.info("The configuration file ("
          + applicationName + ".ApplicationSecurity) could not found."
          + " The application security context will not be initialised");
      }
    }
  }

  /**
   * Initialise the default database tables if required.
   */
  private void initDefaultDatabaseTables()
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

    if (dataSource == null)
    {
      throw new WebApplicationException("Failed to initialise the default database tables:"
          + "Failed to retrieve the application data source using the JNDI names"
          + " (java:app/jdbc/ApplicationDataSource) and"
          + " (java:comp/env/jdbc/ApplicationDataSource)");
    }

    Connection connection = null;

    try
    {
      connection = dataSource.getConnection();

      DatabaseMetaData metaData = connection.getMetaData();

      logger.info("Connected to the " + metaData.getDatabaseProductName()
          + " application database with version " + metaData.getDatabaseProductVersion());

      // Determine the suffix for the SQL files containing the database DDL
      String databaseFileSuffix;

      switch (metaData.getDatabaseProductName())
      {
        case "H2":

          databaseFileSuffix = "H2";

          break;

        default:

          logger.info("The default database tables will not be populated for the database type ("
              + metaData.getDatabaseProductName() + ")");

          return;
      }

      // Create and populate the database tables if required
      if (!DAOUtil.tableExists(connection, null,
          DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA, "REGISTRY"))
      {
        logger.info("Creating and populating the default database tables");

        String resourcePath = "/guru/mmp/application/persistence/Application" + databaseFileSuffix
          + ".sql";
        int numberOfStatementsExecuted = 0;
        int numberOfFailedStatements = 0;
        List<String> sqlStatements;

        try
        {
          sqlStatements = DAOUtil.loadSQL(resourcePath);
        }
        catch (Throwable e)
        {
          throw new WebApplicationException(
              "Failed to load the SQL statements from the resource file (" + resourcePath + ")", e);
        }

        for (String sqlStatement : sqlStatements)
        {
          try
          {
            DAOUtil.executeStatement(connection, sqlStatement);
            numberOfStatementsExecuted++;
          }
          catch (Throwable e)
          {
            logger.error("Failed to execute the SQL statement: " + sqlStatement, e);
            numberOfFailedStatements++;
          }
        }

        if (numberOfStatementsExecuted != sqlStatements.size())
        {
          throw new WebApplicationException("Failed to execute " + numberOfFailedStatements
              + " SQL statement(s) in the resource file (" + resourcePath + ")");
        }
      }
    }
    catch (Throwable e)
    {
      logger.error("Failed to initialise the default database tables", e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }
}
