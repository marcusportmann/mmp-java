
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VsanDiskFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VsanDiskFault">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}VsanFault">
 *       &lt;sequence>
 *         &lt;element name="device" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VsanDiskFault", propOrder = {
    "device"
})
@XmlSeeAlso({
    DuplicateDisks.class,
    DiskTooSmall.class,
    DiskIsNonLocal.class,
    DiskIsUSB.class,
    VsanIncompatibleDiskMapping.class,
    DiskIsLastRemainingNonSSD.class,
    DiskHasPartitions.class,
    InsufficientDisks.class
})
public class VsanDiskFault
    extends VsanFault
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    protected String device;

    /**
     * Gets the value of the device property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDevice() {
        return device;
    }

    /**
     * Sets the value of the device property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDevice(String value) {
        this.device = value;
    }

}
