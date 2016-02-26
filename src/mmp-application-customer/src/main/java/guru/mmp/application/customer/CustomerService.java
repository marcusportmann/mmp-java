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

package guru.mmp.application.customer;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.Organisation;
import guru.mmp.application.util.ServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.transaction.Transactional;

/**
 * The <code>CustomerService</code> class provides the Customer Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class CustomerService
  implements ICustomerService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  /* The name of the Customer Service instance. */
  private String instanceName = ServiceUtil.getServiceInstanceName("Customer Service");

  /* Entity Manager */
  @PersistenceContext(unitName = "Customer")
  private EntityManager entityManager;

  /**
   * Constructs a new <code>CustomerService</code>.
   */
  public CustomerService() {}

  /**
   * Create the new customer.
   *
   * @param customer the <code>Customer</code> instance containing the information for the new
   *                 customer
   *
   * @throws CustomerServiceException
   */
  @Transactional
  public void createCustomer(Customer customer)
    throws CustomerServiceException
  {
    try
    {
      entityManager.persist(customer);
      entityManager.flush();
      entityManager.detach(customer);
    }
    catch (Throwable e)
    {
      throw new CustomerServiceException("Failed to create the customer (" + customer.getId()
        + ")", e);
    }
  }

  /**
   * Create the new organisation.
   *
   * @param organisation the <code>Organisation</code> instance containing the information for the
   *                     new organisation
   *
   * @throws CustomerServiceException
   */
  @Transactional
  public void createOrganisation(Organisation organisation)
    throws CustomerServiceException
  {
    try
    {
      entityManager.persist(organisation);
      entityManager.flush();
      entityManager.detach(organisation);
    }
    catch (Throwable e)
    {
      throw new CustomerServiceException("Failed to create the organisation ("
        + organisation.getId() + ")", e);
    }
  }

  /**
   * Retrieve the customer.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the customer
   *
   * @return the customer or <code>null</code> if the customer could not be found
   *
   * @throws CustomerServiceException
   */
  public Customer getCustomer(UUID id)
    throws CustomerServiceException
  {
    try
    {
      return entityManager.find(Customer.class, id);
    }
    catch (Throwable e)
    {
      throw new CustomerServiceException("Failed to retrieve the organisation (" + id + ")", e);
    }
  }

  /**
   * Retrieve the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   *
   * @return the organisation or <code>null</code> if the organisation could not be found
   *
   * @throws CustomerServiceException
   */
  public Organisation getOrganisation(UUID id)
    throws CustomerServiceException
  {
    try
    {
      return entityManager.find(Organisation.class, id);
    }
    catch (Throwable e)
    {
      throw new CustomerServiceException("Failed to retrieve the organisation (" + id + ")", e);
    }
  }

  /**
   * Initialise the Customer Service instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Customer Service (" + instanceName + ")");

    try
    {
      // Initialise the configuration for the Customer Service instance
      initConfiguration();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Customer Service", e);
    }
  }

  /**
   * Update the customer.
   *
   * @param customer the <code>Customer</code> instance containing the customer information
   *
   * @return the updated customer
   *
   * @throws CustomerServiceException
   */
  public Customer updateCustomer(Customer customer)
    throws CustomerServiceException
  {
    try
    {
      Customer updatedCustomer = entityManager.merge(customer);
      entityManager.detach(updatedCustomer);

      return updatedCustomer;
    }
    catch (Throwable e)
    {
      throw new CustomerServiceException("Failed to update the customer (" + customer.getId()
        + ")", e);
    }
  }

  /**
   * Update the organisation.
   *
   * @param organisation the <code>Organisation</code> instance containing the organisation
   *                     information
   *
   * @return the updated organisation
   *
   * @throws CustomerServiceException
   */
  public Organisation updateOrganisation(Organisation organisation)
    throws CustomerServiceException
  {
    try
    {
      Organisation updatedOrganisation = entityManager.merge(organisation);
      entityManager.detach(updatedOrganisation);

      return updatedOrganisation;
    }
    catch (Throwable e)
    {
      throw new CustomerServiceException("Failed to update the organisation ("
        + organisation.getId() + ")", e);
    }
  }

  /**
   * Initialise the configuration for the Customer Service instance.
   *
   * @throws CustomerServiceException
   */
  private void initConfiguration()
    throws CustomerServiceException
  {
    try {}
    catch (Throwable e)
    {
      throw new CustomerServiceException(
        "Failed to initialise the configuration for the Customer Service", e);
    }
  }
}
