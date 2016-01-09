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
 *         &lt;element name="Send_STR_STRResult" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"sendSTRSTRResult"})
@XmlRootElement(name = "Send_STR_STRResponse")
public class SendSTRSTRResponse
{

  @XmlElement(name = "Send_STR_STRResult")
  protected String sendSTRSTRResult;

  /**
   * Gets the value of the sendSTRSTRResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getSendSTRSTRResult()
  {
    return sendSTRSTRResult;
  }

  /**
   * Sets the value of the sendSTRSTRResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setSendSTRSTRResult(String value)
  {
    this.sendSTRSTRResult = value;
  }
}
