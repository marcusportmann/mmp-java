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

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SimpleDataAccessObject</code> class is the common base class which all Simple Data
 * Access Objects (JDBC) must extend.
 *
 * @author Marcus Portmann
 */
public abstract class SimpleDataAccessObject
  implements IDataAccessObject
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SimpleDataAccessObject.class);
  private DataSource dataSource;
  private boolean hasExistingDataSource;

  /**
   * Constructs a new <code>SimpleDataAccessObject</code>.
   */
  public SimpleDataAccessObject()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Initialising the Simple Data Access Object");
    }
  }

  /**
   * Constructs a new <code>SimpleDataAccessObject</code> using the specified data source.
   *
   * @param existingDataSource the existing data source
   */
  public SimpleDataAccessObject(DataSource existingDataSource)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Initialising the Simple Data Access Object using an existing data source");
    }

    dataSource = existingDataSource;
    hasExistingDataSource = true;
  }

  /**
   * Returns a database connection from the data source associated with this DAO.
   *
   * @return a database connection from the data source associated with this DAO
   * @throws DAOException
   */
  public Connection getConnection()
    throws DAOException
  {
    try
    {
      return getDataSource().getConnection();
    }
    catch (Throwable e)
    {
      if (!hasExistingDataSource)
      {
        dataSource = null;
      }

      throw new DAOException("Failed to retrieve a connection from the data source ("
          + getDataSourceName() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Returns the data source associated with this DAO.
   *
   * @return the data source associated with this DAO
   * @throws DAOException
   */
  public DataSource getDataSource()
    throws DAOException
  {
    if (dataSource == null)
    {
      try
      {
        // Retrieve the Data Source from JNDI
        InitialContext ic = new InitialContext();

        try
        {
          dataSource = (javax.sql.DataSource) ic.lookup(getDataSourceName());
        }
        finally
        {
          ic.close();
        }
      }
      catch (Throwable e)
      {
        throw new DAOException("Failed to lookup the data source (" + getDataSourceName()
            + ") using JNDI: " + e.getMessage(), e);
      }
    }

    return dataSource;
  }

  /**
   * Returns the JNDI name of the data source associated with this DAO.
   * <p/>
   * This method must be implemented by the DAOs derived from this abstract class.
   *
   * @return the JNDI name of the data source associated with this DAO
   */
  public abstract String getDataSourceName();
}
