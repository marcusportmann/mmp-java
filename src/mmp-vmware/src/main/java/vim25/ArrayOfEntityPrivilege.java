
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfEntityPrivilege complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfEntityPrivilege">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EntityPrivilege" type="{urn:vim25}EntityPrivilege" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfEntityPrivilege", propOrder = {
    "entityPrivilege"
})
public class ArrayOfEntityPrivilege
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "EntityPrivilege")
    protected List<EntityPrivilege> entityPrivilege;

    /**
     * Gets the value of the entityPrivilege property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entityPrivilege property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntityPrivilege().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntityPrivilege }
     * 
     * 
     */
    public List<EntityPrivilege> getEntityPrivilege() {
        if (entityPrivilege == null) {
            entityPrivilege = new ArrayList<EntityPrivilege>();
        }
        return this.entityPrivilege;
    }

}