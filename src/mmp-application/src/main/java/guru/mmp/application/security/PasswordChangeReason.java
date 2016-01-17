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

/**
 * The <code>PasswordChangeReason</code> enumeration defines the possible reasons for why a user's
 * password was changed.
 */
public enum PasswordChangeReason
{
  USER(0, "User"), ADMINISTRATIVE(1, "Administrative"), FORGOTTEN(2, "Forgotten");

  private String description;
  private int id;

  PasswordChangeReason(int id, String description)
  {
    this.id = id;
    this.description = description;
  }

  /**
   * Returns the description for the password change reason.
   *
   * @return the description for the password change reason
   */
  public String description()
  {
    return description;
  }

  /**
   * Returns the numeric identifier for the password change reason.
   *
   * @return the numeric identifier for the password change reason
   */
  public int id()
  {
    return id;
  }
}
