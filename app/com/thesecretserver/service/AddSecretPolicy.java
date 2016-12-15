
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secretPolicy" type="{urn:thesecretserver.com}SecretPolicyDetail" minOccurs="0"/>
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
    "token",
    "secretPolicy"
})
@XmlRootElement(name = "AddSecretPolicy")
public class AddSecretPolicy {

    protected String token;
    protected SecretPolicyDetail secretPolicy;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the secretPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link SecretPolicyDetail }
     *     
     */
    public SecretPolicyDetail getSecretPolicy() {
        return secretPolicy;
    }

    /**
     * Sets the value of the secretPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretPolicyDetail }
     *     
     */
    public void setSecretPolicy(SecretPolicyDetail value) {
        this.secretPolicy = value;
    }

}
