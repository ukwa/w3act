
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
 *         &lt;element name="UpdateIsFavoriteResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "updateIsFavoriteResult"
})
@XmlRootElement(name = "UpdateIsFavoriteResponse")
public class UpdateIsFavoriteResponse {

    @XmlElement(name = "UpdateIsFavoriteResult")
    protected WebServiceResult updateIsFavoriteResult;

    /**
     * Ruft den Wert der updateIsFavoriteResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getUpdateIsFavoriteResult() {
        return updateIsFavoriteResult;
    }

    /**
     * Legt den Wert der updateIsFavoriteResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setUpdateIsFavoriteResult(WebServiceResult value) {
        this.updateIsFavoriteResult = value;
    }

}
