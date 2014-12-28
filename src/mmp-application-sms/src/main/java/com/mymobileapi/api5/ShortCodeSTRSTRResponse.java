
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
 *         &lt;element name="ShortCode_STR_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "shortCodeSTRSTRResult" })
@XmlRootElement(name = "ShortCode_STR_STRResponse")
public class ShortCodeSTRSTRResponse
{
  @XmlElement(name = "ShortCode_STR_STRResult")
  protected String shortCodeSTRSTRResult;

  /**
   * Gets the value of the shortCodeSTRSTRResult property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getShortCodeSTRSTRResult()
  {
    return shortCodeSTRSTRResult;
  }

  /**
   * Sets the value of the shortCodeSTRSTRResult property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setShortCodeSTRSTRResult(String value)
  {
    this.shortCodeSTRSTRResult = value;
  }
}
