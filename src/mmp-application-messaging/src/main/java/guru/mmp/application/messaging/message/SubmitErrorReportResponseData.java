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

package guru.mmp.application.messaging.message;

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessagingException;
import guru.mmp.application.messaging.WbxmlMessageData;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.UUID;

/**
 * The <code>SubmitErrorReportResponseData</code> class manages the data for a
 * "Submit Error Report Response" message.
 * <p/>
 * This is an asynchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SubmitErrorReportResponseData
  extends WbxmlMessageData
{
  /**
   * The message returned when a submitted error report is successfully processed.
   */
  public static final String ERROR_MESSAGE_SUCCESS = "Success";

  /**
   * The UUID for the "Submit Error Report Response" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
    "8be50cfa-2fb1-4634-9bfa-d01e77eaf766");

  /**
   * The error code indicating the result of processing the submitted error report where a code
   * of '0' indicates success and a non-zero code indicates an error condition.
   */
  public int errorCode;

  /**
   * The error message describing the result of processing the submitted error report.
   */
  public String errorMessage;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report that was
   * submitted for processing.
   */
  public UUID errorReportId;

  /**
   * Constructs a new <code>SubmitErrorReportResponseData</code>.
   */
  public SubmitErrorReportResponseData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>SubmitErrorReportResponseData</code>.
   *
   * @param errorCode     the error code indicating the result of processing the submitted error
   *                      report
   * @param errorMessage  the error message describing the result of processing the submitted error
   *                      report
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report that was submitted for processing
   */
  public SubmitErrorReportResponseData(int errorCode, String errorMessage, UUID errorReportId)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.errorReportId = errorReportId;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   * <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean fromMessageData(byte[] messageData)
    throws MessagingException
  {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("SubmitErrorReportResponse"))
    {
      return false;
    }

    if ((!rootElement.hasChild("ErrorCode")) || (!rootElement.hasChild("ErrorMessage")) ||
      (!rootElement.hasChild("ErrorReportId")))
    {
      return false;
    }

    errorCode = Integer.parseInt(rootElement.getChildText("ErrorCode"));
    errorMessage = rootElement.getChildText("ErrorMessage");
    errorReportId = UUID.fromString(rootElement.getChildText("ErrorReportId"));

    return true;
  }

  /**
   * Returns the error code indicating the result of processing the submitted error report where a
   * code of '0' indicates success and a non-zero code indicates an error condition.
   *
   * @return the error code indicating the result of processing the submitted error report where a
   * code of '0' indicates success and a non-zero code indicates an error condition
   */
  public int getErrorCode()
  {
    return errorCode;
  }

  /**
   * Set the error code indicating the result of processing the submitted error report where a code
   * of '0' indicates success and a non-zero code indicates an error condition.
   *
   * @param errorCode the error code indicating the result of processing the submitted error report
   *                  where a code of '0' indicates success and a non-zero code indicates an error
   *                  condition
   */
  public void setErrorCode(int errorCode)
  {
    this.errorCode = errorCode;
  }

  /**
   * Returns the error message describing the result of processing the submitted error report.
   *
   * @return the error message describing the result of processing the submitted error report
   */
  public String getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * Set the error message describing the result of processing the submitted error report.
   *
   * @param errorMessage the error message describing the result of processing the submitted error
   *                     report
   */
  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the error report
   * that was submitted for processing.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the error report
   * that was submitted for processing
   */
  public UUID getErrorReportId()
  {
    return errorReportId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the error report that
   * was submitted for processing.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report that was submitted for processing
   */
  public void setErrorReportId(UUID errorReportId)
  {
    this.errorReportId = errorReportId;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   * message
   *
   * @throws MessagingException
   */
  public byte[] toMessageData()
    throws MessagingException
  {
    Element rootElement = new Element("SubmitErrorReportResponse");

    rootElement.addContent(new Element("ErrorCode", String.valueOf(errorCode)));
    rootElement.addContent(new Element("ErrorMessage", StringUtil.notNull(errorMessage)));
    rootElement.addContent(new Element("ErrorReportId", errorReportId.toString()));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "SubmitErrorReportResponseData {" + "errorReportId=\"" + errorReportId + "\", " +
      "errorCode=\"" + errorCode + "\", " + "errorMessage=\"" + StringUtil.notNull(errorMessage) +
      "\"" + "}";
  }
}
