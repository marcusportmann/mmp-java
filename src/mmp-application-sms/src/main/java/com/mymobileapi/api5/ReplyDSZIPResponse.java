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
 *         &lt;element name="Reply_DS_ZIPResult" type="{http://www.w3
 *         .org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"replyDSZIPResult"})
@XmlRootElement(name = "Reply_DS_ZIPResponse")
public class ReplyDSZIPResponse
{

  @XmlElement(name = "Reply_DS_ZIPResult")
  protected byte[] replyDSZIPResult;

  /**
   * Gets the value of the replyDSZIPResult property.
   *
   * @return possible object is
   * byte[]
   */
  public byte[] getReplyDSZIPResult()
  {
    return replyDSZIPResult;
  }

  /**
   * Sets the value of the replyDSZIPResult property.
   *
   * @param value allowed object is
   *              byte[]
   */
  public void setReplyDSZIPResult(byte[] value)
  {
    this.replyDSZIPResult = value;
  }
}
