
package vim25;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TooManyNativeClonesOnFile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TooManyNativeClonesOnFile">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:vim25}FileFault">
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
@XmlType(name = "TooManyNativeClonesOnFile")
public class TooManyNativeClonesOnFile
    extends FileFault
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;

}