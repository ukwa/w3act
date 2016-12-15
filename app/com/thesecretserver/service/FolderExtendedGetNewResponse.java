
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
 *         &lt;element name="FolderExtendedGetNewResult" type="{urn:thesecretserver.com}FolderExtendedGetNewResult" minOccurs="0"/>
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
    "folderExtendedGetNewResult"
})
@XmlRootElement(name = "FolderExtendedGetNewResponse")
public class FolderExtendedGetNewResponse {

    @XmlElement(name = "FolderExtendedGetNewResult")
    protected FolderExtendedGetNewResult folderExtendedGetNewResult;

    /**
     * Gets the value of the folderExtendedGetNewResult property.
     * 
     * @return
     *     possible object is
     *     {@link FolderExtendedGetNewResult }
     *     
     */
    public FolderExtendedGetNewResult getFolderExtendedGetNewResult() {
        return folderExtendedGetNewResult;
    }

    /**
     * Sets the value of the folderExtendedGetNewResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderExtendedGetNewResult }
     *     
     */
    public void setFolderExtendedGetNewResult(FolderExtendedGetNewResult value) {
        this.folderExtendedGetNewResult = value;
    }

}
