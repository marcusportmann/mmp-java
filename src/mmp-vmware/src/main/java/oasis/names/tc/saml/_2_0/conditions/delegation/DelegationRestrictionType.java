
package oasis.names.tc.saml._2_0.conditions.delegation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oasis.names.tc.saml._2_0.assertion.ConditionAbstractType;


/**
 * <p>Java class for DelegationRestrictionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DelegationRestrictionType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:assertion}ConditionAbstractType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:conditions:delegation}Delegate" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelegationRestrictionType", propOrder = {
    "delegate"
})
public class DelegationRestrictionType
    extends ConditionAbstractType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "Delegate", required = true)
    protected List<DelegateType> delegate;

    /**
     * Gets the value of the delegate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the delegate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDelegate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DelegateType }
     * 
     * 
     */
    public List<DelegateType> getDelegate() {
        if (delegate == null) {
            delegate = new ArrayList<DelegateType>();
        }
        return this.delegate;
    }

}
