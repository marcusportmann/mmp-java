
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EnterMaintenanceModeRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnterMaintenanceModeRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="_this" type="{urn:vim25}ManagedObjectReference"/>
 *         &lt;element name="timeout" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="evacuatePoweredOffVms" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="maintenanceSpec" type="{urn:vim25}HostMaintenanceSpec" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnterMaintenanceModeRequestType", propOrder = {
    "_this",
    "timeout",
    "evacuatePoweredOffVms",
    "maintenanceSpec"
})
public class EnterMaintenanceModeRequestType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected ManagedObjectReference _this;
    protected int timeout;
    protected Boolean evacuatePoweredOffVms;
    protected HostMaintenanceSpec maintenanceSpec;

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
     * Gets the value of the timeout property.
     * 
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     */
    public void setTimeout(int value) {
        this.timeout = value;
    }

    /**
     * Gets the value of the evacuatePoweredOffVms property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEvacuatePoweredOffVms() {
        return evacuatePoweredOffVms;
    }

    /**
     * Sets the value of the evacuatePoweredOffVms property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEvacuatePoweredOffVms(Boolean value) {
        this.evacuatePoweredOffVms = value;
    }

    /**
     * Gets the value of the maintenanceSpec property.
     * 
     * @return
     *     possible object is
     *     {@link HostMaintenanceSpec }
     *     
     */
    public HostMaintenanceSpec getMaintenanceSpec() {
        return maintenanceSpec;
    }

    /**
     * Sets the value of the maintenanceSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link HostMaintenanceSpec }
     *     
     */
    public void setMaintenanceSpec(HostMaintenanceSpec value) {
        this.maintenanceSpec = value;
    }

}
