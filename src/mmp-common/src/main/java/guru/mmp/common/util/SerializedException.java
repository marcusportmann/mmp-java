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

package guru.mmp.common.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The <code>SerializedException</code> exception is an abstract base class for exceptions that
 * need to serialize their stack traces.
 *
 * The serialized stack trace can be retrieved using the <code>getDetail</code> method.
 *
 * @author Marcus Portmann
 */
public abstract class SerializedException extends Exception
{
  private static final String NO_ERROR_CODE = "NONE";
  private static final String NO_ERROR_MESSAGE = "NONE";
  private static final String WHEN_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
  private static final long serialVersionUID = 1000000;
  private String code;
  private String detail;
  private String when;

  /**
   * Constructs a new <code>SerializedException</code>.
   */
  public SerializedException()
  {
    super();
    init(NO_ERROR_CODE, NO_ERROR_MESSAGE, null);
  }

  /**
   * Constructs a new <code>SerializedException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public SerializedException(String message)
  {
    super(message);
    init(NO_ERROR_CODE, message, null);
  }

  /**
   * Constructs a new <code>SerializedException</code> with the specified cause and a
   * message of <code>(cause==null ? null : cause.toString())</code> (which typically contains the
   * class and message of cause).
   *
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method.
   *              (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public SerializedException(Throwable cause)
  {
    super((cause == null)
        ? null
        : cause.toString());
    init(NO_ERROR_CODE, (cause == null)
        ? null
        : cause.toString(), cause);
  }

  /**
   * Constructs a new <code>SerializedException</code> with the specified code and message.
   *
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public SerializedException(String code, String message)
  {
    super(message);
    init(code, message, null);
  }

  /**
   * Constructs a new <code>SerializedException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public SerializedException(String message, Throwable cause)
  {
    super(message);
    init(NO_ERROR_CODE, message, cause);
  }

  /**
   * Constructs a new <code>SerializedException</code> with the specified code, message and cause.
   *
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public SerializedException(String code, String message, Throwable cause)
  {
    super(message);
    init(code, message, cause);
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
   * Returns the detail for the exception which may be a nested stack trace.
   *
   * @return the detail for the exception which may be a nested stack trace
   */
  public String getDetail()
  {
    return detail;
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
   * Initialise a new <code>SerializedException</code> with the specified error code, message and
   * cause.
   *
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  private void init(String code, String message, Throwable cause)
  {
    try
    {
      // Save the code if when is specified
      if (code != null)
      {
        this.code = code;
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
          detail = baos.toString();
        }
        catch (Throwable e)
        {
          detail = "Failed to dump the stack for the exception (" + cause + "): " + e.getMessage();
        }
      }
      else
      {
        detail = message;
      }
    }
    catch (Throwable e)
    {
      detail = "Failed to initialise the serialized exception: " + e.getMessage();
    }
  }
}
