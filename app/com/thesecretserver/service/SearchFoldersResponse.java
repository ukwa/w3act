
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
 *         &lt;element name="SearchFoldersResult" type="{urn:thesecretserver.com}SearchFolderResult" minOccurs="0"/>
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
    "searchFoldersResult"
})
@XmlRootElement(name = "SearchFoldersResponse")
public class SearchFoldersResponse {

    @XmlElement(name = "SearchFoldersResult")
    protected SearchFolderResult searchFoldersResult;

    /**
     * Ruft den Wert der searchFoldersResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SearchFolderResult }
     *     
     */
    public SearchFolderResult getSearchFoldersResult() {
        return searchFoldersResult;
    }

    /**
     * Legt den Wert der searchFoldersResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchFolderResult }
     *     
     */
    public void setSearchFoldersResult(SearchFolderResult value) {
        this.searchFoldersResult = value;
    }

}
