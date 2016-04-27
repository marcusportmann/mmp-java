
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScsiLunCapabilities complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScsiLunCapabilities">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}DynamicData">
 *       &lt;sequence>
 *         &lt;element name="updateDisplayNameSupported" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScsiLunCapabilities", propOrder = {
    "updateDisplayNameSupported"
})
public class ScsiLunCapabilities
    extends DynamicData
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    protected boolean updateDisplayNameSupported;

    /**
     * Gets the value of the updateDisplayNameSupported property.
     * 
     */
    public boolean isUpdateDisplayNameSupported() {
        return updateDisplayNameSupported;
    }

    /**
     * Sets the value of the updateDisplayNameSupported property.
     * 
     */
    public void setUpdateDisplayNameSupported(boolean value) {
        this.updateDisplayNameSupported = value;
    }

}
