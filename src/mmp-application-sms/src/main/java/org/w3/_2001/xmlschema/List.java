
package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;

/**
 *
 *           itemType attribute and simpleType child are mutually
 *           exclusive, but one or other is required
 *
 *
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;sequence>
 *         &lt;element name="simpleType" type="{http://www.w3.org/2001/XMLSchema}localSimpleType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="itemType" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "simpleType" })
@XmlRootElement(name = "list")
public class List extends Annotated
{
  @XmlAttribute(name = "itemType")
  protected QName itemType;
  protected LocalSimpleType simpleType;

  /**
   * Gets the value of the itemType property.
   *
   * @return
   *     possible object is
   *     {@link QName }
   *
   */
  public QName getItemType()
  {
    return itemType;
  }

  /**
   * Gets the value of the simpleType property.
   *
   * @return
   *     possible object is
   *     {@link LocalSimpleType }
   *
   */
  public LocalSimpleType getSimpleType()
  {
    return simpleType;
  }

  /**
   * Sets the value of the itemType property.
   *
   * @param value
   *     allowed object is
   *     {@link QName }
   *
   */
  public void setItemType(QName value)
  {
    this.itemType = value;
  }

  /**
   * Sets the value of the simpleType property.
   *
   * @param value
   *     allowed object is
   *     {@link LocalSimpleType }
   *
   */
  public void setSimpleType(LocalSimpleType value)
  {
    this.simpleType = value;
  }
}
