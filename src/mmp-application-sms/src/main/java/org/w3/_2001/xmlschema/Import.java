
package org.w3._2001.xmlschema;

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
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="schemaLocation" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "import")
public class Import extends Annotated
{
  @XmlAttribute(name = "namespace")
  @XmlSchemaType(name = "anyURI")
  protected String namespace;
  @XmlAttribute(name = "schemaLocation")
  @XmlSchemaType(name = "anyURI")
  protected String schemaLocation;

  /**
   * Gets the value of the namespace property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getNamespace()
  {
    return namespace;
  }

  /**
   * Gets the value of the schemaLocation property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getSchemaLocation()
  {
    return schemaLocation;
  }

  /**
   * Sets the value of the namespace property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setNamespace(String value)
  {
    this.namespace = value;
  }

  /**
   * Sets the value of the schemaLocation property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setSchemaLocation(String value)
  {
    this.schemaLocation = value;
  }
}
