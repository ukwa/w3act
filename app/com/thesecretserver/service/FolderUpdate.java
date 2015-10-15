
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modifiedFolder" type="{urn:thesecretserver.com}Folder" minOccurs="0"/>
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
    "token",
    "modifiedFolder"
})
@XmlRootElement(name = "FolderUpdate")
public class FolderUpdate {

    protected String token;
    protected Folder modifiedFolder;

    /**
     * Ruft den Wert der token-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Legt den Wert der token-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Ruft den Wert der modifiedFolder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Folder }
     *     
     */
    public Folder getModifiedFolder() {
        return modifiedFolder;
    }

    /**
     * Legt den Wert der modifiedFolder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Folder }
     *     
     */
    public void setModifiedFolder(Folder value) {
        this.modifiedFolder = value;
    }

}
