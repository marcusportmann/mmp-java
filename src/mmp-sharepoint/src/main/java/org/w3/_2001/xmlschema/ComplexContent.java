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
 *       &lt;choice>
 *         &lt;element name="restriction" type="{http://www.w3
 *         .org/2001/XMLSchema}complexRestrictionType"/>
 *         &lt;element name="extension" type="{http://www.w3.org/2001/XMLSchema}extensionType"/>
 *       &lt;/choice>
 *       &lt;attribute name="mixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "extension", "restriction" })
@XmlRootElement(name = "complexContent")
public class ComplexContent extends Annotated
{
  protected ExtensionType extension;
  @XmlAttribute(name = "mixed")
  protected Boolean mixed;
  protected ComplexRestrictionType restriction;

  /**
   * Gets the value of the extension property.
   *
   * @return possible object is
   * {@link ExtensionType }
   */
  public ExtensionType getExtension()
  {
    return extension;
  }

  /**
   * Gets the value of the restriction property.
   *
   * @return possible object is
   * {@link ComplexRestrictionType }
   */
  public ComplexRestrictionType getRestriction()
  {
    return restriction;
  }

  /**
   * Gets the value of the mixed property.
   *
   * @return possible object is
   * {@link Boolean }
   */
  public Boolean isMixed()
  {
    return mixed;
  }

  /**
   * Sets the value of the extension property.
   *
   * @param value allowed object is
   *              {@link ExtensionType }
   */
  public void setExtension(ExtensionType value)
  {
    this.extension = value;
  }

  /**
   * Sets the value of the mixed property.
   *
   * @param value allowed object is
   *              {@link Boolean }
   */
  public void setMixed(Boolean value)
  {
    this.mixed = value;
  }

  /**
   * Sets the value of the restriction property.
   *
   * @param value allowed object is
   *              {@link ComplexRestrictionType }
   */
  public void setRestriction(ComplexRestrictionType value)
  {
    this.restriction = value;
  }
}
