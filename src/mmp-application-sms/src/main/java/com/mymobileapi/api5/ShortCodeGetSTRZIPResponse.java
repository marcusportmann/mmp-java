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
 *         &lt;element name="ShortCode_Get_STR_ZIPResult" type="{http://www.w3
 *         .org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"shortCodeGetSTRZIPResult"})
@XmlRootElement(name = "ShortCode_Get_STR_ZIPResponse")
public class ShortCodeGetSTRZIPResponse
{

  @XmlElement(name = "ShortCode_Get_STR_ZIPResult")
  protected byte[] shortCodeGetSTRZIPResult;

  /**
   * Gets the value of the shortCodeGetSTRZIPResult property.
   *
   * @return possible object is
   * byte[]
   */
  public byte[] getShortCodeGetSTRZIPResult()
  {
    return shortCodeGetSTRZIPResult;
  }

  /**
   * Sets the value of the shortCodeGetSTRZIPResult property.
   *
   * @param value allowed object is
   *              byte[]
   */
  public void setShortCodeGetSTRZIPResult(byte[] value)
  {
    this.shortCodeGetSTRZIPResult = value;
  }
}
