
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfVirtualMachineRelocateSpecDiskLocator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfVirtualMachineRelocateSpecDiskLocator">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VirtualMachineRelocateSpecDiskLocator" type="{urn:vim25}VirtualMachineRelocateSpecDiskLocator" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfVirtualMachineRelocateSpecDiskLocator", propOrder = {
    "virtualMachineRelocateSpecDiskLocator"
})
public class ArrayOfVirtualMachineRelocateSpecDiskLocator
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "VirtualMachineRelocateSpecDiskLocator")
    protected List<VirtualMachineRelocateSpecDiskLocator> virtualMachineRelocateSpecDiskLocator;

    /**
     * Gets the value of the virtualMachineRelocateSpecDiskLocator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the virtualMachineRelocateSpecDiskLocator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVirtualMachineRelocateSpecDiskLocator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VirtualMachineRelocateSpecDiskLocator }
     * 
     * 
     */
    public List<VirtualMachineRelocateSpecDiskLocator> getVirtualMachineRelocateSpecDiskLocator() {
        if (virtualMachineRelocateSpecDiskLocator == null) {
            virtualMachineRelocateSpecDiskLocator = new ArrayList<VirtualMachineRelocateSpecDiskLocator>();
        }
        return this.virtualMachineRelocateSpecDiskLocator;
    }

}
