package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;attribute name="schemaLocation" use="required" type="{http://www.w3
 *       .org/2001/XMLSchema}anyURI" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "include")
public class Include extends Annotated
{
  @XmlAttribute(name = "schemaLocation", required = true)
  @XmlSchemaType(name = "anyURI")
  protected String schemaLocation;

  /**
   * Gets the value of the schemaLocation property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getSchemaLocation()
  {
    return schemaLocation;
  }

  /**
   * Sets the value of the schemaLocation property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setSchemaLocation(String value)
  {
    this.schemaLocation = value;
  }
}
