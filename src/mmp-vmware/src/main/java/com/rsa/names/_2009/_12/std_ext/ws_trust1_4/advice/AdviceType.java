
package com.rsa.names._2009._12.std_ext.ws_trust1_4.advice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdviceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdviceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Attribute" type="{http://www.rsa.com/names/2009/12/std-ext/WS-Trust1.4/advice}AttributeType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="AdviceSource" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdviceType", propOrder = {
    "attribute"
})
public class AdviceType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "Attribute", required = true)
    protected List<AttributeType> attribute;
    @XmlAttribute(name = "AdviceSource", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String adviceSource;

    /**
     * Gets the value of the attribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeType }
     * 
     * 
     */
    public List<AttributeType> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<AttributeType>();
        }
        return this.attribute;
    }

    /**
     * Gets the value of the adviceSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdviceSource() {
        return adviceSource;
    }

    /**
     * Sets the value of the adviceSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdviceSource(String value) {
        this.adviceSource = value;
    }

}
