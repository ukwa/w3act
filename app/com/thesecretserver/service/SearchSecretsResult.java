
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SearchSecretsResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchSecretsResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="SecretSummaries" type="{urn:thesecretserver.com}ArrayOfSecretSummary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchSecretsResult", propOrder = {
    "errors",
    "secretSummaries"
})
public class SearchSecretsResult {

    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "SecretSummaries")
    protected ArrayOfSecretSummary secretSummaries;

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
     * Gets the value of the secretSummaries property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretSummary }
     *     
     */
    public ArrayOfSecretSummary getSecretSummaries() {
        return secretSummaries;
    }

    /**
     * Sets the value of the secretSummaries property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretSummary }
     *     
     */
    public void setSecretSummaries(ArrayOfSecretSummary value) {
        this.secretSummaries = value;
    }

}
