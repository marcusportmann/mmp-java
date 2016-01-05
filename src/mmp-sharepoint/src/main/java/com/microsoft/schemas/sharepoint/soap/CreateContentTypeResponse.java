package com.microsoft.schemas.sharepoint.soap;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CreateContentTypeResult" type="{http://www.w3
 *         .org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"createContentTypeResult"})
@XmlRootElement(name = "CreateContentTypeResponse")
public class CreateContentTypeResponse
{

  @XmlElement(name = "CreateContentTypeResult")
  protected String createContentTypeResult;

  /**
   * Gets the value of the createContentTypeResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getCreateContentTypeResult()
  {
    return createContentTypeResult;
  }

  /**
   * Sets the value of the createContentTypeResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setCreateContentTypeResult(String value)
  {
    this.createContentTypeResult = value;
  }
}
