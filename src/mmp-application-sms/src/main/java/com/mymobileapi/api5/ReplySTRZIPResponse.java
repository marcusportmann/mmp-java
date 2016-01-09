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
 *         &lt;element name="Reply_STR_ZIPResult" type="{http://www.w3
 *         .org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"replySTRZIPResult"})
@XmlRootElement(name = "Reply_STR_ZIPResponse")
public class ReplySTRZIPResponse
{

  @XmlElement(name = "Reply_STR_ZIPResult")
  protected byte[] replySTRZIPResult;

  /**
   * Gets the value of the replySTRZIPResult property.
   *
   * @return possible object is
   * byte[]
   */
  public byte[] getReplySTRZIPResult()
  {
    return replySTRZIPResult;
  }

  /**
   * Sets the value of the replySTRZIPResult property.
   *
   * @param value allowed object is
   *              byte[]
   */
  public void setReplySTRZIPResult(byte[] value)
  {
    this.replySTRZIPResult = value;
  }
}
