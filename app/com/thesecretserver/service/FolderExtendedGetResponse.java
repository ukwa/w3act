
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
 *         &lt;element name="FolderExtendedGetResult" type="{urn:thesecretserver.com}FolderExtendedGetResult" minOccurs="0"/>
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
    "folderExtendedGetResult"
})
@XmlRootElement(name = "FolderExtendedGetResponse")
public class FolderExtendedGetResponse {

    @XmlElement(name = "FolderExtendedGetResult")
    protected FolderExtendedGetResult folderExtendedGetResult;

    /**
     * Gets the value of the folderExtendedGetResult property.
     * 
     * @return
     *     possible object is
     *     {@link FolderExtendedGetResult }
     *     
     */
    public FolderExtendedGetResult getFolderExtendedGetResult() {
        return folderExtendedGetResult;
    }

    /**
     * Sets the value of the folderExtendedGetResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderExtendedGetResult }
     *     
     */
    public void setFolderExtendedGetResult(FolderExtendedGetResult value) {
        this.folderExtendedGetResult = value;
    }

}
