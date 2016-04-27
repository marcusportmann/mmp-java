
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.rsa.names._2009._12.std_ext.saml2.RSAAdviceType;


/**
 * 
 *         Section 3.6: The Advice element is restricted to only contain
 *         an RSAAdvice element.  The AssertionIDRef, AssersionURIRef,
 *         Assertion and EncryptedAssertion elements are not supported.
 *       
 * 
 * <p>Java class for AdviceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdviceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element ref="{http://www.rsa.com/names/2009/12/std-ext/SAML2.0}RSAAdvice"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdviceType", propOrder = {
    "rsaAdvice"
})
public class AdviceType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "RSAAdvice", namespace = "http://www.rsa.com/names/2009/12/std-ext/SAML2.0", required = true)
    protected List<RSAAdviceType> rsaAdvice;

    /**
     * Gets the value of the rsaAdvice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rsaAdvice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRSAAdvice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RSAAdviceType }
     * 
     * 
     */
    public List<RSAAdviceType> getRSAAdvice() {
        if (rsaAdvice == null) {
            rsaAdvice = new ArrayList<RSAAdviceType>();
        }
        return this.rsaAdvice;
    }

}
