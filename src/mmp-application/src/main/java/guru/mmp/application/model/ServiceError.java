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

package guru.mmp.application.model;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.xml.LocalDateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ServiceError</code> class holds the service error information.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceError", namespace = "http://application.model.mmp.guru",
    propOrder = { "when", "message", "detail" })
public class ServiceError
{
  /**
   * The date and time the service error occurred.
   */
  @XmlElement(name = "When", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private LocalDateTime when;

  /**
   * The message for the service error.
   */
  @XmlElement(name = "Message", required = true)
  private String message;

  /**
   * The detail for the service error
   */
  @XmlElement(name = "Detail", required = true)
  private String detail;

  ServiceError() {}

  /**
   * Constructs a new <code>ServiceError</code>.
   *
   * @param cause the cause of the service error
   */
  public ServiceError(Throwable cause)
  {
    this.when = LocalDateTime.now();
    this.message = (cause.getMessage() != null)
      ? cause.getMessage()
      : cause.getClass().getSimpleName();

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
      this.detail = "Failed to dump the stack for the exception (" + cause + "): " + e.getMessage();
    }
  }
}
