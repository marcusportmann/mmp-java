
package guru.mmp.service.codes.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CodeDataType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CodeDataType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Standard"/&gt;
 *     &lt;enumeration value="Custom"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CodeDataType")
@XmlEnum
public enum CodeDataType {

    @XmlEnumValue("Standard")
    STANDARD("Standard"),
    @XmlEnumValue("Custom")
    CUSTOM("Custom");
    private final String value;

    CodeDataType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CodeDataType fromValue(String v) {
        for (CodeDataType c: CodeDataType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
