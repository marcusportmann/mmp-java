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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import guru.mmp.common.persistence.DAOUtil;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.TransactionManager;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationConfiguration</code> class.
 *
 * @author Marcus Portmann
 */
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackages = { "guru.mmp.application" })
public class ApplicationConfiguration
  implements TransactionManagementConfigurer
{
  private PlatformTransactionManager transactionManager;

  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager()
  {
    return getTransactionManager();
  }

  /**
   * Returns a Spring transaction manager that leverages the Atomikos JTA transaction manager and
   * user transaction.
   *
   * @return a Spring transaction manager that leverages the Atomikos JTA transaction manager and
   *         user transaction
   */
  @Bean
  public PlatformTransactionManager getTransactionManager()
  {
    if (transactionManager == null)
    {
      try
      {
        // Initialise the Atomikos JTA user transaction and transaction manager
        Enhancer transactionManagerEnhancer = new Enhancer();
        transactionManagerEnhancer.setSuperclass(UserTransactionManager.class);
        transactionManagerEnhancer.setCallback(new TransactionManagerTransactionTracker());

        TransactionManager transactionManager =
            (TransactionManager) transactionManagerEnhancer.create();

        Enhancer userTransactionEnhancer = new Enhancer();
        userTransactionEnhancer.setSuperclass(UserTransactionImp.class);
        userTransactionEnhancer.setCallback(new UserTransactionTracker(transactionManager));

        UserTransactionImp userTransaction = (UserTransactionImp) userTransactionEnhancer.create();

        userTransaction.setTransactionTimeout(300);

        return new JtaTransactionManager(userTransaction, transactionManager);
      }
      catch (Throwable e)
      {
        throw new RuntimeException(
            "Failed to initialise the Atomikos JTA user transaction and transaction manager", e);
      }
    }

    return transactionManager;
  }

  /**
   * Initialise the in-memory application database and return a data source that can be used to
   * interact with the database.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean
  @Qualifier("applicationDataSource")
  protected DataSource getDataSource()
  {
    boolean logSQL = false;

    try
    {
      Thread.currentThread().getContextClassLoader().loadClass("org.h2.Driver");

      Class<?> jdbcDataSourceClass = Thread.currentThread().getContextClassLoader().loadClass(
          "org.h2.jdbcx.JdbcDataSource");

      Method setURLMethod = jdbcDataSourceClass.getMethod("setURL", String.class);

      final DataSource jdbcDataSource = (DataSource) jdbcDataSourceClass.newInstance();

      setURLMethod.invoke(jdbcDataSource, "jdbc:h2:mem:" + Thread.currentThread().getName()
          + ";MODE=DB2;DB_CLOSE_DELAY=-1;" + "DB_CLOSE_ON_EXIT=FALSE");

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
              throw new RuntimeException("Failed to shutdown the in-memory application database",
                  e);
            }
          }
          ));

      /*
       * Initialise the in-memory database using the SQL statements contained in the file with the
       * specified resource path.
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

      Class<?> atomikosDataSourceBeanClass = Thread.currentThread().getContextClassLoader()
          .loadClass("com.atomikos.jdbc.AtomikosDataSourceBean");

      Object atomikosDataSourceBean = atomikosDataSourceBeanClass.newInstance();

      Method setUniqueResourceNameMethod = atomikosDataSourceBeanClass.getMethod(
          "setUniqueResourceName", String.class);

      setUniqueResourceNameMethod.invoke(atomikosDataSourceBean, Thread.currentThread().getName()
          + "-ApplicationDataSource");

      Method setXaDataSourceMethod = atomikosDataSourceBeanClass.getMethod("setXaDataSource",
          XADataSource.class);

      setXaDataSourceMethod.invoke(atomikosDataSourceBean, (XADataSource) jdbcDataSource);

      Method setMinPoolSizeMethod = atomikosDataSourceBeanClass.getMethod("setMinPoolSize", Integer
          .TYPE);
      setMinPoolSizeMethod.invoke(atomikosDataSourceBean, 5);

      Method setMaxPoolSizeMethod = atomikosDataSourceBeanClass.getMethod("setMaxPoolSize", Integer
          .TYPE);
      setMaxPoolSizeMethod.invoke(atomikosDataSourceBean, 10);

      return ((DataSource) atomikosDataSourceBean);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the in-memory application database", e);
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
