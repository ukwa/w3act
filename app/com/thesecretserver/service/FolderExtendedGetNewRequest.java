
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FolderExtendedGetNewRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FolderExtendedGetNewRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FolderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ParentFolderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="InheritPermissions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FolderExtendedGetNewRequest", propOrder = {
    "folderName",
    "parentFolderId",
    "inheritPermissions"
})
public class FolderExtendedGetNewRequest {

    @XmlElement(name = "FolderName")
    protected String folderName;
    @XmlElement(name = "ParentFolderId", required = true, type = Integer.class, nillable = true)
    protected Integer parentFolderId;
    @XmlElement(name = "InheritPermissions", required = true, type = Boolean.class, nillable = true)
    protected Boolean inheritPermissions;

    /**
     * Gets the value of the folderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Sets the value of the folderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolderName(String value) {
        this.folderName = value;
    }

    /**
     * Gets the value of the parentFolderId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getParentFolderId() {
        return parentFolderId;
    }

    /**
     * Sets the value of the parentFolderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setParentFolderId(Integer value) {
        this.parentFolderId = value;
    }

    /**
     * Gets the value of the inheritPermissions property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInheritPermissions() {
        return inheritPermissions;
    }

    /**
     * Sets the value of the inheritPermissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInheritPermissions(Boolean value) {
        this.inheritPermissions = value;
    }

}
