
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QueryMemoryOverheadExRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QueryMemoryOverheadExRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="_this" type="{urn:vim25}ManagedObjectReference"/>
 *         &lt;element name="vmConfigInfo" type="{urn:vim25}VirtualMachineConfigInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryMemoryOverheadExRequestType", propOrder = {
    "_this",
    "vmConfigInfo"
})
public class QueryMemoryOverheadExRequestType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected ManagedObjectReference _this;
    @XmlElement(required = true)
    protected VirtualMachineConfigInfo vmConfigInfo;

    /**
     * Gets the value of the this property.
     * 
     * @return
     *     possible object is
     *     {@link ManagedObjectReference }
     *     
     */
    public ManagedObjectReference getThis() {
        return _this;
    }

    /**
     * Sets the value of the this property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagedObjectReference }
     *     
     */
    public void setThis(ManagedObjectReference value) {
        this._this = value;
    }

    /**
     * Gets the value of the vmConfigInfo property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualMachineConfigInfo }
     *     
     */
    public VirtualMachineConfigInfo getVmConfigInfo() {
        return vmConfigInfo;
    }

    /**
     * Sets the value of the vmConfigInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualMachineConfigInfo }
     *     
     */
    public void setVmConfigInfo(VirtualMachineConfigInfo value) {
        this.vmConfigInfo = value;
    }

}
