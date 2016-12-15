
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
     * Gets the value of the folderGetResult property.
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
     * Sets the value of the folderGetResult property.
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
