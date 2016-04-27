
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfVirtualMachineSriovInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfVirtualMachineSriovInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VirtualMachineSriovInfo" type="{urn:vim25}VirtualMachineSriovInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfVirtualMachineSriovInfo", propOrder = {
    "virtualMachineSriovInfo"
})
public class ArrayOfVirtualMachineSriovInfo
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "VirtualMachineSriovInfo")
    protected List<VirtualMachineSriovInfo> virtualMachineSriovInfo;

    /**
     * Gets the value of the virtualMachineSriovInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the virtualMachineSriovInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVirtualMachineSriovInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VirtualMachineSriovInfo }
     * 
     * 
     */
    public List<VirtualMachineSriovInfo> getVirtualMachineSriovInfo() {
        if (virtualMachineSriovInfo == null) {
            virtualMachineSriovInfo = new ArrayList<VirtualMachineSriovInfo>();
        }
        return this.virtualMachineSriovInfo;
    }

}
