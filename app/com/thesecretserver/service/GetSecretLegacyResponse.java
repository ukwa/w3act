
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetSecretLegacyResult" type="{urn:thesecretserver.com}GetSecretResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getSecretLegacyResult"
})
@XmlRootElement(name = "GetSecretLegacyResponse")
public class GetSecretLegacyResponse {

    @XmlElement(name = "GetSecretLegacyResult")
    protected GetSecretResult getSecretLegacyResult;

    /**
     * Gets the value of the getSecretLegacyResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretResult }
     *     
     */
    public GetSecretResult getGetSecretLegacyResult() {
        return getSecretLegacyResult;
    }

    /**
     * Sets the value of the getSecretLegacyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretResult }
     *     
     */
    public void setGetSecretLegacyResult(GetSecretResult value) {
        this.getSecretLegacyResult = value;
    }

}
