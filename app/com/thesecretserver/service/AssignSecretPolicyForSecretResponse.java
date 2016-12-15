
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
 *         &lt;element name="AssignSecretPolicyForSecretResult" type="{urn:thesecretserver.com}SecretPolicyForSecretResult" minOccurs="0"/>
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
    "assignSecretPolicyForSecretResult"
})
@XmlRootElement(name = "AssignSecretPolicyForSecretResponse")
public class AssignSecretPolicyForSecretResponse {

    @XmlElement(name = "AssignSecretPolicyForSecretResult")
    protected SecretPolicyForSecretResult assignSecretPolicyForSecretResult;

    /**
     * Gets the value of the assignSecretPolicyForSecretResult property.
     * 
     * @return
     *     possible object is
     *     {@link SecretPolicyForSecretResult }
     *     
     */
    public SecretPolicyForSecretResult getAssignSecretPolicyForSecretResult() {
        return assignSecretPolicyForSecretResult;
    }

    /**
     * Sets the value of the assignSecretPolicyForSecretResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretPolicyForSecretResult }
     *     
     */
    public void setAssignSecretPolicyForSecretResult(SecretPolicyForSecretResult value) {
        this.assignSecretPolicyForSecretResult = value;
    }

}
