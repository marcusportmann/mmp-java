
package guru.mmp.service.sample;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TestLocalDateTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TestLocalDateTime"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LocalDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TestLocalDateTime", propOrder = {
    "localDateTime"
})
public class TestLocalDateTime implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "LocalDateTime", required = true)
    protected String localDateTime;

    /**
     * Gets the value of the localDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Sets the value of the localDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalDateTime(String value) {
        this.localDateTime = value;
    }

}
