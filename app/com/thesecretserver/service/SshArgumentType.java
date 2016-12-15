
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SshArgumentType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SshArgumentType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Interpreted"/>
 *     &lt;enumeration value="Literal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SshArgumentType")
@XmlEnum
public enum SshArgumentType {

    @XmlEnumValue("Interpreted")
    INTERPRETED("Interpreted"),
    @XmlEnumValue("Literal")
    LITERAL("Literal");
    private final String value;

    SshArgumentType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SshArgumentType fromValue(String v) {
        for (SshArgumentType c: SshArgumentType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
