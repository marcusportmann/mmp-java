
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvalidNetworkResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvalidNetworkResource">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}NasConfigFault">
 *       &lt;sequence>
 *         &lt;element name="remoteHost" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="remotePath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvalidNetworkResource", propOrder = {
    "remoteHost",
    "remotePath"
})
public class InvalidNetworkResource
    extends NasConfigFault
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected String remoteHost;
    @XmlElement(required = true)
    protected String remotePath;

    /**
     * Gets the value of the remoteHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Sets the value of the remoteHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteHost(String value) {
        this.remoteHost = value;
    }

    /**
     * Gets the value of the remotePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * Sets the value of the remotePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemotePath(String value) {
        this.remotePath = value;
    }

}
