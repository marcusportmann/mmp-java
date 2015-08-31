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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The <code>Role</code> class stores the information for a security role.
 *
 * @author Marcus Portmann
 */
public class Role
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private List<Set<Principal>> accountsPrincipals;
  private List<Attribute> attributes;
  private Set<Principal> principals;
  private List<Set<Principal>> rolesPrincipals;

  /**
   * Constructs a new <code>Role</code>.
   *
   * @param principals the principals that uniquely identify the role
   * @param attributes the attributes for the role
   */
  public Role(Set<Principal> principals, List<Attribute> attributes)
  {
    this.principals = principals;
    this.attributes = attributes;
    this.accountsPrincipals = new ArrayList<>();
    this.rolesPrincipals = new ArrayList<>();
  }

  /**
   * Constructs a new <code>Role</code>.
   *
   * @param principals         the principals that uniquely identify the role
   * @param attributes         the attributes for the role
   * @param accountsPrincipals the principals for the accounts that have been assigned the role
   * @param rolesPrincipals    the principals for the roles that have been assigned the role
   */
  public Role(Set<Principal> principals, List<Attribute> attributes,
      List<Set<Principal>> accountsPrincipals, List<Set<Principal>> rolesPrincipals)
  {
    this.principals = principals;
    this.attributes = attributes;
    this.accountsPrincipals = accountsPrincipals;
    this.rolesPrincipals = rolesPrincipals;
  }

  /**
   * Private default constructor.
   */
  @SuppressWarnings("unused")
  private Role() {}

  /**
   * Returns the principals for the accounts that have been assigned the role.
   *
   * @return the principals for the accounts that have been assigned the role
   */
  public List<Set<Principal>> getAccountsPrincipals()
  {
    return accountsPrincipals;
  }

  /**
   * Returns the attributes for the role.
   *
   * @return the attributes for the role
   */
  public List<Attribute> getAttributes()
  {
    return attributes;
  }

  /**
   * Returns the principals that uniquely identify the role.
   *
   * @return the principals that uniquely identify the role
   */
  public Set<Principal> getPrincipals()
  {
    return principals;
  }

  /**
   * Returns the principals for the roles that have been assigned the role.
   *
   * @return the principals for the roles that have been assigned the role
   */
  public List<Set<Principal>> getRolesPrincipals()
  {
    return rolesPrincipals;
  }

  /**
   * Sets the principals for the accounts that have been assigned the role.
   *
   * @param accountsPrincipals the principals for the accounts that have been assigned the role
   */
  public void setAccountsPrincipals(List<Set<Principal>> accountsPrincipals)
  {
    this.accountsPrincipals = accountsPrincipals;
  }

  /**
   * Sets the attributes for the role.
   *
   * @param attributes the attributes for the role
   */
  public void setAttributes(List<Attribute> attributes)
  {
    this.attributes = attributes;
  }

  /**
   * Sets the principals that uniquely identify the role.
   *
   * @param principals the principals that uniquely identify the role
   */
  public void setPrincipals(Set<Principal> principals)
  {
    this.principals = principals;
  }

  /**
   * Sets the principals for the roles that have been assigned the role.
   *
   * @param rolesPrincipals the principals for the roles that have been assigned the role
   */
  public void setRolesPrincipals(List<Set<Principal>> rolesPrincipals)
  {
    this.rolesPrincipals = rolesPrincipals;
  }
}
