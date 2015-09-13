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

package guru.mmp.common.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The <code>ValidationException</code> exception is a common model exception thrown to indicate a
 * validation error condition when working with a Business Object.
 * <p/>
 * The exception can contain a single validation error or the error information for one or more
 * entities e.g. parameters to a Business Object method call.
 * <p/>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidationException extends ModelException
{
  private static final long serialVersionUID = 1000000;
  private List<ValidationError> errors = new ArrayList<>();

  /**
   * Constructs a new <code>ValidationException</code> with the specified error information.
   *
   * @param errors the validation errors associated with the validation exception
   */
  public ValidationException(List<ValidationError> errors)
  {
    super("One or more validation errors occurred");
    this.errors = errors;
  }

  /**
   * Constructs a new <code>ValidationException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public ValidationException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new <code>ValidationException</code> with the specified cause and a message
   * of <code>(cause==null ? null : cause.toString())</code> (which typically contains the class
   * and message of cause).
   *
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method.
   *              (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public ValidationException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Constructs a new <code>ValidationException</code> with the specified error information.
   *
   * @param errors the validation errors associated with the validation exception
   */
  public ValidationException(ValidationError[] errors)
  {
    super("One or more validation errors occurred");
    this.errors = new ArrayList<>();

    Collections.addAll(this.errors, errors);
  }

  /**
   * Constructs a new <code>ValidationException</code> with the specified code and message.
   *
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public ValidationException(String code, String message)
  {
    super(code, message);
  }

  /**
   * Constructs a new <code>ValidationException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public ValidationException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Constructs a new <code>ValidationException</code> with the specified error code and message.
   *
   * @param name    the name of the field the error is associate with
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public ValidationException(String name, String code, String message)
  {
    super(code, message);
    errors.add(new ValidationError(name, code, message));
  }

  /**
   * Constructs a new <code>ValidationException</code> with the specified code, message and cause.
   *
   * @param code    the error code identifying the error
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public ValidationException(String code, String message, Throwable cause)
  {
    super(code, message, cause);
  }

  /**
   * Retrieve the validation errors associated with the exception.
   *
   * @return the validation errors associated with the exception
   */
  public List<ValidationError> getErrors()
  {
    return errors;
  }
}
