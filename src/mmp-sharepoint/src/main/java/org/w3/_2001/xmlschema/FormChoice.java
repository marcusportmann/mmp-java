package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for formChoice.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="formChoice">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="qualified"/>
 *     &lt;enumeration value="unqualified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "formChoice")
@XmlEnum
public enum FormChoice
{
  @XmlEnumValue("qualified")
  QUALIFIED("qualified"),
  @XmlEnumValue("unqualified")
  UNQUALIFIED("unqualified");

  private final String value;

  /**
   * Method description
   *
   * @param v
   *
   * @return
   */
  public static FormChoice fromValue(String v)
  {
    for (FormChoice c : FormChoice.values())
    {
      if (c.value.equals(v))
      {
        return c;
      }
    }

    throw new IllegalArgumentException(v);
  }

  FormChoice(String v)
  {
    value = v;
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
