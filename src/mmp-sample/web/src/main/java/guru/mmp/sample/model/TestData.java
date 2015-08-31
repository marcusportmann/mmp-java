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

package guru.mmp.sample.model;

/**
 * The <code>TestData</code> class.
 */
public class TestData
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * Is active?
   */
  public boolean isActive;

  /**
   * The name.
   */
  private String name;

  /**
   * The note.
   */
  private String note;

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
   * Returns is active.
   *
   * @return is active
   */
  public boolean getIsActive()
  {
    return isActive;
  }

  /**
   * Returns the name.
   *
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the note.
   *
   * @return the note
   */
  public String getNote()
  {
    return note;
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
   * Set is active.
   *
   * @param isActive is active
   */
  public void setIsActive(boolean isActive)
  {
    this.isActive = isActive;
  }

  /**
   * Set the name.
   *
   * @param name the name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the note.
   *
   * @param note the note
   */
  public void setNote(String note)
  {
    this.note = note;
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
