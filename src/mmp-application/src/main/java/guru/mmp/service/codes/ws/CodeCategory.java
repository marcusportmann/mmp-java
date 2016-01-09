package guru.mmp.service.codes.ws;

import org.w3._2001.xmlschema.Adapter1;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The CodeCategory type holds the information for a code category.
 * <p/>
 * <p/>
 * <p>Java class for CodeCategory complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="CodeCategory">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CodeDataType" type="{http://ws.codes.service.mmp.guru}CodeDataType"/>
 *         &lt;element name="LastUpdated" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;sequence>
 *           &lt;element name="Codes" type="{http://ws.codes.service.mmp.guru}Code"
 *           maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *         &lt;element name="CodeData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodeCategory", propOrder = {"id", "name", "description", "codeDataType",
  "lastUpdated", "codes", "codeData"})
public class CodeCategory
  implements Serializable
{

  private final static long serialVersionUID = 1000000L;

  @XmlElement(name = "CodeData", required = true, nillable = true)
  protected String codeData;

  @XmlElement(name = "CodeDataType", required = true)
  protected CodeDataType codeDataType;

  @XmlElement(name = "Codes", required = true)
  protected List<Code> codes;

  @XmlElement(name = "Description", required = true)
  protected String description;

  @XmlElement(name = "Id", required = true)
  protected String id;

  @XmlElement(name = "LastUpdated", required = true, type = String.class)
  @XmlJavaTypeAdapter(Adapter1.class)
  @XmlSchemaType(name = "dateTime")
  protected Calendar lastUpdated;

  @XmlElement(name = "Name", required = true)
  protected String name;

  /**
   * Gets the value of the codeData property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getCodeData()
  {
    return codeData;
  }

  /**
   * Gets the value of the codeDataType property.
   *
   * @return possible object is
   * {@link CodeDataType }
   */
  public CodeDataType getCodeDataType()
  {
    return codeDataType;
  }

  /**
   * Gets the value of the codes property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the codes property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getCodes().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link Code }
   */
  public List<Code> getCodes()
  {
    if (codes == null)
    {
      codes = new ArrayList<Code>();
    }
    return this.codes;
  }

  /**
   * Gets the value of the description property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getDescription()
  {
    return description;
  }

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
   * Gets the value of the lastUpdated property.
   *
   * @return possible object is
   * {@link String }
   */
  public Calendar getLastUpdated()
  {
    return lastUpdated;
  }

  /**
   * Gets the value of the name property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the value of the codeData property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setCodeData(String value)
  {
    this.codeData = value;
  }

  /**
   * Sets the value of the codeDataType property.
   *
   * @param value allowed object is
   *              {@link CodeDataType }
   */
  public void setCodeDataType(CodeDataType value)
  {
    this.codeDataType = value;
  }

  /**
   * Sets the value of the description property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setDescription(String value)
  {
    this.description = value;
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
   * Sets the value of the lastUpdated property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setLastUpdated(Calendar value)
  {
    this.lastUpdated = value;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setName(String value)
  {
    this.name = value;
  }
}
