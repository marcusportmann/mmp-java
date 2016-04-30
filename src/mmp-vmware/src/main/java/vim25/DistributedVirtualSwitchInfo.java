
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DistributedVirtualSwitchInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributedVirtualSwitchInfo">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}DynamicData">
 *       &lt;sequence>
 *         &lt;element name="switchName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="switchUuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="distributedVirtualSwitch" type="{urn:vim25}ManagedObjectReference"/>
 *         &lt;element name="networkReservationSupported" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistributedVirtualSwitchInfo", propOrder = {
    "switchName",
    "switchUuid",
    "distributedVirtualSwitch",
    "networkReservationSupported"
})
public class DistributedVirtualSwitchInfo
    extends DynamicData
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected String switchName;
    @XmlElement(required = true)
    protected String switchUuid;
    @XmlElement(required = true)
    protected ManagedObjectReference distributedVirtualSwitch;
    protected Boolean networkReservationSupported;

    /**
     * Gets the value of the switchName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSwitchName() {
        return switchName;
    }

    /**
     * Sets the value of the switchName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSwitchName(String value) {
        this.switchName = value;
    }

    /**
     * Gets the value of the switchUuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSwitchUuid() {
        return switchUuid;
    }

    /**
     * Sets the value of the switchUuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSwitchUuid(String value) {
        this.switchUuid = value;
    }

    /**
     * Gets the value of the distributedVirtualSwitch property.
     * 
     * @return
     *     possible object is
     *     {@link ManagedObjectReference }
     *     
     */
    public ManagedObjectReference getDistributedVirtualSwitch() {
        return distributedVirtualSwitch;
    }

    /**
     * Sets the value of the distributedVirtualSwitch property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagedObjectReference }
     *     
     */
    public void setDistributedVirtualSwitch(ManagedObjectReference value) {
        this.distributedVirtualSwitch = value;
    }

    /**
     * Gets the value of the networkReservationSupported property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNetworkReservationSupported() {
        return networkReservationSupported;
    }

    /**
     * Sets the value of the networkReservationSupported property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNetworkReservationSupported(Boolean value) {
        this.networkReservationSupported = value;
    }

}