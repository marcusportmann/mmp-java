package com.mymobileapi.api5;

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
 *         &lt;element name="Reply_DS_STRResult" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"replyDSSTRResult"})
@XmlRootElement(name = "Reply_DS_STRResponse")
public class ReplyDSSTRResponse
{

  @XmlElement(name = "Reply_DS_STRResult")
  protected String replyDSSTRResult;

  /**
   * Gets the value of the replyDSSTRResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getReplyDSSTRResult()
  {
    return replyDSSTRResult;
  }

  /**
   * Sets the value of the replyDSSTRResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setReplyDSSTRResult(String value)
  {
    this.replyDSSTRResult = value;
  }
}
