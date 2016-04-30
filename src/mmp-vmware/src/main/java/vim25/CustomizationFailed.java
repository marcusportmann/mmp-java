
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomizationFailed complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomizationFailed">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}CustomizationEvent">
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
@XmlType(name = "CustomizationFailed")
@XmlSeeAlso({
    CustomizationLinuxIdentityFailed.class,
    CustomizationNetworkSetupFailed.class,
    CustomizationUnknownFailure.class,
    CustomizationSysprepFailed.class
})
public class CustomizationFailed
    extends CustomizationEvent
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}