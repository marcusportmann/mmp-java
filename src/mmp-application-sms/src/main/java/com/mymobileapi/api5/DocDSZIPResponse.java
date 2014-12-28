
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
 *         &lt;element name="Doc_DS_ZIPResult" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "docDSZIPResult" })
@XmlRootElement(name = "Doc_DS_ZIPResponse")
public class DocDSZIPResponse
{
  @XmlElement(name = "Doc_DS_ZIPResult")
  protected byte[] docDSZIPResult;

  /**
   * Gets the value of the docDSZIPResult property.
   *
   * @return
   *     possible object is
   *     byte[]
   */
  public byte[] getDocDSZIPResult()
  {
    return docDSZIPResult;
  }

  /**
   * Sets the value of the docDSZIPResult property.
   *
   * @param value
   *     allowed object is
   *     byte[]
   */
  public void setDocDSZIPResult(byte[] value)
  {
    this.docDSZIPResult = value;
  }
}
