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

package guru.mmp.sample.web;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.security.context.ApplicationSecurityContext;
import guru.mmp.common.security.context.ServiceSecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.sql.DataSource;

/**
 * The <code>SampleApplicationListener</code> class initialises the sample web application.
 *
 * @author Marcus Portmann
 */
public class SampleApplicationListener
  implements ServletContextListener
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleApplicationListener.class);

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>SampleApplicationListener</code>.
   */
  public SampleApplicationListener() {}

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
    // Initialise the sample database tables if required
    initSampleDatabaseTables();

    // Initialise the sample data
    initSampleData();
  }

  /**
   * Initialise the sample data.
   */
  private void initSampleData()
  {
    try
    {
      byte[] sampleReportDefinitionData = getClasspathResource("guru/mmp/sample/report/SampleReport.jasper");

      ReportDefinition sampleReportDefinition = new ReportDefinition("2a4b74e8-7f03-416f-b058-b35bb06944ef", "MMP", "Sample Report", sampleReportDefinitionData, new Date(), "Administrator", null, null);

      if (!reportingService.reportDefinitionExists(sampleReportDefinition.getId()))
      {
        reportingService.saveReportDefinition(sampleReportDefinition, "Administrator");
        logger.info("Saved the \"Sample Report\" report definition");
      }





    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the sample data", e);
    }
  }

  /**
   * Initialise the sample database tables if required.
   */
  private void initSampleDatabaseTables()
  {
    DataSource dataSource;

    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the sample database tables:"
        + "Failed to retrieve the application data source using the JNDI lookup"
        + " (java:app/jdbc/ApplicationDataSource)", e);
    }

    if (dataSource == null)
    {
      throw new WebApplicationException("Failed to initialise the sample database tables:"
        + "Failed to retrieve the application data source using the JNDI lookup"
        + " (java:app/jdbc/ApplicationDataSource)");
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

          logger.info("The sample database tables will not be populated for the database type ("
            + metaData.getDatabaseProductName() + ")");

          return;
      }

      // Create and populate the database tables if required
      if (!DAOUtil.tableExists(connection, null, "SAMPLE", "DATA"))
      {
        logger.info("Creating and populating the sample database tables");

        String resourcePath = "/guru/mmp/sample/persistence/Sample" + databaseFileSuffix
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
      throw new WebApplicationException("Failed to initialise the sample database tables", e);
    }
    finally
    {
      DAOUtil.close(connection);
    }
  }

  private byte[] getClasspathResource(String path)
  {
    InputStream is = null;

    try
    {
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[4096];
      int numberOfBytesRead;

      while ((numberOfBytesRead = is.read(buffer)) != -1)
      {
        baos.write(buffer, 0, numberOfBytesRead);
      }

      return baos.toByteArray();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to read the classpath resource (" + path + ")", e);
    }
    finally
    {
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to close the input stream for the classpath resource (" + path
          + ")", e);
      }
    }
  }
}
