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
 *         &lt;element name="Username" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}base64Binary"
 *         minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"username", "password", "data"})
@XmlRootElement(name = "Send_ZIP_ZIP")
public class SendZIPZIP
{

  @XmlElement(name = "Data")
  protected byte[] data;

  @XmlElement(name = "Password")
  protected String password;

  @XmlElement(name = "Username")
  protected String username;

  /**
   * Gets the value of the data property.
   *
   * @return possible object is
   * byte[]
   */
  public byte[] getData()
  {
    return data;
  }

  /**
   * Gets the value of the password property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Gets the value of the username property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Sets the value of the data property.
   *
   * @param value allowed object is
   *              byte[]
   */
  public void setData(byte[] value)
  {
    this.data = value;
  }

  /**
   * Sets the value of the password property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setPassword(String value)
  {
    this.password = value;
  }

  /**
   * Sets the value of the username property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setUsername(String value)
  {
    this.username = value;
  }
}
