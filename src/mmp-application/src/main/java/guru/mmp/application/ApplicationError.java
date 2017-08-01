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

package guru.mmp.application;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import guru.mmp.common.util.StringUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationError</code> class holds the information for an application error.
 *
 * @author Marcus Portmann
 */
@JsonPropertyOrder({ "path", "timestamp", "status", "message", "exception", "stackTrace" })
public class ApplicationError
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The URI for the HTTP request that resulted in the error.
   */
  @JsonProperty
  private String path;

  /**
   * The date and time the error occurred.
   */
  @JsonProperty
  private LocalDateTime timestamp;

  /**
   * The HTTP status for the error.
   */
  @JsonProperty
  private String status;

  /**
   * The error message.
   */
  @JsonProperty
  private String message;

  /**
   * The fully qualified name of the exception associated with the error.
   */
  @JsonProperty
  private String exception;

  /**
   * The stack trace associated with the error.
   */
  @JsonProperty
  private String stackTrace;

  /**
   * Constructs a new <code>ApplicationError</code>.
   *
   * @param cause the exception
   */
  public ApplicationError(HttpServletRequest request, HttpStatus responseStatus, Throwable cause)
  {
    this.timestamp = LocalDateTime.now();
    this.exception = cause.getClass().getName();

    ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(cause.getClass(),
        ResponseStatus.class);

    if (annotation != null)
    {
      // Use the HTTP response status specified through the @ResponseStatus annotation
      responseStatus = annotation.value();

      if (!StringUtil.isNullOrEmpty(annotation.reason()))
      {
        this.message = annotation.reason();
      }
      else
      {
        this.message = cause.getMessage();
      }
    }
    else
    {
      this.message = cause.getMessage();
    }

    this.status = responseStatus.getReasonPhrase() + " (" + responseStatus.value() + ")";

    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintWriter pw = new PrintWriter(baos);

      pw.println(cause.getMessage());
      pw.println();
      cause.printStackTrace(pw);
      pw.flush();

      this.stackTrace = baos.toString();
    }
    catch (Throwable e) {}

    this.path = request.getRequestURI();
  }
}
