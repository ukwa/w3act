
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TokenIsValidResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TokenIsValidResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="MaxOfflineSeconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TokenIsValidResult", propOrder = {
    "errors",
    "maxOfflineSeconds",
    "version"
})
public class TokenIsValidResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "MaxOfflineSeconds")
    protected int maxOfflineSeconds;
    @XmlElement(name = "Version")
    protected String version;

    /**
     * Gets the value of the errors property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getErrors() {
        return errors;
    }

    /**
     * Sets the value of the errors property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setErrors(ArrayOfString value) {
        this.errors = value;
    }

    /**
     * Gets the value of the maxOfflineSeconds property.
     * 
     */
    public int getMaxOfflineSeconds() {
        return maxOfflineSeconds;
    }

    /**
     * Sets the value of the maxOfflineSeconds property.
     * 
     */
    public void setMaxOfflineSeconds(int value) {
        this.maxOfflineSeconds = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
