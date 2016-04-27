
package oasis.names.tc.saml._2_0.assertion;

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
 * 
 *         Section 3.8: The Attribute element is restricted in the
 *         following ways:
 *         
 *         1) It may only contain AttributeValue elements;
 *         EncrytpedAttributeValue elements are not supported.
 * 
 *         2) It may not contain any attributes from other namespaces.
 * 
 *         3) The value of the NameFormat attribute MUST be
 *         urn:oasis:names:tc:SAML:2.0:attrname-format:uri
 * 
 *         4) The value of the type attribute of the AttributeValue
 *         element MUST be xsd:string
 * 
 *         5) Nil attribute values are not supported
 * 
 *         NOTE: This schema does not check that the values of
 *         FriendlyName are correct.  
 * 
 *         NOTE: When used in an attribute statement, the value of the
 *         Name attribute is restricted to be one of the values from
 *         Table 4 but there are no restrictions on the name when it is
 *         used in advice.  The profiled schema therefore imposes no
 *         restrictions on the name values.
 *       
 * 
 * <p>Java class for AttributeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttributeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AttributeValue" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NameFormat" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="FriendlyName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeType", propOrder = {
    "attributeValue"
})
public class AttributeType
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "AttributeValue")
    protected List<Object> attributeValue;
    @XmlAttribute(name = "Name", required = true)
    protected String name;
    @XmlAttribute(name = "NameFormat")
    @XmlSchemaType(name = "anyURI")
    protected String nameFormat;
    @XmlAttribute(name = "FriendlyName")
    protected String friendlyName;

    /**
     * Gets the value of the attributeValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAttributeValue() {
        if (attributeValue == null) {
            attributeValue = new ArrayList<Object>();
        }
        return this.attributeValue;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the nameFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameFormat() {
        return nameFormat;
    }

    /**
     * Sets the value of the nameFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameFormat(String value) {
        this.nameFormat = value;
    }

    /**
     * Gets the value of the friendlyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Sets the value of the friendlyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFriendlyName(String value) {
        this.friendlyName = value;
    }

}
