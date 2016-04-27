
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         Section 3.4: The Subject element SHALL contain a NameID and a
 *         SubjectConfirmation but SHALL NOT contain an EncryptedID.
 *       
 * 
 * <p>Java class for SubjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectConfirmation"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectType", propOrder = {
    "nameID",
    "subjectConfirmation"
})
public class SubjectType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "NameID", required = true)
    protected NameIDType nameID;
    @XmlElement(name = "SubjectConfirmation", required = true)
    protected SubjectConfirmationType subjectConfirmation;

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
     * Gets the value of the subjectConfirmation property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectConfirmationType }
     *     
     */
    public SubjectConfirmationType getSubjectConfirmation() {
        return subjectConfirmation;
    }

    /**
     * Sets the value of the subjectConfirmation property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectConfirmationType }
     *     
     */
    public void setSubjectConfirmation(SubjectConfirmationType value) {
        this.subjectConfirmation = value;
    }

}
