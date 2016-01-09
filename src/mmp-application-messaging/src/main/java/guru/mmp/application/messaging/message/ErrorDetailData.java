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

package guru.mmp.application.messaging.message;

/**
 * The <code>ErrorDetailData</code> class holds the information for an error associated with the
 * response data for a message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ErrorDetailData
{
  private String code;

  private String message;

  /**
   * Constructs a new <code>ErrorDetailData</code>.
   *
   * @param code    the error code
   * @param message the error message
   */
  public ErrorDetailData(String code, String message)
  {
    this.code = code;
    this.message = message;
  }

  /**
   * Returns the error code for the error.
   *
   * @return the error code for the error
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the error message for the error.
   *
   * @return the error message for the error
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Set the error code for the error.
   *
   * @param code the error code for the error
   */
  public void setCode(String code)
  {
    this.code = code;
  }

  /**
   * Set the error message for the error.
   *
   * @param message the error message for the error
   */
  public void setMessage(String message)
  {
    this.message = message;
  }
}
