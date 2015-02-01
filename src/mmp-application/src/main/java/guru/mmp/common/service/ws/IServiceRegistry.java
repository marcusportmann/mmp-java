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

/**
 * The <code>IServiceRegistry</code> interface defines the functionality provided by a
 * "Service Registry" which creates JAX-WS web service proxies.
 *
 * @author Marcus Portmann
 */
public interface IServiceRegistry
{
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
    throws ServiceRegistryException;

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
    throws ServiceRegistryException;

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
    throws ServiceRegistryException;
}
