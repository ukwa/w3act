
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FolderSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FolderSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IsChangeToSettings" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="InheritSecretPolicy" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SecretPolicyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FolderSettings", propOrder = {
    "isChangeToSettings",
    "inheritSecretPolicy",
    "secretPolicyId"
})
public class FolderSettings {

    @XmlElement(name = "IsChangeToSettings", required = true, type = Boolean.class, nillable = true)
    protected Boolean isChangeToSettings;
    @XmlElement(name = "InheritSecretPolicy", required = true, type = Boolean.class, nillable = true)
    protected Boolean inheritSecretPolicy;
    @XmlElement(name = "SecretPolicyId", required = true, type = Integer.class, nillable = true)
    protected Integer secretPolicyId;

    /**
     * Gets the value of the isChangeToSettings property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChangeToSettings() {
        return isChangeToSettings;
    }

    /**
     * Sets the value of the isChangeToSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChangeToSettings(Boolean value) {
        this.isChangeToSettings = value;
    }

    /**
     * Gets the value of the inheritSecretPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInheritSecretPolicy() {
        return inheritSecretPolicy;
    }

    /**
     * Sets the value of the inheritSecretPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInheritSecretPolicy(Boolean value) {
        this.inheritSecretPolicy = value;
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

}
