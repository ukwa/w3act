
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
 *         &lt;element name="FolderExtendedCreateResult" type="{urn:thesecretserver.com}FolderExtendedCreateResult" minOccurs="0"/>
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
    "folderExtendedCreateResult"
})
@XmlRootElement(name = "FolderExtendedCreateResponse")
public class FolderExtendedCreateResponse {

    @XmlElement(name = "FolderExtendedCreateResult")
    protected FolderExtendedCreateResult folderExtendedCreateResult;

    /**
     * Gets the value of the folderExtendedCreateResult property.
     * 
     * @return
     *     possible object is
     *     {@link FolderExtendedCreateResult }
     *     
     */
    public FolderExtendedCreateResult getFolderExtendedCreateResult() {
        return folderExtendedCreateResult;
    }

    /**
     * Sets the value of the folderExtendedCreateResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderExtendedCreateResult }
     *     
     */
    public void setFolderExtendedCreateResult(FolderExtendedCreateResult value) {
        this.folderExtendedCreateResult = value;
    }

}
