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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.UUID;

import javax.persistence.*;

/**
 * The <code>CustomerAddress</code> class holds the information for a customer address.
 *
 * @author Marcus Portmann
 */
@Entity
@Table(schema = "MMP", name = "CUSTOMER_ADDRESSES")
public class CustomerAddress
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the customer address.
   */
  @Id
  @Column(name = "ID", nullable = false)
  private UUID id;

  /**
   * The customer the customer address is associated with.
   */
  @Id
  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID", nullable = false)
  private Customer customer;

  /**
   * The name of the customer address.
   */
  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  /**
   * The type of customer address e.g. 0 = Physical Address, 1 = Postal Address, 3 = Billing Address.
   */
  @Convert(converter = CustomerAddressTypeConverter.class)
  @Column(name = "TYPE", nullable = false)
  private CustomerAddressType type;

  /**
   * The first line of the customer address.
   */
  @Column(name = "LINE_1", length = 200)
  private String line1;

  /**
   * The second line of the customer address.
   */
  @Column(name = "LINE_2", length = 200)
  private String line2;

  /**
   * The third line of the customer address.
   */
  @Column(name = "LINE_3", length = 200)
  private String line3;

  /**
   * The city for the customer address
   */
  @Column(name = "CITY", length = 100)
  private String city;

  /**
   * The ISO 3166-2 code for the principal subdivision (province or state) for the address.
   */
  @Column(name = "SUBDIVISION", length = 10)
  private String subDivision;

  /**
   * The ZIP or postal code for the customer address.
   */
  @Column(name = "CODE", length = 30)
  private String code;

  /**
   * The ISO 3166-1 country code for the customer address.
   */
  @Column(name = "COUNTRY", length = 10)
  private String country;

  /**
   * Constructs a new <code>CustomerAddress</code>.
   */
  public CustomerAddress() {}

  /**
   * Constructs a new <code>CustomerAddress</code>.
   *
   * @param customer the customer the customer address is associated with
   */
  public CustomerAddress(Customer customer)
  {
    this.customer = customer;
  }

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

    CustomerAddress other = (CustomerAddress) obj;

    return (id != null) && id.equals(other.id);
  }

  /**
   * Returns the city for the customer address.
   *
   * @return the city for the customer address
   */
  public String getCity()
  {
    return city;
  }

  /**
   * Returns the ZIP or postal code for the customer address.
   *
   * @return the ZIP or postal code for the customer address
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the ISO 3166-1 country code for the customer address.
   *
   * @return the ISO 3166-1 country code for the customer address
   */
  public String getCountry()
  {
    return country;
  }

  /**
   * Returns the first line of the customer address.
   *
   * @return the first line of the customer address
   */
  public String getLine1()
  {
    return line1;
  }

  /**
   * Returns the second line of the customer address.
   *
   * @return the second line of the customer address
   */
  public String getLine2()
  {
    return line2;
  }

  /**
   * Returns the third line of the customer address.
   *
   * @return the third line of the customer address
   */
  public String getLine3()
  {
    return line3;
  }

  /**
   * Returns the name of the customer address.
   *
   * @return the name of the customer address
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the ISO 3166-2 code for the principal subdivision (province or state) for the address.
   *
   * @return the ISO 3166-2 code for the principal subdivision (province or state) for the address
   */
  public String getSubDivision()
  {
    return subDivision;
  }

  /**
   * Returns the type of customer address.
   *
   * @return the type of customer address
   */
  public CustomerAddressType getType()
  {
    return type;
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
   * Set the city for the customer address.
   *
   * @param city the city for the customer address
   */
  public void setCity(String city)
  {
    this.city = city;
  }

  /**
   * Set the ZIP or postal code for the customer address.
   *
   * @param code the ZIP or postal code for the customer address
   */
  public void setCode(String code)
  {
    this.code = code;
  }

  /**
   * Set the ISO 3166-1 country code for the customer address.
   *
   * @param country the ISO 3166-1 country code for the customer address
   */
  public void setCountry(String country)
  {
    this.country = country;
  }

  /**
   * Set the first line of the customer address.
   *
   * @param line1 the first line of the customer address
   */
  public void setLine1(String line1)
  {
    this.line1 = line1;
  }

  /**
   * Set the second line of the customer address.
   *
   * @param line2 the second line of the customer address
   */
  public void setLine2(String line2)
  {
    this.line2 = line2;
  }

  /**
   * Set the third line of the customer address.
   *
   * @param line3 the third line of the customer address
   */
  public void setLine3(String line3)
  {
    this.line3 = line3;
  }

  /**
   * Set the name of the customer address.
   *
   * @param name the name of the customer address
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the ISO 3166-2 code for the principal subdivision (province or state) for the address.
   *
   * @param subDivision the ISO 3166-2 code for the principal subdivision (province or state) for
   *                    the address
   */
  public void setSubDivision(String subDivision)
  {
    this.subDivision = subDivision;
  }

  /**
   * Set the type of customer address.
   *
   * @param type the type of customer address
   */
  public void setType(CustomerAddressType type)
  {
    this.type = type;
  }
}
