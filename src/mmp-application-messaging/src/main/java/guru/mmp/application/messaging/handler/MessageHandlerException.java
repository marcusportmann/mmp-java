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

package guru.mmp.application.messaging.handler;

/**
 * The <code>MessageHandlerException</code> exception is thrown to indicate an error condition when
 * a message handler processes a message.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
public class MessageHandlerException extends Exception
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>MessageHandlerException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>buildMessageFromResultSet
   *                ()</code> method.
   */
  public MessageHandlerException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new <code>MessageHandlerException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>buildMessageFromResultSet
   *                ()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public MessageHandlerException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
