package guru.mmp.service.sample.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for SampleServiceFaultInfo complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="SampleServiceFaultInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Detail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string"
 *         minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SampleServiceFaultInfo", propOrder = {"detail", "message"})
public class SampleServiceFaultInfo
{

  @XmlElementRef(name = "Detail", namespace = "http://ws.sample.service.mmp.guru", type =
    JAXBElement.class)
  protected JAXBElement<String> detail;

  @XmlElementRef(name = "Message", namespace = "http://ws.sample.service.mmp.guru", type =
    JAXBElement.class)
  protected JAXBElement<String> message;

  /**
   * Gets the value of the detail property.
   *
   * @return possible object is
   * {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public JAXBElement<String> getDetail()
  {
    return detail;
  }

  /**
   * Gets the value of the message property.
   *
   * @return possible object is
   * {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public JAXBElement<String> getMessage()
  {
    return message;
  }

  /**
   * Sets the value of the detail property.
   *
   * @param value allowed object is
   *              {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public void setDetail(JAXBElement<String> value)
  {
    this.detail = value;
  }

  /**
   * Sets the value of the message property.
   *
   * @param value allowed object is
   *              {@link JAXBElement }{@code <}{@link String }{@code >}
   */
  public void setMessage(JAXBElement<String> value)
  {
    this.message = value;
  }
}