
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DbType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DbType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AnsiString"/>
 *     &lt;enumeration value="Binary"/>
 *     &lt;enumeration value="Byte"/>
 *     &lt;enumeration value="Boolean"/>
 *     &lt;enumeration value="Currency"/>
 *     &lt;enumeration value="Date"/>
 *     &lt;enumeration value="DateTime"/>
 *     &lt;enumeration value="Decimal"/>
 *     &lt;enumeration value="Double"/>
 *     &lt;enumeration value="Guid"/>
 *     &lt;enumeration value="Int16"/>
 *     &lt;enumeration value="Int32"/>
 *     &lt;enumeration value="Int64"/>
 *     &lt;enumeration value="Object"/>
 *     &lt;enumeration value="SByte"/>
 *     &lt;enumeration value="Single"/>
 *     &lt;enumeration value="String"/>
 *     &lt;enumeration value="Time"/>
 *     &lt;enumeration value="UInt16"/>
 *     &lt;enumeration value="UInt32"/>
 *     &lt;enumeration value="UInt64"/>
 *     &lt;enumeration value="VarNumeric"/>
 *     &lt;enumeration value="AnsiStringFixedLength"/>
 *     &lt;enumeration value="StringFixedLength"/>
 *     &lt;enumeration value="Xml"/>
 *     &lt;enumeration value="DateTime2"/>
 *     &lt;enumeration value="DateTimeOffset"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DbType")
@XmlEnum
public enum DbType {

    @XmlEnumValue("AnsiString")
    ANSI_STRING("AnsiString"),
    @XmlEnumValue("Binary")
    BINARY("Binary"),
    @XmlEnumValue("Byte")
    BYTE("Byte"),
    @XmlEnumValue("Boolean")
    BOOLEAN("Boolean"),
    @XmlEnumValue("Currency")
    CURRENCY("Currency"),
    @XmlEnumValue("Date")
    DATE("Date"),
    @XmlEnumValue("DateTime")
    DATE_TIME("DateTime"),
    @XmlEnumValue("Decimal")
    DECIMAL("Decimal"),
    @XmlEnumValue("Double")
    DOUBLE("Double"),
    @XmlEnumValue("Guid")
    GUID("Guid"),
    @XmlEnumValue("Int16")
    INT_16("Int16"),
    @XmlEnumValue("Int32")
    INT_32("Int32"),
    @XmlEnumValue("Int64")
    INT_64("Int64"),
    @XmlEnumValue("Object")
    OBJECT("Object"),
    @XmlEnumValue("SByte")
    S_BYTE("SByte"),
    @XmlEnumValue("Single")
    SINGLE("Single"),
    @XmlEnumValue("String")
    STRING("String"),
    @XmlEnumValue("Time")
    TIME("Time"),
    @XmlEnumValue("UInt16")
    U_INT_16("UInt16"),
    @XmlEnumValue("UInt32")
    U_INT_32("UInt32"),
    @XmlEnumValue("UInt64")
    U_INT_64("UInt64"),
    @XmlEnumValue("VarNumeric")
    VAR_NUMERIC("VarNumeric"),
    @XmlEnumValue("AnsiStringFixedLength")
    ANSI_STRING_FIXED_LENGTH("AnsiStringFixedLength"),
    @XmlEnumValue("StringFixedLength")
    STRING_FIXED_LENGTH("StringFixedLength"),
    @XmlEnumValue("Xml")
    XML("Xml"),
    @XmlEnumValue("DateTime2")
    DATE_TIME_2("DateTime2"),
    @XmlEnumValue("DateTimeOffset")
    DATE_TIME_OFFSET("DateTimeOffset");
    private final String value;

    DbType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DbType fromValue(String v) {
        for (DbType c: DbType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
