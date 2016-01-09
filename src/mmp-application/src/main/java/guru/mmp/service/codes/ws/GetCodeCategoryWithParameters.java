package guru.mmp.service.codes.ws;

import org.w3._2001.xmlschema.Adapter1;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
 *         &lt;sequence>
 *           &lt;element name="parameters" type="{http://ws.codes.service.mmp.guru}Parameter"
 *           maxOccurs="unbounded"/>
 *         &lt;/sequence>
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
@XmlType(name = "", propOrder = {"id", "parameters", "lastRetrieved", "returnCodesIfCurrent"})
@XmlRootElement(name = "GetCodeCategoryWithParameters")
public class GetCodeCategoryWithParameters
  implements Serializable
{

  private final static long serialVersionUID = 1000000L;

  @XmlElement(required = true)
  protected String id;

  @XmlElement(required = true, type = String.class)
  @XmlJavaTypeAdapter(Adapter1.class)
  @XmlSchemaType(name = "dateTime")
  protected Calendar lastRetrieved;

  @XmlElement(required = true)
  protected List<Parameter> parameters;

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
   * Gets the value of the parameters property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the parameters property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getParameters().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link Parameter }
   */
  public List<Parameter> getParameters()
  {
    if (parameters == null)
    {
      parameters = new ArrayList<Parameter>();
    }
    return this.parameters;
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
