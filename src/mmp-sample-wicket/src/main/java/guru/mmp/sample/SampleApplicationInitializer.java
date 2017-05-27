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

package guru.mmp.sample;

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.jdbc.AtomikosDataSourceBean;
import guru.mmp.application.web.WebApplication;
import guru.mmp.application.web.WebApplicationInitializer;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.sample.web.SampleApplication;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleApplicationInitializer</code> class implements the web application initializer
 * for the Sample application.
 *
 * @author Marcus Portmann
 */
@ComponentScan(basePackages = { "guru.mmp.sample" })
public class SampleApplicationInitializer extends WebApplicationInitializer
{
  private static Object dataSourceLock = new Object();
  private static DataSource dataSource;

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args)
    throws Exception
  {
    SpringApplication.run(SampleApplicationInitializer.class, args);
  }

  /**
   * Returns the Wicket web application.
   *
   * @return the Wicket web application
   */
  @Override
  public WebApplication webApplication()
  {
    return new SampleApplication();
  }

  /**
   * Initialise the in-memory application database and return a data source that can be used to
   * interact with the database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  protected DataSource dataSource()
  {
    synchronized (dataSourceLock)
    {
      if (dataSource == null)
      {
        boolean logSQL = false;

        try
        {
          JdbcDataSource jdbcDataSource = new JdbcDataSource();

          jdbcDataSource.setURL("jdbc:h2:mem:" + Thread.currentThread().getName()
              + ";MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

          Runtime.getRuntime().addShutdownHook(new Thread(() ->
              {
                try
                {
                  try (Connection connection = jdbcDataSource.getConnection();
                    Statement statement = connection.createStatement())

                  {
                    statement.executeUpdate("SHUTDOWN");
                  }
                }
                catch (Throwable e)
                {
                  throw new RuntimeException(
                      "Failed to shutdown the in-memory application database", e);
                }
              }
              ));

          /*
           * Initialise the in-memory database using the SQL statements contained in the file with
           * the specified resource path.
           */
          for (String resourcePath : getDatabaseInitResources())
          {
            try
            {
              // Load the SQL statements used to initialise the database tables
              List<String> sqlStatements = DAOUtil.loadSQL(resourcePath);

              // Get a connection to the in-memory database
              try (Connection connection = jdbcDataSource.getConnection())
              {
                for (String sqlStatement : sqlStatements)
                {
                  if (logSQL)
                  {
                    Logger.getAnonymousLogger().info("Executing SQL statement: " + sqlStatement);
                  }

                  try (Statement statement = connection.createStatement())
                  {
                    statement.execute(sqlStatement);
                  }
                }
              }
            }
            catch (SQLException e)
            {
              try (Connection connection = jdbcDataSource.getConnection();
                Statement shutdownStatement = connection.createStatement())
              {
                shutdownStatement.executeUpdate("SHUTDOWN");
              }
              catch (Throwable f)
              {
                Logger.getAnonymousLogger().severe(
                    "Failed to shutdown the in-memory application database: " + e.getMessage());
              }

              throw e;
            }
          }

          AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

          atomikosDataSourceBean.setUniqueResourceName(Thread.currentThread().getName()
              + "-ApplicationDataSource");

          atomikosDataSourceBean.setXaDataSource(jdbcDataSource);
          atomikosDataSourceBean.setMinPoolSize(5);
          atomikosDataSourceBean.setMaxPoolSize(10);

          dataSource = atomikosDataSourceBean;
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the in-memory application database", e);
        }
      }

      return dataSource;
    }
  }

  /**
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialise the in-memory application database.
   */
  protected List<String> getDatabaseInitResources()
  {
    List<String> resources = new ArrayList<>();

    resources.add("guru/mmp/application/persistence/ApplicationH2.sql");

    return resources;
  }
}
