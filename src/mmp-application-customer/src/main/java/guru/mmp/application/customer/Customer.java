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

import java.io.Serializable;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

/**
 * The <class>Customer</class> class holds the information for a customer.
 *
 * @author Marcus Portmann
 */
@Entity
@Table(schema = "MMP", name = "CUSTOMERS")
public class Customer
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the customer.
   */
  @Id
  @Column(name = "ID", nullable = false)
  private UUID id;

  /**
   * The organisation the customer is associated with.
   */
  @OneToOne
  @JoinColumn(name = "ORGANISATION_ID")
  private Organisation organisation;

  /**
   * The name of the customer.
   */
  @Column(name = "NAME", nullable = false, length = 255)
  private String name;

  /**
   * The addresses for the customer.
   */
  @OneToMany(targetEntity = CustomerAddress.class, mappedBy = "customer", fetch = FetchType.EAGER)
  private List<CustomerAddress> addresses;

  /**
   * Constructs a new <code>Customer</code>.
   */
  public Customer() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the obj argument otherwise
   *         <code>false</code>
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    Customer other = (Customer) obj;

    return (id != null) && id.equals(other.id);
  }

  /**
   * Returns the addresses for the customer.
   *
   * @return the addresses for the customer
   */
  public List<CustomerAddress> getAddresses()
  {
    return addresses;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the customer.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the customer
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the customer.
   *
   * @return the name of the customer
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the organisation the customer is associated with.
   *
   * @return the organisation the customer is associated with
   */
  public Organisation getOrganisation()
  {
    return organisation;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode()
  {
    return (id == null)
        ? 0
        : id.hashCode();
  }

  /**
   * Set the addresses for the customer.
   *
   * @param addresses the addresses for the customer
   */
  public void setAddresses(List<CustomerAddress> addresses)
  {
    this.addresses = addresses;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the customer.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the customer
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the customer.
   *
   * @param name the name of the customer
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the organisation the customer is associated with.
   *
   * @param organisation the organisation the customer is associated with
   */
  public void setOrganisation(Organisation organisation)
  {
    this.organisation = organisation;
  }
}
