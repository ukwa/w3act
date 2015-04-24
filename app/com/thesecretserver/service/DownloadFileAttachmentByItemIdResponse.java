
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
 *         &lt;element name="DownloadFileAttachmentByItemIdResult" type="{urn:thesecretserver.com}FileDownloadResult" minOccurs="0"/>
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
    "downloadFileAttachmentByItemIdResult"
})
@XmlRootElement(name = "DownloadFileAttachmentByItemIdResponse")
public class DownloadFileAttachmentByItemIdResponse {

    @XmlElement(name = "DownloadFileAttachmentByItemIdResult")
    protected FileDownloadResult downloadFileAttachmentByItemIdResult;

    /**
     * Gets the value of the downloadFileAttachmentByItemIdResult property.
     * 
     * @return
     *     possible object is
     *     {@link FileDownloadResult }
     *     
     */
    public FileDownloadResult getDownloadFileAttachmentByItemIdResult() {
        return downloadFileAttachmentByItemIdResult;
    }

    /**
     * Sets the value of the downloadFileAttachmentByItemIdResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileDownloadResult }
     *     
     */
    public void setDownloadFileAttachmentByItemIdResult(FileDownloadResult value) {
        this.downloadFileAttachmentByItemIdResult = value;
    }

}
