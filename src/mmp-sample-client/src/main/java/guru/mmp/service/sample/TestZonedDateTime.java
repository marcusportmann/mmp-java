
package guru.mmp.service.sample;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TestZonedDateTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TestZonedDateTime"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ZonedDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TestZonedDateTime", propOrder = {
    "zonedDateTime"
})
public class TestZonedDateTime implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "ZonedDateTime", required = true)
    protected String zonedDateTime;

    /**
     * Gets the value of the zonedDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZonedDateTime() {
        return zonedDateTime;
    }

    /**
     * Sets the value of the zonedDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZonedDateTime(String value) {
        this.zonedDateTime = value;
    }

}
