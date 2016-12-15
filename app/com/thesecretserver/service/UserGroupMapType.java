
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserGroupMapType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UserGroupMapType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="User"/>
 *     &lt;enumeration value="Group"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UserGroupMapType")
@XmlEnum
public enum UserGroupMapType {

    @XmlEnumValue("User")
    USER("User"),
    @XmlEnumValue("Group")
    GROUP("Group");
    private final String value;

    UserGroupMapType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UserGroupMapType fromValue(String v) {
        for (UserGroupMapType c: UserGroupMapType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
