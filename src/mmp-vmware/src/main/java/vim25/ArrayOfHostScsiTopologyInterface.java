
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfHostScsiTopologyInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfHostScsiTopologyInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HostScsiTopologyInterface" type="{urn:vim25}HostScsiTopologyInterface" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfHostScsiTopologyInterface", propOrder = {
    "hostScsiTopologyInterface"
})
public class ArrayOfHostScsiTopologyInterface
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "HostScsiTopologyInterface")
    protected List<HostScsiTopologyInterface> hostScsiTopologyInterface;

    /**
     * Gets the value of the hostScsiTopologyInterface property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hostScsiTopologyInterface property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHostScsiTopologyInterface().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HostScsiTopologyInterface }
     * 
     * 
     */
    public List<HostScsiTopologyInterface> getHostScsiTopologyInterface() {
        if (hostScsiTopologyInterface == null) {
            hostScsiTopologyInterface = new ArrayList<HostScsiTopologyInterface>();
        }
        return this.hostScsiTopologyInterface;
    }

}