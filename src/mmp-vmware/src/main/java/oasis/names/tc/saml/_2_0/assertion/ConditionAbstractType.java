
package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.rsa.names._2009._12.std_ext.saml2.RenewRestrictionType;
import oasis.names.tc.saml._2_0.conditions.delegation.DelegationRestrictionType;


/**
 * <p>Java class for ConditionAbstractType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConditionAbstractType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConditionAbstractType")
@XmlSeeAlso({
    ProxyRestrictionType.class,
    AudienceRestrictionType.class,
    RenewRestrictionType.class,
    DelegationRestrictionType.class
})
public abstract class ConditionAbstractType implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}
