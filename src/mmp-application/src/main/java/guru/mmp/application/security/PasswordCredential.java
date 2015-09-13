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

/**
 * The <code>PasswordCredential</code> class provides a concrete implementation of a
 * <code>Credential</code> that is used to store a password.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class PasswordCredential
  implements Credential, java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String password;

  /**
   * Constructs a new <code>PasswordCredential</code>.
   *
   * @param password the password for this <code>PasswordCredential</code>
   */
  public PasswordCredential(String password)
  {
    this.password = password;
  }

  /**
   * The equals method checks if the specified object is the same credential as this object.
   *
   * @param another the credential to compare with
   *
   * @return true if the object passed in matches the credential represented by the implementation
   *         of the <code>Credential</code> interface
   */
  @Override
  public boolean equals(Object another)
  {
    if (getClass().isAssignableFrom(another.getClass()))
    {
      if (getClass().cast(another).getPassword().equals(password))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the password for this <code>PasswordCredential</code>.
   *
   * @return the password for this <code>PasswordCredential</code>
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * The hashCode method returns an integer hash code to represent this credential.
   * It can be used to test for non-equality, or as an index key in a hash table.
   *
   * @return an integer hash code representing the credential
   */
  @Override
  public int hashCode()
  {
    return password.hashCode();
  }

  /**
   * Sets the password for this <code>PasswordCredential</code>.
   *
   * @param password the password for this <code>PasswordCredential</code>
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Returns a string representation of this credential.
   *
   * @return a string representation of this credential
   */
  @Override
  public String toString()
  {
    return getClass().getName() + " (" + password + ")";
  }
}
