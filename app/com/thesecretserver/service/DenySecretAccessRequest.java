
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
 *         &lt;element name="approvalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userOverride" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "approvalId",
    "userOverride"
})
@XmlRootElement(name = "DenySecretAccessRequest")
public class DenySecretAccessRequest {

    protected String approvalId;
    protected boolean userOverride;

    /**
     * Gets the value of the approvalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprovalId() {
        return approvalId;
    }

    /**
     * Sets the value of the approvalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprovalId(String value) {
        this.approvalId = value;
    }

    /**
     * Gets the value of the userOverride property.
     * 
     */
    public boolean isUserOverride() {
        return userOverride;
    }

    /**
     * Sets the value of the userOverride property.
     * 
     */
    public void setUserOverride(boolean value) {
        this.userOverride = value;
    }

}
