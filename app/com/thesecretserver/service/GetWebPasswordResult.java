
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetWebPasswordResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetWebPasswordResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="WebPasswords" type="{urn:thesecretserver.com}ArrayOfWebPassword" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetWebPasswordResult", propOrder = {
    "errors",
    "webPasswords"
})
public class GetWebPasswordResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "WebPasswords")
    protected ArrayOfWebPassword webPasswords;

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
     * Gets the value of the webPasswords property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWebPassword }
     *     
     */
    public ArrayOfWebPassword getWebPasswords() {
        return webPasswords;
    }

    /**
     * Sets the value of the webPasswords property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWebPassword }
     *     
     */
    public void setWebPasswords(ArrayOfWebPassword value) {
        this.webPasswords = value;
    }

}
