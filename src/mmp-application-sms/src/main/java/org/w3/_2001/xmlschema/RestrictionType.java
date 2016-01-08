package org.w3._2001.xmlschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for restrictionType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="restrictionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;sequence>
 *         &lt;choice minOccurs="0">
 *           &lt;group ref="{http://www.w3.org/2001/XMLSchema}typeDefParticle"/>
 *           &lt;group ref="{http://www.w3.org/2001/XMLSchema}simpleRestrictionModel"/>
 *         &lt;/choice>
 *         &lt;group ref="{http://www.w3.org/2001/XMLSchema}attrDecls"/>
 *       &lt;/sequence>
 *       &lt;attribute name="base" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "restrictionType",
  propOrder = {"simpleType", "minExclusivesAndMinInclusivesAndMaxExclusives", "sequence", "choice",
    "all", "group", "attributesAndAttributeGroups", "anyAttribute"})
@XmlSeeAlso({ComplexRestrictionType.class, SimpleRestrictionType.class})
public class RestrictionType
  extends Annotated
{
  protected All all;

  protected Wildcard anyAttribute;

  @XmlElements({@XmlElement(name = "attribute", type = AttributeType.class),
    @XmlElement(name = "attributeGroup", type = AttributeGroupRef.class)})
  protected List<Annotated> attributesAndAttributeGroups;

  @XmlAttribute(name = "base", required = true)
  protected QName base;

  protected ExplicitGroup choice;

  protected GroupRef group;

  @XmlElementRefs({@XmlElementRef(name = "minInclusive",
    namespace = "http://www.w3.org/2001/XMLSchema", type = JAXBElement.class, required = false),
    @XmlElementRef(name = "maxExclusive", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false), @XmlElementRef(name = "fractionDigits",
    namespace = "http://www.w3.org/2001/XMLSchema",
    type = JAXBElement.class, required = false),
    @XmlElementRef(name = "whiteSpace", namespace = "http://www.w3.org/2001/XMLSchema",
      type = WhiteSpace.class, required = false),
    @XmlElementRef(name = "totalDigits", namespace = "http://www.w3.org/2001/XMLSchema",
      type = TotalDigits.class, required = false),
    @XmlElementRef(name = "minLength", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false),
    @XmlElementRef(name = "enumeration", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false), @XmlElementRef(name = "maxInclusive",
    namespace = "http://www.w3.org/2001/XMLSchema",
    type = JAXBElement.class, required = false), @XmlElementRef(name = "minExclusive",
    namespace = "http://www.w3.org/2001/XMLSchema",
    type = JAXBElement.class, required = false),
    @XmlElementRef(name = "maxLength", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false),
    @XmlElementRef(name = "pattern", namespace = "http://www.w3.org/2001/XMLSchema",
      type = Pattern.class, required = false),
    @XmlElementRef(name = "length", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false)})
  protected List<Object> minExclusivesAndMinInclusivesAndMaxExclusives;

  protected ExplicitGroup sequence;

  protected LocalSimpleType simpleType;

  /**
   * Gets the value of the all property.
   *
   * @return possible object is
   * {@link All }
   */
  public All getAll()
  {
    return all;
  }

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
   * Gets the value of the base property.
   *
   * @return possible object is
   * {@link QName }
   */
  public QName getBase()
  {
    return base;
  }

  /**
   * Gets the value of the choice property.
   *
   * @return possible object is
   * {@link ExplicitGroup }
   */
  public ExplicitGroup getChoice()
  {
    return choice;
  }

  /**
   * Gets the value of the group property.
   *
   * @return possible object is
   * {@link GroupRef }
   */
  public GroupRef getGroup()
  {
    return group;
  }

  /**
   * Gets the value of the minExclusivesAndMinInclusivesAndMaxExclusives property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the
   * minExclusivesAndMinInclusivesAndMaxExclusives property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getMinExclusivesAndMinInclusivesAndMaxExclusives().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link JAXBElement }{@code <}{@link Facet }{@code >}
   * {@link JAXBElement }{@code <}{@link Facet }{@code >}
   * {@link JAXBElement }{@code <}{@link NumFacet }{@code >}
   * {@link WhiteSpace }
   * {@link TotalDigits }
   * {@link JAXBElement }{@code <}{@link NumFacet }{@code >}
   * {@link JAXBElement }{@code <}{@link NoFixedFacet }{@code >}
   * {@link JAXBElement }{@code <}{@link Facet }{@code >}
   * {@link JAXBElement }{@code <}{@link Facet }{@code >}
   * {@link JAXBElement }{@code <}{@link NumFacet }{@code >}
   * {@link Pattern }
   * {@link JAXBElement }{@code <}{@link NumFacet }{@code >}
   *
   * @return
   */
  public List<Object> getMinExclusivesAndMinInclusivesAndMaxExclusives()
  {
    if (minExclusivesAndMinInclusivesAndMaxExclusives == null)
    {
      minExclusivesAndMinInclusivesAndMaxExclusives = new ArrayList<Object>();
    }

    return this.minExclusivesAndMinInclusivesAndMaxExclusives;
  }

  /**
   * Gets the value of the sequence property.
   *
   * @return possible object is
   * {@link ExplicitGroup }
   */
  public ExplicitGroup getSequence()
  {
    return sequence;
  }

  /**
   * Gets the value of the simpleType property.
   *
   * @return possible object is
   * {@link LocalSimpleType }
   */
  public LocalSimpleType getSimpleType()
  {
    return simpleType;
  }

  /**
   * Sets the value of the all property.
   *
   * @param value allowed object is
   *              {@link All }
   */
  public void setAll(All value)
  {
    this.all = value;
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
   * Sets the value of the base property.
   *
   * @param value allowed object is
   *              {@link QName }
   */
  public void setBase(QName value)
  {
    this.base = value;
  }

  /**
   * Sets the value of the choice property.
   *
   * @param value allowed object is
   *              {@link ExplicitGroup }
   */
  public void setChoice(ExplicitGroup value)
  {
    this.choice = value;
  }

  /**
   * Sets the value of the group property.
   *
   * @param value allowed object is
   *              {@link GroupRef }
   */
  public void setGroup(GroupRef value)
  {
    this.group = value;
  }

  /**
   * Sets the value of the sequence property.
   *
   * @param value allowed object is
   *              {@link ExplicitGroup }
   */
  public void setSequence(ExplicitGroup value)
  {
    this.sequence = value;
  }

  /**
   * Sets the value of the simpleType property.
   *
   * @param value allowed object is
   *              {@link LocalSimpleType }
   */
  public void setSimpleType(LocalSimpleType value)
  {
    this.simpleType = value;
  }
}
