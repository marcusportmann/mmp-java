
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         Section 3.7: The AuthnContext is restricted such that the
 *         AuthnContextClassRef element can only contain the values given
 *         in Table 3.
 *       
 * 
 * <p>Java class for AuthnContextType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuthnContextType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContextClassRef"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthnContextType", propOrder = {
    "authnContextClassRef"
})
public class AuthnContextType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "AuthnContextClassRef", required = true)
    protected String authnContextClassRef;

    /**
     * Gets the value of the authnContextClassRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthnContextClassRef() {
        return authnContextClassRef;
    }

    /**
     * Sets the value of the authnContextClassRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthnContextClassRef(String value) {
        this.authnContextClassRef = value;
    }

}
