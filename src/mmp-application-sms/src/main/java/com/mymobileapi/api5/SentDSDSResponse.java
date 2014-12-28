
package com.mymobileapi.api5;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="Sent_DS_DSResult" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any maxOccurs="2"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sentDSDSResult" })
@XmlRootElement(name = "Sent_DS_DSResponse")
public class SentDSDSResponse
{
  @XmlElement(name = "Sent_DS_DSResult")
  protected SentDSDSResponse.SentDSDSResult sentDSDSResult;

  /**
   * Gets the value of the sentDSDSResult property.
   *
   * @return
   *     possible object is
   *     {@link SentDSDSResponse.SentDSDSResult }
   *
   */
  public SentDSDSResponse.SentDSDSResult getSentDSDSResult()
  {
    return sentDSDSResult;
  }

  /**
   * Sets the value of the sentDSDSResult property.
   *
   * @param value
   *     allowed object is
   *     {@link SentDSDSResponse.SentDSDSResult }
   *
   */
  public void setSentDSDSResult(SentDSDSResponse.SentDSDSResult value)
  {
    this.sentDSDSResult = value;
  }

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
   *         &lt;any maxOccurs="2"/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = { "any" })
  public static class SentDSDSResult
  {
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the any property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     *
     *
     *
     * @return
     */
    public List<Object> getAny()
    {
      if (any == null)
      {
        any = new ArrayList<Object>();
      }

      return this.any;
    }
  }
}
