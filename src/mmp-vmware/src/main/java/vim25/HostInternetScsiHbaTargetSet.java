
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HostInternetScsiHbaTargetSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HostInternetScsiHbaTargetSet">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}DynamicData">
 *       &lt;sequence>
 *         &lt;element name="staticTargets" type="{urn:vim25}HostInternetScsiHbaStaticTarget" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sendTargets" type="{urn:vim25}HostInternetScsiHbaSendTarget" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HostInternetScsiHbaTargetSet", propOrder = {
    "staticTargets",
    "sendTargets"
})
public class HostInternetScsiHbaTargetSet
    extends DynamicData
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    protected List<HostInternetScsiHbaStaticTarget> staticTargets;
    protected List<HostInternetScsiHbaSendTarget> sendTargets;

    /**
     * Gets the value of the staticTargets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the staticTargets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStaticTargets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HostInternetScsiHbaStaticTarget }
     * 
     * 
     */
    public List<HostInternetScsiHbaStaticTarget> getStaticTargets() {
        if (staticTargets == null) {
            staticTargets = new ArrayList<HostInternetScsiHbaStaticTarget>();
        }
        return this.staticTargets;
    }

    /**
     * Gets the value of the sendTargets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sendTargets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSendTargets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HostInternetScsiHbaSendTarget }
     * 
     * 
     */
    public List<HostInternetScsiHbaSendTarget> getSendTargets() {
        if (sendTargets == null) {
            sendTargets = new ArrayList<HostInternetScsiHbaSendTarget>();
        }
        return this.sendTargets;
    }

}
