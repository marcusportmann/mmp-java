
package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for derivationControl.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="derivationControl">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="substitution"/>
 *     &lt;enumeration value="extension"/>
 *     &lt;enumeration value="restriction"/>
 *     &lt;enumeration value="list"/>
 *     &lt;enumeration value="union"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "derivationControl")
@XmlEnum
public enum DerivationControl
{
  @XmlEnumValue("substitution")
  SUBSTITUTION("substitution"),
  @XmlEnumValue("extension")
  EXTENSION("extension"),
  @XmlEnumValue("restriction")
  RESTRICTION("restriction"),
  @XmlEnumValue("list")
  LIST("list"),
  @XmlEnumValue("union")
  UNION("union");

  private final String value;

  DerivationControl(String v)
  {
    value = v;
  }

  /**
   * Method description
   *
   * @param v
   *
   * @return
   */
  public static DerivationControl fromValue(String v)
  {
    for (DerivationControl c : DerivationControl.values())
    {
      if (c.value.equals(v))
      {
        return c;
      }
    }

    throw new IllegalArgumentException(v);
  }

  /**
   * Method description
   *
   * @return
   */
  public String value()
  {
    return value;
  }
}
