
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretPolicySummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretPolicySummary">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecretPolicyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretPolicyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SecretPolicyDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretPolicySummary", propOrder = {
    "secretPolicyId",
    "secretPolicyName",
    "secretPolicyDescription",
    "active"
})
@XmlSeeAlso({
    SecretPolicyDetail.class
})
public class SecretPolicySummary {

    @XmlElement(name = "SecretPolicyId")
    protected int secretPolicyId;
    @XmlElement(name = "SecretPolicyName")
    protected String secretPolicyName;
    @XmlElement(name = "SecretPolicyDescription")
    protected String secretPolicyDescription;
    @XmlElement(name = "Active")
    protected boolean active;

    /**
     * Gets the value of the secretPolicyId property.
     * 
     */
    public int getSecretPolicyId() {
        return secretPolicyId;
    }

    /**
     * Sets the value of the secretPolicyId property.
     * 
     */
    public void setSecretPolicyId(int value) {
        this.secretPolicyId = value;
    }

    /**
     * Gets the value of the secretPolicyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretPolicyName() {
        return secretPolicyName;
    }

    /**
     * Sets the value of the secretPolicyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretPolicyName(String value) {
        this.secretPolicyName = value;
    }

    /**
     * Gets the value of the secretPolicyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretPolicyDescription() {
        return secretPolicyDescription;
    }

    /**
     * Sets the value of the secretPolicyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretPolicyDescription(String value) {
        this.secretPolicyDescription = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

}
