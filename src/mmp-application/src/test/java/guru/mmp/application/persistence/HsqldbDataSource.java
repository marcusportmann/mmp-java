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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOUtil;

import org.apache.commons.dbcp.BasicDataSource;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;

import java.util.logging.Logger;

/**
 * Datasource that shuts down the HSQLDB database when the data source is closed. HSQLDB is
 * normally only shutdown with a JVM hook, so this datasource is better suited when you need to
 * shutdown/restart within one JVM.
 *
 * @author Marcus Portmann
 */
public class HsqldbDataSource extends BasicDataSource
{
  private static final Logger logger = Logger.getLogger(HsqldbDataSource.class.getName());

  /**
   * Close and release all connections that are currently stored in the connection pool associated
   * with the data source.
   * <p/>
   * The HSQLDB database associated with the data source will also be shutdown.
   *
   * @throws SQLException
   */
  @Override
  public synchronized void close()
    throws SQLException
  {
    Connection connection = null;
    Statement statement = null;

    try
    {
      connection = getConnection();

      statement = connection.createStatement();

      statement.executeUpdate("SHUTDOWN");
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }

    super.close();
  }

  /**
   * Returns the <code>java.util.Logger</code> instance for this class.
   *
   * @return the <code>java.util.Logger</code> instance for this class
   *
   * @throws SQLFeatureNotSupportedException
   */
  @Override
  public Logger getParentLogger()
    throws SQLFeatureNotSupportedException
  {
    return logger;
  }
}
