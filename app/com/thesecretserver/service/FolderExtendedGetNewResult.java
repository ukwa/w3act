
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FolderExtendedGetNewResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FolderExtendedGetNewResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}FolderExtendedResultBase">
 *       &lt;sequence>
 *         &lt;element name="Folder" type="{urn:thesecretserver.com}FolderExtended" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FolderExtendedGetNewResult", propOrder = {
    "folder"
})
public class FolderExtendedGetNewResult
    extends FolderExtendedResultBase
{

    @XmlElement(name = "Folder")
    protected FolderExtended folder;

    /**
     * Gets the value of the folder property.
     * 
     * @return
     *     possible object is
     *     {@link FolderExtended }
     *     
     */
    public FolderExtended getFolder() {
        return folder;
    }

    /**
     * Sets the value of the folder property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderExtended }
     *     
     */
    public void setFolder(FolderExtended value) {
        this.folder = value;
    }

}
