
package guru.mmp.service.codes.ws;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 *
 *             The Parameter type holds the information for a parameter.
 *
 *
 * <p>Java class for Parameter complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Parameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Parameter", propOrder = { "name", "value" })
public class Parameter
  implements Serializable
{
  private final static long serialVersionUID = 1000000L;
  @XmlElement(name = "Name", required = true)
  protected String name;
  @XmlElement(name = "Value", required = true)
  protected String value;

  /**
   * Gets the value of the name property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getName()
  {
    return name;
  }

  /**
   * Gets the value of the value property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setName(String value)
  {
    this.name = value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}
