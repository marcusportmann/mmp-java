
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;


/**
 * 
 *         Section 3.5: The NotBefore and NotOnOrAfter attributes MUST be
 *         present and MUST be in UTC time.
 *         
 *         An instance of the Condition element SHALL be present if the
 *         assertion has been delegated.  and an instance SHALL be
 *         included if the assertion is renewable.  (Allowing zero,one or
 *         two instances.)
 * 
 *         A single instance of the AudienceRestriction element SHALL be
 *         included in the Conditions element if and only if the
 *         assertion has been issued aimed at specific Responders.
 *         
 *         A single instance of the ProxyRestriction element MAY be
 *         included in the saml:Conditions element if and only if the
 *         assertion is delegable.  If this element is not included, then
 *         no proxy conditions are stated and SHOULD NOT be assumed by
 *         the client.
 * 
 *         NOTE: The schema does not enforce when the various conditions
 *         are included, only their type.
 * 
 *         TODO: The current schema does not include the condition that
 *         there can be at most two Condition elements, one
 *         AudienceRestriction and one ProxyRestriction.
 * 
 *       
 * 
 * <p>Java class for ConditionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConditionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Condition"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AudienceRestriction"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}ProxyRestriction"/>
 *       &lt;/choice>
 *       &lt;attribute name="NotBefore" use="required" type="{http://www.rsa.com/names/2010/04/std-prof/SAML2.0}UTCTime" />
 *       &lt;attribute name="NotOnOrAfter" use="required" type="{http://www.rsa.com/names/2010/04/std-prof/SAML2.0}UTCTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConditionsType", propOrder = {
    "conditionOrAudienceRestrictionOrProxyRestriction"
})
public class ConditionsType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElements({
        @XmlElement(name = "Condition"),
        @XmlElement(name = "AudienceRestriction", type = AudienceRestrictionType.class),
        @XmlElement(name = "ProxyRestriction", type = ProxyRestrictionType.class)
    })
    protected List<ConditionAbstractType> conditionOrAudienceRestrictionOrProxyRestriction;
    @XmlAttribute(name = "NotBefore", required = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Calendar notBefore;
    @XmlAttribute(name = "NotOnOrAfter", required = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Calendar notOnOrAfter;

    /**
     * Gets the value of the conditionOrAudienceRestrictionOrProxyRestriction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conditionOrAudienceRestrictionOrProxyRestriction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConditionOrAudienceRestrictionOrProxyRestriction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConditionAbstractType }
     * {@link AudienceRestrictionType }
     * {@link ProxyRestrictionType }
     * 
     * 
     */
    public List<ConditionAbstractType> getConditionOrAudienceRestrictionOrProxyRestriction() {
        if (conditionOrAudienceRestrictionOrProxyRestriction == null) {
            conditionOrAudienceRestrictionOrProxyRestriction = new ArrayList<ConditionAbstractType>();
        }
        return this.conditionOrAudienceRestrictionOrProxyRestriction;
    }

    /**
     * Gets the value of the notBefore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getNotBefore() {
        return notBefore;
    }

    /**
     * Sets the value of the notBefore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotBefore(Calendar value) {
        this.notBefore = value;
    }

    /**
     * Gets the value of the notOnOrAfter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getNotOnOrAfter() {
        return notOnOrAfter;
    }

    /**
     * Sets the value of the notOnOrAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotOnOrAfter(Calendar value) {
        this.notOnOrAfter = value;
    }

}
