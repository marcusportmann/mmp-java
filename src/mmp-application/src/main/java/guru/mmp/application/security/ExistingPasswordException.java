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

package guru.mmp.application.security;

/**
 * An <code>ExistingPasswordException</code> is thrown to indicate that a security operation failed
 * as a result of an existing password e.g. when attempting to change a user's password using a
 * password that forms part of the user's password history.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ExistingPasswordException extends Exception
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ExistingPasswordException</code> with <code>null</code> as its
   * message.
   */
  public ExistingPasswordException()
  {
    super();
  }

  /**
   * Constructs a new <code>ExistingPasswordException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public ExistingPasswordException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new <code>ExistingPasswordException</code> with the specified cause and a
   * message of <code>(cause==null ? null : cause.toString())</code> (which typically contains the
   * class and message of cause).
   *
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method.
   *              (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public ExistingPasswordException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Constructs a new <code>ExistingPasswordException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public ExistingPasswordException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
