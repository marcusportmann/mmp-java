
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
 *         &lt;element name="ShortCode_Update_STR_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "shortCodeUpdateSTRSTRResult" })
@XmlRootElement(name = "ShortCode_Update_STR_STRResponse")
public class ShortCodeUpdateSTRSTRResponse
{
  @XmlElement(name = "ShortCode_Update_STR_STRResult")
  protected String shortCodeUpdateSTRSTRResult;

  /**
   * Gets the value of the shortCodeUpdateSTRSTRResult property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getShortCodeUpdateSTRSTRResult()
  {
    return shortCodeUpdateSTRSTRResult;
  }

  /**
   * Sets the value of the shortCodeUpdateSTRSTRResult property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setShortCodeUpdateSTRSTRResult(String value)
  {
    this.shortCodeUpdateSTRSTRResult = value;
  }
}
