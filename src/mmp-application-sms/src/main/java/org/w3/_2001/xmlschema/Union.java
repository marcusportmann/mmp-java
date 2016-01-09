package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * memberTypes attribute must be non-empty or there must be
 * at least one simpleType child
 * <p/>
 * <p/>
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;sequence>
 *         &lt;element name="simpleType" type="{http://www.w3.org/2001/XMLSchema}localSimpleType"
 *         maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="memberTypes">
 *         &lt;simpleType>
 *           &lt;list itemType="{http://www.w3.org/2001/XMLSchema}QName" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"simpleTypes"})
@XmlRootElement(name = "union")
public class Union
  extends Annotated
{
  @XmlAttribute(name = "memberTypes")
  protected List<QName> memberTypes;

  @XmlElement(name = "simpleType")
  protected List<LocalSimpleType> simpleTypes;

  /**
   * Gets the value of the memberTypes property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the memberTypes property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getMemberTypes().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link QName }
   *
   * @return
   */
  public List<QName> getMemberTypes()
  {
    if (memberTypes == null)
    {
      memberTypes = new ArrayList<QName>();
    }

    return this.memberTypes;
  }

  /**
   * Gets the value of the simpleTypes property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the simpleTypes property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getSimpleTypes().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link LocalSimpleType }
   *
   * @return
   */
  public List<LocalSimpleType> getSimpleTypes()
  {
    if (simpleTypes == null)
    {
      simpleTypes = new ArrayList<LocalSimpleType>();
    }

    return this.simpleTypes;
  }
}
