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

package guru.mmp.sample.model;

/**
 * The <code>TestData</code> class.
 */
@SuppressWarnings("unused")
public class TestData
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * Is active?
   */
  public boolean isActive;

  /**
   * The password confirmation.
   */
  private String confirmPassword;

  /**
   * The favourite pet.
   */
  private String favouritePet;

  /**
   * The first name(s).
   */
  private String firstNames;

  /**
   * The last name.
   */
  private String lastName;

  /**
   * The notes.
   */
  private String notes;

  /**
   * The password.
   */
  private String password;

  /**
   * The role.
   */
  private String role;

  /**
   * The title.
   */
  private String title;

  /**
   * Returns the password confirmation.
   *
   * @return the password confirmation
   */
  public String getConfirmPassword()
  {
    return confirmPassword;
  }

  /**
   * Returns the favourite pet.
   *
   * @return the favourite pet
   */
  public String getFavouritePet()
  {
    return favouritePet;
  }

  /**
   * Returns the first name(s).
   *
   * @return the first name(s)
   */
  public String getFirstNames()
  {
    return firstNames;
  }

  /**
   * Returns is active.
   *
   * @return is active
   */
  public boolean getIsActive()
  {
    return isActive;
  }

  /**
   * Returns the last name.
   *
   * @return the last name
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * Return the notes.
   *
   * @return the notes
   */
  public String getNotes()
  {
    return notes;
  }

  /**
   * Returns the password.
   *
   * @return the password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Returns the role.
   *
   * @return the role
   */
  public String getRole()
  {
    return role;
  }

  /**
   * Returns the title.
   *
   * @return the title
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Set the password confirmation.
   *
   * @param confirmPassword the password confirmation
   */
  public void setConfirmPassword(String confirmPassword)
  {
    this.confirmPassword = confirmPassword;
  }

  /**
   * Set the favourite pet.
   *
   * @param favouritePet the favourite pet
   */
  public void setFavouritePet(String favouritePet)
  {
    this.favouritePet = favouritePet;
  }

  /**
   * Set the first name(s).
   *
   * @param firstNames the name
   */
  public void setFirstNames(String firstNames)
  {
    this.firstNames = firstNames;
  }

  /**
   * Set is active.
   *
   * @param isActive is active
   */
  public void setIsActive(boolean isActive)
  {
    this.isActive = isActive;
  }

  /**
   * Set the last name.
   *
   * @param lastName the last name
   */
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  /**
   * Set the notes.
   *
   * @param notes the notes
   */
  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  /**
   * Set the password.
   *
   * @param password the password
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Set the role.
   *
   * @param role the role
   */
  public void setRole(String role)
  {
    this.role = role;
  }

  /**
   * Set the title.
   *
   * @param title the title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }
}
