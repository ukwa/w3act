
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
 *         &lt;element name="UploadFileAttachmentByItemIdResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "uploadFileAttachmentByItemIdResult"
})
@XmlRootElement(name = "UploadFileAttachmentByItemIdResponse")
public class UploadFileAttachmentByItemIdResponse {

    @XmlElement(name = "UploadFileAttachmentByItemIdResult")
    protected WebServiceResult uploadFileAttachmentByItemIdResult;

    /**
     * Gets the value of the uploadFileAttachmentByItemIdResult property.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getUploadFileAttachmentByItemIdResult() {
        return uploadFileAttachmentByItemIdResult;
    }

    /**
     * Sets the value of the uploadFileAttachmentByItemIdResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setUploadFileAttachmentByItemIdResult(WebServiceResult value) {
        this.uploadFileAttachmentByItemIdResult = value;
    }

}
