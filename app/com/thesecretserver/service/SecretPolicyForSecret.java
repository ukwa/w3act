
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretPolicyForSecret complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretPolicyForSecret">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretPolicyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Inherit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretPolicyForSecret", propOrder = {
    "secretId",
    "secretPolicyId",
    "inherit"
})
public class SecretPolicyForSecret {

    @XmlElement(name = "SecretId")
    protected int secretId;
    @XmlElement(name = "SecretPolicyId", required = true, type = Integer.class, nillable = true)
    protected Integer secretPolicyId;
    @XmlElement(name = "Inherit")
    protected boolean inherit;

    /**
     * Gets the value of the secretId property.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Sets the value of the secretId property.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Gets the value of the secretPolicyId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecretPolicyId() {
        return secretPolicyId;
    }

    /**
     * Sets the value of the secretPolicyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecretPolicyId(Integer value) {
        this.secretPolicyId = value;
    }

    /**
     * Gets the value of the inherit property.
     * 
     */
    public boolean isInherit() {
        return inherit;
    }

    /**
     * Sets the value of the inherit property.
     * 
     */
    public void setInherit(boolean value) {
        this.inherit = value;
    }

}
