package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for attributeGroup complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="attributeGroup">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;group ref="{http://www.w3.org/2001/XMLSchema}attrDecls"/>
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}defRef"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributeGroup", propOrder = { "attributesAndAttributeGroups", "anyAttribute" })
@XmlSeeAlso({ AttributeGroupRef.class, AttributeGroup.class })
public abstract class AttributeGroupType extends Annotated
{
  protected Wildcard anyAttribute;
  @XmlElements({ @XmlElement(name = "attribute", type = AttributeType.class) ,
      @XmlElement(name = "attributeGroup", type = AttributeGroupRef.class) })
  protected List<Annotated> attributesAndAttributeGroups;
  @XmlAttribute(name = "name")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NCName")
  protected String name;
  @XmlAttribute(name = "ref")
  protected QName ref;

  /**
   * Gets the value of the anyAttribute property.
   *
   * @return possible object is
   * {@link Wildcard }
   */
  public Wildcard getAnyAttribute()
  {
    return anyAttribute;
  }

  /**
   * Gets the value of the attributesAndAttributeGroups property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the attributesAndAttributeGroups
   * property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAttributesAndAttributeGroups().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link AttributeType }
   * {@link AttributeGroupRef }
   *
   * @return
   */
  public List<Annotated> getAttributesAndAttributeGroups()
  {
    if (attributesAndAttributeGroups == null)
    {
      attributesAndAttributeGroups = new ArrayList<Annotated>();
    }

    return this.attributesAndAttributeGroups;
  }

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
   * Gets the value of the ref property.
   *
   * @return possible object is
   * {@link QName }
   */
  public QName getRef()
  {
    return ref;
  }

  /**
   * Sets the value of the anyAttribute property.
   *
   * @param value allowed object is
   *              {@link Wildcard }
   */
  public void setAnyAttribute(Wildcard value)
  {
    this.anyAttribute = value;
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
   * Sets the value of the ref property.
   *
   * @param value allowed object is
   *              {@link QName }
   */
  public void setRef(QName value)
  {
    this.ref = value;
  }
}
