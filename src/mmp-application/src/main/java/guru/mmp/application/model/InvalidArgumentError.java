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

import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.LocalDateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>InvalidArgumentError</code> class holds the invalid argument error information.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvalidArgumentError", namespace = "http://application.model.mmp.guru",
    propOrder = { "when", "message", "name", "detail", "validationErrors" })
public class InvalidArgumentError
{
  /**
   * The date and time the invalid argument error occurred.
   */
  @XmlElement(name = "When", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private LocalDateTime when;

  /**
   * The message for the invalid argument error.
   */
  @XmlElement(name = "Message", required = true)
  private String message;

  /**
   * The name of the argument associated with the invalid argument error
   */
  @XmlElement(name = "Name", required = true)
  private String name;

  /**
   * The detail for the invalid argument error
   */
  @XmlElement(name = "Detail", required = true)
  private String detail;

  /**
   * The optional validation errors associated with the invalid argument error
   */
  @XmlElement(name = "ValidationErrors")
  private List<ValidationError> validationErrors;

  InvalidArgumentError() {}

  /**
   * Constructs a new <code>InvalidArgumentError</code>.
   *
   * @param cause the cause of the invalid argument error
   */
  public InvalidArgumentError(InvalidArgumentException cause)
  {
    this.when = LocalDateTime.now();
    this.message = (cause.getMessage() != null)
        ? cause.getMessage()
        : "Invalid Argument";
    this.name = (!StringUtil.isNullOrEmpty(cause.getName()))
        ? StringUtil.capitalise(cause.getName())
        : "Unknown";

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

    this.validationErrors = cause.getValidationErrors();
  }
}
