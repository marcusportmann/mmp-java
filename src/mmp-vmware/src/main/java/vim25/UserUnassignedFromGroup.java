
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserUnassignedFromGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserUnassignedFromGroup">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}HostEvent">
 *       &lt;sequence>
 *         &lt;element name="userLogin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="group" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserUnassignedFromGroup", propOrder = {
    "userLogin",
    "group"
})
public class UserUnassignedFromGroup
    extends HostEvent
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected String userLogin;
    @XmlElement(required = true)
    protected String group;

    /**
     * Gets the value of the userLogin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * Sets the value of the userLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserLogin(String value) {
        this.userLogin = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
    }

}