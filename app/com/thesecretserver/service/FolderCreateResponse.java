
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
 *         &lt;element name="FolderCreateResult" type="{urn:thesecretserver.com}CreateFolderResult" minOccurs="0"/>
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
    "folderCreateResult"
})
@XmlRootElement(name = "FolderCreateResponse")
public class FolderCreateResponse {

    @XmlElement(name = "FolderCreateResult")
    protected CreateFolderResult folderCreateResult;

    /**
     * Ruft den Wert der folderCreateResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CreateFolderResult }
     *     
     */
    public CreateFolderResult getFolderCreateResult() {
        return folderCreateResult;
    }

    /**
     * Legt den Wert der folderCreateResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateFolderResult }
     *     
     */
    public void setFolderCreateResult(CreateFolderResult value) {
        this.folderCreateResult = value;
    }

}
