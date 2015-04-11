/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.persistence;

//~--- JDK imports ------------------------------------------------------------

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The <code>DAOException</code> exception is thrown to indicate an error condition when working
 * with a Data Access Object (DAO).
 *
 * @author Marcus Portmann
 */
public class DAOException extends RuntimeException
{
  private static final String NO_ERROR_CODE = "NONE";
  private static final String WHEN_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
  private static final long serialVersionUID = 1000000;
  private String code;
  private Date when;

  /**
   * Constructs a new <code>DAOException</code> with <code>null</code> as its message.
   */
  public DAOException()
  {
    super();
    this.when = new Date();
  }

  /**
   * Constructs a new <code>DAOException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public DAOException(String message)
  {
    super(message);
    this.when = new Date();
  }

  /**
   * Constructs a new <code>DAOException</code> with the specified cause and a message
   * of <code>(cause==null ? null : cause.toString())</code> (which typically contains the class
   * and message of cause).
   *
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method.
   *              (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public DAOException(Throwable cause)
  {
    super(cause);
    this.when = new Date();
  }

  /**
   * Constructs a new <code>DAOException</code> with the specified code and message.
   *
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public DAOException(String code, String message)
  {
    super(message);
    this.code = code;
    this.when = new Date();
  }

  /**
   * Constructs a new <code>DAOException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public DAOException(String message, Throwable cause)
  {
    super(message, cause);
    this.when = new Date();
  }

  /**
   * Constructs a new <code>DAOException</code> with the specified code, message and cause.
   *
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public DAOException(String code, String message, Throwable cause)
  {
    super(message, cause);
    this.code = code;
    this.when = new Date();
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
   * Returns the date and time the exception occurred.
   *
   * @return the date and time the exception occurred
   */
  public Date getWhen()
  {
    return when;
  }

  /**
   * Returns the date and time the exception occurred as a String.
   *
   * @return the date and time the exception occurred as a String
   */
  public String getWhenAsString()
  {
    DateFormat dateFormat = new SimpleDateFormat(WHEN_FORMAT);

    return dateFormat.format(when);
  }
}
