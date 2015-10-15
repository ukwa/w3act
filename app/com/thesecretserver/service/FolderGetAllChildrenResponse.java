
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
 *         &lt;element name="FolderGetAllChildrenResult" type="{urn:thesecretserver.com}GetFoldersResult" minOccurs="0"/>
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
    "folderGetAllChildrenResult"
})
@XmlRootElement(name = "FolderGetAllChildrenResponse")
public class FolderGetAllChildrenResponse {

    @XmlElement(name = "FolderGetAllChildrenResult")
    protected GetFoldersResult folderGetAllChildrenResult;

    /**
     * Ruft den Wert der folderGetAllChildrenResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetFoldersResult }
     *     
     */
    public GetFoldersResult getFolderGetAllChildrenResult() {
        return folderGetAllChildrenResult;
    }

    /**
     * Legt den Wert der folderGetAllChildrenResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetFoldersResult }
     *     
     */
    public void setFolderGetAllChildrenResult(GetFoldersResult value) {
        this.folderGetAllChildrenResult = value;
    }

}
