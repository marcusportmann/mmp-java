
package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *    group type for explicit groups, named top-level groups and
 *    group references
 *
 * <p>Java class for group complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="group">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;group ref="{http://www.w3.org/2001/XMLSchema}particle" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}defRef"/>
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}occurs"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "group", propOrder = { "elementsAndGroupsAndAlls" })
@XmlSeeAlso({ ExplicitGroup.class, RealGroup.class })
public abstract class GroupType extends Annotated
{
  @XmlElementRefs({ @XmlElementRef(name = "any", namespace = "http://www.w3.org/2001/XMLSchema",
      type = Any.class, required = false) ,
      @XmlElementRef(name = "sequence", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "choice", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "group", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "element", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) ,
      @XmlElementRef(name = "all", namespace = "http://www.w3.org/2001/XMLSchema",
      type = JAXBElement.class, required = false) })
  protected List<Object> elementsAndGroupsAndAlls;
  @XmlAttribute(name = "maxOccurs")
  @XmlSchemaType(name = "allNNI")
  protected String maxOccurs;
  @XmlAttribute(name = "minOccurs")
  @XmlSchemaType(name = "nonNegativeInteger")
  protected BigInteger minOccurs;
  @XmlAttribute(name = "name")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NCName")
  protected String name;
  @XmlAttribute(name = "ref")
  protected QName ref;

  /**
   * Gets the value of the elementsAndGroupsAndAlls property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the elementsAndGroupsAndAlls property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getElementsAndGroupsAndAlls().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link JAXBElement }{@code <}{@link LocalElement }{@code >}
   * {@link Any }
   * {@link JAXBElement }{@code <}{@link All }{@code >}
   * {@link JAXBElement }{@code <}{@link ExplicitGroup }{@code >}
   * {@link JAXBElement }{@code <}{@link ExplicitGroup }{@code >}
   * {@link JAXBElement }{@code <}{@link GroupRef }{@code >}
   *
   *
   *
   * @return
   */
  public List<Object> getElementsAndGroupsAndAlls()
  {
    if (elementsAndGroupsAndAlls == null)
    {
      elementsAndGroupsAndAlls = new ArrayList<Object>();
    }

    return this.elementsAndGroupsAndAlls;
  }

  /**
   * Gets the value of the maxOccurs property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getMaxOccurs()
  {
    if (maxOccurs == null)
    {
      return "1";
    }
    else
    {
      return maxOccurs;
    }
  }

  /**
   * Gets the value of the minOccurs property.
   *
   * @return
   *     possible object is
   *     {@link BigInteger }
   *
   */
  public BigInteger getMinOccurs()
  {
    if (minOccurs == null)
    {
      return new BigInteger("1");
    }
    else
    {
      return minOccurs;
    }
  }

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
   * Gets the value of the ref property.
   *
   * @return
   *     possible object is
   *     {@link QName }
   *
   */
  public QName getRef()
  {
    return ref;
  }

  /**
   * Sets the value of the maxOccurs property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setMaxOccurs(String value)
  {
    this.maxOccurs = value;
  }

  /**
   * Sets the value of the minOccurs property.
   *
   * @param value
   *     allowed object is
   *     {@link BigInteger }
   *
   */
  public void setMinOccurs(BigInteger value)
  {
    this.minOccurs = value;
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
   * Sets the value of the ref property.
   *
   * @param value
   *     allowed object is
   *     {@link QName }
   *
   */
  public void setRef(QName value)
  {
    this.ref = value;
  }
}
