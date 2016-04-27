
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VMwareDVSTeamingHealthCheckConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VMwareDVSTeamingHealthCheckConfig">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}VMwareDVSHealthCheckConfig">
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
@XmlType(name = "VMwareDVSTeamingHealthCheckConfig")
public class VMwareDVSTeamingHealthCheckConfig
    extends VMwareDVSHealthCheckConfig
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}
