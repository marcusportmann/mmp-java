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

package guru.mmp.common.crypto;

/**
 * The <code>EncryptionScheme</code> enumeration identifies the types of supported encryption
 * schemes.
 *
 * @author Marcus Portmann
 */
public enum EncryptionScheme
{
  NONE(0, "None"), TRIPLE_DES_CBC_PKCS5(1, "Triple-DES CBC PKCS5"), AES_CFB(2, "AES CFB");

  /** The code identifying the type of encryption scheme. */
  private int code;

  /** The name of the type of encryption scheme. */
  private String name;

  EncryptionScheme(int code, String description)
  {
    this.code = code;
    this.name = description;
  }

  /**
   * Returns the type of encryption scheme given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the type of encryption scheme
   *
   * @return the type of encryption scheme given by the specified numeric code value
   */
  public static EncryptionScheme fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return EncryptionScheme.NONE;

      case 1:
        return EncryptionScheme.TRIPLE_DES_CBC_PKCS5;

      case 2:
        return EncryptionScheme.AES_CFB;

      default:
        return EncryptionScheme.NONE;
    }
  }

  /**
   * Returns the numeric code value identifying the type of encryption scheme.
   *
   * @return the numeric code value identifying the type of encryption scheme
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> value of the numeric code value identifying the type of
   * encryption scheme.
   *
   * @return the <code>String</code> value of the numeric code value identifying the type of
   *         encryption scheme
   */
  @SuppressWarnings("unused")
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the type of encryption scheme.
   *
   * @return the name of the type of encryption scheme
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the type of encryption scheme enumeration value.
   *
   * @return the string representation of the type of encryption scheme enumeration value
   */
  public String toString()
  {
    return name;
  }
}
