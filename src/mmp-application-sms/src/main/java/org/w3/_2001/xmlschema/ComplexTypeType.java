package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for complexType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="complexType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;group ref="{http://www.w3.org/2001/XMLSchema}complexTypeModel"/>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="mixed" type="{http://www.w3.org/2001/XMLSchema}boolean"
 *       default="false" />
 *       &lt;attribute name="abstract" type="{http://www.w3.org/2001/XMLSchema}boolean"
 *       default="false" />
 *       &lt;attribute name="final" type="{http://www.w3.org/2001/XMLSchema}derivationSet" />
 *       &lt;attribute name="block" type="{http://www.w3.org/2001/XMLSchema}derivationSet" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "complexType",
  propOrder = {"sequence", "choice", "all", "group", "attributesAndAttributeGroups", "anyAttribute",
    "complexContent", "simpleContent"})
@XmlSeeAlso({LocalComplexType.class, ComplexType.class})
public abstract class ComplexTypeType
  extends Annotated
{
  @XmlAttribute(name = "abstract")
  protected Boolean _abstract;

  protected All all;

  protected Wildcard anyAttribute;

  @XmlElements({@XmlElement(name = "attribute", type = AttributeType.class),
    @XmlElement(name = "attributeGroup", type = AttributeGroupRef.class)})
  protected List<Annotated> attributesAndAttributeGroups;

  @XmlAttribute(name = "block")
  @XmlSchemaType(name = "derivationSet")
  protected List<String> blocks;

  protected ExplicitGroup choice;

  protected ComplexContent complexContent;

  @XmlAttribute(name = "final")
  @XmlSchemaType(name = "derivationSet")
  protected List<String> finals;

  protected GroupRef group;

  @XmlAttribute(name = "mixed")
  protected Boolean mixed;

  @XmlAttribute(name = "name")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NCName")
  protected String name;

  protected ExplicitGroup sequence;

  protected SimpleContent simpleContent;

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
   * Gets the value of the blocks property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the blocks property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getBlocks().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   *
   * @return
   */
  public List<String> getBlocks()
  {
    if (blocks == null)
    {
      blocks = new ArrayList<String>();
    }

    return this.blocks;
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
   * Gets the value of the complexContent property.
   *
   * @return possible object is
   * {@link ComplexContent }
   */
  public ComplexContent getComplexContent()
  {
    return complexContent;
  }

  /**
   * Sets the value of the complexContent property.
   *
   * @param value allowed object is
   *              {@link ComplexContent }
   */
  public void setComplexContent(ComplexContent value)
  {
    this.complexContent = value;
  }

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
  public List<String> getFinals()
  {
    if (finals == null)
    {
      finals = new ArrayList<String>();
    }

    return this.finals;
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
   * Gets the value of the simpleContent property.
   *
   * @return possible object is
   * {@link SimpleContent }
   */
  public SimpleContent getSimpleContent()
  {
    return simpleContent;
  }

  /**
   * Sets the value of the simpleContent property.
   *
   * @param value allowed object is
   *              {@link SimpleContent }
   */
  public void setSimpleContent(SimpleContent value)
  {
    this.simpleContent = value;
  }

  /**
   * Gets the value of the abstract property.
   *
   * @return possible object is
   * {@link Boolean }
   */
  public boolean isAbstract()
  {
    if (_abstract == null)
    {
      return false;
    }
    else
    {
      return _abstract;
    }
  }

  /**
   * Sets the value of the abstract property.
   *
   * @param value allowed object is
   *              {@link Boolean }
   */
  public void setAbstract(Boolean value)
  {
    this._abstract = value;
  }

  /**
   * Gets the value of the mixed property.
   *
   * @return possible object is
   * {@link Boolean }
   */
  public boolean isMixed()
  {
    if (mixed == null)
    {
      return false;
    }
    else
    {
      return mixed;
    }
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
}
