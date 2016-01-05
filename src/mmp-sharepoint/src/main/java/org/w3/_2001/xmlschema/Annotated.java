package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This type is extended by all types which allow annotation
 * other than <schema> itself
 * <p/>
 * <p/>
 * <p>Java class for annotated complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="annotated">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}openAttrs">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.w3.org/2001/XMLSchema}annotation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "annotated", propOrder = {"annotation"})
@XmlSeeAlso({ExtensionType.class, RestrictionType.class, Notation.class, AttributeType.class,
  Field.class, Selector.class, Keybase.class, ElementType.class, AttributeGroupType.class,
  Wildcard.class, GroupType.class, SimpleContent.class, ComplexContent.class, ComplexTypeType.class,
  Facet.class, Restriction.class, List.class, Union.class, SimpleTypeType.class, Import.class,
  Include.class})
public class Annotated
  extends OpenAttrs
{
  protected Annotation annotation;

  @XmlAttribute(name = "id")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlID
  @XmlSchemaType(name = "ID")
  protected String id;

  /**
   * Gets the value of the annotation property.
   *
   * @return possible object is
   * {@link Annotation }
   */
  public Annotation getAnnotation()
  {
    return annotation;
  }

  /**
   * Sets the value of the annotation property.
   *
   * @param value allowed object is
   *              {@link Annotation }
   */
  public void setAnnotation(Annotation value)
  {
    this.annotation = value;
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
   * Sets the value of the id property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setId(String value)
  {
    this.id = value;
  }
}
