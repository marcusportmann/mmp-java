
package org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import oasis.names.tc.saml._2_0.assertion.AssertionType;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0.TimestampType;
import org.w3._2000._09.xmldsig_.SignatureType;


/**
 * <p>Java class for SecurityHeaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecurityHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd}UsernameToken" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd}BinarySecurityToken" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Assertion" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;anyAttribute processContents='skip' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurityHeaderType", propOrder = {

})
public class SecurityHeaderType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "Timestamp", namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", required = true)
    protected TimestampType timestamp;
    @XmlElement(name = "UsernameToken")
    protected UsernameTokenType usernameToken;
    @XmlElement(name = "BinarySecurityToken")
    protected BinarySecurityTokenType binarySecurityToken;
    @XmlElement(name = "Assertion", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected AssertionType assertion;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link TimestampType }
     *     
     */
    public TimestampType getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimestampType }
     *     
     */
    public void setTimestamp(TimestampType value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the usernameToken property.
     * 
     * @return
     *     possible object is
     *     {@link UsernameTokenType }
     *     
     */
    public UsernameTokenType getUsernameToken() {
        return usernameToken;
    }

    /**
     * Sets the value of the usernameToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link UsernameTokenType }
     *     
     */
    public void setUsernameToken(UsernameTokenType value) {
        this.usernameToken = value;
    }

    /**
     * Gets the value of the binarySecurityToken property.
     * 
     * @return
     *     possible object is
     *     {@link BinarySecurityTokenType }
     *     
     */
    public BinarySecurityTokenType getBinarySecurityToken() {
        return binarySecurityToken;
    }

    /**
     * Sets the value of the binarySecurityToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link BinarySecurityTokenType }
     *     
     */
    public void setBinarySecurityToken(BinarySecurityTokenType value) {
        this.binarySecurityToken = value;
    }

    /**
     * Gets the value of the assertion property.
     * 
     * @return
     *     possible object is
     *     {@link AssertionType }
     *     
     */
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * Sets the value of the assertion property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssertionType }
     *     
     */
    public void setAssertion(AssertionType value) {
        this.assertion = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
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
