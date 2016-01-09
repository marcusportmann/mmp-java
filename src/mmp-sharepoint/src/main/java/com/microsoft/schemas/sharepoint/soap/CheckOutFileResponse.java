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
 *         &lt;element name="CheckOutFileResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"checkOutFileResult"})
@XmlRootElement(name = "CheckOutFileResponse")
public class CheckOutFileResponse
{

  @XmlElement(name = "CheckOutFileResult")
  protected boolean checkOutFileResult;

  /**
   * Gets the value of the checkOutFileResult property.
   */
  public boolean isCheckOutFileResult()
  {
    return checkOutFileResult;
  }

  /**
   * Sets the value of the checkOutFileResult property.
   */
  public void setCheckOutFileResult(boolean value)
  {
    this.checkOutFileResult = value;
  }
}
