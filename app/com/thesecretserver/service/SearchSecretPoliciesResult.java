
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SearchSecretPoliciesResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchSecretPoliciesResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}WebServiceResult">
 *       &lt;sequence>
 *         &lt;element name="SecretPolicies" type="{urn:thesecretserver.com}ArrayOfSecretPolicySummary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchSecretPoliciesResult", propOrder = {
    "secretPolicies"
})
public class SearchSecretPoliciesResult
    extends WebServiceResult
{

    @XmlElement(name = "SecretPolicies")
    protected ArrayOfSecretPolicySummary secretPolicies;

    /**
     * Gets the value of the secretPolicies property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretPolicySummary }
     *     
     */
    public ArrayOfSecretPolicySummary getSecretPolicies() {
        return secretPolicies;
    }

    /**
     * Sets the value of the secretPolicies property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretPolicySummary }
     *     
     */
    public void setSecretPolicies(ArrayOfSecretPolicySummary value) {
        this.secretPolicies = value;
    }

}
