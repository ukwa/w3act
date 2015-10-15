
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
 *         &lt;element name="SearchSecretsByFolderLegacyResult" type="{urn:thesecretserver.com}SearchSecretsResult" minOccurs="0"/>
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
    "searchSecretsByFolderLegacyResult"
})
@XmlRootElement(name = "SearchSecretsByFolderLegacyResponse")
public class SearchSecretsByFolderLegacyResponse {

    @XmlElement(name = "SearchSecretsByFolderLegacyResult")
    protected SearchSecretsResult searchSecretsByFolderLegacyResult;

    /**
     * Ruft den Wert der searchSecretsByFolderLegacyResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SearchSecretsResult }
     *     
     */
    public SearchSecretsResult getSearchSecretsByFolderLegacyResult() {
        return searchSecretsByFolderLegacyResult;
    }

    /**
     * Legt den Wert der searchSecretsByFolderLegacyResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchSecretsResult }
     *     
     */
    public void setSearchSecretsByFolderLegacyResult(SearchSecretsResult value) {
        this.searchSecretsByFolderLegacyResult = value;
    }

}
