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

package guru.mmp.common.service.ws.security;

/**
 * The enumeration giving the supported types of web service security.
 */
public enum WebServiceSecurityType
{
  /**
   * The value indicating that the web service does not implement a security model.
   */
  NONE(0, "None"),

  /**
   * The value indicating that the web service implements the Mutual SSL security model.
   */
  MUTUAL_SSL(1, "Mutual SSL"),

  /**
   * The value indicating that the web service implements the HTTP authentication security model.
   */
  HTTP_AUTHENTICATION(2, "HTTP Authentication"),

  /**
   * The value indicating that the web service implements the digest authentication security
   * model.
   */
  DIGEST_AUTHENTICATION(3, "Digest Authentication");

  private int code;
  private String name;

  WebServiceSecurityType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the web service security type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the web service security type
   *
   * @return the web service security type given by the specified numeric code value
   */
  public static WebServiceSecurityType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return WebServiceSecurityType.NONE;

      case 1:
        return WebServiceSecurityType.MUTUAL_SSL;

      case 2:
        return WebServiceSecurityType.HTTP_AUTHENTICATION;

      default:
        return WebServiceSecurityType.DIGEST_AUTHENTICATION;
    }
  }

  /**
   * Returns the numeric code value identifying the web service security type.
   *
   * @return the numeric code value identifying the web service security type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the name of the web service security type.
   *
   * @return the name of the web service security type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>WebServiceSecurityType</code>
   * enumeration value.
   *
   * @return the string representation of the <code>WebServiceSecurityType</code>
   *         enumeration value
   */
  public String toString()
  {
    return name;
  }
}
