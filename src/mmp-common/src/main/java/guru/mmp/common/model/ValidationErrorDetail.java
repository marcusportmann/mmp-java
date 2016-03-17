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

package guru.mmp.common.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * A <code>ValidationErrorDetail</code> instance holds detailed error information for a
 * <code>ValidationError</code>.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidationErrorDetail
  implements java.io.Serializable
{
  /**
   * No error code specified.
   */
  public static final String NO_ERROR_CODE = "NONE";

  /**
   * No error detail specified.
   */
  public static final String NO_ERROR_DETAIL = "NONE";

  /**
   * No error message specified.
   */
  public static final String NO_ERROR_MESSAGE = "NONE";
  private static final String WHEN_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
  private static final long serialVersionUID = 1000000;
  private String code;
  private String detail;
  private String message;
  private String when;

  /**
   * Constructs a new <code>ValidationErrorDetail</code>.
   */
  public ValidationErrorDetail()
  {
    init(NO_ERROR_CODE, NO_ERROR_MESSAGE, NO_ERROR_DETAIL, null);
  }

  /**
   * Constructs a new <code>ValidationErrorDetail</code> with the specified error message.
   *
   * @param message the error message
   */
  public ValidationErrorDetail(String message)
  {
    init(NO_ERROR_CODE, message, NO_ERROR_DETAIL, null);
  }

  /**
   * Constructs a new <code>ValidationErrorDetail</code> with the specified cause and a error
   * message of <code>(cause==null ? null : cause.toString())</code> (which typically contains the
   * class and message of cause).
   *
   * @param cause The cause which is converted to text for later retrieval by the
   *              <code>getDetail()</code> method. (A <code>null</code> value is permitted if the
   *              cause is nonexistent or unknown)
   */
  public ValidationErrorDetail(Throwable cause)
  {
    init(NO_ERROR_CODE,
        (cause == null)
        ? null
        : cause.toString(), NO_ERROR_DETAIL, cause);
  }

  /**
   * Constructs a new <code>ValidationErrorDetail</code> with the specified error code and error
   * message.
   *
   * @param code    the error code identifying the error
   * @param message the error message
   */
  public ValidationErrorDetail(String code, String message)
  {
    init(code, message, NO_ERROR_DETAIL, null);
  }

  /**
   * Constructs a new <code>ValidationErrorDetail</code> with the specified error message and cause.
   *
   * @param message the error message
   * @param cause   The cause which is converted to text for later retrieval by the
   *                <code>getDetail()</code> method. (A <code>null</code> value is permitted if the
   *                cause is nonexistent or unknown)
   */
  public ValidationErrorDetail(String message, Throwable cause)
  {
    init(NO_ERROR_CODE, message, NO_ERROR_DETAIL, cause);
  }

  /**
   * Constructs a new <code>ValidationErrorDetail</code> with the specified error code, error
   * message and detail.
   *
   * @param code    the error code identifying the error
   * @param message the error message
   * @param detail  the error detail
   */
  public ValidationErrorDetail(String code, String message, String detail)
  {
    init(code, message, detail, null);
  }

  /**
   * Constructs a new <code>ValidationErrorDetail</code> with the specified error code, error
   * message and cause.
   *
   * @param code    the error code identifying the error
   * @param message the error message
   * @param cause   The cause which is converted to text for later retrieval by the
   *                <code>getDetail()</code> method. (A <code>null</code> value is permitted if the
   *                cause is nonexistent or unknown)
   */
  public ValidationErrorDetail(String code, String message, Throwable cause)
  {
    init(code, message, NO_ERROR_DETAIL, cause);
  }

  /**
   * Returns the error code identifying the error or NONE if no error code was specified.
   *
   * @return the error code identifying the error or NONE if no error code was specified
   */
  public String getCode()
  {
    return (code == null)
        ? NO_ERROR_CODE
        : code;
  }

  /**
   * Returns the detail for the error which may be a nested stack trace.
   *
   * @return the detail for the error which may be a nested stack trace
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the error message.
   *
   * @return the error message
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Returns the date and time the exception occurred.
   *
   * @return the date and time the exception occurred
   */
  public String getWhen()
  {
    return when;
  }

  /**
   * Initialise a new <code>ValidationErrorDetail</code> with the specified information.
   *
   * @param code    the error code identifying the error
   * @param message the error message
   * @param detail  the error detail
   * @param cause   The cause which is converted to text for later retrieval by the
   *                <code>getDetail()</code> method. (A <code>null</code> value is permitted if the
   *                cause is nonexistent or unknown)
   */
  private void init(String code, String message, String detail, Throwable cause)
  {
    try
    {
      // Save the error code if one is specified
      if (code != null)
      {
        this.code = code;
      }

      // Save the error message if one is specified
      if (message != null)
      {
        this.message = message;
      }

      // Save the error detail if it is specified
      if (detail != null)
      {
        this.detail = detail;
      }

      // Derive when
      DateFormat dateFormat = new SimpleDateFormat(WHEN_FORMAT);

      when = dateFormat.format(new Date());

      // Dump the stack for the nested exception and add it as the detail
      if (cause != null)
      {
        try
        {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintWriter pw = new PrintWriter(baos);

          pw.println(message);
          pw.println();
          cause.printStackTrace(pw);
          pw.flush();
          this.detail = baos.toString();
        }
        catch (Throwable e)
        {
          this.detail = "Unable to dump the stack for the exception (" + cause + "): "
              + e.getMessage();
        }
      }
    }
    catch (Throwable e)
    {
      this.detail = "Failed to initialise the ValidationErrorDetail: " + e.getMessage();
    }
  }
}
