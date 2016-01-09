package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>Java class for namedGroup complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="namedGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}realGroup">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.w3.org/2001/XMLSchema}annotation" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="all">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}all">
 *                   &lt;group ref="{http://www.w3.org/2001/XMLSchema}allModel"/>
 *                   &lt;anyAttribute processContents='lax' namespace='##other'/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="choice" type="{http://www.w3
 *           .org/2001/XMLSchema}simpleExplicitGroup"/>
 *           &lt;element name="sequence" type="{http://www.w3
 *           .org/2001/XMLSchema}simpleExplicitGroup"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "namedGroup")
public class NamedGroup
  extends RealGroup
  implements Serializable
{
  private final static long serialVersionUID = 1L;
}
