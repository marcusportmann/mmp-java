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

package guru.mmp.application.process;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.process.bpmn.Parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>ProcessService</code> class provides the Process Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ProcessService
  implements IProcessService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

  /** The data source used to provide connections to the database. */
  private DataSource dataSource;

  /* The name of the Process Service instance. */
  private String instanceName;

  /* The DAO providing persistence capabilities for the process infrastructure. */
  @Inject
  private IProcessDAO processDAO;

  /**
   * Constructs a new <code>ProcessService</code>.
   */
  public ProcessService() {}

  /**
   * Delete the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @throws ProcessServiceException
   */
  public void deleteProcessDefinition(String id)
    throws ProcessServiceException
  {
    try
    {
      processDAO.deleteProcessDefinition(id);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to delete the process definition with ID (" + id
          + ")", e);
    }
  }

  /**
   * Returns the number of process definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of process definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws ProcessServiceException
   */
  public int getNumberOfProcessDefinitionsForOrganisation(String organisation)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getNumberOfProcessDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the number of process definitions for the organisation ("
          + organisation + ")", e);
    }
  }

  /**
   * Retrieve the process definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @return the process definition with the specified ID or <code>null</code> if the proces
   *         definition could not be found
   *
   * @throws ProcessServiceException
   */
  public ProcessDefinition getProcessDefinition(String id)
    throws ProcessServiceException
  {
    try
    {
      ProcessDefinition processDefinition = processDAO.getProcessDefinition(id);

      Parser parser = new Parser();
      parser.parse(processDefinition.getData());

      return processDefinition;
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to retrieve the process definition with ID (" + id
          + ")", e);
    }
  }

  /**
   * Returns the summaries for all the process definitions associated with the organisation
   * identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the summaries for all the process definitions associated with the organisation
   *         identified by the specified organisation code
   *
   * @throws ProcessServiceException
   */
  public List<ProcessDefinitionSummary> getProcessDefinitionSummariesForOrganisation(
      String organisation)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getProcessDefinitionSummariesForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the summaries for all the process definitions for the organisation ("
          + organisation + ")", e);
    }
  }

  /**
   * Retrieve the summary for the process definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @return the summary for the process definition with the specified ID or <code>null</code> if
   *         the process definition could not be found
   *
   * @throws ProcessServiceException
   */
  public ProcessDefinitionSummary getProcessDefinitionSummary(String id)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getProcessDefinitionSummary(id);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the summary for the process definition with ID (" + id + ")", e);
    }
  }

  /**
   * Returns all the process definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return all the process definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws ProcessServiceException
   */
  public List<ProcessDefinition> getProcessDefinitionsForOrganisation(String organisation)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getProcessDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve all the process definitions for the organisation (" + organisation
          + ")", e);
    }
  }

  /**
   * Initialise the Process Service instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Process Service instance (" + getInstanceName() + ")");

    try
    {
      // Initialise the configuration for the Process Service instance
      initConfiguration();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialise the Process Service instance", e);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Process Service instance: "
          + e.getMessage());
    }

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
      throw new DAOException("Failed to retrieve the application data source"
          + " using the JNDI names (java:app/jdbc/ApplicationDataSource) and"
          + " (java:comp/env/jdbc/ApplicationDataSource)");
    }
  }

  /**
   * Check whether the process definition with the specified ID exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the proces
   *           definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   *
   * @throws ProcessServiceException
   */
  public boolean processDefinitionExists(String id)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.processDefinitionExists(id);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to check whether the process definition with ID ("
          + id + ") exists", e);
    }
  }

  /**
   * Save the process definition.
   * <p/>
   * This will create a new entry for the process definition in the database or update the
   * existing entry.
   *
   * @param procesDefinition the <code>ProcessDefinition</code> instance containing the information
   *                         for the process definition
   * @param savedBy          the username identifying the user that saved the process definition
   *
   * @return the saved process definition
   *
   * @throws ProcessServiceException
   */
  public ProcessDefinition saveProcessDefinition(ProcessDefinition procesDefinition, String savedBy)
    throws ProcessServiceException
  {
    try
    {
      if (processDAO.processDefinitionExists(procesDefinition.getId()))
      {
        procesDefinition = processDAO.updateProcessDefinition(procesDefinition, savedBy);
      }
      else
      {
        processDAO.createProcessDefinition(procesDefinition);
      }

      return procesDefinition;
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to save the process definition with ID ("
          + procesDefinition.getId() + ")", e);
    }
  }

  /**
   * Retrieves the name of the Process Service instance.
   */
  private String getInstanceName()
  {
    if (instanceName == null)
    {
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

      if (applicationName == null)
      {
        logger.error("Failed to retrieve the application name from JNDI using the names ("
            + "java:app/AppName) and (java:comp/env/ApplicationName) while constructing"
            + " the Process Service instance name");

        applicationName = "Unknown";
      }

      instanceName = applicationName + "::";

      try
      {
        java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

        instanceName += localMachine.getHostName().toLowerCase();
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the server hostname while constructing the Process"
            + " Service instance name", e);
        instanceName = "Unknown";
      }

      // Check if we are running under JBoss and if so retrieve the server name
      if (System.getProperty("jboss.server.name") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("jboss.server.name");
      }

      // Check if we are running under Glassfish and if so retrieve the server name
      else if (System.getProperty("glassfish.version") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("com.sun.aas.instanceName");
      }

      // Check if we are running under WebSphere Application Server Community Edition (Geronimo)
      else if (System.getProperty("org.apache.geronimo.server.dir") != null)
      {
        instanceName = instanceName + "::Geronimo";
      }

      // Check if we are running under WebSphere Application Server Liberty Profile
      else if (System.getProperty("wlp.user.dir") != null)
      {
        instanceName = instanceName + "::WLP";
      }

      /*
       * Check if we are running under WebSphere and if so execute the code below to retrieve the
       * server name.
       */
      else
      {
        try
        {
          instanceName = instanceName + "::" + InitialContext.doLookup("servername").toString();
        }
        catch (Throwable e)
        {
          logger.error("Failed to retrieve the name of the WebSphere server instance from JNDI"
              + " while constructing the Process Service instance name", e);
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
  }

  /**
   * Initialise the configuration for the Process Service instance.
   *
   * @throws ProcessServiceException
   */
  private void initConfiguration()
    throws ProcessServiceException
  {
//  try
//  {
//  }
//  catch (Throwable e)
//  {
//    throw new ProcessServiceException(
//        "Failed to initialise the configuration for the Process Service instance", e);
//  }
  }
}
