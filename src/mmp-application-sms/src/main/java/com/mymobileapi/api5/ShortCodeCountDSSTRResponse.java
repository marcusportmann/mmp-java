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
 *         &lt;element name="ShortCode_Count_DS_STRResult" type="{http://www.w3
 *         .org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"shortCodeCountDSSTRResult"})
@XmlRootElement(name = "ShortCode_Count_DS_STRResponse")
public class ShortCodeCountDSSTRResponse
{

  @XmlElement(name = "ShortCode_Count_DS_STRResult")
  protected String shortCodeCountDSSTRResult;

  /**
   * Gets the value of the shortCodeCountDSSTRResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getShortCodeCountDSSTRResult()
  {
    return shortCodeCountDSSTRResult;
  }

  /**
   * Sets the value of the shortCodeCountDSSTRResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setShortCodeCountDSSTRResult(String value)
  {
    this.shortCodeCountDSSTRResult = value;
  }
}
