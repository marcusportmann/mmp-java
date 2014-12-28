
package guru.mmp.services.codes.ws;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out" type="{http://ws.codes.services.mmp.guru}CodeCategory"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "out" })
@XmlRootElement(name = "GetCodeCategoryWithParametersResponse")
public class GetCodeCategoryWithParametersResponse
  implements Serializable
{
  private final static long serialVersionUID = 1000000L;
  @XmlElement(required = true, nillable = true)
  protected CodeCategory out;

  /**
   * Gets the value of the out property.
   *
   * @return possible object is
   * {@link CodeCategory }
   */
  public CodeCategory getOut()
  {
    return out;
  }

  /**
   * Sets the value of the out property.
   *
   * @param value allowed object is
   *              {@link CodeCategory }
   */
  public void setOut(CodeCategory value)
  {
    this.out = value;
  }
}
