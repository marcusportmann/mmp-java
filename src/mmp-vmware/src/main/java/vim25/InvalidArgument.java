
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvalidArgument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvalidArgument">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}RuntimeFault">
 *       &lt;sequence>
 *         &lt;element name="invalidProperty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvalidArgument", propOrder = {
    "invalidProperty"
})
@XmlSeeAlso({
    InvalidDasRestartPriorityForFtVm.class,
    InvalidIndexArgument.class,
    InvalidDasConfigArgument.class,
    IncompatibleSetting.class,
    InvalidDrsBehaviorForFtVm.class
})
public class InvalidArgument
    extends RuntimeFault
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    protected String invalidProperty;

    /**
     * Gets the value of the invalidProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvalidProperty() {
        return invalidProperty;
    }

    /**
     * Sets the value of the invalidProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvalidProperty(String value) {
        this.invalidProperty = value;
    }

}
