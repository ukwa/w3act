
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FolderExtended complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FolderExtended">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}Folder">
 *       &lt;sequence>
 *         &lt;element name="PermissionSettings" type="{urn:thesecretserver.com}FolderPermissions" minOccurs="0"/>
 *         &lt;element name="Settings" type="{urn:thesecretserver.com}FolderSettings" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FolderExtended", propOrder = {
    "permissionSettings",
    "settings"
})
public class FolderExtended
    extends Folder
{

    @XmlElement(name = "PermissionSettings")
    protected FolderPermissions permissionSettings;
    @XmlElement(name = "Settings")
    protected FolderSettings settings;

    /**
     * Gets the value of the permissionSettings property.
     * 
     * @return
     *     possible object is
     *     {@link FolderPermissions }
     *     
     */
    public FolderPermissions getPermissionSettings() {
        return permissionSettings;
    }

    /**
     * Sets the value of the permissionSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderPermissions }
     *     
     */
    public void setPermissionSettings(FolderPermissions value) {
        this.permissionSettings = value;
    }

    /**
     * Gets the value of the settings property.
     * 
     * @return
     *     possible object is
     *     {@link FolderSettings }
     *     
     */
    public FolderSettings getSettings() {
        return settings;
    }

    /**
     * Sets the value of the settings property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderSettings }
     *     
     */
    public void setSettings(FolderSettings value) {
        this.settings = value;
    }

}
