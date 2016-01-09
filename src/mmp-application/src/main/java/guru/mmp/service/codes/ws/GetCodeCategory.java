package guru.mmp.service.codes.ws;

import org.w3._2001.xmlschema.Adapter1;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;

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
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastRetrieved" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="returnCodesIfCurrent" type="{http://www.w3
 *         .org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "lastRetrieved", "returnCodesIfCurrent"})
@XmlRootElement(name = "GetCodeCategory")
public class GetCodeCategory
  implements Serializable
{

  private final static long serialVersionUID = 1000000L;

  @XmlElement(required = true)
  protected String id;

  @XmlElement(required = true, type = String.class)
  @XmlJavaTypeAdapter(Adapter1.class)
  @XmlSchemaType(name = "dateTime")
  protected Calendar lastRetrieved;

  protected boolean returnCodesIfCurrent;

  /**
   * Gets the value of the id property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getId()
  {
    return id;
  }

  /**
   * Gets the value of the lastRetrieved property.
   *
   * @return possible object is
   * {@link String }
   */
  public Calendar getLastRetrieved()
  {
    return lastRetrieved;
  }

  /**
   * Gets the value of the returnCodesIfCurrent property.
   */
  public boolean isReturnCodesIfCurrent()
  {
    return returnCodesIfCurrent;
  }

  /**
   * Sets the value of the id property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setId(String value)
  {
    this.id = value;
  }

  /**
   * Sets the value of the lastRetrieved property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setLastRetrieved(Calendar value)
  {
    this.lastRetrieved = value;
  }

  /**
   * Sets the value of the returnCodesIfCurrent property.
   */
  public void setReturnCodesIfCurrent(boolean value)
  {
    this.returnCodesIfCurrent = value;
  }
}
