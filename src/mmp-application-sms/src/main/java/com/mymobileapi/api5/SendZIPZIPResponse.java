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
 *         &lt;element name="Send_ZIP_ZIPResult" type="{http://www.w3
 *         .org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"sendZIPZIPResult"})
@XmlRootElement(name = "Send_ZIP_ZIPResponse")
public class SendZIPZIPResponse
{

  @XmlElement(name = "Send_ZIP_ZIPResult")
  protected byte[] sendZIPZIPResult;

  /**
   * Gets the value of the sendZIPZIPResult property.
   *
   * @return possible object is
   * byte[]
   */
  public byte[] getSendZIPZIPResult()
  {
    return sendZIPZIPResult;
  }

  /**
   * Sets the value of the sendZIPZIPResult property.
   *
   * @param value allowed object is
   *              byte[]
   */
  public void setSendZIPZIPResult(byte[] value)
  {
    this.sendZIPZIPResult = value;
  }
}
