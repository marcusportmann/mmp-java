
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfHostDhcpService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfHostDhcpService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HostDhcpService" type="{urn:vim25}HostDhcpService" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfHostDhcpService", propOrder = {
    "hostDhcpService"
})
public class ArrayOfHostDhcpService
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "HostDhcpService")
    protected List<HostDhcpService> hostDhcpService;

    /**
     * Gets the value of the hostDhcpService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hostDhcpService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHostDhcpService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HostDhcpService }
     * 
     * 
     */
    public List<HostDhcpService> getHostDhcpService() {
        if (hostDhcpService == null) {
            hostDhcpService = new ArrayList<HostDhcpService>();
        }
        return this.hostDhcpService;
    }

}
