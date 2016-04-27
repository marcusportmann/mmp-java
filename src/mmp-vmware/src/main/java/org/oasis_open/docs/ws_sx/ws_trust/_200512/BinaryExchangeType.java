
package org.oasis_open.docs.ws_sx.ws_trust._200512;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * 
 *         Modified definition to only support the SSPI exchange
 *         See section 3.5
 *       
 * 
 * <p>Java class for BinaryExchangeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BinaryExchangeType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="ValueType" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" fixed="http://schemas.xmlsoap.org/ws/2005/02/trust/spnego" />
 *       &lt;attribute name="EncodingType" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" fixed="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BinaryExchangeType", propOrder = {
    "value"
})
public class BinaryExchangeType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlValue
    protected String value;
    @XmlAttribute(name = "ValueType", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String valueType;
    @XmlAttribute(name = "EncodingType", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String encodingType;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the valueType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueType() {
        if (valueType == null) {
            return "http://schemas.xmlsoap.org/ws/2005/02/trust/spnego";
        } else {
            return valueType;
        }
    }

    /**
     * Sets the value of the valueType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueType(String value) {
        this.valueType = value;
    }

    /**
     * Gets the value of the encodingType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncodingType() {
        if (encodingType == null) {
            return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
        } else {
            return encodingType;
        }
    }

    /**
     * Sets the value of the encodingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncodingType(String value) {
        this.encodingType = value;
    }

}
