
package com.rsa.names._2010._04.std_prof.saml2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AttributeNames.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AttributeNames">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="http://rsa.com/schemas/attr-names/2009/01/GroupIdentity"/>
 *     &lt;enumeration value="http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname"/>
 *     &lt;enumeration value="http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AttributeNames", namespace = "http://www.rsa.com/names/2010/04/std-prof/SAML2.0")
@XmlEnum
public enum AttributeNames {

    @XmlEnumValue("http://rsa.com/schemas/attr-names/2009/01/GroupIdentity")
    HTTP_RSA_COM_SCHEMAS_ATTR_NAMES_2009_01_GROUP_IDENTITY("http://rsa.com/schemas/attr-names/2009/01/GroupIdentity"),
    @XmlEnumValue("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname")
    HTTP_SCHEMAS_XMLSOAP_ORG_WS_2005_05_IDENTITY_CLAIMS_GIVENNAME("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname"),
    @XmlEnumValue("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname")
    HTTP_SCHEMAS_XMLSOAP_ORG_WS_2005_05_IDENTITY_CLAIMS_SURNAME("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname");
    private final String value;

    AttributeNames(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AttributeNames fromValue(String v) {
        for (AttributeNames c: AttributeNames.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
