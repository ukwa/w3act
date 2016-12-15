
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SshArgumentType2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SshArgumentType2">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Interpreted"/>
 *     &lt;enumeration value="Literal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SshArgumentType2")
@XmlEnum
public enum SshArgumentType2 {

    @XmlEnumValue("Interpreted")
    INTERPRETED("Interpreted"),
    @XmlEnumValue("Literal")
    LITERAL("Literal");
    private final String value;

    SshArgumentType2(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SshArgumentType2 fromValue(String v) {
        for (SshArgumentType2 c: SshArgumentType2 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
