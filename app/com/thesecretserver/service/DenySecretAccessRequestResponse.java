
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
 *         &lt;element name="DenySecretAccessRequestResult" type="{urn:thesecretserver.com}RequestApprovalResult" minOccurs="0"/>
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
    "denySecretAccessRequestResult"
})
@XmlRootElement(name = "DenySecretAccessRequestResponse")
public class DenySecretAccessRequestResponse {

    @XmlElement(name = "DenySecretAccessRequestResult")
    protected RequestApprovalResult denySecretAccessRequestResult;

    /**
     * Gets the value of the denySecretAccessRequestResult property.
     * 
     * @return
     *     possible object is
     *     {@link RequestApprovalResult }
     *     
     */
    public RequestApprovalResult getDenySecretAccessRequestResult() {
        return denySecretAccessRequestResult;
    }

    /**
     * Sets the value of the denySecretAccessRequestResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestApprovalResult }
     *     
     */
    public void setDenySecretAccessRequestResult(RequestApprovalResult value) {
        this.denySecretAccessRequestResult = value;
    }

}
