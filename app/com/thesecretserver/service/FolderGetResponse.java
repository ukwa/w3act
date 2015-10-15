
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
 *         &lt;element name="FolderGetResult" type="{urn:thesecretserver.com}GetFolderResult" minOccurs="0"/>
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
    "folderGetResult"
})
@XmlRootElement(name = "FolderGetResponse")
public class FolderGetResponse {

    @XmlElement(name = "FolderGetResult")
    protected GetFolderResult folderGetResult;

    /**
     * Ruft den Wert der folderGetResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetFolderResult }
     *     
     */
    public GetFolderResult getFolderGetResult() {
        return folderGetResult;
    }

    /**
     * Legt den Wert der folderGetResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetFolderResult }
     *     
     */
    public void setFolderGetResult(GetFolderResult value) {
        this.folderGetResult = value;
    }

}
