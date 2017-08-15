
package guru.mmp.service.codes.ws;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LastRetrieved" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="ReturnCodesIfCurrent" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "lastRetrieved",
    "returnCodesIfCurrent"
})
@XmlRootElement(name = "GetCodeCategory")
public class GetCodeCategory implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "LastRetrieved", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected LocalDateTime lastRetrieved;
    @XmlElement(name = "ReturnCodesIfCurrent")
    protected boolean returnCodesIfCurrent;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the lastRetrieved property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public LocalDateTime getLastRetrieved() {
        return lastRetrieved;
    }

    /**
     * Sets the value of the lastRetrieved property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastRetrieved(LocalDateTime value) {
        this.lastRetrieved = value;
    }

    /**
     * Gets the value of the returnCodesIfCurrent property.
     * 
     */
    public boolean isReturnCodesIfCurrent() {
        return returnCodesIfCurrent;
    }

    /**
     * Sets the value of the returnCodesIfCurrent property.
     * 
     */
    public void setReturnCodesIfCurrent(boolean value) {
        this.returnCodesIfCurrent = value;
    }

}
