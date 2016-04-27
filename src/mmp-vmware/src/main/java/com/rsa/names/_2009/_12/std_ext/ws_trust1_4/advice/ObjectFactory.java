
package com.rsa.names._2009._12.std_ext.ws_trust1_4.advice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.rsa.names._2009._12.std_ext.ws_trust1_4.advice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AttributeValue_QNAME = new QName("http://www.rsa.com/names/2009/12/std-ext/WS-Trust1.4/advice", "AttributeValue");
    private final static QName _AdviceSet_QNAME = new QName("http://www.rsa.com/names/2009/12/std-ext/WS-Trust1.4/advice", "AdviceSet");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.rsa.names._2009._12.std_ext.ws_trust1_4.advice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AdviceSetType }
     * 
     */
    public AdviceSetType createAdviceSetType() {
        return new AdviceSetType();
    }

    /**
     * Create an instance of {@link AdviceType }
     * 
     */
    public AdviceType createAdviceType() {
        return new AdviceType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.rsa.com/names/2009/12/std-ext/WS-Trust1.4/advice", name = "AttributeValue")
    public JAXBElement<Object> createAttributeValue(Object value) {
        return new JAXBElement<Object>(_AttributeValue_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdviceSetType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.rsa.com/names/2009/12/std-ext/WS-Trust1.4/advice", name = "AdviceSet")
    public JAXBElement<AdviceSetType> createAdviceSet(AdviceSetType value) {
        return new JAXBElement<AdviceSetType>(_AdviceSet_QNAME, AdviceSetType.class, null, value);
    }

}
