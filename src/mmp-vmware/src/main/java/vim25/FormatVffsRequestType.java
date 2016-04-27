
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FormatVffsRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FormatVffsRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="_this" type="{urn:vim25}ManagedObjectReference"/>
 *         &lt;element name="createSpec" type="{urn:vim25}HostVffsSpec"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormatVffsRequestType", propOrder = {
    "_this",
    "createSpec"
})
public class FormatVffsRequestType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected ManagedObjectReference _this;
    @XmlElement(required = true)
    protected HostVffsSpec createSpec;

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
     * Gets the value of the createSpec property.
     * 
     * @return
     *     possible object is
     *     {@link HostVffsSpec }
     *     
     */
    public HostVffsSpec getCreateSpec() {
        return createSpec;
    }

    /**
     * Sets the value of the createSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link HostVffsSpec }
     *     
     */
    public void setCreateSpec(HostVffsSpec value) {
        this.createSpec = value;
    }

}
