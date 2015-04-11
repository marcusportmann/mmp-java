
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
 *         &lt;element name="Username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Data" minOccurs="0">
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
@XmlType(name = "", propOrder = { "username", "password", "data" })
@XmlRootElement(name = "Sent_DS_DS")
public class SentDSDS
{
  @XmlElement(name = "Data")
  protected SentDSDS.Data data;
  @XmlElement(name = "Password")
  protected String password;
  @XmlElement(name = "Username")
  protected String username;

  /**
   * Gets the value of the data property.
   *
   * @return
   *     possible object is
   *     {@link SentDSDS.Data }
   *
   */
  public SentDSDS.Data getData()
  {
    return data;
  }

  /**
   * Gets the value of the password property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Gets the value of the username property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Sets the value of the data property.
   *
   * @param value
   *     allowed object is
   *     {@link SentDSDS.Data }
   *
   */
  public void setData(SentDSDS.Data value)
  {
    this.data = value;
  }

  /**
   * Sets the value of the password property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setPassword(String value)
  {
    this.password = value;
  }

  /**
   * Sets the value of the username property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setUsername(String value)
  {
    this.username = value;
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
  public static class Data
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
