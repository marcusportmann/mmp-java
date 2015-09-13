/*
 * Copyright 2015 Marcus Portmann
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

import java.security.Principal;

import java.util.*;

/**
 * The <code>Account</code> class stores the information for a security account.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class Account
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private boolean accountLocked;
  private List<Attribute> attributes;
  private boolean credentialExpirySupported;
  private List<Credential> credentials;
  private boolean credentialsExpired;
  private java.util.Date credentialsExpiryTime;
  private boolean lockoutSupported;
  private Set<Principal> principals;

  /**
   * Constructs a new <code>Account</code>.
   */
  public Account() {}

  /**
   * Add the new attribute to the account.
   *
   * @param attribute the attribute to add to the account
   */
  public void addAttribute(Attribute attribute)
  {
    if (attributes == null)
    {
      attributes = new ArrayList<>();
    }

    attributes.add(attribute);
  }

  /**
   * Returns the attribute with the specified name or <code>null</code> if the attribute could not
   * be found.
   *
   * @param name the name of the attribute
   *
   * @return the attribute with the specified name or <code>null</code> if the attribute could not
   *         be found
   */
  public Attribute getAttribute(String name)
  {
    if (attributes == null)
    {
      return null;
    }

    for (Attribute attribute : attributes)
    {
      if (attribute.getName().equalsIgnoreCase(name))
      {
        return attribute;
      }
    }

    return null;
  }

  /**
   * Returns the attributes for the account.
   *
   * @return the attributes for the account
   */
  public List<Attribute> getAttributes()
  {
    if (attributes == null)
    {
      attributes = new ArrayList<>();
    }

    return attributes;
  }

  /**
   * Returns the credentials for the account.
   *
   * @return the credentials for the account
   */
  public List<Credential> getCredentials()
  {
    if (credentials == null)
    {
      credentials = new ArrayList<>();
    }

    return credentials;
  }

  /**
   * Returns the date and time that the account's credentials will expire.
   *
   * @return the date and time that the account's credentials will expire
   */
  public Date getCredentialsExpiryTime()
  {
    return credentialsExpiryTime;
  }

  /**
   * Returns the principals for the account.
   *
   * @return the principals for the account
   */
  public Set<Principal> getPrincipals()
  {
    if (principals == null)
    {
      principals = new HashSet<>();
    }

    return principals;
  }

  /**
   * Returns true if the account is locked or false otherwise.
   *
   * @return true if the account is locked or false otherwise
   */
  public boolean isAccountLocked()
  {
    return accountLocked;
  }

  /**
   * Returns true if credential expiry is supported for the account or false otherwise.
   *
   * @return true if credential expiry is supported for the account or false otherwise
   */
  public boolean isCredentialExpirySupported()
  {
    return credentialExpirySupported;
  }

  /**
   * Returns true if the accounts credentials have expired or false otherwise.
   *
   * @return true if the accounts credentials have expired or false otherwise
   */
  public boolean isCredentialsExpired()
  {
    return credentialsExpired;
  }

  /**
   * Returns true if account lockout is supported for the account or false otherwise.
   *
   * @return true if account lockout is supported for the account or false otherwise
   */
  public boolean isLockoutSupported()
  {
    return lockoutSupported;
  }

  /**
   * Set whether the account is locked.
   *
   * @param accountLocked is the account locked
   */
  public void setAccountLocked(boolean accountLocked)
  {
    this.accountLocked = accountLocked;
  }

  /**
   * Set the attributes for the account.
   *
   * @param attributes the attributes for the account
   */
  public void setAttributes(List<Attribute> attributes)
  {
    this.attributes = attributes;
  }

  /**
   * Set whether the account supports credential expiry.
   *
   * @param credentialExpirySupported whether the account supports credential expiry
   */
  public void setCredentialExpirySupported(boolean credentialExpirySupported)
  {
    this.credentialExpirySupported = credentialExpirySupported;
  }

  /**
   * Set the credentials for the account.
   *
   * @param credentials the credentials for the account
   */
  public void setCredentials(List<Credential> credentials)
  {
    this.credentials = credentials;
  }

  /**
   * Set whether the account's credentials have expired.
   *
   * @param credentialsExpired <code>true</code> if the account's credentials have expired or
   *                           <code>false</code> otherwise
   */
  public void setCredentialsExpired(boolean credentialsExpired)
  {
    this.credentialsExpired = credentialsExpired;
  }

  /**
   * Set the date and time when the account's credentials will expire.
   *
   * @param credentialsExpiryTime the date and time when the account's credentials will expire
   */
  public void setCredentialsExpiryTime(Date credentialsExpiryTime)
  {
    this.credentialsExpiryTime = credentialsExpiryTime;
  }

  /**
   * Set whether account lockout is supported.
   *
   * @param lockoutSupported whether account lockout is supported
   */
  public void setLockoutSupported(boolean lockoutSupported)
  {
    this.lockoutSupported = lockoutSupported;
  }

  /**
   * Set the principals for the account.
   *
   * @param principals the principals for the account
   */
  public void setPrincipals(Set<Principal> principals)
  {
    this.principals = principals;
  }
}
