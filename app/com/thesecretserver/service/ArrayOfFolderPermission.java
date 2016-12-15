
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfFolderPermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfFolderPermission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FolderPermission" type="{urn:thesecretserver.com}FolderPermission" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfFolderPermission", propOrder = {
    "folderPermission"
})
public class ArrayOfFolderPermission {

    @XmlElement(name = "FolderPermission", nillable = true)
    protected List<FolderPermission> folderPermission;

    /**
     * Gets the value of the folderPermission property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folderPermission property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolderPermission().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FolderPermission }
     * 
     * 
     */
    public List<FolderPermission> getFolderPermission() {
        if (folderPermission == null) {
            folderPermission = new ArrayList<FolderPermission>();
        }
        return this.folderPermission;
    }

}
