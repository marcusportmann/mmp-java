
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfVirtualSCSISharing complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfVirtualSCSISharing">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VirtualSCSISharing" type="{urn:vim25}VirtualSCSISharing" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfVirtualSCSISharing", propOrder = {
    "virtualSCSISharing"
})
public class ArrayOfVirtualSCSISharing
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "VirtualSCSISharing")
    protected List<VirtualSCSISharing> virtualSCSISharing;

    /**
     * Gets the value of the virtualSCSISharing property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the virtualSCSISharing property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVirtualSCSISharing().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VirtualSCSISharing }
     * 
     * 
     */
    public List<VirtualSCSISharing> getVirtualSCSISharing() {
        if (virtualSCSISharing == null) {
            virtualSCSISharing = new ArrayList<VirtualSCSISharing>();
        }
        return this.virtualSCSISharing;
    }

}
