
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfGuestRegKeyRecordSpec complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfGuestRegKeyRecordSpec">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GuestRegKeyRecordSpec" type="{urn:vim25}GuestRegKeyRecordSpec" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfGuestRegKeyRecordSpec", propOrder = {
    "guestRegKeyRecordSpec"
})
public class ArrayOfGuestRegKeyRecordSpec
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "GuestRegKeyRecordSpec")
    protected List<GuestRegKeyRecordSpec> guestRegKeyRecordSpec;

    /**
     * Gets the value of the guestRegKeyRecordSpec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guestRegKeyRecordSpec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuestRegKeyRecordSpec().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GuestRegKeyRecordSpec }
     * 
     * 
     */
    public List<GuestRegKeyRecordSpec> getGuestRegKeyRecordSpec() {
        if (guestRegKeyRecordSpec == null) {
            guestRegKeyRecordSpec = new ArrayList<GuestRegKeyRecordSpec>();
        }
        return this.guestRegKeyRecordSpec;
    }

}
