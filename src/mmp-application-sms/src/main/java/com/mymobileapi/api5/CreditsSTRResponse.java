
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
 *         &lt;element name="Credits_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "creditsSTRResult" })
@XmlRootElement(name = "Credits_STRResponse")
public class CreditsSTRResponse
{
  @XmlElement(name = "Credits_STRResult")
  protected String creditsSTRResult;

  /**
   * Gets the value of the creditsSTRResult property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getCreditsSTRResult()
  {
    return creditsSTRResult;
  }

  /**
   * Sets the value of the creditsSTRResult property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setCreditsSTRResult(String value)
  {
    this.creditsSTRResult = value;
  }
}
