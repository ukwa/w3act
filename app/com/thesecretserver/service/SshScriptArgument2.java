
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SshScriptArgument2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SshScriptArgument2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SshType" type="{urn:thesecretserver.com}SshArgumentType2"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SshScriptArgument2", propOrder = {
    "name",
    "value",
    "sshType"
})
public class SshScriptArgument2 {

    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Value")
    protected String value;
    @XmlElement(name = "SshType", required = true)
    @XmlSchemaType(name = "string")
    protected SshArgumentType2 sshType;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the sshType property.
     * 
     * @return
     *     possible object is
     *     {@link SshArgumentType2 }
     *     
     */
    public SshArgumentType2 getSshType() {
        return sshType;
    }

    /**
     * Sets the value of the sshType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SshArgumentType2 }
     *     
     */
    public void setSshType(SshArgumentType2 value) {
        this.sshType = value;
    }

}
