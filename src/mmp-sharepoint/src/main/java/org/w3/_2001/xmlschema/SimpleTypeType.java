package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;

/**
 * <p>Java class for simpleType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="simpleType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;group ref="{http://www.w3.org/2001/XMLSchema}simpleDerivation"/>
 *       &lt;attribute name="final" type="{http://www.w3.org/2001/XMLSchema}simpleDerivationSet" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simpleType", propOrder = {"union", "list", "restriction"})
@XmlSeeAlso({LocalSimpleType.class, SimpleType.class})
public abstract class SimpleTypeType
  extends Annotated
{
  @XmlAttribute(name = "final")
  @XmlSchemaType(name = "simpleDerivationSet")
  protected java.util.List<String> finals;

  protected org.w3._2001.xmlschema.List list;

  @XmlAttribute(name = "name")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NCName")
  protected String name;

  protected Restriction restriction;

  protected Union union;

  /**
   * Gets the value of the finals property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the finals property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getFinals().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   *
   * @return
   */
  public java.util.List<String> getFinals()
  {
    if (finals == null)
    {
      finals = new ArrayList<String>();
    }

    return this.finals;
  }

  /**
   * Gets the value of the list property.
   *
   * @return possible object is
   * {@link org.w3._2001.xmlschema.List }
   */
  public org.w3._2001.xmlschema.List getList()
  {
    return list;
  }

  /**
   * Sets the value of the list property.
   *
   * @param value allowed object is
   *              {@link org.w3._2001.xmlschema.List }
   */
  public void setList(org.w3._2001.xmlschema.List value)
  {
    this.list = value;
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
   * Gets the value of the restriction property.
   *
   * @return possible object is
   * {@link Restriction }
   */
  public Restriction getRestriction()
  {
    return restriction;
  }

  /**
   * Sets the value of the restriction property.
   *
   * @param value allowed object is
   *              {@link Restriction }
   */
  public void setRestriction(Restriction value)
  {
    this.restriction = value;
  }

  /**
   * Gets the value of the union property.
   *
   * @return possible object is
   * {@link Union }
   */
  public Union getUnion()
  {
    return union;
  }

  /**
   * Sets the value of the union property.
   *
   * @param value allowed object is
   *              {@link Union }
   */
  public void setUnion(Union value)
  {
    this.union = value;
  }
}
