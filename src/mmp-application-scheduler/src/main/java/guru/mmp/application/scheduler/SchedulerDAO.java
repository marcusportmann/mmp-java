/*
 * Copyright 2016 Marcus Portmann
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

package guru.mmp.application.scheduler;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>SchedulerDAO</code> class implements the job-related persistence operations.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class SchedulerDAO
  implements ISchedulerDAO
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SchedulerDAO.class);
  @SuppressWarnings("unused")
  private String createJobSQL;

  /**
   * The data source used to provide connections to the database.
   */
  private DataSource dataSource;
  @SuppressWarnings("unused")
  private String deleteJobSQL;
  private String getJobParametersSQL;
  private String getJobSQL;
  private String getJobsSQL;
  private String getNextJobScheduledForExecutionSQL;
  private String getNextUnscheduledJobSQL;
  private String getNumberOfJobsSQL;
  private String getUnscheduledJobsSQL;
  private String incrementJobExecutionAttemptsSQL;
  private String lockJobSQL;
  private String resetJobLocksSQL;
  private String scheduleJobSQL;
  private String setJobStatusSQL;
  private String unlockJobSQL;

  /**
   * Constructs a new <code>SchedulerDAO</code>.
   */
  public SchedulerDAO() {}

  /**
   * Create the entry for the job in the database.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   *
   * @throws DAOException
   */
  public void createJob(Job job)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createJobSQL))
    {
      statement.setObject(1, job.getId());
      statement.setString(2, job.getName());
      statement.setString(3, job.getSchedulingPattern());
      statement.setString(4, job.getJobClass());
      statement.setInt(5, job.getStatus().getCode());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", createJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to add the job (%s) to the database",
          job.getName()), e);
    }
  }

  /**
   * Retrieve the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the job or <code>null</code> if the job could not be found
   *
   * @throws DAOException
   */
  public Job getJob(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getJobSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getJob(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to retrieve the job (%s) from the database",
          id), e);
    }
  }

  /**
   * Retrieve the parameters for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the parameters for the job
   *
   * @throws DAOException
   */
  public List<JobParameter> getJobParameters(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getJobParametersSQL))
    {
      statement.setObject(1, id);

      List<JobParameter> jobParameters = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          jobParameters.add(getJobParameter(rs));
        }
      }

      return jobParameters;
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the parameters for the job (%s) from the database", id), e);
    }
  }

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   *
   * @throws DAOException
   */
  public List<Job> getJobs()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getJobsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<Job> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(getJob(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the jobs", e);
    }
  }

  /**
   * Retrieve the next job that is scheduled for execution.
   * <p/>
   * The job will be locked to prevent duplicate processing.
   *
   * @param executionRetryDelay the delay in milliseconds between successive attempts to execute
   *                            a job
   * @param lockName            the name of the lock that should be applied to the job scheduled
   *                            for execution when it is retrieved
   *
   * @return the next job that is scheduled for execution or <code>null</code> if no jobs are
   *         currently scheduled for execution
   *
   * @throws DAOException
   */
  public Job getNextJobScheduledForExecution(int executionRetryDelay, String lockName)
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

      Job job = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            getNextJobScheduledForExecutionSQL))
      {
        Timestamp processedBefore = new Timestamp(System.currentTimeMillis() - executionRetryDelay);

        statement.setInt(1, Job.Status.SCHEDULED.getCode());
        statement.setTimestamp(2, processedBefore);
        statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            Timestamp updated = new Timestamp(System.currentTimeMillis());

            job = getJob(rs);

            job.setStatus(Job.Status.EXECUTING);
            job.setLockName(lockName);
            job.setUpdated(updated);

            try (PreparedStatement updateStatement = connection.prepareStatement(lockJobSQL))
            {
              updateStatement.setInt(1, Job.Status.EXECUTING.getCode());
              updateStatement.setString(2, lockName);
              updateStatement.setTimestamp(3, updated);
              updateStatement.setObject(4, job.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new DAOException(String.format(
                    "No rows were affected as a result of executing the SQL statement (%s)",
                    lockJobSQL));
              }
            }
          }
        }
      }

      transactionManager.commit();

      return job;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(
            "Failed to rollback the transaction while retrieving the next job that has been "
            + "scheduled for execution from the database", f);
      }

      throw new DAOException(
          "Failed to retrieve the next job that has been scheduled for execution from the database",
          e);
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
        logger.error(
            "Failed to resume the transaction while retrieving the next job that has been "
            + "scheduled for execution from the database", e);
      }
    }
  }

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   *
   * @throws DAOException
   */
  public int getNumberOfJobs()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfJobsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the number of jobs", e);
    }
  }

  /**
   * Retrieve the unscheduled jobs.
   *
   * @return the unscheduled jobs
   *
   * @throws DAOException
   */
  public List<Job> getUnscheduledJobs()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUnscheduledJobsSQL))
    {
      List<Job> unscheduledJobs = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          unscheduledJobs.add(getJob(rs));
        }
      }

      return unscheduledJobs;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the unscheduled jobs from the database", e);
    }
  }

  /**
   * Increment the execution attempts for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @throws DAOException
   */
  public void incrementJobExecutionAttempts(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(incrementJobExecutionAttemptsSQL))
    {
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());

      statement.setTimestamp(1, currentTime);
      statement.setTimestamp(2, currentTime);
      statement.setObject(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            incrementJobExecutionAttemptsSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to increment the execution attempts for the job (%s) in the database", id), e);
    }
  }

  /**
   * Initialise the <code>SchedulerDAO</code> instance.
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
      throw new DAOException("Failed to retrieve the application data source using the JNDI names "
          + "(java:app/jdbc/ApplicationDataSource) and (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.MMP_DATABASE_SCHEMA + DAOUtil.getSchemaSeparator(
          dataSource);

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to initialise the %s data access object: %s",
          getClass().getName(), e.getMessage()), e);
    }
  }

  /**
   * Lock a job.
   *
   * @param id       the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status   the new status for the locked job
   * @param lockName the name of the lock that should be applied to the job
   *
   * @throws DAOException
   */
  public void lockJob(UUID id, Job.Status status, String lockName)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(lockJobSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setString(2, lockName);
      statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      statement.setObject(4, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", lockJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to lock and set the status for the job (%s) to (%s) in the database", id,
          status), e);
    }
  }

  /**
   * Reschedule the job for execution.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   *
   * @throws DAOException
   */
  public void rescheduleJob(UUID id, String schedulingPattern)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      Predictor predictor = new Predictor(schedulingPattern, System.currentTimeMillis());

      Date nextExecution = predictor.nextMatchingDate();

      scheduleJob(connection, id, nextExecution);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to reschedule the job (%s) for execution", id),
          e);
    }
  }

  /**
   * Reset the job locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the jobs
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   *
   * @throws DAOException
   */
  public int resetJobLocks(String lockName, Job.Status status, Job.Status newStatus)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetJobLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, lockName);
      statement.setInt(4, status.getCode());

      return statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to reset the locks for the jobs with status (%s) that have been locked using the "
          + "lock name (%s)", status, lockName), e);
    }
  }

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   *
   * @throws DAOException
   */
  public boolean scheduleNextUnscheduledJobForExecution()
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

      boolean hasMoreUnscheduledJobs;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextUnscheduledJobSQL))
      {
        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            Job job = getJob(rs);

            Date nextExecution = null;

            try
            {
              Predictor predictor = new Predictor(job.getSchedulingPattern(),
                  System.currentTimeMillis());

              nextExecution = predictor.nextMatchingDate();
            }
            catch (Throwable e)
            {
              logger.error(String.format(
                  "The next execution date could not be determined for the unscheduled job (%s) "
                  + "with the scheduling pattern (%s): The job will be marked as FAILED",
                  job.getId(), job.getSchedulingPattern()), e);
            }

            if (nextExecution == null)
            {
              setJobStatus(connection, job.getId(), Job.Status.FAILED);
            }
            else
            {
              logger.info(String.format(
                  "Scheduling the unscheduled job (%s) for execution at (%s)", job.getId(),
                  nextExecution));

              scheduleJob(connection, job.getId(), nextExecution);
            }

            hasMoreUnscheduledJobs = true;
          }
          else
          {
            hasMoreUnscheduledJobs = false;
          }
        }
      }

      transactionManager.commit();

      return hasMoreUnscheduledJobs;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(
            "Failed to rollback the transaction while scheduling the next unscheduled job", e);
      }

      if (e instanceof DAOException)
      {
        throw((DAOException) e);
      }
      else
      {
        throw new DAOException("Failed to schedule the next unscheduled job", e);
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
        logger.error("Failed to resume the transaction while scheduling the next unscheduled job",
            e);
      }
    }
  }

  /**
   * Set the status for the job.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the job
   *
   * @throws DAOException
   */
  public void setJobStatus(UUID id, Job.Status status)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      setJobStatus(connection, id, status);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to set the status (%s)  for the job (%s) in the database", status, id), e);
    }
  }

  /**
   * Unlock a locked job.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the unlocked job
   *
   * @throws DAOException
   */
  public void unlockJob(UUID id, Job.Status status)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockJobSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result  of executing the SQL statement (%s)",
            unlockJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to unlock and set the status for the job (%s) to (%s) in the database", id,
          status), e);
    }
  }

  /**
   * Build the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects referenced by the DAO
   */
  protected void buildStatements(String schemaPrefix)
  {
    // createJobSQL
    createJobSQL = "INSERT INTO " + schemaPrefix + "JOBS" + " (ID, NAME, SCHEDULING_PATTERN, "
        + "JOB_CLASS, STATUS) VALUES (?, ?, ?, ?, ?)";

    // deleteJobSQL
    deleteJobSQL = "DELETE FROM " + schemaPrefix + "JOBS J WHERE J.ID=?";

    // getNextJobScheduledForExecutionSQL
    getNextJobScheduledForExecutionSQL = "SELECT J.ID, J.NAME, J.SCHEDULING_PATTERN, J.JOB_CLASS, "
        + "J.STATUS, J.EXECUTION_ATTEMPTS, J.LOCK_NAME, J.LAST_EXECUTED, J.NEXT_EXECUTION, "
        + "J.UPDATED FROM " + schemaPrefix + "JOBS J "
        + "WHERE J.STATUS=? AND ((J.EXECUTION_ATTEMPTS=0) OR ((J.EXECUTION_ATTEMPTS>0) "
        + "AND (J.LAST_EXECUTED<?))) AND J" + ".NEXT_EXECUTION <= ? "
        + "ORDER BY J.UPDATED FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getNextUnscheduledJobSQL
    getNextUnscheduledJobSQL = "SELECT J.ID, J.NAME, J.SCHEDULING_PATTERN, J.JOB_CLASS, J.STATUS, "
        + "J.EXECUTION_ATTEMPTS, J.LOCK_NAME, J.LAST_EXECUTED, J.NEXT_EXECUTION, J.UPDATED "
        + "FROM " + schemaPrefix + "JOBS J WHERE J.NEXT_EXECUTION IS NULL AND J.STATUS <= 2 "
        + "ORDER BY J.UPDATED FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getNumberOfJobsSQL
    getNumberOfJobsSQL = "SELECT COUNT(J.ID) FROM " + schemaPrefix + "JOBS J";

    // getJobSQL
    getJobSQL = "SELECT J.ID, J.NAME, J.SCHEDULING_PATTERN, J.JOB_CLASS, J.STATUS, "
        + "J.EXECUTION_ATTEMPTS, J.LOCK_NAME, J.LAST_EXECUTED, J.NEXT_EXECUTION, J.UPDATED "
        + "FROM " + schemaPrefix + "JOBS J WHERE J.ID = ?";

    // getJobsSQL
    getJobsSQL = "SELECT J.ID, J.NAME, J.SCHEDULING_PATTERN, J.JOB_CLASS, J.STATUS, "
        + "J.EXECUTION_ATTEMPTS, J.LOCK_NAME, J.LAST_EXECUTED, J.NEXT_EXECUTION, J.UPDATED "
        + "FROM" + " " + schemaPrefix + "JOBS J";

    // getJobParametersSQL
    getJobParametersSQL = "SELECT JP.ID, JP.JOB_ID, JP.NAME, JP.VALUE FROM " + schemaPrefix
        + "JOB_PARAMETERS JP WHERE JP.JOB_ID = ?";

    // getUnscheduledJobsSQL
    getUnscheduledJobsSQL = "SELECT J.ID, J.NAME, J.SCHEDULING_PATTERN, J.JOB_CLASS, J.STATUS, "
        + "J.EXECUTION_ATTEMPTS, J.LOCK_NAME, J.LAST_EXECUTED, J.NEXT_EXECUTION, J.UPDATED "
        + "FROM " + schemaPrefix + "JOBS J WHERE J.NEXT_EXECUTION IS NULL";

    // lockJobSQL
    lockJobSQL = "UPDATE " + schemaPrefix + "JOBS J SET J.STATUS=?, J.LOCK_NAME=?, J.UPDATED=? "
        + "WHERE J.ID=?";

    // incrementJobExecutionAttemptsSQL
    incrementJobExecutionAttemptsSQL = "UPDATE " + schemaPrefix + "JOBS J "
        + "SET J.EXECUTION_ATTEMPTS=EXECUTION_ATTEMPTS + 1, J.UPDATED=?, J.LAST_EXECUTED=? "
        + "WHERE J.ID=?";

    // resetJobLocksSQL
    resetJobLocksSQL = "UPDATE " + schemaPrefix + "JOBS J "
        + "SET J.STATUS=?, J.LOCK_NAME=NULL, J.UPDATED=? WHERE J.LOCK_NAME=? AND J.STATUS=?";

    // scheduleJobSQL
    scheduleJobSQL = "UPDATE " + schemaPrefix + "JOBS J "
        + "SET J.STATUS=1, J.EXECUTION_ATTEMPTS=0, J.NEXT_EXECUTION=?, UJ.PDATED=? WHERE ID=?";

    // setJobStatusSQL
    setJobStatusSQL = "UPDATE " + schemaPrefix + "JOBS J SET J.STATUS=? WHERE J.ID=?";

    // unlockJobSQL
    unlockJobSQL = "UPDATE " + schemaPrefix + "JOBS J "
        + "SET J.STATUS=?, J.UPDATED=?, J.LOCK_NAME=NULL WHERE J.ID=?";
  }

  private Job getJob(ResultSet rs)
    throws SQLException
  {
    return new Job((UUID) rs.getObject(1), rs.getString(2), rs.getString(3), rs.getString(4), Job
        .Status.fromCode(rs.getInt(5)), rs.getInt(6), rs.getString(7), rs.getTimestamp(8),
        rs.getTimestamp(9), rs.getTimestamp(10));
  }

  private JobParameter getJobParameter(ResultSet rs)
    throws SQLException
  {
    return new JobParameter(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4));
  }

  private void scheduleJob(Connection connection, UUID id, Date nextExecution)
  {
    try (PreparedStatement statement = connection.prepareStatement(scheduleJobSQL))
    {
      statement.setTimestamp(1, new Timestamp(nextExecution.getTime()));
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            scheduleJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to schedule the job (%s)", id), e);
    }
  }

  private void setJobStatus(Connection connection, UUID id, Job.Status status)
  {
    try (PreparedStatement statement = connection.prepareStatement(setJobStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setObject(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            setJobStatusSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to set the status (%s)  for the job (%s) in the database", status, id), e);
    }
  }
}
