
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FileDownloadResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileDownloadResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}WebServiceResult">
 *       &lt;sequence>
 *         &lt;element name="FileAttachment" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileDownloadResult", propOrder = {
    "fileAttachment",
    "fileName"
})
public class FileDownloadResult
    extends WebServiceResult
{

    @XmlElement(name = "FileAttachment")
    protected byte[] fileAttachment;
    @XmlElement(name = "FileName")
    protected String fileName;

    /**
     * Gets the value of the fileAttachment property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFileAttachment() {
        return fileAttachment;
    }

    /**
     * Sets the value of the fileAttachment property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFileAttachment(byte[] value) {
        this.fileAttachment = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

}
