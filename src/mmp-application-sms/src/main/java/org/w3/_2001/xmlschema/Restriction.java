
package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *           base attribute and simpleType child are mutually
 *           exclusive, but one or other is required
 *
 *
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;group ref="{http://www.w3.org/2001/XMLSchema}simpleRestrictionModel"/>
 *       &lt;attribute name="base" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "simpleType", "minExclusivesAndMinInclusivesAndMaxExclusives" })
@XmlRootElement(name = "restriction")
public class Restriction extends Annotated
{
  @XmlAttribute(name = "base")
  protected QName base;
  @XmlElementRefs({ @XmlElementRef(name = "minInclusive",
      namespace = "http://www.w3.org/2001/XMLSchema", type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "maxExclusive", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "fractionDigits", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "whiteSpace", namespace = "http://www.w3.org/2001/XMLSchema",
      type = WhiteSpace.class, required = false) ,
      @XmlElementRef(name = "totalDigits", namespace = "http://www.w3.org/2001/XMLSchema",
      type = TotalDigits.class, required = false) ,
      @XmlElementRef(name = "minLength", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "enumeration", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "maxInclusive", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "minExclusive", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "maxLength", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "pattern", namespace = "http://www.w3.org/2001/XMLSchema",
      type = Pattern.class, required = false) ,
      @XmlElementRef(name = "length", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) })
  protected List<Object> minExclusivesAndMinInclusivesAndMaxExclusives;
  protected LocalSimpleType simpleType;

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
   * Gets the value of the minExclusivesAndMinInclusivesAndMaxExclusives property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the minExclusivesAndMinInclusivesAndMaxExclusives property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getMinExclusivesAndMinInclusivesAndMaxExclusives().add(newItem);
   * </pre>
   *
   *
   * <p>
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
   *
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
   * Gets the value of the simpleType property.
   *
   * @return
   *     possible object is
   *     {@link LocalSimpleType }
   *
   */
  public LocalSimpleType getSimpleType()
  {
    return simpleType;
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
   * Sets the value of the simpleType property.
   *
   * @param value
   *     allowed object is
   *     {@link LocalSimpleType }
   *
   */
  public void setSimpleType(LocalSimpleType value)
  {
    this.simpleType = value;
  }
}
