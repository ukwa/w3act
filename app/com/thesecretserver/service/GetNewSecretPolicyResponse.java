
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
 *         &lt;element name="GetNewSecretPolicyResult" type="{urn:thesecretserver.com}SecretPolicyResult" minOccurs="0"/>
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
    "getNewSecretPolicyResult"
})
@XmlRootElement(name = "GetNewSecretPolicyResponse")
public class GetNewSecretPolicyResponse {

    @XmlElement(name = "GetNewSecretPolicyResult")
    protected SecretPolicyResult getNewSecretPolicyResult;

    /**
     * Gets the value of the getNewSecretPolicyResult property.
     * 
     * @return
     *     possible object is
     *     {@link SecretPolicyResult }
     *     
     */
    public SecretPolicyResult getGetNewSecretPolicyResult() {
        return getNewSecretPolicyResult;
    }

    /**
     * Sets the value of the getNewSecretPolicyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretPolicyResult }
     *     
     */
    public void setGetNewSecretPolicyResult(SecretPolicyResult value) {
        this.getNewSecretPolicyResult = value;
    }

}
