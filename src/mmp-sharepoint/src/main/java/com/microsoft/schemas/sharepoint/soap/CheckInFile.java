package com.microsoft.schemas.sharepoint.soap;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pageUrl" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *         &lt;element name="CheckinType" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"pageUrl", "comment", "checkinType"})
@XmlRootElement(name = "CheckInFile")
public class CheckInFile
{

  @XmlElement(name = "CheckinType")
  protected String checkinType;

  protected String comment;

  protected String pageUrl;

  /**
   * Gets the value of the checkinType property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getCheckinType()
  {
    return checkinType;
  }

  /**
   * Gets the value of the comment property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getComment()
  {
    return comment;
  }

  /**
   * Gets the value of the pageUrl property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getPageUrl()
  {
    return pageUrl;
  }

  /**
   * Sets the value of the checkinType property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setCheckinType(String value)
  {
    this.checkinType = value;
  }

  /**
   * Sets the value of the comment property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setComment(String value)
  {
    this.comment = value;
  }

  /**
   * Sets the value of the pageUrl property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setPageUrl(String value)
  {
    this.pageUrl = value;
  }
}
