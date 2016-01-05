package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}wildcard">
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}occurs"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "any")
public class Any
  extends Wildcard
{
  @XmlAttribute(name = "maxOccurs")
  @XmlSchemaType(name = "allNNI")
  protected String maxOccurs;

  @XmlAttribute(name = "minOccurs")
  @XmlSchemaType(name = "nonNegativeInteger")
  protected BigInteger minOccurs;

  /**
   * Gets the value of the maxOccurs property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getMaxOccurs()
  {
    if (maxOccurs == null)
    {
      return "1";
    }
    else
    {
      return maxOccurs;
    }
  }

  /**
   * Sets the value of the maxOccurs property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setMaxOccurs(String value)
  {
    this.maxOccurs = value;
  }

  /**
   * Gets the value of the minOccurs property.
   *
   * @return possible object is
   * {@link BigInteger }
   */
  public BigInteger getMinOccurs()
  {
    if (minOccurs == null)
    {
      return new BigInteger("1");
    }
    else
    {
      return minOccurs;
    }
  }

  /**
   * Sets the value of the minOccurs property.
   *
   * @param value allowed object is
   *              {@link BigInteger }
   */
  public void setMinOccurs(BigInteger value)
  {
    this.minOccurs = value;
  }
}
