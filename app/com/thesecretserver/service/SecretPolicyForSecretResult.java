
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretPolicyForSecretResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretPolicyForSecretResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}WebServiceResult">
 *       &lt;sequence>
 *         &lt;element name="SecretPolicyForSecret" type="{urn:thesecretserver.com}SecretPolicyForSecret" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretPolicyForSecretResult", propOrder = {
    "secretPolicyForSecret"
})
public class SecretPolicyForSecretResult
    extends WebServiceResult
{

    @XmlElement(name = "SecretPolicyForSecret")
    protected SecretPolicyForSecret secretPolicyForSecret;

    /**
     * Gets the value of the secretPolicyForSecret property.
     * 
     * @return
     *     possible object is
     *     {@link SecretPolicyForSecret }
     *     
     */
    public SecretPolicyForSecret getSecretPolicyForSecret() {
        return secretPolicyForSecret;
    }

    /**
     * Sets the value of the secretPolicyForSecret property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretPolicyForSecret }
     *     
     */
    public void setSecretPolicyForSecret(SecretPolicyForSecret value) {
        this.secretPolicyForSecret = value;
    }

}
