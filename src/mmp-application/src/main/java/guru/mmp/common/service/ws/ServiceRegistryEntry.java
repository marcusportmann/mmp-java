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

package guru.mmp.common.service.ws;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * The <code>ServiceRegistryEntry</code> class stores the configuration information for a web
 * service that describes how to connect to the web service.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ServiceRegistryEntry
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The constant indicating that the web service implements the Client SSL security model.
   */
  public static final int SECURITY_TYPE_CLIENT_SSL = 3;

  /**
   * The constant indicating that the web service implements the HTTP authentication security model.
   */
  public static final int SECURITY_TYPE_DIGEST = 5;

  /**
   * The constant indicating that the web service implements the HTTP authentication security model.
   */
  public static final int SECURITY_TYPE_HTTP_AUTHENTICATION = 4;

  /**
   * The constant indicating that the web service does not implement a security model.
   */
  public static final int SECURITY_TYPE_NONE = 0;

  /**
   * The constant indicating that the web service implements the WS-Security Username Token
   * security model.
   */
  public static final int SECURITY_TYPE_WS_SECURITY_USERNAME_TOKEN = 2;

  /**
   * The constant indicating that the web service implements the WS-Security X509 certificates
   * security model.
   */
  public static final int SECURITY_TYPE_WS_SECURITY_X509_CERTIFICATE = 1;

  /**
   * The endpoint for the web service.
   */
  private String endpoint;

  /**
   * The name used to uniquely identify the web service.
   */
  private String name;

  /**
   * The password to use when accessing a web service with username-password security enabled.
   */
  private String password;

  /**
   * Does the web service require a user security token?
   */
  private boolean requiresUserToken;

  /**
   * The type of security model implemented by the web service i.e. 0 = None, 1 = WS-Security,
   * 2 = Client SSL, 3 = Username-Password.
   */
  private int securityType;

  /**
   * The fully qualified name of the Java web service client class.
   */
  private String serviceClass;

  /**
   * Does the web service support compression?
   */
  private boolean supportsCompression;

  /**
   * The username to use when accessing a web service with username-password security enabled.
   */
  private String username;

  /**
   * The location of the WSDL defining the web service on the classpath.
   */
  private String wsdlLocation;

  /**
   * Constructs a new <code>ServiceRegistryEntry</code>.
   */
  public ServiceRegistryEntry() {}

  /**
   * Constructs a new <code>ServiceRegistryEntry</code>.
   *
   * @param name                the name used to uniquely identify the web service
   * @param securityType        the type of security model implemented by the web service i.e.
   *                            0 = None, 1 = WS-Security, 2 = Client SSL, 3 = Username-Passord
   * @param requiresUserToken   <code>true</code> if the web service requires a user security token
   *                            or <code>false</code> otherwise
   * @param supportsCompression <code>true</code> if the web service supports compression or
   *                            <code>false</code> otherwise
   * @param endpoint            the endpoint for the web service
   * @param serviceClass        the fully qualified name of the Java web service client class
   * @param wsdlLocation        the location of the WSDL defining the web service on the classpath
   */
  public ServiceRegistryEntry(String name, int securityType, boolean requiresUserToken,
      boolean supportsCompression, String endpoint, String serviceClass, String wsdlLocation)
  {
    this.name = name;
    this.securityType = securityType;
    this.requiresUserToken = requiresUserToken;
    this.supportsCompression = supportsCompression;
    this.endpoint = endpoint;
    this.serviceClass = serviceClass;
    this.wsdlLocation = wsdlLocation;
  }

  /**
   * Constructs a new <code>ServiceRegistryEntry</code>.
   *
   * @param name                the name used to uniquely identify the web service
   * @param securityType        the type of security model implemented by the web service i.e.
   *                            0 = None, 1 = WS-Security, 2 = Client SSL, 3 = Username-Passord
   * @param requiresUserToken   <code>true</code> if the web service requires a user security token
   *                            or <code>false</code> otherwise
   * @param supportsCompression <code>true</code> if the web service supports compression or
   *                            <code>false</code> otherwise
   * @param endpoint            the endpoint for the web service
   * @param serviceClass        the fully qualified name of the Java web service client class
   * @param wsdlLocation        the location of the WSDL defining the web service on the classpath
   * @param username            the username to use when accessing a web service with
   *                            username-password security enabled
   * @param password            the password to use when accessing a web service with
   *                            username-password security enabled
   */
  public ServiceRegistryEntry(String name, int securityType, boolean requiresUserToken,
      boolean supportsCompression, String endpoint, String serviceClass, String wsdlLocation,
      String username, String password)
  {
    this.name = name;
    this.securityType = securityType;
    this.requiresUserToken = requiresUserToken;
    this.supportsCompression = supportsCompression;
    this.endpoint = endpoint;
    this.serviceClass = serviceClass;
    this.wsdlLocation = wsdlLocation;
    this.username = username;
    this.password = password;
  }

  /**
   * Returns the endpoint for the web service.
   *
   * @return the endpoint for the web service
   */
  public String getEndpoint()
  {
    return endpoint;
  }

  /**
   * Returns the name used to uniquely identify the web service.
   *
   * @return the name used to uniquely identify the web service
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the password to use when accessing a web service with username-password security
   * enabled.
   *
   * @return the password to use when accessing a web service with username-password security
   * enabled
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Returns <code>true</code> if the web service requires a user security token or
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if the web service requires a user security token or
   *         <code>false</code> otherwise
   */
  public boolean getRequiresSecurityToken()
  {
    return requiresUserToken;
  }

  /**
   * Returns the type of security model implemented by the web service i.e. 0 = None,
   * 1 = WS-Security, 2 = Client SSL, 3 = Username-Password.
   *
   * @return the type of security model implemented by the web service i.e. 0 = None,
   * 1 = WS-Security, 2 = Client SSL, 3 = Username-Password
   */
  public int getSecurityType()
  {
    return securityType;
  }

  /**
   * Returns the fully qualified name of the Java web service client class.
   *
   * @return the fully qualified name of the Java web service client class
   */
  public String getServiceClass()
  {
    return serviceClass;
  }

  /**
   * Returns <code>true</code> if the web service supports compression or <code>false</code>
   * otherwise.
   *
   * @return <code>true</code> if the web service supports compression or <code>false</code>
   *         otherwise
   */
  public boolean getSupportsCompression()
  {
    return supportsCompression;
  }

  /**
   * Returns the username to use when accessing a web service with username-password security
   * enabled.
   *
   * @return the username to use when accessing a web service with username-password security
   *         enabled
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Returns the location of the WSDL defining the web service on the classpath.
   *
   * @return the location of the WSDL defining the web service on the classpath
   */
  public String getWsdlLocation()
  {
    return wsdlLocation;
  }

  /**
   * Set the endpoint for the web service.
   *
   * @param endpoint the endpoint for the web service
   */
  public void setEndpoint(String endpoint)
  {
    this.endpoint = endpoint;
  }

  /**
   * Set the name used to uniquely identify the web service.
   *
   * @param name the name used to uniquely identify the web service
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the password to use when accessing a web service with username-password security enabled.
   *
   * @param password the password to use when accessing a web service with username-password
   *                 security enabled
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * See whether the web service requires a user security token.
   *
   * @param requiresSecurityToken <code>true</code> if the web service requires a user security
   *                              token or <code>false</code> otherwise
   */
  public void setRequiresSecurityToken(boolean requiresSecurityToken)
  {
    this.requiresUserToken = requiresSecurityToken;
  }

  /**
   * Set the type of security model implemented by the web service i.e. 0 = None, 1 = WS-Security,
   * 2 = Client SSL, 3 = Username-Password.
   *
   * @param securityType the type of security model implemented by the web service i.e. 0 = None,
   *                     1 = WS-Security, 2 = Client SSL, 3 = Username-Password
   */
  public void setSecurityType(int securityType)
  {
    this.securityType = securityType;
  }

  /**
   * Set the fully qualified name of the Java web service client class.
   *
   * @param serviceClass the fully qualified name of the Java web service client class
   */
  public void setServiceClass(String serviceClass)
  {
    this.serviceClass = serviceClass;
  }

  /**
   * Set whether the web service supports compression.
   *
   * @param supportsCompression <code>true</code> if the web service supports compression or
   *                            <code>false</code> otherwise
   */
  public void setSupportsCompression(boolean supportsCompression)
  {
    this.supportsCompression = supportsCompression;
  }

  /**
   * Set the username to use when accessing a web service with username-password security enabled.
   *
   * @param username the username to use when accessing a web service with username-password
   *                 security enabled
   */
  public void setUsername(String username)
  {
    this.username = username;
  }

  /**
   * Set the location of the WSDL defining the web service on the classpath.
   *
   * @param wsdlLocation the location of the WSDL defining the web service on the classpath
   */
  public void setWsdlLocation(String wsdlLocation)
  {
    this.wsdlLocation = wsdlLocation;
  }
}
