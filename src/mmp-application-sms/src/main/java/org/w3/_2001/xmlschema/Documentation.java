package org.w3._2001.xmlschema;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;any processContents='lax'/>
 *       &lt;/sequence>
 *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "content" })
@XmlRootElement(name = "documentation")
public class Documentation
{
  @XmlAnyAttribute
  private Map<QName, String> otherAttributes = new HashMap<QName, String>();
  @XmlMixed
  @XmlAnyElement(lax = true)
  protected List<Object> content;
  @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
  protected String lang;
  @XmlAttribute(name = "source")
  @XmlSchemaType(name = "anyURI")
  protected String source;

  /**
   * Gets the value of the content property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the content property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getContent().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   * {@link Object }
   * {@link Element }
   *
   * @return
   */
  public List<Object> getContent()
  {
    if (content == null)
    {
      content = new ArrayList<Object>();
    }

    return this.content;
  }

  /**
   * Gets the value of the lang property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getLang()
  {
    return lang;
  }

  /**
   * Gets a map that contains attributes that aren't bound to any typed property on this class.
   * <p/>
   * <p/>
   * the map is keyed by the name of the attribute and
   * the value is the string value of the attribute.
   * <p/>
   * the map returned by this method is live, and you can add new attribute
   * by updating the map directly. Because of this design, there's no setter.
   *
   * @return always non-null
   */
  public Map<QName, String> getOtherAttributes()
  {
    return otherAttributes;
  }

  /**
   * Gets the value of the source property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getSource()
  {
    return source;
  }

  /**
   * Sets the value of the lang property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setLang(String value)
  {
    this.lang = value;
  }

  /**
   * Sets the value of the source property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setSource(String value)
  {
    this.source = value;
  }
}
