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

package guru.mmp.common.service.ws;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.service.ws.security.CXFDigestSecurityProxyConfigurator;
import guru.mmp.common.service.ws.security.WSSUsernameTokenSecurityHandlerResolver;
import guru.mmp.common.service.ws.security.WebServiceClientSecurityHandlerResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.net.URL;

import java.sql.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

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
  private DataSource dataSource;
  private String getNumberOfServiceRegistryEntriesSQL;
  private String getServiceRegistryEntrySQL;

  /* The web service client cache. */
  private ConcurrentMap<String, CachedWebServiceClient> webServiceClientCache;

  /**
   * Constructs a new <code>Service Registry</code>.
   */
  public ServiceRegistry() {}

  /**
   * Returns the cached web service client for the <code>ServiceRegistryEntry</code> instance.
   *
   * @param serviceRegistryEntry the <code>ServiceRegistryEntry</code> instance
   *
   * @return the cached web service client for the <code>ServiceRegistryEntry</code> instance
   *
   * @throws ServiceRegistryException
   */
  public CachedWebServiceClient getCachedWebServiceClient(ServiceRegistryEntry serviceRegistryEntry)
    throws ServiceRegistryException
  {
    CachedWebServiceClient cachedWebServiceClient =
      webServiceClientCache.get(serviceRegistryEntry.getName());

    if (cachedWebServiceClient != null)
    {
      return cachedWebServiceClient;
    }

    try
    {
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

      // Retrieve the WSDL for the web service as a resource from the classpath
      URL wsdlLocation = contextClassLoader.getResource(serviceRegistryEntry.getWsdlLocation());

      if (wsdlLocation == null)
      {
        throw new ServiceRegistryException("Failed to retrieve the WSDL for the web service ("
            + serviceRegistryEntry.getWsdlLocation() + ") as a resource from the classpath");
      }

      // Retrieve the Java service class for the web service
      Class<?> clazz = contextClassLoader.loadClass(serviceRegistryEntry.getServiceClass());

      /*
       * Retrieve the @WebServiceClient annotation from the service class to determine the qname
       * for the web service.
       */
      WebServiceClient webServiceClientAnnotation = clazz.getAnnotation(WebServiceClient.class);

      if (webServiceClientAnnotation == null)
      {
        throw new ServiceRegistryException(
            "Failed to retrieve the @WebServiceClient annotation from the service class ("
            + serviceRegistryEntry.getServiceClass() + ")");
      }

      Constructor<?> constructor = clazz.getConstructor(URL.class, QName.class);

      QName serviceQName = new QName(webServiceClientAnnotation.targetNamespace(),
        webServiceClientAnnotation.name());

      Object serviceObject = constructor.newInstance(wsdlLocation, serviceQName);

      if (serviceObject instanceof javax.xml.ws.Service)
      {
        Service service = (Service) serviceObject;

        // Setup the JAX-WS handler that implements the required Web Service Security model
        if (serviceRegistryEntry.getSecurityType()
            == ServiceRegistryEntry.SECURITY_TYPE_WSS_SECURITY_X509_CERTIFICATES)
        {
          service.setHandlerResolver(new WebServiceClientSecurityHandlerResolver());
        }
        else if (serviceRegistryEntry.getSecurityType()
            == ServiceRegistryEntry.SECURITY_TYPE_WSS_SECURITY_USERNAME_TOKEN)
        {
          service.setHandlerResolver(
              new WSSUsernameTokenSecurityHandlerResolver(
                serviceRegistryEntry.getUsername(), serviceRegistryEntry.getPassword()));
        }

        /*
         * Find the method with no parameters annotated with @WebEndpoint.
         *
         * Then use the value of the "name" parameter on this annotation to determine the port name.
         */
        String portName = null;

        for (Method method : clazz.getMethods())
        {
          WebEndpoint webEndpointAnnotation = method.getAnnotation(WebEndpoint.class);

          if (webEndpointAnnotation != null)
          {
            portName = webEndpointAnnotation.name();

            break;
          }
        }

        if (portName == null)
        {
          throw new ServiceRegistryException("Failed to determine the port name using the"
              + " @WebEndpoint annotation on one of the methods on the service class ("
              + serviceRegistryEntry.getServiceClass() + ")");
        }

        QName portQName = new QName(webServiceClientAnnotation.targetNamespace(), portName);

        cachedWebServiceClient = new CachedWebServiceClient(portQName, service);

        webServiceClientCache.put(serviceRegistryEntry.getName(), cachedWebServiceClient);

        return cachedWebServiceClient;
      }
      else
      {
        throw new ServiceRegistryException("The service class ("
            + serviceRegistryEntry.getServiceClass()
            + ") is not derived from javax.xml.ws.Service");
      }
    }
    catch (Throwable e)
    {
      throw new ServiceRegistryException(
          "Failed to retrieve the cached web service client for the web service ("
          + serviceRegistryEntry.getName() + ")", e);
    }
  }

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
   *         configuration information for different web services in the database
   *
   * @throws ServiceRegistryException
   */
  public int getNumberOfServiceRegistryEntries()
    throws ServiceRegistryException
  {
    // Store the value in the database
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = getConnection();

      statement = connection.prepareStatement(getNumberOfServiceRegistryEntriesSQL);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getInt(1);
      }
      else
      {
        return 0;
      }
    }
    catch (Throwable e)
    {
      throw new ServiceRegistryException(
          "Failed to retrieve the number of Service Registry entries: " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Returns the web service proxy for the web service with the specified name.
   *
   * @param name                     the name used to uniquely identify the web service
   * @param serviceEndpointInterface the Java interface for the service endpoint
   * @param <T>                      the Java type for the service endpoint interface
   *
   * @return the web service proxy for the web service with the specified name
   *
   * @throws ServiceRegistryException
   */
  public <T> T getServiceProxy(String name, Class<T> serviceEndpointInterface)
    throws ServiceRegistryException
  {
    // Retrieve the ServiceRegistryEntry for the web service
    ServiceRegistryEntry serviceRegistryEntry = getServiceRegistryEntry(name);

    if (serviceRegistryEntry == null)
    {
      throw new ServiceRegistryException(
          "Failed to retrieve the service proxy for the web service (" + name
          + "): A service registry entry could not be found for the web service");
    }

    try
    {
      CachedWebServiceClient cachedWebServiceClient =
        getCachedWebServiceClient(serviceRegistryEntry);

      T proxy = cachedWebServiceClient.getService().getPort(cachedWebServiceClient.getPortQName(),
        serviceEndpointInterface);

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) proxy);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
          serviceRegistryEntry.getEndpoint());

      // Setup HTTP authentication
      if (serviceRegistryEntry.getSecurityType()
          == ServiceRegistryEntry.SECURITY_TYPE_HTTP_AUTHENTICATION)
      {
        bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
            serviceRegistryEntry.getUsername());
        bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
            serviceRegistryEntry.getPassword());
      }
      else if (serviceRegistryEntry.getSecurityType() == ServiceRegistryEntry.SECURITY_TYPE_DIGEST)
      {
        CXFDigestSecurityProxyConfigurator.configureProxy(proxy,
            serviceRegistryEntry.getUsername(), serviceRegistryEntry.getPassword());
      }

      return proxy;
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
   *         for the web service with the specified name that describes how to connect to the web
   *         service or <code>null</code> if an entry with the specified name could not be found
   *
   * @throws ServiceRegistryException
   */
  public ServiceRegistryEntry getServiceRegistryEntry(String name)
    throws ServiceRegistryException
  {
    if ((name == null) || (name.length() == 0))
    {
      throw new ServiceRegistryException("Failed to get the Service Registry entry (" + name
          + "): The specified name is invalid");
    }

    // Store the value in the database
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = getConnection();

      statement = connection.prepareStatement(getServiceRegistryEntrySQL);
      statement.setString(1, name);
      rs = statement.executeQuery();

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
    catch (Throwable e)
    {
      throw new ServiceRegistryException("Failed to get the Service Registry entry (" + name
          + "): " + e.getMessage(), e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Initialise the Service Registry.
   */
  @PostConstruct
  public void init()
  {
    webServiceClientCache = new ConcurrentHashMap<>();

    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      throw new DAOException("Failed to initialise the Service Registry:"
          + " Failed to retrieve the data source using the JNDI lookup"
          + " (java:app/jdbc/ApplicationDataSource)");
    }

    Connection connection = null;

    try
    {
      // Retrieve the database meta data
      String schemaSeparator = ".";
      String idQuote = "\"";

      try
      {
        connection = dataSource.getConnection();

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
      finally
      {
        DAOUtil.close(connection);
      }

      // Determine the schema prefix
      String schemaPrefix = idQuote + DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA
        + idQuote + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Exception e)
    {
      throw new ServiceRegistryException("Failed to initialise the Service Registry: "
          + e.getMessage(), e);
    }
    catch (Throwable e)
    {
      throw new ServiceRegistryException("Failed to initialise the Service Registry: "
          + e.getMessage());
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
   * Generate the SQL statements for the <code>Service Registry</code>.
   *
   * @param schemaPrefix the schema prefix to append to database objects reference by the
   *                     <code>Service Registry</code>
   *
   * @throws SQLException if a database error occurs
   */
  protected void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // getNumberOfServiceRegistryEntriesSQL
    getNumberOfServiceRegistryEntriesSQL = "SELECT COUNT(NAME) FROM " + schemaPrefix
        + "SERVICE_REGISTRY";

    // getServiceRegistryEntrySQL
    getServiceRegistryEntrySQL = "SELECT NAME, SECURITY_TYPE, REQUIRES_USER_TOKEN,"
        + " SUPPORTS_COMPRESSION, ENDPOINT, SERVICE_CLASS, WSDL_LOCATION, USERNAME, PASSWORD FROM "
        + schemaPrefix + "SERVICE_REGISTRY WHERE NAME=?";
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

  /**
   * The <code>CachedWebServiceClient</code> class stores the cached information for a web service
   * client.
   *
   * @author Marcus Portmann
   */
  public class CachedWebServiceClient
  {
    /**
     * The QName for the port.
     */
    private QName portQName;

    /**
     * The web service client.
     */
    private Service service;

    /**
     * Constructs a new <code>CachedWebServiceClient</code>.
     *
     * @param portQName the QName for the port
     * @param service   the web service client
     */
    public CachedWebServiceClient(QName portQName, Service service)
    {
      this.portQName = portQName;
      this.service = service;
    }

    /**
     * Returns the QName for the port.
     *
     * @return the QName for the port
     */
    public QName getPortQName()
    {
      return portQName;
    }

    /**
     * Returns the web service client.
     *
     * @return the web service client
     */
    public Service getService()
    {
      return service;
    }

    /**
     * Set the QName for the port.
     *
     * @param portQName the QName for the port
     */
    public void setPortQName(QName portQName)
    {
      this.portQName = portQName;
    }

    /**
     * Set the web service client.
     *
     * @param service the web service client
     */
    public void setService(Service service)
    {
      this.service = service;
    }
  }
}
