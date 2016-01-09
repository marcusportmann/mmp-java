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
 *         &lt;element name="Sent_STR_STRResult" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"sentSTRSTRResult"})
@XmlRootElement(name = "Sent_STR_STRResponse")
public class SentSTRSTRResponse
{

  @XmlElement(name = "Sent_STR_STRResult")
  protected String sentSTRSTRResult;

  /**
   * Gets the value of the sentSTRSTRResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getSentSTRSTRResult()
  {
    return sentSTRSTRResult;
  }

  /**
   * Sets the value of the sentSTRSTRResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setSentSTRSTRResult(String value)
  {
    this.sentSTRSTRResult = value;
  }
}
