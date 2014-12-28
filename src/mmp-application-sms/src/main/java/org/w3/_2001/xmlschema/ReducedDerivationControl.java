
package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for reducedDerivationControl.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="reducedDerivationControl">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}derivationControl">
 *     &lt;enumeration value="extension"/>
 *     &lt;enumeration value="restriction"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "reducedDerivationControl")
@XmlEnum(DerivationControl.class)
public enum ReducedDerivationControl
{
  @XmlEnumValue("extension")
  EXTENSION(DerivationControl.EXTENSION),
  @XmlEnumValue("restriction")
  RESTRICTION(DerivationControl.RESTRICTION);

  private final DerivationControl value;

  ReducedDerivationControl(DerivationControl v)
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
  public static ReducedDerivationControl fromValue(DerivationControl v)
  {
    for (ReducedDerivationControl c : ReducedDerivationControl.values())
    {
      if (c.value.equals(v))
      {
        return c;
      }
    }

    throw new IllegalArgumentException(v.toString());
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
