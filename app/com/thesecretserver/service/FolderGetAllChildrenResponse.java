
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the folderGetAllChildrenResult property.
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
     * Sets the value of the folderGetAllChildrenResult property.
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
