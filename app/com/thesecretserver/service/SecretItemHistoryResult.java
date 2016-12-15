
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretItemHistoryResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretItemHistoryResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SecretItemHistories" type="{urn:thesecretserver.com}ArrayOfSecretItemHistoryWebServiceResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretItemHistoryResult", propOrder = {
    "errors",
    "success",
    "secretItemHistories"
})
public class SecretItemHistoryResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "Success")
    protected boolean success;
    @XmlElement(name = "SecretItemHistories")
    protected ArrayOfSecretItemHistoryWebServiceResult secretItemHistories;

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
     * Gets the value of the success property.
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

    /**
     * Gets the value of the secretItemHistories property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretItemHistoryWebServiceResult }
     *     
     */
    public ArrayOfSecretItemHistoryWebServiceResult getSecretItemHistories() {
        return secretItemHistories;
    }

    /**
     * Sets the value of the secretItemHistories property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretItemHistoryWebServiceResult }
     *     
     */
    public void setSecretItemHistories(ArrayOfSecretItemHistoryWebServiceResult value) {
        this.secretItemHistories = value;
    }

}
