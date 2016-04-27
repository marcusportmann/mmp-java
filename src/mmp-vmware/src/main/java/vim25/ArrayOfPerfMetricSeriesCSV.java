
package vim25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfPerfMetricSeriesCSV complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfPerfMetricSeriesCSV">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PerfMetricSeriesCSV" type="{urn:vim25}PerfMetricSeriesCSV" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfPerfMetricSeriesCSV", propOrder = {
    "perfMetricSeriesCSV"
})
public class ArrayOfPerfMetricSeriesCSV
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "PerfMetricSeriesCSV")
    protected List<PerfMetricSeriesCSV> perfMetricSeriesCSV;

    /**
     * Gets the value of the perfMetricSeriesCSV property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the perfMetricSeriesCSV property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerfMetricSeriesCSV().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PerfMetricSeriesCSV }
     * 
     * 
     */
    public List<PerfMetricSeriesCSV> getPerfMetricSeriesCSV() {
        if (perfMetricSeriesCSV == null) {
            perfMetricSeriesCSV = new ArrayList<PerfMetricSeriesCSV>();
        }
        return this.perfMetricSeriesCSV;
    }

}
