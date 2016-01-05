package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for typeDerivationControl.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="typeDerivationControl">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}derivationControl">
 *     &lt;enumeration value="extension"/>
 *     &lt;enumeration value="restriction"/>
 *     &lt;enumeration value="list"/>
 *     &lt;enumeration value="union"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "typeDerivationControl")
@XmlEnum(DerivationControl.class)
public enum TypeDerivationControl
{
  @XmlEnumValue("extension")
  EXTENSION(DerivationControl.EXTENSION),
  @XmlEnumValue("restriction")
  RESTRICTION(DerivationControl.RESTRICTION),
  @XmlEnumValue("list")
  LIST(DerivationControl.LIST),
  @XmlEnumValue("union")
  UNION(DerivationControl.UNION);

  private final DerivationControl value;

  /**
   * Method description
   *
   * @param v
   *
   * @return
   */
  public static TypeDerivationControl fromValue(DerivationControl v)
  {
    for (TypeDerivationControl c : TypeDerivationControl.values())
    {
      if (c.value.equals(v))
      {
        return c;
      }
    }

    throw new IllegalArgumentException(v.toString());
  }

  TypeDerivationControl(DerivationControl v)
  {
    value = v;
  }

  /**
   * Method description
   *
   * @return
   */
  public DerivationControl value()
  {
    return value;
  }
}
