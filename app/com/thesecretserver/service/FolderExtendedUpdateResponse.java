
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
 *         &lt;element name="FolderExtendedUpdateResult" type="{urn:thesecretserver.com}FolderExtendedUpdateResult" minOccurs="0"/>
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
    "folderExtendedUpdateResult"
})
@XmlRootElement(name = "FolderExtendedUpdateResponse")
public class FolderExtendedUpdateResponse {

    @XmlElement(name = "FolderExtendedUpdateResult")
    protected FolderExtendedUpdateResult folderExtendedUpdateResult;

    /**
     * Gets the value of the folderExtendedUpdateResult property.
     * 
     * @return
     *     possible object is
     *     {@link FolderExtendedUpdateResult }
     *     
     */
    public FolderExtendedUpdateResult getFolderExtendedUpdateResult() {
        return folderExtendedUpdateResult;
    }

    /**
     * Sets the value of the folderExtendedUpdateResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderExtendedUpdateResult }
     *     
     */
    public void setFolderExtendedUpdateResult(FolderExtendedUpdateResult value) {
        this.folderExtendedUpdateResult = value;
    }

}
