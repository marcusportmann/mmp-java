
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;


/**
 * 
 *         Section 3.7: The AuthnStatement element is restricted to not
 *         contain the SessionIndex and SessionNotOnOrAfter attributes
 *         and to not contain the SubjectLocality element.  It MUST
 *         contain an AuthnInstant in UTC time.
 *       
 * 
 * <p>Java class for AuthnStatementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuthnStatementType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:assertion}StatementAbstractType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContext"/>
 *       &lt;/sequence>
 *       &lt;attribute name="AuthnInstant" use="required" type="{http://www.rsa.com/names/2010/04/std-prof/SAML2.0}UTCTime" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthnStatementType", propOrder = {
    "authnContext"
})
public class AuthnStatementType
    extends StatementAbstractType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "AuthnContext", required = true)
    protected AuthnContextType authnContext;
    @XmlAttribute(name = "AuthnInstant", required = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Calendar authnInstant;

    /**
     * Gets the value of the authnContext property.
     * 
     * @return
     *     possible object is
     *     {@link AuthnContextType }
     *     
     */
    public AuthnContextType getAuthnContext() {
        return authnContext;
    }

    /**
     * Sets the value of the authnContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthnContextType }
     *     
     */
    public void setAuthnContext(AuthnContextType value) {
        this.authnContext = value;
    }

    /**
     * Gets the value of the authnInstant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getAuthnInstant() {
        return authnInstant;
    }

    /**
     * Sets the value of the authnInstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthnInstant(Calendar value) {
        this.authnInstant = value;
    }

}
