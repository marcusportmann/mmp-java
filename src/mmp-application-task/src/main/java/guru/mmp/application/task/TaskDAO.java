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

package guru.mmp.application.task;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>TaskDAO</code> class implements the task-related persistence operations.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class TaskDAO
  implements ITaskDAO
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TaskDAO.class);
  @SuppressWarnings("unused")
  private String createScheduledTaskSQL;

  /**
   * The data source used to provide connections to the database.
   */
  private DataSource dataSource;
  @SuppressWarnings("unused")
  private String deleteScheduledTaskSQL;
  private String getNextTaskScheduledForExecutionSQL;
  private String getNextUnscheduledTaskSQL;
  @SuppressWarnings("unused")
  private String getScheduledTaskByIdSQL;
  private String getScheduledTaskParametersSQL;
  private String getUnscheduledTasksSQL;
  private String incrementScheduledTaskExecutionAttemptsSQL;
  private String lockScheduledTaskSQL;
  private String resetScheduledTaskLocksSQL;
  private String scheduleTaskSQL;
  private String setScheduledTaskStatusSQL;
  private String unlockScheduledTaskSQL;

  /**
   * Constructs a new <code>TaskDAO</code>.
   */
  public TaskDAO() {}

  /**
   * Create the entry for the scheduled task in the database.
   *
   * @param scheduledTask the <code>ScheduledTask</code> instance containing the information for
   *                      the scheduled task
   *
   * @throws DAOException
   */
  public void createScheduledTask(ScheduledTask scheduledTask)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(createScheduledTaskSQL))
    {
      statement.setString(1, scheduledTask.getId());
      statement.setString(2, scheduledTask.getName());
      statement.setString(3, scheduledTask.getSchedulingPattern());
      statement.setString(4, scheduledTask.getTaskClass());
      statement.setInt(5, ScheduledTask.Status.SCHEDULED.getCode());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
          + createScheduledTaskSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the scheduled task (" + scheduledTask.getName() + ") to the database", e);
    }

  }

  /**
   * Retrieve the next task that is scheduled for execution.
   * <p/>
   * The scheduled task will be locked to prevent duplicate processing.
   *
   * @param executionRetryDelay the delay in milliseconds between successive attempts to execute
   *                            a scheduled task
   * @param lockName            the name of the lock that should be applied to the task scheduled
   *                            for execution when it is retrieved
   *
   * @return the next task that is scheduled for execution or <code>null</code> if no tasks are
   *         currently scheduled for execution
   *
   * @throws DAOException
   */
  public ScheduledTask getNextTaskScheduledForExecution(int executionRetryDelay, String lockName)
    throws DAOException
  {
    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      ScheduledTask scheduledTask = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(getNextTaskScheduledForExecutionSQL))
      {
        Timestamp processedBefore = new Timestamp(System.currentTimeMillis() - executionRetryDelay);

        statement.setInt(1, ScheduledTask.Status.SCHEDULED.getCode());
        statement.setTimestamp(2, processedBefore);
        statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            Timestamp updated = new Timestamp(System.currentTimeMillis());

            scheduledTask = getScheduledTask(rs);

            scheduledTask.setStatus(ScheduledTask.Status.EXECUTING);
            scheduledTask.setLockName(lockName);
            scheduledTask.setUpdated(updated);

            try (PreparedStatement updateStatement =
                connection.prepareStatement(lockScheduledTaskSQL))
            {
              updateStatement.setInt(1, ScheduledTask.Status.EXECUTING.getCode());
              updateStatement.setString(2, lockName);
              updateStatement.setTimestamp(3, updated);
              updateStatement.setString(4, scheduledTask.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new DAOException(
                    "No rows were affected as a result of executing the SQL statement ("
                    + lockScheduledTaskSQL + ")");
              }
            }
          }
        }
      }

      transactionManager.commit();

      return scheduledTask;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving "
            + "the next task that has been scheduled for execution from the database", f);
      }

      throw new DAOException("Failed to retrieve the next task that has been scheduled for "
          + "execution from the database", e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving "
            + "the next task that has been scheduled for execution from the database", e);
      }
    }
  }

  /**
   * Retrieve the parameters for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @return the parameters for the scheduled task
   *
   * @throws DAOException
   */
  public List<ScheduledTaskParameter> getScheduledTaskParameters(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getScheduledTaskParametersSQL))
    {
      statement.setString(1, id);

      List<ScheduledTaskParameter> scheduledTaskParameters = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          scheduledTaskParameters.add(getScheduledTaskParameter(rs));
        }
      }

      return scheduledTaskParameters;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the parameters for the scheduled task (" + id
          + ") from the database", e);
    }
  }

  /**
   * Retrieve the unscheduled tasks.
   *
   * @return the unscheduled tasks
   *
   * @throws DAOException
   */
  public List<ScheduledTask> getUnscheduledTasks()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUnscheduledTasksSQL))
    {
      List<ScheduledTask> unscheduledTasks = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          unscheduledTasks.add(getScheduledTask(rs));
        }
      }

      return unscheduledTasks;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the unscheduled tasks from the database", e);
    }
  }

  /**
   * Increment the execution attempts for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @throws DAOException
   */
  public void incrementScheduledTaskExecutionAttempts(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(incrementScheduledTaskExecutionAttemptsSQL))
    {
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());

      statement.setTimestamp(1, currentTime);
      statement.setTimestamp(2, currentTime);
      statement.setString(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + incrementScheduledTaskExecutionAttemptsSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to increment the execution attempts for the scheduled task ("
          + id + ") in the database", e);
    }
  }

  /**
   * Initialise the <code>TaskDAO</code> instance.
   */
  @PostConstruct
  public void init()
  {
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

    try
    {
      // Retrieve the database meta data
      String schemaSeparator;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.DEFAULT_DATABASE_SCHEMA + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to initialise the " + getClass().getName()
          + " data access object: " + e.getMessage(), e);
    }
  }

  /**
   * Lock a scheduled task.
   *
   * @param id       the ID uniquely identifying the scheduled task
   * @param status   the new status for the locked scheduled task
   * @param lockName the name of the lock that should be applied to the scheduled task
   *
   * @throws DAOException
   */
  public void lockScheduledTask(String id, ScheduledTask.Status status, String lockName)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(lockScheduledTaskSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setString(2, lockName);
      statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      statement.setString(4, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + lockScheduledTaskSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to lock and set the status for the scheduled task (" + id
          + ") to (" + status + ") in the database", e);
    }
  }

  /**
   * Reschedule the task for execution.
   *
   * @param id                the ID uniquely identifying the scheduled task
   * @param schedulingPattern the cron-style scheduling pattern for the scheduled task used to
   *                          determine the next execution time
   *
   * @throws DAOException
   */
  public void rescheduleTask(String id, String schedulingPattern)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      Predictor predictor = new Predictor(schedulingPattern, System.currentTimeMillis());

      Date nextExecution = predictor.nextMatchingDate();

      scheduleTask(connection, id, nextExecution);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to reschedule the task (" + id + ") for execution", e);
    }
  }

  /**
   * Reset the scheduled task locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the scheduled tasks
   * @param status    the current status of the scheduled tasks that have been locked
   * @param newStatus the new status for the scheduled tasks that have been unlocked
   *
   * @return the number of scheduled task locks reset
   *
   * @throws DAOException
   */
  public int resetScheduledTaskLocks(String lockName, ScheduledTask.Status status,
      ScheduledTask.Status newStatus)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetScheduledTaskLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, lockName);
      statement.setInt(4, status.getCode());

      return statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to reset the locks for the scheduled tasks with status ("
          + status + ") that have been locked using the lock name (" + lockName + ")", e);
    }
  }

  /**
   * Schedule the next unscheduled task for execution.
   *
   * @return <code>true</code> if there are more unscheduled tasks to schedule or
   *         <code>false</code> if there are no more unscheduled tasks to schedule
   *
   * @throws DAOException
   */
  public boolean scheduleNextUnscheduledTaskForExecution()
    throws DAOException
  {
    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      boolean hasMoreUnscheduledTasks;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextUnscheduledTaskSQL))
      {
        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            ScheduledTask scheduledTask = getScheduledTask(rs);

            Date nextExecution = null;

            try
            {
              Predictor predictor = new Predictor(scheduledTask.getSchedulingPattern(),
                System.currentTimeMillis());

              nextExecution = predictor.nextMatchingDate();
            }
            catch (Throwable e)
            {
              logger.error(
                  "The next execution date could not be determined for the unscheduled task ("
                  + scheduledTask.getId() + ") with the scheduling pattern ("
                  + scheduledTask.getSchedulingPattern()
                  + "): The scheduled task will be marked as FAILED", e);
            }

            if (nextExecution == null)
            {
              setScheduledTaskStatus(connection, scheduledTask.getId(),
                  ScheduledTask.Status.FAILED);
            }
            else
            {
              logger.info("Scheduling the unscheduled task (" + scheduledTask.getId()
                  + ") for execution at (" + nextExecution + ")");

              scheduleTask(connection, scheduledTask.getId(), nextExecution);
            }

            hasMoreUnscheduledTasks = true;
          }
          else
          {
            hasMoreUnscheduledTasks = false;
          }
        }
      }

      transactionManager.commit();

      return hasMoreUnscheduledTasks;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while scheduling "
            + "the next unscheduled task for execution", e);
      }

      if (e instanceof DAOException)
      {
        throw((DAOException) e);
      }
      else
      {
        throw new DAOException("Failed to schedule the next unscheduled task for execution", e);
      }
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while scheduling "
            + "the next unscheduled task for execution", e);
      }
    }
  }

  /**
   * Set the status for the scheduled task with the specified ID.
   *
   * @param id     the ID uniquely identifying the scheduled task
   * @param status the new status for the scheduled task
   *
   * @throws DAOException
   */
  public void setScheduledTaskStatus(String id, ScheduledTask.Status status)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      setScheduledTaskStatus(connection, id, status);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to set the status (" + status + ")  for the scheduled task ("
          + id + ") in the database", e);
    }
  }

  /**
   * Unlock a locked scheduled task.
   *
   * @param id     the ID uniquely identifying the scheduled task
   * @param status the new status for the unlocked scheduled task
   *
   * @throws DAOException
   */
  public void unlockScheduledTask(String id, ScheduledTask.Status status)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockScheduledTaskSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(
            "No rows were affected as a result  of executing the SQL statement ("
            + unlockScheduledTaskSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to unlock and set the status for the scheduled task (" + id
          + ") to (" + status + ") in the database", e);
    }
  }

  /**
   * Build the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects referenced by the DAO
   */
  protected void buildStatements(String schemaPrefix)
  {
    // createScheduledTaskSQL
    createScheduledTaskSQL = "INSERT INTO " + schemaPrefix + "SCHEDULED_TASKS"
      + " (ID, NAME, SCHEDULING_PATTERN, TASK_CLASS, STATUS) VALUES (?, ?, ?, ?, ?)";

    // deleteScheduledTaskSQL
    deleteScheduledTaskSQL = "DELETE FROM " + schemaPrefix + "SCHEDULED_TASKS WHERE ID=?";

    // getNextTaskScheduledForExecutionSQL
    getNextTaskScheduledForExecutionSQL =
      "SELECT ID, NAME, SCHEDULING_PATTERN, TASK_CLASS, STATUS,"
      + " EXECUTION_ATTEMPTS, LOCK_NAME, LAST_EXECUTED, NEXT_EXECUTION, UPDATED" + " FROM "
      + schemaPrefix + "SCHEDULED_TASKS"
      + " WHERE STATUS=? AND ((EXECUTION_ATTEMPTS=0) OR ((EXECUTION_ATTEMPTS>0)"
      + " AND (LAST_EXECUTED<?))) AND NEXT_EXECUTION <= ?"
      + " ORDER BY UPDATED FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getNextUnscheduledTaskSQL
    getNextUnscheduledTaskSQL = "SELECT ID, NAME, SCHEDULING_PATTERN, TASK_CLASS, STATUS,"
        + " EXECUTION_ATTEMPTS, LOCK_NAME, LAST_EXECUTED, NEXT_EXECUTION, UPDATED" + " FROM "
        + schemaPrefix + "SCHEDULED_TASKS" + " WHERE NEXT_EXECUTION IS NULL AND STATUS <= 2"
        + " ORDER BY UPDATED FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getScheduledTaskByIdSQL
    getScheduledTaskByIdSQL = "SELECT ID, NAME, SCHEDULING_PATTERN, TASK_CLASS, STATUS,"
        + " EXECUTION_ATTEMPTS, LOCK_NAME, LAST_EXECUTED, NEXT_EXECUTION, UPDATED" + " FROM "
        + schemaPrefix + "SCHEDULED_TASKS" + " WHERE ID = ?";

    // getScheduledTaskParametersSQL
    getScheduledTaskParametersSQL = "SELECT ID, SCHEDULED_TASK_ID, NAME, VALUE" + " FROM "
        + schemaPrefix + "SCHEDULED_TASK_PARAMETERS" + " WHERE SCHEDULED_TASK_ID = ?";

    // getUnscheduledTasksSQL
    getUnscheduledTasksSQL = "SELECT ID, NAME, SCHEDULING_PATTERN, TASK_CLASS, STATUS,"
        + " EXECUTION_ATTEMPTS, LOCK_NAME, LAST_EXECUTED, NEXT_EXECUTION, UPDATED" + " FROM "
        + schemaPrefix + "SCHEDULED_TASKS" + " WHERE NEXT_EXECUTION IS NULL";

    // lockScheduledTaskSQL
    lockScheduledTaskSQL = "UPDATE " + schemaPrefix + "SCHEDULED_TASKS"
        + " SET STATUS=?, LOCK_NAME=?, UPDATED=? WHERE ID=?";

    // incrementScheduledTaskExecutionAttemptsSQL
    incrementScheduledTaskExecutionAttemptsSQL = "UPDATE " + schemaPrefix + "SCHEDULED_TASKS"
        + " SET EXECUTION_ATTEMPTS=EXECUTION_ATTEMPTS + 1, UPDATED=?, LAST_EXECUTED=?"
        + " WHERE ID=?";

    // resetScheduledTaskLocksSQL
    resetScheduledTaskLocksSQL = "UPDATE " + schemaPrefix + "SCHEDULED_TASKS"
        + " SET STATUS=?, LOCK_NAME=NULL, UPDATED=?" + " WHERE LOCK_NAME=? AND STATUS=?";

    // scheduleTaskSQL
    scheduleTaskSQL = "UPDATE " + schemaPrefix + "SCHEDULED_TASKS"
        + " SET STATUS=1, EXECUTION_ATTEMPTS = 0, NEXT_EXECUTION=?, UPDATED=?" + " WHERE ID=?";

    // setScheduledTaskStatusSQL
    setScheduledTaskStatusSQL = "UPDATE " + schemaPrefix
        + "SCHEDULED_TASKS SET STATUS=? WHERE ID=?";

    // unlockScheduledTaskSQL
    unlockScheduledTaskSQL = "UPDATE " + schemaPrefix + "SCHEDULED_TASKS"
        + " SET STATUS=?, UPDATED=?, LOCK_NAME=NULL WHERE ID=?";
  }

  private ScheduledTask getScheduledTask(ResultSet rs)
    throws SQLException
  {
    return new ScheduledTask(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
        ScheduledTask.Status.fromCode(rs.getInt(5)), rs.getInt(6), rs.getString(7),
        rs.getTimestamp(8), rs.getTimestamp(9), rs.getTimestamp(10));
  }

  private ScheduledTaskParameter getScheduledTaskParameter(ResultSet rs)
    throws SQLException
  {
    return new ScheduledTaskParameter(rs.getLong(1), rs.getString(2), rs.getString(3),
        rs.getString(4));
  }

  private void scheduleTask(Connection connection, String id, Date nextExecution)
  {
    try (PreparedStatement statement = connection.prepareStatement(scheduleTaskSQL))
    {
      statement.setTimestamp(1, new Timestamp(nextExecution.getTime()));
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + scheduleTaskSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to schedule the task (" + id + ")", e);
    }
  }

  private void setScheduledTaskStatus(Connection connection, String id, ScheduledTask.Status status)
  {
    try (PreparedStatement statement = connection.prepareStatement(setScheduledTaskStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setString(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + setScheduledTaskStatusSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to set the status (" + status + ")  for the scheduled task ("
          + id + ") in the database", e);
    }
  }
}
