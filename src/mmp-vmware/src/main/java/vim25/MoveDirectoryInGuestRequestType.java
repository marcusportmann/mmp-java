
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MoveDirectoryInGuestRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MoveDirectoryInGuestRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="_this" type="{urn:vim25}ManagedObjectReference"/>
 *         &lt;element name="vm" type="{urn:vim25}ManagedObjectReference"/>
 *         &lt;element name="auth" type="{urn:vim25}GuestAuthentication"/>
 *         &lt;element name="srcDirectoryPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dstDirectoryPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoveDirectoryInGuestRequestType", propOrder = {
    "_this",
    "vm",
    "auth",
    "srcDirectoryPath",
    "dstDirectoryPath"
})
public class MoveDirectoryInGuestRequestType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected ManagedObjectReference _this;
    @XmlElement(required = true)
    protected ManagedObjectReference vm;
    @XmlElement(required = true)
    protected GuestAuthentication auth;
    @XmlElement(required = true)
    protected String srcDirectoryPath;
    @XmlElement(required = true)
    protected String dstDirectoryPath;

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
     * Gets the value of the vm property.
     * 
     * @return
     *     possible object is
     *     {@link ManagedObjectReference }
     *     
     */
    public ManagedObjectReference getVm() {
        return vm;
    }

    /**
     * Sets the value of the vm property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagedObjectReference }
     *     
     */
    public void setVm(ManagedObjectReference value) {
        this.vm = value;
    }

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link GuestAuthentication }
     *     
     */
    public GuestAuthentication getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link GuestAuthentication }
     *     
     */
    public void setAuth(GuestAuthentication value) {
        this.auth = value;
    }

    /**
     * Gets the value of the srcDirectoryPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcDirectoryPath() {
        return srcDirectoryPath;
    }

    /**
     * Sets the value of the srcDirectoryPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcDirectoryPath(String value) {
        this.srcDirectoryPath = value;
    }

    /**
     * Gets the value of the dstDirectoryPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDstDirectoryPath() {
        return dstDirectoryPath;
    }

    /**
     * Sets the value of the dstDirectoryPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDstDirectoryPath(String value) {
        this.dstDirectoryPath = value;
    }

}
