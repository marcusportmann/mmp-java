
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VirtualMachineBootOptionsBootableEthernetDevice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VirtualMachineBootOptionsBootableEthernetDevice">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}VirtualMachineBootOptionsBootableDevice">
 *       &lt;sequence>
 *         &lt;element name="deviceKey" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualMachineBootOptionsBootableEthernetDevice", propOrder = {
    "deviceKey"
})
public class VirtualMachineBootOptionsBootableEthernetDevice
    extends VirtualMachineBootOptionsBootableDevice
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    protected int deviceKey;

    /**
     * Gets the value of the deviceKey property.
     * 
     */
    public int getDeviceKey() {
        return deviceKey;
    }

    /**
     * Sets the value of the deviceKey property.
     * 
     */
    public void setDeviceKey(int value) {
        this.deviceKey = value;
    }

}
