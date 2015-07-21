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

package guru.mmp.application.messaging;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <b>MessagePartResult</b> class stores the results of a message part being uploaded to
 * the server-side messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class MessagePartResult
{
  /**
   * The message part result code returned to indicate an invalid request.
   */
  public static final int ERROR_INVALID_REQUEST = -1;

  /**
   * The message part result code returned to indicate that an error occurred while queueing a
   * message part.
   */
  public static final int ERROR_QUEUEING_FAILED = -3;

  /**
   * The message part result code returned to indicate an error processing a message part with an
   * unrecognised message type.
   */
  public static final int ERROR_UNRECOGNISED_TYPE = -2;

  /**
   * The message part result code returned to indicated successful uploading of a message part.
   */
  public static final int SUCCESS = 0;

  /**
   * The flattened information for the exception that resulted from uploading the message part.
   */
  String exception;

  /**
   * The result code.
   * <p/>
   * A result code of 0 is used to indicate that the message part was uploaded successfully.
   */
  private long code;

  /**
   * The user-friendly text description of the result of uploading the message part.
   * <p/>
   * The <b>detail</b> may be blank if the message part was uploaded successfully.
   */
  private String detail;

  /**
   * Constructs a new <code>MessagePartResult</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message part result information
   */
  public MessagePartResult(Document document)
  {
    Element rootElement = document.getRootElement();

    this.code = Long.parseLong(rootElement.getAttributeValue("code"));
    this.detail = rootElement.getAttributeValue("detail");

    if (rootElement.hasChild("Exception"))
    {
      Element exceptionElement = rootElement.getChild("Exception");

      exception = exceptionElement.getText();
    }
  }

  /**
   * Constructs a new <code>MessagePartResult</code>.
   *
   * @param code   the result code
   * @param detail the text description of the result of uploading the message part
   */
  public MessagePartResult(long code, String detail)
  {
    this.code = code;
    this.detail = detail;
  }

  /**
   * Constructs a new <code>MessagePartResult</code>.
   *
   * @param code   the result code
   * @param detail the text description of the result of uploading the message part
   * @param cause  the exception that resulted from uploading the message part
   */
  public MessagePartResult(long code, String detail, Throwable cause)
  {
    this.code = code;
    this.detail = detail;

    if (cause != null)
    {
      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        cause.printStackTrace(pw);
        pw.flush();
        exception = baos.toString();
      }
      catch (Throwable e)
      {
        exception = "Unable to dump the stack for the exception (" + cause + "): " + e.getMessage();
      }
    }
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message received response
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message received response
   *         information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePartResult")
        && (rootElement.getAttributes().size() == 2)
        && !((!rootElement.hasAttribute("code")) || (!rootElement.hasAttribute("detail")));
  }

  /**
   * Returns the result code.
   * <p/>
   * A result code of 0 is used to indicate that the message part was uploaded successfully.
   *
   * @return the result code
   */
  public long getCode()
  {
    return code;
  }

  /**
   * Returns the user-friendly text description of the result of uploading the message part.
   *
   * @return the user-friendly text description of the result of uploading the message part
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the flattened information for the exception that resulted from uploading the message
   * part.
   *
   * @return the flattened information for the exception that resulted from uploading the message
   *         part
   */
  public String getException()
  {
    return exception;
  }

  /**
   * Set the result code;
   *
   * @param code the result code
   */
  public void setCode(long code)
  {
    this.code = code;
  }

  /**
   * Set the user-friendly text description of the result of uploading the message part.
   *
   * @param detail the user-friendly text description of the result of uploading the message part
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /**
   * Set the flattened information for the exception that resulted from uploading the message part.
   *
   * @param exception the flattened information for the exception that resulted from uploading the
   *                  message part
   */
  public void setException(String exception)
  {
    this.exception = exception;
  }

  /**
   * Returns the String representation of the message part result.
   *
   * @return the String representation of the message part result.
   */
  @Override
  public String toString()
  {
    return "<MessagePartResult" + " code=\"" + code + "\"" + " detail=\"" + detail + "\"" + "/>";
  }

  /**
   * Returns the WBXML representation of the message received response.
   *
   * @return the WBXML representation of the message received response
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessagePartResult");

    rootElement.setAttribute("code", Long.toString(code));
    rootElement.setAttribute("detail", detail);

    if (exception != null)
    {
      Element exceptionElement = new Element("Exception");

      exceptionElement.addContent(exception);
      rootElement.addContent(exceptionElement);
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}
