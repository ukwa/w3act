
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ApproveSecretAccessRequestResult" type="{urn:thesecretserver.com}RequestApprovalResult" minOccurs="0"/>
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
    "approveSecretAccessRequestResult"
})
@XmlRootElement(name = "ApproveSecretAccessRequestResponse")
public class ApproveSecretAccessRequestResponse {

    @XmlElement(name = "ApproveSecretAccessRequestResult")
    protected RequestApprovalResult approveSecretAccessRequestResult;

    /**
     * Ruft den Wert der approveSecretAccessRequestResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RequestApprovalResult }
     *     
     */
    public RequestApprovalResult getApproveSecretAccessRequestResult() {
        return approveSecretAccessRequestResult;
    }

    /**
     * Legt den Wert der approveSecretAccessRequestResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestApprovalResult }
     *     
     */
    public void setApproveSecretAccessRequestResult(RequestApprovalResult value) {
        this.approveSecretAccessRequestResult = value;
    }

}
