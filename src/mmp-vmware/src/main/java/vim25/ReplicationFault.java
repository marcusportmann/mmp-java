
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReplicationFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReplicationFault">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}VimFault">
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
@XmlType(name = "ReplicationFault")
@XmlSeeAlso({
    ReplicationInvalidOptions.class,
    ReplicationNotSupportedOnHost.class,
    IncompatibleHostForVmReplication.class,
    ReplicationVmFault.class,
    ReplicationIncompatibleWithFT.class,
    ReplicationConfigFault.class
})
public class ReplicationFault
    extends VimFault
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}
