
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LineEnding.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LineEnding">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NewLine"/>
 *     &lt;enumeration value="CarriageReturn"/>
 *     &lt;enumeration value="CarriageReturnNewLine"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LineEnding")
@XmlEnum
public enum LineEnding {

    @XmlEnumValue("NewLine")
    NEW_LINE("NewLine"),
    @XmlEnumValue("CarriageReturn")
    CARRIAGE_RETURN("CarriageReturn"),
    @XmlEnumValue("CarriageReturnNewLine")
    CARRIAGE_RETURN_NEW_LINE("CarriageReturnNewLine");
    private final String value;

    LineEnding(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LineEnding fromValue(String v) {
        for (LineEnding c: LineEnding.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
