
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretPolicyDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretPolicyDetail">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}SecretPolicySummary">
 *       &lt;sequence>
 *         &lt;element name="SecretPolicyItems" type="{urn:thesecretserver.com}ArrayOfSecretPolicyItem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretPolicyDetail", propOrder = {
    "secretPolicyItems"
})
public class SecretPolicyDetail
    extends SecretPolicySummary
{

    @XmlElement(name = "SecretPolicyItems")
    protected ArrayOfSecretPolicyItem secretPolicyItems;

    /**
     * Gets the value of the secretPolicyItems property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretPolicyItem }
     *     
     */
    public ArrayOfSecretPolicyItem getSecretPolicyItems() {
        return secretPolicyItems;
    }

    /**
     * Sets the value of the secretPolicyItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretPolicyItem }
     *     
     */
    public void setSecretPolicyItems(ArrayOfSecretPolicyItem value) {
        this.secretPolicyItems = value;
    }

}
