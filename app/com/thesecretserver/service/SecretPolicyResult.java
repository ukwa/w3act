
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretPolicyResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretPolicyResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}WebServiceResult">
 *       &lt;sequence>
 *         &lt;element name="SecretPolicy" type="{urn:thesecretserver.com}SecretPolicyDetail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretPolicyResult", propOrder = {
    "secretPolicy"
})
public class SecretPolicyResult
    extends WebServiceResult
{

    @XmlElement(name = "SecretPolicy")
    protected SecretPolicyDetail secretPolicy;

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
