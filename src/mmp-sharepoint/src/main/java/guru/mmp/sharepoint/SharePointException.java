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

package guru.mmp.sharepoint;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.xml.soap.Detail;

/**
 * The <code>SharePointException</code> exception is thrown to indicate an error condition when
 * interacting with SharePoint.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SharePointException extends Exception
{
  private static final String NO_ERROR_CODE = "NONE";
  private static final String WHEN_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
  private static final long serialVersionUID = 1000000;
  private String sharePointErrorCode;
  private String sharePointErrorDescription;
  private Date when;

  /**
   * Constructs a new <code>SharePointException</code> with <code>null</code> as its message.
   */
  public SharePointException()
  {
    super();
    this.when = new Date();
  }

  /**
   * Constructs a new <code>SharePointException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public SharePointException(String message)
  {
    super(message);
    this.when = new Date();
  }

  /**
   * Constructs a new <code>SharePointException</code> with the specified cause and a message
   * of <code>(cause==null ? null : cause.toString())</code> (which typically contains the class
   * and message of cause).
   *
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method.
   *              (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public SharePointException(Throwable cause)
  {
    super(cause);
    this.when = new Date();
  }

  /**
   * Constructs a new <code>SharePointException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public SharePointException(String message, Throwable cause)
  {
    super(message, cause);
    this.when = new Date();

    if (cause instanceof javax.xml.ws.soap.SOAPFaultException)
    {
      javax.xml.ws.soap.SOAPFaultException sfe = (javax.xml.ws.soap.SOAPFaultException) cause;

      if (sfe.getFault() != null)
      {
        this.sharePointErrorCode = sfe.getFault().getFaultCode();

        try
        {
          if (sfe.getFault().getDetail() != null)
          {
            Detail detail = sfe.getFault().getDetail();

            NodeList nodeList =
              detail.getElementsByTagNameNS("http://schemas.microsoft.com/sharepoint/soap/",
                "errorcode");

            if (nodeList.getLength() > 0)
            {
              this.sharePointErrorCode = nodeList.item(0).getTextContent();
            }

            nodeList =
              detail.getElementsByTagNameNS("http://schemas.microsoft.com/sharepoint/soap/",
                "errorstring");

            if (nodeList.getLength() > 0)
            {
              String sharePointErrorDescription = nodeList.item(0).getTextContent();

              if ((sharePointErrorDescription.indexOf("\r\n")) != -1)
              {
                String[] lines = sharePointErrorDescription.split("\r\n");

                /*
                 * The first line that is not blank will be used as the brief description of the
                 * SharePoint error.
                 */
                for (String line : lines)
                {
                  line = line.trim();

                  if (line.length() > 0)
                  {
                    this.sharePointErrorDescription = line;

                    break;
                  }
                }
              }
              else if (sharePointErrorDescription.contains("\n"))
              {
                String[] lines = sharePointErrorDescription.split("\n");

                /*
                 * The first line that is not blank will be used as the brief description of the
                 * SharePoint error.
                 */
                for (String line : lines)
                {
                  line = line.trim();

                  if (line.length() > 0)
                  {
                    this.sharePointErrorDescription = line;

                    break;
                  }
                }
              }
              else
              {
                this.sharePointErrorDescription = sharePointErrorDescription;
              }
            }
          }
        }
        catch (Throwable e)
        {
          this.sharePointErrorDescription =
            "Failed to extract the SharePoint error information from SOAP fault";

          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Returns the SharePoint error code identifying the error or NONE if no error code was specified.
   *
   * @return the SharePoint error code identifying the error or NONE if no error code was specified
   */
  public String getSharePointErrorCode()
  {
    return (sharePointErrorCode == null)
        ? NO_ERROR_CODE
        : sharePointErrorCode;
  }

  /**
   * Returns the SharePoint error description.
   *
   * @return the SharePoint error description
   */
  public String getSharePointErrorDescription()
  {
    return (sharePointErrorDescription == null)
        ? ""
        : sharePointErrorDescription;
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

  /**
   * Returns a short description of this exception.
   *
   * @return a short description of this exception
   */
  @Override
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();

    buffer.append(getClass().getName()).append(": ");

    if (sharePointErrorCode != null)
    {
      buffer.append("[").append(sharePointErrorCode).append("] ");
    }

    buffer.append(getMessage());

    if (sharePointErrorDescription != null)
    {
      buffer.append(" (").append(sharePointErrorDescription).append(")");
    }

    return buffer.toString();
  }
}
