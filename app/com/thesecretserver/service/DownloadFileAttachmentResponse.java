
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
 *         &lt;element name="DownloadFileAttachmentResult" type="{urn:thesecretserver.com}FileDownloadResult" minOccurs="0"/>
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
    "downloadFileAttachmentResult"
})
@XmlRootElement(name = "DownloadFileAttachmentResponse")
public class DownloadFileAttachmentResponse {

    @XmlElement(name = "DownloadFileAttachmentResult")
    protected FileDownloadResult downloadFileAttachmentResult;

    /**
     * Gets the value of the downloadFileAttachmentResult property.
     * 
     * @return
     *     possible object is
     *     {@link FileDownloadResult }
     *     
     */
    public FileDownloadResult getDownloadFileAttachmentResult() {
        return downloadFileAttachmentResult;
    }

    /**
     * Sets the value of the downloadFileAttachmentResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileDownloadResult }
     *     
     */
    public void setDownloadFileAttachmentResult(FileDownloadResult value) {
        this.downloadFileAttachmentResult = value;
    }

}
