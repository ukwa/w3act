
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
 *         &lt;element name="AddSecretPolicyResult" type="{urn:thesecretserver.com}SecretPolicyResult" minOccurs="0"/>
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
    "addSecretPolicyResult"
})
@XmlRootElement(name = "AddSecretPolicyResponse")
public class AddSecretPolicyResponse {

    @XmlElement(name = "AddSecretPolicyResult")
    protected SecretPolicyResult addSecretPolicyResult;

    /**
     * Gets the value of the addSecretPolicyResult property.
     * 
     * @return
     *     possible object is
     *     {@link SecretPolicyResult }
     *     
     */
    public SecretPolicyResult getAddSecretPolicyResult() {
        return addSecretPolicyResult;
    }

    /**
     * Sets the value of the addSecretPolicyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretPolicyResult }
     *     
     */
    public void setAddSecretPolicyResult(SecretPolicyResult value) {
        this.addSecretPolicyResult = value;
    }

}
