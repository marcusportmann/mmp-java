
package com.mymobileapi.api5;

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
 *         &lt;element name="Sent_STR_ZIPResult" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sentSTRZIPResult" })
@XmlRootElement(name = "Sent_STR_ZIPResponse")
public class SentSTRZIPResponse
{
  @XmlElement(name = "Sent_STR_ZIPResult")
  protected byte[] sentSTRZIPResult;

  /**
   * Gets the value of the sentSTRZIPResult property.
   *
   * @return
   *     possible object is
   *     byte[]
   */
  public byte[] getSentSTRZIPResult()
  {
    return sentSTRZIPResult;
  }

  /**
   * Sets the value of the sentSTRZIPResult property.
   *
   * @param value
   *     allowed object is
   *     byte[]
   */
  public void setSentSTRZIPResult(byte[] value)
  {
    this.sentSTRZIPResult = value;
  }
}
