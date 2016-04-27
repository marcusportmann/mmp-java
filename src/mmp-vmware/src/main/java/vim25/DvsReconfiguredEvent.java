
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DvsReconfiguredEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DvsReconfiguredEvent">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}DvsEvent">
 *       &lt;sequence>
 *         &lt;element name="configSpec" type="{urn:vim25}DVSConfigSpec"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DvsReconfiguredEvent", propOrder = {
    "configSpec"
})
public class DvsReconfiguredEvent
    extends DvsEvent
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected DVSConfigSpec configSpec;

    /**
     * Gets the value of the configSpec property.
     * 
     * @return
     *     possible object is
     *     {@link DVSConfigSpec }
     *     
     */
    public DVSConfigSpec getConfigSpec() {
        return configSpec;
    }

    /**
     * Sets the value of the configSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link DVSConfigSpec }
     *     
     */
    public void setConfigSpec(DVSConfigSpec value) {
        this.configSpec = value;
    }

}
