
package com.microsoft.schemas.sharepoint.soap;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CheckInFileResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "checkInFileResult" })
@XmlRootElement(name = "CheckInFileResponse")
public class CheckInFileResponse
{
  @XmlElement(name = "CheckInFileResult")
  protected boolean checkInFileResult;

  /**
   * Gets the value of the checkInFileResult property.
   *
   *
   * @return
   */
  public boolean isCheckInFileResult()
  {
    return checkInFileResult;
  }

  /**
   * Sets the value of the checkInFileResult property.
   *
   *
   * @param value
   */
  public void setCheckInFileResult(boolean value)
  {
    this.checkInFileResult = value;
  }
}
