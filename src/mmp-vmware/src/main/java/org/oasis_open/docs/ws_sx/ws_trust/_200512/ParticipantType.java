
package org.oasis_open.docs.ws_sx.ws_trust._200512;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.wsaddressing.W3CEndpointReference;


/**
 * 
 *         Modified definition to restrict the contents to an endpoint reference
 *         See section 4.2
 *       
 * 
 * <p>Java class for ParticipantType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParticipantType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.w3.org/2005/08/addressing}EndpointReference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParticipantType", propOrder = {
    "endpointReference"
})
public class ParticipantType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "EndpointReference", namespace = "http://www.w3.org/2005/08/addressing", required = true)
    protected W3CEndpointReference endpointReference;

    /**
     * Gets the value of the endpointReference property.
     * 
     * @return
     *     possible object is
     *     {@link W3CEndpointReference }
     *     
     */
    public W3CEndpointReference getEndpointReference() {
        return endpointReference;
    }

    /**
     * Sets the value of the endpointReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link W3CEndpointReference }
     *     
     */
    public void setEndpointReference(W3CEndpointReference value) {
        this.endpointReference = value;
    }

}
