
package guru.mmp.service.codes.ws;

import java.io.Serializable;
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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="out" type="{http://ws.codes.service.mmp.guru}CodeCategory"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "out"
})
@XmlRootElement(name = "GetCodeCategoryResponse")
public class GetCodeCategoryResponse implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true, nillable = true)
    protected CodeCategory out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link CodeCategory }
     *     
     */
    public CodeCategory getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeCategory }
     *     
     */
    public void setOut(CodeCategory value) {
        this.out = value;
    }

}
