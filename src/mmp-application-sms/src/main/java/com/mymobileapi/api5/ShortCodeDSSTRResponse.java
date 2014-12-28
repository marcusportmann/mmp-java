
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
 *         &lt;element name="ShortCode_DS_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "shortCodeDSSTRResult" })
@XmlRootElement(name = "ShortCode_DS_STRResponse")
public class ShortCodeDSSTRResponse
{
  @XmlElement(name = "ShortCode_DS_STRResult")
  protected String shortCodeDSSTRResult;

  /**
   * Gets the value of the shortCodeDSSTRResult property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getShortCodeDSSTRResult()
  {
    return shortCodeDSSTRResult;
  }

  /**
   * Sets the value of the shortCodeDSSTRResult property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setShortCodeDSSTRResult(String value)
  {
    this.shortCodeDSSTRResult = value;
  }
}
