
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
 *         &lt;element name="GetSecretAuditResult" type="{urn:thesecretserver.com}GetSecretAuditResult" minOccurs="0"/>
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
    "getSecretAuditResult"
})
@XmlRootElement(name = "GetSecretAuditResponse")
public class GetSecretAuditResponse {

    @XmlElement(name = "GetSecretAuditResult")
    protected GetSecretAuditResult getSecretAuditResult;

    /**
     * Gets the value of the getSecretAuditResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretAuditResult }
     *     
     */
    public GetSecretAuditResult getGetSecretAuditResult() {
        return getSecretAuditResult;
    }

    /**
     * Sets the value of the getSecretAuditResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretAuditResult }
     *     
     */
    public void setGetSecretAuditResult(GetSecretAuditResult value) {
        this.getSecretAuditResult = value;
    }

}
