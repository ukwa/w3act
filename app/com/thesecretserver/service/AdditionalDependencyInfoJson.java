
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionalDependencyInfoJson complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionalDependencyInfoJson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Regex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PowershellArguments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalDependencyInfoJson", propOrder = {
    "regex",
    "powershellArguments"
})
public class AdditionalDependencyInfoJson {

    @XmlElement(name = "Regex")
    protected String regex;
    @XmlElement(name = "PowershellArguments")
    protected String powershellArguments;

    /**
     * Gets the value of the regex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Sets the value of the regex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegex(String value) {
        this.regex = value;
    }

    /**
     * Gets the value of the powershellArguments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowershellArguments() {
        return powershellArguments;
    }

    /**
     * Sets the value of the powershellArguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowershellArguments(String value) {
        this.powershellArguments = value;
    }

}
