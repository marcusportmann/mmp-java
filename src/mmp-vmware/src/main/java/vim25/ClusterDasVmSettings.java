
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClusterDasVmSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClusterDasVmSettings">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}DynamicData">
 *       &lt;sequence>
 *         &lt;element name="restartPriority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isolationResponse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vmToolsMonitoringSettings" type="{urn:vim25}ClusterVmToolsMonitoringSettings" minOccurs="0"/>
 *         &lt;element name="vmComponentProtectionSettings" type="{urn:vim25}ClusterVmComponentProtectionSettings" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClusterDasVmSettings", propOrder = {
    "restartPriority",
    "isolationResponse",
    "vmToolsMonitoringSettings",
    "vmComponentProtectionSettings"
})
public class ClusterDasVmSettings
    extends DynamicData
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    protected String restartPriority;
    protected String isolationResponse;
    protected ClusterVmToolsMonitoringSettings vmToolsMonitoringSettings;
    protected ClusterVmComponentProtectionSettings vmComponentProtectionSettings;

    /**
     * Gets the value of the restartPriority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestartPriority() {
        return restartPriority;
    }

    /**
     * Sets the value of the restartPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestartPriority(String value) {
        this.restartPriority = value;
    }

    /**
     * Gets the value of the isolationResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsolationResponse() {
        return isolationResponse;
    }

    /**
     * Sets the value of the isolationResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsolationResponse(String value) {
        this.isolationResponse = value;
    }

    /**
     * Gets the value of the vmToolsMonitoringSettings property.
     * 
     * @return
     *     possible object is
     *     {@link ClusterVmToolsMonitoringSettings }
     *     
     */
    public ClusterVmToolsMonitoringSettings getVmToolsMonitoringSettings() {
        return vmToolsMonitoringSettings;
    }

    /**
     * Sets the value of the vmToolsMonitoringSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClusterVmToolsMonitoringSettings }
     *     
     */
    public void setVmToolsMonitoringSettings(ClusterVmToolsMonitoringSettings value) {
        this.vmToolsMonitoringSettings = value;
    }

    /**
     * Gets the value of the vmComponentProtectionSettings property.
     * 
     * @return
     *     possible object is
     *     {@link ClusterVmComponentProtectionSettings }
     *     
     */
    public ClusterVmComponentProtectionSettings getVmComponentProtectionSettings() {
        return vmComponentProtectionSettings;
    }

    /**
     * Sets the value of the vmComponentProtectionSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClusterVmComponentProtectionSettings }
     *     
     */
    public void setVmComponentProtectionSettings(ClusterVmComponentProtectionSettings value) {
        this.vmComponentProtectionSettings = value;
    }

}
