
package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for wildcard complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="wildcard">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}namespaceList" default="##any" />
 *       &lt;attribute name="processContents" default="strict">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="skip"/>
 *             &lt;enumeration value="lax"/>
 *             &lt;enumeration value="strict"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wildcard")
@XmlSeeAlso({ Any.class })
public class Wildcard extends Annotated
{
  @XmlAttribute(name = "namespace")
  @XmlSchemaType(name = "namespaceList")
  protected List<String> namespaces;
  @XmlAttribute(name = "processContents")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String processContents;

  /**
   * Gets the value of the namespaces property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the namespaces property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getNamespaces().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   *
   *
   *
   * @return
   */
  public List<String> getNamespaces()
  {
    if (namespaces == null)
    {
      namespaces = new ArrayList<String>();
    }

    return this.namespaces;
  }

  /**
   * Gets the value of the processContents property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getProcessContents()
  {
    if (processContents == null)
    {
      return "strict";
    }
    else
    {
      return processContents;
    }
  }

  /**
   * Sets the value of the processContents property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setProcessContents(String value)
  {
    this.processContents = value;
  }
}
