package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}openAttrs">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{http://www.w3.org/2001/XMLSchema}annotation"/>
 *         &lt;group ref="{http://www.w3.org/2001/XMLSchema}redefinable"/>
 *       &lt;/choice>
 *       &lt;attribute name="schemaLocation" use="required" type="{http://www.w3
 *       .org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"annotationsAndSimpleTypesAndComplexTypes"})
@XmlRootElement(name = "redefine")
public class Redefine
  extends OpenAttrs
{
  @XmlElements({@XmlElement(name = "annotation", type = Annotation.class),
    @XmlElement(name = "simpleType", type = SimpleType.class),
    @XmlElement(name = "complexType", type = ComplexType.class),
    @XmlElement(name = "group", type = Group.class),
    @XmlElement(name = "attributeGroup", type = AttributeGroup.class)})
  protected List<OpenAttrs> annotationsAndSimpleTypesAndComplexTypes;

  @XmlAttribute(name = "id")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlID
  @XmlSchemaType(name = "ID")
  protected String id;

  @XmlAttribute(name = "schemaLocation", required = true)
  @XmlSchemaType(name = "anyURI")
  protected String schemaLocation;

  /**
   * Gets the value of the annotationsAndSimpleTypesAndComplexTypes property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the
   * annotationsAndSimpleTypesAndComplexTypes property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAnnotationsAndSimpleTypesAndComplexTypes().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link Annotation }
   * {@link SimpleType }
   * {@link ComplexType }
   * {@link Group }
   * {@link AttributeGroup }
   *
   * @return
   */
  public List<OpenAttrs> getAnnotationsAndSimpleTypesAndComplexTypes()
  {
    if (annotationsAndSimpleTypesAndComplexTypes == null)
    {
      annotationsAndSimpleTypesAndComplexTypes = new ArrayList<OpenAttrs>();
    }

    return this.annotationsAndSimpleTypesAndComplexTypes;
  }

  /**
   * Gets the value of the id property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getId()
  {
    return id;
  }

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
   * Sets the value of the id property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setId(String value)
  {
    this.id = value;
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
