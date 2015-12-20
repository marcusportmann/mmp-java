package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.TransactionManagerFactory;

import net.sf.cglib.proxy.Enhancer;

import org.h2.jdbcx.JdbcDataSource;

//~--- JDK imports ------------------------------------------------------------

import java.io.PrintWriter;
import java.io.Serializable;

import java.lang.reflect.Method;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;

import javax.sql.*;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * The <code>JdbcDataSourceAutoEnlistWrapper</code> class.
 *
 * @author Marcus Portmann
 */
public class AutoEnlistJdbcDataSource
  implements XADataSource, DataSource, Serializable
{
  private static final long serialVersionUID = 1000000;
  private JdbcDataSource jdbcDataSource;
  private Map<Transaction, Connection> transactionConnectionMap = new ConcurrentHashMap<>();
  private Map<Connection, Transaction> connectionTransactionMap = new ConcurrentHashMap<>();

  /**
   * Constructs a new <code>JdbcDataSourceAutoEnlistWrapper</code>.
   *
   * @param jdbcDataSource the wrapped data source
   */
  public AutoEnlistJdbcDataSource(JdbcDataSource jdbcDataSource)
  {
    this.jdbcDataSource = jdbcDataSource;
  }

  /**
   * Method description
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public Connection getConnection()
    throws SQLException
  {
    try
    {
      TransactionManager transactionManager = TransactionManagerFactory.getTransactionManager();

      if (transactionManager != null)
      {
        Transaction transaction = transactionManager.getTransaction();

        if (transaction != null)
        {
          // If we have already associated a connection with this transaction
          Connection existingConnection = transactionConnectionMap.get(transaction);

          if (existingConnection != null)
          {
            return existingConnection;
          }

          XAConnection xaConnection = jdbcDataSource.getXAConnection();

          transaction.enlistResource(xaConnection.getXAResource());

          Connection connection =
            (Connection) newAutoEnlistConnection(xaConnection.getConnection());

          transactionConnectionMap.put(transaction, connection);
          connectionTransactionMap.put(connection, transaction);

          return connection;
        }
      }

      return jdbcDataSource.getConnection();
    }
    catch (Throwable e)
    {
      throw new SQLException("Failed to retrieve the XAConnection and automatically enlist"
          + " the XAConnection in the current transaction when retrieving the Connection", e);
    }
  }

  /**
   * Method description
   *
   * @param username
   * @param password
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public Connection getConnection(String username, String password)
    throws SQLException
  {
    try
    {
      XAConnection xaConnection = getXAConnection(username, password);

      TransactionManager transactionManager = TransactionManagerFactory.getTransactionManager();

      if (transactionManager != null)
      {
        Transaction transaction = transactionManager.getTransaction();

        if (transaction != null)
        {
          transaction.enlistResource(xaConnection.getXAResource());
        }
      }

      return xaConnection.getConnection();
    }
    catch (Throwable e)
    {
      throw new SQLException("Failed to retrieve the XAConnection and automatically enlist"
          + " the XAConnection in the current transaction when retrieving the Connection", e);
    }
  }

  /**
   * Method description
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public PrintWriter getLogWriter()
    throws SQLException
  {
    return jdbcDataSource.getLogWriter();
  }

  /**
   * Method description
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public int getLoginTimeout()
    throws SQLException
  {
    return jdbcDataSource.getLoginTimeout();
  }

  /**
   * Method description
   *
   * @return
   *
   * @throws SQLFeatureNotSupportedException
   */
  @Override
  public Logger getParentLogger()
    throws SQLFeatureNotSupportedException
  {
    return Logger.getAnonymousLogger();
  }

  /**
   * Method description
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public XAConnection getXAConnection()
    throws SQLException
  {
    return jdbcDataSource.getXAConnection();
  }

  /**
   * Method description
   *
   * @param user
   * @param password
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public XAConnection getXAConnection(String user, String password)
    throws SQLException
  {
    return jdbcDataSource.getXAConnection(user, password);
  }

  /**
   * Method description
   *
   * @param iface
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public boolean isWrapperFor(Class<?> iface)
    throws SQLException
  {
    return jdbcDataSource.isWrapperFor(iface);
  }

  /**
   * Method description
   *
   * @param connection
   *
   * @return
   */
  public Object newAutoEnlistConnection(Connection connection)
  {
    return java.lang.reflect.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class<?>[] { Connection.class }, new AutoEnlistConnection(connection));
  }

  /**
   * Method description
   *
   * @param out
   *
   * @throws SQLException
   */
  @Override
  public void setLogWriter(PrintWriter out)
    throws SQLException
  {
    jdbcDataSource.setLogWriter(out);
  }

  /**
   * Method description
   *
   * @param seconds
   *
   * @throws SQLException
   */
  @Override
  public void setLoginTimeout(int seconds)
    throws SQLException
  {
    jdbcDataSource.setLoginTimeout(seconds);
  }

//@Override
//public PooledConnection getPooledConnection()
//  throws SQLException
//{
//  return jdbcDataSource.getPooledConnection();
//}

//@Override
//public PooledConnection getPooledConnection(String user, String password)
//  throws SQLException
//{
//  return jdbcDataSource.getPooledConnection(user, password);
//}

  /**
   * Method description
   *
   * @param url
   */
  public void setURL(String url)
  {
    jdbcDataSource.setURL(url);
  }

//@Override
//public Reference getReference()
//  throws NamingException
//{
//  return jdbcDataSource.getReference();
//}

  /**
   * Method description
   *
   * @param iface
   * @param <T>
   *
   * @return
   *
   * @throws SQLException
   */
  @Override
  public <T> T unwrap(Class<T> iface)
    throws SQLException
  {
    return jdbcDataSource.unwrap(iface);
  }

  /**
   * The <code>AutoEnlistConnection</code> class.
   *
   *  @author Marcus Portmann
   */
  public class AutoEnlistConnection
    implements java.lang.reflect.InvocationHandler
  {
    private Connection connection;

    private AutoEnlistConnection(Connection connection)
    {
      this.connection = connection;
    }

    /**
     * Method description
     *
     * @param proxy
     * @param method
     * @param args
     *
     * @return
     *
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable
    {
      try
      {
        if (method.getName().equals("close"))
        {
          Transaction transaction = connectionTransactionMap.get(proxy);

          if (transaction != null)
          {
            connectionTransactionMap.remove(proxy);
            transactionConnectionMap.remove(transaction);
          }

          return method.invoke(connection, args);
        }
        else
        {
          return method.invoke(connection, args);
        }
      }
      catch (Throwable e)
      {
        throw new RuntimeException(
            "The AutoEnlistConnection dynamic proxy failed to invoke the method ("
            + method.getName() + ") on the connection (" + connection.toString() + ")", e);
      }

    }
  }
}
