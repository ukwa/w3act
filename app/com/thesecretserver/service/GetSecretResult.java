
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSecretResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSecretResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="SecretError" type="{urn:thesecretserver.com}SecretError" minOccurs="0"/>
 *         &lt;element name="Secret" type="{urn:thesecretserver.com}Secret" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSecretResult", propOrder = {
    "errors",
    "secretError",
    "secret"
})
public class GetSecretResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "SecretError")
    protected SecretError secretError;
    @XmlElement(name = "Secret")
    protected Secret secret;

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
     * Gets the value of the secretError property.
     * 
     * @return
     *     possible object is
     *     {@link SecretError }
     *     
     */
    public SecretError getSecretError() {
        return secretError;
    }

    /**
     * Sets the value of the secretError property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretError }
     *     
     */
    public void setSecretError(SecretError value) {
        this.secretError = value;
    }

    /**
     * Gets the value of the secret property.
     * 
     * @return
     *     possible object is
     *     {@link Secret }
     *     
     */
    public Secret getSecret() {
        return secret;
    }

    /**
     * Sets the value of the secret property.
     * 
     * @param value
     *     allowed object is
     *     {@link Secret }
     *     
     */
    public void setSecret(Secret value) {
        this.secret = value;
    }

}
