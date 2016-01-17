
package com.mymobileapi.api5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Send_DS_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sendDSSTRResult"
})
@XmlRootElement(name = "Send_DS_STRResponse")
public class SendDSSTRResponse {

    @XmlElement(name = "Send_DS_STRResult")
    protected String sendDSSTRResult;

    /**
     * Gets the value of the sendDSSTRResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendDSSTRResult() {
        return sendDSSTRResult;
    }

    /**
     * Sets the value of the sendDSSTRResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendDSSTRResult(String value) {
        this.sendDSSTRResult = value;
    }

}
