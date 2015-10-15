
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSecretTemplatesResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSecretTemplatesResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="SecretTemplates" type="{urn:thesecretserver.com}ArrayOfSecretTemplate" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSecretTemplatesResult", propOrder = {
    "errors",
    "secretTemplates"
})
public class GetSecretTemplatesResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "SecretTemplates")
    protected ArrayOfSecretTemplate secretTemplates;

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
     * Gets the value of the secretTemplates property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretTemplate }
     *     
     */
    public ArrayOfSecretTemplate getSecretTemplates() {
        return secretTemplates;
    }

    /**
     * Sets the value of the secretTemplates property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretTemplate }
     *     
     */
    public void setSecretTemplates(ArrayOfSecretTemplate value) {
        this.secretTemplates = value;
    }

}
