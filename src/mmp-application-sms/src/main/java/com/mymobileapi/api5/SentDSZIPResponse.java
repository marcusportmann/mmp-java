
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
 *         &lt;element name="Sent_DS_ZIPResult" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
    "sentDSZIPResult"
})
@XmlRootElement(name = "Sent_DS_ZIPResponse")
public class SentDSZIPResponse {

    @XmlElement(name = "Sent_DS_ZIPResult")
    protected byte[] sentDSZIPResult;

    /**
     * Gets the value of the sentDSZIPResult property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSentDSZIPResult() {
        return sentDSZIPResult;
    }

    /**
     * Sets the value of the sentDSZIPResult property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSentDSZIPResult(byte[] value) {
        this.sentDSZIPResult = value;
    }

}
