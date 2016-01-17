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

package guru.mmp.application.security;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

/**
 * The <code>UserPrincipal</code> class implements a <code>Principal</code> identity for a user
 * identity that authenticates with a username and password.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class UserPrincipal
  implements Principal
{
  private String username;

  /**
   * Constructs a new <code>UsernamePrincipal</code>.
   *
   * @param username the username for the principal identity
   */
  public UserPrincipal(String username)
  {
    this.username = username;
  }

  /**
   * The equals method checks if the specified object is the same principal as this object.
   *
   * @param another the principal to compare with
   *
   * @return true if the object passed in matches the principal represented by the implementation
   * of the <code>Principal</code> interface
   */
  @Override
  public boolean equals(Object another)
  {
    if (another instanceof UserPrincipal)
    {
      if (getClass().cast(another).getUsername().equalsIgnoreCase(username))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Return the name of this principal identity; that is, return the username.
   *
   * @return the name of this principal identity
   */
  public String getName()
  {
    return username;
  }

  /**
   * Returns the username for the principal identity.
   *
   * @return the username for the principal identity
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * The hashCode method returns an integer hash code to represent this principal.
   * It can be used to test for non-equality, or as an index key in a hash table.
   *
   * @return an integer hash code representing the principal
   */
  @Override
  public int hashCode()
  {
    return username.hashCode();
  }

  /**
   * Returns a string representation of this principal.
   *
   * @return a string representation of this principal
   */
  @Override
  public String toString()
  {
    return getClass().getName() + " (" + username + ")";
  }
}
