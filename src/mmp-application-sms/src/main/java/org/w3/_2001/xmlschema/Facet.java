package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for facet complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="facet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;attribute name="value" use="required" type="{http://www.w3
 *       .org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean"
 *       default="false" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facet")
@XmlSeeAlso({NoFixedFacet.class, WhiteSpace.class, NumFacet.class})
public class Facet
  extends Annotated
{
  @XmlAttribute(name = "fixed")
  protected Boolean fixed;

  @XmlAttribute(name = "value", required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String value;

  /**
   * Gets the value of the value property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Gets the value of the fixed property.
   *
   * @return possible object is
   * {@link Boolean }
   */
  public boolean isFixed()
  {
    if (fixed == null)
    {
      return false;
    }
    else
    {
      return fixed;
    }
  }

  /**
   * Sets the value of the fixed property.
   *
   * @param value allowed object is
   *              {@link Boolean }
   */
  public void setFixed(Boolean value)
  {
    this.fixed = value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}
