
package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for extensionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="extensionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.w3.org/2001/XMLSchema}typeDefParticle" minOccurs="0"/>
 *         &lt;group ref="{http://www.w3.org/2001/XMLSchema}attrDecls"/>
 *       &lt;/sequence>
 *       &lt;attribute name="base" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extensionType",
    propOrder = { "sequence", "choice", "all", "group", "attributesAndAttributeGroups",
    "anyAttribute" })
@XmlSeeAlso({ SimpleExtensionType.class })
public class ExtensionType extends Annotated
{
  protected All all;
  protected Wildcard anyAttribute;
  @XmlElements({ @XmlElement(name = "attribute", type = AttributeType.class) ,
      @XmlElement(name = "attributeGroup", type = AttributeGroupRef.class) })
  protected List<Annotated> attributesAndAttributeGroups;
  @XmlAttribute(name = "base", required = true)
  protected QName base;
  protected ExplicitGroup choice;
  protected GroupRef group;
  protected ExplicitGroup sequence;

  /**
   * Gets the value of the all property.
   *
   * @return
   *     possible object is
   *     {@link All }
   *
   */
  public All getAll()
  {
    return all;
  }

  /**
   * Gets the value of the anyAttribute property.
   *
   * @return
   *     possible object is
   *     {@link Wildcard }
   *
   */
  public Wildcard getAnyAttribute()
  {
    return anyAttribute;
  }

  /**
   * Gets the value of the attributesAndAttributeGroups property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the attributesAndAttributeGroups property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAttributesAndAttributeGroups().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link AttributeType }
   * {@link AttributeGroupRef }
   *
   *
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
   * Gets the value of the base property.
   *
   * @return
   *     possible object is
   *     {@link QName }
   *
   */
  public QName getBase()
  {
    return base;
  }

  /**
   * Gets the value of the choice property.
   *
   * @return
   *     possible object is
   *     {@link ExplicitGroup }
   *
   */
  public ExplicitGroup getChoice()
  {
    return choice;
  }

  /**
   * Gets the value of the group property.
   *
   * @return
   *     possible object is
   *     {@link GroupRef }
   *
   */
  public GroupRef getGroup()
  {
    return group;
  }

  /**
   * Gets the value of the sequence property.
   *
   * @return
   *     possible object is
   *     {@link ExplicitGroup }
   *
   */
  public ExplicitGroup getSequence()
  {
    return sequence;
  }

  /**
   * Sets the value of the all property.
   *
   * @param value
   *     allowed object is
   *     {@link All }
   *
   */
  public void setAll(All value)
  {
    this.all = value;
  }

  /**
   * Sets the value of the anyAttribute property.
   *
   * @param value
   *     allowed object is
   *     {@link Wildcard }
   *
   */
  public void setAnyAttribute(Wildcard value)
  {
    this.anyAttribute = value;
  }

  /**
   * Sets the value of the base property.
   *
   * @param value
   *     allowed object is
   *     {@link QName }
   *
   */
  public void setBase(QName value)
  {
    this.base = value;
  }

  /**
   * Sets the value of the choice property.
   *
   * @param value
   *     allowed object is
   *     {@link ExplicitGroup }
   *
   */
  public void setChoice(ExplicitGroup value)
  {
    this.choice = value;
  }

  /**
   * Sets the value of the group property.
   *
   * @param value
   *     allowed object is
   *     {@link GroupRef }
   *
   */
  public void setGroup(GroupRef value)
  {
    this.group = value;
  }

  /**
   * Sets the value of the sequence property.
   *
   * @param value
   *     allowed object is
   *     {@link ExplicitGroup }
   *
   */
  public void setSequence(ExplicitGroup value)
  {
    this.sequence = value;
  }
}
