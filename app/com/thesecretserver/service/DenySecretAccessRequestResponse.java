
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
     * Ruft den Wert der denySecretAccessRequestResult-Eigenschaft ab.
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
     * Legt den Wert der denySecretAccessRequestResult-Eigenschaft fest.
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
