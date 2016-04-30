
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IscsiFaultVnicAlreadyBound complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IscsiFaultVnicAlreadyBound">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}IscsiFault">
 *       &lt;sequence>
 *         &lt;element name="vnicDevice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IscsiFaultVnicAlreadyBound", propOrder = {
    "vnicDevice"
})
public class IscsiFaultVnicAlreadyBound
    extends IscsiFault
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected String vnicDevice;

    /**
     * Gets the value of the vnicDevice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVnicDevice() {
        return vnicDevice;
    }

    /**
     * Sets the value of the vnicDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVnicDevice(String value) {
        this.vnicDevice = value;
    }

}