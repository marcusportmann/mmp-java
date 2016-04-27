
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HostTargetTransport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HostTargetTransport">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}DynamicData">
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
@XmlType(name = "HostTargetTransport")
@XmlSeeAlso({
    HostParallelScsiTargetTransport.class,
    HostInternetScsiTargetTransport.class,
    HostBlockAdapterTargetTransport.class,
    HostFibreChannelTargetTransport.class
})
public class HostTargetTransport
    extends DynamicData
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}
