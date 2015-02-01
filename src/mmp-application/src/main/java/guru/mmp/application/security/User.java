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

package guru.mmp.application.security;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.HashMap;

/**
 * The <code>User</code> class stores the information for a user.
 *
 * @author Marcus Portmann
 */
public class User
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String description;
  private String email;
  private String faxNumber;
  private String firstNames;
  private long id;
  private String lastName;
  private String mobileNumber;
  private String password;
  private Integer passwordAttempts;
  private Date passwordExpiry;
  private String phoneNumber;
  private HashMap<String, String> properties;
  private String title;
  private String username;

  /**
   * Constructs a new <code>User</code>.
   */
  public User()
  {
    this.properties = new HashMap<>();
  }

  /**
   * Constructs a new <code>User</code>.
   *
   * @param username the username uniquely identifying the user
   */
  public User(String username)
  {
    this.username = username;
    this.properties = new HashMap<>();
  }

  /**
   * Returns the description for the user
   *
   * @return the description for the user
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the e-mail address for the user
   *
   * @return the e-mail address for the user
   */
  public String getEmail()
  {
    return email;
  }

  /**
   * Returns the fax number for the user
   *
   * @return the fax number for the user
   */
  public String getFaxNumber()
  {
    return faxNumber;
  }

  /**
   * Returns the first name(s) for the user
   *
   * @return the first name(s) for the user
   */
  public String getFirstNames()
  {
    return firstNames;
  }

  /**
   * Returns the unique numeric ID for the user.
   *
   * @return the unique numeric ID for the user
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the last name for the user
   *
   * @return the last name for the user
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * Returns the mobile number for the user
   *
   * @return the mobile number for the user
   */
  public String getMobileNumber()
  {
    return mobileNumber;
  }

  /**
   * Returns the password hash for the user
   *
   * @return the password hash for the user
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Returns the number of failed authentication attempts as a result of an incorrect password for
   * the user
   *
   * @return the number of failed authentication attempts as a result of an incorrect password for
   *         the user
   */
  public Integer getPasswordAttempts()
  {
    return passwordAttempts;
  }

  /**
   * Returns the date and time the password for the user expires
   *
   * @return the date and time the password for the user expires
   */
  public Date getPasswordExpiry()
  {
    return passwordExpiry;
  }

  /**
   * Returns the phone number for the user
   *
   * @return the phone number for the user
   */
  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  /**
   * Returns the value of the user property with the specified name or <code>null</code> if the
   * user property does not exist.
   *
   * @param name the name of the user property
   *
   * @return the value of the user property with the specified name or <code>null</code> if the
   *         user property does not exist
   */
  public String getProperty(String name)
  {
    return properties.get(name);
  }

  /**
   * Returns the title for the user
   *
   * @return the title for the user
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Returns the username for the user.
   *
   * @return the username for the user
   */
  public String getUsername()
  {
    if (username != null)
    {
      username = username.toLowerCase();
    }

    return username;
  }

  /**
   * Returns <code>true</code> if the user has a property with the specified name or
   * <code>false</code> otherwise.
   *
   * @param name the name of the user property
   *
   * @return <code>true</code> if the user has a property with the specified name or
   *         <code>false</code> otherwise
   */
  public boolean hasProperty(String name)
  {
    return properties.containsKey(name);
  }

  /**
   * Set the description for the user.
   *
   * @param description the description for the user
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the e-mail address for the user.
   *
   * @param email the e-mail address for the user
   */
  public void setEmail(String email)
  {
    this.email = email;
  }

  /**
   * Set the fax number for the user.
   *
   * @param faxNumber the fax number for the user
   */
  public void setFaxNumber(String faxNumber)
  {
    this.faxNumber = faxNumber;
  }

  /**
   * Set the first name(s) for the user.
   *
   * @param firstNames the first name(s) for the user
   */
  public void setFirstNames(String firstNames)
  {
    this.firstNames = firstNames;
  }

  /**
   * Set the unique numeric ID for the user.
   *
   * @param id the unique numeric ID for the user
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the last name for the user.
   *
   * @param lastName the last name for the user
   */
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  /**
   * Set the mobile number for the user.
   *
   * @param mobileNumber the mobile number for the user
   */
  public void setMobileNumber(String mobileNumber)
  {
    this.mobileNumber = mobileNumber;
  }

  /**
   * Set the password for the user.
   *
   * @param password the password for the user
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Set the password attempts for the user.
   *
   * @param passwordAttempts the password attempts for the user
   */
  public void setPasswordAttempts(int passwordAttempts)
  {
    this.passwordAttempts = passwordAttempts;
  }

  /**
   * Set the password expiry for the user
   *
   * @param passwordExpiry the password expiry for the user
   */
  public void setPasswordExpiry(Date passwordExpiry)
  {
    this.passwordExpiry = passwordExpiry;
  }

  /**
   * Set the phone number for the user.
   *
   * @param phoneNumber the phone number for the user
   */
  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Set the value of the user property.
   *
   * @param name  the name of the user property
   * @param value the value of the user property
   */
  public void setProperty(String name, String value)
  {
    properties.put(name, value);
  }

  /**
   * Set the title for the user.
   *
   * @param title the title for the user
   */
  public void setTitle(String title)
  {
    this.title = title;
  }
}
