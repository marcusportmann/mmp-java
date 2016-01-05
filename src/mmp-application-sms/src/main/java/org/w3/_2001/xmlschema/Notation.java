package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="public" type="{http://www.w3.org/2001/XMLSchema}public" />
 *       &lt;attribute name="system" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "notation")
public class Notation
  extends Annotated
{
  @XmlAttribute(name = "public")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "public")
  protected String _public;

  @XmlAttribute(name = "name", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NCName")
  protected String name;

  @XmlAttribute(name = "system")
  @XmlSchemaType(name = "anyURI")
  protected String system;

  /**
   * Gets the value of the name property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setName(String value)
  {
    this.name = value;
  }

  /**
   * Gets the value of the public property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getPublic()
  {
    return _public;
  }

  /**
   * Sets the value of the public property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setPublic(String value)
  {
    this._public = value;
  }

  /**
   * Gets the value of the system property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getSystem()
  {
    return system;
  }

  /**
   * Sets the value of the system property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setSystem(String value)
  {
    this.system = value;
  }
}
