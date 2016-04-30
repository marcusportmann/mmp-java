
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfHostSslThumbprintInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfHostSslThumbprintInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HostSslThumbprintInfo" type="{urn:vim25}HostSslThumbprintInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfHostSslThumbprintInfo", propOrder = {
    "hostSslThumbprintInfo"
})
public class ArrayOfHostSslThumbprintInfo
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "HostSslThumbprintInfo")
    protected List<HostSslThumbprintInfo> hostSslThumbprintInfo;

    /**
     * Gets the value of the hostSslThumbprintInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hostSslThumbprintInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHostSslThumbprintInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HostSslThumbprintInfo }
     * 
     * 
     */
    public List<HostSslThumbprintInfo> getHostSslThumbprintInfo() {
        if (hostSslThumbprintInfo == null) {
            hostSslThumbprintInfo = new ArrayList<HostSslThumbprintInfo>();
        }
        return this.hostSslThumbprintInfo;
    }

}