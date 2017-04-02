/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application.codes;

/**
 * The enumeration giving the possible code category types.
 *
 * @author Marcus Portmann
 */
public enum CodeCategoryType
{
  LOCAL_STANDARD(0, "Local Standard Codes"), LOCAL_CUSTOM(1, "Local Custom Codes"),
      REMOTE_HTTP_SERVICE(2, "Remote HTTP Service"), REMOTE_WEB_SERVICE(3, "Remote Web Service"),
      CODE_PROVIDER(4, "Code Provider");

  private int code;
  private String name;

  CodeCategoryType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the code category type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the code category type
   *
   * @return the code category type given by the specified numeric code value
   */
  public static CodeCategoryType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return CodeCategoryType.LOCAL_STANDARD;

      case 1:
        return CodeCategoryType.LOCAL_CUSTOM;

      case 2:
        return CodeCategoryType.REMOTE_HTTP_SERVICE;

      case 3:
        return CodeCategoryType.REMOTE_WEB_SERVICE;

      case 4:
        return CodeCategoryType.CODE_PROVIDER;

      default:
        return CodeCategoryType.LOCAL_STANDARD;
    }
  }

  /**
   * Returns the numeric code value identifying the code category type.
   *
   * @return the numeric code value identifying the code category type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the name of the code category type.
   *
   * @return the name of the code category type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>CodeCategoryType</code> enumeration value.
   *
   * @return the string representation of the <code>CodeCategoryType</code> enumeration value
   */
  public String toString()
  {
    return name;
  }
}
