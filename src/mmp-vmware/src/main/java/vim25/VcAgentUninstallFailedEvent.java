
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VcAgentUninstallFailedEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VcAgentUninstallFailedEvent">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}HostEvent">
 *       &lt;sequence>
 *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VcAgentUninstallFailedEvent", propOrder = {
    "reason"
})
public class VcAgentUninstallFailedEvent
    extends HostEvent
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    protected String reason;

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

}