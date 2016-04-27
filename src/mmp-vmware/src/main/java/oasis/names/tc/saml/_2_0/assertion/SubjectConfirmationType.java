
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         Section 3.4.1: The SubjectConfirmationType is restricted to
 *         only contain a NameID and not a BaseID or EncryptedID.  It
 *         must also contain the SubjectConfirmationData element
 * 
 *         Note: The NameID element is present if the token is delegated
 *         in which case it contains the identity of the delegate.
 *       
 * 
 * <p>Java class for SubjectConfirmationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectConfirmationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectConfirmationData"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Method" use="required" type="{http://www.rsa.com/names/2010/04/std-prof/SAML2.0}SubjectConfirmationMethods" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectConfirmationType", propOrder = {
    "nameID",
    "subjectConfirmationData"
})
public class SubjectConfirmationType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "NameID")
    protected NameIDType nameID;
    @XmlElement(name = "SubjectConfirmationData", required = true)
    protected SubjectConfirmationDataType subjectConfirmationData;
    @XmlAttribute(name = "Method", required = true)
    protected String method;

    /**
     * Gets the value of the nameID property.
     * 
     * @return
     *     possible object is
     *     {@link NameIDType }
     *     
     */
    public NameIDType getNameID() {
        return nameID;
    }

    /**
     * Sets the value of the nameID property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIDType }
     *     
     */
    public void setNameID(NameIDType value) {
        this.nameID = value;
    }

    /**
     * Gets the value of the subjectConfirmationData property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectConfirmationDataType }
     *     
     */
    public SubjectConfirmationDataType getSubjectConfirmationData() {
        return subjectConfirmationData;
    }

    /**
     * Sets the value of the subjectConfirmationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectConfirmationDataType }
     *     
     */
    public void setSubjectConfirmationData(SubjectConfirmationDataType value) {
        this.subjectConfirmationData = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

}
