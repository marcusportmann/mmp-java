
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2000._09.xmldsig_.SignatureType;
import org.w3._2001.xmlschema.Adapter1;


/**
 * 
 *         Section 3.2: The Version Attribute MUST be present and MUST
 *         contain the value "2.0", the ID attribute MUST be preent and
 *         MUST contain a UUID with a "_" prefix and the IssueInstant
 *         MUST be in UTC time.
 * 
 *         An assertion MUST contain an AuthnStatement but
 *         cannot contain an AuthZDecisionStatement or a Statement.
 * 
 *         TODO: The current schema is not quite correct since it
 *         requires the AuthnStatement to be before the
 *         AttributeStatement.
 *       
 * 
 * <p>Java class for AssertionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssertionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Issuer"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Subject"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Conditions"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Advice" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnStatement"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AttributeStatement" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="2.0" />
 *       &lt;attribute name="ID" use="required" type="{http://www.rsa.com/names/2010/04/std-prof/SAML2.0}UUID" />
 *       &lt;attribute name="IssueInstant" use="required" type="{http://www.rsa.com/names/2010/04/std-prof/SAML2.0}UTCTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssertionType", propOrder = {
    "issuer",
    "signature",
    "subject",
    "conditions",
    "advice",
    "authnStatement",
    "attributeStatement"
})
public class AssertionType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "Issuer", required = true)
    protected NameIDType issuer;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;
    @XmlElement(name = "Subject", required = true)
    protected SubjectType subject;
    @XmlElement(name = "Conditions", required = true)
    protected ConditionsType conditions;
    @XmlElement(name = "Advice")
    protected AdviceType advice;
    @XmlElement(name = "AuthnStatement", required = true)
    protected AuthnStatementType authnStatement;
    @XmlElement(name = "AttributeStatement")
    protected AttributeStatementType attributeStatement;
    @XmlAttribute(name = "Version", required = true)
    protected String version;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "IssueInstant", required = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Calendar issueInstant;

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link NameIDType }
     *     
     */
    public NameIDType getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIDType }
     *     
     */
    public void setIssuer(NameIDType value) {
        this.issuer = value;
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
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectType }
     *     
     */
    public SubjectType getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectType }
     *     
     */
    public void setSubject(SubjectType value) {
        this.subject = value;
    }

    /**
     * Gets the value of the conditions property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionsType }
     *     
     */
    public ConditionsType getConditions() {
        return conditions;
    }

    /**
     * Sets the value of the conditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionsType }
     *     
     */
    public void setConditions(ConditionsType value) {
        this.conditions = value;
    }

    /**
     * Gets the value of the advice property.
     * 
     * @return
     *     possible object is
     *     {@link AdviceType }
     *     
     */
    public AdviceType getAdvice() {
        return advice;
    }

    /**
     * Sets the value of the advice property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdviceType }
     *     
     */
    public void setAdvice(AdviceType value) {
        this.advice = value;
    }

    /**
     * Gets the value of the authnStatement property.
     * 
     * @return
     *     possible object is
     *     {@link AuthnStatementType }
     *     
     */
    public AuthnStatementType getAuthnStatement() {
        return authnStatement;
    }

    /**
     * Sets the value of the authnStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthnStatementType }
     *     
     */
    public void setAuthnStatement(AuthnStatementType value) {
        this.authnStatement = value;
    }

    /**
     * Gets the value of the attributeStatement property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeStatementType }
     *     
     */
    public AttributeStatementType getAttributeStatement() {
        return attributeStatement;
    }

    /**
     * Sets the value of the attributeStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeStatementType }
     *     
     */
    public void setAttributeStatement(AttributeStatementType value) {
        this.attributeStatement = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "2.0";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the issueInstant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getIssueInstant() {
        return issueInstant;
    }

    /**
     * Sets the value of the issueInstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueInstant(Calendar value) {
        this.issueInstant = value;
    }

}
