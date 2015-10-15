
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
     * Ruft den Wert der downloadFileAttachmentResult-Eigenschaft ab.
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
     * Legt den Wert der downloadFileAttachmentResult-Eigenschaft fest.
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
