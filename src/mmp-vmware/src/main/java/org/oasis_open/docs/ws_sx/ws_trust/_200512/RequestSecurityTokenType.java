
package org.oasis_open.docs.ws_sx.ws_trust._200512;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import com.rsa.names._2009._12.std_ext.ws_trust1_4.advice.AdviceSetType;


/**
 * 
 *         This is a modified version of the WS-Trust RST definition setup to only allow the elements permitted by Castle in an issue / renew / validate requests.
 *       
 * 
 * <p>Java class for RequestSecurityTokenType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestSecurityTokenType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}TokenType" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}RequestType"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}Lifetime" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}Renewing" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}BinaryExchange" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}KeyType" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}SignatureAlgorithm" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}UseKey" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}DelegateTo" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}Delegatable" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}Participants" minOccurs="0"/>
 *         &lt;element ref="{http://www.rsa.com/names/2009/12/std-ext/WS-Trust1.4/advice}AdviceSet" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}ValidateTarget" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/ws-sx/ws-trust/200512}RenewTarget" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="Context" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;anyAttribute processContents='skip'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestSecurityTokenType", propOrder = {

})
public class RequestSecurityTokenType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "TokenType")
    protected String tokenType;
    @XmlElement(name = "RequestType", required = true)
    protected String requestType;
    @XmlElement(name = "Lifetime")
    protected LifetimeType lifetime;
    @XmlElement(name = "Renewing")
    protected RenewingType renewing;
    @XmlElement(name = "BinaryExchange")
    protected BinaryExchangeType binaryExchange;
    @XmlElement(name = "KeyType")
    protected String keyType;
    @XmlElement(name = "SignatureAlgorithm")
    protected String signatureAlgorithm;
    @XmlElement(name = "UseKey")
    protected UseKeyType useKey;
    @XmlElement(name = "DelegateTo")
    protected DelegateToType delegateTo;
    @XmlElement(name = "Delegatable")
    protected Boolean delegatable;
    @XmlElement(name = "Participants")
    protected ParticipantsType participants;
    @XmlElement(name = "AdviceSet", namespace = "http://www.rsa.com/names/2009/12/std-ext/WS-Trust1.4/advice")
    protected AdviceSetType adviceSet;
    @XmlElement(name = "ValidateTarget")
    protected ValidateTargetType validateTarget;
    @XmlElement(name = "RenewTarget")
    protected RenewTargetType renewTarget;
    @XmlAttribute(name = "Context")
    @XmlSchemaType(name = "anyURI")
    protected String context;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the tokenType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Sets the value of the tokenType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenType(String value) {
        this.tokenType = value;
    }

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

    /**
     * Gets the value of the lifetime property.
     * 
     * @return
     *     possible object is
     *     {@link LifetimeType }
     *     
     */
    public LifetimeType getLifetime() {
        return lifetime;
    }

    /**
     * Sets the value of the lifetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifetimeType }
     *     
     */
    public void setLifetime(LifetimeType value) {
        this.lifetime = value;
    }

    /**
     * Gets the value of the renewing property.
     * 
     * @return
     *     possible object is
     *     {@link RenewingType }
     *     
     */
    public RenewingType getRenewing() {
        return renewing;
    }

    /**
     * Sets the value of the renewing property.
     * 
     * @param value
     *     allowed object is
     *     {@link RenewingType }
     *     
     */
    public void setRenewing(RenewingType value) {
        this.renewing = value;
    }

    /**
     * Gets the value of the binaryExchange property.
     * 
     * @return
     *     possible object is
     *     {@link BinaryExchangeType }
     *     
     */
    public BinaryExchangeType getBinaryExchange() {
        return binaryExchange;
    }

    /**
     * Sets the value of the binaryExchange property.
     * 
     * @param value
     *     allowed object is
     *     {@link BinaryExchangeType }
     *     
     */
    public void setBinaryExchange(BinaryExchangeType value) {
        this.binaryExchange = value;
    }

    /**
     * Gets the value of the keyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyType() {
        return keyType;
    }

    /**
     * Sets the value of the keyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyType(String value) {
        this.keyType = value;
    }

    /**
     * Gets the value of the signatureAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Sets the value of the signatureAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureAlgorithm(String value) {
        this.signatureAlgorithm = value;
    }

    /**
     * Gets the value of the useKey property.
     * 
     * @return
     *     possible object is
     *     {@link UseKeyType }
     *     
     */
    public UseKeyType getUseKey() {
        return useKey;
    }

    /**
     * Sets the value of the useKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link UseKeyType }
     *     
     */
    public void setUseKey(UseKeyType value) {
        this.useKey = value;
    }

    /**
     * Gets the value of the delegateTo property.
     * 
     * @return
     *     possible object is
     *     {@link DelegateToType }
     *     
     */
    public DelegateToType getDelegateTo() {
        return delegateTo;
    }

    /**
     * Sets the value of the delegateTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DelegateToType }
     *     
     */
    public void setDelegateTo(DelegateToType value) {
        this.delegateTo = value;
    }

    /**
     * Gets the value of the delegatable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDelegatable() {
        return delegatable;
    }

    /**
     * Sets the value of the delegatable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDelegatable(Boolean value) {
        this.delegatable = value;
    }

    /**
     * Gets the value of the participants property.
     * 
     * @return
     *     possible object is
     *     {@link ParticipantsType }
     *     
     */
    public ParticipantsType getParticipants() {
        return participants;
    }

    /**
     * Sets the value of the participants property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipantsType }
     *     
     */
    public void setParticipants(ParticipantsType value) {
        this.participants = value;
    }

    /**
     * Gets the value of the adviceSet property.
     * 
     * @return
     *     possible object is
     *     {@link AdviceSetType }
     *     
     */
    public AdviceSetType getAdviceSet() {
        return adviceSet;
    }

    /**
     * Sets the value of the adviceSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdviceSetType }
     *     
     */
    public void setAdviceSet(AdviceSetType value) {
        this.adviceSet = value;
    }

    /**
     * Gets the value of the validateTarget property.
     * 
     * @return
     *     possible object is
     *     {@link ValidateTargetType }
     *     
     */
    public ValidateTargetType getValidateTarget() {
        return validateTarget;
    }

    /**
     * Sets the value of the validateTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValidateTargetType }
     *     
     */
    public void setValidateTarget(ValidateTargetType value) {
        this.validateTarget = value;
    }

    /**
     * Gets the value of the renewTarget property.
     * 
     * @return
     *     possible object is
     *     {@link RenewTargetType }
     *     
     */
    public RenewTargetType getRenewTarget() {
        return renewTarget;
    }

    /**
     * Sets the value of the renewTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link RenewTargetType }
     *     
     */
    public void setRenewTarget(RenewTargetType value) {
        this.renewTarget = value;
    }

    /**
     * Gets the value of the context property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets the value of the context property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContext(String value) {
        this.context = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
