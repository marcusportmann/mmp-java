package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * This type is extended by almost all schema types
 * to allow attributes from other namespaces to be
 * added to user schemas.
 * <p/>
 * <p/>
 * <p>Java class for openAttrs complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="openAttrs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "openAttrs")
@XmlSeeAlso({ Redefine.class, Annotation.class, Annotated.class, Schema.class })
public class OpenAttrs
{
  @XmlAnyAttribute
  private Map<QName, String> otherAttributes = new HashMap<QName, String>();

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
}
