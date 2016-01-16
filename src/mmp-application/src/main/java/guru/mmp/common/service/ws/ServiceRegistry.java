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

package guru.mmp.common.service.ws;

import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.service.ws.security.WebServiceClientSecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.ws.Service;
import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The <code>ServiceRegistry</code> class provides an implementation of a "Service Registry" which
 * creates JAX-WS web service proxies.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ServiceRegistry
  implements IServiceRegistry
{
  /* Logger */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

  private ConcurrentMap<String, Class<?>> cachedWebServiceClientClasses = new ConcurrentHashMap<>();

  private DataSource dataSource;

  private String getNumberOfServiceRegistryEntriesSQL;

  private String getServiceRegistryEntrySQL;

  /**
   * Constructs a new <code>Service Registry</code>.
   */
  public ServiceRegistry() {}

  /**
   * Returns the <code>DataSource</code> for the <code>Service Registry</code>.
   *
   * @return the <code>DataSource</code> for the <code>Service Registry</code>
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Returns the number of <code>ServiceRegistryEntry</code> instances containing the
   * configuration information for different web services in the database.
   *
   * @return the number of <code>ServiceRegistryEntry</code> instances containing the
   * configuration information for different web services in the database
   *
   * @throws ServiceRegistryException
   */
  public int getNumberOfServiceRegistryEntries()
    throws ServiceRegistryException
  {
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(
        getNumberOfServiceRegistryEntriesSQL))
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
      throw new ServiceRegistryException(
        "Failed to retrieve the number of Service Registry entries: " + e.getMessage(), e);
    }
  }

  /**
   * Returns the web service proxy for the web service with the specified name.
   *
   * @param name             the name used to uniquely identify the web service
   * @param serviceInterface the Java interface for the web service
   * @param <T>              the Java type for the web service interface
   *
   * @return the web service proxy for the web service with the specified name
   *
   * @throws ServiceRegistryException
   */
  public <T> T getServiceProxy(String name, Class<T> serviceInterface)
    throws ServiceRegistryException
  {
    // Retrieve the ServiceRegistryEntry for the web service
    ServiceRegistryEntry serviceRegistryEntry = getServiceRegistryEntry(name);

    if (serviceRegistryEntry == null)
    {
      throw new ServiceRegistryException(
        "Failed to retrieve the service proxy for the web service (" + name + "): A service " +
          "registry entry could not be found for the web service");
    }

    try
    {
      Class<?> serviceClass = getWebServiceClientClass(serviceRegistryEntry.getServiceClass());

      if (serviceRegistryEntry.getSecurityType() == ServiceRegistryEntry.SECURITY_TYPE_NONE)
      {
        return WebServiceClientSecurityHelper.getServiceProxy(serviceClass, serviceInterface,
          serviceRegistryEntry.getWsdlLocation(), serviceRegistryEntry.getEndpoint());
      }
      else if (serviceRegistryEntry.getSecurityType() ==
        ServiceRegistryEntry.SECURITY_TYPE_CLIENT_SSL)
      {
        return WebServiceClientSecurityHelper.getClientSSLServiceProxy(serviceClass,
          serviceInterface, serviceRegistryEntry.getWsdlLocation(),
          serviceRegistryEntry.getEndpoint());
      }
      else if (serviceRegistryEntry.getSecurityType() == ServiceRegistryEntry.SECURITY_TYPE_DIGEST)
      {
        return WebServiceClientSecurityHelper.getDigestAuthenticationServiceProxy(serviceClass,
          serviceInterface, serviceRegistryEntry.getWsdlLocation(),
          serviceRegistryEntry.getEndpoint(), serviceRegistryEntry.getUsername(),
          serviceRegistryEntry.getPassword());
      }
      else if (serviceRegistryEntry.getSecurityType() ==
        ServiceRegistryEntry.SECURITY_TYPE_HTTP_AUTHENTICATION)
      {
        return WebServiceClientSecurityHelper.getHTTPAuthenticationServiceProxy(serviceClass,
          serviceInterface, serviceRegistryEntry.getWsdlLocation(),
          serviceRegistryEntry.getEndpoint(), serviceRegistryEntry.getUsername(),
          serviceRegistryEntry.getPassword());
      }
      else if (serviceRegistryEntry.getSecurityType() ==
        ServiceRegistryEntry.SECURITY_TYPE_WS_SECURITY_USERNAME_TOKEN)
      {
        return WebServiceClientSecurityHelper.getWSSecurityUsernameTokenServiceProxy(serviceClass,
          serviceInterface, serviceRegistryEntry.getWsdlLocation(),
          serviceRegistryEntry.getEndpoint(), serviceRegistryEntry.getUsername(),
          serviceRegistryEntry.getPassword());
      }
      else if (serviceRegistryEntry.getSecurityType() ==
        ServiceRegistryEntry.SECURITY_TYPE_WS_SECURITY_X509_CERTIFICATE)
      {
        return WebServiceClientSecurityHelper.getWSSecurityX509CertificateServiceProxy(serviceClass,
          serviceInterface, serviceRegistryEntry.getWsdlLocation(),
          serviceRegistryEntry.getEndpoint());
      }
      else
      {
        return WebServiceClientSecurityHelper.getServiceProxy(serviceClass, serviceInterface,
          serviceRegistryEntry.getWsdlLocation(), serviceRegistryEntry.getEndpoint());
      }
    }
    catch (Throwable e)
    {
      throw new ServiceRegistryException(
        "Failed to retrieve the service proxy for the web service (" + name + ")", e);
    }
  }

  /**
   * Returns the <code>ServiceRegistryEntry</code> instance containing the configuration information
   * for the web service with the specified name that describes how to connect to the web service
   * or <code>null</code> if an entry with the specified name could not be found.
   *
   * @param name the name used to uniquely identify the web service
   *
   * @return the <code>ServiceRegistryEntry</code> instance containing the configuration information
   * for the web service with the specified name that describes how to connect to the web
   * service or <code>null</code> if an entry with the specified name could not be found
   *
   * @throws ServiceRegistryException
   */
  public ServiceRegistryEntry getServiceRegistryEntry(String name)
    throws ServiceRegistryException
  {
    if ((name == null) || (name.length() == 0))
    {
      throw new ServiceRegistryException(
        "Failed to get the Service Registry entry (" + name + "): The specified name is invalid");
    }

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getServiceRegistryEntrySQL))
    {
      statement.setString(1, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return new ServiceRegistryEntry(rs.getString(1), rs.getInt(2),
            rs.getString(3).equalsIgnoreCase("Y"), rs.getString(4).equalsIgnoreCase("Y"),
            rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new ServiceRegistryException(
        "Failed to get the Service Registry entry (" + name + "): " + e.getMessage(), e);
    }
  }

  /**
   * Initialise the Service Registry.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored)
    {
    }

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored)
      {
      }
    }

    if (dataSource == null)
    {
      throw new ServiceRegistryException(
        "Failed to retrieve the application data source using the JNDI names " +
          "(java:app/jdbc/ApplicationDataSource) and (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Retrieve the database meta data
      String schemaSeparator;
      String idQuote;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }

        // Retrieve the identifier enquoting string for the database
        idQuote = metaData.getIdentifierQuoteString();

        if ((idQuote == null) || (idQuote.length() == 0))
        {
          idQuote = "\"";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = idQuote + DataAccessObject.MMP_DATABASE_SCHEMA + idQuote +
        schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Exception e)
    {
      throw new ServiceRegistryException(
        "Failed to initialise the Service Registry: " + e.getMessage(), e);
    }
    catch (Throwable e)
    {
      throw new ServiceRegistryException(
        "Failed to initialise the Service Registry: " + e.getMessage());
    }
  }

  /**
   * Set the <code>DataSource</code> for the <code>Service Registry</code>.
   *
   * @param dataSource the <code>DataSource</code> for the <code>Service Registry</code>
   */
  public void setDataSource(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Generate the SQL statements.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects
   *
   * @throws SQLException if a database error occurs
   */
  protected void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // getNumberOfServiceRegistryEntriesSQL
    getNumberOfServiceRegistryEntriesSQL = "SELECT COUNT(NAME) FROM " + schemaPrefix +
      "SERVICE_REGISTRY";

    // getServiceRegistryEntrySQL
    getServiceRegistryEntrySQL = "SELECT NAME, SECURITY_TYPE, REQUIRES_USER_TOKEN," + " " +
      "SUPPORTS_COMPRESSION, ENDPOINT, SERVICE_CLASS, WSDL_LOCATION, USERNAME, PASSWORD FROM " +
      schemaPrefix + "SERVICE_REGISTRY WHERE NAME=?";
  }

  /**
   * Returns a connection from the data source associated with the Service Registry.
   *
   * @return a connection from the data source associated with the Service Registry
   *
   * @throws ServiceRegistryException
   */
  private Connection getConnection()
    throws ServiceRegistryException
  {
    try
    {
      return dataSource.getConnection();
    }
    catch (Throwable e)
    {
      throw new ServiceRegistryException(
        "Failed to retrieve a database connection from the data source: " + e.getMessage(), e);
    }
  }

  private Class<?> getWebServiceClientClass(String name)
  {
    Class<?> cachedWebServiceClientClass = cachedWebServiceClientClasses.get(name);

    if (cachedWebServiceClientClass == null)
    {
      try
      {
        cachedWebServiceClientClass = Thread.currentThread().getContextClassLoader().loadClass(
          name);

        if (!Service.class.isAssignableFrom(cachedWebServiceClientClass))
        {
          throw new RuntimeException(
            "The web service client class (" + cachedWebServiceClientClass.getName() + ") does " +
              "not extend the javax.xml.ws.Service class");
        }
        else
        {
          cachedWebServiceClientClasses.put(name, cachedWebServiceClientClass);
        }
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to load the web service client class (" + name + ")", e);
      }
    }

    return cachedWebServiceClientClass;
  }
}
