
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VsanIncompatibleDiskMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VsanIncompatibleDiskMapping">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}VsanDiskFault">
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
@XmlType(name = "VsanIncompatibleDiskMapping")
public class VsanIncompatibleDiskMapping
    extends VsanDiskFault
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}