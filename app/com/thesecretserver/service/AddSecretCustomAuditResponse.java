
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
 *         &lt;element name="AddSecretCustomAuditResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "addSecretCustomAuditResult"
})
@XmlRootElement(name = "AddSecretCustomAuditResponse")
public class AddSecretCustomAuditResponse {

    @XmlElement(name = "AddSecretCustomAuditResult")
    protected WebServiceResult addSecretCustomAuditResult;

    /**
     * Ruft den Wert der addSecretCustomAuditResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getAddSecretCustomAuditResult() {
        return addSecretCustomAuditResult;
    }

    /**
     * Legt den Wert der addSecretCustomAuditResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setAddSecretCustomAuditResult(WebServiceResult value) {
        this.addSecretCustomAuditResult = value;
    }

}
