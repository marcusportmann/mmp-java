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
 *         &lt;element name="ShortCode_Get_DS_STRResult" type="{http://www.w3
 *         .org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"shortCodeGetDSSTRResult"})
@XmlRootElement(name = "ShortCode_Get_DS_STRResponse")
public class ShortCodeGetDSSTRResponse
{

  @XmlElement(name = "ShortCode_Get_DS_STRResult")
  protected String shortCodeGetDSSTRResult;

  /**
   * Gets the value of the shortCodeGetDSSTRResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getShortCodeGetDSSTRResult()
  {
    return shortCodeGetDSSTRResult;
  }

  /**
   * Sets the value of the shortCodeGetDSSTRResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setShortCodeGetDSSTRResult(String value)
  {
    this.shortCodeGetDSSTRResult = value;
  }
}
