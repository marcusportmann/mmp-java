
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;
import org.w3c.dom.Element;


/**
 * 
 *         Section 3.4.2: The SubjectConfirmationDataType SHALL NOT
 *         contain the NotBefore, Recipient, InResponseTo or Address
 *         attributes. The NotOnOrAfter attribute SHALL be present if the
 *         confirmation type is bearer and MUST be UTC time.
 * 
 *         If the value of the Method attribute is
 *         urn:oasis:names:tc:SAML:2.0:cm:bearer then this attribute
 *         SHALL be present and SHALL contain a time that is not later
 *         than the NotOnOrAfter attribute of the Conditions element
 * 
 *         This element SHALL be present in all SubjectConfirmation
 *         elements but if the value of the Method attribute of the
 *         SubjectConfirmation is
 *         urn:oasis:names:tc:SAML:2.0:cm:holder-of-key then this element
 *         SHALL be of type saml:KeyInfoConfirmationDataType.  In this
 *         case, the element SHALL contain a single KeyInfo element with
 *         an X509Data element containing a certificate within an
 *         X509Certificate element.
 * 
 *         Note: This profile does not enforce the fact that the
 *         NotOnOrAfter attribute must be present if the method is
 *         bearer.
 *       
 * 
 * <p>Java class for SubjectConfirmationDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectConfirmationDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="NotOnOrAfter" type="{http://www.rsa.com/names/2010/04/std-prof/SAML2.0}UTCTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectConfirmationDataType", propOrder = {
    "content"
})
@XmlSeeAlso({
    KeyInfoConfirmationDataType.class
})
public class SubjectConfirmationDataType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlMixed
    @XmlAnyElement(lax = true)
    protected List<Object> content;
    @XmlAttribute(name = "NotOnOrAfter")
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Calendar notOnOrAfter;

    /**
     * 
     *         Section 3.4.2: The SubjectConfirmationDataType SHALL NOT
     *         contain the NotBefore, Recipient, InResponseTo or Address
     *         attributes. The NotOnOrAfter attribute SHALL be present if the
     *         confirmation type is bearer and MUST be UTC time.
     * 
     *         If the value of the Method attribute is
     *         urn:oasis:names:tc:SAML:2.0:cm:bearer then this attribute
     *         SHALL be present and SHALL contain a time that is not later
     *         than the NotOnOrAfter attribute of the Conditions element
     * 
     *         This element SHALL be present in all SubjectConfirmation
     *         elements but if the value of the Method attribute of the
     *         SubjectConfirmation is
     *         urn:oasis:names:tc:SAML:2.0:cm:holder-of-key then this element
     *         SHALL be of type saml:KeyInfoConfirmationDataType.  In this
     *         case, the element SHALL contain a single KeyInfo element with
     *         an X509Data element containing a certificate within an
     *         X509Certificate element.
     * 
     *         Note: This profile does not enforce the fact that the
     *         NotOnOrAfter attribute must be present if the method is
     *         bearer.
     *       Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link String }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
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
