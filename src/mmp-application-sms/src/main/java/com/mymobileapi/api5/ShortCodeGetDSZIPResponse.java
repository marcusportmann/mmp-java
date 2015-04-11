
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
 *         &lt;element name="ShortCode_Get_DS_ZIPResult" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "shortCodeGetDSZIPResult" })
@XmlRootElement(name = "ShortCode_Get_DS_ZIPResponse")
public class ShortCodeGetDSZIPResponse
{
  @XmlElement(name = "ShortCode_Get_DS_ZIPResult")
  protected byte[] shortCodeGetDSZIPResult;

  /**
   * Gets the value of the shortCodeGetDSZIPResult property.
   *
   * @return
   *     possible object is
   *     byte[]
   */
  public byte[] getShortCodeGetDSZIPResult()
  {
    return shortCodeGetDSZIPResult;
  }

  /**
   * Sets the value of the shortCodeGetDSZIPResult property.
   *
   * @param value
   *     allowed object is
   *     byte[]
   */
  public void setShortCodeGetDSZIPResult(byte[] value)
  {
    this.shortCodeGetDSZIPResult = value;
  }
}
