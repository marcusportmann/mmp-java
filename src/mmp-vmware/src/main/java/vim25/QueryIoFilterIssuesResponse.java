
package vim25;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="returnval" type="{urn:vim25}IoFilterQueryIssueResult"/>
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
    "returnval"
})
@XmlRootElement(name = "QueryIoFilterIssuesResponse")
public class QueryIoFilterIssuesResponse
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(required = true)
    protected IoFilterQueryIssueResult returnval;

    /**
     * Gets the value of the returnval property.
     * 
     * @return
     *     possible object is
     *     {@link IoFilterQueryIssueResult }
     *     
     */
    public IoFilterQueryIssueResult getReturnval() {
        return returnval;
    }

    /**
     * Sets the value of the returnval property.
     * 
     * @param value
     *     allowed object is
     *     {@link IoFilterQueryIssueResult }
     *     
     */
    public void setReturnval(IoFilterQueryIssueResult value) {
        this.returnval = value;
    }

}
