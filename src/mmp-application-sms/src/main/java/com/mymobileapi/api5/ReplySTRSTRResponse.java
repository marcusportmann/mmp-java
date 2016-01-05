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
 *         &lt;element name="Reply_STR_STRResult" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"replySTRSTRResult"})
@XmlRootElement(name = "Reply_STR_STRResponse")
public class ReplySTRSTRResponse
{

  @XmlElement(name = "Reply_STR_STRResult")
  protected String replySTRSTRResult;

  /**
   * Gets the value of the replySTRSTRResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getReplySTRSTRResult()
  {
    return replySTRSTRResult;
  }

  /**
   * Sets the value of the replySTRSTRResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setReplySTRSTRResult(String value)
  {
    this.replySTRSTRResult = value;
  }
}
