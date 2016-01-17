
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
 *         &lt;element name="ShortCode_Count_STR_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "shortCodeCountSTRSTRResult"
})
@XmlRootElement(name = "ShortCode_Count_STR_STRResponse")
public class ShortCodeCountSTRSTRResponse {

    @XmlElement(name = "ShortCode_Count_STR_STRResult")
    protected String shortCodeCountSTRSTRResult;

    /**
     * Gets the value of the shortCodeCountSTRSTRResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortCodeCountSTRSTRResult() {
        return shortCodeCountSTRSTRResult;
    }

    /**
     * Sets the value of the shortCodeCountSTRSTRResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortCodeCountSTRSTRResult(String value) {
        this.shortCodeCountSTRSTRResult = value;
    }

}
