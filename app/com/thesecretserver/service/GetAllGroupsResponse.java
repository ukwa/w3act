
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
 *         &lt;element name="GetAllGroupsResult" type="{urn:thesecretserver.com}GetAllGroupsResult" minOccurs="0"/>
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
    "getAllGroupsResult"
})
@XmlRootElement(name = "GetAllGroupsResponse")
public class GetAllGroupsResponse {

    @XmlElement(name = "GetAllGroupsResult")
    protected GetAllGroupsResult getAllGroupsResult;

    /**
     * Ruft den Wert der getAllGroupsResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetAllGroupsResult }
     *     
     */
    public GetAllGroupsResult getGetAllGroupsResult() {
        return getAllGroupsResult;
    }

    /**
     * Legt den Wert der getAllGroupsResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAllGroupsResult }
     *     
     */
    public void setGetAllGroupsResult(GetAllGroupsResult value) {
        this.getAllGroupsResult = value;
    }

}
