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

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.transaction.Transactional;

/**
 * The <code>ICustomerService</code> interface defines the functionality that must be
 * provided by an Customer Service implementation.
 *
 * @author Marcus Portmann
 */
public interface ICustomerService
{
  /**
   * Create the new customer.
   *
   * @param customer the <code>Customer</code> instance containing the information for the new
   *                 customer
   *
   * @throws CustomerServiceException
   */
  void createCustomer(Customer customer)
    throws CustomerServiceException;

  /**
   * Create the new organisation.
   *
   * @param organisation the <code>Organisation</code> instance containing the information for the
   *                     new organisation
   *
   * @throws CustomerServiceException
   */
  void createOrganisation(Organisation organisation)
    throws CustomerServiceException;

  /**
   * Retrieve the customer.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the customer
   *
   * @return the customer or <code>null</code> if the customer could not be found
   *
   * @throws CustomerServiceException
   */
  Customer getCustomer(UUID id)
    throws CustomerServiceException;

  /**
   * Retrieve the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   *
   * @return the organisation or <code>null</code> if the organisation could not be found
   *
   * @throws CustomerServiceException
   */
  Organisation getOrganisation(UUID id)
    throws CustomerServiceException;

  /**
   * Update the customer.
   *
   * @param customer the <code>Customer</code> instance containing the customer information
   *
   * @return the updated customer
   *
   * @throws CustomerServiceException
   */
  Customer updateCustomer(Customer customer)
    throws CustomerServiceException;

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
  Organisation updateOrganisation(Organisation organisation)
    throws CustomerServiceException;
}
