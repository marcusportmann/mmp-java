
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvalidDeviceBacking complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvalidDeviceBacking">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}InvalidDeviceSpec">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvalidDeviceBacking")
public class InvalidDeviceBacking
    extends InvalidDeviceSpec
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}
